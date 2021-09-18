package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.sz.commons.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        String errerMesssage = getErrorMessage(selectedFile);
        MessageDto messageCode = new MessageDto();
         if(!StringUtil.isEmpty(errerMesssage)){
             messageCode.setMsgType("N");
             messageCode.setDescription(errerMesssage);
             return JsonUtil.toJson(messageCode);
         }else {
             messageCode.setMsgType("Y");
         }
         try{
             if(reloadIndex == -1){
                 ParamUtil.setSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId+SEESION_FILES_MAP_AJAX_MAX_INDEX,size+1);
                 if (needMaxGlobal) {
                     ParamUtil.setSessionAttr(request, GLOBAL_MAX_INDEX_SESSION_ATTR, size + 1);
                 }
                 map.put(fileAppendId+size, FileUtils.multipartFileToFile(selectedFile));
             }else {
                 map.put(fileAppendId+reloadIndex, FileUtils.multipartFileToFile(selectedFile));
                 size = reloadIndex;
             }

         }catch (Exception e){
             log.error(e.getMessage(),e);
             log.info("----------change MultipartFile to file  falie-----------------");
             return "";
         }
        ParamUtil.setSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId,(Serializable)map);

        StringBuilder stringBuilder = new StringBuilder();
        String suffix = "Div"+size;
        String deleteButtonString = "      <button type=\"button\" class=\"btn btn-secondary btn-sm\"\n" +
                "                                                    onclick=\"javascript:deleteFileFeAjax('replaceForDelete',indexReplace);\">\n" +
                "                                                Delete</button>";
        String reUploadButtonString="  <button type=\"button\" class=\"btn btn-secondary btn-sm\"\n" +
                "                                                    onclick=\"javascript:reUploadFileFeAjax('replaceForUpload',indexReplace,'replaceForUploadForm');\">\n" +
                "                                               ReUpload</button>";
        if(selectedFile != null && !StringUtil.isEmpty(selectedFile.getOriginalFilename())) {
            String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
            //name
            String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit) + "." + fileSplit[fileSplit.length - 1];
            String CSRF = ParamUtil.getString(request, "OWASP_CSRFTOKEN");
            String url = "<a href=\"pageContext.request.contextPath/download-session-file?fileAppendIdDown=replaceFileAppendIdDown&fileIndexDown=replaceFileIndexDown&OWASP_CSRFTOKEN=replaceCsrf\" title=\"Download\" class=\"downloadFile\">";
            fileName = url + fileName + "</a>";
            stringBuilder.append("<Div ").append(" id ='").append(fileAppendId).append(suffix).append("' >").
                    append(fileName.replace("pageContext.request.contextPath", "/hcsa-licence-web")
                            .replace("replaceFileAppendIdDown", fileAppendId)
                            .replace("replaceFileIndexDown", String.valueOf(size)).replace("replaceCsrf", CSRF))
                    .append(' ').append(deleteButtonString.replace("replaceForDelete", fileAppendId).
                    replace("indexReplace", String.valueOf(size)))
                    .append(reUploadButtonString.replace("replaceForUploadForm", uploadFormId).
                            replace("replaceForUpload", fileAppendId).
                            replace("indexReplace", String.valueOf(size))
                    ).append("</Div>")
            ;
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
        if(!StringUtil.isEmpty(selectedFile.getOriginalFilename())) {
            String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
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
}
