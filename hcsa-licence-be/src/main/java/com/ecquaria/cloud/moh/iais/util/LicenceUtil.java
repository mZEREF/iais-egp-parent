package com.ecquaria.cloud.moh.iais.util;

import java.util.Calendar;
import java.util.Date;

/**
 * LicenceUtil
 *
 * @author suocheng
 * @date 2/7/2020
 */

public class LicenceUtil {
    public static Date getExpiryDate(Date startDate, int yearLength){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR,yearLength);
        return  calendar.getTime();
    }
}
