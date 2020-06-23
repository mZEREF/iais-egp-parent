<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayConfig"%>
<%@page import="com.ecquaria.egp.core.payment.api.util.GatewaySubmit"%>
<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayConstants"%>
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
	String svc_refno = request.getParameter("svc_refno");
	String pymt_status = request.getParameter("pymt_status");
	String cps_refno = request.getParameter("cps_refno");
	String amount = request.getParameter("amount");
	Map<String, String> fieldMap = new HashMap<String, String>();
	fieldMap.put(GatewayConstants.PYMT_STATUS, pymt_status);
	fieldMap.put(GatewayConstants.SVCREF_NO, svc_refno);
	fieldMap.put(GatewayConstants.CPS_REFNO, cps_refno);
	fieldMap.put(GatewayConstants.ACTION_KEY, GatewayConstants.ACTION_UPDATE);
	fieldMap.put(GatewayConstants.REGISTRY_NAME_KEY, GatewayConfig.payment_registry_name);
	fieldMap.put(GatewayConstants.INPUT_CHARSET, GatewayConfig.input_charset);
	fieldMap.put(GatewayConstants.AMOUNT_KEY, amount);
%>
<%=GatewaySubmit.buildForm(fieldMap, GatewayConfig.common_gateway_service_url, "get", "OK")%>