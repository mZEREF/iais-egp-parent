package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.collections4.MapUtils;

import static com.ecquaria.cloud.moh.iais.constant.HmacConstants.HMAC_SHA_1;
import static com.ecquaria.cloud.moh.iais.constant.HmacConstants.HMAC_SHA_256;
import static com.ecquaria.cloud.moh.iais.constant.HmacConstants.HMAC_SHA_384;
import static com.ecquaria.cloud.moh.iais.constant.HmacConstants.HMAC_SHA_512;

/**
 * HmacHelper
 *
 * @author Jinhua
 * @date 2019/11/28 17:26
 */
public class HmacHelper {

    /**
     * the map storing the hmac algorithms supported by tyk(version 2.7.6)
     */
    private static final Map<String, String> ALGORITHM_MAP = MapUtils.putAll(IaisCommonUtils.genNewHashMap(),
            new String[]{HMAC_SHA_1, "HmacSHA1", HMAC_SHA_256, "HmacSHA256", HMAC_SHA_384, "HmacSHA384",
                    HMAC_SHA_512, "HmacSHA512"});

    /**
     * the signature object which contains date header and authorization header
     */
    public static class Signature {
        private String date;
        private String authorization;

        private Signature() {

        }

        /**
         * @return value of http header 'date'
         */
        public String date() {
            return date;
        }

        /**
         * @return value of http header 'authorization'
         */
        public String authorization() {
            return authorization;
        }
    }

    private HmacHelper() {
    }

    /**
     * validate the algorithm string
     *
     * @return the origin string or value of the origin string in the map
     * @throws NoSuchAlgorithmException if the algorithm not found in the map
     */
    private static String validateAlgorithm(String algorithm) throws NoSuchAlgorithmException {
        String alg = ALGORITHM_MAP.get(algorithm);
        if (Objects.nonNull(alg)) {
            return alg;
        } else if (ALGORITHM_MAP.containsValue(algorithm)) {
            return algorithm;
        } else {
            throw new NoSuchAlgorithmException("Algorithm " + algorithm + " not available");
        }
    }

    /**
     * encrypt a string using the specified algorithm
     *
     * @param encryptKey  the secret key for encryption
     * @param encryptText the text you want to encrypt
     * @param algorithm   the algorithm used for encryption
     * @return the encrypted string
     */
    private static String encrypt(String encryptKey, String encryptText, String algorithm) {
        try {
            algorithm = validateAlgorithm(algorithm);
            SecretKeySpec signInKey = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(signInKey);
            byte[] rawHmac = mac.doFinal(encryptText.getBytes(StandardCharsets.UTF_8));
            byte[] result = Base64.getEncoder().encode(rawHmac);
            return new String(result, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IaisRuntimeException(e);
        }
    }

    /**
     * get the hmac signature object used for tyk hmac authorization
     *
     * @param keyId     the id of the hmac enabled key
     * @param secretKey the hmac secret
     * @param algorithm the algorithm used for encryption
     * @return the hmac signature object
     */
    public static Signature getSignature(String keyId, String secretKey, String algorithm) {
        Signature object = new Signature();
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'UTC'", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = format.format(new Date());
            object.date = date;
            String signature = encrypt(secretKey, "date: " + date, algorithm);
            Map<String, String> invertMap = MapUtils.invertMap(ALGORITHM_MAP);
            String alg = invertMap.get(algorithm);
            if (Objects.nonNull(alg)) {
                algorithm = alg;
            }
            object.authorization = "Signature keyId=\"" + keyId +
                    "\",algorithm=\"" + algorithm +
                    "\",signature=\"" + URLEncoder.encode(signature, StandardCharsets.UTF_8.name()) + "\"";
            return object;
        } catch (UnsupportedEncodingException e) {
            throw new IaisRuntimeException(e);
        }
    }

    /**
     * get the hmac signature object used for tyk hmac authorization, using the default hmac-sha1 algorithm
     *
     * @param keyId     the id of the hmac enabled key
     * @param secretKey the hmac secret
     * @return the hmac signature object
     */
    public static Signature getSignature(String keyId, String secretKey) {
        return getSignature(keyId, secretKey, HMAC_SHA_1);
    }

}
