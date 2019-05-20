<%@page import="com.ecquaria.egp.core.helper.OTPHelper"%>
<%@page import="sop.rbac.user.User"%>
<%@page import="sop.iwe.SessionManager"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%
/*
  You can customize this default file:
  /D:/eclipse/plugins/com.ecquaria.eclipse.sit_6.1.1/WebPage.jsp.default
*/

//handle to the Engine APIs
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="none"/>
<webui:setAttribute name="header-ext">
<%
/* You can add additional content (SCRIPT, STYLE elements)
 * which need to be placed inside HEAD element here.
 */
%>
</webui:setAttribute>

<webui:setAttribute name="title">
<%
/* You can set your page title here. */
%>

<%=process.runtime.getCurrentComponentName()%>

</webui:setAttribute>
<form method="post" action=<%=process.runtime.continueURL()%> target="main">
User Id: <input type="text" name="userId"/>
<input type="submit" value="Submit">
<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
</form>
<iframe name="main">

</iframe>