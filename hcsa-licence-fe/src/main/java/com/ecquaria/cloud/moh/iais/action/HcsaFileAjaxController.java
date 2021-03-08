package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.sz.commons.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * @author wangyu
 * @date 2021/03/05
 */
@Controller
@Slf4j
public class HcsaFileAjaxController {
    public  final static String SEESION_FILES_MAP_AJAX = "seesion_files_map_ajax_fe";
    @ResponseBody
    @PostMapping(value = "ajax-upload-file",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String ajaxUpload(HttpServletRequest request,@RequestParam("selectedFile") MultipartFile selectedFile, @RequestParam("fileAppendId")String fileAppendId,@RequestParam("uploadFormId") String uploadFormId,@RequestParam("reloadIndex") int reloadIndex){
        log.info("-----------ajax-upload-file start------------");
        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request,SEESION_FILES_MAP_AJAX+fileAppendId);
        if(map == null){
            map = IaisCommonUtils.genNewHashMap();
         }
         int size = map.size();
         try{
             if(reloadIndex == -1){
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
        MessageDto messageCode = new MessageDto();
        messageCode.setDescription(stringBuilder.toString());
        log.info("-----------ajax-upload-file end------------");
        return JsonUtil.toJson(messageCode);
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
            }
        }
        log.info("-----------deleteFeCallFile end------------");
        return AppConsts.YES;
    }
}
