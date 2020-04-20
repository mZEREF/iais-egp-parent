package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-02-17 15:27
 **/
public class WithdrawnValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) httpServletRequest.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
        String withdrawnReason = ParamUtil.getRequestString(mulReq, "withdrawalReason");
        if (!commonsMultipartFile.isEmpty()) {
            if (commonsMultipartFile.getSize() > 5 * 1024 * 1024) {
                errorMap.put("withdrawalFile", "The file has exceeded the maximum upload size of 5MB.");
            }
            String[] fileSplit = commonsMultipartFile.getOriginalFilename().split("\\.");
            String fileType = fileSplit[fileSplit.length - 1];
            if (!fileType.toLowerCase().equals("pdf")
                    && !fileType.toLowerCase().equals("jpg")
                    && !fileType.toLowerCase().equals("jpeg")
                    && !fileType.toLowerCase().equals("png")) {
                errorMap.put("withdrawalFile", "The file type is incorrect.");
            }
        }
        if (StringUtil.isEmpty(withdrawnReason)) {
            errorMap.put("withdrawnReason", "ERR0009");
        }
        if ("WDR005".equals(withdrawnReason)) {
            String withdrawnRemarks = ParamUtil.getRequestString(mulReq, "withdrawnRemarks");
            if (StringUtil.isEmpty(withdrawnRemarks)) {
                errorMap.put("withdrawnRemarks", "ERR0009");
            }
        }
        return errorMap;
    }
}
