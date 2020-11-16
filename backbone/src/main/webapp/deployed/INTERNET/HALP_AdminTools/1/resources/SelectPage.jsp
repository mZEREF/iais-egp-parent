<%
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="none"/>
<form method="post" action="<%=process.runtime.continueURL()%>">
<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <table>
    <tr>
      <td>Function:</td>
    </tr>
    <tr>
      <td><input type="radio" name="functionType" value="BOW">Browser</td>
    </tr>
    <tr>
      <td></td><td><input type="submit" value="Run"></td>
    </tr>
  </table>
</form>