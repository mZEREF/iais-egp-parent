package com.ecquaria.egp.core.payment.api.util;

import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.ecquaria.egp.core.payment.api.config.GatewayConstants;

import java.util.HashMap;
import java.util.Map;

public class GatewayNotify {

	public static String verifyNotify(Map<String, String> fields) throws Exception{
		
		String json = "";
		
        boolean verified = true;
        
        Map<String, String> sParaNew = GatewayCore.paraFilter(fields);
        String sign_type = fields.get(GatewayConstants.SIGN_TYPE_KEY);
        
        String sign = fields.get(GatewayConstants.SIGN_KEY);
        
        if(sign == null) {sign = "";}
        
		if(sign_type != null && (sign_type.equals(GatewayConstants.SIGN_TYPE_MD5) || sign_type.equals(GatewayConstants.SIGN_TYPE_RSA)))
			verified = GatewayCore.verifySign(sParaNew, sign, sign_type);
		
		if(!verified){
	    	throw new Exception("Invalid notify request parameter.");
	    }else{
	    	Map<String, String> map = new HashMap<String, String>();
			StringBuffer sb = new StringBuffer();
			
	    	map.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayConfig.payment_registry_name);
	    	map.put(GatewayConstants.CPS_REFNO, fields.get(GatewayConstants.CPS_REFNO));
	    	map.put(GatewayConstants.SVCREF_NO, fields.get(GatewayConstants.SVCREF_NO));
	    	map.put(GatewayConstants.INPUT_CHARSET, fields.get(GatewayConstants.INPUT_CHARSET));
	    	map.put(GatewayConstants.NOTIFY_STATUS, "success");
	    	String ressign = "";
	    	if(sign_type != null && (sign_type.equals(GatewayConstants.SIGN_TYPE_MD5) || sign_type.equals(GatewayConstants.SIGN_TYPE_RSA)))
	    		ressign = GatewayCore.buildSign(map, sign_type);
	    	
	    	sb.append("{").append("\""+GatewayConstants.REGISTRY_NAME_KEY+"\":\"").append(GatewayConfig.payment_registry_name).append("\",")
	    	.append("\""+GatewayConstants.NOTIFY_STATUS+"\":").append("\"success\",")
	    	.append("\""+GatewayConstants.CPS_REFNO+"\":").append("\""+fields.get(GatewayConstants.CPS_REFNO)+"\",")
	    	.append("\""+GatewayConstants.SVCREF_NO+"\":").append("\""+fields.get(GatewayConstants.SVCREF_NO)+"\",")
	    	.append("\""+GatewayConstants.INPUT_CHARSET+"\":").append("\""+fields.get(GatewayConstants.INPUT_CHARSET)+"\",")
	    	.append("\"sign\":\"").append(ressign).append("\",\"sign_type\":\"")
	    	.append(GatewayConfig.sign_type).append("\"}");
	    	
	    	json = sb.toString();
	    }
		
		return json;
	}
}
