package sg.gov.moh.iais.egp.bsb.util;


/** This is a very common class, may be moved to common module in the future */
public class LogUtil {
    private LogUtil() {}

    /**
     * Escape carriage return and line feeders
     * This must be called when you want to log a string which is read from request
     */
    public static String escapeCrlf(String str) {
        return str == null ? null : str.replaceAll("[\r\n]", "  ");
    }
}
