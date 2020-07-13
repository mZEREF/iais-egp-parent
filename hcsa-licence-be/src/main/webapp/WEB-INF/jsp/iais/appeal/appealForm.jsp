<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="main-content">
  <div class="container">
  <form id="mainForm" enctype="multipart/form-data" style="margin-left: 15%" class="__egovform" method="post" action=<%=process.runtime.continueURL()%> >
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <c:set value="${applicationViewDto.applicationDto}" var="applicationDto"/>
  <div class="form-group">
    <div class="col-xs-12 col-md-10" style="margin-left: 1%">
      <h1 style="border-bottom: none;margin-top:60px;">Appeal Form</h1>
      <label style="font-size: 25px">You are appealing for:</label>
    </div>

    <div  class="col-xs-12 col-md-10">
      <div class="col-xs-12 col-md-6">
        <input type="text" name="appealingFor" disabled  value="${appealNo}">
        <span name="iaisErrorMsg" class="error-msg" id="error_submit"></span>
      </div>
    </div>
  </div>

    <div class="form-group">
      <div class="col-xs-12 col-md-10">
        <div class="col-xs-12 col-md-6" style="margin-bottom: 20px">
          <label style="font-size: 25px">Reason For Appeal<span class="mandatory"> *</span></label>
          <select id="reasonSelect" name="reasonSelect" disabled>
            <option value="">Please Select</option>
            <option value="MS001" <c:if test="${premiseMiscDto.reason=='MS001'}">selected="selected"</c:if> >Appeal against rejection</option>
            <option value="MS002" <c:if test="${premiseMiscDto.reason=='MS002'}">selected="selected"</c:if>>Appeal against late renewal fee</option>
            <option value="MS003" <c:if test="${premiseMiscDto.reason=='MS003'}">selected="selected"</c:if>>Appeal for appointment of additional CGO to a service</option>
            <option value="MS008" <c:if test="${premiseMiscDto.reason=='MS008'}">selected="selected"</c:if>>Appeal against use of restricted words in HCI Name</option>
            <option value="MS004" <c:if test="${premiseMiscDto.reason=='MS004'}">selected="selected"</c:if>>Appeal for change of licence period</option><option value="MS007" <c:if test="${premiseMiscDto.reason=='MS007'}">selected="selected"</c:if>>Others</option>
          </select>

          <div style="margin-top: 1%"> <span  class="error-msg" name="iaisErrorMsg" id="error_reason"></span></div>


          <div class="col-xs-12 col-md-10" id="othersReason" style="display: none" >
            <label style="font-size: 20px;margin-top: 1%">Others reason</label>
            <input type="text" maxlength="100" disabled  name="othersReason" value="${premiseMiscDto.otherReason}" >
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
                  <input type="text" maxlength="100" name="proposedHciName" value="${premiseMiscDto.newHciName}">
                  <span ></span>
                </div>
              </div>

            </c:forEach>
          </div>

        </div>

      </div>
      </div>
    <div style="display: none;margin-top: 10px;margin-left: 1%" id="cgo" class="col-xs-12 col-md-9" >
      <%@include file="cgo.jsp"%>
    </div>
      <div class="col-xs-12 col-md-10" style="margin-left: 1%">

        <label style="font-size: 25px">Any supporting remarks<span class="mandatory"> *</span></label>

      </div >
    <div  class="col-xs-12 col-md-10" style="margin-left: 1%" >

      <textarea cols="120" style="font-size: 20px" rows="10" disabled name="remarks" maxlength="300" >${premiseMiscDto.remarks}</textarea>

      <div> <span class="error-msg" id="error_remarks" name="iaisErrorMsg"></span></div>

    </div>


    <div class="form-group">
  <div >
    <div>
      <div class="col-xs-12 col-md-10" >
        <label style="font-size: 25px;margin-top: 25px;" >File Upload For Appeal Reasons</label>
      </div>

      <div class="col-xs-12">
        <div class="document-upload-list">
          <div class="file-upload-gp">
            <div class="fileContent col-xs-2">
              <c:if test="${not empty appealSpecialDocDto}">
                <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo&fileRo=<iais:mask name="fileRo"  value="${appealSpecialDocDto.fileRepoId}"/>&fileRepoName=${appealSpecialDocDto.docName}"
                   title="Download" class="downloadFile"><c:out
                        value="${appealSpecialDocDto.docName}"></c:out></a>
              </c:if>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
    </div>

    <br>
    <br> <br> <br>
    <c:if test="${rfi=='rfi'}">
        <div class="row">
          <div class="col-xs-12 col-sm-10" style="margin-bottom: 1%">
            <div class="text-right text-center-mobile">
              <a class="btn btn-primary" href="#" id="submit">Submit</a>
            </div>
          </div>
        </div>
    </c:if>
  </form>
  </div>
</div>
<style>
  .mandatory{
    color: rgb(255,0,0);
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
    // SOP.Crud.cfxSubmit("mainForm", "submit","submit","");
  window.opener=null;
  window.open('','_self');
  window.close();
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

  function deletes() {

      $('#control--runtime--1').attr("hidden");

  }
  $('#cancel').click(function () {

      SOP.Crud.cfxSubmit("mainForm", "cancel","cancel","");

  });


  function cancelSaveDraft() {

  }

  function cancel() {

  }


</script>

</>

