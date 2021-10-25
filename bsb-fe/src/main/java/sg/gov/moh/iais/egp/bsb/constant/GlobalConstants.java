package sg.gov.moh.iais.egp.bsb.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Constants in this class may be replaced by values from spring config in future
 */
public class GlobalConstants {
    private GlobalConstants() {}

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static final String WEB_ROOT = "/bsb-fe/themes/fe";
}
