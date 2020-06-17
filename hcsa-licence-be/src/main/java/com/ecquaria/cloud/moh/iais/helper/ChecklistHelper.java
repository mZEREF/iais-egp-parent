package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ConfigExcelItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
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
        if (file == null){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "GENERAL_ERR0004"));
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

    private static boolean uploadType(String type, String service, String module, String subType, String hciCode){
        if (StringUtils.isEmpty(type) && StringUtils.isEmpty(service) &&  StringUtils.isEmpty(module) && StringUtils.isEmpty(subType) && StringUtils.isEmpty(hciCode)){
            return true;
        }else {
            return false;
        }
    }

    public static boolean validateTemplate(HttpServletRequest request, ChecklistConfigDto excelTemplate){
        int action = excelTemplate.getWebAction();
        String configId = excelTemplate.getId();
        boolean isCommon = excelTemplate.isCommon();
        String type = excelTemplate.getType();
        String module = excelTemplate.getModule();
        String service = excelTemplate.getSvcName();
        String subType = excelTemplate.getSvcSubType();
        String hciCode = excelTemplate.getHciCode();
        String effectiveStartDate = excelTemplate.getEftStartDate();
        String effectiveEndDate = excelTemplate.getEftEndDate();

        List<ConfigExcelItemDto> allItem = excelTemplate.getExcelTemplate();

        if (IaisCommonUtils.isEmpty(allItem)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR017"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return true;
        }

        if (HcsaChecklistConstants.UPDATE == action){
            if (StringUtils.isEmpty(configId)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
                return true;
            }
        }

        if (StringUtils.isEmpty(effectiveStartDate) || StringUtils.isEmpty(effectiveEndDate)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "Effective Start Date and end date is mandatory , format should be DD/MM/YYYY"));
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
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "Effective Start Date and end date is mandatory , format should be DD/MM/YYYY"));
                return true;
            }
        }

        boolean order = uploadType(type, service, module, subType, hciCode);

        if (isCommon != order){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "Only common or service can be created"));
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
