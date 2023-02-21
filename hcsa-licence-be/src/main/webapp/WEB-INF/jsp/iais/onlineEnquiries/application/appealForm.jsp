<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

            <c:set value="${applicationViewDto.applicationDto}" var="applicationDto" scope="request"/>
            <c:set value="${applicationViewDto.appealNo}" var="appealNo" scope="request"/>
            <c:set value="${applicationViewDto.premiseMiscDto}" var="premiseMiscDto" scope="request"/>
            <c:set value="${applicationViewDto.hciNames}" var="hciNames" scope="request"/>
            <c:set value="${applicationViewDto.cgoMandatoryCount}" var="CgoMandatoryCount" scope="request"/>
            <c:set value="${applicationViewDto.feAppealSpecialDocDto}" var="feAppealSpecialDocDto" scope="request"/>
            <c:set value="${applicationViewDto.governanceOfficersList}" var="GovernanceOfficersList" scope="request"/>
            <c:set value="${applicationViewDto.specialtySelectList}" var="SpecialtySelectList" scope="request"/>
            <c:set value="${applicationViewDto.idTypeSelect}" var="IdTypeSelect" scope="request"/>
            <c:set value="${applicationViewDto.cgoSelectList}" var="CgoSelectList" scope="request"/>

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
                        <label style="font-size: 25px">Reason For Appeal</label>
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
                                        <input class="form-check-input"  onclick="isCheck(this)" type="checkbox" disabled <c:if test="${fn:length(hciNames)==1}">checked="checked" </c:if> name="selectHciName" aria-invalid="false" value="${hciName}">
                                        <label class="form-check-label"><span class="check-square"></span>${hciName}</label>
                                    </div>

                                    <div class="col-xs-12 col-md-10" id="proposedHciName" style="display: none" >
                                        <label style="font-size: 20px">Proposed  HCI Name</label>
                                        <input type="text" maxlength="100" name="proposedHciName" disabled value="${premiseMiscDto.newHciName}">
                                        <span ></span>
                                    </div>
                                </div>

                            </c:forEach>
                        </div>

                    </div>

                </div>
            </div>
            <div style="display: none;margin-top: 10px;margin-left: 1%" id="cgo" class="col-xs-12 col-md-10" >
                <%@include file="../../appeal/cgo.jsp"%>
            </div>
            <div class="col-xs-12 col-md-10" style="margin-left: 1%">

                <label style="font-size: 25px">Any supporting remarks</label>

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
                                    <div class="fileContent col-xs-12">
                                        <c:forEach items="${feAppealSpecialDocDto}" var="v" varStatus="index">
                                            <div class="col-xs-12">
                                                <iais:downloadLink fileRepoIdName="fileRo${index.index}" fileRepoId="${v.fileRepoId}" docName="${v.getDocName()}"/>
                                            </div>
                                        </c:forEach>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
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

    });
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    };

    function deletes() {
        $('#control--runtime--1').attr("hidden");
    }

</script>



