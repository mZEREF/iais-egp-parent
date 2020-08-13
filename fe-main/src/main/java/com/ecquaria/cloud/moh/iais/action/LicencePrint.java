package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JarFileUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.utils.PDFGenerator;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @Author weilu
 * @Date 2020/7/24 11:14
 */
@Delegator("licencePrint")
@Slf4j
public class LicencePrint {

    @Autowired
    private InboxService inboxService;

    public void action(BaseProcessClass bpc) throws IOException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        List<String> ids = (List<String>) ParamUtil.getSessionAttr(bpc.request, "lic-print-Ids");
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        if(!IaisCommonUtils.isEmpty(ids)){
            for (String licId:ids) {
                byte[] bytes;
                LicenceViewDto licenceViewDto = inboxService.getLicenceViewDtoByLicenceId(licId);
                StringBuilder sb = new StringBuilder();
                sb.append("<div class=\"container\">\n" +
                        "    <div class=\"row\">\n" +
                        "      <div class=\"col-xs-12\">\n" +
                        "        <div class=\"instruction-content center-content\">\n" +
                        "          <h3>Licence detail</h3>\n" +
                        "          <!--startpoint-->\n" +
                        "          <div id = \"printDev\">\n" +
                        "            <p>This is a dummy licence with Licence Number ")
                        .append(licenceViewDto.getLicenceDto().getLicenceNo())
                        .append("</p>\n" +
                                "            <ul class=\"info-content\">\n" +
                                "              <li>\n" +
                                "                <p>Name of Licensee:")
                        .append(licenceViewDto.getLicenceDto().getLicenceNo() )
                        .append(licenceViewDto.getLicenseeDto().getName())
                        .append("</p>\n" +
                                "              </li>\n" +
                                "              <li>\n" +
                                "                <p>Service Licence: ")
                        .append(licenceViewDto.getLicenceDto().getLicenceNo() )
                        .append(licenceViewDto.getLicenseeDto().getName())
                        .append(licenceViewDto.getLicenceDto().getSvcName())
                        .append("</p>\n" +
                                "              </li>\n" +
                                "              <li>\n" +
                                "              <p>Licence Start and End Date:")
                        .append(licenceViewDto.getStartDate())
                        .append(" to ")
                        .append(licenceViewDto.getEndDate())
                        .append("</p>\n" +
                                "              </li>\n" +
                                "              <li>\n" +
                                "                <p>Licensed Premises:")
                        .append(licenceViewDto.getHciName())
                        .append("</p>\n" +
                                "              </li>\n" +
                                "            </ul>\n" +
                                "            <p>Premises Address:")
                        .append(licenceViewDto.getAddress())
                        .append("</p>\n" +
                                "          </div>\n" +
                                "        </div>\n" +
                                "      </div>\n" +
                                "    </div>\n" +
                                "  </div>");
                File templateDir = ResourceUtils.getFile("classpath:pdfTemplate");
                PDFGenerator pdfGenerator = new PDFGenerator(templateDir);
                File pdfFile = new File("LICENCE.pdf");
                Map<String, String> map = IaisCommonUtils.genNewHashMap();

                map.put("licence",sb.toString());
                OutputStream outputStream = Files.newOutputStream(Paths.get(pdfFile.getPath()));
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
}
