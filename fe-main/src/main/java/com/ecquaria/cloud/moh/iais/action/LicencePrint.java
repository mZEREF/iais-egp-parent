package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
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
                File templateDir = ResourceUtils.getFile("classpath:pdfTemplate");
                log.info("=======templateDir.getPath()-->:{}", templateDir.getPath());
                PDFGenerator pdfGenerator = new PDFGenerator(templateDir);
                String fileName = "LICENCE" + fileNum ;
                File pdfFile = new File(fileName+".pdf");
//                Map<String,String> VehicleNo = licenceViewDto.getVehicleNoMap();
//                String VehicleNo1 = VehicleNo.get("VehicleNo1");
//                String VehicleNo2 = VehicleNo.get("VehicleNo2");
                Map<String, String> map = IaisCommonUtils.genNewHashMap();
                map.put("licenceNo",licenceViewDto.getLicenceNo());
                map.put("licenseeName",licenceViewDto.getLicenseeName());
                map.put("serviceName",licenceViewDto.getServiceName());
                if(StringUtil.isEmpty(licenceViewDto.getHivTesting())){
                    map.put("hivTesting","<br/>");
                }else{
                    map.put("hivTesting",licenceViewDto.getHivTesting());
                }
                if(StringUtil.isNotEmpty(licenceViewDto.getBaseServiceName())){
                    map.put("baseServiceName",licenceViewDto.getBaseServiceName());
                }
                //map.put("hciName",licenceViewDto.getHciName());
                map.put("businessName",StringUtil.isEmpty(licenceViewDto.getBusinessName())?AppConsts.EMPTY_STR_NA:licenceViewDto.getBusinessName());
                map.put("address",licenceViewDto.getAddress());
               // map.put("vehicleNo",StringUtil.isEmpty(VehicleNo1)?AppConsts.EMPTY_STR_NA:VehicleNo1);
                map.put("vehicleNo",licenceViewDto.getVehicleNo());
//                if(StringUtil.isNotEmpty(VehicleNo2)){
//                    map.put("vehicleNo2",VehicleNo2);
//                }
                map.put("startDate",licenceViewDto.getStartDate());
                map.put("endDate",licenceViewDto.getEndDate());
                OutputStream outputStream = java.nio.file.Files.newOutputStream(Paths.get(fileName+".pdf"));
                try {
                    pdfGenerator.generate(outputStream, "licence.ftl", map);
                    //pdfGenerator.generate(outputStream, "single_licence.ftl", map);
//                    String ftlName = null;
//                    if(StringUtil.isNotEmpty(licenceViewDto.getBaseServiceName())){
//                       if(StringUtil.isEmpty(VehicleNo2)){
//                           ftlName = "sls_single_licence.ftl";
//                       }else{
//                           ftlName = "sls_multiple_licence.ftl";
//                       }
//                    }else{
//                        if(StringUtil.isEmpty(VehicleNo2)){
//                            ftlName = "single_licence.ftl";
//                        }else{
//                            ftlName = "multiple_licence.ftl";
//                        }
//                    }
//                    log.info(StringUtil.changeForLog("The ftlName is -->:"+ftlName));
//                    pdfGenerator.generate(outputStream, ftlName, map);
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
}
