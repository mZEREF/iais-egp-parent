package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
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
public class EfoDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        String dateStarted = ParamUtil.getRequestString(httpServletRequest, "efoDateStarted");
        String reason = ParamUtil.getRequestString(httpServletRequest, "reasonSelect");
        if (!StringUtil.isEmpty(dateStarted) ) {
            Date today = new Date();
            Date sDate;
            try {
                sDate = Formatter.parseDateTime(dateStarted, AppConsts.DEFAULT_DATE_FORMAT);
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
                sDate = new Date();
            }
            if( sDate.after(today)) {
                errorMap.put("startDate", MessageUtil.replaceMessage("DS_ERR010","Date Started", "field"));
            }
        }

        if (!StringUtil.isEmpty(reason)&& DataSubmissionConsts.EFO_REASON_OTHERS.equals(reason)) {
            String othersReason = ParamUtil.getRequestString(httpServletRequest, "othersReason");
            if (StringUtil.isEmpty(othersReason)) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Others Reason", "field");
                errorMap.put("othersReason", errMsg);
            }else if(othersReason.length()>100){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","100");
                repMap.put("fieldNo","Others Reason");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("othersReason", errMsg);
            }
        }
        return errorMap;
    }

}
