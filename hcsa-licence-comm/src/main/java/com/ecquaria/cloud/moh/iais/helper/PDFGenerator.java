package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.google.common.base.Charsets;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import static java.nio.file.Files.newOutputStream;

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
		if (StringUtils.isEmpty(ftlName) || IaisCommonUtils.isEmpty(params)){
			log.error("params is empty !!!");
			return;
		}

		String optHtmlName = System.currentTimeMillis() + FILE_HTML ;
		File optHtmlFile = MiscUtil.generateFile(optHtmlName);
		if (!optHtmlFile.exists()){
			boolean flag = optHtmlFile.createNewFile();
			if (!flag) {
				log.error("Creat File Error.");
			}
		}

		try (Writer out = new BufferedWriter(new OutputStreamWriter(newOutputStream(optHtmlFile.toPath()), Charsets.UTF_8.name()))){
			Template tp = cfg.getTemplate(ftlName);
			tp.process(params, out);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(optHtmlFile.getPath());
			renderer.layout();
			renderer.createPDF(os);
		}catch (TemplateNotFoundException | DocumentException e){
			throw e;
		}finally {
			IaisCommonUtils.deleteTempFile(optHtmlFile);
		}
	}
	public byte[] convertHtmlToPDF(String ftlName, Map<String, String> params) throws IOException, TemplateException{
		byte[] bytes = null;
		if (StringUtils.isEmpty(ftlName) || IaisCommonUtils.isEmpty(params)){
			log.error("params is empty !!!");
			return bytes;
		}

		String optHtmlName = System.currentTimeMillis() + FILE_HTML ;
		File optHtmlFile = MiscUtil.generateFile(optHtmlName);
		if (!optHtmlFile.exists()){
			boolean flag = optHtmlFile.createNewFile();
			if (!flag) {
				log.error("Creat File Error.");
			}
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (Writer out = new BufferedWriter(new OutputStreamWriter(newOutputStream(optHtmlFile.toPath()), Charsets.UTF_8.name()))){
			Template tp = cfg.getTemplate(ftlName);
			tp.process(params, out);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(optHtmlFile.getPath());
			renderer.layout();
			renderer.createPDF(baos);
		}catch (TemplateNotFoundException e){
			log.error(e.getMessage(),e);
		}catch ( DocumentException e){
			log.error(e.getMessage(),e);
		} finally{
			IaisCommonUtils.deleteTempFile(optHtmlFile);
		}
		bytes = baos.toByteArray();
		return bytes;
	}
}
