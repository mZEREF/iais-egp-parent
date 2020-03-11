<%@ page import="com.ecquaria.cloud.ServerConfig" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<webui:setLayout name="none" />
<%
    String sURL = ServerConfig.getInstance().getSopSiteURL();
    pageContext.setAttribute("sURL", sURL);
%>
<meta http-equiv="refresh" content="0;url=<c:out value='${sURL}'/>">