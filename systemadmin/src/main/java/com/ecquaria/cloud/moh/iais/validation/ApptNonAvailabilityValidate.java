package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        ApptUserCalendarDto apptUserCalendarDto = appointmentClient.getCalenderBySysUserCorrIdAndStatus(apptNonAvailabilityDateDto.getUserCorrId(), AppointmentConstants.CALENDAR_STATUS_RESERVED).getEntity();
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> nonAvaDate = MiscUtil.getDateInPeriodByRecurrence(apptNonAvailabilityDateDto.getBlockOutStart(),
                        apptNonAvailabilityDateDto.getBlockOutEnd(), apptNonAvailabilityDateDto.getRecurrence());
        List<String> inspectionDate = getInspDateByCalendar(apptUserCalendarDto);
        if(!IaisCommonUtils.isEmpty(inspectionDate)) {
            for (Date date : nonAvaDate) {
                for (String inspDate : inspectionDate) {
                    String nonDate = sdf2.format(date);
                    if (nonDate.equals(inspDate)) {
                        errMap.put("nonAvaStartDate", "UC_INSP_ERR0004");
                    }
                }
            }
        }
        return errMap;
    }

    private List<String> getInspDateByCalendar(ApptUserCalendarDto apptUserCalendarDto) {
        if(apptUserCalendarDto != null){
            List<Date> timeSlots = apptUserCalendarDto.getStartSlot();
            if(!IaisCommonUtils.isEmpty(timeSlots)) {
                List<String> inspDates = IaisCommonUtils.genNewArrayList();
                for (Date date : timeSlots) {
                    String inspDate = Formatter.formatDateTime(date, "yyyy-MM-dd");
                    inspDates.add(inspDate);
                }
                Set<String> inspDateSet = new HashSet<>(inspDates);
                inspDates = new ArrayList<>(inspDateSet);
                return inspDates;
            }
        }
        return null;
    }
}
