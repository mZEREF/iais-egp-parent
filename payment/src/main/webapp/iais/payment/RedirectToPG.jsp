<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayConstants"%>
<%@page import="com.ecquaria.egp.core.payment.api.services.GatewayAPI"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setLayout name="none"/>
<%
  String svc_refno = request.getParameter("svc_refno");
  svc_refno ="test";
  String amount = request.getParameter("amount");
  amount = "100";
  String description = request.getParameter("description");
  description = "test";
  Map<String, String> fieldMap = new HashMap<String, String>();
  fieldMap.put(GatewayConstants.AMOUNT_KEY, amount);
  fieldMap.put(GatewayConstants.PYMT_DESCRIPTION_KEY, description);
  fieldMap.put(GatewayConstants.SVCREF_NO, svc_refno);
%>
<%=GatewayAPI.create_partner_trade_by_buyer(fieldMap)%>