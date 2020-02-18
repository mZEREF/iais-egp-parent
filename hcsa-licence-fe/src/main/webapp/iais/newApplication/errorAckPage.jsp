<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-blank"/>
<form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  error !!!!
</form>


<script type="text/javascript">



</script>