<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
<input type="hidden" id = "premisesTypeValue" value="${appGrpPremisesDto.premisesType}">
<input type="hidden" id = "baseContextPath" value="<%=request.getContextPath()%>">