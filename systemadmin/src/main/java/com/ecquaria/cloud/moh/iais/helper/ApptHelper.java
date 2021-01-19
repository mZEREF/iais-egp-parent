package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/6/3
 **/

public final class ApptHelper {
    private ApptHelper(){}

    public static List<String> getInspDateByCalendar(ApptUserCalendarDto apptUserCalendarDto) {
        if(apptUserCalendarDto != null){
            List<Date> timeSlots = apptUserCalendarDto.getStartSlot();
            if(!IaisCommonUtils.isEmpty(timeSlots)) {
                List<String> inspDates = IaisCommonUtils.genNewArrayList();
                for (Date date : timeSlots) {
                    String inspDate = Formatter.formatDateTime(date, "yyyy-MM-dd");
                    if (inspDates.contains(inspDate)){
                        continue;
                    }
                    inspDates.add(inspDate);
                }

                return inspDates;
            }
        }

        return null;
    }

    public static void preYearOption(HttpServletRequest request, Date start, Date end) {
        List<SelectOption> dropYear = IaisCommonUtils.genNewArrayList();
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        int from = c.get(Calendar.YEAR);
        c.setTime(end);
        int to = c.get(Calendar.YEAR);
        for (int i = to; i > from - 10; i--){
            dropYear.add(new SelectOption(String.valueOf(i), String.valueOf(i)));
        }

        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT, (Serializable) dropYear);
        ParamUtil.setRequestAttr(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT, dropYear);
    }
}
