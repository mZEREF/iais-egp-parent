package com.ecquaria.cloud.moh.iais.validation;


import com.ecquaria.cloud.moh.iais.common.constant.audit.AuditTrailConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamCacheHelper;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


@Slf4j
public class AuditTrailDtoValidate implements CustomizeValidator {
    private static final int MONTH_DAY = 30;

	@Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
		String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
		String startDate = ParamUtil.getRequestString(request, AuditTrailConstants.PARAM_STARTDATE);
		String endDate = ParamUtil.getRequestString(request, AuditTrailConstants.PARAM_ENDDATE);
		switch (currentAction){
			case AuditTrailConstants.ACTION_QUERY:
				if (StringUtils.isEmpty(startDate)
						|| StringUtils.isEmpty(endDate)){
					errMap.put("actionTime", MessageCodeKey.ERR0010);
				}else {
					int reduceDay = getReduceDay(IaisEGPHelper.parseToDate(startDate, "dd/MM/yyyy"),
							IaisEGPHelper.parseToDate(endDate, "dd/MM/yyyy"));

					String paramVal = SystemParamCacheHelper
							.getParamValueById(SystemParamCacheHelper.AUDIT_TRAIL_TIME_LIMIT) == null ? "3" :
								SystemParamCacheHelper
							.getParamValueById(SystemParamCacheHelper.AUDIT_TRAIL_TIME_LIMIT);

					String msg = MessageUtil.getMessageDesc("GENERAL_ERR0010");

					if ((reduceDay > (Integer.valueOf(paramVal) * MONTH_DAY))){
						errMap.put("actionTime", MessageUtil.formatMessage(msg, paramVal));
					}
				}
				break;
			default:
				//nonthing
		}

        return errMap;
    }

    private Integer getReduceDay(Date startDate, Date endDate){
	    int day = (int) ((endDate.getTime() - startDate.getTime()) / (1000*3600*24));
	    return day;
    }

}
