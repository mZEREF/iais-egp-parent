package com.ecquaria.egp.core.payment.api.util;


import com.ecquaria.egp.core.payment.api.config.GatewayConstants;
import com.ecquaria.egp.core.payment.api.config.GatewayStripeConfig;

import java.util.HashMap;
import java.util.Map;

public class GatewayStripeNotify {

	public static String verifyNotify(Map<String, String> fields) throws Exception{
		
		String json = "";
		
        boolean verified = true;
        
        Map<String, String> sParaNew = GatewayStripeCore.paraFilter(fields);
        String sign_type = fields.get(GatewayConstants.SIGN_TYPE_KEY);

        String sign = fields.get(GatewayConstants.SIGN_KEY);

        if(sign == null) {sign = "";}

		if(sign_type != null && (sign_type.equals(GatewayConstants.SIGN_TYPE_MD5) || sign_type.equals(GatewayConstants.SIGN_TYPE_RSA)))
			verified = GatewayStripeCore.verifySign(sParaNew, sign, sign_type);
		
		if(!verified){
	    	throw new Exception("Invalid notify request parameter.");
	    }else{
	    	Map<String, String> map = new HashMap<String, String>();
			StringBuilder sb = new StringBuilder();
			
	    	map.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayStripeConfig.payment_registry_name);
	    	map.put(GatewayConstants.CPS_REFNO, fields.get(GatewayConstants.CPS_REFNO));
	    	map.put(GatewayConstants.SVCREF_NO, fields.get(GatewayConstants.SVCREF_NO));
	    	map.put(GatewayConstants.INPUT_CHARSET, fields.get(GatewayConstants.INPUT_CHARSET));
	    	map.put(GatewayConstants.NOTIFY_STATUS, "success");
	    	String ressign = "";
	    	if(sign_type != null && (sign_type.equals(GatewayConstants.SIGN_TYPE_MD5) || sign_type.equals(GatewayConstants.SIGN_TYPE_RSA)))
	    		ressign = GatewayStripeCore.buildSign(map, sign_type);
	    	
	    	sb.append('{').append('\"' + GatewayConstants.REGISTRY_NAME_KEY + "\":\"").append(GatewayStripeConfig.payment_registry_name).append("\",")
					.append('\"' + GatewayConstants.NOTIFY_STATUS + "\":").append("\"success\",")
					.append('\"' + GatewayConstants.CPS_REFNO + "\":").append('\"').append(fields.get(GatewayConstants.CPS_REFNO)).append("\",")
					.append('\"' + GatewayConstants.SVCREF_NO + "\":").append('\"').append(fields.get(GatewayConstants.SVCREF_NO)).append("\",")
					.append('\"' + GatewayConstants.INPUT_CHARSET + "\":").append('\"').append(fields.get(GatewayConstants.INPUT_CHARSET)).append("\",")
	    	.append("\"sign\":\"").append(ressign).append("\",\"sign_type\":\"")
	    	.append(GatewayStripeConfig.sign_type).append("\"}");
	    	
	    	json = sb.toString();
	    }
		
		return json;
	}
}
