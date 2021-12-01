package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author junyu
 * @date 2021/03/11
 */
@Controller
@Slf4j
public class AdhocWriteMultipartFileAjaxController {

    @Autowired
    private SystemParamConfig systemParamConfig;
    //upload file
    @RequestMapping(value = "/uploadRfiFromFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public @ResponseBody String uploadInternalFile(HttpServletRequest request,@RequestParam("selectedFile") MultipartFile selectedFile,@RequestParam("uploadKey") String configIndex) throws Exception {
        String data = "";
        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"rfiFileDto"+configIndex);
        if(blastManagementDto != null){

            GiroAccountFormDocDto doc =new GiroAccountFormDocDto();
            long size = selectedFile.getSize() / 1024;
            doc.setDocName(selectedFile.getOriginalFilename());
            doc.setDocSize(Integer.valueOf(String.valueOf(size)));
            doc.setPassDocValidate(true);
            List<String> fileTypes = Arrays.asList(systemParamConfig.getUploadFileType().split(","));
            Long fileSize=(systemParamConfig.getUploadFileLimit() * 1024 *1024L);
            String errUploadFile="";
            if((doc.getDocSize()!=null)){
                Map<String, Boolean> map = IaisCommonUtils.genNewHashMap();
                if (doc.getDocSize() != null) {
                    String filename = doc.getDocName();
                    String fileType = filename.substring(filename.lastIndexOf(46) + 1);
                    String s = fileType.toUpperCase();
                    if (!fileTypes.contains(s)) {
                        map.put("fileType", Boolean.FALSE);
                    } else {
                        map.put("fileType", Boolean.TRUE);
                    }

                    if (size > fileSize) {
                        map.put("fileSize", Boolean.FALSE);
                    } else {
                        map.put("fileSize", Boolean.TRUE);
                    }
                    //size
                    if(!map.get("fileSize")){
                        doc.setPassDocValidate(false);
                        errUploadFile=MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(systemParamConfig.getUploadFileLimit()),"sizeMax");
                    }
                    //type
                    if(!map.get("fileType")){
                        doc.setPassDocValidate(false);
                        errUploadFile=MessageUtil.replaceMessage("GENERAL_ERR0018", systemParamConfig.getUploadFileType(),"fileType");
                    }
                    if(filename.length()>100){
                        doc.setPassDocValidate(false);
                        errUploadFile=MessageUtil.getMessageDesc("GENERAL_ERR0022");
                    }
                }
            }
            if(doc.isPassDocValidate()){
                String fileName = selectedFile.getOriginalFilename();
                if(fileName!=null&&fileName.indexOf('\\') > 0){
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
                ParamUtil.setSessionAttr(request,"rfiFileDto"+configIndex,blastManagementDto);
            }
            data = setHtmlValue(request,blastManagementDto.getAttachmentDtos(),errUploadFile,configIndex);
        }
        return data;
    }

    @RequestMapping(value = "/showRfiFromFile",  method = RequestMethod.POST)
    public @ResponseBody String showInternalFile(HttpServletRequest request) throws UnsupportedEncodingException {
        String data = "";
        String configIndex = request.getParameter("showIndex");

        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"rfiFileDto"+configIndex);
        if(blastManagementDto != null){
            String errUploadFile="";
            data = setHtmlValue(request,blastManagementDto.getAttachmentDtos(),errUploadFile,configIndex);
        }
        return data;
    }

    @RequestMapping(value = "/deleteGiroFromFile", method = RequestMethod.POST)
    @ResponseBody
    public String deleteInternalFile(HttpServletRequest request) throws UnsupportedEncodingException {
        String data = "";
        String deleteWriteMessageFileId = request.getParameter("deleteWriteMessageFileId");
        String configIndex = request.getParameter("configIndex");
        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"rfiFileDto"+configIndex);
        if(blastManagementDto != null && !StringUtil.isEmpty(deleteWriteMessageFileId)){
            List<AttachmentDto> deletedFileList = getDeletedFileList(blastManagementDto.getAttachmentDtos(), deleteWriteMessageFileId);
            blastManagementDto.setAttachmentDtos(deletedFileList);
            ParamUtil.setSessionAttr(request,"rfiFileDto"+configIndex,blastManagementDto);
            data = setHtmlValue(request,deletedFileList,"",configIndex);

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

    private String setHtmlValue(HttpServletRequest request,List<AttachmentDto> attachmentDtoList,String errUploadFile,String configIndex) throws UnsupportedEncodingException {
        StringBuilder data = new StringBuilder();
        int i=0;
        if(!IaisCommonUtils.isEmpty(attachmentDtoList)){
            for(AttachmentDto temp : attachmentDtoList){
                String CSRF = ParamUtil.getString(request,"OWASP_CSRFTOKEN");
                if(CSRF!=null){
                    ParamUtil.setSessionAttr(request,"replaceCsrf",CSRF);
                }else {
                    CSRF= (String) ParamUtil.getSessionAttr(request,"replaceCsrf");
                }
                String urls="/hcsa-licence-web/download-rfi-file?configIndex="+configIndex+"&filerepo=fileRo"+i+"&OWASP_CSRFTOKEN=replaceCsrf"+"&fileRo"+i+"="+ MaskUtil.maskValue("fileRo"+i,temp.getId()) +"&fileRepoName="+URLEncoder.encode(temp.getDocName(), StandardCharsets.UTF_8.toString());
                if(StringUtil.isEmpty(CSRF)){
                    urls="/hcsa-licence-web/download-rfi-file?configIndex="+configIndex+"&filerepo=fileRo"+i+"&fileRo"+i+"="+ MaskUtil.maskValue("fileRo"+i,temp.getId()) +"&fileRepoName="+URLEncoder.encode(temp.getDocName(), StandardCharsets.UTF_8.toString());
                }else {
                    urls=urls.replace("replaceCsrf",CSRF);
                }
                String box = "<p class='fileList'>" +
                        "<a href=\""+urls+"\">"+temp.getDocName()+"</a>" +
                        "&emsp;<button name='fileDeleteButton' value='" +
                        temp.getDocSize() +
                        "' " +
                        "type=\"button\" class=\"btn btn-secondary btn-sm\" onclick=\"writeMessageDeleteFile('" +
                        temp.getId() +
                        "','"+configIndex+"')\">Delete</button></p>" +
                        "<input hidden name='fileSize' value='" +
                        temp.getDocSize() +
                        "'/>\n";
                data.append(box);
                i++;
            }
        }
        if(!"".equals(errUploadFile)){
            data.append("<br>").append("<span name=\"iaisErrorMsg\" class=\"error-msg\"\n" +
                    "id=\"uploadFile\">").append(errUploadFile).append("</span>");
        }
        return data.toString();
    }

    @GetMapping(value = "/download-rfi-file")
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String configIndex = ParamUtil.getRequestString(request, "configIndex");

        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);

        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request,"rfiFileDto"+configIndex);
        if(blastManagementDto != null&&blastManagementDto.getAttachmentDtos()!=null){
            for (AttachmentDto att:blastManagementDto.getAttachmentDtos()
            ) {
                if(att.getId().equals(fileRepoId)){
                    byte[] fileData =att.getData();
                    if(fileData != null){
                        try {
                            response.addHeader("Content-Disposition", "attachment;filename=\"" +  fileRepoName+"\"");
                            response.addHeader("Content-Length", "" + fileData.length);
                            response.setContentType("application/x-octet-stream");
                        }catch (Exception e){
                            log.error(e.getMessage(),e);
                        }
                        OutputStream ops = null;
                        try {
                            ops = new BufferedOutputStream(response.getOutputStream());
                            ops.write(fileData);
                            ops.flush();
                        } catch (IOException e) {
                            log.error(e.getMessage(),e);
                        }finally {
                            if(ops != null){
                                ops.close();
                            }
                        }
                    }
                    return;
                }
            }
        }
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }
}
