package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesUpdateEmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.EmailAttachmentDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @author junyu
 * @date 2022/07/22
 */
@Controller
@Slf4j
public class WriteEmailAttAjaxController {


    //upload file
    @RequestMapping(value = "/uploadWriteMessageFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public @ResponseBody String uploadInternalFile(HttpServletRequest request,@RequestParam("selectFile") MultipartFile selectedFile) throws Exception {
        String data = "";
        AppPremisesUpdateEmailDto emailDto = (AppPremisesUpdateEmailDto) ParamUtil.getSessionAttr(request,"appPremisesUpdateEmailDto");
        if(emailDto != null){
            String fileName = selectedFile.getOriginalFilename();
            if(fileName != null && fileName.indexOf('\\') > 0){
                fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
            }
            File toFile = FileUtils.multipartFileToFile(selectedFile, request.getSession().getId());
            byte[] fileToByteArray = FileUtils.readFileToByteArray(toFile);
            EmailAttachmentDto attachmentDto = new EmailAttachmentDto();
            attachmentDto.setContent(fileToByteArray);
            attachmentDto.setFileName(fileName);
            attachmentDto.setId(UUID.randomUUID().toString());
            if(IaisCommonUtils.isEmpty(emailDto.getAttachmentDtos())){
                List<EmailAttachmentDto> attachmentDtos = IaisCommonUtils.genNewArrayList();
                attachmentDtos.add(attachmentDto);
                emailDto.setAttachmentDtos(attachmentDtos);
            }else{
                emailDto.getAttachmentDtos().add(attachmentDto);
            }
            ParamUtil.setSessionAttr(request,"appPremisesUpdateEmailDto",emailDto);
            data = setHtmlValue(emailDto.getAttachmentDtos());
        }
        return data;
    }

    @RequestMapping(value = "/deleteWriteMessageFile", method = RequestMethod.POST)
    @ResponseBody
    public String deleteInternalFile(HttpServletRequest request){
        String data = "";
        String deleteWriteMessageFileId = request.getParameter("deleteWriteMessageFileId");
        AppPremisesUpdateEmailDto emailDto = (AppPremisesUpdateEmailDto) ParamUtil.getSessionAttr(request,"appPremisesUpdateEmailDto");
        if(emailDto != null && !StringUtil.isEmpty(deleteWriteMessageFileId)){
            List<EmailAttachmentDto> deletedFileList = getDeletedFileList(emailDto.getAttachmentDtos(), deleteWriteMessageFileId);
            emailDto.setAttachmentDtos(deletedFileList);
            ParamUtil.setSessionAttr(request,"appPremisesUpdateEmailDto",emailDto);
            data = setHtmlValue(deletedFileList);

        }
        return data;
    }

    private List<EmailAttachmentDto> getDeletedFileList(List<EmailAttachmentDto> attachmentDtoList, String deleteWriteMessageFileId){
        List<EmailAttachmentDto> result = null;
        if(!IaisCommonUtils.isEmpty(attachmentDtoList) && !StringUtil.isEmpty(deleteWriteMessageFileId)){
            result = IaisCommonUtils.genNewArrayList();
            for(EmailAttachmentDto attachmentDto : attachmentDtoList){
                if(!deleteWriteMessageFileId.equals(attachmentDto.getId())){
                    result.add(attachmentDto);
                }
            }
        }
        return result;
    }

    private String setHtmlValue(List<EmailAttachmentDto> attachmentDtoList){
        StringBuilder data = new StringBuilder();
        if(!IaisCommonUtils.isEmpty(attachmentDtoList)){
            for(EmailAttachmentDto temp : attachmentDtoList){
                String box = "<p class='fileList'>" +
                        temp.getFileName() +
                        "&emsp;<button name='fileDeleteButton' value='" +
                        temp.getFileName() +
                        "' " +
                        "type=\"button\" class=\"btn btn-secondary btn-sm\" onclick=\"writeMessageDeleteFile('" +
                        temp.getId() +
                        "')\">Delete</button></p>" +
                        "\n";
                data.append(box);
            }
        }
        return data.toString();
    }
}
