<%@ page import="com.ncs.secureconnect.sim.lite.SIMUtil4Corpass" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.LoginHelper" %>
<%
    try {
      SIMUtil4Corpass.doCorpPassLogin(request, response);
    } catch (Exception e) {
      out.println("<br><b>Error initializing Login </b></br>");
      IaisEGPHelper.sendRedirect(request, response, LoginHelper.MAIN_WEB_URL);
    }
%>