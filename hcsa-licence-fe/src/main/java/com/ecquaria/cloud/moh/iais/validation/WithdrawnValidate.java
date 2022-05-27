package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-02-17 15:27
 **/
@Component
public class WithdrawnValidate implements CustomizeValidator {

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) httpServletRequest.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
        String withdrawnReason = ParamUtil.getRequestString(mulReq, "withdrawalReason");
        if (commonsMultipartFile != null) {
            if (commonsMultipartFile.getSize() > 5 * 1024 * 1024) {
                errorMap.put("withdrawalFile", "The file has exceeded the maximum upload size of 5MB.");
            }
            String fileName = commonsMultipartFile.getOriginalFilename();
            if (!StringUtil.isEmpty(fileName)){
                boolean result = false;
                String fileTypeStr = systemParamConfig.getUploadFileType();
                String[] fileArray = FileUtils.fileTypeToArray(fileTypeStr);
                String[] fileSplit = fileName .split("\\.");
                String fileType = fileSplit[fileSplit.length - 1].toUpperCase();
                if (fileArray!=null && fileArray.length >0){
                    for (int i=0;i<fileArray.length;i++){
                        if (fileType.equals(fileArray[i])){
                            result = true;
                        }
                    }
                }
                if (!result){
                    errorMap.put("withdrawalFile", MessageUtil.replaceMessage("GENERAL_ERR0018", fileTypeStr, "fileType"));
                }
            }
        }
        if (StringUtil.isEmpty(withdrawnReason)) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Reason for Withdrawal","field");
            errorMap.put("withdrawnReason", errMsg);
        }
        if ("WDR005".equals(withdrawnReason)) {
            String withdrawnRemarks = ParamUtil.getRequestString(mulReq, "withdrawnRemarks");
            if (StringUtil.isEmpty(withdrawnRemarks)) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Remarks", "field");
                errorMap.put("withdrawnRemarks", errMsg);
            }
        }
        return errorMap;
    }
}
