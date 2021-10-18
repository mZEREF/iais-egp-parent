package sg.gov.moh.iais.egp.bsb.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

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

    public static String addDate(Date date,int addTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, addTime);
        Date dt1 = calendar.getTime();
        String endDtStr = sdf.format(dt1);
        return endDtStr;
    }

    public static String dateToStr(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dtStr = sdf.format(date);
        return dtStr;
    }

    public static boolean checkDateVal(String dateStr, String start, String end) {
        boolean isDateRight = false;
        Date date = null;
        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = null;

        if (14 == dateStr.length()) {
            sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        } else if (8 == dateStr.length()) {
            sdf = new SimpleDateFormat("yyyyMMdd");
        } else
            return false;

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error(String.valueOf(e), e);
        }

        if ((start == null) && (end != null)) {
            try {
                endDate = sdf.parse(end);
            } catch (ParseException ex1) {
                log.error(String.valueOf(ex1), ex1);
            }
            if ((date != null) && (endDate != null))// Check parameters for
            {
                if (date.compareTo(endDate) <= 0)
                    isDateRight = true;
            }
        } else if ((start != null) && (end == null)) {
            try {
                startDate = sdf.parse(start);
            } catch (ParseException ex1) {
                log.error(String.valueOf(ex1), ex1);
            }
            if ((date != null) && (startDate != null)) {
                if (date.compareTo(startDate) >= 0)
                    isDateRight = true;
            }
        } else if ((start != null) && (end != null)) {
            try {
                startDate = sdf.parse(start);
                endDate = sdf.parse(end);
            } catch (ParseException ex2) {
                System.out.println(ex2.toString());
            }
            if ((startDate != null) && (date != null) && (endDate != null)) {
                if ((date.compareTo(startDate) >= 0)
                        && (date.compareTo(endDate) <= 0))
                    isDateRight = true;
            }
        }
        return isDateRight;
    }

    public static String generateRandomNum(){
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        int ends = random.nextInt(99);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return s.append(sdf.format(new Date())).append("HALP").append(String.format("%02d",ends)).toString();
    }

    public static String generateRandomByDate(){
        StringBuilder stringBuilder = new StringBuilder();
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);

        int num = (int) (Math.random() * (10000 - 1000) + 1000);
        return stringBuilder.append(dateString).append(num).toString();
    }
}
