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

<div class="main-content">
  <form id="mainForm" enctype="multipart/form-data"  class="__egovform" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">

    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div>
      <h1>Appeal Form</h1>
    </div>
  <div class="form-group">
    <div class="col-xs-12 col-md-10">
      <label style="font-size: 25px">You are appealing for:</label>
    </div>

    <div class="col-xs-12 col-md-5">
      <input type="text" name="appealingFor" disabled  value="${appealingFor}" >
    </div>
  </div>

    <div class="form-group">
      <div class="col-xs-12 col-md-8" >
      <label style="font-size: 25px">Reason for Appeal<span class="mandatory">*</span></label>
      <select id="reasonSelect" name="reasonSelect">
        <option value="">please select an appeal reason</option>
        <option value="MS001"<c:if test="${type=='licence'}">disabled</c:if> <c:if test="${appPremiseMiscDto.reason=='MS001'}">selected="selected"</c:if> >Appeal against rejection</option>
        <option value="MS002" <c:if test="${type=='licence'}">disabled</c:if><c:if test="${appPremiseMiscDto.reason=='MS002'}">selected="selected"</c:if>>Appeal against late renewal fee</option>
        <option value="MS003"<c:if test="${type=='licence'}">disabled</c:if> <c:if test="${appPremiseMiscDto.reason=='MS003'}">selected="selected"</c:if>>Appeal for appointment of additional CGO to a service</option>
        <option value="MS008"<c:if test="${type=='licence'}">disabled</c:if> <c:if test="${appPremiseMiscDto.reason=='MS008'}">selected="selected"</c:if>>Appeal against use of restricted words in HCI Name</option>
        <option value="MS004"<c:if test="${type=='application'}">disabled</c:if>  <c:if test="${appPremiseMiscDto.reason=='MS004'}">selected="selected"</c:if>>Appeal for change of licence period</option>
       <%-- <option value="MS005" <c:if test="${appPremiseMiscDto.reason=='MS005'}">selected="selected"</c:if>>Appeal against suspension</option>
        <option value="MS006" <c:if test="${appPremiseMiscDto.reason=='MS006'}">selected="selected"</c:if>>Appeal against revocation</option>--%>
      </select>

        <div> <span  class="error-msg" name="iaisErrorMsg" id="error_reason"></span></div>

      <div class="col-xs-12 col-md-10" id="licenceYear"  style="display: none">
        <label style="font-size: 20px;">Licence year</label>
        <input type="number" name="licenceYear" value="${appPremiseMiscDto.newLicYears}">

      </div>
      <div style="display: none;margin-top: 10px" id="cgo" >

        <%@include file="cgo.jsp"%>

      </div>

      <div class="form-check-gp" id="selectHciNameAppeal" style="display: none">
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

      <div class="col-xs-12 col-md-10">

        <label style="font-size: 25px">Any supporting remarks<span class="mandatory">*</span></label>

      </div >
    <div  class="col-xs-12 col-md-10">

      <textarea cols="50" style="font-size: 20px" rows="10" name="remarks" maxlength="300" >${appPremiseMiscDto.remarks}</textarea>

      <div> <span class="error-msg" id="error_remarks" name="iaisErrorMsg"></span></div>

    </div>


    <div class="form-group">
  <div >
    <div>
      <div class="col-xs-12 col-md-10" >
        <label style="font-size: 25px;margin-top: 25px">File Upload for Appeal Reasons</label>
      </div>

      <div class="col-xs-12">
        <div class="document-upload-list">
          <div class="file-upload-gp">
            <div class="fileContent col-xs-2">
              ${upFile.originalFilename}
              <input class="selectedFile" id="selectedFile" name = "selectedFile"  type="file" style="display: none;" aria-label="selectedFile1">
              <a class="btn btn-file-upload btn-secondary" >Upload</a>

            </div>
            <span name="iaisErrorMsg" class="error-msg" id="error_file"></span>
            <div class="col-xs-12 col-md-4" >
              <span  name="fileName" style="font-size: 25px;color: #2199E8;text-align: center">${filename}</span>
              <input type="text" value="Y" style="display: none" name="isDelete" id="isDelete">
              <input type="text" value="${filename}" style="display: none" id="isFile">
              <a class="btn  btn-secondary" style="margin-left: 20px;display: none" name="delete" id="delete" >delete</a>
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
        <div class="col-xs-12 col-sm-10">
          <div class="text-right text-center-mobile">
            <a class="btn btn-primary" href="#" id="save">Save</a>
            <a class="btn btn-primary" href="#" id="submit">Submit</a>
            <a class="btn btn-primary" href="#" id="cancel">Cancel</a>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/include/validation.jsp" %>
  </form>
</div>
<style>
  .mandatory{
    color: #f00;
  }

</style>
<script  type="text/javascript">

  $(document).ready(function () {
      var reason= $('#reasonSelect option:selected').val();
      if("MS003"==reason){
          $('#cgo').attr("style" ,"display: block;margin-top: 20px");

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

    SOP.Crud.cfxSubmit("mainForm", "submit","submit","");

});

$('#delete').click(function () {
  $('.selectedFile').val('');
  $(this).attr("style","display: none");
  $("span[name='fileName']").html('');
  $('#isDelete').val('N');


});

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
    file= file.split("\\");
    $("span[name='fileName']").html(file[file.length-1]);

    if(file!=''){
        $('#delete').attr("style","display: inline-block;margin-left: 20px");
        $('#isDelete').val('Y');
    }

 /*   $fileUploadContentEle = $(this).closest('div.file-upload-gp');
    $fileUploadContentEle.find('.delBtn').removeClass('hidden');*/
});
  function getFileName(o) {
      var pos = o.lastIndexOf("\\");
      return o.substring(pos + 1);
  };

</script>

</>

