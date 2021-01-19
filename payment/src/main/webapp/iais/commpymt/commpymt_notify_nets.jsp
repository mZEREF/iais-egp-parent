<%@page language="java" contentType="application/json; charset=utf-8" %>
<%@ page import="com.ecquaria.egp.core.payment.api.services.GatewayNetsAPI"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%%><webui:setLayout name="none"/>
<%=GatewayNetsAPI.verifyNotify(request)%>