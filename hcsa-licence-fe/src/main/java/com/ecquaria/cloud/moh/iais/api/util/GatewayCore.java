package com.ecquaria.cloud.moh.iais.api.util;

import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class GatewayCore {

	public static String buildSign(Map<String, String> sArray, String sign_type) throws Exception {
		if(GatewayConstants.SIGN_TYPE_MD5.equals(sign_type)){
			return buildMd5Sign(sArray);
		}else if(GatewayConstants.SIGN_TYPE_RSA.equals(sign_type)){
			return buildRSASign(sArray);
		}else{
			throw new Exception("Invalid sign type.");
		}
	}
	
	private static String buildMd5Sign(Map<String, String> sArray) {
        String prestr = createLinkString(sArray);
        prestr = prestr + GatewayConfig.key;
        String mysign = GatewayMd5Encrypt.md5(prestr);
        return mysign;
    }

	private static String buildRSASign(Map<String, String> sArray) {
		String prestr = createLinkString(sArray);
        String mysign = GatewayRSAEncrypt.sign(prestr);
        return mysign;
	}
	
	public static boolean verifySign(Map<String, String> sArray, String sign, String sign_type){
	    sArray.remove("OWASP_CSRFTOKEN");
		if(GatewayConstants.SIGN_TYPE_MD5.equals(sign_type)){
			String signStr = buildMd5Sign(sArray);
			if(signStr != null && !("".equals(signStr)) && signStr.equals(sign)){
				return true;
			}else{
				return false;
			}
		}else if(GatewayConstants.SIGN_TYPE_RSA.equals(sign_type)){
			String prestr = createLinkString(sArray);
			return GatewayRSAEncrypt.verifySign(prestr, sign);
		}
		
		return false;
	}
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = IaisCommonUtils.genNewHashMap();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (Map.Entry<String, String> entry : sArray.entrySet()) {
            if (entry.getValue() == null || "".equals(entry.getValue()) || "sign".equalsIgnoreCase(entry.getKey())
                || "sign_type".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuilder prestr = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {
                prestr.append(key).append('=').append(value);
            } else {
                prestr.append(key).append('=').append(value).append('&');
            }
        }

        return prestr.toString();
    }

    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(GatewayConfig.log_path);
            writer.write(sWord);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
