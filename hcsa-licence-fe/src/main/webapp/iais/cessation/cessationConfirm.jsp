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
                        <c:forEach items="${appCessConDtos}" var="appCess" varStatus="num">
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
                                <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci">
                                    <td class="col-xs-1" align="center">
                                        <p><c:out value="${appCessHci.hciName}"></c:out></p>
                                    </td>
                                    <td class="col-xs-1" align="center">
                                        <p><c:out value="${appCessHci.hciAddress}"></c:out></p>
                                    </td>
                                    <td class="col-xs-1" align="center">
                                        <fmt:formatDate value="${appCessationDtos[num.index].effectiveDate}"
                                                        pattern="dd/MM/yyyy"></fmt:formatDate>
                                    </td>
                                    <td class="col-xs-2" align="center">
                                        <iais:select id="${num.count}cessationReasonId"
                                                     name="${num.count}cessationReason"
                                                     options="reasonOption"
                                                     value="${appCessationDtos[num.index].reason}" disabled="true"/>
                                        <c:if test="${appCessationDtos[num.index].otherReason !=null}">
                                            <div id="reason"><input type="text" disabled name="otherReason" value="${appCessationDtos[num.index].otherReason}">
                                            </div>
                                        </c:if>
                                    </td>
                                    <td class="col-xs-3" align="center">
                                        <table>
                                            <tr>
                                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="radio"
                                                           name="${num.count}patRadio"
                                                           value="yes"
                                                           id="${num.count}radioYes"
                                                           <c:if test="${appCessationDtos[num.index].patNeedTrans==true}">checked</c:if> onclick="return false">Yes
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="radio"
                                                           name="${num.count}patRadio"
                                                           value="no"
                                                           id="${num.count}radioNo"
                                                           <c:if test="${appCessationDtos[num.index].patNeedTrans==false}">checked</c:if> onclick="return false">No
                                                </td>
                                            </tr>
                                            <tr id="${num.count}patYes" hidden>
                                                <td><h6>Patients' Record will be transferred to</h6>
                                                </td>
                                                <td><iais:select
                                                        name="${num.count}patientSelect"
                                                        options="patientsOption"
                                                        firstOption="Please select"
                                                        id="${num.count}patientSelectId"
                                                        value="${appCessationDtos[num.index].patientSelect}" disabled="true"/></td>
                                            </tr>
                                            <tr id="${num.count}patNo" hidden
                                                align="center">
                                                <td><textarea
                                                        name="${num.count}patNoRemarks"
                                                        cols="40" rows="4" maxlength="8000"
                                                        title="content" disabled="disabled" ><c:out
                                                        value="${appCessationDtos[num.index].patNoRemarks}"/></textarea>
                                                </td>
                                            </tr>
                                            <tr id="${num.count}patHciName" hidden>
                                                <td>HCI Name</td>
                                                <td><input type="text"
                                                           name="${num.count}patHciName"
                                                           value="${appCessationDtos[num.index].patHciName}" readonly>
                                                </td>
                                            </tr>
                                            <tr id="${num.count}patRegNo" hidden>
                                                <td>Professional Registered No.</td>
                                                <td><input type="text"
                                                           name="${num.count}patRegNo"
                                                           value="${appCessationDtos[num.index].patRegNo}" readonly>
                                                </td>
                                            </tr>
                                            <tr id="${num.count}patOthers" hidden>
                                                <td>Others</td>
                                                <td><input type="text"
                                                           name="${num.count}patOthers"
                                                           value="${appCessationDtos[num.index].patOthers}" readonly>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td class="col-xs-1" align="center">
                                        <input type="checkbox" name="whichTodo" value="${appCess.licenceId}"  <c:if test="${appCessationDtos[num.index].whichTodo !=null}">checked</c:if> onclick="return false" >
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
                            <td colspan="9"><input type="checkbox" onclick="return false" checked name="sure" id="confirmInfo">
                                <label for="confirmInfo">I have read the information</label>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
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


   $(document).ready(function () {
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