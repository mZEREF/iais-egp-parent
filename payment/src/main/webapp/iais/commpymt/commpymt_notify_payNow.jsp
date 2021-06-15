<%@page language="java" contentType="application/json; charset=utf-8" %>
<%@ page import="com.ecquaria.egp.core.payment.api.services.GatewayStripeAPI"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%%><webui:setLayout name="none"/>
<%=GatewayStripeAPI.verifyNotify(request)%>