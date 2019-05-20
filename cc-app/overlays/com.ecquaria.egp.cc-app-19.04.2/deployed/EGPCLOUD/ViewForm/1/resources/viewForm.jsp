
<%@page import="com.ecquaria.cloud.mc.application.Application"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%
/*
  You can customize this default file:
  /D:/DevKit/eclipse/plugins/com.ecquaria.eclipse.sit_6.1.1/WebPage.jsp.default
*/

//handle to the Engine APIs
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
Application app = (Application)request.getAttribute("entity");
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

<jsp:include page="/WEB-INF/jsp/inc/egovform-view-include.jsp"/>
<%
	String appNo = app.getNo();
	if(StringHelper.isEmpty(appNo)){
		appNo = "";
	}
%>
<input type="hidden" name="appNo" value="<%=appNo%>">
<%=new String(app.getFormHtml()) %>
