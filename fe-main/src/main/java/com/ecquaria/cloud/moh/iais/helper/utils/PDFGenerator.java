package com.ecquaria.cloud.moh.iais.helper.utils;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.google.common.base.Charsets;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.apache.commons.lang.StringUtils;

/**
 * @author yichen
 * @Date:2020/7/17
 */

@Slf4j
public class PDFGenerator {
	private static final String FILE_HTML = ".html";
	private Configuration cfg;

	public PDFGenerator(File templateDir) throws IOException {
		cfg = new Configuration(Configuration.VERSION_2_3_28);
		cfg.setDirectoryForTemplateLoading(templateDir);
		cfg.setDefaultEncoding(Charsets.UTF_8.name());
	}

	public void setFont(ITextRenderer renderer, String fontPath) throws IOException, DocumentException {
		setFont(renderer, fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	}

	private void setFont(ITextRenderer renderer, String fontPath, String encoding, boolean embedded) throws IOException, DocumentException {
		ITextFontResolver resolver = renderer.getFontResolver();
		resolver.addFont(fontPath, encoding, embedded);
	}

	public void generate(OutputStream os, String ftlName, Map<String, String> params) throws IOException, TemplateException,DocumentException {
		generate(os,ftlName,params,null);
	}

	public void generate(OutputStream os, String ftlName, Map<String, String> params,String templateDirPath) throws IOException, TemplateException,DocumentException {
		if (StringUtils.isEmpty(ftlName) || IaisCommonUtils.isEmpty(params)){
			log.debug("params is empty !!!");
			return;
		}

		String optHtmlName = System.currentTimeMillis() + FILE_HTML ;
		File optHtmlFile = new File(optHtmlName);
		if (!optHtmlFile.exists()){
			boolean flag = optHtmlFile.createNewFile();
			if (!flag) {
				log.debug("Creat File Error.");
			}
		}

		try (Writer out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(optHtmlFile.getPath())), Charsets.UTF_8.name()))){
			Template tp = cfg.getTemplate(ftlName);
			tp.process(params, out);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(optHtmlFile.getPath());
			if(!StringUtil.isEmpty(templateDirPath)){
				//templateDirPath = "file://"+templateDirPath+File.separator;
				templateDirPath="file:///usr/local/tomcat/webapps/main-web/";
				//log.info("The File.separator is -->:"+File.separator);
				log.info("The templateDirPath is -->:"+templateDirPath);
				renderer.getSharedContext().setBaseURL(templateDirPath);
			}
			renderer.layout();
			renderer.createPDF(os);
		}catch (TemplateNotFoundException | DocumentException e){
			throw e;
		}finally {
			IaisCommonUtils.deleteTempFile(optHtmlFile);
		}
	}
}
