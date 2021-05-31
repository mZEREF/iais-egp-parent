<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" >
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <iais:body >
                            <div class="col-xs-12">
                                <div class="tab-gp dashboard-tab">
                                    <br><br><br>
                                    <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                        <li class="complete" role="presentation"><a href="#tabLicenseeDetails"
                                                                                    aria-controls="tabLicenseeDetails"
                                                                                    role="tab"
                                                                                    data-toggle="tab">Licensee
                                            Details</a>
                                        </li>
                                        <li class="complete" role="presentation"><a href="#tabPersonnelDetails"
                                                                                    aria-controls="tabPersonnelDetails"
                                                                                    role="tab"
                                                                                    data-toggle="tab">Personnel
                                            Details</a>
                                        </li>
                                        <li class="active" role="presentation"><a href="#tabInspectionReport"
                                                                                  aria-controls="tabInspectionReport"
                                                                                  role="tab"
                                                                                  data-toggle="tab">Inspection
                                            Report</a>
                                        </li>
                                    </ul>
                                    <div class="tab-nav-mobile visible-xs visible-sm">
                                        <div class="swiper-wrapper" role="tablist">
                                            <div class="swiper-slide"><a href="#tabLicenseeDetails"
                                                                         aria-controls="tabLicenseeDetails" role="tab"
                                                                         data-toggle="tab">Licensee Details</a></div>
                                            <div class="swiper-slide"><a href="#tabPersonnelDetails"
                                                                         aria-controls="tabPersonnelDetails" role="tab"
                                                                         data-toggle="tab">Personnel Details</a></div>
                                            <div class="swiper-slide"><a href="#tabComplianceHistory"
                                                                         aria-controls="tabComplianceHistory" role="tab"
                                                                         data-toggle="tab">Compliance History</a></div>
                                        </div>
                                        <div class="swiper-button-prev"></div>
                                        <div class="swiper-button-next"></div>
                                    </div>

                                    <div class="tab-content ">
                                        <div class="tab-pane" id="tabLicenseeDetails" role="tabpanel">
                                            <%@include file="licenseeDetails.jsp" %>
                                        </div>

                                        <div class="tab-pane" id="tabPersonnelDetails" role="tabpanel">
                                            <%@include file="personnelDetails.jsp" %>
                                        </div>
                                        <div class="tab-pane active" id="tabInspectionReport" role="tabpanel">
                                            <div class="panel panel-default">
                                                <!-- Default panel contents -->
                                                <div class="alert alert-info" role="alert">
                                                    <strong>
                                                        <h4>Past Inspection Reports</h4>
                                                    </strong>
                                                </div>
                                                <input type="hidden" name="confirmAction" value="">

                                                    <%--        <div class="row">--%>
                                                <div class="panel-heading" role="alert">
                                                    <strong>
                                                        Section A (HCI Details)
                                                    </strong>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table class="table">
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Licence No.</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;<c:out
                                                                                value="${insRepDto.licenceNo}"/></p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Service Name</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;<c:out
                                                                                value="${insRepDto.serviceName}"/></p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;HCI Code</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;<c:out
                                                                                value="${insRepDto.hciCode}"/><c:if
                                                                                test="${empty insRepDto.hciCode}">-</c:if></p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;HCI Name</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;<c:out
                                                                                value="${insRepDto.hciName}"/><c:if
                                                                                test="${empty insRepDto.hciName}">-</c:if></p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;HCI Address</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;<c:out
                                                                                value="${insRepDto.hciAddress}"/></p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Licensee Name</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;<c:out
                                                                                value="${insRepDto.licenseeName}"/></p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Clinical Governance Officer(s)</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <c:if test="${insRepDto.clinicalGovernanceOfficer != null && not empty insRepDto.clinicalGovernanceOfficer}">
                                                                            <p><c:forEach
                                                                                    items="${insRepDto.clinicalGovernanceOfficer}"
                                                                                    var="cgoName">
                                                                                &nbsp;<c:out value="${cgoName}"/><br>
                                                                            </c:forEach></p>
                                                                        </c:if>
                                                                        <c:if test="${ empty insRepDto.clinicalGovernanceOfficer}">&nbsp;-</c:if>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Principal Officer(s)</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <c:if test="${ not empty insRepDto.principalOfficers}">
                                                                            <p>
                                                                                <c:forEach
                                                                                        items="${insRepDto.principalOfficers}"
                                                                                        var="poName">
                                                                                    &nbsp;<c:out value="${poName}"/><br>
                                                                                </c:forEach>
                                                                            </p>
                                                                        </c:if>
                                                                        <c:if test="${ empty insRepDto.principalOfficers}">&nbsp;-</c:if>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Subsumed Services</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <c:if test="${insRepDto.subsumedServices != null && not empty insRepDto.subsumedServices}">
                                                                            <c:forEach var="service"
                                                                                       items="${insRepDto.subsumedServices}">
                                                                                <p>&nbsp;<c:out
                                                                                        value="${service}"></c:out></p>
                                                                            </c:forEach>
                                                                        </c:if>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel-heading" role="alert">
                                                    <strong>
                                                        Section B (Type of Inspection)
                                                    </strong>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table class="table">
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Date of Inspection</p>
                                                                    </td>
                                                                    <td class="col-xs-8">
                                                                        &nbsp;<fmt:formatDate
                                                                            value="${insRepDto.inspectionDate}"
                                                                            pattern="dd/MM/yyyy"></fmt:formatDate><c:if
                                                                            test="${empty insRepDto.inspectionDate}">-</c:if>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Time of Inspection</p>
                                                                    </td>
                                                                    <td class="col-xs-8">
                                                                        &nbsp;<c:out
                                                                            value="${insRepDto.inspectionStartTime}"></c:out>-<c:out
                                                                            value="${insRepDto.inspectionEndTime}"></c:out>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Reason for Visit</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;${insRepDto.reasonForVisit}</p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Inspected By</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <c:if test="${not empty insRepDto.inspectors}">
                                                                            <p><c:forEach
                                                                                    items="${insRepDto.inspectors}"
                                                                                    var="inspector"
                                                                                    varStatus="status">
                                                                                &nbsp;<c:out value="${inspector}"/>
                                                                            </c:forEach></p>
                                                                        </c:if>
                                                                        <c:if test="${empty insRepDto.inspectors}">&nbsp;-</c:if>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Other Inspection Officer(s)</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <c:if test="${not empty insRepDto.inspectOffices}">
                                                                            <p><c:forEach
                                                                                    items="${insRepDto.inspectOffices}"
                                                                                    var="ioName">
                                                                                &nbsp;<c:out value="${ioName}"/><br>
                                                                            </c:forEach></p>
                                                                        </c:if>
                                                                        <c:if test="${ empty insRepDto.inspectOffices}">&nbsp;-</c:if>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Reported By</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;${insRepDto.reportedBy}</p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Report Noted By</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;${insRepDto.reportNoteBy}</p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel-heading" role="alert">
                                                    <strong>
                                                        Section C (Inspection Findings)
                                                    </strong>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <div class="text">
                                                                <p>
                                                                    <strong><span>&nbsp;Part I: Inspection Checklist</span></strong>
                                                                </p>
                                                            </div>
                                                            <table class="table">
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Checklist Used</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;${insRepDto.serviceName}</p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                            </table>
                                                            <div class="text">
                                                                <p><strong><span>&nbsp;Part II: Findings</span></strong>
                                                                </p>
                                                            </div>
                                                            <div class="table-gp">
                                                                <table class="table">
                                                                    <tr>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;Remarks</p>
                                                                        </td>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;${insRepDto.taskRemarks}</p>
                                                                        </td>
                                                                        <td class="col-xs-4"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;Marked for Audit</p>
                                                                        </td>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;<c:out
                                                                                    value="${insRepDto.markedForAudit}"/>&nbsp;&nbsp;<fmt:formatDate
                                                                                    value="${insRepDto.tcuDate}"
                                                                                    pattern="dd/MM/yyyy"/></p>
                                                                        </td>
                                                                        <td class="col-xs-4"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;Recommended Best Practices</p>
                                                                        </td>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;${insRepDto.bestPractice}</p>
                                                                        </td>
                                                                        <td class="col-xs-4"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;Non-Compliances</p>
                                                                        </td>
                                                                        <td colspan="2" class="col-xs-8">
                                                                            <c:if test="${insRepDto.ncRegulation != null && not empty insRepDto.ncRegulation}">
                                                                                <table class="table">
                                                                                    <thead>
                                                                                    <tr>
                                                                                        <th>SN</th>
                                                                                        <th>Checklist Item</th>
                                                                                        <th>Regulation Clause</th>
                                                                                        <th>Findings/NCs</th>
                                                                                    </tr>
                                                                                    </thead>
                                                                                    <tbody>
                                                                                    <c:forEach
                                                                                            items="${insRepDto.ncRegulation}"
                                                                                            var="ncRegulations"
                                                                                            varStatus="status">
                                                                                        <tr>
                                                                                            <td>
                                                                                                <p>&nbsp;<c:out
                                                                                                        value="${status.count}"/></p>
                                                                                            </td>
                                                                                            <td>
                                                                                                <p>&nbsp;<c:out
                                                                                                        value="${ncRegulations.nc}"/></p>
                                                                                            </td>
                                                                                            <td>
                                                                                                <p>&nbsp;<c:out
                                                                                                        value="${ncRegulations.regulation}"/></p>
                                                                                            </td>
                                                                                            <td>
                                                                                                <p><c:out value="${ncRegulations.ncs}"></c:out></p>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </c:forEach>
                                                                                    </tbody>
                                                                                </table>
                                                                            </c:if>
                                                                            <c:if test="${insRepDto.ncRegulation == null}">
                                                                                <p>&nbsp;0</p>
                                                                            </c:if>
                                                                        </td>
                                                                        <td class="col-xs-4"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;Status</p>
                                                                        </td>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;${insRepDto.status}</p>
                                                                        </td>
                                                                        <td class="col-xs-4"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;Risk Level </p>
                                                                        </td>
                                                                        <td class="col-xs-4">
                                                                            &nbsp;${insRepDto.riskLevel}
                                                                            <c:if test="${empty insRepDto.riskLevel}">&nbsp;-</c:if>
                                                                        </td>
                                                                        <td class="col-xs-4"></td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel-heading" role="alert">
                                                    <strong>
                                                        Section D (Rectification)
                                                    </strong>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table class="table">
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Rectified</p>
                                                                    </td>
                                                                    <td colspan="2" class="col-xs-8">
                                                                        <c:if test="${insRepDto.ncRectification != null}">
                                                                            <table class="table">
                                                                                <thead>
                                                                                <tr>
                                                                                    <th>SN</th>
                                                                                    <th>Checklist Item</th>
                                                                                    <th>Findings/NCs</th>
                                                                                    <th>Rectified?</th>
                                                                                </tr>
                                                                                </thead>
                                                                                <tbody>
                                                                                <c:forEach
                                                                                        items="${insRepDto.ncRectification}"
                                                                                        var="ncRectification"
                                                                                        varStatus="status">
                                                                                    <tr>
                                                                                        <td>
                                                                                            <p>&nbsp;<c:out
                                                                                                    value="${status.count}"></c:out></p>
                                                                                        </td>
                                                                                        <td>
                                                                                            <p>&nbsp;<c:out
                                                                                                    value="${ncRectification.nc}"></c:out></p>
                                                                                        </td>
                                                                                        <td>
                                                                                            <p><c:out value="${ncRectification.ncs}"></c:out></p>
                                                                                        </td>
                                                                                        <td>
                                                                                            <p>&nbsp;<c:out
                                                                                                    value="${ncRectification.rectified}"></c:out></p>
                                                                                        </td>
                                                                                    </tr>
                                                                                </c:forEach>
                                                                                </tbody>
                                                                            </table>
                                                                        </c:if>
                                                                        <c:if test="${insRepDto.ncRectification == null}">
                                                                            <p>&nbsp;NA</p>
                                                                        </c:if>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Remarks</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;<c:out value="${insRepDto.inspectypeRemarks}"></c:out></p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>

                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Rectified Within KPI? <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip"
                                                                                                                                   data-html="true"
                                                                                                                                   data-original-title="${kpiInfo}">i</a></p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;<c:out
                                                                                value="${insRepDto.rectifiedWithinKPI}"></c:out></p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel-heading" role="alert">
                                                    <strong>
                                                        Section E (Recommendations)
                                                    </strong>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table class="table">
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Recommendation </p>
                                                                    </td>
                                                                    <td class="col-xs-4">

                                                                        &nbsp;${insRepDto.recommendation}
                                                                        <c:if test="${empty insRepDto.recommendation}">-</c:if>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>

                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="sectionF" class="panel-heading" role="alert">
                                                    <strong>
                                                        Section F (After Action)
                                                    </strong>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table class="table">
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;Follow up Action</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>
                                                                            <c:out value="${appPremisesRecommendationDto.followUpAction}"/>
                                                                            <c:if test="${empty appPremisesRecommendationDto.followUpAction}">-</c:if>
                                                                        </p>
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>&nbsp;To Engage Enforcement?</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <input type="checkbox" id="enforcement" disabled
                                                                               name="engageEnforcement"
                                                                               <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if> >
                                                                    </td>
                                                                    <td class="col-xs-4"></td>
                                                                </tr>
                                                                <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">
                                                                    <tr id="engageRemarks">
                                                                        <td class="col-xs-4">
                                                                            <p>&nbsp;Enforcement Remarks </p>
                                                                        </td>
                                                                        <td class="col-xs-4">
                                                                            <c:out value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/>
                                                                            <c:if test="${empty appPremisesRecommendationDto.engageEnforcementRemarks}">-</c:if>
                                                                        </td>
                                                                        <td class="col-xs-4"></td>
                                                                    </tr>
                                                                </c:if>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <a onclick="javascript:SOP.Crud.cfxSubmit('mainForm');"><em class="fa fa-angle-left"> </em> Back</a>
                            </div>
                        </iais:body>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
