package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.PDFGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import sop.webflow.rt.api.BaseProcessClass;

import static java.nio.file.Files.newOutputStream;

/**
 * @Author weilu
 * @Date 2020/7/24 11:14
 */
@Delegator("licencePrintFe")
@Slf4j
public class LicencePrint {
    public void action(BaseProcessClass bpc) throws IOException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        List<String> ids = (List<String>) ParamUtil.getRequestAttr(bpc.request, "licIds");
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        if(IaisCommonUtils.isEmpty(ids)){
            byte[] bytes;
            File templateDir = ResourceUtils.getFile("classpath:pdfTemplate");
            PDFGenerator pdfGenerator = new PDFGenerator(templateDir);
            StringBuilder sb = new StringBuilder();
            File pdfFile = new File("LICENCE.pdf");
            Map<String, String> map = IaisCommonUtils.genNewHashMap();
            map.put("licence","LICENCE");
            OutputStream outputStream = newOutputStream(Paths.get("LICENCE.pdf"));
            try {
                pdfGenerator.generate(outputStream, "licence.ftl", map);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            bytes = FileUtils.readFileToByteArray(pdfFile);
            bpc.request.setAttribute("pdf", bytes);
        }
    }
}
