package com.ecquaria.cloud.moh.iais.action;


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
import java.io.File;
import java.io.Serializable;
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

    public  final static String SEESION_FILES_MAP_AJAX = "seesion_files_map_ajax_fe";
    public  final static String SEESION_FILES_MAP_AJAX_MAX_INDEX = "_MaxIndex";
    @ResponseBody
    @PostMapping(value = "ajax-upload-file",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String ajaxUpload(HttpServletRequest request,@RequestParam("selectedFile") MultipartFile selectedFile, @RequestParam("fileAppendId")String fileAppendId,@RequestParam("uploadFormId") String uploadFormId,@RequestParam("reloadIndex") int reloadIndex){
        log.info("-----------ajax-upload-file start------------");
        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId);
        Integer size;
        if(map == null){
            size = 0;
            map = IaisCommonUtils.genNewHashMap();
         }else {
            size = (Integer) ParamUtil.getSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId+SEESION_FILES_MAP_AJAX_MAX_INDEX);
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
        String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
        //name
        String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit)+"."+fileSplit[fileSplit.length-1];
        stringBuilder.append("<Div ").append(" id ='").append(fileAppendId).append(suffix).append("' >").append(fileName)
                .append(" ").append(deleteButtonString.replace("replaceForDelete",fileAppendId).
                                      replace("indexReplace",String.valueOf(size)))
                .append( reUploadButtonString.replace("replaceForUploadForm",uploadFormId).
                        replace("replaceForUpload",fileAppendId).
                        replace("indexReplace",String.valueOf(size))
                ).append("</Div>")
        ;
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
        if( !map.isEmpty()) {
                log.info(StringUtil.changeForLog("------ fileAppendId : " +fileAppendId +"-----------"));
                log.info(StringUtil.changeForLog("------ fileAppendIndex : " +index +"-----------"));
                map.remove(fileAppendId+index);
            ParamUtil.setSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId,(Serializable)map);
            }
        }
        log.info("-----------deleteFeCallFile end------------");

        return AppConsts.YES;
    }
}
