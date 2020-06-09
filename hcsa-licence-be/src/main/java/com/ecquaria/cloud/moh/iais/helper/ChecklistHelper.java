package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.ChecklistConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yi chen
 * @Date:2020/6/9
 **/

public final class ChecklistHelper {

    private ChecklistHelper(){}

    public static Map<String, String> validationFile(HttpServletRequest request, MultipartFile file){
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        if (file == null){
            errorMap.put(ChecklistConstant.FILE_UPLOAD_ERROR, MessageCodeKey.GENERAL_ERR0004);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return errorMap;
        }

        String originalFileName = file.getOriginalFilename();
        if (!FileUtils.isExcel(originalFileName)){
            errorMap.put(ChecklistConstant.FILE_UPLOAD_ERROR, MessageCodeKey.GENERAL_ERR0005);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return errorMap;
        }

        if (FileUtils.outFileSize(file.getSize())){
            errorMap.put(ChecklistConstant.FILE_UPLOAD_ERROR, MessageCodeKey.GENERAL_ERR0004);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return errorMap;
        }

        return errorMap;
    }
}
