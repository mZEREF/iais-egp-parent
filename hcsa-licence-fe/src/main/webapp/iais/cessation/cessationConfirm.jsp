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
                                        <fmt:formatDate value="${appCessationConfirmDto.effectiveDate}"
                                                        pattern="dd/MM/yyyy"></fmt:formatDate>
                                    </td>
                                    <td class="col-xs-1">
                                            <c:out value="${appCessationConfirmDto.cessationReason}"/>
                                        <c:if test="${appCessationConfirmDto.otherReason !=null}">
                                            <div id="reason"><input type="text" disabled name="otherReason" value="${appCessationConfirmDto.otherReason}">
                                            </div>
                                        </c:if>

                                    </td>
                                    <td class="col-xs-4">
                                        <table>
                                            <tr>
                                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="radio" name="patRadio"  <c:if test="${appCessationConfirmDto.patRadio=='yes'}">checked</c:if> value="yes" id="radioYes" onclick="return false">Yes
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="radio" name="patRadio" value="no" <c:if test="${appCessationConfirmDto.patRadio=='no'}">checked</c:if> id="radioNo" onclick="return false">No</td>
                                            </tr>
                                            <tr id="patYes">
                                                <td><h6>Patients' Record will be transferred to</h6></td>
                                                <td>${appCessationConfirmDto.patientSelect}</td>
                                            </tr>
                                            <c:if test="${appCessationConfirmDto.patNoRemarks !=null}">
                                            <tr id="patNo" hidden align="center">
                                                <td><textarea name="patNoRemarks" disabled cols="40" rows="4" maxlength="8000" title="content">${appCessationConfirmDto.patNoRemarks}</textarea></td>
                                            </tr>
                                            </c:if>
                                            <c:if test="${appCessationConfirmDto.patHciName !=null}">
                                            <tr id="patHciName">
                                                <td>HCI Name</td>
                                                <td><input type="text" disabled name="patHciName" value="${appCessationConfirmDto.patHciName}"></td>
                                            </tr>
                                            </c:if>
                                            <c:if test="${appCessationConfirmDto.patRegNo !=null}">
                                            <tr id="patRegNo">
                                                <td>Professional Registered No.</td>
                                                <td><input type="text" disabled name="patRegNo" value="${appCessationConfirmDto.patRegNo}"></td>
                                            </tr>
                                            </c:if>
                                            <c:if test="${appCessationConfirmDto.patOthers !=null}">
                                            <tr id="patOthers">
                                                <td>Others</td>
                                                <td><input type="text" disabled name="patOthers" value="${appCessationConfirmDto.patOthers}"></td>
                                            </tr>
                                            </c:if>
                                        </table>
                                    </td>
                                    <td class="col-xs-1">
                                        <input type="checkbox" name="whichTodo">
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
                            <td colspan="9"><input type="checkbox" onclick="return false" checked name="sure" id="confirmInfo">I have read the information</td>
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

   function confirmSubmit() {
       $("[name='action_type']").val("submit");
       $("#mainForm").submit();
   }

   function confirmBack() {
       $("[name='action_type']").val("back");
       $("#mainForm").submit();
   }
</script>