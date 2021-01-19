package com.ecquaria.cloud.moh.iais.api.util;


import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
    public static String buildUrl(Map<String, String> sParaTemp, String gateway, String strMethod,
                                   String strButtonName) throws Exception {
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        StringBuilder url = new StringBuilder();
        url.append(gateway);
        appendQueryFields(url,sPara);

        return url.toString();
    }
    /**
     * This method is for creating a URL query string.
     *
     * @param bud
     *            is the inital URL for appending the encoded fields to
     * @param fields
     *            is the input parameters from the order page
     * @throws UnsupportedEncodingException
     */
    // Method for creating a URL query string
    private static void appendQueryFields(StringBuilder bud, Map<String, String> fields) throws UnsupportedEncodingException {

        // create a list
        List<String> fieldNames = new ArrayList<String>(fields.keySet());
        Collections.sort(fieldNames);

        Iterator<String> itr = fieldNames.iterator();

        // move through the list and create a series of URL key/value pairs
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);

            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // append the URL parameters
                bud.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.name()));
                bud.append('=');
                bud.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.name()));
                // add a '&' to the end if we have more fields coming.
                if (itr.hasNext()) {
                    bud.append('&');
                }
            }
        }
        // remove the end char '&'
        int index = bud.length()-1;
        if("&".equals(bud.substring(index))){
            bud.delete(index, index+1);
        }
    }
}

