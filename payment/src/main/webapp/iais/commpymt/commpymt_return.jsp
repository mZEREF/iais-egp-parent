<%@page import="sop.i18n.Language"%>
<%@page import="sop.iwe.SessionManager"%>
<%@page import="sop.iwe.SessionManager.LoginInformation"%>
<%@page import="com.ecquaria.egp.core.helper.CurrencyHelper"%>
<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayConstants"%>
<%@page import="com.ecquaria.egp.core.payment.api.util.GatewayCore"%>
<%@page import="com.ecquaria.egp.core.payment.api.services.GatewayAPI"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.util.Enumeration"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setLayout name="none"/>

<%
    request.setAttribute("egp-payment-include.jsp.INCLUDED", Boolean.TRUE);
    //Set Language
    LoginInformation loginInfo = SessionManager.getInstance(request).getLoginInfo();
    String charset = "UTF-8";
    String textDirection = "LTR";
//    if (loginInfo != null) {
//        Language lang = loginInfo.getLanguage();
//        charset = lang.getCharset();
//        textDirection = lang.getTextDirection();
//    }
    
    pageContext.setAttribute("charset", charset);
    pageContext.setAttribute("textDirection", textDirection);

    response.setContentType("text/html; charset=" + charset);
%>

<meta http-equiv="Content-Type" content="text/html; charset=<c:out value='${charset}' />" />

<%
	Map<String, String> fields = new HashMap<String, String>();
	for (Enumeration<String> enum3 = request.getParameterNames(); enum3.hasMoreElements();) {
	    String fieldName = (String) enum3.nextElement();
	    String fieldValue = request.getParameter(fieldName);
	    if ((fieldValue != null) && (fieldValue.length() > 0)) {
	        fields.put(fieldName, fieldValue);
	    }
	}
	
	String signStr = fields.remove(GatewayConstants.SIGN_KEY);
	String signType = fields.remove(GatewayConstants.SIGN_TYPE_KEY);
	
	String gateway_ref_no = fields.get(GatewayConstants.CPS_REFNO);
	String input_charset = fields.get(GatewayConstants.INPUT_CHARSET);
	String pymt_status = fields.get(GatewayConstants.PYMT_STATUS);
	String ref_no = fields.get(GatewayConstants.SVCREF_NO);
	String amount = fields.get(GatewayConstants.AMOUNT_KEY);

	if(!GatewayAPI.verifyParameter(request)){
    	throw new Exception("Invalid payment return parameter.");
    }
%>
<%=gateway_ref_no%><br/>
<%=pymt_status%><br/>
<%=ref_no%><br/>
<%=CurrencyHelper.formatCurrency(Double.valueOf(amount)) %><br/>