<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
<%@ include file="/include/formHidden.jsp" %>
<input type="hidden" name="confirmAction" value="">
<div class="tab-pane" id="tabInspection" role="tabpanel">
    <%--        <div class="row">--%>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4 style="border-bottom: none">Section A (HCI Details)</h4>
            </strong>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table">
                    <tr>
                        <td class="col-xs-4">
                            <p>Licence No.</p>
                        </td>
                        <td class="col-xs-4">
                            <p><c:out value="${insRepDto.licenceNo}"/></p>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Service Name</p>
                        </td>
                        <td class="col-xs-4">
                            <p><c:out value="${insRepDto.serviceName}"/></p>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>HCI Code</p>
                        </td>
                        <td class="col-xs-4">
                            <p><c:out value="${insRepDto.hciCode}"/></p>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>HCI Name</p>
                        </td>
                        <td class="col-xs-4">
                            <p><c:out value="${insRepDto.hciName}"/></p>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>HCI Address</p>
                        </td>
                        <td class="col-xs-4">
                            <p><c:out value="${insRepDto.hciAddress}"/></p>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Licensee Name</p>
                        </td>
                        <td class="col-xs-4">
                            <p><c:out value="${insRepDto.licenseeName}"/></p>
                        </td>
                        <td class="col-xs-4"/>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Principal Officers</p>
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
                <h4 style="border-bottom: none">Section B (Type of Inspection)</h4>
            </strong>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table">
                    <tr>
                        <td class="col-xs-4">
                            <p>Date of Inspection</p>
                        </td>
                        <td class="col-xs-8">
                            <fmt:formatDate value="${insRepDto.inspectionDate}"
                                            pattern="dd/MM/yyyy"></fmt:formatDate>
                        </td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Time of Inspection</p>
                        </td>
                        <td class="col-xs-8">
                            <c:out value="${insRepDto.inspectionStartTime}"></c:out>-<c:out value="${insRepDto.inspectionEndTime}"></c:out>
                        </td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Reason for Visit</p>
                        </td>
                        <td class="col-xs-4">
                            <p>${insRepDto.reasonForVisit}</p>
                        </td>
                        <td class="col-xs-4"></td>
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
                        <td class="col-xs-4"></td>
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
                        <td class="col-xs-4"></td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Reported By</p>
                        </td>
                        <td class="col-xs-4">
                            <p>${insRepDto.reportedBy}</p>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Report Noted By</p>
                        </td>
                        <td class="col-xs-4">
                            <p>${insRepDto.reportNoteBy}</p>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4 style="border-bottom: none">Section C (Inspection Findings)</h4>
            </strong>
        </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <div class="text ">
                    <p><span>Part I: Inspection Checklist</span></p>
                </div>
                <table class="table">
                    <tr>
                        <td class="col-xs-4">
                            <p>Checklist Used</p>
                        </td>
                        <td class="col-xs-4">
                            <p>${insRepDto.serviceName}</p>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                </table>
                <div class="text ">
                    <p><span>Part II: Findings</span></p>
                </div>
                <div class="table-gp">
                    <table class="table" >
                        <tr>
                            <td class="col-xs-4">
                                <p>Remarks</p>
                            </td>
                            <td class="col-xs-4">
                                <p>${insRepDto.taskRemarks}</p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Marked for Audit</p>
                            </td>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.markedForAudit}"/> <fmt:formatDate value="${insRepDto.tcuDate}" pattern="dd/MM/yyyy"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Recommended Best Practices</p>
                            </td>
                            <td class="col-xs-4">
                                <p>${insRepDto.bestPractice}</p>
                            </td>
                            <td class="col-xs-4"/>
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
                                <p>${insRepDto.status}</p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Risk Level <strong style="color:#ff0000;"> *</strong></p>
                            </td>
                            <td class="col-xs-4">
                                <iais:select name="riskLevel" options="riskLevelOptions" firstOption="Please select" value="${appPremisesRecommendationDto.riskLevel}"/>
                                <span id="error_riskLevel" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4">
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4 style="border-bottom: none">Section D (Rectification)</h4>
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
                                <p>NA</p>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Remarks <strong style="color:#ff0000;"> *</strong></p>
                        </td>
                        <div>
                            <td class="col-xs-4">
                                <p><textarea style="resize:none" name="remarks" cols="50" rows="6" title="content" maxlength="8000"><c:if test="${appPremisesRecommendationDto.remarks ==null}">${reportRemarks}</c:if><c:if test="${appPremisesRecommendationDto.remarks !=null}"><c:out value="${appPremisesRecommendationDto.remarks}"/></c:if></textarea></p>
                                <span id="error_remarks" name="iaisErrorMsg" class="error-msg"></span>
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
                <h4 style="border-bottom: none">Section E (Recommendations)</h4>
            </strong>
        </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table">
                    <tr>
                        <td class="col-xs-4">
                            <p>Recommendation <strong style="color:#ff0000;"> *</strong></p>
                        </td>
                            <td class="col-xs-4">
                                <iais:select name="recommendation" options="recommendationOption" firstOption="Please select" value="${appPremisesRecommendationDto.recommendation}" onchange="javascirpt:changeRecommendation(this.value);"/>
                            </td>
                        <td class="col-xs-4"></td>
                    </tr>
                    <tr id="period" hidden>
                        <td class="col-xs-4">
                            <p>Period <strong style="color:#ff0000;"> *</strong></p>
                        </td>
                        <td class="col-xs-4">
                            <iais:select name="periods" options="riskOption" firstOption="Please select" onchange="javascirpt:changePeriod(this.value);" value="${appPremisesRecommendationDto.period}"/>
                            <span id="error_period" name="iaisErrorMsg" class="error-msg"></span>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                    <tr id="selfPeriod" hidden>
                        <td class="col-xs-4">
                            <p>Other Period <strong style="color:#ff0000;"> *</strong></p>
                        </td>
                        <td class="col-xs-4">
                            <input id=recomInNumber type="text" name="number" maxlength="2" value="${appPremisesRecommendationDto.recomInNumber}">
                            <iais:select id="chronoUnit" name="chrono" options="chronoOption" firstOption="Please select" value="${appPremisesRecommendationDto.chronoUnit}"/>
                            <span id="error_recomInNumber" name="iaisErrorMsg" class="error-msg"></span>
                            <span id="error_chronoUnit" name="iaisErrorMsg" class="error-msg"></span>
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
                                <P><textarea style="resize:none" name="followUpAction" cols="50" rows="6" title="content" maxlength="8000"><c:out value="${appPremisesRecommendationDto.followUpAction}"/></textarea></P>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>To Engage Enforcement?</p>
                            </td>
                            <td class="col-xs-4">
                                <input type="checkbox" id="enforcement" name="engageEnforcement" onchange="javascirpt:changeEngage();" <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if> >
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr id="engageRemarks" hidden>
                            <td class="col-xs-4">
                                <p>Enforcement Remarks <strong style="color:#ff0000;"> *</strong></p>
                            </td>
                            <td class="col-xs-4">
                                <textarea style="resize:none" name="enforcementRemarks" cols="50" rows="6" title="content" MAXLENGTH="4000"><c:out value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/></textarea>
                                <span id="error_enforcementRemarks" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
</div>
<%@include file="/include/validation.jsp" %>

<script type="text/javascript">

    function insRepsubmit() {
        $("#mainForm").submit();
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
        if (obj == "Approved") {
            document.getElementById("period").style.display = "";
            $("#period").show();
        } else {
            document.getElementById("period").style.display = "none";
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
        if ($("#recommendation").val() == "Approved") {
            changeRecommendation("Approved");
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

