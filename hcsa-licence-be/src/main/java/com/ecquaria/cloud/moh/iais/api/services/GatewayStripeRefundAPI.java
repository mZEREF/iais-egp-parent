package com.ecquaria.cloud.moh.iais.api.services;


import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayStripeRefundConfig;
import com.ecquaria.cloud.moh.iais.api.util.GatewayStripeRefundCore;
import com.ecquaria.cloud.moh.iais.api.util.GatewayStripeRefundNotify;
import com.ecquaria.cloud.moh.iais.api.util.GatewayStripeRefundSubmit;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class GatewayStripeRefundAPI {

	public static String create_partner_trade_by_buyer(
			Map<String, String> sParaTemp,HttpServletRequest request,String returnUrl) throws Exception {

		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayStripeRefundConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, returnUrl);
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "https://" + request.getServerName()+GatewayStripeRefundConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayStripeRefundConfig.input_charset);

//		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, "moh");
//		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, "return");
//		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "notify");
//		sParaTemp.put(GatewayConstants.INPUT_CHARSET, "UTF_8");

		String strButtonName = "OK";

		return GatewayStripeRefundSubmit.buildForm(sParaTemp, "https://" + request.getServerName()+GatewayStripeRefundConfig.common_gateway_url, "get",
				strButtonName);
	}
	public static Map<String, String> create_partner_trade_by_buyer_sParaTemp(
			Map<String, String> sParaTemp, HttpServletRequest request, String returnUrl) throws Exception {

		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayStripeRefundConfig.payment_registry_name);
		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, returnUrl);
		String svcName=request.getServerName();
		svcName=svcName.replace("intra","inter");
		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "https://" + svcName+GatewayStripeRefundConfig.notify_url);
		sParaTemp.put(GatewayConstants.INPUT_CHARSET, GatewayStripeRefundConfig.input_charset);

//		sParaTemp.put(GatewayConstants.REGISTRY_NAME_KEY, "moh");
//		sParaTemp.put(GatewayConstants.RETURN_URL_KEY, "return");
//		sParaTemp.put(GatewayConstants.NOTIFY_URL_KEY, "notify");
//		sParaTemp.put(GatewayConstants.INPUT_CHARSET, "UTF_8");

		String strButtonName = "OK";

		return GatewayStripeRefundSubmit.buildUrl(sParaTemp, "https://" + svcName+GatewayStripeRefundConfig.common_gateway_url, "get",
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
		
		return GatewayStripeRefundNotify.verifyNotify(fields);
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
		Map<String, String> sParaNew = GatewayStripeRefundCore.paraFilter(fields);
		
		if(GatewayStripeRefundCore.verifySign(sParaNew, signStr, sign_type)){
			return true;
		}else{
			return false;
		}
	}
}
