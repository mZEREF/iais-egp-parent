<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <div class="panel-heading"><h2><strong>Cessation Confirm Form</strong></h2></div>
        <div class="panel-heading"><h4><strong>Please key in to cessation information</strong></h4></div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table" border="1" cellspacing="0" cellpadding="0" width="100%">
                        <thead>
                        <tr>
                            <th style="text-align:center;">S/N</th>
                            <th style="text-align:center;width: 10%">Licence No.</th>
                            <th style="text-align:center;">Service Name</th>
                            <th style="text-align:center;">HCI Name</th>
                            <th style="text-align:center;width: 15%">HCI Address</th>
                            <th style="text-align:center;width: 10%">Effective Date <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" data-original-title="<p>The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.</p>">i</a></th>
                            <th style="text-align:center;">Cessation Reasons</th>
                            <th style="text-align:center;">Patients' Record will be transferred</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${confirmDtos}" var="appCess" varStatus="num">
                            <c:set var="hciDtoNum" value="${fn:length(appCess.appCessHciDtos)}"/>
                            <%--                           <c:if test="${hciDtoNum > 1}">--%>
                            <tr style="text-align:center;">
                                <td rowspan="${hciDtoNum}">
                                    <p><c:out value="${num.count}"></c:out></p>
                                </td>
                                <td rowspan="${hciDtoNum}">
                                    <p><c:out value="${appCess.licenceNo}"></c:out></p>
                                </td>
                                <td rowspan="${hciDtoNum}">
                                    <p><c:out value="${appCess.svcName}"></c:out></p>
                                </td>
                                <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid" begin="0" end="0">
                                    <td><p><c:out value="${appCessHci.hciName}"></c:out></p></td>
                                    <td><p><c:out value="${appCessHci.hciAddress}"></c:out></p></td>
                                    <td><iais:datePicker id="effectiveDate"
                                                         name="${num.count}effectiveDate${uid.count}"
                                                         dateVal="${appCessHci.effectiveDate}"/>
                                        <span id="error_${num.count}effectiveDate${uid.count}"
                                              name="iaisErrorMsg"
                                              class="error-msg"></span></td>
                                    <td><iais:select id="${num.count}reasonId${uid.count}"
                                                     name="${num.count}reason${uid.count}"
                                                     options="reasonOption"
                                                     firstOption="Please select"
                                                     onchange="javascirpt:changeReason(this.value);"
                                                     value="${appCessHci.reason}"/>
                                        <span id="error_${num.count}reason${uid.count}" name="iaisErrorMsg"
                                              class="error-msg"></span>
                                        <div id="${num.count}reason${uid.count}" hidden><input
                                                type="text"
                                                name="${num.count}otherReason${uid.count}"
                                                value="${appCessHci.otherReason}">
                                            <span id="error_${num.count}otherReason${uid.count}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </div></td>
                                    <td><table>
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
                                            <span id="error_${num.count}patRadio${uid.count}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patYes${uid.count}" hidden>
                                            <td>
                                                <div>Patients'Record will</div>
                                                <div>be transferred to</div>
                                            </td>
                                            <td style="width: 57%"><iais:select
                                                    name="${num.count}patientSelect${uid.count}"
                                                    options="patientsOption"
                                                    firstOption="Please select"
                                                    id="${num.count}patientSelectId${uid.count}"
                                                    onchange="javascirpt:changePatient(this.value);"
                                                    value="${appCessHci.patientSelect}"/></td>
                                            <span id="error_${num.count}patientSelect${uid.count}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patHciName${uid.count}" hidden>
                                            <td>HCI Name</td>
                                            <td><input type="text"
                                                       name="${num.count}patHciName${uid.count}"
                                                       value="${appCessHci.patHciName}">
                                            </td>
                                            <span id="error_${num.count}patHciName${uid.count}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patRegNo${uid.count}" hidden>
                                            <td>Professional Registered No.</td>
                                            <td><input type="text"
                                                       name="${num.count}patRegNo${uid.count}"
                                                       value="${appCessHci.patRegNo}">
                                            </td>
                                            <span id="error_${num.count}patRegNo${uid.count}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patOthers${uid.count}" hidden>
                                            <td>Others</td>
                                            <td><input type="text"
                                                       name="${num.count}patOthers${uid.count}"
                                                       value="${appCessHci.patOthers}">
                                            </td>
                                            <span id="error_${num.count}patOthers${uid.count}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patNo${uid.count}" hidden align="center">
                                            <td><textarea
                                                    name="${num.count}patNoRemarks${uid.count}"
                                                    cols="40" rows="4" maxlength="8000"
                                                    title="content"><c:out
                                                    value="${appCessHci.patNoRemarks}"/></textarea>
                                            </td>
                                            <span id="error_${num.count}patNoRemarks${uid.count}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                    </table></td>
                                    <td><input type="checkbox" name="${num.count}whichTodo${uid.count}"
                                               <c:if test="${appCessHci.whichTodo != null}">checked</c:if>>
                                        <span id="error_whichTodo" name="iaisErrorMsg"
                                              class="error-msg"></span></td>
                                </c:forEach>
                            </tr>
                            <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid" begin="1">
                                <tr style="text-align:center;">
                                    <td><p><c:out value="${appCessHci.hciName}"></c:out></p></td>
                                    <td><p><c:out value="${appCessHci.hciAddress}"></c:out></p></td>
                                    <td><iais:datePicker id="effectiveDate"
                                                         name="${num.count}effectiveDate${uid.count+1}"
                                                         dateVal="${appCessHci.effectiveDate}"/>
                                        <span id="error_${num.count}effectiveDate${uid.count+1}"
                                              name="iaisErrorMsg"
                                              class="error-msg"></span></td>
                                    <td><iais:select id="${num.count}reasonId${uid.count+1}"
                                                     name="${num.count}reason${uid.count+1}"
                                                     options="reasonOption"
                                                     firstOption="Please select"
                                                     onchange="javascirpt:changeReason(this.value);"
                                                     value="${appCessHci.reason}"/>
                                        <span id="error_${num.count}reason${uid.count+1}" name="iaisErrorMsg"
                                              class="error-msg"></span>
                                        <div id="${num.count}reason${uid.count+1}" hidden><input
                                                type="text"
                                                name="${num.count}otherReason${uid.count+1}"
                                                value="${appCessHci.otherReason}">
                                            <span id="error_${num.count}otherReason${uid.count+1}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </div></td>
                                    <td><table>
                                        <tr style="text-align:center;">
                                            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <input type="radio"
                                                       name="${num.count}patRadio${uid.count+1}"
                                                       value="yes"
                                                       id="${num.count}radioYes${uid.count+1}"
                                                       <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                       onchange="javascirpt:changePatSelect(this.value);">Yes
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <input type="radio"
                                                       name="${num.count}patRadio${uid.count+1}"
                                                       value="no"
                                                       id="${num.count}radioNo${uid.count+1}"
                                                       <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                                       onchange="javascirpt:changePatSelect(this.value);">No
                                            </td>
                                            <span id="error_${num.count}patRadio${uid.count+1}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patYes${uid.count+1}" style="text-align:center;" hidden>
                                            <td>
                                                <div>Patients'Record will</div>
                                                <div>be transferred to</div>
                                            </td>
                                            <td style="width: 57%"><iais:select
                                                    name="${num.count}patientSelect${uid.count+1}"
                                                    options="patientsOption"
                                                    firstOption="Please select"
                                                    id="${num.count}patientSelectId${uid.count+1}"
                                                    onchange="javascirpt:changePatient(this.value);"
                                                    value="${appCessHci.patientSelect}"/></td>
                                            <span id="error_${num.count}patientSelect${uid.count+1}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patHciName${uid.count+1}" style="text-align:center;" hidden>
                                            <td>HCI Name</td>
                                            <td><input type="text"
                                                       name="${num.count}patHciName${uid.count+1}"
                                                       value="${appCessHci.patHciName}">
                                            </td>
                                            <span id="error_${num.count}patHciName${uid.count+1}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patRegNo${uid.count+1}" style="text-align:center;" hidden>
                                            <td>Professional Registered No.</td>
                                            <td><input type="text"
                                                       name="${num.count}patRegNo${uid.count+1}"
                                                       value="${appCessHci.patRegNo}">
                                            </td>
                                            <span id="error_${num.count}patRegNo${uid.count+1}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patOthers${uid.count+1}" hidden>
                                            <td>Others</td>
                                            <td><input type="text"
                                                       name="${num.count}patOthers${uid.count+1}"
                                                       value="${appCessHci.patOthers}">
                                            </td>
                                            <span id="error_${num.count}patOthers${uid.count+1}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                        <tr id="${num.count}patNo${uid.count+1}" hidden style="text-align:center;">
                                            <td><textarea
                                                    name="${num.count}patNoRemarks${uid.count+1}"
                                                    cols="40" rows="4" maxlength="8000"
                                                    title="content"><c:out
                                                    value="${appCessHci.patNoRemarks}"/></textarea>
                                            </td>
                                            <span id="error_${num.count}patNoRemarks${uid.count+1}"
                                                  name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </tr>
                                    </table></td>
                                    <td><input type="checkbox" name="${num.count}whichTodo${uid.count+1}"
                                               <c:if test="${appCessHci.whichTodo != null}">checked</c:if>>
                                            <%--                                       <span id="error_whichTodo" name="iaisErrorMsg"--%>
                                            <%--                                             class="error-msg"></span>--%>
                                    </td>
                                </tr>
                            </c:forEach>
                            <%--                            </c:if>--%>
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
            <button id="backButton" type="button" class="btn btn-primary" onclick="confirmBack()">
                Back
            </button>
            <button id="submitButton" type="button" class="btn btn-primary" onclick="confirmSubmit()">
                Next
            </button>
        </div>
    </div>
</form>

<script type="text/javascript">
    function changeReason() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "reasonId" + j).val() == "CES001") {
                    $("#" + i + "reason" + j).show();
                } else {
                    $("#" + i + "reason" + j).hide();
                }
            }

        }
    }

    function changePatient() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "patientSelectId" + j).val() == "CES004") {
                    $("#" + i + "patOthers" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES005") {
                    $("#" + i + "patHciName" + j).show();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES006") {
                    $("#" + i + "patRegNo" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                }
            }

        }
    }

    function changePatSelect() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioYes' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).show();
                    $("#" + i + "patNo" + j).hide();
                } else if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patNo" + j).show();
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
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
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "reasonId" + j).val() == "CES001") {
                    $("#" + i + "reason" + j).show();
                } else if ($("#" + i + "reasonId" + j).val() != "CES001") {
                    $("#" + i + "reason" + j).hide();
                }
                if ($('#' + i + 'radioYes' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).show();
                    $("#" + i + "patNo" + j).hide();
                } else if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "patNo" + j).show();
                }
                if ($("#" + i + "patientSelectId" + j).val() == "CES004") {
                    $("#" + i + "patOthers" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES005") {
                    $("#" + i + "patHciName" + j).show();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES006") {
                    $("#" + i + "patRegNo" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                }
            }


        }
    });

    $(document).ready(function () {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "patOthers" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "div" + j).hide();
                }
            }

        }
    });

</script>