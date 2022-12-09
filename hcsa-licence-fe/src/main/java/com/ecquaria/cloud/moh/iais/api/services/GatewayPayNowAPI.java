package com.ecquaria.cloud.moh.iais.api.services;


import com.ecquaria.cloud.moh.iais.api.config.GatewayPayNowConfig;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.util.GatewayPayNowCore;
import com.ecquaria.cloud.moh.iais.api.util.GatewayPayNowNotify;
import com.ecquaria.cloud.moh.iais.api.util.GatewayPayNowSubmit;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class GatewayPayNowAPI {

	private static final String HEAD_HTTP = "https://";

	public static String create_partner_trade_by_buyer(
			Map<String, String> sParaTemp,HttpServletRequest request,String returnUrl) throws Exception {

		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayPayNowConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, returnUrl);
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, HEAD_HTTP + request.getServerName()+GatewayPayNowConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayPayNowConfig.input_charset);

		String strButtonName = "OK";

		return GatewayPayNowSubmit.buildForm(sParaTemp, HEAD_HTTP + request.getServerName()+GatewayPayNowConfig.common_gateway_url, "get",
				strButtonName);
	}
	public static String create_partner_trade_by_buyer_url(
			Map<String, String> sParaTemp,HttpServletRequest request,String returnUrl) throws Exception {

		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayPayNowConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, returnUrl);
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, HEAD_HTTP + request.getServerName()+GatewayPayNowConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayPayNowConfig.input_charset);

		String strButtonName = "OK";

		return GatewayPayNowSubmit.buildUrl(sParaTemp, HEAD_HTTP + request.getServerName()+GatewayPayNowConfig.common_gateway_url, "get",
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

		return GatewayPayNowNotify.verifyNotify(fields);
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
		Map<String, String> sParaNew = GatewayPayNowCore.paraFilter(fields);

		if(GatewayPayNowCore.verifySign(sParaNew, signStr, signType)){
			return true;
		}else{
			return false;
		}
	}
}
