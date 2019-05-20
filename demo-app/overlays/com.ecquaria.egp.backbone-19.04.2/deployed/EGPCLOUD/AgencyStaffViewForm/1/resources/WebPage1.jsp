<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib prefix="egov-smc" uri="ecquaria/sop/egov-smc" %>
<webui:setLayout name="egp-blank"/>
<%

  response.setContentType("text/html;charset=UTF-8");
//handle to the Engine APIs
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
String proposedName = (String)request.getAttribute("proposedName");
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
<script type="text/javascript">

  function fillForm(){
      var originalUrl = parent.location.href;
      var redirectUrl = originalUrl.substring(0, originalUrl.indexOf("OWASP_CSRFTOKEN") - 1);
      document.getElementById("redirectForm").action = redirectUrl;
      document.getElementById("proceed").click();
  }

  window.onload = fillForm;

</script>
<div class="alert-success">
  <p>
    <egov-smc:message key="egpFormSaved">EGP Form changes have been saved.</egov-smc:message>
  </p>
</div>

<form id="redirectForm" method="post" action="" target="_parent">
    <input id="proceed" type="submit" class="small blue" value="Proceed" style="display:none;" />
</form>
