<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bsb" uri="http://www.ecq.com/iais" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection-comment-report.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <div id="fileUploadInputDiv" style="display: none"></div>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="commentInsReportPanel" role="tabpanel">
                                    <div class="form-horizontal">
                                        <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Review of Inspection Report</p>
                                        <br/>
                                        <%--@elvariable id="reportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto"--%>
                                        <div class="tab-pane" id="tabInspection" role="tabpanel">
                                            <div class="alert alert-info" role="alert">
                                                <strong>
                                                    <h4 style="border-bottom: none">Section A (Facility Details)</h4>
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
                                                            <tr>
                                                                <td class="col-2">Name of Facility</td>
                                                                <td><c:out value="${reportDto.facName}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Address</td>
                                                                <td><c:out value="${reportDto.facilityAddress}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Facility Classification</td>
                                                                <td><iais:code code="${reportDto.classification}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Facility Activity Type</td>
                                                                <td><c:out value="${reportDto.activityType}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Person-in-Charge</td>
                                                                <td><c:out value="${reportDto.adminName}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Contact information of person-in-charge</td>
                                                                <td><c:out value="${reportDto.adminMobileNo} / ${reportDto.adminEmail}"/></td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="alert alert-info" role="alert">
                                                <strong>
                                                    <h4 style="border-bottom: none">Section B (Inspection Details)</h4>
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
                                                            <tr>
                                                                <td class="col-2">Date of Inspection</td>
                                                                <td><c:out value="${reportDto.inspectionDt}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Purpose of Inspection</td>
                                                                <td><c:out value="${reportDto.inspectionPurpose}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Facility Representative</td>
                                                                <td><c:out value="${reportDto.facilityRepresentative}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>MOH Inspector</td>
                                                                <td><c:out value="${reportDto.mohInspector}"/></td>
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
                                                    <div class="text">
                                                        <h4><strong><span>Part I: Inspection Checklist</span></strong></h4>
                                                    </div>
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tr>
                                                                <td class="col-2">
                                                                    <p>Checklist Used</p>
                                                                </td>
                                                                <td>
                                                                    <p><c:out value="${reportDto.checkListUsed}"/></p>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="text">
                                                        <h4><strong><span>Part II: Findings/Observations</span></strong></h4>
                                                    </div>
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tr>
                                                                <td class="col-2">Observations</td>
                                                                <td><c:out value="${reportDto.observation}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Remarks</td>
                                                                <td><c:out value="${reportDto.observationRemarks}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Inspection Findings</td>
                                                                <td>
                                                                    <p>BATA Inspection</p>
                                                                    <c:if test="${reportDto.isUnExcludedCheckListItemBsbListExist}">
                                                                        <table aria-describedby="" class="table">
                                                                            <thead>
                                                                            <tr style="font-size: 1.3rem">
                                                                                <th scope="col" style="width: 12%;font-size: 1.3rem">Checklist Item</th>
                                                                                <th scope="col" style="width: 14%;font-size: 1.3rem">Item Description</th>
                                                                                <th scope="col" style="width: 15%;font-size: 1.3rem">Findings/Non-Compliance</th>
                                                                                <th scope="col" style="width: 15%;font-size: 1.3rem">Action Required</th>
                                                                                <th scope="col" style="width: 15%;font-size: 1.3rem">Rectified?</th>
                                                                                <th scope="col" style="width: 13%;font-size: 1.3rem">Applicant's Remarks</th>
                                                                            </tr>
                                                                            </thead>
                                                                            <c:forEach var="checkList" items="${reportDto.checkListItemBsbList}" varStatus="status">
                                                                                <c:if test="${checkList.excludeFromApplicantVersion ne 'Y'}">
                                                                                <tr>
                                                                                    <td style="font-size: 1.3rem"><c:out value="${checkList.checklistItem}"/></td>
                                                                                    <td style="font-size: 1.3rem"><c:out value="${checkList.itemDescription}"/></td>
                                                                                    <td style="font-size: 1.3rem"><c:out value="${checkList.finding}"/></td>
                                                                                    <td style="font-size: 1.3rem"><c:out value="${checkList.actionRequired}"/></td>
                                                                                    <td style="font-size: 1.3rem"><c:choose><c:when test="${checkList.rectified eq 'true'}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
                                                                                    <td style="font-size: 1.3rem"><textarea id="applicantInput--v--${checkList.id}" name="applicantInput--v--${checkList.id}" style="width: 100%" rows="5" maxlength="1500" data-type="reportInput"><c:out value="${checkList.applicantInput}"/></textarea></td>
                                                                                </tr>
                                                                                </c:if>
                                                                            </c:forEach>
                                                                        </table>
                                                                    </c:if>
                                                                    <c:if test="${!reportDto.isUnExcludedCheckListItemBsbListExist}">
                                                                        <p>0</p>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div>


                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="text">
                                                        <p><h4><strong><span>Part III: Follow-Up Item</span></strong></h4></p>
                                                    </div>
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tr>
                                                                <td class="col-2">
                                                                    <p>Follow-up Items</p>
                                                                </td>
                                                                <td>
                                                                    <p>BATA Inspection</p>
                                                                    <c:if test="${reportDto.isUnExcludedFollowUpItemListExist}">
                                                                        <table aria-describedby="" class="table">
                                                                            <thead>
                                                                            <tr >
                                                                                <th scope="col" style="width: 12%;font-size: 1.3rem">Checklist Item</th>
                                                                                <th scope="col" style="width: 14%;font-size: 1.3rem">Item Description</th>
                                                                                <th scope="col" style="width: 15%;font-size: 1.3rem">Observations for Follow-up</th>
                                                                                <th scope="col" style="width: 15%;font-size: 1.3rem">Action Required</th>
                                                                                <th scope="col" style="width: 15%;font-size: 1.3rem">Due Date</th>
                                                                                <th scope="col" style="width: 13%;font-size: 1.3rem">Applicant's Remarks</th>
                                                                            </tr>
                                                                            </thead>
                                                                            <c:forEach var="followUp" items="${reportDto.followUpItemGeneralList}" varStatus="status">
                                                                                <c:if test="${followUp.excludeFromApplicantVersion ne 'Y'}">
                                                                                <tr>
                                                                                    <td style="font-size: 1.3rem"><c:out value="${followUp.checklistItem}"/></td>
                                                                                    <td style="font-size: 1.3rem"><c:out value="${followUp.itemDescription}"/></td>
                                                                                    <td style="font-size: 1.3rem"><c:out value="${followUp.observation}"/></td>
                                                                                    <td style="font-size: 1.3rem"><c:out value="${followUp.actionRequired}"/></td>
                                                                                    <td style="font-size: 1.3rem"><c:out value="${followUp.dueDt}"/></td>
                                                                                    <td style="font-size: 1.3rem"><textarea id="applicantInput--v--${followUp.id}" name="applicantInput--v--${followUp.id}" style="width: 100%" rows="5" maxlength="1500" data-type="reportInput" ><c:out value="${followUp.applicantInput}"/></textarea></td>
                                                                                </tr>
                                                                                </c:if>
                                                                            </c:forEach>
                                                                        </table>
                                                                    </c:if>
                                                                    <c:if test="${!reportDto.isUnExcludedFollowUpItemListExist}">
                                                                        <p>0</p>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="alert alert-info" role="alert">
                                                <strong>
                                                    <h4 style="border-bottom: none">Section D (Recommendation)</h4>
                                                </strong>
                                            </div>
                                            <div class="row">
                                                <div class="col-12">
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tr>
                                                                <td class="col-2">Deficiency <span style="color: red">*</span></td>
                                                                <td>
                                                                    <div>
                                                                        <label>
                                                                            <input type="checkbox" name="deficiency" <c:if test="${reportDto.deficiency.contains(MasterCodeConstants.VALUE_DEFICIENCY_MAJOR)}">checked="checked"</c:if> value="${MasterCodeConstants.VALUE_DEFICIENCY_MAJOR}" data-type="reportInput" disabled="disabled"/>
                                                                        </label>
                                                                        <span class="check-square">Major</span>
                                                                        <label>
                                                                            <input type="checkbox" name="deficiency" <c:if test="${reportDto.deficiency.contains(MasterCodeConstants.VALUE_DEFICIENCY_MINOR)}">checked="checked"</c:if> value="${MasterCodeConstants.VALUE_DEFICIENCY_MINOR}" data-type="reportInput" disabled="disabled"/>
                                                                        </label>
                                                                        <span class="check-square">Minor</span>
                                                                        <label>
                                                                            <input type="checkbox" name="deficiency" <c:if test="${reportDto.deficiency.contains(MasterCodeConstants.VALUE_DEFICIENCY_NIL)}">checked="checked"</c:if> value="${MasterCodeConstants.VALUE_DEFICIENCY_NIL}" data-type="reportInput" disabled="disabled"/>
                                                                        </label>
                                                                        <span class="check-square">NIL</span>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>Outcome</td>
                                                                <td><bsb:code code="${reportDto.outcome}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Remarks</td>
                                                                <td><c:out value="${reportDto.recommendationRemarks}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td><label for="applicantRemarks"></label>Applicant Remarks</td>
                                                                <td>
                                                                    <p>
                                                                        <textarea id="applicantRemarks" style="width: 100%" cols="200" rows="5" maxlength="1500"><c:out value="${reportDto.applicantRemarks}"/></textarea>
                                                                    </p>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6 ">
                                            <a class="back" id="previous" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${appId}'/>"><em class="fa fa-angle-left"></em> Previous</a>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="button-group">
                                                <a class="btn btn-primary" id="submitBtn" >Submit</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>