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
  <form id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
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

      <div style="display: none" id="cgo">

        <%@include file="cgo.jsp"%>

      </div>

      <div class="form-check-gp" id="selectHciNameAppeal" style="display: none">
        <h2>Select HCI Name To Appeal</h2>
        <div class="form-check" >
          <input class="form-check-input"  onclick="isCheck(this)" type="checkbox" name="selectHciName" aria-invalid="false" value="11">
          <label class="form-check-label"><span class="check-square"></span>Default</label>
        </div>

        <div class="col-xs-12 col-md-10" id="proposedHciName" style="display: none" >
          <h2>Proposed  HCI Name</h2>
          <input type="text" maxlength="100" name="proposedHciName">
        </div>

      </div>

  </div>

<div class="col-xs-12 col-md-10">

  <h2>Any supporting remarks</h2>
  <textarea cols="50" rows="10" name="remarks" maxlength="300"></textarea>

</div >
  <div class="col-xs-12 col-md-10">
    <div>
      <label>File Upload for Appeal Reasons</label><br>
      <div class="text-center col-xs-12">
        <div class="document-upload-list">
          <div class="file-upload-gp">
            <div class="fileContent col-xs-12">
              <input class="selectedFile" id="selectedFile" name = "${docConfig.id}selectedFile" type="file" style="display: none;" aria-label="selectedFile1">
              <a class="btn btn-file-upload btn-secondary" >Upload</a>
            </div>
          </div>
        </div>
      </div>


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
  if("MS003"==reason){
      $('#cgo').attr("style" ,"display: block");

  }else  {
      $('#cgo').attr("style" ,"display: none");

  }
  if("MS004"==reason){
    $('#selectHciNameAppeal').attr("style","display: block");

    // $('#idChecked').click(function () {
      // if($('#idChecked').prop("checked")){
      //   $('#proposedHciName').attr("style","display: block");
      // }else {
      //
      // }
      // });
  }else {
      $('#selectHciNameAppeal').attr("style","display: none");
  }
    
});


function isCheck(obj) {

  if($(obj).prop("checked")){
      $('#proposedHciName').attr("style","display: block");
  }else {
      $('#proposedHciName').attr("style","display: none");
  }

}


$('.selectedFile').change(function () {
    var file = $(this).val();
    $(this).parent().children('div:eq(0)').children('span:eq(0)').html(getFileName(file));
    $(this).parent().children('div:eq(0)').removeClass('hidden');
    $fileUploadContentEle = $(this).closest('div.file-upload-gp');
    $fileUploadContentEle.find('.delBtn').removeClass('hidden');
});

</script>

</>

