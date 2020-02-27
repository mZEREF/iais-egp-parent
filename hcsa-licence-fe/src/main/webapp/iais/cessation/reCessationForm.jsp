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
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${num.count}"></c:out></p>
                                </td>
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${appCess.licenceNo}"></c:out></p>
                                </td>
                                <td class="col-xs-1" align="center">
                                    <p><c:out value="${appCess.svcName}"></c:out></p>
                                </td>
                                    <%--                                <td colspan="6" class="col-xs-9">--%>
                                    <%--                                    <table class="table" border="1" cellspacing="0" cellpadding="0">--%>
                                <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid">
                                    <%--                                            <tr>--%>
                                    <td class="col-xs-1" align="center">
                                        <p><c:out value="${appCessHci.hciName}"></c:out></p>
                                    </td>
                                    <td class="col-xs-1" align="center">
                                        <p><c:out value="${appCessHci.hciAddress}"></c:out></p>
                                    </td>
                                    <td class="col-xs-1" align="center">
                                        <iais:datePicker id="effectiveDate"
                                                         name="${num.count}effectiveDate"
                                                         dateVal="${appCessationDtos[num.index].effectiveDate}"/>
                                        <span id="error_effectiveDate" name="iaisErrorMsg"
                                              class="error-msg"></span>
                                    </td>
                                    <td class="col-xs-2" align="center">
                                        <iais:select id="${num.count}cessationReasonId"
                                                     name="${num.count}cessationReason"
                                                     options="reasonOption"
                                                     firstOption="Please select"
                                                     onchange="javascirpt:changeReason(this.value);"
                                                     value="${appCessationDtos[num.index].cessationReason}"/>
                                        <span id="error_cessationReason" name="iaisErrorMsg"
                                              class="error-msg"></span>
                                        <div id="${num.count}reason" hidden><input
                                                type="text"
                                                name="${num.count}otherReason">
                                            <span id="error_otherReason" name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </div>
                                    </td>
                                    <td class="col-xs-3" align="center">
                                        <table>
                                            <tr>
                                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="radio"
                                                           name="${num.count}patRadio"
                                                           value="yes"
                                                           id="${num.count}radioYes"
                                                           <c:if test="${appCessationDtos[num.index].patRadio == 'yes'}">checked</c:if>
                                                           onchange="javascirpt:changePatSelect(this.value);">Yes
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="radio"
                                                           name="${num.count}patRadio"
                                                           value="no"
                                                           id="${num.count}radioNo"
                                                           <c:if test="${appCessationDtos[num.index].patRadio == 'no'}">checked</c:if>
                                                           onchange="javascirpt:changePatSelect(this.value);">No
                                                </td>
                                                <span id="error_patRadio" name="iaisErrorMsg"
                                                      class="error-msg"></span>
                                            </tr>
                                            <tr id="${num.count}patYes" hidden>
                                                <td>
                                                    <div>Patients'Record will</div>
                                                    <div>be transferred to</div>
                                                </td>
                                                <td><iais:select
                                                        name="${num.count}patientSelect"
                                                        options="patientsOption"
                                                        firstOption="Please select"
                                                        id="${num.count}patientSelectId"
                                                        onchange="javascirpt:changePatient(this.value);"
                                                        value="${appCessationDtos[num.index].patientSelect}"/></td>
                                                <span id="error_patientSelect" name="iaisErrorMsg"
                                                      class="error-msg"></span>
                                            </tr>
                                            <tr id="${num.count}patHciName" hidden>
                                                <td>HCI Name</td>
                                                <td><input type="text"
                                                           name="${num.count}patHciName"
                                                           value="${appCessationDtos[num.index].patHciName}">
                                                </td>
                                                <span id="error_patHciName" name="iaisErrorMsg"
                                                      class="error-msg"></span>
                                            </tr>
                                            <tr id="${num.count}patRegNo" hidden>
                                                <td>Professional Registered No.</td>
                                                <td><input type="text"
                                                           name="${num.count}patRegNo"
                                                           value="${appCessationDtos[num.index].patRegNo}">
                                                </td>
                                                <span id="error_patRegNo" name="iaisErrorMsg"
                                                      class="error-msg"></span>
                                            </tr>
                                            <tr id="${num.count}patOthers" hidden>
                                                <td>Others</td>
                                                <td><input type="text"
                                                           name="${num.count}patOthers"
                                                           value="${appCessationDtos[num.index].patOthers}">
                                                </td>
                                                <span id="error_patOthers" name="iaisErrorMsg"
                                                      class="error-msg"></span>
                                            </tr>
                                            <tr id="${num.count}patNo" hidden align="center">
                                                <td><textarea
                                                        name="${num.count}patNoRemarks"
                                                        cols="40" rows="4" maxlength="8000"
                                                        title="content"><c:out
                                                        value="${appCessationDtos[num.index].patNoRemarks}"/></textarea>
                                                </td>
                                                <span id="error_patNoRemarks" name="iaisErrorMsg"
                                                      class="error-msg"></span>
                                            </tr>
                                        </table>
                                    </td>
                                    <td class="col-xs-1" align="center">
                                        <input type="checkbox" name="${num.count}whichTodo" value="${appCess.licenceId}"  <c:if test="${appCessationDtos[num.index].whichTodo != null}">checked</c:if>>
                                        <span id="error_whichTodo" name="iaisErrorMsg"
                                              class="error-msg"></span>
                                    </td>
                                    <%--                                            </tr>--%>
                                </c:forEach>
                                    <%--                                    </table>--%>
                                    <%--                                </td>--%>
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
        for (var i = 1; i < 3; i++) {
            if ($("#" + i + "cessationReasonId").val() == "OtherReasons") {
                $("#" + i + "reason").show();
            } else {
                $("#" + i + "reason").hide();
            }
        }
    }

    function changePatient() {
        for (var i = 1; i < 3; i++) {
            if ($("#" + i + "patientSelectId").val() == "Others") {
                $("#" + i + "patOthers").show();
                $("#" + i + "patHciName").hide();
                $("#" + i + "patRegNo").hide();
            } else if ($("#" + i + "patientSelectId").val() == "hciName") {
                $("#" + i + "patHciName").show();
                $("#" + i + "patOthers").hide();
                $("#" + i + "patRegNo").hide();
            } else if ($("#" + i + "patientSelectId").val() == "regNo") {
                $("#" + i + "patRegNo").show();
                $("#" + i + "patHciName").hide();
                $("#" + i + "patOthers").hide();
            }
        }
    }

    function changePatSelect() {
        for (var i = 1; i < 3; i++) {
            if ($('#' + i + 'radioYes').is(':checked')) {
                $("#" + i + "patYes").show();
                $("#" + i + "patNo").hide();
            } else if ($('#' + i + 'radioNo').is(':checked')) {
                $("#" + i + "patNo").show();
                $("#" + i + "patYes").hide();
                $("#" + i + "patHciName").hide();
                $("#" + i + "patOthers").hide();
                $("#" + i + "patRegNo").hide();
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
            if ($("#" + i + "cessationReasonId").val() == "OtherReasons") {
                $("#" + i + "reason").show();
            } else if ($("#" + i + "cessationReasonId").val() != "OtherReasons") {
                $("#" + i + "reason").hide();
            }
            if ($('#' + i + 'radioYes').is(':checked')) {
                $("#" + i + "patYes").show();
                $("#" + i + "patNo").hide();
            } else if ($('#' + i + 'radioNo').is(':checked')) {
                $("#" + i + "patYes").hide();
                $("#" + i + "patHciName").hide();
                $("#" + i + "patOthers").hide();
                $("#" + i + "patRegNo").hide();
                $("#" + i + "patNo").show();
            }
            if ($("#" + i + "patientSelectId").val() == "Others") {
                $("#" + i + "patOthers").show();
                $("#" + i + "patHciName").hide();
                $("#" + i + "patRegNo").hide();
            } else if ($("#" + i + "patientSelectId").val() == "hciName") {
                $("#" + i + "patHciName").show();
                $("#" + i + "patOthers").hide();
                $("#" + i + "patRegNo").hide();
            } else if ($("#" + i + "patientSelectId").val() == "regNo") {
                $("#" + i + "patRegNo").show();
                $("#" + i + "patHciName").hide();
                $("#" + i + "patOthers").hide();
            }

        }
    });

    $(document).ready(function () {
        for (var i = 1; i < 3; i++) {
            if ($('#' + i + 'radioNo').is(':checked')) {
                $("#" + i + "patYes").hide();
                $("#" + i + "patHciName").hide();
                $("#" + i + "patOthers").hide();
                $("#" + i + "patRegNo").hide();
                $("#" + i + "div").hide();
            }
        }

    });

</script>