package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.utils.PDFGenerator;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import sop.webflow.rt.api.BaseProcessClass;

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
        byte[] buf = new byte[1024];
        List<File> pdfFileList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(ids)){
            int fileNum = 1;
            for (String licId:ids) {
                LicenceViewDto licenceViewDto = inboxService.getLicenceViewDtoByLicenceId(licId);
                //licenceViewDto.setLicSvcVehicleDtos(getTestData());
                File templateDir = ResourceUtils.getFile("classpath:pdfTemplate");
                log.info("=======templateDir.getPath()-->:{}", templateDir.getPath());
                PDFGenerator pdfGenerator = new PDFGenerator(templateDir);
                String fileName = "LICENCE" + fileNum ;
                File pdfFile = new File(fileName+".pdf");
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                String ftlName = null;
                map.put("licenceNo",StringUtil.viewNonNullHtml(licenceViewDto.getLicenceNo()));
                map.put("licenseeName",StringUtil.viewNonNullHtml(licenceViewDto.getLicenseeName()));
                map.put("serviceName",StringUtil.viewNonNullHtml(licenceViewDto.getServiceName()));
                map.put("businessName",StringUtil.viewNonNullHtml(licenceViewDto.getBusinessName()));
                map.put("premisesType",StringUtil.viewNonNullHtml(licenceViewDto.getPremiseTypeDisply()));
                map.put("lable",StringUtil.viewNonNullHtml(licenceViewDto.getLable()));
                List<String> contentList = licenceViewDto.getContent();
                List<String> eachPageList = IaisCommonUtils.genNewArrayList();
                for(int i = 0;i<contentList.size();i++){
                    if(i == 0){
                        map.put("content",contentList.get(i));
                    }else if (i > 0 && i < contentList.size() -1){
                        eachPageList.add(contentList.get(i));
                    }else {
                        map.put("content2",contentList.get(i));
                    }
                }
                if(IaisCommonUtils.isNotEmpty(eachPageList)){
                    map.put("lists",eachPageList);
                }
                map.put("startDate",licenceViewDto.getStartDate());
                map.put("endDate",licenceViewDto.getEndDate());
                if(contentList.size()==1){
                    ftlName = "p2_single_licence.ftl";
                }else{
                    ftlName = "multiple_licence.ftl";
                }
                try {
                    log.info(StringUtil.changeForLog("The ftlName is -->:"+ftlName));
                    OutputStream outputStream = java.nio.file.Files.newOutputStream(Paths.get(fileName+".pdf"));
                    pdfGenerator.generate(outputStream, ftlName, map);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
                pdfFileList.add(pdfFile);
                fileNum++;
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream in = null;
            try {
                ZipOutputStream out = new ZipOutputStream(bos);
                for (int i = 0; i < pdfFileList.size(); i++) {
                    // ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());
                    try {
                        in = java.nio.file.Files.newInputStream(pdfFileList.get(i).toPath());
//                    FileInputStream in = new FileInputStream(pdfFileList.get(i));
                        out.putNextEntry(new ZipEntry(pdfFileList.get(i).getName()));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                    } catch (Exception e) {
                        log.info(e.getMessage(),e);
                    } finally {
                        if (in != null){
                            in.close();
                        }
                    }
                }
                out.close();
//                bytes = FileUtils.readFileToByteArray(pdfFile);
                bpc.request.setAttribute("pdf", bos.toByteArray());
            } catch (Exception e) {
                log.info(e.getMessage(),e);
            }finally {
                if (in != null){
                    in.close();
                }
                bos.close();
            }
        }
    }
    private  List<LicSvcVehicleDto> getTestData(){
        List<LicSvcVehicleDto> licSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
        for(int i = 0 ;i<31;i++){
            LicSvcVehicleDto licSvcVehicleDto1 = new LicSvcVehicleDto();
            licSvcVehicleDto1.setVehicleNum("vehicleNo"+i);
            licSvcVehicleDtos.add(licSvcVehicleDto1);
        }

        return  licSvcVehicleDtos;
    }
}
