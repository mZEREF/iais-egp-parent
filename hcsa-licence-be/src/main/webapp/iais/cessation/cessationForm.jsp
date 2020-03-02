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
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${appCessationDtos}" var="appCess" varStatus="num">
                            <tr>
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${num.count}"></c:out></p>
                                </td>
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${appCess.licenceNo}"></c:out></p>
                                </td>
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${appCess.svcName}"></c:out></p>
                                </td>
                                <td colspan="9" class="col-xs-9">
                                    <table class="table" border="1" cellspacing="0" cellpadding="0">
                                        <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid">
                                            <tr>
                                                <td class="col-xs-1" align="center">
                                                    <p><c:out value="${appCessHci.hciName}"></c:out></p>
                                                </td>
                                                <td class="col-xs-1" align="center">
                                                    <p><c:out value="${appCessHci.hciAddress}"></c:out></p>
                                                </td>
                                                <td class="col-xs-1" align="center">
                                                    <iais:datePicker id="effectiveDate"
                                                                     name="${num.count}effectiveDate${uid.count}"
                                                                     dateVal="${appCessHci.effectiveDate}"/>
                                                    <span id="error_${num.count}effectiveDate${uid.count}" name="iaisErrorMsg"
                                                          class="error-msg"></span>
                                                </td>
                                                <td class="col-xs-2" align="center">
                                                    <iais:select id="${num.count}reasonId${uid.count}"
                                                                 name="${num.count}reason${uid.count}"
                                                                 options="reasonOption"
                                                                 firstOption="Please select"
                                                                 onchange="javascirpt:changeReason(this.value);"
                                                                 value="${appCessHci.reason}"/>
                                                    <span id="error_${num.count}reason${uid.count}" name="iaisErrorMsg"
                                                          class="error-msg"></span>
                                                    <div id="${num.count}reason${uid.count}" hidden><input
                                                            type="text"
                                                            name="${num.count}otherReason${uid.count}" value="${appCessHci.otherReason}">
                                                        <span id="error_${num.count}otherReason${uid.count}" name="iaisErrorMsg"
                                                              class="error-msg"></span>
                                                    </div>
                                                </td>
                                                <td class="col-xs-3" align="center">
                                                    <table>
                                                        <tr>
                                                            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                                <input type="radio"
                                                                       name="${num.count}patRadio${uid.count}"
                                                                       value="yes"
                                                                       id="${num.count}radioYes${uid.count}"
                                                                       <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                                       onchange="javascirpt:changePatSelect(this.value);">Yes
                                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                                <input type="radio"
                                                                       name="${num.count}patRadio${uid.count}"
                                                                       value="no"
                                                                       id="${num.count}radioNo${uid.count}"
                                                                       <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                                                       onchange="javascirpt:changePatSelect(this.value);">No
                                                            </td>
                                                            <span id="error_${num.count}patRadio${uid.count}" name="iaisErrorMsg"
                                                                  class="error-msg"></span>
                                                        </tr>
                                                        <tr id="${num.count}patYes${uid.count}" hidden>
                                                            <td>
                                                                <div>Patients'Record will</div>
                                                                <div>be transferred to</div>
                                                            </td>
                                                            <td><iais:select
                                                                    name="${num.count}patientSelect${uid.count}"
                                                                    options="patientsOption"
                                                                    firstOption="Please select"
                                                                    id="${num.count}patientSelectId${uid.count}"
                                                                    onchange="javascirpt:changePatient(this.value);"
                                                                    value="${appCessHci.patientSelect}"/></td>
                                                            <span id="error_${num.count}patientSelect${uid.count}" name="iaisErrorMsg"
                                                                  class="error-msg"></span>
                                                        </tr>
                                                        <tr id="${num.count}patHciName${uid.count}" hidden>
                                                            <td>HCI Name</td>
                                                            <td><input type="text"
                                                                       name="${num.count}patHciName${uid.count}"
                                                                       value="${appCessHci.patHciName}">
                                                            </td>
                                                            <span id="error_${num.count}patHciName${uid.count}" name="iaisErrorMsg"
                                                                  class="error-msg"></span>
                                                        </tr>
                                                        <tr id="${num.count}patRegNo${uid.count}" hidden>
                                                            <td>Professional Registered No.</td>
                                                            <td><input type="text"
                                                                       name="${num.count}patRegNo${uid.count}"
                                                                       value="${appCessHci.patRegNo}">
                                                            </td>
                                                            <span id="error_${num.count}patRegNo${uid.count}" name="iaisErrorMsg"
                                                                  class="error-msg"></span>
                                                        </tr>
                                                        <tr id="${num.count}patOthers${uid.count}" hidden>
                                                            <td>Others</td>
                                                            <td><input type="text"
                                                                       name="${num.count}patOthers${uid.count}"
                                                                       value="${appCessHci.patOthers}">
                                                            </td>
                                                            <span id="error_${num.count}patOthers${uid.count}" name="iaisErrorMsg"
                                                                  class="error-msg"></span>
                                                        </tr>
                                                        <tr id="${num.count}patNo${uid.count}" hidden align="center">
                                                            <td><textarea
                                                                    name="${num.count}patNoRemarks${uid.count}"
                                                                    cols="40" rows="4" maxlength="8000"
                                                                    title="content"><c:out
                                                                    value="${appCessHci.patNoRemarks}"/></textarea>
                                                            </td>
                                                            <span id="error_${num.count}patNoRemarks${uid.count}" name="iaisErrorMsg"
                                                                  class="error-msg"></span>
                                                        </tr>
                                                    </table>
                                                </td>
                                                <td class="col-xs-1" align="center">
                                                    <input type="checkbox" name="${num.count}whichTodo${uid.count}" value="${appCess.licenceId}"  <c:if test="${appCessHci.whichTodo != null}">checked</c:if>>
                                                    <span id="error_whichTodo" name="iaisErrorMsg"
                                                          class="error-msg"></span>
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
                            <td colspan="9"><input type="checkbox" name="readInfo" id="confirmInfo">
                                <label for="confirmInfo">I have read the information</label>
                            </td>
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
    function changeReason() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++){
                if ($("#" + i +"reasonId"+ j ).val() == "OtherReasons") {
                    $("#" + i + "reason"+ j ).show();
                } else {
                    $("#" + i + "reason"+ j ).hide();
                }
            }

        }
    }

    function changePatient() {
        for (var i = 1; i < 3; i++) {
            for (var j = 1; j < 3; j++){
                if ($("#" + i + "patientSelectId"+ j ).val() == "Others") {
                    $("#" + i + "patOthers"+ j ).show();
                    $("#" + i + "patHciName"+ j ).hide();
                    $("#" + i + "patRegNo"+ j ).hide();
                } else if ($("#" + i + "patientSelectId"+ j ).val() == "hciName") {
                    $("#" + i + "patHciName"+ j ).show();
                    $("#" + i + "patOthers"+ j ).hide();
                    $("#" + i + "patRegNo"+ j ).hide();
                } else if ($("#" + i + "patientSelectId"+ j ).val() == "regNo") {
                    $("#" + i + "patRegNo"+ j ).show();
                    $("#" + i + "patHciName"+ j ).hide();
                    $("#" + i + "patOthers"+ j ).hide();
                }
            }

        }
    }

    function changePatSelect() {
        for (var i = 1; i < 3; i++) {
            for (var j = 1; j < 3; j++){
                if ($('#' + i + 'radioYes'+ j).is(':checked')) {
                    $("#" + i + "patYes"+ j).show();
                    $("#" + i + "patNo"+ j).hide();
                } else if ($('#' + i + 'radioNo'+ j).is(':checked')) {
                    $("#" + i + "patNo"+ j).show();
                    $("#" + i + "patYes"+ j).hide();
                    $("#" + i + "patHciName"+ j).hide();
                    $("#" + i + "patOthers"+ j).hide();
                    $("#" + i + "patRegNo"+ j).hide();
                }
            }

        }
    }

    function submitSure() {
        if ($('#confirmInfo').is(':checked')) {
            $("#mainForm").submit();
        }
    }

    $(document).ready(function () {
        for (var i = 1; i < 3; i++) {
            for (var j = 1; j < 3; j++){
                if ($("#" + i + "reasonId"+ j).val() == "OtherReasons") {
                    $("#" + i + "reason"+ j).show();
                } else if ($("#" + i + "reasonId"+ j).val() != "OtherReasons") {
                    $("#" + i + "reason"+ j).hide();
                }
                if ($('#' + i + 'radioYes'+ j).is(':checked')) {
                    $("#" + i + "patYes"+ j).show();
                    $("#" + i + "patNo"+ j).hide();
                } else if ($('#' + i + 'radioNo'+ j).is(':checked')) {
                    $("#" + i + "patYes"+ j).hide();
                    $("#" + i + "patHciName"+ j).hide();
                    $("#" + i + "patOthers"+ j).hide();
                    $("#" + i + "patRegNo"+ j).hide();
                    $("#" + i + "patNo"+ j).show();
                }
                if ($("#" + i + "patientSelectId"+ j).val() == "Others") {
                    $("#" + i + "patOthers"+ j).show();
                    $("#" + i + "patHciName"+ j).hide();
                    $("#" + i + "patRegNo"+ j).hide();
                } else if ($("#" + i + "patientSelectId"+ j).val() == "hciName") {
                    $("#" + i + "patHciName"+ j).show();
                    $("#" + i + "patOthers"+ j).hide();
                    $("#" + i + "patRegNo"+ j).hide();
                } else if ($("#" + i + "patientSelectId"+ j).val() == "regNo") {
                    $("#" + i + "patRegNo"+ j).show();
                    $("#" + i + "patHciName"+ j).hide();
                    $("#" + i + "patOthers"+ j).hide();
                }
            }


        }
    });

    $(document).ready(function () {
        for (var i = 1; i < 3; i++) {
            for (var j = 1; j < 3; j++){
                if ($('#' + i + 'radioNo'+ j).is(':checked')) {
                    $("#" + i + "patYes"+ j).hide();
                    $("#" + i + "patHciName"+ j).hide();
                    $("#" + i + "patOthers"+ j).hide();
                    $("#" + i + "patRegNo"+ j).hide();
                    $("#" + i + "div"+ j).hide();
                }
            }

        }

    });

</script>