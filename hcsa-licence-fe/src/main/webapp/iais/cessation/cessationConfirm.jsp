<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<webui:setLayout name="iais-internet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<form id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <div class="main-content">
        <div class="panel-heading"><h2><strong>Cessation Confirmation Form</strong></h2></div>
        <div class="panel-heading"><h4><strong>Please key in to cessation information</strong></h4></div>
        <div class="row">
                    <table border="1">
                        <thead>
                        <tr style="padding: 1%">
                            <th style="text-align:center;padding: 1%;width: 5%">S/N</th>
                            <th style="text-align:center;padding: 1%;width: 5%">Licence No.</th>
                            <th style="text-align:center;padding: 1%;width: 10%">Service Name</th>
                            <th style="text-align:center;padding: 1%;width: 10%">HCI Name</th>
                            <th style="text-align:center;padding: 1%;width: 10%">HCI Address</th>
                            <th style="text-align:center;padding: 1%;width: 12%">Effective Date <a
                                    class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true"
                                    data-original-title="<p>The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.</p>">i</a>
                            </th>
                            <th style="text-align:center;padding: 1%;width: 15%">Cessation Reasons</th>
                            <th style="text-align:center;padding: 1%;width: 28%">Patients' Record will be transferred</th>
                            <th style="text-align:center;padding: 1%;width: 5%"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${confirmDtos}" var="appCess" varStatus="num">
                            <c:set var="hciDtoNum" value="${fn:length(appCess.appCessHciDtos)}"/>
                            <%--                           <c:if test="${hciDtoNum > 1}">--%>
                            <tr style="text-align:center;height: 13em">
                                <td rowspan="${hciDtoNum}">
                                    <p><c:out value="${num.count}"/></p>
                                </td>
                                <td rowspan="${hciDtoNum}">
                                    <p><c:out value="${appCess.licenceNo}"/></p>
                                </td>
                                <td rowspan="${hciDtoNum}">
                                    <p><c:out value="${appCess.svcName}"/></p>
                                </td>
                                <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid" begin="0" end="0">
                                    <td><p><c:out value="${appCessHci.hciName}"></c:out></p></td>
                                    <td><p><c:out value="${appCessHci.hciAddress}"></c:out></p></td>
                                    <td style="padding: 1%"><fmt:formatDate value="${appCessHci.effectiveDate}" pattern="dd/MM/yyyy"/></td>
                                    <td style="margin-right: 1%;padding: 1%"><iais:select disabled="true" id="${num.count}reasonId${uid.count}" name="${num.count}reason${uid.count}" options="reasonOption" value="${appCessHci.reason}"/>
                                        <div style="margin-top: 25%" id="${num.count}reason${uid.count}" hidden><textarea disabled="disabled" style="resize:none" type="text" name="${num.count}otherReason${uid.count}" rows="2" cols="30" maxlength="200" res><c:out value="${appCessHci.otherReason}"/></textarea></div>
                                    </td>
                                    <td style="padding-left: 4%;width: 30em; position: relative">
                                        <table>
                                            <tr>
                                                <td style="padding-left: 4%;width: 10%;position: absolute;top: 5%;left: 30% ;width: 20%">
                                                    <input type="radio" name="${num.count}patRadio${uid.count}" value="yes" id="${num.count}radioYes${uid.count}" <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if> onchange="javascirpt:changePatSelect(this.value);"> Yes</td>
                                                <td style="padding-left: 2%;width: 10%;position: absolute;top: 5%;right: 30% ;width: 20%">
                                                    <input type="radio" name="${num.count}patRadio${uid.count}" value="no" id="${num.count}radioNo${uid.count}" <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if> onchange="javascirpt:changePatSelect(this.value);"> No</td>
                                            </tr>
                                            <tr id="${num.count}patYes${uid.count}" hidden>
                                                <td style="position: absolute;top: 25%;left: 3% ;width: 40%"><div>Who will take over your patients' case records?</div></td>
                                                <td style="position: absolute;top: 25%;right: 5% ;width: 50%"><iais:select disabled="true" name="${num.count}patientSelect${uid.count}" options="patientsOption" firstOption="Please select" id="${num.count}patientSelectId${uid.count}" onchange="javascirpt:changePatient(this.value);" value="${appCessHci.patientSelect}"/></td>
                                            </tr>
                                            <tr id="${num.count}patHciName${uid.count}" hidden><td style="position: absolute;top: 55%;left: 3% ;width: 30%">HCI Name</td><td style="position: absolute;top: 55%;right: 3% ;width: 55%"><textarea rows="2" cols="30" disabled ="disabled" style="resize:none" maxlength="100" name="${num.count}patHciName${uid.count}"><c:out value="${appCessHci.patHciName}"/></textarea></td><span style="position: absolute;top: 85%;left: 10% ;width: 80%" id="error_${num.count}patHciName${uid.count}" name="iaisErrorMsg" class="error-msg"></span>
                                            </tr>
                                            <tr id="${num.count}patRegNo${uid.count}" hidden>
                                                <td style="position: absolute;top: 55%;left: 3% ;width: 30%">Professional Registered No.</td>
                                                <td style="position: absolute;top: 55%;right: 3% ;width: 55%"><textarea style="resize:none" disabled="disabled" maxlength="20" rows="2" cols="30" name="${num.count}patRegNo${uid.count}"><c:out value="${appCessHci.patRegNo}"/></textarea></td>
                                            </tr>
                                            <tr id="${num.count}patOthers${uid.count}" hidden>
                                                <td style="position: absolute;top: 55%;left: 3% ;width: 30%">Others</td>
                                                <td style="position: absolute;top: 55%;right: 3% ;width: 55%"><textarea disabled="disabled" style="resize:none" maxlength="100" rows="2" cols="30" name="${num.count}patOthers${uid.count}"><c:out value="${appCessHci.patOthers}"/></textarea></td>
                                            </tr>
                                            <tr id="${num.count}patNo${uid.count}" hidden align="center">
                                                <td style="position: absolute;top: 30%;left: 3% ;width: 40%">Reason for no patients' records transfer</td>
                                                <td style="position: absolute;top: 30%;right: 3% ;width: 55%"><textarea disabled="disabled" style="resize:none" name="${num.count}patNoRemarks${uid.count}" cols="30" rows="2" maxlength="200" title="content"><c:out
                                                        value="${appCessHci.patNoRemarks}"/></textarea>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td style="width: 2%;"><input type="checkbox" name="${num.count}whichTodo${uid.count}" value="${appCess.licenceId}" <c:if test="${appCessHci.whichTodo != null}">checked</c:if>></td>
                                </c:forEach>
                            </tr>










                            <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid" begin="1">
                                <tr style="text-align:center;height: 13em">
                                    <td><p><c:out value="${appCessHci.hciName}"></c:out></p></td>
                                    <td><p><c:out value="${appCessHci.hciAddress}"></c:out></p></td>
                                    <td style="padding: 1%"><fmt:formatDate value="${appCessHci.effectiveDate}" pattern="dd/MM/yyyy"/>
                                        <span id="error_${num.count}effectiveDate${uid.count+1}"
                                              name="iaisErrorMsg"
                                              class="error-msg"></span></td>
                                    <td><iais:select id="${num.count}reasonId${uid.count+1}"
                                                     name="${num.count}reason${uid.count+1}"
                                                     options="reasonOption"
                                                     value="${appCessHci.reason}" disabled="true"/>
                                        <div style="margin-top: 25%" id="${num.count}reason${uid.count+1}" hidden><input
                                                type="text"
                                                name="${num.count}otherReason${uid.count+1}"
                                                value="${appCessHci.otherReason}" disabled>
                                        </div>
                                    </td>
                                    <td style="padding-left: 4%;width: 30em; position: relative">
                                        <table>
                                            <tr>
                                                <td style="padding-left: 4%;width: 10%;position: absolute;top: 10%;left: 30% ;width: 20%">
                                                    <input type="radio"
                                                           name="${num.count}patRadio${uid.count+1}"
                                                           value="yes"
                                                           id="${num.count}radioYes${uid.count+1}"
                                                           <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                           onchange="javascirpt:changePatSelect(this.value);" disabled> Yes
                                                </td>
                                                <td style="padding-left: 2%;width: 10%;position: absolute;top: 10%;right: 30% ;width: 20%">
                                                    <input type="radio"
                                                           name="${num.count}patRadio${uid.count+1}"
                                                           value="no"
                                                           id="${num.count}radioNo${uid.count+1}"
                                                           <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if> onclick="return false"> No
                                                </td>
                                            </tr>
                                            <tr id="${num.count}patYes${uid.count+1}" hidden>
                                                <td style="position: absolute;top: 30%;left: 3% ;width: 40%">
                                                    <div>Who will take over your patients' case records?</div>
                                                </td>
                                                <td style="width: 57%;position: absolute;top: 30%;right: 3% ;width: 55%">
                                                    <iais:select
                                                            name="${num.count}patientSelect${uid.count+1}"
                                                            options="patientsOption"
                                                            id="${num.count}patientSelectId${uid.count+1}"
                                                            value="${appCessHci.patientSelect}" disabled="true"/></td>
                                            </tr>
                                            <tr id="${num.count}patHciName${uid.count+1}" hidden>
                                                <td style="position: absolute;top: 60%;left: 3% ;width: 30%">HCI Name
                                                </td>
                                                <td style="position: absolute;top: 60%;right: 3% ;width: 55%"><input
                                                        type="text"
                                                        name="${num.count}patHciName${uid.count+1}"
                                                        value="${appCessHci.patHciName}" disabled>
                                                </td>
                                            </tr>
                                            <tr id="${num.count}patRegNo${uid.count+1}" hidden>
                                                <td style="position: absolute;top: 60%;left: 3% ;width: 30%">
                                                    Professional Registered No.
                                                </td>
                                                <td style="position: absolute;top: 60%;right: 3% ;width: 55%"><input
                                                        type="text"
                                                        name="${num.count}patRegNo${uid.count+1}"
                                                        value="${appCessHci.patRegNo}" disabled>
                                                </td>
                                            </tr>
                                            <tr id="${num.count}patOthers${uid.count+1}" hidden>
                                                <td style="position: absolute;top: 60%;left: 3% ;width: 30%">Others</td>
                                                <td style="position: absolute;top: 60%;right: 3% ;width: 55%"><input
                                                        type="text"
                                                        name="${num.count}patOthers${uid.count+1}"
                                                        value="${appCessHci.patOthers}" disabled>
                                                </td>
                                            </tr>
                                            <tr id="${num.count}patNo${uid.count+1}" hidden align="center">
                                                <td style="position: absolute;top: 30%;left: 3% ;width: 40%">Reason for no patients' records transfer</td>
                                                <td style="position: absolute;top: 30%;right: 3% ;width: 55%">
                                                <textarea
                                                        name="${num.count}patNoRemarks${uid.count+1}"
                                                        cols="30" rows="3" maxlength="8000"
                                                        title="content" disabled="disabled" ><c:out
                                                        value="${appCessHci.patNoRemarks}"/></textarea>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td style="width: 2%"><input type="checkbox" name="${num.count}whichTodo${uid.count+1}"
                                                                 value="${appCess.licenceId}"
                                                                 <c:if test="${appCessHci.whichTodo != null}">checked</c:if>>
                                    </td>
                                </tr>
                            </c:forEach>
                            <%--                            </c:if>--%>
                        </c:forEach>
                        </tbody>
                    </table>
        </div>

        <div style="width: 70%;margin-left: 1%;margin-right: 1%"><c:out value="${text1}"/></div>
        <br/>
        <div style="width: 70%;margin-left: 1%;margin-right: 1%"><c:out value="${text2}"/></div>
        <br/>
        <div style="margin-left: 1%;margin-right: 1%"><input type="checkbox" onclick="return false" checked name="sure" id="confirmInfo">
            <label style="font-weight: normal" for="confirmInfo">I have read the information</label>
        </div>


        <div align="right">
            <button id="backButton" type="button" class="btn btn-primary" onclick="confirmBack('back')">
                Back
            </button>
            <button id="submitButton" type="button" class="btn btn-primary" onclick="confirmSubmit('submit')">
                Next
            </button>
        </div>
    </div>
</form>

<script type="text/javascript">

    function confirmSubmit(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }

    function confirmBack(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }


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