package com.ecquaria.cloud.moh.iais.api.util;


import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayNetsConfig;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class GatewayNetsCore {

	public static String buildSign(Map<String, String> sArray, String signType) throws Exception {
		if(GatewayConstants.SIGN_TYPE_MD5.equals(signType)){
			return buildMd5Sign(sArray);
		}else if(GatewayConstants.SIGN_TYPE_RSA.equals(signType)){
			return buildRSASign(sArray);
		}else{
			throw new Exception("Invalid sign type.");
		}
	}
	
	private static String buildMd5Sign(Map<String, String> sArray) {
        String prestr = createLinkString(sArray);
        prestr = prestr + GatewayNetsConfig.key;
        return GatewayMd5Encrypt.md5(prestr);
    }

	private static String buildRSASign(Map<String, String> sArray) {
		String prestr = createLinkString(sArray);
        return GatewayRSAEncrypt.sign(prestr);
	}
	
	public static boolean verifySign(Map<String, String> sArray, String sign, String signType){
	    sArray.remove("OWASP_CSRFTOKEN");
		if(GatewayConstants.SIGN_TYPE_MD5.equals(signType)){
			String signStr = buildMd5Sign(sArray);
			if(signStr != null && !("".equals(signStr)) && signStr.equals(sign)){
				return true;
			}else{
				return false;
			}
		}else if(GatewayConstants.SIGN_TYPE_RSA.equals(signType)){
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

        List<String> keys = new ArrayList<>(params.keySet());
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
    }
}
