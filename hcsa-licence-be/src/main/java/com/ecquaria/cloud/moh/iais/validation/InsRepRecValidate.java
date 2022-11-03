package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/1/17 16:41
 */
public class InsRepRecValidate implements CustomizeValidator {
    private static final String RECOMMENDATION = "recommendation";
    private static final String CHRONO = "chrono";
    private static final String NUMBER = "number";
    private static final String OTHERS = "Others";

    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = new HashMap<>(34);
        String chrono = ParamUtil.getRequestString(httpServletRequest, CHRONO);
        String number = ParamUtil.getRequestString(httpServletRequest, NUMBER);
        String enforcement = ParamUtil.getRequestString(httpServletRequest, "engageEnforcement");
        String enforcementRemarks = ParamUtil.getRequestString(httpServletRequest, "enforcementRemarks");
        String periods = ParamUtil.getRequestString(httpServletRequest, "periods");
        String recommendation = ParamUtil.getRequestString(httpServletRequest, RECOMMENDATION);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(httpServletRequest, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        if (!InspectionReportConstants.REJECTED.equals(recommendation)&&OTHERS.equals(periods)&&!ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)&&!ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Other Period", "field");
            if (StringUtil.isEmpty(chrono)) {

                errorMap.put("chronoUnit", errMsg);
            } else if (StringUtil.isEmpty(number)) {
                errorMap.put("recomInNumber",errMsg);
            } else {
                try {
                    Integer.parseInt(number);
                } catch (NumberFormatException e) {
                    errorMap.put("recomInNumber", "GENERAL_ERR0002");
                }
            }
        }
        if(!StringUtil.isEmpty(enforcement)&&StringUtil.isEmpty(enforcementRemarks)){
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Enforcement Remarks", "field");
            errorMap.put("enforcementRemarks", errMsg);
        }
        if(!StringUtil.isEmpty(recommendation)){
            if(InspectionReportConstants.APPROVED.equals(recommendation)||InspectionReportConstants.APPROVEDLTC.equals(recommendation)){
                if(StringUtil.isEmpty(periods)){
                    String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Period", "field");
                    errorMap.put("periods", errMsg);
                }
            }
        }
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(applicationType)){
            recommendation  = ParamUtil.getRequestString(httpServletRequest, "recommendationRfc");
        }
        verifyVehicleEasMtsReport( httpServletRequest,errorMap,applicationViewDto,recommendation);
        verifySpecialServiceReport( httpServletRequest,errorMap,applicationViewDto,recommendation);
        verifyOtherServiceReport( httpServletRequest,errorMap,applicationViewDto,recommendation);
            return errorMap;
    }

    private void verifyVehicleEasMtsReport(HttpServletRequest request, Map<String, String> errorMap, ApplicationViewDto applicationViewDto,String recommendation){
        if(HcsaLicenceBeConstant.EDIT_VEHICLE_FLAG.equalsIgnoreCase((String)ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.APP_VEHICLE_FLAG))){
            List<String> rejectCode = IaisCommonUtils.genNewArrayList(2);
            rejectCode.add(InspectionReportConstants.REJECTED);
            rejectCode.add(InspectionReportConstants.RFC_REJECTED);
            HcsaApplicationViewValidate.valiVehicleEasMtsCommon(request,errorMap,applicationViewDto,StringUtil.getNonNull(recommendation),rejectCode, null);
        }
    }
    private void verifySpecialServiceReport(HttpServletRequest request, Map<String, String> errorMap, ApplicationViewDto applicationViewDto,String recommendation){
        if(HcsaLicenceBeConstant.EDIT_VEHICLE_FLAG.equalsIgnoreCase((String)ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.APP_SPECIAL_FLAG))){
            List<String> rejectCode = IaisCommonUtils.genNewArrayList(2);
            rejectCode.add(InspectionReportConstants.REJECTED);
            rejectCode.add(InspectionReportConstants.RFC_REJECTED);
            HcsaApplicationViewValidate.valiSpecialServiceCommon(request,errorMap,applicationViewDto,StringUtil.getNonNull(recommendation),rejectCode, null);
        }
    }
    private void verifyOtherServiceReport(HttpServletRequest request, Map<String, String> errorMap, ApplicationViewDto applicationViewDto,String recommendation){
        if(HcsaLicenceBeConstant.EDIT_VEHICLE_FLAG.equalsIgnoreCase((String)ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.APP_OTHER_FLAG))){
            List<String> rejectCode = IaisCommonUtils.genNewArrayList(2);
            rejectCode.add(InspectionReportConstants.REJECTED);
            rejectCode.add(InspectionReportConstants.RFC_REJECTED);
            HcsaApplicationViewValidate.valiOtherServiceCommon(request,errorMap,applicationViewDto,StringUtil.getNonNull(recommendation),rejectCode, null);
        }
    }
}
