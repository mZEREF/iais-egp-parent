<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<webui:setLayout name="iais-internet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%@include file="./dashboard.jsp" %>
<div class="container" >
  <form id="mainForm" method="post" style="margin-left: 3%" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="row" style="margin-left:5px">
      <h2>${INBOX_ERR001}</h2>
    </div>
    <div class="row">
      <div class="text-right text-center-mobile" style="margin-bottom: 1%">
        <a class="btn btn-primary" href="/main-web/eservice/INTERNET/MohInternetInbox" id="dashboard">Go to Dashboard</a>
      </div>
    </div>
  </form>
</div>
<style>

</style>


</>
