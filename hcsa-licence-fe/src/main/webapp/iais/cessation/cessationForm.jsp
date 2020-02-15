<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<form id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="panel-heading"><h2><strong>Cessation Form</strong></h2></div>
        <div class="panel-heading"><h4><strong>Please key in to cessation information</strong></h4></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table" border="1" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr>
                            <th>S/N</th>
                            <th>Licence No.</th>
                            <th>Service Name</th>
                            <th>HCI Name</th>
                            <th>HCI Address</th>
                            <th>Effective Date</th>
                            <th>Cessation Reasons</th>
                            <th>Patients' Record will be transferred</th>
                            <th>todo</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${appCessDtosByLicIds}" var="appCess" varStatus="num">
                            <tr>
                                <td class="col-xs-1">
                                    <p><c:out value="${num.count}"></c:out></p>
                                </td>
                                <td class="col-xs-1">
                                    <p><c:out value="${appCess.licenceNo}"></c:out></p>
                                </td>
                                <td class="col-xs-1">
                                    <p><c:out value="${appCess.svcName}"></c:out></p>
                                </td>
                                <td colspan="6" class="col-xs-9">
                                    <table class="table" border="1" cellspacing="0" cellpadding="0">
                                        <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid">
                                            <tr>
                                            <td class="col-xs-1">
                                                <p><c:out value="${appCessHci.hciName}"></c:out></p>
                                            </td>
                                            <td class="col-xs-1">
                                                <p><c:out value="${appCessHci.hciAddress}"></c:out></p>
                                            </td>
                                            <td class="col-xs-1">
                                                <iais:datePicker id="effectiveDate" name="effectiveDate" dateVal="${appCessationDto.effectiveDate}"/>
                                                <span id="error_effectiveDate" name="iaisErrorMsg" class="error-msg"></span>
                                            </td>
                                            <td class="col-xs-1">
                                                <iais:select name="cessationReason" options="reasonOption"
                                                             firstOption="Please select"
                                                             onchange="javascirpt:changeReason(this.value);" value="${appCessationDto.cessationReason}"/>
                                                <span id="error_cessationReason" name="iaisErrorMsg" class="error-msg"></span>
                                                <div id="reason" hidden><input type="text" name="otherReason">
                                                    <span id="error_otherReason" name="iaisErrorMsg" class="error-msg"></span>
                                                </div>
                                            </td>
                                            <td class="col-xs-4">
                                                <table>
                                                    <tr>
                                                        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                            <input type="radio" name="patRadio" value="yes" id="${num.count}radioYes${uid.count}"
                                                                   <c:if test="${appCessationDto.patRadio == 'yes'}">checked</c:if>
                                                                   onchange="javascirpt:changePatSelect(this.value);">Yes ${uid.count}
                                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                            <input type="radio" name="patRadio" value="no" id="${num.count}radioNo${uid.count}" <c:if test="${appCessationDto.patRadio == 'no'}">checked</c:if> onchange="javascirpt:changePatSelect(this.value);">No</td>
                                                        <span id="error_patRadio" name="iaisErrorMsg" class="error-msg"></span>
                                                    </tr>
                                                    <tr id="${num.count}patYes${uid.count}" hidden>
                                                        <td><h6>Patients' Record will be transferred to</h6></td>
                                                        <td><iais:select name="patientSelect" options="patientsOption"
                                                                         firstOption="Please select"
                                                                         onchange="javascirpt:changePatient(this.value);" value="${appCessationDto.patientSelect}"/></td>
                                                        <span id="error_patientSelect" name="iaisErrorMsg" class="error-msg"></span>
                                                    </tr>
                                                    <tr id="${num.count}patNo${uid.count}" hidden align="center">
                                                        <td><textarea name="patNoRemarks" cols="40" rows="4" maxlength="8000" title="content"></textarea></td>
                                                        <span id="error_patNoRemarks" name="iaisErrorMsg" class="error-msg"></span>
                                                    </tr>
                                                    <tr id="${num.count}patHciName${uid.count}" hidden>
                                                        <td>HCI Name</td>
                                                        <td><input type="text" name="patHciName" value="${appCessationDto.patHciName}"></td>
                                                        <span id="error_patHciName" name="iaisErrorMsg" class="error-msg"></span>
                                                    </tr>
                                                    <tr id="${num.count}patRegNo${uid.count}" hidden>
                                                        <td>Professional Registered No.</td>
                                                        <td><input type="text" name="patRegNo" value="${appCessationDto.patRegNo}"></td>
                                                        <span id="error_patRegNo" name="iaisErrorMsg" class="error-msg"></span>
                                                    </tr>
                                                    <tr id="${num.count}patOthers${uid.count}" hidden>
                                                        <td>Others</td>
                                                        <td><input type="text" name="patOthers" value="${appCessationDto.patOthers}"></td>
                                                        <span id="error_patOthers" name="iaisErrorMsg" class="error-msg"></span>
                                                    </tr>
                                                </table>
                                            </td>
                                            <td class="col-xs-1">
                                                <input type="checkbox" name="whichTodo">
                                                <span id="error_whichTodo" name="iaisErrorMsg" class="error-msg"></span>
                                            </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td colspan="9"><c:out value="${text1}"/></td>
                        </tr>
                        <tr>
                            <td colspan="9"><c:out value="${text2}"/></td>
                        </tr>
                        <tr>
                            <td colspan="9"><input type="checkbox" name="readInfo" id="confirmInfo">I have read the information</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div align="right">
            <button id="backButton" type="button" class="btn btn-primary">
                Back
            </button>
            <button id="submitButton" type="button" class="btn btn-primary" onclick="submitSure()">
                Next
            </button>
        </div>
    </div>
    <%@include file="/include/validation.jsp" %>
</form>

<script type="text/javascript">
    function changeReason(obj) {
        if (obj == "OtherReasons") {
            $("#reason").show();
        } else {
            $("#reason").hide();
        }
    }

    function changePatient(obj) {
        if (obj == "Others") {
            $("#patOthers").show();
            $("#patHciName").hide();
            $("#patRegNo").hide();
        } else if(obj=="hciName") {
            $("#patHciName").show();
            $("#patOthers").hide();
            $("#patRegNo").hide();
        }else if(obj=="regNo"){
            $("#patRegNo").show();
            $("#patHciName").hide();
            $("#patOthers").hide();
        }
    }

    function changePatSelect() {
        for (var i = 1; i < 3; i++) {
            for (var j = 1; i < 3; i++) {
                if($("#"+i+"radioYes"+j).is(':checked')) {
                    $("#"+i+"patYes"+j).show();
                    $("#"+j+"atNo"+j).hide();
                }else if($('#'+i+'radioNo'+j).is(':checked')){
                    $("#"+i+"patNo"+j).show();
                    $("#"+i+"patYes"+j).hide();
                    $("#"+i+"patHciName"+j).hide();
                    $("#"+i+"patOthers"+j).hide();
                    $("#"+i+"patRegNo"+j).hide();
                }
            }

        }

    }

    function submitSure() {
        if($('#confirmInfo').is(':checked')){
            $("#mainForm").submit();
        }
    }

    $(document).ready(function () {
        //changeReason(obj);
        if ($("#cessationReason").val() == "Others") {
            $("#reason").show();
        } else {
            $("#reason").hide();
        }
        //changePatient(obj);
        if ($("#patientSelect").val() == "Others") {
            $("#patOthers").show();
            $("#patHciName").hide();
            $("#patRegNo").hide();
        } else if($("#patientSelect").val()=="hciName") {
            $("#patHciName").show();
            $("#patOthers").hide();
            $("#patRegNo").hide();
        }else if($("#patientSelect").val()=="regNo"){
            $("#patRegNo").show();
            $("#patHciName").hide();
            $("#patOthers").hide();
        }
        //changePatSelect();
        if($('#radioYes').is(':checked')) {
            $("#patYes").show();
            $("#patNo").hide();
        }else if($('#radioNo').is(':checked')){
            $("#patNo").show();
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#patOthers").hide();
            $("#patRegNo").hide();
        }
    });


</script>