<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%
	RedirectUtil.redirect(request.getContextPath() + "/process/EGPCLOUD/MyMessageManagement", request, response);
	//response.sendRedirect("/cc-ui/process/DEMOPROJECT/JustTestService/");
%>