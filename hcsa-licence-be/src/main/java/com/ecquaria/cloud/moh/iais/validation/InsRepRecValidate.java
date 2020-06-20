package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.submission.client.App;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
        if (OTHERS.equals(periods)&&!ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)&&!ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)) {
            if (StringUtil.isEmpty(chrono)) {
                errorMap.put("chronoUnit", "ERR0009");
            } else if (StringUtil.isEmpty(number)) {
                errorMap.put("recomInNumber", "ERR0009");
            } else {
                try {
                    Integer.parseInt(number);
                } catch (NumberFormatException e) {
                    errorMap.put("recomInNumber", "GENERAL_ERR0002");
                }
            }
        }
        if(!StringUtil.isEmpty(enforcement)&&StringUtil.isEmpty(enforcementRemarks)){
            errorMap.put("enforcementRemarks", "ERR0009");
        }
        if(!StringUtil.isEmpty(recommendation)){
            if(InspectionReportConstants.APPROVED.equals(recommendation)||InspectionReportConstants.APPROVEDLTC.equals(recommendation)){
                if(StringUtil.isEmpty(periods)){
                    errorMap.put("periods", "ERR0009");
                }
            }
        }
            return errorMap;
    }
}
