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
	String amount = request.getParameter("amount");
	String description = request.getParameter("description");
	Map<String, String> fieldMap = new HashMap<String, String>();
	fieldMap.put(GatewayConstants.AMOUNT_KEY, amount);
	fieldMap.put(GatewayConstants.PYMT_DESCRIPTION_KEY, description);
	fieldMap.put(GatewayConstants.SVCREF_NO, svc_refno);
%>
<%=GatewayAPI.create_partner_trade_by_buyer(fieldMap,request)%>