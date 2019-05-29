package com.ecquaria.cloud.moh.iais.common.util;

import com.ecquaria.cloud.moh.iais.common.AppConsts;

public final class StringUtil {



    public static String getValue(Object obj) {
        return (null == obj) ? "" : getValue(obj.toString());
    }

    /**
     * returns an empty string if it's null, otherwise itself.
     *
     * @param str
     * @return
     */
    public static String getValue(String str) {
        return (str == null) ? "" : str.trim();
    }

    /**
     * returns an empty string if it's null, otherwise itself.
     *
     * @param str
     * @return
     */
    public static String getValue(int val) {
        return (val == AppConsts.EMPTY_INT) ? "" : String.valueOf(val);
    }

    /**
     * returns an empty string if it's null, otherwise itself.
     *
     * @param str
     * @return
     */
    public static String getValue(long val) {
        return (val == AppConsts.EMPTY_LONG) ? "" : String.valueOf(val);
    }

    /**
     * returns true if null or empty, otherwise false.
     */
    public static boolean isEmpty(String string) {
        return "".equals(StringUtil.getValue(string));
    }

    /**
     * returns " " if null or empty, otherwise str.
     */
    public static String getBlankEmptyString(String str) {
        return isEmpty(str) ? AppConsts.EMPTY_BLANK : str;
    }

    /**
     * display the value in view model.
     * will return "-" if the value is empty or null
     * @param str
     * @return
     */
    public static String viewValue(String str) {
        String s = getValue(str);
        return "".equals(s) ? AppConsts.EMPTY_STR : s;
    }

    public static String viewValue(int val) {
        return String.valueOf(val);
    }

    public static String viewValue(long val) {
        return String.valueOf(val);
    }

    public static String getDefaultEmptyStr(String str) {
        String s = getValue(str);

        return "".equals(s) ? AppConsts.EMPTY_STR : s;
    }
    /**
     *    encode the logger print value.
    */
    public static String encodeLogger(String logger){
        return logger;
    }
}
