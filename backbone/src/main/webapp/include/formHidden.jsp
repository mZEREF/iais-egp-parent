<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass proce =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<input type="hidden" name="sopEngineTabRef" value="<%=proce.rtStatus.getTabRef()%>">
<input type="hidden" name="iaisErrorFlag" id="iaisErrorFlag"/>
