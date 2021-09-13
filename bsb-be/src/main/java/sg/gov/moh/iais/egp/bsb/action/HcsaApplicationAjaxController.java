package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityDoc;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhilin
 * @date 2020/03/30
 */
@Controller
@Slf4j
public class HcsaApplicationAjaxController {

    private static final String FACILITY = "facility";

//    @Autowired
//    private FillUpCheckListGetAppClient uploadFileClient;

    @Autowired
    private FileRepoClient fileRepoClient;

    @Autowired
    private DocClient docClient;
//    @Autowired
//    InsepctionNcCheckListService insepctionNcCheckListService;
    //upload file
    @RequestMapping(value = "/uploadInternalFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public String uploadInternalFile(BaseProcessClass bpc,HttpServletRequest request, @RequestParam("selectedFile") MultipartFile selectedFile, @RequestParam("fileRemark")String remark) {
        String data = "";
        request.setAttribute("selectedFile", selectedFile);
        String CSRF = ParamUtil.getString(request, "OWASP_CSRFTOKEN");
//        HcsaApplicationUploadFileValidate uploadFileValidate = new HcsaApplicationUploadFileValidate();
//        Map<String, String> errorMap = uploadFileValidate.validate(request);
//        if(!errorMap.isEmpty()){
//            AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
//            appIntranetDocDto.setFileSn(-1);
//            appIntranetDocDto.setNoFilesMessage(errorMap.get("selectedFile"));
//            return  JsonUtil.parseToJson(appIntranetDocDto);
//        }else{
//        AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
        FacilityDoc facilityDoc = new FacilityDoc();
        FacilityDoc doc = new FacilityDoc();
        if (selectedFile != null && !StringUtil.isEmpty(selectedFile.getOriginalFilename())) {
            //size
            long size = selectedFile.getSize();
            facilityDoc.setSize(size / 1024);
            doc.setSize(size / 1024);
            log.info(StringUtil.changeForLog("HcsaApplicationAjaxController uploadInternalFile OriginalFilename ==== " + selectedFile.getOriginalFilename()));
            //type
            String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
//                String fileType = fileSplit[fileSplit.length - 1];
//                facilityDoc.setDocType(fileType);
            //name
            String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit);
            facilityDoc.setName(fileName);
            doc.setName(fileName);

            //status
//                appIntranetDocDto.setDocStatus(AppConsts.COMMON_STATUS_ACTIVE);
            //APP_PREM_CORRE_ID
//                TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request,"taskDto");
//                appIntranetDocDto.setAppPremCorrId(taskDto.getRefNo());
            //set audit
            doc.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            Facility facility=new Facility();
            Facility facility1 = (Facility) ParamUtil.getSessionAttr(request, FACILITY);
            facility.setId(facility1.getId());
            facilityDoc.setFacility(facility);
            doc.setFacility(facility);

            facilityDoc.setSubmitAt(new Date());
            doc.setSubmitAt(new Date());
            facilityDoc.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            doc.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
//                appIntranetDocDto.setSubmitDtString(Formatter.formatDateTime(appIntranetDocDto.getSubmitDt(), "dd/MM/yyyy HH:mm:ss"));
            doc.setSubmitByName(doc.getAuditTrailDto().getMohUserId());
//                if(StringUtil.isEmpty(remark)){
//                    appIntranetDocDto.setDocDesc(fileName);
//                }else {
//                    appIntranetDocDto.setDocDesc(remark);
//                }
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setFileName(fileName);
            fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fileRepoDto.setRelativePath(AppConsts.FALSE);

            //save file to file DB
            String repo_id = fileRepoClient.saveFiles(selectedFile, JsonUtil.parseToJson(fileRepoDto)).getEntity();
            facilityDoc.setFileRepoId(repo_id);
            doc.setFileRepoId(repo_id);
            //            appIntranetDocDto.set
//                appIntranetDocDto.setAppDocType(ApplicationConsts.APP_DOC_TYPE_COM);
            String id = docClient.saveFacilityDoc(facilityDoc).getEntity();
            facilityDoc.setId(id);
            doc.setId(id);


            // set appIntranetDocDto to seesion
//            ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
            AuditDocDto auditDocDto = (AuditDocDto)ParamUtil.getSessionAttr(request,"auditDocDto");
            if (auditDocDto==null){
                auditDocDto = new AuditDocDto();
            }
//            List<AppIntranetDocDto> appIntranetDocDtos;
            List<FacilityDoc> facilityDocs;
            if(auditDocDto != null && auditDocDto.getFacilityDocs() != null){
                facilityDocs = auditDocDto.getFacilityDocs();
            }else {
                facilityDocs = new ArrayList<>(5);
            }
            Integer index = (Integer) ParamUtil.getSessionAttr(request,"AppIntranetDocDtoIndex");
            int fileSizes = facilityDocs.size();
            if(index == null){
                index = fileSizes;
            }else {
                index++;
            }
            ParamUtil.setSessionAttr(request,"AppIntranetDocDtoIndex",index);
            String  mask =MaskUtil.maskValue("fileRo"+index, facilityDoc.getFileRepoId());
            String url ="<a href=\"pageContext.request.contextPath/file-repo?filerepo=fileRostatus.index&fileRostatus.index=maskDec&fileRepoName=interalFile.docName&OWASP_CSRFTOKEN=csrf\" title=\"Download\" class=\"downloadFile\">";
            try{
                String docName = selectedFile.getOriginalFilename() == null ? "" : URLEncoder.encode(selectedFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
                url= url.replaceAll("pageContext.request.contextPath","/hcsa-licence-web").replaceAll("status.index",String.valueOf(index)).
                        replaceAll("interalFile.docName", docName).replaceAll("maskDec",mask).replaceAll("csrf",CSRF);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
//            facilityDoc.setUrl(url);
            InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
            facilityDoc.setFileSn((serListDto != null && serListDto.getCopyAppPremisesSpecialDocDto()!= null) ? 999:fileSizes);
//            facilityDoc.setIsUpload(Boolean.TRUE);
            facilityDocs.add(doc);
            if (auditDocDto != null){
                auditDocDto.setFacilityDocs(facilityDocs);
                auditDocDto.setIsUpload(Boolean.TRUE);
            }
            ParamUtil.setSessionAttr(request,"docDto", facilityDoc);
            ParamUtil.setSessionAttr(request,"auditDocDto", auditDocDto);
            //call back upload file succeeded
            if( !StringUtil.isEmpty( facilityDoc.getId())){
//                facilityDoc.setMaskId(MaskUtil.maskValue("interalFileId", facilityDoc.getId()));
            }
            String appIntranetDocDtoJsonStr = JsonUtil.parseToJson(facilityDoc);
            data = appIntranetDocDtoJsonStr;
            ParamUtil.setRequestAttr(request, "doDocument", "Y");
        }
//    }
        return data;
    }

//    @RequestMapping(value = "/deleteInternalFile", method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String, Object> deleteInternalFile(HttpServletRequest request){
//        String guid = MaskUtil.unMaskValue("interalFileId", request.getParameter("appDocId"));
//        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
//        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
//        if(applicationViewDto != null && applicationViewDto.getAppIntranetDocDtoList() != null){
//            List<AppIntranetDocDto> appIntranetDocDtos = applicationViewDto.getAppIntranetDocDtoList();
//            AppIntranetDocDto appIntranetDocDe  = null;
//            for(AppIntranetDocDto appIntranetDocDto : appIntranetDocDtos){
//                if(appIntranetDocDto.getId().equalsIgnoreCase(guid)){
//                    uploadFileClient.deleteAppIntranetDocsById( appIntranetDocDto.getId());
//                    insepctionNcCheckListService.removeFiles(appIntranetDocDto.getId());
//                    appIntranetDocDe = appIntranetDocDto;
//                }
//            }
//            if(appIntranetDocDe!= null)
//            appIntranetDocDtos.remove( appIntranetDocDe);
//            boolean isUpload = false;
//            for(AppIntranetDocDto appIntranetDocDto : appIntranetDocDtos){
//                if(appIntranetDocDto.getIsUpload() != null && appIntranetDocDto.getIsUpload()){
//                    isUpload = true;
//                    break;
//                }
//            }
//            applicationViewDto.setIsUpload(isUpload);
//            ParamUtil.setSessionAttr(request,"applicationViewDto",(Serializable) applicationViewDto);
//            InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
//            map.put("fileSn", (serListDto != null && serListDto.getCopyAppPremisesSpecialDocDto()!= null) ? -1 : appIntranetDocDtos.size());
//            if(appIntranetDocDtos.size() == 0){
//                map.put("noFilesMessage", MessageUtil.getMessageDesc("GENERAL_ACK018"));
//            }
//        }
//        return map;
//    }


    @RequestMapping(value = "/verifyFileExist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> verifyFileExist(HttpServletRequest request){
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String reportId  = ParamUtil.getString(request,"repoId");
        if(StringUtil.isEmpty(reportId)){
            map.put("verify","N");
            return map;
        }
        byte[] data = fileRepoClient.getFileFormDataBase(reportId).getEntity();;
        if(data == null){
            map.put("verify","N");
            return map;
        }
        map.put("verify","Y");
        return map;
    }
}
