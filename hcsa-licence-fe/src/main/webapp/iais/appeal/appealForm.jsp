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
    <div>
      <h1>Appeal Form</h1>
    </div>
  <div class="form-group">
    <div class="col-xs-12 col-md-10">
      <h2>You are appealing for:</h2>
    </div>

    <div class="col-xs-12 col-md-5">
      <input type="text" name="">
    </div>
  </div>
  <div class="col-xs-12 col-md-10">
    <h2>Reason for Appeal</h2>
    <select>
      <option>please select an appeal reason</option>
      <option>Appeal against rejection</option>
      <option>Appeal against late renewal fee</option>
    </select>

  </div>
<div class="col-xs-12 col-md-10">

  <h2>Any supporting remarks</h2>
  <textarea cols="50" rows="10"></textarea>

</div >
  <div class="col-xs-12 col-md-10">
    <div>
      <label>File Upload for Appeal Reasons</label><br>
      <button >Browse File</button>
    </div>

  </div>
    <br>
    <br> <br> <br>
    <div class="application-tab-footer">
      <div class="row">
        <div class="col-xs-12 col-sm-10">
          <div class="text-right text-center-mobile">
            <a class="btn btn-primary" href="#" id="save">Save</a>
            <a class="btn btn-primary" href="#" id="submit">Submit</a>
            <a class="btn btn-primary" href="#" id="cancel">Cancel</a>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<script>

$('#submit').click(function () {

    SOP.Crud.cfxSubmit("mainForm", "submit");

});

</script>

</>

