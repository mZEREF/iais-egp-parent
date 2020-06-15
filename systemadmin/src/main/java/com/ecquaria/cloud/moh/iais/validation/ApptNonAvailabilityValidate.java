package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.ApptHelper;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/5 9:46
 **/
@Component
public class ApptNonAvailabilityValidate implements CustomizeValidator {

    @Autowired
    private AppointmentClient appointmentClient;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(request, "inspNonAvailabilityDto");
        ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
        apptAppInfoShowDto.setSysUserCorrIds(apptNonAvailabilityDateDto.getUserSysCorrIds());
        apptAppInfoShowDto.setCalendarStatus(AppointmentConstants.CALENDAR_STATUS_RESERVED);
        ApptUserCalendarDto apptUserCalendarDto = appointmentClient.getCalenderBySysUserCorrIdAndStatus(apptAppInfoShowDto).getEntity();
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> nonAvaDate = MiscUtil.getDateInPeriodByRecurrence(apptNonAvailabilityDateDto.getBlockOutStart(),
                        apptNonAvailabilityDateDto.getBlockOutEnd(), apptNonAvailabilityDateDto.getRecurrence());
        List<String> inspectionDate = ApptHelper.getInspDateByCalendar(apptUserCalendarDto);
        if(apptNonAvailabilityDateDto.getBlockOutStart() == null || apptNonAvailabilityDateDto.getBlockOutEnd() == null){
            return null;
        }
        if(apptNonAvailabilityDateDto.getBlockOutStart().after(apptNonAvailabilityDateDto.getBlockOutEnd())){
            errMap.put("nonAvaDate", "APPT_ERROR0002");
            return errMap;
        }
        if(!IaisCommonUtils.isEmpty(inspectionDate)) {
            for (Date date : nonAvaDate) {
                for (String inspDate : inspectionDate) {
                    String nonDate = sdf2.format(date);
                    if (nonDate.equals(inspDate)) {
                        errMap.put("nonAvaDate", "APPT_ERROR0001");
                    }
                }
            }
        }
        return errMap;
    }


}
