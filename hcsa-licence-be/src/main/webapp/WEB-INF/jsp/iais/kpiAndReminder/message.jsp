<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<div class="main-content">
<form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <div class="bg-title" style="margin-top:1%;margin-left: 1%  ">

      <h2>Acknowledgement</h2>


  </div>
  <input style="display: none" value="${message}" id="message">
  <label class="complete" style="margin-left: 1%"></label>

  <div class="application-tab-footer">
    <div class="row">
      <div class="col-xs-12 col-sm-10">
        <div class="text-right text-center-mobile">

          <a class="btn btn-primary" href="#" id="cancel">Cancel</a>

        </div>
      </div>
    </div>
  </div>

</form>
</div>

<script type="text/javascript">
  $(document).ready(function () {
      $("#message +label").html($('#message').val());
  });

  $('#cancel').click(function () {
      SOP.Crud.cfxSubmit("mainForm", "cancel");
  });
</script>
</>
