package com.ecquaria.cloud.moh.iais.constant;

/**
 * HmacConstants
 *
 * @author Jinhua
 * @date 2019/11/28 17:29
 */
public class HmacConstants {
    private HmacConstants() {

    }

    /**
     * key of http header 'date'
     */
    public static final String HEADER_DATE = "date";
    /**
     * key of http header 'authorization'
     */
    public static final String HEADER_AUTHORIZATION = "authorization";
    /**
     * the default algorithm used for hmac encryption
     */
    public static final String HMAC_SHA_1 = "hmac-sha1";
    public static final String HMAC_SHA_256 = "hmac-sha256";
    public static final String HMAC_SHA_384 = "hmac-sha384";
    public static final String HMAC_SHA_512 = "hmac-sha512";

    public static final String HELP_MESSAGE = "\"java -cp eic-helper.jar com.ecquaria.eic.helper.hmac.HmacVerify\" requires 3 or 4 arguments"
            + "\n\nUsage:  java -cp eic-helper.jar com.ecquaria.eic.helper.hmac.HmacVerify <api_url> <tyk_key> <hmac_secret> <algorithm>";

    public static final String HTTPS = "https";

    public static final int OK = 200;
}
