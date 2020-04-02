package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationUploadFileValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * @author zhilin
 * @date 2020/03/30
 */
@Controller
@Slf4j
public class HcsaApplicationAjaxController{

    @Autowired
    private FillUpCheckListGetAppClient uploadFileClient;

    @Autowired
    FileRepoClient fileRepoClient;

    //upload file
    @RequestMapping(value = "/uploadInternalFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public String uploadInternalFile(HttpServletRequest request,@RequestParam("selectedFile") MultipartFile selectedFile){
        String data = "";
        request.setAttribute("selectedFile",selectedFile);
        String CSRF = ParamUtil.getString(request,"OWASP_CSRFTOKEN");
        HcsaApplicationUploadFileValidate uploadFileValidate = new HcsaApplicationUploadFileValidate();
        Map<String, String> errorMap = new HashMap<>();
        if(!errorMap.isEmpty()){
            AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
            appIntranetDocDto.setFileSn(-1);
            return  JsonUtil.parseToJson(appIntranetDocDto);
        }else{
            AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
            //size
            long size = selectedFile.getSize();
            appIntranetDocDto.setDocSize(String.valueOf(size/1024));
            //type
            String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
            String fileType = fileSplit[fileSplit.length - 1];
            appIntranetDocDto.setDocType(fileType);
            //name
            String fileName = fileSplit[0];
//            String fileName = UUID.randomUUID().toString();
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
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setFileName(fileName);
            fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fileRepoDto.setRelativePath(AppConsts.FALSE);

            //save file to file DB
            String repo_id = fileRepoClient.saveFiles(selectedFile, JsonUtil.parseToJson(fileRepoDto)).getEntity();
            appIntranetDocDto.setFileRepoId(repo_id);
//            appIntranetDocDto.set
            String id = uploadFileClient.saveAppIntranetDocByAppIntranetDoc(appIntranetDocDto).getEntity();
            appIntranetDocDto.setId(id);
             // set appIntranetDocDto to seesion
            ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
            List<AppIntranetDocDto> appIntranetDocDtos;
            if(applicationViewDto != null && applicationViewDto.getAppIntranetDocDtoList() != null){
                appIntranetDocDtos = applicationViewDto.getAppIntranetDocDtoList();
            }else {
                appIntranetDocDtos = new ArrayList<>(5);
            }
            Integer index = (Integer) ParamUtil.getSessionAttr(request,"AppIntranetDocDtoIndex");
            int fileSizes = appIntranetDocDtos.size();
            if(index == null){
                index = fileSizes;
            }else {
                index++;
            }
            ParamUtil.setSessionAttr(request,"AppIntranetDocDtoIndex",index);
           String  mask =MaskUtil.maskValue("fileRo"+index, appIntranetDocDto.getFileRepoId());
           String url ="<a href=\"pageContext.request.contextPath/file-repo?filerepo=fileRostatus.index&fileRostatus.index=maskDec&fileRepoName=interalFile.docName&OWASP_CSRFTOKEN=csrf\" title=\"Download\" class=\"downloadFile\">";
            url= url.replaceAll("pageContext.request.contextPath","/hcsa-licence-web").replaceAll("status.index",String.valueOf(index)).
                   replaceAll("interalFile.docName",appIntranetDocDto.getDocName()).replaceAll("maskDec",mask).replaceAll("csrf",CSRF);
            appIntranetDocDto.setUrl(url);
            appIntranetDocDto.setFileSn(fileSizes);
            appIntranetDocDtos.add( appIntranetDocDto);
            applicationViewDto.setAppIntranetDocDtoList(appIntranetDocDtos);
            ParamUtil.setSessionAttr(request,"applicationViewDto",(Serializable) applicationViewDto);
            //call back upload file succeeded
            String appIntranetDocDtoJsonStr = JsonUtil.parseToJson(appIntranetDocDto);
            data = appIntranetDocDtoJsonStr;
            ParamUtil.setRequestAttr(request, "doDocument", "Y");
//            request.setAttribute("doDocument","Y");
        }
        return data;
    }

    @RequestMapping(value = "/deleteInternalFile", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteInternalFile(HttpServletRequest request){
        String guid = MaskUtil.unMaskValue("appDocId", request.getParameter("appDocId"));
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        if(applicationViewDto != null && applicationViewDto.getAppIntranetDocDtoList() != null){
            List<AppIntranetDocDto> appIntranetDocDtos = applicationViewDto.getAppIntranetDocDtoList();
            AppIntranetDocDto appIntranetDocDe  = null;
            for(AppIntranetDocDto appIntranetDocDto : appIntranetDocDtos){
                if(appIntranetDocDto.getId().equalsIgnoreCase(guid)){
                    uploadFileClient.deleteAppIntranetDocsById( appIntranetDocDto.getId());
                    fileRepoClient.removeFileById(appIntranetDocDto.getFileRepoId());
                    appIntranetDocDe = appIntranetDocDto;
                }
            }
            if(appIntranetDocDe!= null)
            appIntranetDocDtos.remove( appIntranetDocDe);
            ParamUtil.setSessionAttr(request,"applicationViewDto",(Serializable) applicationViewDto);
            map.put("fileSn",appIntranetDocDtos.size());
            if(appIntranetDocDtos.size() == 0){
                map.put("noFilesMessage", MessageUtil.getMessageDesc("ACK018"));
            }
        }
        return map;
    }

}
