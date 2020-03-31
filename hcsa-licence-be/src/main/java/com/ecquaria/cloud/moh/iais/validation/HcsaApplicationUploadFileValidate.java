package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;

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
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) httpServletRequest.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
        if(commonsMultipartFile.isEmpty()){
            errMap.put("selectedFile","The file cannot be empty.");
        }else{
            if(commonsMultipartFile.getSize() > FileMaxLength * 1024 * 1024){
                errMap.put("selectedFile","The file size must less than 4M.");
            }
        }
        return errMap;
    }
}
