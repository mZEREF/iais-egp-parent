package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
        MultipartFile commonsMultipartFile = (MultipartFile) httpServletRequest.getAttribute("selectedFile");
        if(commonsMultipartFile.isEmpty()){
            errMap.put("selectedFile","The file cannot be empty.");
        }else{
            Map<String, Boolean> booleanMap = ValidationUtils.validateFile(commonsMultipartFile);
            Boolean fileSize = booleanMap.get("fileSize");
            Boolean fileType = booleanMap.get("fileType");
            //size
            if(!fileSize){
                errMap.put("selectedFile","The file size must less than " + FileMaxLength + "M.");
            }
            //type
            if(!fileType){
                errMap.put("selectedFile","The file type is invalid.");
            }
        }
        return errMap;
    }
}
