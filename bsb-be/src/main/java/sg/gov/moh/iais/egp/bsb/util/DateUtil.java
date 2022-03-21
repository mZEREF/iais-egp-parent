package sg.gov.moh.iais.egp.bsb.util;


import sg.gov.moh.iais.egp.bsb.constant.AppConstants;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author YiMing
 * @version 2022/1/20 15:04
 **/
public class DateUtil {
    private DateUtil() {}

    public static String toString(Date date) {

        String time;
        SimpleDateFormat formater = new SimpleDateFormat();
        formater.applyPattern("dd/MM/yyyy");
        time = formater.format(date);
        return time;
    }

    public static String convertToString(LocalDate localDate){
        return AppConstants.DEFAULT_DATE_FORMATTER.format(localDate);
    }

    public static String convertToString(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            pattern = AppConstants.DEFAULT_DATE_TIME_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
