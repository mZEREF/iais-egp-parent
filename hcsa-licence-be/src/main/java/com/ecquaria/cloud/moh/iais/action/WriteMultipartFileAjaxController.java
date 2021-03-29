package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * @author junyu
 * @date 2021/03/11
 */
@Controller
@Slf4j
public class WriteMultipartFileAjaxController {


    //upload file
    @RequestMapping(value = "/uploadGiroFromFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public @ResponseBody String uploadInternalFile(HttpServletRequest request,@RequestParam("selectFile") MultipartFile selectedFile) throws Exception {
        String data = "";
        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"giroAcctFileDto");
        if(blastManagementDto != null){
            long size = selectedFile.getSize()/1024;
            String fileName = selectedFile.getOriginalFilename();
            if(fileName.indexOf('\\') > 0){
                fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
            }
            File toFile = FileUtils.multipartFileToFile(selectedFile);
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
            ParamUtil.setSessionAttr(request,"giroAcctFileDto",blastManagementDto);
            data = setHtmlValue(request,blastManagementDto.getAttachmentDtos());
        }
        return data;
    }

    @RequestMapping(value = "/deleteGiroFromFile", method = RequestMethod.POST)
    @ResponseBody
    public String deleteInternalFile(HttpServletRequest request){
        String data = "";
        String deleteWriteMessageFileId = request.getParameter("deleteWriteMessageFileId");
        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"giroAcctFileDto");
        if(blastManagementDto != null && !StringUtil.isEmpty(deleteWriteMessageFileId)){
            List<AttachmentDto> deletedFileList = getDeletedFileList(blastManagementDto.getAttachmentDtos(), deleteWriteMessageFileId);
            blastManagementDto.setAttachmentDtos(deletedFileList);
            ParamUtil.setSessionAttr(request,"giroAcctFileDto",blastManagementDto);
            data = setHtmlValue(request,deletedFileList);

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

    private String setHtmlValue(HttpServletRequest request,List<AttachmentDto> attachmentDtoList){
        StringBuilder data = new StringBuilder();
        int i=0;
        if(!IaisCommonUtils.isEmpty(attachmentDtoList)){
            for(AttachmentDto temp : attachmentDtoList){
                String CSRF = ParamUtil.getString(request,"OWASP_CSRFTOKEN");
                String urls="/hcsa-licence-web/download-giro-file?filerepo=fileRo"+i+"&OWASP_CSRFTOKEN=replaceCsrf"+"&fileRo"+i+"="+ MaskUtil.maskValue("fileRo"+i,temp.getId()) +"&fileRepoName="+temp.getDocName();
                String box = "<p class='fileList'>" +
                        "<a href=\""+urls.replace("replaceCsrf",CSRF)+"\">"+temp.getDocName()+"</a>" +
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
                i++;
            }
        }
        return data.toString();
    }

    @GetMapping(value = "/download-giro-file")
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);

        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"giroAcctFileDto");
        if(blastManagementDto != null&&blastManagementDto.getAttachmentDtos()!=null){
            for (AttachmentDto att:blastManagementDto.getAttachmentDtos()
            ) {
                if(att.getId().equals(fileRepoId)){
                    byte[] fileData =att.getData();
                    if(fileData != null){
                        try {
                            response.addHeader("Content-Disposition", "attachment;filename=\"" +  URLEncoder.encode(fileRepoName, StandardCharsets.UTF_8.toString())+"\"");
                            response.addHeader("Content-Length", "" + fileData.length);
                            response.setContentType("application/x-octet-stream");
                        }catch (Exception e){
                            log.error(e.getMessage(),e);
                        }
                        OutputStream ops = null;
                        try {
                            ops = new BufferedOutputStream(response.getOutputStream());
                        } catch (IOException e) {
                            log.error(e.getMessage(),e);
                        }
                        try {
                            ops.write(fileData);
                            ops.close();
                            ops.flush();
                        } catch (IOException e) {
                            log.error(e.getMessage(),e);
                        }
                    }
                    return;
                }
            }
        }
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }
}
