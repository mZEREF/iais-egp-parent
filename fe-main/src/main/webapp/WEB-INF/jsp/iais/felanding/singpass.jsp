<%@ page import="com.ncs.secureconnect.sim.lite.SIMUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.LoginHelper" %>
<%!
  static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("singpass.jsp");
%>
<%
  try {
    SIMUtil.doSingPassLogin(request, response);
  } catch (Exception e) {
    logger.error(e.getMessage(),e);
    out.println("<br><b>Error initializing Login </b></br>");
    IaisEGPHelper.sendRedirect(request, response, LoginHelper.MAIN_WEB_URL);
  }
%>
