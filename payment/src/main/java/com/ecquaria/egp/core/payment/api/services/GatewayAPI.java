package com.ecquaria.egp.core.payment.api.services;

import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.ecquaria.egp.core.payment.api.config.GatewayConstants;
import com.ecquaria.egp.core.payment.api.util.GatewayCore;
import com.ecquaria.egp.core.payment.api.util.GatewayNotify;
import com.ecquaria.egp.core.payment.api.util.GatewaySubmit;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class GatewayAPI {

	public static String create_partner_trade_by_buyer(
			Map<String, String> sParaTemp) throws Exception {

		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, GatewayConfig.return_url);
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, GatewayConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayConfig.input_charset);

//		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, "moh");
//		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, "return");
//		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "notify");
//		sParaTemp.put(GatewayConstants.INPUT_CHARSET, "UTF_8");

		String strButtonName = "OK";

		return GatewaySubmit.buildForm(sParaTemp, GatewayConfig.common_gateway_url, "get",
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
		
		return GatewayNotify.verifyNotify(fields);
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
		Map<String, String> sParaNew = GatewayCore.paraFilter(fields);
		
		if(GatewayCore.verifySign(sParaNew, signStr, sign_type)){
			return true;
		}else{
			return false;
		}
	}
}
