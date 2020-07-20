<%--
<%@ page import="com.ncs.secureconnect.sim.lite.SIMUtil4Corpass" %>
<%@ page import="sg.gov.msf.ecda.ems.admin.sudoEASY.SudoEASYCorporation" %>
<%!
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("corpPassLogin.jsp");
%>
<%
    if (SudoEASYCorporation.isEnableCorpPass()) {
        try {
            SIMUtil4Corpass.doCorpPassLogin(request, response);
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            out.println("<br><b>Error initializing Login </b></br>");
            return;
        }
    } else {
%>
    <jsp:forward page="/system/logout.jsp"></jsp:forward>
<%        
    }
%>--%>
