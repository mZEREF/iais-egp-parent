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
        <div class="panel-heading"><strong>Cessation Form</strong></div>
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
                                <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci">
                                    <td class="col-xs-1">
                                        <p><c:out value="${appCessHci.hciName}"></c:out></p>
                                    </td>
                                    <td class="col-xs-1">
                                        <p><c:out value="${appCessHci.hciAddress}"></c:out></p>
                                    </td>
                                    <td class="col-xs-1">
                                        <iais:datePicker id="effectiveDate" name="effectiveDate"/>
                                        <span id="error_effectiveDate" name="iaisErrorMsg" class="error-msg"></span>
                                    </td>
                                    <td class="col-xs-1">
                                        <iais:select name="cessationReason" options="reasonOption"
                                                     firstOption="Please select"
                                                     onchange="javascirpt:changeReason(this.value);"/>
                                        <div id="reason" hidden><input type="text" name="otherReason"></div>
                                    </td>
                                    <td class="col-xs-4">
                                        <table>
                                            <tr>
                                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="todo">Yes&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="todo">No</td>
                                            </tr>
                                            <tr>
                                                <td><h6>Patients' Record will be transferred to</h6></td>
                                                <td><iais:select name="patient" options="patientsOption"
                                                                 firstOption="Please select"
                                                                 onchange="javascirpt:changePatient(this.value);"/></td>
                                            </tr>
                                            <tr id="patHciName" hidden>
                                                <td>HCI Name</td>
                                                <td><input type="text" name="patHciName"></td>
                                            </tr>
                                            <tr id="patRegNo" hidden>
                                                <td>Professional Registered No.</td>
                                                <td><input type="text" name="patRegNo"></td>
                                            </tr>
                                            <tr id="patOthers" hidden>
                                                <td>Others</td>
                                                <td><input type="text" name="patOthers"></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td class="col-xs-1">
                                        <input type="checkbox" name="todo">
                                    </td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td colspan="9"><c:out value="${text1}"/></td>
                        </tr>
                        <tr>
                            <td colspan="9"><c:out value="${text2}"/></td>
                        </tr>
                        <tr>
                            <td colspan="9"><input type="checkbox" name="sure">I have read the information</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    function changeReason(obj) {
        if (obj == "Others") {
            $("#reason").show();
        } else {
            document.getElementById("reason").style.display = "none";
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


</script>