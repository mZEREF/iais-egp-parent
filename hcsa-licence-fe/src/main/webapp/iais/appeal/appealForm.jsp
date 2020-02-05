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
      <input type="text" name="appealingFor">
    </div>
  </div>
  <div class="col-xs-12 col-md-8" >
    <h2>Reason for Appeal</h2>
    <select id="reasonSelect" name="reasonSelect">
      <option value="">please select an appeal reason</option>
      <option value="MS001">Appeal against rejection</option>
      <option value="MS002">Appeal against late renewal fee</option>
      <option value="MS003">Appeal for appointment of additional CGO to a service</option>
      <option value="MS004">Appeal against use of restricted words in HCI Name</option>
      <option value="MS005">Appeal for change of licence period</option>

    </select>

      <div >

        <%@include file="cgo.jsp"%>

      </div>





      <div class="form-check-gp">
        <h2>Select HCI Name To Appeal</h2>
        <div class="form-check" >
          <input class="form-check-input"  type="checkbox" name="selectHciName" aria-invalid="false" value="">
          <label class="form-check-label"><span class="check-square"></span>Default</label>
        </div>

        <div class="form-check">
          <input class="form-check-input" type="checkbox" name="selectHciName" aria-invalid="false" value="">
          <label class="form-check-label" ><span class="check-square"></span>Default</label>
        </div>

      </div>

  </div>

    <div class="col-xs-12 col-md-10" >
      <h2>Proposed  HCI Name</h2>
      <input type="text" maxlength="100" name="proposedHciName">
    </div>
<div class="col-xs-12 col-md-10">

  <h2>Any supporting remarks</h2>
  <textarea cols="50" rows="10" name="remarks" maxlength="300"></textarea>

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
<script  type="text/javascript">

$('#submit').click(function () {

    SOP.Crud.cfxSubmit("mainForm", "submit");

});


$('#save').click(function () {
    SOP.Crud.cfxSubmit("mainForm", "save");
});

$('#reasonSelect').change(function () {

  var reason= $('#reasonSelect option:selected').val();


});

</script>

</>

