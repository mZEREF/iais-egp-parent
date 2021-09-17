package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
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
 * @author zhilin
 * @date 2020/03/30
 */
@Controller
@Slf4j
public class WriteMessageAjaxController {


    //upload file
    @RequestMapping(value = "/uploadWriteMessageFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public @ResponseBody String uploadInternalFile(HttpServletRequest request,@RequestParam("selectFile") MultipartFile selectedFile) throws Exception {
        String data = "";
        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"blastManagementDto");
        if(blastManagementDto != null){
            long size = selectedFile.getSize()/1024;
            String fileName = selectedFile.getOriginalFilename();
            if(fileName != null && fileName.indexOf('\\') > 0){
                fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
            }
            File toFile = FileUtils.multipartFileToFile(selectedFile, request.getSession().getId());
            byte[] fileToByteArray = FileUtils.readFileToByteArray(toFile);
            AttachmentDto attachmentDto = new AttachmentDto();
            attachmentDto.setData(fileToByteArray);
            attachmentDto.setDocName(fileName);
            attachmentDto.setDocSize(Long.toString(size));
            attachmentDto.setId(UUID.randomUUID().toString());
            if(IaisCommonUtils.isEmpty(blastManagementDto.getAttachmentDtos())){
                List<AttachmentDto> attachmentDtos = IaisCommonUtils.genNewArrayList();
                attachmentDtos.add(attachmentDto);
                blastManagementDto.setAttachmentDtos(attachmentDtos);
            }else{
                blastManagementDto.getAttachmentDtos().add(attachmentDto);
            }
            ParamUtil.setSessionAttr(request,"blastManagementDto",blastManagementDto);
            data = setHtmlValue(blastManagementDto.getAttachmentDtos());
        }
        return data;
    }

    @RequestMapping(value = "/deleteWriteMessageFile", method = RequestMethod.POST)
    @ResponseBody
    public String deleteInternalFile(HttpServletRequest request){
        String data = "";
        String deleteWriteMessageFileId = request.getParameter("deleteWriteMessageFileId");
        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"blastManagementDto");
        if(blastManagementDto != null && !StringUtil.isEmpty(deleteWriteMessageFileId)){
            List<AttachmentDto> deletedFileList = getDeletedFileList(blastManagementDto.getAttachmentDtos(), deleteWriteMessageFileId);
            blastManagementDto.setAttachmentDtos(deletedFileList);
            ParamUtil.setSessionAttr(request,"blastManagementDto",blastManagementDto);
            data = setHtmlValue(deletedFileList);

        }
        return data;
    }

    private List<AttachmentDto> getDeletedFileList(List<AttachmentDto> attachmentDtoList, String deleteWriteMessageFileId){
        List<AttachmentDto> result = null;
        if(!IaisCommonUtils.isEmpty(attachmentDtoList) && !StringUtil.isEmpty(deleteWriteMessageFileId)){
            result = IaisCommonUtils.genNewArrayList();
            for(AttachmentDto attachmentDto : attachmentDtoList){
                if(!deleteWriteMessageFileId.equals(attachmentDto.getId())){
                    result.add(attachmentDto);
                }
            }
        }
        return result;
    }

    private String setHtmlValue(List<AttachmentDto> attachmentDtoList){
        StringBuilder data = new StringBuilder();
        if(!IaisCommonUtils.isEmpty(attachmentDtoList)){
            for(AttachmentDto temp : attachmentDtoList){
                String box = "<p class='fileList'>" +
                        temp.getDocName() +
                        "&emsp;<button name='fileDeleteButton' value='" +
                        temp.getDocSize() +
                        "' " +
                        "type=\"button\" class=\"btn btn-secondary btn-sm\" onclick=\"writeMessageDeleteFile('" +
                        temp.getId() +
                        "')\">Delete</button></p>" +
                        "<input hidden name='fileSize' value='" +
                        temp.getDocSize() +
                        "'/>\n";
                data.append(box);
            }
        }
        return data.toString();
    }
}
