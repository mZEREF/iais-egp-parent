<%@ include file="/fams2/commons/imports.jsp"%>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
  (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setAttribute name="title">
<%=process.runtime.getCurrentComponentName()%>
</webui:setAttribute>

<form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
<input type="hidden" name="act" id="act"/>
<%@include file="/fams2/***.jsp"%>
</form>
