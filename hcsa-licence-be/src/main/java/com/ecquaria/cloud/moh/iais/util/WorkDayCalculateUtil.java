package com.ecquaria.cloud.moh.iais.util;


import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * WorkDayCalculateUtils
 *
 * @author junyu
 * @date 2021/3/9
 */

public class WorkDayCalculateUtil {

    private WorkDayCalculateUtil(){}


    public static Date  getDate(Date currentDate, int days, List<Date> holidays ) {
        if (days == 0) {
            return currentDate;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int step = days < 0 ? -1 : 1;
        int i = 0;
        int daysAbs = Math.abs(days);
        while (i < daysAbs) {
            calendar.add(Calendar.DATE, step);
            i++;
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

                i--;
            }else {
                for (Date holiday:holidays
                ) {
                    String holidayStr= Formatter.formatDateTime(holiday, Formatter.DATE);
                    String calendarStr= Formatter.formatDateTime(calendar.getTime(), Formatter.DATE);
                    if (holidayStr.equals(calendarStr)){
                        i--;
                    }
                }
            }

        }

        return calendar.getTime();
    }

    public static Date  getDate(Date currentDate, int days, List<Date> holidays ,List<ApptNonWorkingDateDto> nonWorkingDateListByWorkGroupId) {
        List<Integer> nonWkrDays = IaisCommonUtils.genNewArrayList();
        Map<String, Integer> mapDays = IaisCommonUtils.genNewHashMap() ;
        mapDays.put("Sunday",Calendar.SUNDAY);
        mapDays.put("Monday",Calendar.MONDAY);
        mapDays.put("Tuesday",Calendar.TUESDAY);
        mapDays.put("Wednesday",Calendar.WEDNESDAY);
        mapDays.put("Thursday",Calendar.THURSDAY);
        mapDays.put("Friday",Calendar.FRIDAY);
        mapDays.put("Saturday",Calendar.SATURDAY);
        if(nonWorkingDateListByWorkGroupId!=null){
            for (ApptNonWorkingDateDto nonWorkDto:nonWorkingDateListByWorkGroupId
            ) {
                if(mapDays.get(nonWorkDto.getRecursivceDate())!=null){
                    nonWkrDays.add(mapDays.get(nonWorkDto.getRecursivceDate()));
                }
            }
        }else {
            nonWkrDays.add(Calendar.SUNDAY);
            nonWkrDays.add(Calendar.SATURDAY);
        }
        if (days == 0) {
            return currentDate;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int step = days < 0 ? -1 : 1;
        int i = 0;
        int daysAbs = Math.abs(days);
        while (i < daysAbs) {
            calendar.add(Calendar.DATE, step);
            i++;

            if ( nonWkrDays.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                i--;
            }else {
                for (Date holiday:holidays
                ) {
                    String holidayStr= Formatter.formatDateTime(holiday, Formatter.DATE);
                    String calendarStr= Formatter.formatDateTime(calendar.getTime(), Formatter.DATE);
                    if (holidayStr.equals(calendarStr)){
                        i--;
                    }
                }
            }

        }

        return calendar.getTime();
    }
}
