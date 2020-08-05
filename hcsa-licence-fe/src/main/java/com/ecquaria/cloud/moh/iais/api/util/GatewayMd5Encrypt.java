package com.ecquaria.cloud.moh.iais.api.util;

import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

public class GatewayMd5Encrypt {

	public static String md5(String text) {
        return DigestUtils.md5Hex(getContentBytes(text, GatewayConfig.input_charset));
    }
	
	private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            charset = "UTF-8";
        }

        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5 encrypt error, your charset is:" + charset,e);
        }
    }
}
