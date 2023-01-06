package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.PDFGenerator;
import com.ecquaria.cloud.moh.iais.service.LicenceViewPrintService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

/**
 * LicenceViewServiceImpl
 *
 * @author suocheng
 * @date 12/27/2022
 */
@Service
@Slf4j
public class LicenceViewServicePrintImpl implements LicenceViewPrintService {

    @Autowired
    private HcsaLicenceCommonClient hcsaLicenceCommonClient;
    @Autowired
    private HcsaServiceClient hcsaServiceClient;



    @Override
    public LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId) {
        log.info(StringUtil.changeForLog("The getLicenceViewDtoByLicenceId start ..."));
        LicenceViewDto licenceViewDto = hcsaLicenceCommonClient.getAllStatusLicenceByLicenceId(licenceId).getEntity();

        //for service name
        List<LicPremisesScopeDto> licPremisesScopeDtos = licenceViewDto.getLicPremisesScopeDtos();
        String categorys = getCategorys(licPremisesScopeDtos);
        log.info(StringUtil.changeForLog("The getLicenceViewDtoByLicenceId categorys is -->:"+categorys));
        String serviceNmae = licenceViewDto.getLicenceDto().getSvcName();
        log.info(StringUtil.changeForLog("The getLicenceViewDtoByLicenceId serviceNmae is -->:"+serviceNmae));
        if(StringUtil.isNotEmpty(categorys)){
            serviceNmae = serviceNmae +" (" + categorys+")";
        }
        licenceViewDto.setServiceName(serviceNmae);
        //for Special service Header
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceClient.getHcsaSvcSpePremisesTypeDtos(licenceViewDto.getLicenceDto().getSvcName(),
                licenceViewDto.getLicenceDto().getServiceId()).getEntity();
        if(IaisCommonUtils.isNotEmpty(hcsaSvcSpePremisesTypeDtos)){
            licenceViewDto.setCategoryHeader(StringUtil.isEmpty(hcsaSvcSpePremisesTypeDtos.get(0).getSpecialSvcSecName())?
                    "disciplines/specified tests":hcsaSvcSpePremisesTypeDtos.get(0).getSpecialSvcSecName());
        }

        List<LicPremSubSvcRelDto> licPremSubSvcRelDtos = licenceViewDto.getLicPremSubSvcRelDtos();
        List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos = hcsaServiceClient.getHcsaSvcSpecifiedCorrelationDtos(
                licenceViewDto.getLicenceDto().getSvcName(),
                licenceViewDto.getLicenceDto().getServiceId(),
                licenceViewDto.getPremisesType()).getEntity();
        List<InnerLicenceViewData> innerLicenceViewDataList = tidyInnerLicenceViewData(null, licPremSubSvcRelDtos,hcsaSvcSpecifiedCorrelationDtos);
        List<String> disciplinesSpecifieds = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(innerLicenceViewDataList)) {
            StringBuilder str = new StringBuilder();
            int eachPage = 14;
            for (int i = 0; i < innerLicenceViewDataList.size(); i++) {
                int d = (i + 1) % eachPage;
                str.append("<p>("+(i+1)+") ").append(StringUtil.viewNonNullHtml(innerLicenceViewDataList.get(i).getValue()));
                List<String> innerLicenceViewDatas = innerLicenceViewDataList.get(i).getInnerLicenceViewDatas();
                if (IaisCommonUtils.isNotEmpty(innerLicenceViewDatas)) {
                    str.append("<br></br>");
                    for (int j = 0; j < innerLicenceViewDatas.size(); j++) {
                        str.append("&nbsp;&nbsp;&nbsp;- ").append(StringUtil.viewNonNullHtml(innerLicenceViewDatas.get(j)));
                        if (j != innerLicenceViewDatas.size() - 1) {
                            str.append("<br></br>");
                        }
                    }
                }
                if (d == 0) {
                    str.append("</p>");
                    disciplinesSpecifieds.add(str.toString());
                    str = new StringBuilder();
                } else if (i == innerLicenceViewDataList.size() - 1) {
                    str.append("</p>");
                    disciplinesSpecifieds.add(str.toString());
                } else {
                    str.append("</p>");
                }
            }
        }
        licenceViewDto.setDisciplinesSpecifieds(disciplinesSpecifieds);
        log.info(StringUtil.changeForLog("The getLicenceViewDtoByLicenceId end ..."));
        return licenceViewDto;
    }

    @Override
    public byte[] printToPdf(List<String> licenceIds) throws IOException {
        byte[] result = new byte[1024];
        byte[] buf = new byte[1024];
        List<File> pdfFileList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(licenceIds)){
            int fileNum = 1;
            for (String licId:licenceIds) {
                LicenceViewDto licenceViewDto = this.getLicenceViewDtoByLicenceId(licId);
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
                /*contentList.add("<p>test0</p>");
                contentList.add("<p>test1</p>");*/
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
                map.put("onlyLicenseeName",StringUtil.viewNonNullHtml(licenceViewDto.getOnlyLicenseeName()));
                List<String> disciplinesSpecifieds = licenceViewDto.getDisciplinesSpecifieds();
                /*disciplinesSpecifieds.add("<li>test</li>");
                disciplinesSpecifieds.add("<li>test1</li>");*/
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
                map.put("categoryHeader",StringUtil.viewNonNullHtml(licenceViewDto.getCategoryHeader()));// Special service Header
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
                result = bos.toByteArray();
                //bpc.request.setAttribute("pdf", bos.toByteArray());
            } catch (Exception e) {
                log.info(e.getMessage(),e);
            }finally {
                if (in != null){
                    in.close();
                }
                bos.close();
            }
        }
        return result;
    }

    @Override
    public void downloadLicencsToPdf(List<String> licenceIds, HttpServletResponse response) throws IOException {
        byte[] content = this.printToPdf(licenceIds);
        String fileName = "Licence.zip" ;
        response.addHeader("Content-Disposition", "attachment;filename="+fileName);
        response.addHeader("Content-Length", "" + content.length);
        //out.clear();
        response.setContentType("application/x-octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(content);
        ops.close();
        ops.flush();

    }

    private void logMap(Map<String, Object> map){
        log.info(StringUtil.changeForLog("The logMap start ..."));
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(map)));
        log.info(StringUtil.changeForLog("The logMap end ..."));
    }
    @Getter
    @Setter
    static class InnerLicenceViewData {

        String value;
        List<String> innerLicenceViewDatas;

    }

    private String getHcsaServiceSubTypeDisplayName(List<HcsaServiceSubTypeDto> hcsaServiceSubTypeDtos, String id) {
        String result = "";
        for (HcsaServiceSubTypeDto hcsaServiceSubTypeDto : hcsaServiceSubTypeDtos) {
            if (hcsaServiceSubTypeDto.getId().equals(id)) {
                result = hcsaServiceSubTypeDto.getSubtypeName();
                break;
            }
        }
        return result;
    }

    private String getHcsaServiceDtoDisplayName(List<HcsaServiceDto> hcsaServiceDtos, String code) {
        String result = "";
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
            if (hcsaServiceDto.getSvcCode().equals(code)) {
                result = hcsaServiceDto.getSvcName();
                break;
            }
        }
        return result;
    }

    private boolean isLever0(List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos, String svcCode){
        boolean result = false;
        if(IaisCommonUtils.isNotEmpty(hcsaSvcSpecifiedCorrelationDtos) && StringUtil.isNotEmpty(svcCode)){
            for(HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto : hcsaSvcSpecifiedCorrelationDtos){
                if(svcCode.equals(hcsaSvcSpecifiedCorrelationDto.getSpecifiedSvcId())){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private List<InnerLicenceViewData> tidyInnerLicenceViewData(List<LicPremisesScopeDto> licPremisesScopeDtos,
                                                                List<LicPremSubSvcRelDto> licPremSubSvcRelDtos,List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos ) {
        List<InnerLicenceViewData> result = IaisCommonUtils.genNewArrayList();
       /* if (IaisCommonUtils.isNotEmpty(licPremisesScopeDtos)) {
            List<String> ids = IaisCommonUtils.genNewArrayList();
            for (LicPremisesScopeDto licPremisesScopeDto : licPremisesScopeDtos) {
                ids.add(licPremisesScopeDto.getSubTypeId());
            }
            List<HcsaServiceSubTypeDto> hcsaServiceSubTypeDtos = hcsaServiceClient.getHcsaServiceSubTypeDtosByIds(ids).getEntity();
            for (LicPremisesScopeDto licPremisesScopeDto : licPremisesScopeDtos) {
                String subTypeDisplayName =  getHcsaServiceSubTypeDisplayName(hcsaServiceSubTypeDtos, licPremisesScopeDto.getSubTypeId());
                if(StringUtil.isNotEmpty(subTypeDisplayName)){
                    InnerLicenceViewData innerLicenceViewData = new InnerLicenceViewData();
                    innerLicenceViewData.setValue(subTypeDisplayName );
                    result.add(innerLicenceViewData);
                }
            }
        }*/
        if (IaisCommonUtils.isNotEmpty(licPremSubSvcRelDtos)) {
            List<String> svcCodes = IaisCommonUtils.genNewArrayList();
            for (LicPremSubSvcRelDto licPremSubSvcRelDto : licPremSubSvcRelDtos) {
                svcCodes.add(licPremSubSvcRelDto.getSvcCode());
            }
            List<HcsaServiceDto> hcsaServiceDtos = hcsaServiceClient.getHcsaServiceDtoByCode(svcCodes).getEntity();
            InnerLicenceViewData innerLicenceViewData;
            List<String> innerLicenceViewDataList = IaisCommonUtils.genNewArrayList();
            for (LicPremSubSvcRelDto licPremSubSvcRelDto : licPremSubSvcRelDtos) {
                if (isLever0(hcsaSvcSpecifiedCorrelationDtos,licPremSubSvcRelDto.getSvcCode())) {
                    innerLicenceViewData = new InnerLicenceViewData();
                    innerLicenceViewData.setValue(getHcsaServiceDtoDisplayName(hcsaServiceDtos, licPremSubSvcRelDto.getSvcCode()));
                    innerLicenceViewDataList = IaisCommonUtils.genNewArrayList();
                    innerLicenceViewData.setInnerLicenceViewDatas(innerLicenceViewDataList);
                    result.add(innerLicenceViewData);
                } else {
                    innerLicenceViewDataList.add(getHcsaServiceDtoDisplayName(hcsaServiceDtos, licPremSubSvcRelDto.getSvcCode()));
                }
            }
        }
        return result;
    }
    private String getCategorys(List<LicPremisesScopeDto> licPremisesScopeDtos){
        StringBuilder categorys = new StringBuilder();
        if (IaisCommonUtils.isNotEmpty(licPremisesScopeDtos)) {
            List<String> ids = IaisCommonUtils.genNewArrayList();
            for (LicPremisesScopeDto licPremisesScopeDto : licPremisesScopeDtos) {
                ids.add(licPremisesScopeDto.getSubTypeId());
            }
            List<HcsaServiceSubTypeDto> hcsaServiceSubTypeDtos = hcsaServiceClient.getHcsaServiceSubTypeDtosByIds(ids).getEntity();
            for (LicPremisesScopeDto licPremisesScopeDto : licPremisesScopeDtos) {
                String subTypeDisplayName =  getHcsaServiceSubTypeDisplayName(hcsaServiceSubTypeDtos, licPremisesScopeDto.getSubTypeId());
                if(StringUtil.isNotEmpty(subTypeDisplayName)){
                    if(StringUtil.isNotEmpty(categorys.toString())){
                        categorys.append(", ");
                    }
                    categorys.append(subTypeDisplayName);
                }
            }
        }
        return categorys.toString();
    }
}
