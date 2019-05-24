package sg.gov.moh.iais.common;

import java.util.Date;
import java.util.Locale;

public final class AppConsts {// default settings used in app
    public static final Locale	DEFAULT_LOCALE								= new Locale("en", "SG");
    public static final String	DEFAULT_NUMBER_PATTERN						= "#,###,##0.00";
    public static final String	DEFAULT_NUMBER_EDIT_PATTERN					= "###.##";
    public static final String	DEFAULT_FORMAT_DATE							= "dd/MM/yyyy";
    public static final String	DEFAULT_FORMAT_DATE_MONTH					= "/MM/yyyy";
    public static final String	DEFAULT_FORMAT_TIME							= "HH:mm";
    public static final String	DEFAULT_FORMAT_TIME_12						= "hh:mm a";
    public static final String	DEFAULT_FORMAT_TIME_HOUR					= "HH";
    public static final String	DEFAULT_FORMAT_TIME_MINUTE					= "mm";
    public static final String	DEFAULT_FORMAT_DATE_TIME					= "dd/MM/yyyy HH:mm";
    public static final String  DEFAULT_FORMAT_PL_DATE_TIME                 = "dd/MM/yyyy HH:mm:ss SSS";
    public static final String	CERTAIN_FORMAT_DATE_TIME					= "dd/MM/yyyy HH:mm a";
    public static final String	FORMAT_DATE_GENERATED_NO					= "ddMMyyyy";
    public static final String	MONTH_FOR_COMPARE							= "yyyyMM";
    public static final String	DATE_FOR_COMPARE							= "yyyyMMdd";
    public static final String	DATE_TIME_FOR_COMPARE						= "yyyyMMddHHmmss";
    public static final String	DATE_TIME_FOR_FILE_NAME						= "yyyyMMdd HHmm";
    public static final String	DEFAULT_FORMAT_DATE_TIME_FOR_DB				= "DD/MM/YYYY HH24:MI";
    public static final String	FORMAT_DATE_TIME_FOR_DB						= "yyyy-MM-dd HH:mm";
    public static final String	FORM_DEFAULT_FORMAT_DATE					= "dd/MM/yyyy";
    public static final String  ORACLE_DEFAULT_FORMAT_DATE              	= "yyyy-MM-dd hh:mm:ss";
    public static final String	CERTAIN_FORMAT_DATE_TIME_Pay				= "yyyy/MM/dd HH:mm:ss z";

    public static final String	DEFAULT_MONEY_PATTERN						= "[1-9]{1}\\d{0,2}(\\,{0,1}\\d{3}){0,3}(\\.\\d{1,2})?";

    public static final int		DEFAULT_REMOVE_INDEX						= -1;
    public static final Date    EMPTY_DATE									= new Date(-5364662400000L); // Wed Jan 01 00:00:00 GMT 1800


    public static final String	EMPTY_STR									= "-";
    public static final String  DFT_DELIMITER                        		= "|";
    public static final String	USER_GROUP_CONN_STR							= "_";
    public static final String	DEFAULT_DELIMITER							= ";";
    public static final String	LEFT_BRACKETS								= "{";
    public static final String	RIGHT_BRACKETS								= "}";
    public static final String	OPENING_BRACKET								= "[";
    public static final String	CLOSING_BRACKET								= "]";
    public static final String	SLASH										= "/";
    public static final String	COLON										= ":";
    public static final String  COMMA                                       = ",";
    public static final String	EMPTY_STRING								= "";
    public static final String	EMPTY_BLANK									= " ";

    public static final int		EMPTY_INT									= 0;
    public static final long	EMPTY_LONG									= 0L;
    public static final double	EMPTY_DOUBLE								= 0.0;

    public static final int		INVALID_INT									= -1;
    public static final long	INVALID_LONG								= -1L;
    public static final double	INVALID_DOUBLE								= -1.0;

    public static final byte	DAY_OF_WEEK_SUNDAY							= 0;
    public static final byte	DAY_OF_WEEK_MONDAY							= 1;
    public static final byte	DAY_OF_WEEK_TUESDAY							= 2;
    public static final byte	DAY_OF_WEEK_WEDNESDAY						= 3;
    public static final byte	DAY_OF_WEEK_THURSDAY						= 4;
    public static final byte	DAY_OF_WEEK_FRIDAY							= 5;
    public static final byte	DAY_OF_WEEK_SATURDAY						= 6;
}
