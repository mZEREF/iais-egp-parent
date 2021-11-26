package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.systeminfo.ServicesSysteminfo;
import com.ecquaria.sz.commons.util.JsonUtil;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wangyu
 * @date 2021/03/05
 */
@Controller
@Slf4j
public class HcsaFileAjaxController {

    @Autowired
    private SystemParamConfig systemParamConfig;

    public static final String SEESION_FILES_MAP_AJAX = "seesion_files_map_ajax_fe";
    public static final String SEESION_FILES_MAP_AJAX_MAX_INDEX = "_MaxIndex";
    public static final String GLOBAL_MAX_INDEX_SESSION_ATTR = "sessIon_GlObal__MaxINdex_Attr";

    @ResponseBody
    @PostMapping(value = "ajax-upload-file",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String ajaxUpload(HttpServletRequest request,@RequestParam("selectedFile") MultipartFile selectedFile,
                             @RequestParam("fileAppendId")String fileAppendId, @RequestParam("uploadFormId") String uploadFormId,
                             @RequestParam("reloadIndex") int reloadIndex,
                             @RequestParam(value = "needGlobalMaxIndex", required = false) boolean needMaxGlobal){
        log.info("-----------ajax-upload-file start------------");
        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId);
        int size = 0;
        if (needMaxGlobal && ParamUtil.getSessionAttr(request, GLOBAL_MAX_INDEX_SESSION_ATTR) != null) {
            size = (int) ParamUtil.getSessionAttr(request, GLOBAL_MAX_INDEX_SESSION_ATTR);
        }
        if (map == null) {
            map = IaisCommonUtils.genNewHashMap();
         } else if (size <= 0) {
            size = (Integer) ParamUtil.getSessionAttr(request,SEESION_FILES_MAP_AJAX
                    + fileAppendId + SEESION_FILES_MAP_AJAX_MAX_INDEX);
        }
        String errorMessage = getErrorMessage(selectedFile);
        MessageDto messageCode = new MessageDto();
         if(!StringUtil.isEmpty(errorMessage)){
             messageCode.setMsgType("N");
             messageCode.setDescription(errorMessage);
             return JsonUtil.toJson(messageCode);
         }else {
             messageCode.setMsgType("Y");
         }
         File toFile = null;
         String tempFolder = null;
         try{
             String toFileName = StringUtil.obscured(FilenameUtils.getName(selectedFile.getOriginalFilename()));
             if(reloadIndex == -1){
                 ParamUtil.setSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId+SEESION_FILES_MAP_AJAX_MAX_INDEX,size+1);
                 if (needMaxGlobal) {
                     ParamUtil.setSessionAttr(request, GLOBAL_MAX_INDEX_SESSION_ATTR, size + 1);
                 }
                 tempFolder = request.getSession().getId() + fileAppendId + size;
                 toFile = FileUtils.multipartFileToFile(selectedFile, tempFolder, toFileName);
                 map.put(fileAppendId + size, toFile);
             }else {
                 tempFolder = request.getSession().getId() + fileAppendId + reloadIndex;
                 toFile = FileUtils.multipartFileToFile(selectedFile, tempFolder, toFileName);
                 map.put(fileAppendId + reloadIndex, toFile);
                 size = reloadIndex;
             }

         }catch (Exception e){
             log.error(e.getMessage(),e);
             log.info("----------change MultipartFile to file  falie-----------------");
             return "";
         }
         // Save File to other nodes
        saveFileToOtherNodes(selectedFile, toFile, tempFolder);

        ParamUtil.setSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId,(Serializable)map);

        StringBuilder stringBuilder = new StringBuilder();
        String suffix = "Div"+size;
        String deleteButtonString = "      <button type=\"button\" class=\"btn btn-secondary btn-sm\"\n" +
                "                                                    onclick=\"javascript:deleteFileFeAjax('replaceForDelete',indexReplace);\">\n" +
                "                                                Delete</button>";
        String reUploadButtonString="  <button type=\"button\" class=\"btn btn-secondary btn-sm\"\n" +
                "                                                    onclick=\"javascript:reUploadFileFeAjax('replaceForUpload',indexReplace,'replaceForUploadForm');\">\n" +
                "                                               ReUpload</button>";
        if(selectedFile != null) {
            String originalFileName = selectedFile.getOriginalFilename();
            if(originalFileName != null) {
                String[] fileSplit = originalFileName.split("\\.");
                //name
                String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit) + "." + fileSplit[fileSplit.length - 1];
                String CSRF = ParamUtil.getString(request, "OWASP_CSRFTOKEN");
                String url = "<a href=\"pageContext.request.contextPath/download-session-file?fileAppendIdDown=replaceFileAppendIdDown&fileIndexDown=replaceFileIndexDown&OWASP_CSRFTOKEN=replaceCsrf\" title=\"Download\" class=\"downloadFile\">";
                fileName = url + fileName + "</a>";
                stringBuilder.append("<Div ").append(" id ='").append(fileAppendId).append(suffix).append("' >").
                        append(fileName.replace("pageContext.request.contextPath", "/hcsa-licence-web")
                                .replace("replaceFileAppendIdDown", fileAppendId)
                                .replace("replaceFileIndexDown", String.valueOf(size)).replace("replaceCsrf", StringUtil.getNonNull(CSRF)))
                        .append(' ').append(deleteButtonString.replace("replaceForDelete", fileAppendId).
                        replace("indexReplace", String.valueOf(size)))
                        .append(reUploadButtonString.replace("replaceForUploadForm", uploadFormId).
                                replace("replaceForUpload", fileAppendId).
                                replace("indexReplace", String.valueOf(size))
                        ).append("</Div>")
                ;
            }
        }
        messageCode.setDescription(stringBuilder.toString());
        log.info("-----------ajax-upload-file end------------");
        return JsonUtil.toJson(messageCode);
    }

    private  String getErrorMessage(MultipartFile selectedFile){
        if(selectedFile.isEmpty()){
            return MessageUtil.getMessageDesc("GENERAL_ACK018");
        }
        int maxSize = systemParamConfig.getUploadFileLimit();
        String fileTypesString = FileUtils.getStringFromSystemConfigString(systemParamConfig.getUploadFileType());
        List<String> fileTypes = Arrays.asList(fileTypesString.split(","));
        Map<String, Boolean> booleanMap = ValidationUtils.validateFile(selectedFile,fileTypes,(maxSize * 1024 *1024l));
        Boolean fileSize = booleanMap.get("fileSize");
        Boolean fileType = booleanMap.get("fileType");
        //size
        if(!fileSize){
            return MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf( maxSize),"sizeMax");
        }
        //type
        if(!fileType){
            String type = FileUtils.getFileTypeMessage(fileTypesString);
            return MessageUtil.replaceMessage("GENERAL_ERR0018",type,"fileType");
        }

        //name
        String orginName = selectedFile.getOriginalFilename();
        if(orginName != null) {
            String[] fileSplit = orginName.split("\\.");
            String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit) + "." + fileSplit[fileSplit.length - 1];
            if (fileName.length() > 100) {
                return MessageUtil.getMessageDesc("GENERAL_ERR0022");
            }
        }
        return "";
    }
    @RequestMapping(value = "/deleteFeCallFile", method = RequestMethod.POST)
    @ResponseBody
    public String deleteFeCallFile(HttpServletRequest request){
        log.info("-----------deleteFeCallFile start------------");
        String fileAppendId = ParamUtil.getString(request,"fileAppendId");
        String index  = ParamUtil.getString(request,"fileIndex");
        if( !StringUtil.isEmpty(fileAppendId) && !StringUtil.isEmpty(index)){
        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request,SEESION_FILES_MAP_AJAX + fileAppendId);
        if( !IaisCommonUtils.isEmpty(map)) {
                log.info(StringUtil.changeForLog("------ fileAppendId : " +fileAppendId +"-----------"));
                log.info(StringUtil.changeForLog("------ fileAppendIndex : " +index +"-----------"));
                map.remove(fileAppendId+index);
            ParamUtil.setSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId,(Serializable)map);
            }
        }
        log.info("-----------deleteFeCallFile end------------");

        return AppConsts.YES;
    }

    @RequestMapping(value = "/download-session-file", method = RequestMethod.GET)
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("download-session-file start ...."));

        String fileAppendId =  ParamUtil.getString(request,"fileAppendIdDown");
        String index =  ParamUtil.getString(request,"fileIndexDown");

        if( !StringUtil.isEmpty(fileAppendId) && !StringUtil.isEmpty(index)){
            Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request,SEESION_FILES_MAP_AJAX + fileAppendId);
            if( !IaisCommonUtils.isEmpty(map)) {
                log.info(StringUtil.changeForLog("------ fileAppendId : " +fileAppendId +"-----------"));
                log.info(StringUtil.changeForLog("------ fileAppendIndex : " +index +"-----------"));
                File file = map.get(fileAppendId+index);
                if(file != null){
                    byte[] fileData = FileUtils.readFileToByteArray(file);
                    if(fileData != null){
                        try {
                            response.addHeader("Content-Disposition", "attachment;filename=\"" +  file.getName()+"\"");
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
                            if(ops!=null){
                                ops.close();
                            }
                        }
                    }
                    return;
                }else {
                    log.info(StringUtil.changeForLog("------no find file :" +fileAppendId+index +" parh -----------"));
                }
                ParamUtil.setSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId,(Serializable)map);
            }
        }
        log.debug(StringUtil.changeForLog("download-session-file end ...."));
    }

    private void saveFileToOtherNodes(MultipartFile selectedFile, File toFile, String tempFolder) {
        List<String> ipAddrs = ServicesSysteminfo.getInstance().getAddressesByServiceName("hcsa-licence-web");
        if (ipAddrs != null && ipAddrs.size() > 1 && toFile != null) {
            String localIp = MiscUtil.getLocalHostExactAddress();
            log.info(StringUtil.changeForLog("Local Ip is ==>" + localIp));
            RestTemplate restTemplate = new RestTemplate();
            for (String ip : ipAddrs) {
                if (localIp.equals(ip)) {
                    continue;
                }
                try {
                    String port = ConfigHelper.getString("server.port", "8080");
                    StringBuilder apiUrl = new StringBuilder("http://");
                    apiUrl.append(ip).append(':').append(port).append("/hcsa-licence-web/tempFile-handler");
                    log.info("Request URL ==> {}", apiUrl);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                    MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
                    HttpHeaders fileHeader = new HttpHeaders();
                    byte[] content = selectedFile.getBytes();
                    ByteArrayResource fileContentAsResource = new ByteArrayResource(content) {
                        @Override
                        public String getFilename() {
                            return toFile.getName();
                        }
                    };
                    HttpEntity<ByteArrayResource> fileEnt = new HttpEntity<>(fileContentAsResource, fileHeader);
                    multipartRequest.add("selectedFile", fileEnt);
                    HttpHeaders jsonHeader = new HttpHeaders();
                    jsonHeader.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> jsonPart = new HttpEntity<>(toFile.getName(), jsonHeader);
                    multipartRequest.add("fileName", jsonPart);
                    jsonHeader = new HttpHeaders();
                    jsonHeader.setContentType(MediaType.APPLICATION_JSON);
                    jsonPart = new HttpEntity<>("ajaxUpload" + tempFolder, jsonHeader);
                    multipartRequest.add("folderName", jsonPart);
                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, headers);
                    restTemplate.postForObject(apiUrl.toString(), requestEntity, String.class);
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
