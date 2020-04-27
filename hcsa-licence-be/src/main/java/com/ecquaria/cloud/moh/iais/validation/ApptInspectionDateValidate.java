package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/15 10:34
 **/
@Component
public class ApptInspectionDateValidate implements CustomizeValidator {

    @Autowired
    private AppointmentClient appointmentClient;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, "apptInspectionDateDto");
        AppointmentDto specificApptDto = apptInspectionDateDto.getSpecificApptDto();
        Date specificStartDate = apptInspectionDateDto.getSpecificStartDate();
        Date specificEndDate = apptInspectionDateDto.getSpecificEndDate();
        if(specificStartDate == null || specificEndDate == null){
            return null;
        }
        specificApptDto.setStartDate(Formatter.formatDateTime(specificStartDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
        specificApptDto.setEndDate(Formatter.formatDateTime(specificEndDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        //key userId value date
        int statusCode = appointmentClient.validateUserCalendar(specificApptDto).getStatusCode();
        if(statusCode == 406){
            errMap.put("specificDate", "UC_INSP_ERR0007");
        }
        ParamUtil.setSessionAttr(request, "apptInspectionDateDto", apptInspectionDateDto);
        return errMap;
    }
}
