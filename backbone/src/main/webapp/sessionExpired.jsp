<%-- <%@ taglib uri="/workdesk.tld" prefix="workdesk"%> --%>
<%@page import="com.ecquaria.cloud.helper.EngineHelper"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%-- <%@include file="/system/sopCommonInclude.jsp"%> --%>
<webui:setLayout name="none" />
<%
	response.setContentType("text/html;charset=UTF-8");
%>
<workdesk:baseName name="smc-client">

<link rel="stylesheet" href="<%=EngineHelper.getResourcePath() %>/_statics/login/css/screen.css" type="text/css"/>
<link rel="shortcut icon" href="<%=EngineHelper.getResourcePath() %>/_statics/images/ecq.gif" type="image/x-icon" />
<html>
<workdesk:i18nMeta />
    <head>
    </head>
    <body class="login-smc">
	
    <br>
	<center>
	<table class="session-missing">
		<tbody>
		<tr><td>
			<form name="redirectForm" action="/egov" method="post">
				<SPAN class="error-message"><egov-smc:message key="sessionExpired">Session expired</egov-smc:message></SPAN>
				<BR><BR><BR>
				<egov-smc:commonLabel>Please click</egov-smc:commonLabel> <A HREF="#" onclick="redirectForm.submit();"><font color="orange"><b><I><egov-smc:commonLabel>HERE</egov-smc:commonLabel></I></b></font></A> <egov-smc:message key="toReLogin">to re-login.</egov-smc:message>
			</form>
		</td></tr>
		</tbody>
	</table>
	</center>
    <BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>
    </body>
</html>
</workdesk:baseName>
