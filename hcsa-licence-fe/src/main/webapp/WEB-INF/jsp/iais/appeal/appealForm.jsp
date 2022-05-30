<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<webui:setLayout name="iais-internet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<%@include file="./dashboard.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<div class="container">
  <form id="mainForm" enctype="multipart/form-data" name="mainForm" class="__egovform form-horizontal" method="post" action=<%=process.runtime.continueURL()%> >
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_value" id="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" id="crud_action_additional" value="">
    <input type="hidden" name="crud_action_type" id="crud_action_type" value="">
    <input type="hidden" id="configFileSize" value="${configFileSize}"/>
    <input type="hidden" name="applicationType" value="APTY002"/>

    <div id="div_print">
      <div class="form-group">
        <div class="col-xs-12 col-md-9" style="margin-left: 2%">
          <label style="font-size: 25px">You are appealing for:</label>
        </div>
        <div class="col-xs-12 col-sm-2">
          <p class="print text-right"><a href="#" id="print-review"> <em class="fa fa-print"></em>Print</a></p>
        </div>
        <div  class="col-xs-12 col-md-10">
          <div class="col-xs-12 col-md-6" style="margin-left: 1%">
            <a type="text" name="appealingFor" id="appealingFor"  value="${appealNo}" onclick="link()" >${appealNo}</a>
            <span class="appMaskNo" style="display: none"><iais:mask name="appNo" value="${appealNo}"/></span>
            <input type="hidden" value="${id}" id="licenceId">
            <input type="hidden" value="${type}" id="parametertype">
            <span name="iaisErrorMsg" class="error-msg" id="error_submit"></span>
          </div>
        </div>
      </div>
      <div>
        <div class="form-group">
          <div class="col-xs-12 col-md-10" style="margin-left: 1%">
            <div class="col-xs-12 col-md-6" style="margin-bottom: 20px">
              <label style="font-size: 25px">Reason For Appeal<span class="mandatory"> *</span></label>
              <select id="reasonSelect" name="reasonSelect" style="margin-left: 2%">
                <option value="">Please Select</option>
                <c:forEach items="${selectOptionList}" var="selectOption">
                  <option value="${selectOption.value}" <c:if test="${appPremiseMiscDto.reason==selectOption.value}">selected="selected"</c:if> >${selectOption.text}</option>
                </c:forEach>
              </select>

              <div style="margin-top: 1%"> <span  class="error-msg" name="iaisErrorMsg" id="error_reason"></span></div>


              <div class="col-xs-12 col-md-10" id="othersReason" style="display: none" >
                <label style="font-size: 20px;margin-top: 1%">Others reason<span class="mandatory"> *</span></label>
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
                      <label style="font-size: 20px">Proposed  HCI Name<span class="mandatory"> *</span></label>
                      <input type="text" maxlength="100" name="proposedHciName" value="${appPremiseMiscDto.newHciName}">
                      <span class="error-msg" name="iaisErrorMsg" id="error_proposedHciName"></span>
                    </div>
                  </div>

                </c:forEach>
              </div>

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

        <textarea cols="120" style="font-size: 20px;width: 100%" rows="10" name="remarks" maxlength="300" >${appPremiseMiscDto.remarks}</textarea>

        <div> <span class="error-msg" id="error_remarks" name="iaisErrorMsg"></span></div>

      </div>

      <div class="form-group">
        <div >
          <div>
            <div class="col-xs-12 col-md-10" >
              <label style="font-size: 25px;margin-top: 25px;margin-left: 1%" >File Upload For Appeal Reasons</label>
            </div>

            <div class="col-xs-12">
              <div class="document-upload-list" >
                <div class="file-upload-gp row" >
                  <div class="fileContent col-xs-5">
                    <input class="selectedFile"  id="selectedFile" name = "selectedFile"  onclick="fileClicked(event)"  onchange="javascript:doUserRecUploadConfirmFile(event)" type="file" style="display: none;" aria-label="selectedFile1" >
                    <a class="btn btn-file-upload btn-secondary" href="javascript:void(0);" onclick="doFileAddEvent()">Upload</a>
                  </div>
                </div>
                <span class="error-msg" name="iaisErrorMsg" id="error_selectedFileError"></span>
                <div class="col-xs-12" >
            <span  name="selectedFileShowId" id="selectedFileShowId">
            <c:forEach items="${pageShowFiles}" var="pageShowFileDto" varStatus="ind">
              <div id="${pageShowFileDto.fileMapId}">
                  <span  name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                    <iais:downloadLink fileRepoIdName="fileRo0" fileRepoId="${pageShowFileDto.fileUploadUrl}" docName="${pageShowFileDto.fileName}"/>
                  </span>
                <span class="error-msg" name="iaisErrorMsg" id="error_file${ind.index}"></span>

                <button type="button" class="btn btn-secondary btn-sm" onclick="javascript:deleteFileFeAjax('selectedFile',${pageShowFileDto.index});">
                Delete</button>  <button type="button" class="btn btn-secondary btn-sm" onclick="javascript:reUploadFileFeAjax('selectedFile',${pageShowFileDto.index},'mainForm');">
              ReUpload</button>
              </div>

            </c:forEach>
            </span>
                </div>
              </div>
              <div class="col-xs-12">
                <span name="iaisErrorMsg" class="error-msg" id="error_file"></span>
                <span class="error-msg" id="error_litterFile_Show" name="error_litterFile_Show"  style="color: #D22727; font-size: 1.6rem"></span>
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
              <a class="btn btn-secondary" href="javascript:void(0);" id="cancel">Cancel</a>
              <c:if test="${empty rfiApplication}">
                <a class="btn btn-secondary" href="javascript:void(0);" id="save">Save</a>
              </c:if>
              <a class="btn btn-primary" href="javascript:void(0);" id="submit">Submit</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <input type="hidden"  id="saveDraftSuccess" name="saveDraftSuccess" value="${saveDraftSuccess}">
    <iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
    <%@ include file="./FeFileCallAjax.jsp" %>
    <input type="hidden" value="${need_print}" id="need_print" />
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
          $('#proposedHciName').attr("style","display: block");  }else {
          $('#proposedHciName').attr("style","display: none");
      }
      if(  $('#isFile').val()!=''){
          $('#delete').attr("style","display: inline-block;margin-left: 20px");
          $('#isDelete').val('Y');
      }
      if($('#need_print').val()=='need_print'){
          var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohAppealPrint/1/",request)%>';
          window.open(url,'_blank');
      }
  });
$('#submit').click(function () {
    if("N" == $('#isDelete').val()){
        uploadFileValidate();
    }
  var error = $('#error_litterFile_Show').html();
  if(error == undefined || error == ""){
    $('input[type="text"]').removeClass('disabled');
    $('input[type="text"]').prop('disabled',false);
    Utils.submit('mainForm','submit','submit','','');
   /* SOP.Crud.cfxSubmit("mainForm", "submit","submit","");*/
  }
});
  $("#print-review").click(function () {
    $('input[type="text"]').removeClass('disabled');
    $('input[type="text"]').prop('disabled',false);
    Utils.submit('mainForm','print','print','','');
  });
  var debug = true;//true: add debug logs when cloning
  var evenMoreListeners = true;//demonstrat re-attaching javascript Event Listeners (Inline Event Listeners don't need to be re-attached)
  if (evenMoreListeners) {
    var allFleChoosers = $("input[type='file']");
    addEventListenersTo(allFleChoosers);
    function addEventListenersTo(fileChooser) {
      fileChooser.change(function (event) {
          console.log("file( #" + event.target.id + " ) : " + event.target.value.split("\\").pop());
    /*  a();*/

         });
         fileChooser.click(function (event) { console.log("open( #" + event.target.id + " )") });
       }
     }

     function doFileAddEvent() {
         clearFlagValueFEFile();
     }

    function link(){
      var type = $('#parametertype').val();
      if(type=='application'){
         var v= $('.appMaskNo').html();
          showPopupWindow("${pageContext.request.contextPath}/eservice/INTERNET/MohFeApplicationView?appNo="+v);
      }else {
          if (type == "licence") {
              showPopupWindow("${pageContext.request.contextPath}/eservice/INTERNET/MohLicenceView?licenceId=" + $('#licenceId').val() + "&appeal=appeal");
          }
      }
    }
     var a=function ajax(){
         var form = new FormData($("#mainForm")[0]);
         $.ajax({
             type:"post",
             url:"${pageContext.request.contextPath}/ajax-upload-file",
          data: form,
          async:true,
          dataType: "json",
          processData: false,
          contentType: false,
          success: function (data) {
            var html ='';
            for(var i=0;i<data.length;i++){
                html+='<input type="text" value="'+v[i]+'" style="display: none" >';
            }
            alert(html);
          }
      });
  }
  function doUserRecUploadConfirmFile(event) {
      ajaxCallUploadForMax('mainForm',"selectedFile", true);

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
    $('#error_litterFile_Show').html($("#fileMaxMBMessage").val());
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
  $('input[type="text"]').removeClass('disabled');
  $('input[type="text"]').prop('disabled',false);
    Utils.submit('mainForm','save','save','','');
   /* SOP.Crud.cfxSubmit("mainForm", "save","save","");*/
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
      Utils.submit('mainForm','cancel','cancel','','');
   /*   SOP.Crud.cfxSubmit("mainForm", "cancel","cancel","");*/

  });


  function cancelSaveDraft() {

  }
  function cancel() {
      $('#saveDraft').modal('hide');
  }

  function jumpPage() {
      Utils.submit('mainForm','cancel','cancel','','');
     /* SOP.Crud.cfxSubmit("mainForm", "cancel","cancel","");*/
  }
</script>

</>

