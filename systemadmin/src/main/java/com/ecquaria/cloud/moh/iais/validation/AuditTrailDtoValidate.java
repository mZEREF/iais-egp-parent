package com.ecquaria.cloud.moh.iais.validation;


import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.audit.AuditTrailConstant;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;


@Slf4j
@Component
public class AuditTrailDtoValidate implements CustomizeValidator {
    private static final int MONTH_DAY = 7;

	@Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
		String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
		String startDate = ParamUtil.getRequestString(request, AuditTrailConstant.PARAM_STARTDATE);
		String endDate = ParamUtil.getRequestString(request, AuditTrailConstant.PARAM_ENDDATE);
		switch (currentAction){
			case AuditTrailConstant.ACTION_QUERY:
				if (!StringUtils.isEmpty(startDate)
						&& !StringUtils.isEmpty(endDate)){
					try {
						Date st = Formatter.parseDate(startDate);
						Date ed = Formatter.parseDate(endDate);
						if (st.after(ed)){
							errMap.put("compareDateError", "AUDIT_ERR009");
						}else {
							int reduceDay = getReduceDay(IaisEGPHelper.parseToDate(startDate, AppConsts.DEFAULT_DATE_FORMAT),
									IaisEGPHelper.parseToDate(endDate, AppConsts.DEFAULT_DATE_FORMAT));

							int value = SystemParamUtil.getAuditTrailSearchWeek();
							value = value == 0 ? 3 : value;
							log.info(StringUtil.changeForLog("audit trail week" + value));
							String msg = MessageUtil.getMessageDesc("GENERAL_ERR0010");

							if ((reduceDay >= (value * MONTH_DAY))){
								errMap.put("compareDateError", MessageUtil.formatMessage(msg, String.valueOf(value)));
							}
						}
					} catch (ParseException e) {
						log.error(e.getMessage(), e);
					}
				}

				break;
			default:
				//nothing
		}

        return errMap;
    }

    private Integer getReduceDay(Date startDate, Date endDate){
	    int day = (int) ((endDate.getTime() - startDate.getTime()) / (1000*3600*24));
	    return day;
    }

}
