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
        <div class="row" style="margin-left: 1%;margin-right: 1%">
            <table border="1">
                <thead>
                <tr>
                    <th style="text-align:center;width: 3%">S/N</th>
                    <th style="text-align:center;width: 7%">Licence No.</th>
                    <th style="text-align:center;width: 10%">Service Name</th>
                    <th style="text-align:center;width: 7%">HCI Name</th>
                    <th style="text-align:center;width: 10%">HCI Address</th>
                    <th style="text-align:center;width: 13%">Effective Date <a
                            class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true"
                            data-original-title="<p>The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.</p>">i</a>
                    </th>
                    <th style="text-align:center;width: 14%">Cessation Reasons</th>
                    <th style="text-align:center;width: 28%">Patients' Record will be transferred</th>
                    <th style="text-align:center;width: 8%">To Cease?</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${confirmDtos}" var="appCess" varStatus="num">
                    <c:set var="hciDtoNum" value="${fn:length(appCess.appCessHciDtos)}"/>
                    <tr style="height: 14em">
                        <td style="text-align:center" rowspan="${hciDtoNum}">
                            <p><c:out value="${num.count}"/></p>
                        </td>
                        <td style="text-align:center; padding: 0" rowspan="${hciDtoNum}">
                            <p><c:out value="${appCess.licenceNo}"/></p>
                        </td>
                        <td style="text-align:center" rowspan="${hciDtoNum}">
                            <p><c:out value="${appCess.svcName}"/></p>
                        </td>
                        <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid" begin="0" end="0">
                            <td style="text-align:center"><p><c:out value="${appCessHci.hciName}"></c:out></p></td>
                            <td style="text-align:center"><p><c:out value="${appCessHci.hciAddress}"></c:out></p></td>
                            <td style="text-align:center"><fmt:formatDate value="${appCessHci.effectiveDate}" pattern="dd/MM/yyyy"/></td>
                            <td style="text-align:center"><iais:select disabled="true" id="${num.count}reasonId${uid.count}" name="${num.count}reason${uid.count}" options="reasonOption" value="${appCessHci.reason}"/>
                                <div style="margin-top: 30%" id="${num.count}reason${uid.count}" hidden><iais:input needDisabled="true" type="text" name="${num.count}otherReason${uid.count}" value="${appCessHci.otherReason}"></iais:input></div>
                            </td>
                            <td style="width: 30em; position: relative">
                                <table>
                                    <tr>
                                        <td style="padding-left: 4%;width: 10%;position: absolute;top: 5%;left: 30% ;width: 20%">
                                            <input onclick="return false" type="radio" name="${num.count}patRadio${uid.count}" value="yes" id="${num.count}radioYes${uid.count}" <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if> onchange="javascirpt:changePatSelect(this.value);"> Yes</td>
                                        <td style="padding-left: 2%;width: 10%;position: absolute;top: 5%;right: 30% ;width: 20%">
                                            <input onclick="return false" type="radio" name="${num.count}patRadio${uid.count}" value="no" id="${num.count}radioNo${uid.count}" <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if> onchange="javascirpt:changePatSelect(this.value);"> No</td>
                                    </tr>
                                    <tr id="${num.count}patYes${uid.count}" hidden>
                                        <td style="position: absolute;top: 25%;left: 3% ;width: 40%"><div>Who will take over your patients' case records?</div></td>
                                        <td style="position: absolute;top: 25%;right: 5% ;width: 50%"><iais:select disabled="true" name="${num.count}patientSelect${uid.count}" options="patientsOption" firstOption="Please Select" id="${num.count}patientSelectId${uid.count}" onchange="javascirpt:changePatient(this.value);" value="${appCessHci.patientSelect}"/></td>
                                    </tr>
                                    <tr id="${num.count}patHciName${uid.count}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">HCI Name</td>
                                        <td style="position: absolute;top: 55%;right: 5% ;width: 50%"><iais:input type="text" needDisabled="true" value="${appCessHci.patHciName}" maxLength="100" name="${num.count}patHciName${uid.count}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patRegNo${uid.count}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">Professional Regn No.</td>
                                        <td style="position: absolute;top: 55%;right: 5% ;width: 50%"><iais:input needDisabled="true" cssClass="disabled" type="text" name="${num.count}patRegNo${uid.count}" value="${appCessHci.patRegNo}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patOthers${uid.count}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">Others</td>
                                        <td style="position: absolute;top: 55%;right: 5% ;width: 50%"><iais:input needDisabled="true" type="text" name="${num.count}patOthers${uid.count}" value="${appCessHci.patOthers}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patNo${uid.count}" hidden align="center">
                                        <td style="position: absolute;top: 30%;left: 3% ;width: 40%">Reason for no patients' records transfer</td>
                                        <td style="position: absolute;top: 30%;right: 5% ;width: 50%"><iais:input needDisabled="true" type="text" name="${num.count}patNoRemarks${uid.count}" value="${appCessHci.patNoRemarks}"></iais:input></td>
                                    </tr>
                                </table>
                            </td>
                            <td style="text-align:center"><input type="checkbox" name="${num.count}whichTodo${uid.count}" value="${appCess.licenceId}" <c:if test="${appCessHci.premiseIdChecked != null}">checked</c:if>></td>
                        </c:forEach>
                    </tr>

                    <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid" begin="1">
                        <tr style="height: 14em">
                            <td style="text-align:center"><p><c:out value="${appCessHci.hciName}"></c:out></p></td>
                            <td style="text-align:center"><p><c:out value="${appCessHci.hciAddress}"></c:out></p></td>
                            <td style="text-align:center"><fmt:formatDate value="${appCessHci.effectiveDate}" pattern="dd/MM/yyyy"/></td>
                            <td style="text-align:center"><iais:select disabled="true" id="${num.count}reasonId${uid.count+1}" name="${num.count}reason${uid.count+1}" options="reasonOption" value="${appCessHci.reason}"/>
                                <div style="margin-top: 30%" id="${num.count}reason${uid.count+1}" hidden><iais:input needDisabled="true" type="text" name="${num.count}otherReason${uid.count}" value="${appCessHci.otherReason}"></iais:input></div>
                            </td>
                            <td style="padding-left: 4%;width: 30em; position: relative">
                                <table>
                                    <tr>
                                        <td style="padding-left: 4%;width: 10%;position: absolute;top: 5%;left: 30% ;width: 20%">
                                            <input onclick="return false" type="radio" name="${num.count}patRadio${uid.count+1}" value="yes" id="${num.count}radioYes${uid.count+1}" <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if> onchange="javascirpt:changePatSelect(this.value);"> Yes</td>
                                        <td style="padding-left: 2%;width: 10%;position: absolute;top: 5%;right: 30% ;width: 20%">
                                            <input onclick="return false" type="radio" name="${num.count}patRadio${uid.count+1}" value="no" id="${num.count}radioNo${uid.count+1}" <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if> onchange="javascirpt:changePatSelect(this.value);"> No</td>
                                    </tr>
                                    <tr id="${num.count}patYes${uid.count+1}" hidden>
                                        <td style="position: absolute;top: 25%;left: 3% ;width: 40%"><div>Who will take over your patients' case records?</div></td>
                                        <td style="position: absolute;top: 25%;right: 5% ;width: 50%"><iais:select disabled="true" name="${num.count}patientSelect${uid.count+1}" options="patientsOption" firstOption="Please Select" id="${num.count}patientSelectId${uid.count+1}" onchange="javascirpt:changePatient(this.value);" value="${appCessHci.patientSelect}"/></td>
                                    </tr>
                                    <tr id="${num.count}patHciName${uid.count+1}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">HCI Name</td>
                                        <td style="position: absolute;top: 55%;right: 5% ;width: 50%"><iais:input type="text" needDisabled="true" value="${appCessHci.patHciName}" maxLength="100" name="${num.count}patHciName${uid.count+1}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patRegNo${uid.count+1}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">Professional Regn No.</td>
                                        <td style="position: absolute;top: 55%;right: 5% ;width: 50%"><iais:input needDisabled="true" cssClass="disabled" type="text" name="${num.count}patRegNo${uid.count+1}" value="${appCessHci.patRegNo}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patOthers${uid.count+1}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">Others</td>
                                        <td style="position: absolute;top: 55%;right: 5% ;width: 50%"><iais:input needDisabled="true" type="text" name="${num.count}patOthers${uid.count+1}" value="${appCessHci.patOthers}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patNo${uid.count+1}" hidden align="center">
                                        <td style="position: absolute;top: 30%;left: 3% ;width: 40%">Reason for no patients' records transfer</td>
                                        <td style="position: absolute;top: 30%;right: 5% ;width: 50%"><iais:input needDisabled="true" type="text" name="${num.count}patNoRemarks${uid.count+1}" value="${appCessHci.patNoRemarks}"></iais:input></td>
                                    </tr>
                                </table>
                            </td>
                            <td style="text-align:center"><input type="checkbox" name="${num.count}whichTodo${uid.count+1}" value="${appCess.licenceId}" <c:if test="${appCessHci.premiseIdChecked != null}">checked</c:if>></td>
                        </tr>
                    </c:forEach>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div style="width: 70%;margin-left: 1%;margin-right: 1%"><c:out value="${text1}"/></div>
        <br/>
        <div style="width: 70%;margin-left: 1%;margin-right: 1%"><c:out value="${text2}"/></div>
        <br/>
        <div style="margin-left: 1%;margin-right: 1%"><input type="checkbox" onclick="return false" checked name="sure" id="confirmInfo"><label style="font-weight: normal" for="confirmInfo">I have read the information</label></div>
        <iais:action>
            <a onclick="confirmBack('back')"><em class="fa fa-angle-left" style="margin-bottom: 1%;margin-left: 1%"></em> Back</a>
            <a style="margin-bottom: 1%;margin-left: 87%" class="btn btn-primary" onclick="confirmSubmit('submit')">Submit</a>
        </iais:action>
    </div>
</form>

<style>
    td {
        padding: 1%
    }
    th {
        padding: 1%
    }
    #effectiveDate{
        margin-bottom: 0px;
    }
    input[type='text']{
        margin-bottom: 0px;
    }
</style>
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

    $(document).ready(function () {
        $('input[type="text"]').css('border-color','#ededed');
        $('input[type="text"]').css('color','#999');
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