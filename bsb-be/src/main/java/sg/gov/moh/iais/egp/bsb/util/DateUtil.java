package sg.gov.moh.iais.egp.bsb.util;

import java.text.SimpleDateFormat;
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

}
