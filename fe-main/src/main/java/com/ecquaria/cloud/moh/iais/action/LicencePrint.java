package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceViewPrintService;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Author weilu
 * @Date 2020/7/24 11:14
 */
@Delegator("licencePrint")
@Slf4j
public class LicencePrint {

   /* @Autowired
    private InboxService inboxService;*/

    @Autowired
    private LicenceViewPrintService licenceViewPrintService;

    public void action(BaseProcessClass bpc) throws IOException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        List<String> ids = (List<String>) ParamUtil.getSessionAttr(bpc.request, "lic-print-Ids");
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        bpc.request.setAttribute("pdf",licenceViewPrintService.printToPdf(ids));
       /* byte[] buf = new byte[1024];
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
                int totle = 0;
                map.put("licenceNo",StringUtil.viewNonNullHtml(licenceViewDto.getLicenceNo()));
                map.put("licenseeName",StringUtil.viewNonNullHtml(licenceViewDto.getLicenseeName()));
                map.put("serviceName",StringUtil.viewNonNullHtml(licenceViewDto.getServiceName()));
                map.put("businessName",StringUtil.viewNonNullHtml(licenceViewDto.getBusinessName()));
                map.put("premisesType",StringUtil.viewNonNullHtml(licenceViewDto.getPremiseTypeDisply()));
                map.put("lable",StringUtil.viewNonNullHtml(licenceViewDto.getLable()));
                List<String> contentList = licenceViewDto.getContent();
                List<String> eachPageList = IaisCommonUtils.genNewArrayList();
                *//*contentList.add("<p>test0</p>");
                contentList.add("<p>test1</p>");*//*
                totle = totle+ contentList.size();
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
                map.put("content2Page",totle);
                if(ApplicationConsts.PREMISES_TYPE_PERMANENT.equals(licenceViewDto.getPremisesType())){
                    map.put("canShowAddressNote",Boolean.TRUE);
                }
                map.put("startDate",licenceViewDto.getStartDate());
                map.put("endDate",licenceViewDto.getEndDate());
                List<String> disciplinesSpecifieds = licenceViewDto.getDisciplinesSpecifieds();
                *//*disciplinesSpecifieds.add("<li>test</li>");
                disciplinesSpecifieds.add("<li>test1</li>");*//*
                map.put("disciplinesSpecifiedsFirst","");
                if(disciplinesSpecifieds.size() >0){
                    totle = totle+ disciplinesSpecifieds.size();
                 map.put("needDisciplinesSpecifieds",Boolean.TRUE);
                 map.put("disciplinesSpecifiedsFirst",disciplinesSpecifieds.get(0));
                 if(disciplinesSpecifieds.size() >1){
                     disciplinesSpecifieds.remove(0);
                     map.put("disciplinesSpecifieds",disciplinesSpecifieds);
                 }
                }
                map.put("tody",Formatter.formatDateTime(new Date(),AppConsts.DATE_FORMAT_LICENCE));
                map.put("totle",totle);
                if(contentList.size()==1){
                    ftlName = "p2_single_licence.ftl";
                }else{
                    ftlName = "p2_multiple_licence.ftl";
                }
                try {
                    logMap(map);
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
        }*/
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

    private void logMap(Map<String, Object> map){
       log.info(StringUtil.changeForLog("The logMap start ..."));
       if(map != null){
           Set<Map.Entry<String, Object>> entries = map.entrySet();
           Iterator<Map.Entry<String, Object>> entryIterator = entries.iterator();
           while (entryIterator.hasNext()){
               if (entryIterator.next() != null){
                   log.info(StringUtil.changeForLog(entryIterator.next().getKey() + ":" + entryIterator.next().getValue()));
               }
           }
//           for(String key :map.keySet()){
//               log.info(StringUtil.changeForLog(key + ":" +map.get(key)));
//           }
       }
       log.info(StringUtil.changeForLog("The logMap end ..."));
    }
}
