<%--
<%@ page import="com.ncs.secureconnect.sim.lite.SIMUtil4Corpass" %>

<%!
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("corpPassLogout.jsp");
%>
<%

    try {
        SIMUtil4Corpass.doCorpPassLogout(request, response);
    } catch (Exception e) {
    	logger.error(e.getMessage(),e);
        out.println("<br><b>Error initializing Login </b></br>");
        return;
    }


%>

--%>
