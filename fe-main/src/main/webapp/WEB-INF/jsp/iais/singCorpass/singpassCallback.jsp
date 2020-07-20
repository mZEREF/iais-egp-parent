<%--
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="sg.gov.msf.ecda.ems.common.util.StringUtil"%>
<%@page import="sg.gov.msf.ecda.ems.common.SOPLoginUtil"%>
<%@page import="ecq.commons.config.Config"%>
<%@page import="sg.gov.msf.ecda.ems.common.util.ParamUtil"%>
<%@ page import="com.ncs.secureconnect.sim.lite.SIMUtil, 
com.ncs.secureconnect.sim.common.LoginInfo,
ncs.secureconnect.sim.entities.Constants" %>

<html>
<head>
    <title>eService Application</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>

<body>
<%
	String samlArt = (String)request.getParameter(Constants.SAML_ART);
	String pass = (String)ParamUtil.getString(request, "pass");
	if ("true".equals(Config.get("test.mode"))) {
	    SOPLoginUtil.testSingpassLogin(request, response);
	} else {
   	 	LoginInfo oLoginInfo = SIMUtil.doSingPassArtifactResolution(request, samlArt);
		if (oLoginInfo != null) {
			ParamUtil.setSessionAttr(request, "singpass.loginfo.userid",oLoginInfo.getUserID());
%>
	    <br><b>User ID</b>:<%= oLoginInfo.getUserID() %>
 <%
	        response.sendRedirect(SIMUtil.getHomePage());
        }
    }
	out.println("<br><b><a href=\"/eService/sim/logout\">Logout SingPass</a></b><br>");
%>

<br></br>
</body>
</html>
--%>
