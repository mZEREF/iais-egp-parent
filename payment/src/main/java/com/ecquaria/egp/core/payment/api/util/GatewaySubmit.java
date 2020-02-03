package com.ecquaria.egp.core.payment.api.util;

import com.ecquaria.egp.core.payment.api.config.GatewayConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GatewaySubmit {

    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) throws Exception {

    	Map<String, String> sPara = GatewayCore.paraFilter(sParaTemp);
    	String mysign = GatewayCore.buildSign(sPara, GatewayConfig.sign_type);
        
        sPara.put("sign", mysign);
        sPara.put("sign_type", GatewayConfig.sign_type);

        return sPara;
    }

    public static String buildForm(Map<String, String> sParaTemp, String gateway, String strMethod,
                                   String strButtonName) throws Exception {
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"gatewaysubmit\" name=\"gatewaysubmit\" action=\"" + gateway
                      + "_input_charset=" + GatewayConfig.input_charset + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['gatewaysubmit'].submit();</script>");

        return sbHtml.toString();
    }
}

