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
        <input type="hidden" name="preInspReport" value="1">
        <div class="main-content">
            <div class="center-content">
                <div class="intranet-content">
                    <iais:body >
                    <div class="col-md-12">
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
                                        <style>
                                            .table-gp table.table > tbody > tr > td {
                                                padding: 15px 25px 15px 15Px;
                                                border-bottom: 1px solid #E2E2E2;
                                                vertical-align: top;
                                            }
                                        </style>
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
                                            <div class="col-md-12">
                                                <div class="table-gp">
                                                    <table aria-describedby="" class="table">
                                                        <thead style="display: none">
                                                        <tr>
                                                            <th scope="col"></th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>


                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Licence No.</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <p><c:out
                                                                        value="${insRepDto.licenceNo}"/></p>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Service Name</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <p><c:out
                                                                        value="${insRepDto.serviceName}"/></p>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>HCI Code</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <p><c:out
                                                                        value="${insRepDto.hciCode}"/><c:if
                                                                        test="${empty insRepDto.hciCode}">-</c:if></p>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>HCI Name</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <p><c:out
                                                                        value="${insRepDto.hciName}"/><c:if
                                                                        test="${empty insRepDto.hciName}">-</c:if></p>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>HCI Address</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <p><c:out
                                                                        value="${insRepDto.hciAddress}"/></p>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Licensee Name</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <p><c:out
                                                                        value="${insRepDto.licenseeName}"/></p>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Clinical Governance Officer(s)</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <c:if test="${insRepDto.clinicalGovernanceOfficer != null && not empty insRepDto.clinicalGovernanceOfficer}">
                                                                    <p><c:forEach
                                                                            items="${insRepDto.clinicalGovernanceOfficer}"
                                                                            var="cgoName">
                                                                        <c:out value="${cgoName}"/><br>
                                                                    </c:forEach></p>
                                                                </c:if>
                                                                <c:if test="${ empty insRepDto.clinicalGovernanceOfficer}">-</c:if>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Principal Officer(s)</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <c:if test="${ not empty insRepDto.principalOfficers}">
                                                                    <p>
                                                                        <c:forEach
                                                                                items="${insRepDto.principalOfficers}"
                                                                                var="poName">
                                                                            <c:out value="${poName}"/><br>
                                                                        </c:forEach>
                                                                    </p>
                                                                </c:if>
                                                                <c:if test="${ empty insRepDto.principalOfficers}">-</c:if>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Subsumed Services</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <c:if test="${insRepDto.subsumedServices != null && not empty insRepDto.subsumedServices}">
                                                                    <c:forEach var="service"
                                                                               items="${insRepDto.subsumedServices}">
                                                                        <p><c:out
                                                                                value="${service}"></c:out></p>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        </tbody>
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
                                            <div class="col-md-12">
                                                <div class="table-gp">
                                                    <table aria-describedby="" class="table">
                                                        <thead style="display: none">
                                                        <tr>
                                                            <th scope="col"></th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Date of Inspection</p>
                                                            </td>
                                                            <td class="col-md-8">
                                                                <fmt:formatDate
                                                                        value="${insRepDto.inspectionDate}"
                                                                        pattern="dd/MM/yyyy"></fmt:formatDate><c:if
                                                                    test="${empty insRepDto.inspectionDate}">-</c:if>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Time of Inspection</p>
                                                            </td>
                                                            <td class="col-md-8">
                                                                <c:out
                                                                        value="${insRepDto.inspectionStartTime}"></c:out>-<c:out
                                                                    value="${insRepDto.inspectionEndTime}"></c:out>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Reason for Visit</p>
                                                            </td>
                                                            <td class="col-md-8">
                                                                <p>${insRepDto.reasonForVisit}</p>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Inspected By</p>
                                                            </td>
                                                            <td class="col-md-8">
                                                                <c:if test="${not empty insRepDto.inspectors}">
                                                                    <p><c:forEach
                                                                            items="${insRepDto.inspectors}"
                                                                            var="inspector"
                                                                            varStatus="status">
                                                                        <c:out value="${inspector}"/>
                                                                    </c:forEach></p>
                                                                </c:if>
                                                                <c:if test="${empty insRepDto.inspectors}">-</c:if>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Other Inspection Officer(s)</p>
                                                            </td>
                                                            <td class="col-md-8">
                                                                <c:if test="${not empty insRepDto.inspectOffices}">
                                                                    <p><c:forEach
                                                                            items="${insRepDto.inspectOffices}"
                                                                            var="ioName">
                                                                        <c:out value="${ioName}"/><br>
                                                                    </c:forEach></p>
                                                                </c:if>
                                                                <c:if test="${ empty insRepDto.inspectOffices}">-</c:if>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Reported By</p>
                                                            </td>
                                                            <td class="col-md-8">
                                                                <p>${insRepDto.reportedBy}</p>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Report Noted By</p>
                                                            </td>
                                                            <td class="col-md-8">
                                                                <p>${insRepDto.reportNoteBy}</p>
                                                            </td>
                                                        </tr>
                                                        </tbody>
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
                                            <div class="col-md-12">
                                                <div class="table-gp">
                                                    <div class="text">
                                                        <p>
                                                            <strong><span>&nbsp;Part I: Inspection Checklist</span></strong>
                                                        </p>
                                                    </div>
                                                    <table aria-describedby="" class="table">
                                                        <thead style="display: none">
                                                        <tr>
                                                            <th scope="col"></th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <tr>
                                                            <td class="col-md-4">
                                                                <p>Checklist Used</p>
                                                            </td>
                                                            <td class="col-md-4">
                                                                <p>${insRepDto.serviceName}</p>
                                                            </td>
                                                            <td class="col-md-4"></td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                    <div class="text">
                                                        <p><strong><span>&nbsp;Part II: Findings</span></strong>
                                                        </p>
                                                    </div>
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <tr>
                                                                <td class="col-md-4">
                                                                    <p>Remarks</p>
                                                                </td>
                                                                <td class="col-md-4">
                                                                    <p>${insRepDto.taskRemarks}</p>
                                                                </td>
                                                                <td class="col-md-4"></td>
                                                            </tr>
                                                            <tr>
                                                                <td class="col-md-4">
                                                                    <p>Marked for Audit</p>
                                                                </td>
                                                                <td class="col-md-4">
                                                                    <p><c:out value="${insRepDto.markedForAudit}"/>&nbsp;<fmt:formatDate value="${insRepDto.tcuDate}" pattern="dd/MM/yyyy"/></p>
                                                                </td>
                                                                <td class="col-md-4"></td>
                                                            </tr>
                                                            <tr>
                                                                <td class="col-md-4">
                                                                    <p>Recommendation</p>
                                                                </td>
                                                                <td class="col-md-4">
                                                                    <p>${insRepDto.bestPractice}</p>
                                                                </td>
                                                                <td class="col-md-4"></td>
                                                            </tr>
                                                            <tr>
                                                                <td class="col-md-4">
                                                                    <p>Observation</p>
                                                                </td>
                                                                <td class="col-md-4">
                                                                    <p>${insRepDto.observation}</p>
                                                                </td>
                                                                <td class="col-md-4"></td>
                                                            </tr>
                                                            <tr>
                                                                <td class="col-md-4">
                                                                    <p>Non-Compliances</p>
                                                                </td>
                                                                <td colspan="2" class="col-md-8">
                                                                    <c:if test="${insRepDto.ncRegulation != null && not empty insRepDto.ncRegulation}">
                                                                        <table aria-describedby="" class="table">
                                                                            <thead>
                                                                            <tr>
                                                                                <th scope="col" >SN</th>
                                                                                <c:if test="${specialServiceForChecklistDecide == '1'}"><th scope="col" >Vehicle Name</th></c:if>
                                                                                <th scope="col" >Checklist Item</th>
                                                                                <th scope="col" >Regulation Clause</th>
                                                                                <th scope="col" >Findings/NCs</th>
                                                                            </tr>
                                                                            </thead>
                                                                            <tbody>
                                                                            <c:forEach
                                                                                    items="${insRepDto.ncRegulation}"
                                                                                    var="ncRegulations"
                                                                                    varStatus="status">
                                                                                <tr>
                                                                                    <td style="padding: 15px 25px 15px 0;">
                                                                                        <p><c:out
                                                                                                value="${status.count}"/></p>
                                                                                    </td>
                                                                                    <c:if test="${specialServiceForChecklistDecide == '1'}">
                                                                                        <td style="padding: 15px 25px 15px 0;"><c:out value="${ncRegulations.vehicleName}"/></td>
                                                                                    </c:if>
                                                                                    <td style="padding: 15px 25px 15px 0;">
                                                                                        <p><c:out
                                                                                                value="${ncRegulations.nc}"/></p>
                                                                                    </td>
                                                                                    <td style="padding: 15px 25px 15px 0;">
                                                                                        <p><c:out
                                                                                                value="${ncRegulations.regulation}"/></p>
                                                                                    </td>
                                                                                    <td style="padding: 15px 25px 15px 0;">
                                                                                        <p><c:out value="${ncRegulations.ncs}"/></p>
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
                                                                <td class="col-md-4">
                                                                    <p>Status</p>
                                                                </td>
                                                                <td class="col-md-4">
                                                                    <p>${insRepDto.status}</p>
                                                                </td>
                                                                <td class="col-md-4"></td>
                                                            </tr>
                                                            <tr>
                                                                <td class="col-md-4">
                                                                    <p>Risk Level </p>
                                                                </td>
                                                                <td class="col-md-4">
                                                                        ${insRepDto.riskLevel}
                                                                    <c:if test="${empty insRepDto.riskLevel}">-</c:if>
                                                                </td>
                                                                <td class="col-md-4"></td>
                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                            <c:if test="${appType!='APTY007' && appType!='APTY009'}">
                                                <div class="panel-heading" role="alert">
                                                    <strong>
                                                        Section E (Recommendations)
                                                    </strong>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="table-gp">
                                                            <table aria-describedby="" class="table">
                                                                <thead style="display: none">
                                                                <tr>
                                                                    <th scope="col"></th>
                                                                </tr>
                                                                </thead>
                                                                <tbody>
                                                                <tr>
                                                                    <td class="col-md-4">
                                                                        <p>Recommendation </p>
                                                                    </td>
                                                                    <td class="col-md-4">

                                                                            ${insRepDto.recommendation}
                                                                        <c:if test="${empty insRepDto.recommendation}">-</c:if>
                                                                    </td>
                                                                    <td class="col-md-4"></td>
                                                                </tr>
                                                                </tbody>
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
                                                    <div class="col-md-12">
                                                        <div class="table-gp">
                                                            <table aria-describedby="" class="table">
                                                                <thead style="display: none">
                                                                <tr>
                                                                    <th scope="col"></th>
                                                                </tr>
                                                                </thead>
                                                                <tbody>

                                                                <tr>
                                                                    <td class="col-md-4">
                                                                        <p>Follow up Action</p>
                                                                    </td>
                                                                    <td class="col-md-4">
                                                                        <p>
                                                                            <c:out value="${appPremisesRecommendationDto.followUpAction}"/>
                                                                            <c:if test="${empty appPremisesRecommendationDto.followUpAction}">-</c:if>
                                                                        </p>
                                                                    </td>
                                                                    <td class="col-md-4"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-md-4">
                                                                        <p>To Engage Enforcement?</p>
                                                                    </td>
                                                                    <td class="col-md-4">
                                                                        <input type="checkbox" id="enforcement" disabled
                                                                               name="engageEnforcement"
                                                                               <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if> >
                                                                    </td>
                                                                    <td class="col-md-4"></td>
                                                                </tr>
                                                                <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">
                                                                    <tr id="engageRemarks">
                                                                        <td class="col-md-4">
                                                                            <p>Enforcement Remarks </p>
                                                                        </td>
                                                                        <td class="col-md-4">
                                                                            <c:out value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/>
                                                                            <c:if test="${empty appPremisesRecommendationDto.engageEnforcementRemarks}">-</c:if>
                                                                        </td>
                                                                        <td class="col-md-4"></td>
                                                                    </tr>
                                                                </c:if>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:if>

                                            <c:if test="${appType=='APTY007'|| appType=='APTY009'}">
                                            <div class="panel-heading" role="alert">
                                                <strong>
                                                    Section E (After Action)
                                                </strong>
                                            </div>
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>Follow up Action</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p><textarea style="resize: none" disabled name="followUpAction" cols="50" rows="6"
                                                                                     title="content" maxlength="8000"><c:out
                                                                                value="${appPremisesRecommendationDto.followUpAction}"/></textarea></p>
                                                                    </td>
                                                                    <td class="col-xs-4"/>
                                                                </tr>
                                                                <c:if test="${appPremisesRecommendationDto.engageEnforcementRemarks!=null}">
                                                                    <tr>
                                                                        <td class="col-xs-4">
                                                                            <p>To Engage Enforcement?</p>
                                                                        </td>
                                                                        <td class="col-xs-4">
                                                                            <input type="checkbox" disabled checked>
                                                                        </td>
                                                                        <td class="col-xs-4"></td>
                                                                    </tr>
                                                                    <tr id="engageRemarks">
                                                                        <td class="col-xs-4">
                                                                            <p>Enforcement Remarks</p>
                                                                        </td>
                                                                        <td class="col-xs-4">
                                                                            <textarea style="resize: none" disabled cols="50" rows="6" title="content"
                                                                                      MAXLENGTH="4000"><c:out
                                                                                    value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/></textarea>
                                                                        </td>
                                                                        <td class="col-xs-4"/>
                                                                    </tr>
                                                                </c:if>
                                                                <c:if test="${appPremisesRecommendationDto.engageEnforcementRemarks==null}">
                                                                    <tr>
                                                                        <td class="col-xs-4">
                                                                            <p>To Engage Enforcement?</p>
                                                                        </td>
                                                                        <td class="col-xs-4">
                                                                            <input type="checkbox" disabled name="engageEnforcement">
                                                                        </td>
                                                                        <td class="col-xs-4"></td>
                                                                    </tr>
                                                                </c:if>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <a href="#" onclick="javascript:SOP.Crud.cfxSubmit('mainForm');"><em class="fa fa-angle-left"> </em> Back</a>
                        </div>
                        </iais:body>
                    </div>
                </div>
            </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
