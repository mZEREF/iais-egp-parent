<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="ecq.commons.helper.StringHelper" %>
<%@ page import="sop.audit.SOPAuditLogConstants" %>
<%@ page import="sop.audit.SOPAuditLog" %>
<%@ page import="com.ecquaria.cloud.ServerConfig" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<webui:setLayout name="none" />
<%
    String sURL = ServerConfig.getInstance().getSopSiteURL();
    String sUserDomain = sop.iwe.SessionManager.getInstance(request)
            .getCurrentUserDomain();


    if (session != null) {
        sop.rbac.user.UserIdentifier userIden = new  sop.rbac.user.UserIdentifier();
        sop.iwe.SessionManager session_mgmt = sop.iwe.SessionManager.getInstance(request);
        String userdomain = session_mgmt.getCurrentUserDomain();
        String userid=session_mgmt.getCurrentUserID();
        if(!StringHelper.isEmpty(userid) && !StringHelper.isEmpty(userdomain)){
	        userIden.setUserDomain(userdomain);
	        userIden.setId(userid);
	        String event = SOPAuditLogConstants.getLogEvent(
	                SOPAuditLogConstants.KEY_LOGOUT,
	                new String[] { userIden.getUserDomain() });
	
	        String eventData = SOPAuditLogConstants.getLogEventData(
	                SOPAuditLogConstants.KEY_LOGOUT, new String[] { userIden.getUserDomain(),userIden.getId() });
	
	        SOPAuditLog.log(userIden, event, eventData,
	                SOPAuditLogConstants.MODULE_LOGOUT);
        }
        session.invalidate();
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            Cookie cookie = request.getCookies()[0];
            cookie.setMaxAge(0);
        }
    }
    pageContext.setAttribute("sURL", sURL);
%>
<meta http-equiv="refresh" content="0;url=<c:out value='${sURL}'/>">