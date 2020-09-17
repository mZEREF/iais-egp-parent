package com.ecquaria.cloud.moh.iais.api.util;


import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GatewayStripeSubmit {

    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) throws Exception {

    	Map<String, String> sPara = GatewayStripeCore.paraFilter(sParaTemp);
    	String mysign = GatewayStripeCore.buildSign(sPara, GatewayConfig.sign_type);
        
        sPara.put("sign", mysign);
        sPara.put("sign_type", GatewayConfig.sign_type);

        return sPara;
    }

    public static String buildForm(Map<String, String> sParaTemp, String gateway, String strMethod,
                                   String strButtonName) throws Exception {
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuilder sbHtml = new StringBuilder();

        sbHtml.append("<form id=\"GatewayStripeSubmit\" name=\"GatewayStripeSubmit\" action=\"").append(gateway).append("_input_charset=").append(GatewayConfig.input_charset).append("\" method=\"").append(strMethod).append("\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"").append(name).append("\" value=\"").append(value).append("\"/>");
        }

        sbHtml.append("<input type=\"submit\" value=\"").append(strButtonName).append("\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['GatewayStripeSubmit'].submit();</script>");

        return sbHtml.toString();
    }
}

