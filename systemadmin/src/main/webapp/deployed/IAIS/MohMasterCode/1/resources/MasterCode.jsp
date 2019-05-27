<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%


//handle to the Engine APIs
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
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
<jsp:include page = "WEB-INF/jsp/mastercode.jsp" flush="true"></jsp:include>

<form method="post" class="__egovform" action=<%=process.runtime.continueURL()%>>
<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
</form>
