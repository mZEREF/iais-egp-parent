package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ConfigExcelItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.ChecklistConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author yi chen
 * @Date:2020/6/9
 **/

public final class ChecklistHelper {

    private ChecklistHelper(){}

    public static boolean validateFile(HttpServletRequest request, MultipartFile file){
        if (file == null || file.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "GENERAL_ERR0020"));
            return true;
        }

        String originalFileName = file.getOriginalFilename();
        if (!FileUtils.isExcel(originalFileName)){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "GENERAL_ERR0005"));
            return true;
        }

        if (FileUtils.outFileSize(file.getSize())){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "GENERAL_ERR0004"));
            return true;
        }

        return false;
    }

    public static boolean validateTemplate(HttpServletRequest request, ChecklistConfigDto excelTemplate){
        int action = excelTemplate.getWebAction();
        String configId = excelTemplate.getId();
        boolean isCommon = excelTemplate.isCommon();
        String type = excelTemplate.getType();
        String module = excelTemplate.getModule();
        String service = excelTemplate.getSvcName();

        if (HcsaChecklistConstants.UPDATE == action){
            if (StringUtils.isEmpty(configId)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
                return true;
            }
        }

        if (!isCommon){
            if (StringUtil.isEmpty(module)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, MessageUtil.replaceMessage("GENERAL_ERR0006", "Module", "field")));
                return true;
            }

            if (StringUtil.isEmpty(type)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, MessageUtil.replaceMessage("GENERAL_ERR0006", "Type", "field")));
                return true;
            }

            if (StringUtil.isEmpty(service)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, MessageUtil.replaceMessage("GENERAL_ERR0006", "Service", "field")));
                return true;
            }
        }

        String effectiveStartDate = excelTemplate.getEftStartDate();
        String effectiveEndDate = excelTemplate.getEftEndDate();

        if (StringUtils.isEmpty(effectiveStartDate) || StringUtils.isEmpty(effectiveEndDate)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR014"));
            return true;
        }else {
            try {
                Date sDate = IaisEGPHelper.parseToDate(effectiveStartDate);
                Date eDate = IaisEGPHelper.parseToDate(effectiveEndDate);

                if (IaisEGPHelper.isAfterDate(eDate, sDate)){
                    ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR013"));
                    return true;
                }

            }catch (IaisRuntimeException e){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR014"));
                return true;
            }
        }

        List<ConfigExcelItemDto> allItem = excelTemplate.getExcelTemplate();

        if (IaisCommonUtils.isEmpty(allItem)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR017"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return true;
        }

        return false;
    }

    public static void replaceErrorMsgContentMasterCode(List<ErrorMsgContent> errorMsgContentList) {
        for (ErrorMsgContent errorMsgContent : errorMsgContentList){
            int idx = 0;
            for(String error : errorMsgContent.getErrorMsgList()){
                String msg = MessageUtil.getMessageDesc(error);
                errorMsgContent.getErrorMsgList().set(idx++, msg);
            }
        }


    }
}
