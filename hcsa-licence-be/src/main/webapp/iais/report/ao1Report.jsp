<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
    <input type="hidden" name="action_type" value="">
    <div class="row">
        <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section A (HCI Details)</h4></strong>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Licence No</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.licenceNo}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Service Name</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.serviceName}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>HCI Code</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.hciCode}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>HCI Name</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.hciName}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>HCI Address</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.hciAddress}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Licensee Name:</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.licenseeName}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Principal Officer</p>
                            </td>
                            <td class="col-xs-4">
                                <c:if test="${insRepDto.principalOfficers != null && not empty insRepDto.principalOfficers}">
                                    <p><c:forEach items="${insRepDto.principalOfficers}" var="poName">
                                        <c:out value="${poName}"/><br>
                                    </c:forEach></p>
                                </c:if>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Subsumed Services</p>
                            </td>
                            <td class="col-xs-4">
                                <c:if test="${insRepDto.subsumedServices != null && not empty insRepDto.subsumedServices}">
                                    <c:forEach var="service" items="${insRepDto.subsumedServices}">
                                        <p><c:out value="${service}"></c:out></p>
                                    </c:forEach>
                                </c:if>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section B (Type of Inspection)</h4></strong>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Date of Inspection</p>
                            </td>
                            <td class="col-xs-4">
                                <fmt:formatDate value="${insRepDto.inspectionDate}"
                                                pattern="dd/MM/yyyy"></fmt:formatDate>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Time of Inspection</p>
                            </td>
                            <td class="col-xs-4">
                                <c:out value="${insRepDto.inspectionStartTime}"></c:out>-<c:out value="${insRepDto.inspectionEndTime}"></c:out>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Reason for Visit</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.reasonForVisit}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Inspected By</p>
                            </td>
                            <td class="col-xs-4">
                                <c:if test="${insRepDto.inspectors != null && not empty insRepDto.inspectors}">
                                    <p><c:forEach items="${insRepDto.inspectors}" var="inspector" varStatus="status">
                                    <p><c:out value="${inspector}"></c:out></p>
                                </c:forEach></p>
                                </c:if>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Other Inspection Officer</p>
                            </td>
                            <td class="col-xs-4">
                                <c:if test="${insRepDto.inspectOffices != null && not empty insRepDto.inspectOffices}">
                                    <p><c:forEach items="${insRepDto.inspectOffices}" var="ioName">
                                        <c:out value="${ioName}"/><br>
                                    </c:forEach></p>
                                </c:if>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Reported By</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.reportedBy}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Report Noted By</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.reportedBy}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section C (Inspection Findings)</h4></strong>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <div class="text">
                        <p><span>Part I: Inspection Checklist</span></p>
                    </div>
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Checklist Used</p>
                            </td>
                            <td colspan="2" class="col-xs-8">
                                <p><c:out value="${insRepDto.serviceName}"/></p>
                            </td>
                        </tr>
                    </table>
                    <div class="text">
                        <p><span>Part II: Findings</span></p>
                    </div>
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Remarks</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.taskRemarks}"/></p>
                            </td>
                            <td class="col-xs-4">
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Marked for Audit</p>
                            </td>
                            <td class="col-xs-4">
                            <p><c:out value="${insRepDto.markedForAudit}"/> <fmt:formatDate value="${insRepDto.tcuDate}" pattern="dd/MM/yyyy"/></p>
                            </td>
                            <td class="col-xs-4">
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Recommended Best Practices</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.bestPractice}"/></p>
                            </td>
                            <td class="col-xs-4">
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Non-Compliances</p>
                            </td>
                            <td colspan="2" class="col-xs-8">
                                <c:if test="${insRepDto.ncRegulation != null && not empty insRepDto.ncRegulation}">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>SN</th>
                                            <th>Checklist Item</th>
                                            <th>Regulation Clause</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${insRepDto.ncRegulation}" var="ncRegulations"
                                                   varStatus="status">
                                            <tr>
                                                <td>
                                                    <p><c:out value="${status.count}"></c:out></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${ncRegulations.nc}"></c:out></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${ncRegulations.regulation}"></c:out></p>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </c:if>
                                <c:if test="${insRepDto.ncRegulation == null}">
                                    <p>0</p>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Status</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.status}"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Risk Level <strong style="color:#ff0000;"> *</strong></p>
                            </td>
                            <td class="col-xs-4">
                                <iais:select name="riskLevel" options="riskLevelOptions"  firstOption="Please Select" value="${appPremisesRecommendationDto.riskLevel}"/>
                                <span id="error_riskLevel" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section D (Rectification)</h4>
            </strong>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Rectified</p>
                            </td>
                            <td colspan="2" class="col-xs-8">
                                <c:if test="${insRepDto.ncRectification != null}">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>SN</th>
                                            <th>Checklist Item</th>
                                            <th>Rectified?</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${insRepDto.ncRectification}" var="ncRectification"
                                                   varStatus="status">
                                            <tr>
                                                <td>
                                                    <p><c:out value="${status.count}"></c:out></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${ncRectification.nc}"></c:out></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${ncRectification.rectified}"></c:out></p>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </c:if>
                                <c:if test="${insRepDto.ncRectification == null}">
                                    NA
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Remarks</p>
                            </td>
                            <div>
                                <td class="col-xs-4">
                                    <p><c:out value="${inspectorRemarks}"/></p>
                                </td>
                            </div>
                            <td class="col-xs-4">
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-4">
                                <p>Rectified Within KPI?</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.rectifiedWithinKPI}"></c:out></p>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section E (Recommendations)</h4>
            </strong>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Recommendation</p>
                            </td>
                            <td class="col-xs-4">
                                <p><iais:code code="${appPremisesRecommendationDto.period}"/></p>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section F (After Action)</h4>
            </strong>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Follow up Action</p>
                            </td>
                            <td class="col-xs-4">
                                <textarea style="resize: none" name="followUpAction" cols="50" rows="6" title="content" maxlength="8000"><c:out value="${appPremisesRecommendationDto.followUpAction}"/></textarea>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>To Engage Enforcement?</p>
                            </td>
                            <td class="col-xs-4">
                                <input type="checkbox" id="enforcement" name="engageEnforcement" onchange="javascirpt:changeEngage();"
                                       <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if>>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr id="engageRemarks" hidden>
                            <td class="col-xs-4">
                                <p>Enforcement Remarks <strong style="color:#ff0000;"> *</strong></p>
                            </td>
                            <td class="col-xs-4">
                                <textarea style="resize: none" name="enforcementRemarks" cols="50" rows="6" title="content" MAXLENGTH="4000"><c:out value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/></textarea>
                                <span id="error_enforcementRemarks" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <%@include file="/include/validation.jsp" %>

<script type="text/javascript">

    function reportaosubmit() {
        if ($("#processingDecision").val() =="Approval"){
            $("#mainForm").submit();
        }else if ($("#processingDecision").val() =="Reject"){
            $("[name='action_type']").val("back");
             $("#mainForm").submit();
        }else {
            $("#mainForm").submit();
        }

    }

    function submit(action) {
        $("[name='action_type']").val(action);
        var mainPoolForm = document.getElementById('aomainForm');
        mainPoolForm.submit();

    }

    function changePeriod(obj) {
        if (obj == "Others") {
            document.getElementById("selfPeriod").style.display = "";
            $("#selfPeriod").show();
        } else {
            document.getElementById("selfPeriod").style.display = "none";
        }
    }

    function changeRecommendation(obj) {
        if (obj == "Others") {
            document.getElementById("period").style.display = "";
            $("#period").show();
        } else {
            document.getElementById("period").style.display = "none";
        }
    }

    function changeTcu() {
        if ($('#tcuNeeded').is(':checked')) {
            document.getElementById("tcuDate").style.display = "";
            $("#tcuDate").show();
        } else {
            document.getElementById("tcuDate").style.display = "none";
        }
    }

    function changeEngage() {
        if ($('#enforcement').is(':checked')) {
            document.getElementById("engageRemarks").style.display = "";
            $("#engageRemarks").show();
        } else {
            document.getElementById("engageRemarks").style.display = "none";
        }
    }


    $(document).ready(function () {
        if ($("#recommendation").val() == "Approval") {
            changeRecommendation("Approval");
        }
        if ($("#periods").val() == "Others") {
            changePeriod("Others");
        }
        if ($('#tcuNeeded').is(':checked')) {
            $("#tcuDate").show();
        }
        if ($('#enforcement').is(':checked')) {
            $("#engageRemarks").show();
        }
    });

</script>

