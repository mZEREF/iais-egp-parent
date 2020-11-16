package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zhilin
 * @date 3/30/2020
 */
public class HcsaApplicationUploadFileValidate implements CustomizeValidator {
    private final Integer FileMaxLength = 4;
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(httpServletRequest,"applicationViewDto");
        int maxFile = FileMaxLength;
        if(applicationViewDto != null){
            maxFile = applicationViewDto.getSystemMaxFileSize();
        }
        MultipartFile commonsMultipartFile = (MultipartFile) httpServletRequest.getAttribute("selectedFile");
        if(commonsMultipartFile.isEmpty()){
            errMap.put("selectedFile",MessageUtil.replaceMessage("GENERAL_ERR0006","Document","field"));
        }else if(applicationViewDto != null){
            List<String> fileTypes = Arrays.asList(applicationViewDto.getSystemFileType().split(","));
            Map<String, Boolean> booleanMap = ValidationUtils.validateFile(commonsMultipartFile,fileTypes,(maxFile * 1024 *1024l));
            Boolean fileSize = booleanMap.get("fileSize");
            Boolean fileType = booleanMap.get("fileType");
            //size
            if(!fileSize){
                errMap.put("selectedFile",MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(maxFile),"sizeMax"));
            }
            //type
            if(!fileType){
                String type = getFileType(applicationViewDto.getSystemFileType());
                errMap.put("selectedFile",MessageUtil.replaceMessage("GENERAL_ERR0018",type,"fileType"));
            }
        }
        return errMap;
    }

    private String getFileType(String type){
        String[] fileTypelist = type.split(",");
        if(fileTypelist.length >5) {
            StringBuilder stringBiff = new StringBuilder();
            stringBiff.append("<br/>");
            for(int indexlist = 0;indexlist <fileTypelist.length; indexlist++){
                if(indexlist== fileTypelist.length-1){
                    stringBiff .append( fileTypelist[indexlist]);
                }  else if(indexlist >0 && indexlist %5 == 0) {
                    stringBiff .append(fileTypelist[indexlist]).append(",<br/>");

                }else {
                    stringBiff.append(fileTypelist[indexlist]).append(',') ;
                }
            }
            return stringBiff.toString();
        }else {
            return "<br/>" + type;
        }
    }
}
