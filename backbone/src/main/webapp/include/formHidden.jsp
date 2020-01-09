<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass proce =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<input type="hidden" name="sopEngineTabRef" value="<%=proce.rtStatus.getTabRef()%>">
<input type="hidden" name="iaisErrorFlag" id="iaisErrorFlag"/>
<input type="hidden" name="crud_action_type" id="crud_action_type"/>
<input type="hidden" name="crud_action_value" id="crud_action_value"/>
<input type="hidden" name="crud_action_additional" id="crud_action_additional"/>
