<%--
<%@ page import="com.ncs.secureconnect.sim.lite.SIMUtil" %>
<%!
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("singpassLogin.jsp");
%>
<%
    try {
		SIMUtil.doSingPassLogin(request, response);
    } catch (Exception e) {
    	logger.error(e.getMessage(),e);
		out.println("<br><b>Error initializing Login </b></br>");
        return;
    }
%>
--%>
