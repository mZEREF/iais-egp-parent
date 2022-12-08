package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationUploadFileValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@RestController
@Slf4j
public class HcsaApplicationAjaxController{

    @Autowired
    private FillUpCheckListGetAppClient uploadFileClient;

    @Autowired
    FileRepoClient fileRepoClient;

    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;

    @Autowired
    private ApplicationService applicationService;

    //upload file
    @RequestMapping(value = "/uploadInternalFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public String uploadInternalFile(HttpServletRequest request,@RequestParam("selectedFile") MultipartFile selectedFile,@RequestParam("fileRemark")String remark){
        String data = "";
        request.setAttribute("selectedFile",selectedFile);
        String CSRF = ParamUtil.getString(request,"OWASP_CSRFTOKEN");
        HcsaApplicationUploadFileValidate uploadFileValidate = new HcsaApplicationUploadFileValidate();
        Map<String, String> errorMap = uploadFileValidate.validate(request);
        if(!errorMap.isEmpty()){
            AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
            appIntranetDocDto.setFileSn(-1);
            appIntranetDocDto.setNoFilesMessage(errorMap.get("selectedFile"));
            return  JsonUtil.parseToJson(appIntranetDocDto);
        }else{
            AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
            if(selectedFile != null && !StringUtil.isEmpty(selectedFile.getOriginalFilename())) {
                //size
                long size = selectedFile.getSize();
                appIntranetDocDto.setDocSize(String.valueOf(size/1024));
                log.info(StringUtil.changeForLog("HcsaApplicationAjaxController uploadInternalFile OriginalFilename ==== " + selectedFile.getOriginalFilename()));
                //type
                String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
                String fileType = fileSplit[fileSplit.length - 1];
                appIntranetDocDto.setDocType(fileType);
                //name
                String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit);
                appIntranetDocDto.setDocName(fileName);

                //status
                appIntranetDocDto.setDocStatus(AppConsts.COMMON_STATUS_ACTIVE);
                //APP_PREM_CORRE_ID
                TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request,"taskDto");
                appIntranetDocDto.setAppPremCorrId(taskDto.getRefNo());
                //set audit
                appIntranetDocDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appIntranetDocDto.setSubmitDt(new Date());
                appIntranetDocDto.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
                appIntranetDocDto.setSubmitDtString(Formatter.formatDateTime(appIntranetDocDto.getSubmitDt(), "dd/MM/yyyy HH:mm:ss"));
                appIntranetDocDto.setSubmitByName(appIntranetDocDto.getAuditTrailDto().getMohUserId());
                if(StringUtil.isEmpty(remark)){
                    appIntranetDocDto.setDocDesc(fileName);
                }else {
                    appIntranetDocDto.setDocDesc(remark);
                }
                FileRepoDto fileRepoDto = new FileRepoDto();
                fileRepoDto.setFileName(fileName);
                fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                fileRepoDto.setRelativePath(AppConsts.FALSE);

                //save file to file DB
                String repo_id = fileRepoClient.saveFiles(selectedFile, JsonUtil.parseToJson(fileRepoDto)).getEntity();
                appIntranetDocDto.setFileRepoId(repo_id);
                //            appIntranetDocDto.set
                ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
                if(applicationViewDto.getApplicationDto().getStatus().equals(ApplicationConsts.APPLICATION_STATUS_ASO_EMAIL_PENDING)){
                    appIntranetDocDto.setAppDocType(ApplicationConsts.APP_DOC_TYPE_EMAIL_ATTACHMENT);
                }else {
                    appIntranetDocDto.setAppDocType(ApplicationConsts.APP_DOC_TYPE_COM);
                }
                String id = uploadFileClient.saveAppIntranetDocByAppIntranetDoc(appIntranetDocDto).getEntity();
                appIntranetDocDto.setId(id);
             // set appIntranetDocDto to seesion
            List<AppIntranetDocDto> appIntranetDocDtos;
            if(applicationViewDto.getAppIntranetDocDtoList() != null){
                appIntranetDocDtos = applicationViewDto.getAppIntranetDocDtoList();
            }else {
                appIntranetDocDtos = new ArrayList<>(5);
            }
            Integer index = (Integer) ParamUtil.getSessionAttr(request,"AppIntranetDocDtoIndex");
            int fileSizes = appIntranetDocDtos.size();
            if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationViewDto.getApplicationDto().getApplicationType())){
                fileSizes = (int) appIntranetDocDtos
                        .stream()
                        .filter(appIntranetDocDto1 -> !ApplicationConsts.APP_DOC_TYPE_SELF_DEC_FORM.equals(appIntranetDocDto1.getAppDocType()))
                        .count();
            }
            if(ApplicationConsts.APPLICATION_STATUS_ASO_EMAIL_PENDING.equals(applicationViewDto.getApplicationDto().getStatus())){
                fileSizes = (int) appIntranetDocDtos
                        .stream()
                        .filter(appIntranetDocDto1 -> ApplicationConsts.APP_DOC_TYPE_EMAIL_ATTACHMENT.equals(appIntranetDocDto1.getAppDocType()))
                        .count();
            }
            if(index == null){
                index = fileSizes;
            }else {
                index++;
            }
            ParamUtil.setSessionAttr(request,"AppIntranetDocDtoIndex",index);
            String  mask =MaskUtil.maskValue("fileRo"+index, appIntranetDocDto.getFileRepoId());
            String url ="<a href=\"pageContext.request.contextPath/file-repo?filerepo=fileRostatus.index&fileRostatus.index=maskDec&fileRepoName=interalFile.docName&OWASP_CSRFTOKEN=csrf\" title=\"Download\" class=\"downloadFile\">";
            try{
                String docName = selectedFile.getOriginalFilename() == null ? "" : URLEncoder.encode(selectedFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
                url= url.replaceAll("pageContext.request.contextPath","/hcsa-licence-web").replaceAll("status.index",String.valueOf(index)).
                       replaceAll("interalFile.docName", docName).replaceAll("maskDec",mask).replaceAll("csrf",StringUtil.getNonNull(CSRF));
            }catch (Exception e){
               log.error(e.getMessage(),e);
            }
            appIntranetDocDto.setUrl(url);
            InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
            appIntranetDocDto.setFileSn((serListDto != null && serListDto.getCopyAppPremisesSpecialDocDto()!= null) ? 999:fileSizes);
            appIntranetDocDto.setIsUpload(Boolean.TRUE);
            appIntranetDocDtos.add( appIntranetDocDto);
            if (applicationViewDto != null){
                applicationViewDto.setAppIntranetDocDtoList(appIntranetDocDtos);
                applicationViewDto.setIsUpload(Boolean.TRUE);
            }
            ParamUtil.setSessionAttr(request,"applicationViewDto",(Serializable) applicationViewDto);
            //call back upload file succeeded
            if( !StringUtil.isEmpty( appIntranetDocDto.getId())){
                appIntranetDocDto.setMaskId(MaskUtil.maskValue("interalFileId", appIntranetDocDto.getId()));
            }
            String appIntranetDocDtoJsonStr = JsonUtil.parseToJson(appIntranetDocDto);
            data = appIntranetDocDtoJsonStr;
            ParamUtil.setRequestAttr(request, "doDocument", "Y");
            }
        }
        return data;
    }

    @RequestMapping(value = "/deleteInternalFile", method = RequestMethod.POST)
    public Map<String, Object> deleteInternalFile(HttpServletRequest request){
        String guid = MaskUtil.unMaskValue("interalFileId", request.getParameter("appDocId"));
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        if(applicationViewDto != null && applicationViewDto.getAppIntranetDocDtoList() != null){
            List<AppIntranetDocDto> appIntranetDocDtos = applicationViewDto.getAppIntranetDocDtoList();
            AppIntranetDocDto appIntranetDocDe  = null;
            for(AppIntranetDocDto appIntranetDocDto : appIntranetDocDtos){
                if(appIntranetDocDto.getId().equalsIgnoreCase(guid)){
                    uploadFileClient.deleteAppIntranetDocsById( appIntranetDocDto.getId());
                    insepctionNcCheckListService.removeFiles(appIntranetDocDto.getId());
                    appIntranetDocDe = appIntranetDocDto;
                }
            }
            if(appIntranetDocDe!= null)
                appIntranetDocDtos.remove( appIntranetDocDe);
            boolean isUpload = false;
            for(AppIntranetDocDto appIntranetDocDto : appIntranetDocDtos){
                if(appIntranetDocDto.getIsUpload() != null && appIntranetDocDto.getIsUpload()){
                    isUpload = true;
                    break;
                }
            }
            applicationViewDto.setIsUpload(isUpload);
            ParamUtil.setSessionAttr(request,"applicationViewDto",(Serializable) applicationViewDto);
            InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
            int fileSizes = appIntranetDocDtos.size();
            if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationViewDto.getApplicationDto().getApplicationType())){
                fileSizes = (int) appIntranetDocDtos
                        .stream()
                        .filter(appIntranetDocDto1 -> !ApplicationConsts.APP_DOC_TYPE_SELF_DEC_FORM.equals(appIntranetDocDto1.getAppDocType()))
                        .count();
            }
            if(ApplicationConsts.APPLICATION_STATUS_ASO_EMAIL_PENDING.equals(applicationViewDto.getApplicationDto().getStatus())){
                fileSizes = (int) appIntranetDocDtos
                        .stream()
                        .filter(appIntranetDocDto1 -> ApplicationConsts.APP_DOC_TYPE_EMAIL_ATTACHMENT.equals(appIntranetDocDto1.getAppDocType()))
                        .count();
            }
            map.put("fileSn", (serListDto != null && serListDto.getCopyAppPremisesSpecialDocDto()!= null) ? -1 : fileSizes);
            if(fileSizes == 0){
                map.put("noFilesMessage", MessageUtil.getMessageDesc("GENERAL_ACK018"));
            }
        }
        return map;
    }


    @RequestMapping(value = "/verifyFileExist", method = RequestMethod.POST)
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

    @PostMapping("/canApproveValidation")
    public ResponseEntity<Map<String, String>> validateCanApprove(@RequestBody ApplicationViewDto applicationViewDto) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        applicationService.validateCanApprove(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL, applicationViewDto, errMap);

        return ResponseEntity.ok(errMap);
    }
}
