<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<webui:setLayout name="iais-internet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%@include file="../appeal/dashboard.jsp" %>
<div class="container">
    <form id="mainForm" enctype="multipart/form-data"  class="__egovform" method="post" action=<%=process.runtime.continueURL()%> >
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" id="configFileSize" value="${configFileSize}"/>
        <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">

        <div class="form-group">
            <div class="col-xs-12 col-md-10" style="margin-left: 2%">
                <label style="font-size: 25px">You are appealing for:</label>
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

        <div class="form-group">
            <div class="col-xs-12 col-md-10" style="margin-left: 1%">
                <div class="col-xs-12 col-md-6" style="margin-bottom: 20px">
                    <label style="font-size: 25px">Reason For Appeal</label>
                    <select id="reasonSelect" disabled name="reasonSelect" style="margin-left: 2%">
                        <option value="">Please Select</option>
                        <c:forEach items="${selectOptionList}" var="selectOption">
                            <option value="${selectOption.value}" <c:if test="${appPremiseMiscDto.reason==selectOption.value}">selected="selected"</c:if> >${selectOption.text}</option>
                        </c:forEach>
                    </select>

                    <div style="margin-top: 1%"> <span  class="error-msg" name="iaisErrorMsg" id="error_reason"></span></div>


                    <div class="col-xs-12 col-md-10" id="othersReason" style="display: none" >
                        <label style="font-size: 20px;margin-top: 1%">Others reason</label>
                        <input type="text" maxlength="100" disabled name="othersReason" value="${appPremiseMiscDto.otherReason}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_otherReason"></span>
                    </div>

                    <div class="form-check-gp" id="selectHciNameAppeal" style="display: none" class="col-xs-12 col-md-6">
                        <label style="font-size: 20px">Select HCI Name To Appeal</label>
                        <c:forEach items="${hciNames}" var="hciName" >
                            <div >
                                <div class="form-check" >
                                    <input class="form-check-input" disabled onclick="isCheck(this)" type="checkbox" <c:if test="${fn:length(hciNames)==1}">checked="checked" </c:if> name="selectHciName" aria-invalid="false" value="${hciName}">
                                    <label class="form-check-label"><span class="check-square"></span>${hciName}</label>
                                </div>

                                <div class="col-xs-12 col-md-10" id="proposedHciName" style="display: none" >
                                    <label style="font-size: 20px">Proposed  HCI Name</label>
                                    <input type="text" maxlength="100" disabled name="proposedHciName" value="${appPremiseMiscDto.newHciName}">
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
            <%@include file="../appeal/cgoView.jsp"%>

        </div>
        <div class="col-xs-12 col-md-10" style="margin-left: 2%">

            <label style="font-size: 25px">Any supporting remarks</label>

        </div >
        <div  class="col-xs-12 col-md-10" style="margin-left: 2%" >

            <textarea cols="120" disabled style="font-size: 20px" rows="10" name="remarks" maxlength="300" >${appPremiseMiscDto.remarks}</textarea>

            <div> <span class="error-msg" id="error_remarks" name="iaisErrorMsg"></span></div>

        </div>


        <div class="form-group">
            <div >
                <div>
                    <div class="col-xs-12 col-md-10" style="margin-left: 2%">
                        <label style="font-size: 25px;margin-top: 25px;" >File Upload For Appeal Reasons</label>
                    </div>

                    <div class="col-xs-12" style="margin-bottom: 20px;">
                        <div class="document-upload-list">
                            <div class="file-upload-gp">
                                <div class="fileContent ">
                                    ${upFile.originalFilename}
                                </div>
                                <span name="iaisErrorMsg" class="error-msg" id="error_file"></span>
                                <span class="error-msg" id="error_litterFile_Show" name="error_litterFile_Show"  style="color: #D22727; font-size: 1.6rem"></span>
                                <div class="col-xs-12 col-md-4" style="margin-left: 1%" >
                                    <c:forEach items="${pageShowFiles}" var="pageShowFileDto" varStatus="ind">
                                        <div id="${pageShowFileDto.fileMapId}">
                                            <span  name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                                                <iais:downloadLink fileRepoIdName="fileRo0" fileRepoId="${pageShowFileDto.fileUploadUrl}" docName="${pageShowFileDto.fileName}"/>
                                            </span>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_file${ind.index}"></span>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
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
    $('#Back').click(function (){
        location.href="https://${pageContext.request.serverName}/main-web<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohInternetInbox?initPage=initApp",request)%>";

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
            $('#error_litterFile_Show').html($("#fileMaxLengthMessage").val());
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

