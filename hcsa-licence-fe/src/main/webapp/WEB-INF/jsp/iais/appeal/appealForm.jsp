<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<webui:setLayout name="iais-internet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<%@include file="./dashboard.jsp" %>
<div class="container">
  <form id="mainForm" enctype="multipart/form-data"  class="__egovform" method="post" action=<%=process.runtime.continueURL()%> >
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" id="configFileSize" value="${configFileSize}"/>

  <div class="form-group">
    <div class="col-xs-12 col-md-10" style="margin-left: 2%">
      <label style="font-size: 25px">You are appealing for:</label>
    </div>

    <div  class="col-xs-12 col-md-10">
      <div class="col-xs-12 col-md-6" style="margin-left: 1%">
        <input type="text" name="appealingFor" disabled  value="${appealNo}" >
        <span name="iaisErrorMsg" class="error-msg" id="error_submit"></span>
      </div>
    </div>
  </div>

    <div class="form-group">
      <div class="col-xs-12 col-md-10" style="margin-left: 1%">
        <div class="col-xs-12 col-md-6" style="margin-bottom: 20px">
          <label style="font-size: 25px">Reason For Appeal<span class="mandatory"> *</span></label>
          <select id="reasonSelect" name="reasonSelect" style="margin-left: 2%">
            <option value="">Please Select</option>
          <c:if test="${type=='application'}"><c:if test="${applicationAPPROVED=='APPROVED'}">
            <option value="MS001" <c:if test="${appPremiseMiscDto.reason=='MS001'}">selected="selected"</c:if> >Appeal against rejection</option></c:if>
          </c:if>
          <c:if test="${lateFee==true}">
            <option value="MS002" <c:if test="${appPremiseMiscDto.reason=='MS002'}">selected="selected"</c:if>>Appeal against late renewal fee</option>
          </c:if>
            <c:if test="${maxCGOnumber==true}">
              <c:if test="${type=='application'}">
                <option value="MS003" <c:if test="${appPremiseMiscDto.reason=='MS003'}">selected="selected"</c:if>>Appeal for appointment of additional CGO to a service</option>
              </c:if>
            </c:if>

          <c:if test="${type=='application'}"><option value="MS008" <c:if test="${appPremiseMiscDto.reason=='MS008'}">selected="selected"</c:if>>Appeal against use of restricted words in HCI Name</option></c:if>
          <c:if test="${type=='licence'}"> <option value="MS004" <c:if test="${appPremiseMiscDto.reason=='MS004'}">selected="selected"</c:if>>Appeal for change of licence period</option></c:if>
             <option value="MS007" <c:if test="${appPremiseMiscDto.reason=='MS007'}">selected="selected"</c:if>>Others</option>
             <%--<option value="MS006" <c:if test="${appPremiseMiscDto.reason=='MS006'}">selected="selected"</c:if>>Appeal against revocation</option>--%>
          </select>

          <div style="margin-top: 1%"> <span  class="error-msg" name="iaisErrorMsg" id="error_reason"></span></div>


          <div class="col-xs-12 col-md-10" id="othersReason" style="display: none" >
            <label style="font-size: 20px;margin-top: 1%">Others reason</label>
            <input type="text" maxlength="100"   name="othersReason" value="${appPremiseMiscDto.otherReason}" >
            <span class="error-msg" name="iaisErrorMsg" id="error_otherReason"></span>
          </div>

          <div class="form-check-gp" id="selectHciNameAppeal" style="display: none" class="col-xs-12 col-md-6">
            <label style="font-size: 20px">Select HCI Name To Appeal</label>
            <c:forEach items="${hciNames}" var="hciName" >
              <div >
                <div class="form-check" >
                  <input class="form-check-input"  onclick="isCheck(this)" type="checkbox" <c:if test="${fn:length(hciNames)==1}">checked="checked" </c:if> name="selectHciName" aria-invalid="false" value="${hciName}">
                  <label class="form-check-label"><span class="check-square"></span>${hciName}</label>
                </div>

                <div class="col-xs-12 col-md-10" id="proposedHciName" style="display: none" >
                  <label style="font-size: 20px">Proposed  HCI Name</label>
                  <input type="text" maxlength="100" name="proposedHciName" value="${appPremiseMiscDto.newHciName}">
                  <span ></span>
                </div>
              </div>

            </c:forEach>
          </div>

        </div>

      </div>
      </div>
    <div style="display: none;margin-top: 10px;margin-left: 1%" id="cgo" class="col-xs-12 col-md-9" >
      <%--     <a class="btn  btn-secondary" onclick="deletes()" style="margin-left: 20px;"  >delete</a>--%>
      <%@include file="cgo.jsp"%>

    </div>
      <div class="col-xs-12 col-md-10" style="margin-left: 2%">

        <label style="font-size: 25px">Any supporting remarks<span class="mandatory"> *</span></label>

      </div >
    <div  class="col-xs-12 col-md-10" style="margin-left: 2%" >

      <textarea cols="120" style="font-size: 20px" rows="10" name="remarks" maxlength="300" >${appPremiseMiscDto.remarks}</textarea>

      <div> <span class="error-msg" id="error_remarks" name="iaisErrorMsg"></span></div>

    </div>


    <div class="form-group">
  <div >
    <div>
      <div class="col-xs-12 col-md-10" >
        <label style="font-size: 25px;margin-top: 25px;margin-left: 1%" >File Upload For Appeal Reasons</label>
      </div>

      <div class="col-xs-12">
        <div class="document-upload-list">
          <div class="file-upload-gp">
            <div class="fileContent col-xs-2">
              ${upFile.originalFilename}
              <input class="selectedFile"  id="selectedFile" name = "selectedFile"  type="file" style="display: none;" aria-label="selectedFile1" onchange="javascript:doUserRecUploadConfirmFile()">
              <a class="btn btn-file-upload btn-secondary">Upload</a>

            </div>
            <span name="iaisErrorMsg" class="error-msg" id="error_file"></span>
            <span class="error-msg" id="error_litterFile_Show" name="error_litterFile_Show"  style="color: #D22727; font-size: 1.6rem"></span>
            <div class="col-xs-12 col-md-4" >
              <span  name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">${filename}</span>
              <input type="text" value="Y" style="display: none" name="isDelete" id="isDelete">
              <input type="text" value="${filename}" style="display: none" id="isFile">

              <a class="btn btn-danger btn-sm" style="margin-left: 20px;display: none" name="delete" id="delete" >X</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
    </div>

    <br>
    <br> <br> <br>
    <div >
      <div class="row">
        <div class="col-xs-12 col-sm-12" style="margin-bottom: 1%">
          <div class="text-right text-center-mobile">
            <a class="btn btn-secondary" href="#" id="cancel">Cancel</a>
            <a class="btn btn-secondary" href="#" id="save">Save</a>
            <a class="btn btn-primary" href="#" id="submit">Submit</a>
          </div>
        </div>
      </div>
    </div>
    <input type="hidden"  id="saveDraftSuccess" name="saveDraftSuccess" value="${saveDraftSuccess}">
    <c:if test="${!('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
      <iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>
    </c:if>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
  </form>
</div>
<style>
  .mandatory{
    color: rgb(255,0,0);
  }

</style>
<script  type="text/javascript">

  $(document).ready(function () {
      if($('#saveDraftSuccess').val()=='success'){
          $('#saveDraft').modal('show');
      }
      var reason= $('#reasonSelect option:selected').val();
      if("MS003"==reason){
          $('#cgo').attr("style" ,"display: block;margin-top: 10px;margin-left: 1%");

      }else  {
          $('#cgo').attr("style" ,"display: none");

      }
      if("MS008"==reason){
          $('#selectHciNameAppeal').attr("style","display: block;margin-top: 20px");

      }else {
          $('#selectHciNameAppeal').attr("style","display: none");
      }
      if("MS004"==reason){
          $('#licenceYear').attr("style","display: block;margin-top: 20px");
      }else {
          $('#licenceYear').attr("style","display: none");
      }
      if("MS007"==reason){
          $('#othersReason').attr("style","display: block");
      }else {
          $('#othersReason').attr("style","display: none");
      }
      if(  $("input[name='selectHciName']").prop("checked")){
          $('#proposedHciName').attr("style","display: block");
      }else {
          $('#proposedHciName').attr("style","display: none");
      }
      if(  $('#isFile').val()!=''){
          $('#delete').attr("style","display: inline-block;margin-left: 20px");
          $('#isDelete').val('Y');
      }


  });

$('#submit').click(function () {
  uploadFileValidate();
  var error = $('#error_litterFile_Show').html();
  if(error == undefined || error == ""){
    SOP.Crud.cfxSubmit("mainForm", "submit","submit","");
  }
});

  function doUserRecUploadConfirmFile() {
    var file = $('#selectedFile').val();
    file= file.split("\\");
    $("span[name='fileName']").html(file[file.length-1]);

    if(file!=''){
      $('#delete').attr("style","display: inline-block;margin-left: 20px");
      $('#isDelete').val('Y');
      $('#error_litterFile_Show').html("");
      $('#error_file').html("");
    }
    uploadFileValidate();
  }


function uploadFileValidate(){
  var configFileSize = $("#configFileSize").val();
  var error  = validateUploadSizeMaxOrEmpty(configFileSize,'selectedFile');
  var flag = true;
  if( error =="Y"){
    $('#delete').attr("style","display: inline-block;margin-left: 20px");
    $('#isDelete').val('Y');
    $('#error_litterFile_Show').html("");
    $('#error_file').html("");
  }else if(error == "E"){
    deleteFileFunction();
    $('#error_litterFile_Show').html("");
    $('#error_file').html("");
  } else if(error =="N"){
    clearFileFunction();
    flag = false;
    $('#error_litterFile_Show').html('The file has exceeded the maximum upload size of '+ configFileSize + 'M.');
    $('#error_file').html("");
  }
  return flag;
}

$('#delete').click(function () {
  deleteFileFunction();
});

function deleteFileFunction(){
  $('.selectedFile').val('');
  $('#delete').attr("style","display: none");
  $("span[name='fileName']").html('');
  $('#isDelete').val('N');
  $('#error_litterFile_Show').html("");
  $('#error_file').html("");
}

function clearFileFunction(){
  $('.selectedFile').val('');
  $('#delete').attr("style","display: none");
  $("span[name='fileName']").html('');
  $('#isDelete').val('N');
}

$('#save').click(function () {
    SOP.Crud.cfxSubmit("mainForm", "save","save","");
});

$('#reasonSelect').change(function () {

  var reason= $('#reasonSelect option:selected').val();
  if("MS003"==reason){
      $('#cgo').attr("style" ,"display: block");

  }else  {
      $('#cgo').attr("style" ,"display: none");

  }
  if("MS008"==reason){
    $('#selectHciNameAppeal').attr("style","display: block");

  }else {
      $('#selectHciNameAppeal').attr("style","display: none");
  }
  if("MS004"==reason){
      $('#licenceYear').attr("style","display: block");
  }else {
      $('#licenceYear').attr("style","display: none");
  }
  if("MS007"==reason){
      $('#othersReason').attr("style","display: block");
  }else {
      $('#othersReason').attr("style","display: none");
  }
    
});


function isCheck(obj) {

  if($(obj).prop("checked")){
      $('#proposedHciName').attr("style","display: block");
  }else {
      $('#proposedHciName').attr("style","display: none");
  }

}

  function getFileName(o) {
      var pos = o.lastIndexOf("\\");
      return o.substring(pos + 1);
  };

  function deletes() {

      $('#control--runtime--1').attr("hidden");

  }
  $('#cancel').click(function () {

      SOP.Crud.cfxSubmit("mainForm", "cancel","cancel","");

  });


  function cancelSaveDraft() {

  }
  function cancel() {
      $('#saveDraft').modal('hide');
  }

  function jumpPage() {
      SOP.Crud.cfxSubmit("mainForm", "cancel","cancel","");
  }
</script>

</>

