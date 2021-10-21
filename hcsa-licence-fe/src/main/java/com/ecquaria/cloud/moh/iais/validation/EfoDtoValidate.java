package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * EfoDtoValidate
 *
 * @author junyu
 * @date 2021/10/21
 */
@Component
@Slf4j
public class EfoDtoValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        String dateStarted = ParamUtil.getRequestString(httpServletRequest, IntranetUserConstant.INTRANET_STARTDATE);
        String reason = ParamUtil.getRequestString(httpServletRequest, IntranetUserConstant.INTRANET_MOBILENO);
        if (!StringUtil.isEmpty(dateStarted) ) {
            String[] eftStartDateStr = dateStarted.split("/");
            Date today = new Date();
            //get today string
            String todayStr = Formatter.formatDateTime(today, AppConsts.DEFAULT_DATE_FORMAT);
            //get start Date By request
            Date sDate;
            try {
                sDate = Formatter.parseDateTime(dateStarted, AppConsts.DEFAULT_DATE_FORMAT);
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
                sDate = new Date();
            }
            if( today.after(sDate)) {
                errorMap.put("dateStarted", "USER_ERR007");
            }
        }

        if ("WDR005".equals(reason)) {
            String othersReason = ParamUtil.getRequestString(httpServletRequest, IntranetUserConstant.INTRANET_OFFICETELNO);
            if (StringUtil.isEmpty(othersReason)) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Remarks", "field");
                errorMap.put("othersReason", errMsg);
            }
        }
        return errorMap;
    }

}
