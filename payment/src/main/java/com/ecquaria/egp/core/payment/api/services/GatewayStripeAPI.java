package com.ecquaria.egp.core.payment.api.services;


import com.ecquaria.egp.core.payment.api.config.GatewayConstants;
import com.ecquaria.egp.core.payment.api.config.GatewayStripeConfig;
import com.ecquaria.egp.core.payment.api.util.GatewayStripeCore;
import com.ecquaria.egp.core.payment.api.util.GatewayStripeNotify;
import com.ecquaria.egp.core.payment.api.util.GatewayStripeSubmit;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class GatewayStripeAPI {

	public static String create_partner_trade_by_buyer(HttpServletRequest request, Map<String, String> sParaTemp) throws Exception {
		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayStripeConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, GatewayStripeConfig.return_url);
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "https://" + request.getServerName()+GatewayStripeConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayStripeConfig.input_charset);

//		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, "moh");
//		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, "return");
//		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "notify");
//		sParaTemp.put(GatewayConstants.INPUT_CHARSET, "UTF_8");

		String strButtonName = "OK";

		return GatewayStripeSubmit.buildForm(sParaTemp, "https://" + request.getServerName()+GatewayStripeConfig.common_gateway_url, "get",
				strButtonName);
	}
	
	public static String verifyNotify(HttpServletRequest request) throws Exception{
		Map<String, String> fields = new HashMap<String, String>();
		for (Enumeration<String> enum3 = request.getParameterNames(); enum3.hasMoreElements();) {
		    String fieldName = (String) enum3.nextElement();
		    String fieldValue = request.getParameter(fieldName);
		    if ((fieldValue != null) && (fieldValue.length() > 0)) {
		        fields.put(fieldName, fieldValue);
		    }
		}
		
		return GatewayStripeNotify.verifyNotify(fields);
	}
	
	public static boolean verifyParameter(HttpServletRequest request) throws Exception{
		Map<String, String> fields = new HashMap<String, String>();
		for (Enumeration<String> enum3 = request.getParameterNames(); enum3.hasMoreElements();) {
		    String fieldName = (String) enum3.nextElement();
		    String fieldValue = request.getParameter(fieldName);
		    if ((fieldValue != null) && (fieldValue.length() > 0)) {
		        fields.put(fieldName, fieldValue);
		    }
		}
		
		String signStr = fields.get(GatewayConstants.SIGN_KEY);
		String sign_type = fields.get(GatewayConstants.SIGN_TYPE_KEY);
		Map<String, String> sParaNew = GatewayStripeCore.paraFilter(fields);
		
		if(GatewayStripeCore.verifySign(sParaNew, signStr, sign_type)){
			return true;
		}else{
			return false;
		}
	}


}
