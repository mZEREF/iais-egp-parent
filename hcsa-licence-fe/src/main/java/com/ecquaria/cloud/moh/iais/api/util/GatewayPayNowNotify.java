package com.ecquaria.cloud.moh.iais.api.util;


import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayPayNowConfig;

import java.util.HashMap;
import java.util.Map;

public class GatewayPayNowNotify {

	public static String verifyNotify(Map<String, String> fields) throws Exception{

		String json = "";

		boolean verified = true;

		Map<String, String> sParaNew = GatewayPayNowCore.paraFilter(fields);
		String signType = fields.get(GatewayConstants.SIGN_TYPE_KEY);

		String sign = fields.get(GatewayConstants.SIGN_KEY);

		if(sign == null) {sign = "";}

		if(signType != null && (signType.equals(GatewayConstants.SIGN_TYPE_MD5) || signType.equals(GatewayConstants.SIGN_TYPE_RSA)))
			verified = GatewayPayNowCore.verifySign(sParaNew, sign, signType);

		if(!verified){
			throw new Exception("Invalid notify request parameter.");
		}else{
			Map<String, String> map = new HashMap<>();
			StringBuilder sb = new StringBuilder();

			map.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayPayNowConfig.payment_registry_name);
			map.put(GatewayConstants.CPS_REFNO, fields.get(GatewayConstants.CPS_REFNO));
			map.put(GatewayConstants.SVCREF_NO, fields.get(GatewayConstants.SVCREF_NO));
			map.put(GatewayConstants.INPUT_CHARSET, fields.get(GatewayConstants.INPUT_CHARSET));
			map.put(GatewayConstants.NOTIFY_STATUS, "success");
			String ressign = "";
			if(signType != null && (signType.equals(GatewayConstants.SIGN_TYPE_MD5) || signType.equals(GatewayConstants.SIGN_TYPE_RSA)))
				ressign = GatewayPayNowCore.buildSign(map, signType);

			sb.append('{').append('\"' + GatewayConstants.REGISTRY_NAME_KEY + "\":\"").append(GatewayPayNowConfig.payment_registry_name).append("\",")
					.append('\"' + GatewayConstants.NOTIFY_STATUS + "\":").append("\"success\",")
					.append('\"' + GatewayConstants.CPS_REFNO + "\":").append('\"').append(fields.get(GatewayConstants.CPS_REFNO)).append("\",")
					.append('\"' + GatewayConstants.SVCREF_NO + "\":").append('\"').append(fields.get(GatewayConstants.SVCREF_NO)).append("\",")
					.append('\"' + GatewayConstants.INPUT_CHARSET + "\":").append('\"').append(fields.get(GatewayConstants.INPUT_CHARSET)).append("\",")
					.append("\"sign\":\"").append(ressign).append("\",\"sign_type\":\"")
					.append(GatewayPayNowConfig.sign_type).append("\"}");

			json = sb.toString();
		}

		return json;
	}
}
