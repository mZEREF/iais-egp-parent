package com.ecquaria.cloud.moh.iais.api.services;


import com.ecquaria.cloud.moh.iais.api.config.GatewayStripeConfig;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.util.GatewayStripeCore;
import com.ecquaria.cloud.moh.iais.api.util.GatewayStripeNotify;
import com.ecquaria.cloud.moh.iais.api.util.GatewayStripeSubmit;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class GatewayStripeAPI {

	private static final String HEAD_HTTP = "https://";

	public static String create_partner_trade_by_buyer(
			Map<String, String> sParaTemp,HttpServletRequest request,String returnUrl) throws Exception {

		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayStripeConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, returnUrl);
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, HEAD_HTTP + request.getServerName()+GatewayStripeConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayStripeConfig.input_charset);
		String strButtonName = "OK";

		return GatewayStripeSubmit.buildForm(sParaTemp, HEAD_HTTP + request.getServerName()+GatewayStripeConfig.common_gateway_url, "get",
				strButtonName);
	}
	public static String create_partner_trade_by_buyer_url(
			Map<String, String> sParaTemp,HttpServletRequest request,String returnUrl) throws Exception {

		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayStripeConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, returnUrl);
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, HEAD_HTTP + request.getServerName()+GatewayStripeConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayStripeConfig.input_charset);
		String strButtonName = "OK";

		return GatewayStripeSubmit.buildUrl(sParaTemp, HEAD_HTTP + request.getServerName()+GatewayStripeConfig.common_gateway_url, "get",
				strButtonName);
	}
	
	public static String verifyNotify(HttpServletRequest request) throws Exception{
		Map<String, String> fields = new HashMap<>();
		for (Enumeration<String> enum3 = request.getParameterNames(); enum3.hasMoreElements();) {
		    String fieldName = enum3.nextElement();
		    String fieldValue = request.getParameter(fieldName);
		    if ((fieldValue != null) && (fieldValue.length() > 0)) {
		        fields.put(fieldName, fieldValue);
		    }
		}
		
		return GatewayStripeNotify.verifyNotify(fields);
	}
	
	public static boolean verifyParameter(HttpServletRequest request) throws Exception{
		Map<String, String> fields = new HashMap<>();
		for (Enumeration<String> enum3 = request.getParameterNames(); enum3.hasMoreElements();) {
		    String fieldName = enum3.nextElement();
		    String fieldValue = request.getParameter(fieldName);
		    if ((fieldValue != null) && (fieldValue.length() > 0)) {
		        fields.put(fieldName, fieldValue);
		    }
		}
		
		String signStr = fields.get(GatewayConstants.SIGN_KEY);
		String signType = fields.get(GatewayConstants.SIGN_TYPE_KEY);
		Map<String, String> sParaNew = GatewayStripeCore.paraFilter(fields);
		
		if(GatewayStripeCore.verifySign(sParaNew, signStr, signType)){
			return true;
		}else{
			return false;
		}
	}
}
