<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="dashboard.jsp" %>
<%@include file="../common/dashboard.jsp" %>
<br/>
<div class="main-content">
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type_form_value" value="">
  <input type="hidden" name="crud_action_type_value" value="">

  <div class="form-check row" style="margin-left: 2%" >
    <div class="col-xs-10">
      <input class="form-check-input"   type="checkbox"  name="licenceName" aria-invalid="false" value="">
      <label class="form-check-label"><span class="check-square"></span>L/20CLB0273/CLB/002/201</label>
    </div>
  </div>
  <div class="form-check row"  >
    <div class="col-xs-2" >
      <a class="btn btn-primary" id="amendmentSubmitPremises">submit</a>
    </div>
  </div>



</form>
</div>
<script >
  $('#amendmentSubmitPremises').click(function () {

      SOP.Crud.cfxSubmit("menuListForm", "prepareAckPage","","");

  });
</script>
