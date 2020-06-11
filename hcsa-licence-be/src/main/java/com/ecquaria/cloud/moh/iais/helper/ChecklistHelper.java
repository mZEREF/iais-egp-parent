package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.ChecklistConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author yi chen
 * @Date:2020/6/9
 **/

public final class ChecklistHelper {

    private ChecklistHelper(){}

    public static Map<String, String> validateFile(HttpServletRequest request, MultipartFile file){
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

    private static boolean uploadType(String type, String service, String module, String subType, String hciCode){
        if (StringUtils.isEmpty(type) && StringUtils.isEmpty(service) &&  StringUtils.isEmpty(module) && StringUtils.isEmpty(subType) && StringUtils.isEmpty(hciCode)){
            return true;
        }else {
            return false;
        }
    }

    public static boolean validateTemplate(HttpServletRequest request, ChecklistConfigDto excelTemplate){
        boolean isCommon = excelTemplate.isCommon();
        String type = excelTemplate.getType();
        String module = excelTemplate.getModule();
        String service = excelTemplate.getSvcName();
        String subType = excelTemplate.getSvcSubType();
        String hciCode = excelTemplate.getHciCode();
        String effectiveStartDate = excelTemplate.getEftStartDate();
        String effectiveEndDate = excelTemplate.getEftEndDate();

        if (StringUtils.isEmpty(effectiveStartDate) || StringUtils.isEmpty(effectiveEndDate)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "Effective Start Date and end date is mandatory , format should be DD/MM/YYYY"));
            return true;
        }else {
            try {
                IaisEGPHelper.parseToDate(effectiveStartDate);
                IaisEGPHelper.parseToDate(effectiveEndDate);
            }catch (IaisRuntimeException e){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "Effective Start Date and end date is mandatory , format should be DD/MM/YYYY"));
                return true;
            }
        }

        boolean order = uploadType(type, service, module, subType, hciCode);

        if (isCommon && order){

        }else if (!isCommon && !order){

        }else {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "Only common or service can be created"));
            return true;
        }

        return false;
    }

    public static void replaceErrorMsgContentMasterCode(HttpServletRequest request, List<ErrorMsgContent> errorMsgContentList) {
        for (ErrorMsgContent errorMsgContent : errorMsgContentList){
            int idx = 0;
            for(String error : errorMsgContent.getErrorMsgList()){
                String msg = MessageUtil.getMessageDesc(error);
                errorMsgContent.getErrorMsgList().set(idx++, msg);
            }
        }


    }
}
