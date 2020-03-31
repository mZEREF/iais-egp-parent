package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhilin
 * @date 2020/03/30
 */
@Controller
@Slf4j
public class HcsaApplicationAjaxController {

    @Autowired
    private FillUpCheckListGetAppClient uploadFileClient;

    @Autowired
    FileRepoClient fileRepoClient;

    //upload file
    @RequestMapping(value = "/uploadInternalFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public @ResponseBody
    String uploadInternalFile(HttpServletRequest request,@RequestParam("selectedFile") MultipartFile selectedFile){
        String data = "";
        request.setAttribute("selectedFile",selectedFile);
        HcsaApplicationUploadFileValidate uploadFileValidate = new HcsaApplicationUploadFileValidate();
        Map<String, String> errorMap = uploadFileValidate.validate(request);
        if(!errorMap.isEmpty()){
            //String jsonStr = WebValidationHelper.generateJsonStr(errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, errorMap);
            //data = jsonStr;
        }else{
//            MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
//            CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) mulReq.getFile("internalFile");
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

            //call back upload file succeeded
            String appIntranetDocDtoJsonStr = JsonUtil.parseToJson(appIntranetDocDto);
            data = appIntranetDocDtoJsonStr;
            ParamUtil.setRequestAttr(request, "doDocument", "Y");
//            request.setAttribute("doDocument","Y");
        }
        return data;
    }
}
