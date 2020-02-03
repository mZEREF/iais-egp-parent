package com.ecquaria.egp.core.payment.api.util;

import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.ecquaria.egp.core.payment.api.config.GatewayConstants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(GatewayConfig.log_path);
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
