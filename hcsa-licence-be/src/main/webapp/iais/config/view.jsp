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
    <div class="col-lg-12 col-xs-10">
    <div class="bg-title" style="text-align: center">
      <h2>  HCSA Configurator Module</h2>
    </div>
      <div class="form-group">
      <div class="col-xs-12 col-md-10">
        <h2 class="component-title">Preview HCSA Service Edit</h2>
      </div>
      </div>
      <div class="form-group">
      <div class="col-xs-12 col-md-8">
        <label class="col-xs-12 col-md-8 control-label" for="serviceName">Service Name<span class="mandatory">*</span></label>
        <div class="col-xs-12 col-md-4">
          <input id="serviceName" type="text">
        </div>
      </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="description">Service Description<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" type="text">
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

</script>
</>
