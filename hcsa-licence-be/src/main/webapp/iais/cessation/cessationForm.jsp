<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <div class="panel-heading"><h2><strong>Cessation Form</strong></h2> <span id="readInfo" class="error-msg"
                                                                                  hidden>Please agree to the declaration statement</span>
        </div>
        <div class="panel-heading"><h4><strong>Please key in to cessation information</strong></h4>
            <span id="error_choose" name="iaisErrorMsg" class="error-msg"/>
        </div>
        <div class="row" style="margin-left: 1%;margin-right: 1%">
            <table border="1">
                <thead>
                <tr>
                    <th style="text-align:center;width: 3%;padding: 0">S/N</th>
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
                <c:forEach items="${appCessationDtos}" var="appCess" varStatus="num">
                    <c:set var="hciDtoNum" value="${fn:length(appCess.appCessHciDtos)}"/>
                    <tr style="height: 14em">
                        <td style="text-align:center" rowspan="${hciDtoNum}">
                            <p><c:out value="${num.count}"/></p>
                        </td>
                        <td style="text-align:center;padding: 0" rowspan="${hciDtoNum}">
                            <p><c:out value="${appCess.licenceNo}"/></p>
                        </td>
                        <td style="text-align:center" rowspan="${hciDtoNum}">
                            <p><c:out value="${appCess.svcName}"/></p>
                        </td>
                        <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid" begin="0" end="0">
                            <td style="text-align:center"><p><c:out value="${appCessHci.hciName}"></c:out></p></td>
                            <td style="text-align:center"><p><c:out value="${appCessHci.hciAddress}"></c:out></p></td>
                            <td>
                                <iais:datePicker id="effectiveDate" name="${num.count}effectiveDate${uid.count}" dateVal="${appCessHci.effectiveDate}"/>
                            </td>
                            <td><iais:select needErrorSpan="false" name="${num.count}reason${uid.count}" id="${num.count}reasonId${uid.count}" options="reasonOption" firstOption="Please Select" onchange="javascirpt:changeReason(this.value);" value="${appCessHci.reason}"/>
                                <div style="margin-top: 40%" id="${num.count}reason${uid.count}" hidden>
                                    <iais:input type="text" maxLength="200" name="${num.count}otherReason${uid.count}" value="${appCessHci.otherReason}"></iais:input>
                                </div>
                                <span id="error_${num.count}reason${uid.count}" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td style="width: 30em; position: relative">
                                <table>
                                    <tr>
                                        <td style="padding-left: 4%;width: 10%;position: absolute;top: 5%;left: 30% ;width: 20%">
                                            <input type="radio" name="${num.count}patRadio${uid.count}" value="yes"
                                                   id="${num.count}radioYes${uid.count}"
                                                   <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                   onchange="javascirpt:changePatSelect(this.value);"> Yes
                                            <span style="position: absolute;top: 70%;left: 20% ;width: 200%"
                                                  id="error_${num.count}patRadio${uid.count}" name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </td>
                                        <td style="padding-left: 2%;width: 10%;position: absolute;top: 5%;right: 30% ;width: 20%">
                                            <input type="radio" name="${num.count}patRadio${uid.count}" value="no"
                                                   id="${num.count}radioNo${uid.count}"
                                                   <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                                   onchange="javascirpt:changePatSelect(this.value);"> No
                                        </td>
                                    </tr>
                                    <tr id="${num.count}patYes${uid.count}" hidden>
                                        <td style="position: absolute;top: 25%;left: 3% ;width: 40%">
                                            <div>Who will take over your patients' case records?</div>
                                        </td>
                                        <td style="position: absolute;top: 25%;right: 3% ;width: 55%"><iais:select
                                                name="${num.count}patientSelect${uid.count}" options="patientsOption"
                                                firstOption="Please Select" id="${num.count}patientSelectId${uid.count}"
                                                onchange="javascirpt:changePatient(this.value);"
                                                value="${appCessHci.patientSelect}"/></td>
                                    </tr>
                                    <tr id="${num.count}patHciName${uid.count}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">HCI Name</td>
                                        <td style="position: absolute;top: 55%;right: 3% ;width: 55%"><iais:input
                                                type="text" value="${appCessHci.patHciName}" maxLength="100"
                                                name="${num.count}patHciName${uid.count}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patRegNo${uid.count}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">Professional Regn No.</td>
                                        <td style="position: absolute;top: 55%;right: 3% ;width: 55%"><iais:input
                                                type="text" maxLength="20" name="${num.count}patRegNo${uid.count}"
                                                value="${appCessHci.patRegNo}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patOthers${uid.count}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">Others</td>
                                        <td style="position: absolute;top: 55%;right: 3% ;width: 55%"><iais:input
                                                type="text" value="${appCessHci.patOthers}" maxLength="100"
                                                name="${num.count}patOthers${uid.count}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patNo${uid.count}" hidden align="center">
                                        <td style="position: absolute;top: 30%;left: 3% ;width: 40%">Reason for no
                                            patients' records transfer
                                        </td>
                                        <td style="position: absolute;top: 30%;right: 3% ;width: 55%"><iais:input
                                                type="text" value="${appCessHci.patNoRemarks}" maxLength="200"
                                                name="${num.count}patNoRemarks${uid.count}"></iais:input>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td style="text-align:center"><input type="checkbox" name="${num.count}whichTodo${uid.count}"
                                                          value="${appCess.licenceId}"
                                                          <c:if test="${appCessHci.whichTodo != null}">checked</c:if>>
                                <span id="error_whichTodo" name="iaisErrorMsg" class="error-msg"></span></td>
                        </c:forEach>
                    </tr>

                    <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid" begin="1">
                        <tr style="height: 14em">
                            <td style="text-align:center"><p><c:out value="${appCessHci.hciName}"></c:out></p></td>
                            <td style="text-align:center"><p><c:out value="${appCessHci.hciAddress}"></c:out></p></td>
                            <td>
                                <iais:datePicker id="effectiveDate" name="${num.count}effectiveDate${uid.count+1}" dateVal="${appCessHci.effectiveDate}"/>
                            </td>
                            <td><iais:select
                                    name="${num.count}reason${uid.count+1}" id="${num.count}reasonId${uid.count+1}"
                                    options="reasonOption" firstOption="Please Select"
                                    onchange="javascirpt:changeReason(this.value);"
                                    value="${appCessHci.reason}" needErrorSpan="false"/>
                                <div style="margin-top: 30%" id="${num.count}reason${uid.count+1}" hidden>
                                    <iais:input type="text" maxLength="200" name="${num.count}otherReason${uid.count+1}" value="${appCessHci.otherReason}"></iais:input>
                                </div>
                                <span id="error_${num.count}reason${uid.count+1}" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td style="padding-left: 4%;width: 30em; position: relative">
                                <table>
                                    <tr>
                                        <td style="padding-left: 4%;width: 10%;position: absolute;top: 5%;left: 30% ;width: 20%">
                                            <input type="radio" name="${num.count}patRadio${uid.count+1}" value="yes"
                                                   id="${num.count}radioYes${uid.count+1}"
                                                   <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                   onchange="javascirpt:changePatSelect(this.value);"> Yes
                                            <span style="position: absolute;top: 70%;left: 20% ;width: 200%"
                                                  id="error_${num.count}patRadio${uid.count+1}" name="iaisErrorMsg"
                                                  class="error-msg"></span>
                                        </td>
                                        <td style="padding-left: 2%;width: 10%;position: absolute;top: 5%;right: 30% ;width: 20%">
                                            <input type="radio" name="${num.count}patRadio${uid.count+1}" value="no"
                                                   id="${num.count}radioNo${uid.count+1}"
                                                   <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                                   onchange="javascirpt:changePatSelect(this.value);"> No
                                        </td>
                                    </tr>
                                    <tr id="${num.count}patYes${uid.count+1}" hidden>
                                        <td style="position: absolute;top: 25%;left: 3% ;width: 40%">
                                            <div>Who will take over your patients' case records?</div>
                                        </td>
                                        <td style="position: absolute;top: 25%;right: 3% ;width: 55%"><iais:select
                                                name="${num.count}patientSelect${uid.count+1}" options="patientsOption"
                                                firstOption="Please Select" id="${num.count}patientSelectId${uid.count+1}"
                                                onchange="javascirpt:changePatient(this.value);"
                                                value="${appCessHci.patientSelect}"/></td>
                                    </tr>
                                    <tr id="${num.count}patHciName${uid.count+1}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">HCI Name</td>
                                        <td style="position: absolute;top: 55%;right: 3% ;width: 55%"><iais:input
                                                type="text" value="${appCessHci.patHciName}" maxLength="100"
                                                name="${num.count}patHciName${uid.count+1}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patRegNo${uid.count+1}" hidden>
                                        <td style="position: absolute;top: 55%;left: 4% ;width: 30%">Professional Regn
                                            No.
                                        </td>
                                        <td style="position: absolute;top: 55%;right: 3% ;width: 55%"><iais:input
                                                type="text" maxLength="20" name="${num.count}patRegNo${uid.count+1}"
                                                value="${appCessHci.patRegNo}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patOthers${uid.count+1}" hidden>
                                        <td style="position: absolute;top: 55%;left: 3% ;width: 30%">Others</td>
                                        <td style="position: absolute;top: 55%;right: 3% ;width: 55%"><iais:input
                                                type="text" value="${appCessHci.patOthers}" maxLength="100"
                                                name="${num.count}patOthers${uid.count+1}"></iais:input></td>
                                    </tr>
                                    <tr id="${num.count}patNo${uid.count+1}" hidden align="center">
                                        <td style="position: absolute;top: 30%;left: 3% ;width: 40%">Reason for no
                                            patients' records transfer
                                        </td>
                                        <td style="position: absolute;top: 30%;right: 3% ;width: 55%"><iais:input
                                                type="text" value="${appCessHci.patNoRemarks}" maxLength="200"
                                                name="${num.count}patNoRemarks${uid.count+1}"></iais:input>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td style="text-align:center"><input type="checkbox" name="${num.count}whichTodo${uid.count+1}"
                                                          value="${appCess.licenceId}"
                                                          <c:if test="${appCessHci.whichTodo != null}">checked</c:if>>
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
        <div style="margin-left: 1%;margin-right: 1%"><input type="checkbox" name="readInfo" id="confirmInfo" <c:if test="${readInfo != null}">checked</c:if>>
            <label style="font-weight: normal" for="confirmInfo">I have read the information</label>
        </div>
        <iais:action>
            <a href="/main-web/eservice/INTERNET/MohInternetInbox" style="margin-bottom: 1%;margin-left: 1%"><em class="fa fa-angle-left"></em> Back</a>
            <a style="margin-bottom: 1%;margin-left: 89%" class="btn btn-primary" onclick="submitSure('submit')">Next</a>
        </iais:action>
    </div>
    <%@include file="/include/validation.jsp" %>
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



    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }


    function submitSure(action) {
        if ($('#confirmInfo').is(':checked')) {
            submit(action);
            $("#readInfo").hide();
        } else {
            $("#readInfo").show();
        }
    }

    function back(action) {
        submit(action);
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