package com.ecquaria.cloud.moh.iais.api.services;


import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayNetsConfig;
import com.ecquaria.cloud.moh.iais.api.util.GatewayNetsCore;
import com.ecquaria.cloud.moh.iais.api.util.GatewayNetsNotify;
import com.ecquaria.cloud.moh.iais.api.util.GatewayNetsSubmit;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class GatewayNetsAPI {

	public static String create_partner_trade_by_buyer(
			HttpServletRequest request, Map<String, String> sParaTemp) throws Exception {

		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayNetsConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, GatewayConfig.return_url);
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "https://" + request.getServerName()+GatewayNetsConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayNetsConfig.input_charset);

//		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, "moh");
//		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, "return");
//		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "notify");
//		sParaTemp.put(GatewayConstants.INPUT_CHARSET, "UTF_8");

		String strButtonName = "OK";

		return GatewayNetsSubmit.buildForm(sParaTemp, "https://" + request.getServerName()+GatewayNetsConfig.common_gateway_url, "get",
				strButtonName);
	}

	public static String create_partner_trade_by_buyer_url(
			Map<String, String> sParaTemp,HttpServletRequest request,String returnUrl) throws Exception {

		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayNetsConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, returnUrl);
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "https://" + request.getServerName()+GatewayNetsConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayNetsConfig.input_charset);

//		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, "moh");
//		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, "return");
//		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "notify");
//		sParaTemp.put(GatewayConstants.INPUT_CHARSET, "UTF_8");

		String strButtonName = "OK";

		return GatewayNetsSubmit.buildUrl(sParaTemp, "https://" + request.getServerName()+GatewayNetsConfig.common_gateway_url, "get",
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
		
		return GatewayNetsNotify.verifyNotify(fields);
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
		Map<String, String> sParaNew = GatewayNetsCore.paraFilter(fields);
		
		if(GatewayNetsCore.verifySign(sParaNew, signStr, sign_type)){
			return true;
		}else{
			return false;
		}
	}


}
