<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%
	String continueUrl = (String) request.getAttribute("continueUrl");
	//System.out.println("=============== continueUrl: "+continueUrl);
	RedirectUtil.redirect(continueUrl, request, response);
%>
