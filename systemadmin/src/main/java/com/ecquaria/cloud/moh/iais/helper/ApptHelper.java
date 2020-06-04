package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;

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
}
