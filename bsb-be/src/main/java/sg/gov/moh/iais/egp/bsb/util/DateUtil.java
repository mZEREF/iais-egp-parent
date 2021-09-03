package sg.gov.moh.iais.egp.bsb.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/9/3 9:19
 * DESCRIPTION: TODO
 **/
@Slf4j
public class DateUtil {
    public static Date yearAgoDt(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,-1);
        return calendar.getTime();
    }
}
