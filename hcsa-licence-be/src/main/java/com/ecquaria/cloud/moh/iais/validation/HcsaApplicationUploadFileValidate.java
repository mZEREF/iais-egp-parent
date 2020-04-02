package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zhilin
 * @date 3/30/2020
 */
public class HcsaApplicationUploadFileValidate implements CustomizeValidator {
    private final Integer FileMaxLength = 10;
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        MultipartFile commonsMultipartFile = (MultipartFile) httpServletRequest.getAttribute("selectedFile");
        if(commonsMultipartFile.isEmpty()){
            errMap.put("selectedFile","The file cannot be empty.");
        }else{
            if(commonsMultipartFile.getSize() > FileMaxLength * 1024 * 1024){
                errMap.put("selectedFile","The file size must less than 10M.");
            }
        }
        return errMap;
    }
}
