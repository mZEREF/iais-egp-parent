package com.ecquaria.cloud.moh.iais.util;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
                    if (holiday.equals(calendar.getTime())){
                        i--;
                    }
                }
            }

        }

        return calendar.getTime();
    }
}
