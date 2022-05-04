<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%@page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<style>
    .report-sub-title > h4 {
        font-size: 1.8rem;
        font-weight: 700;
        color: black;
        margin: 0;
    }
</style>

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
                                            <div class="alert alert-info report-sub-title" role="alert">
                                                <h4 style="border-bottom: none">Section A (Facility Details)</h4>
                                            </div>
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tr>
                                                                <td class="col-4">Name of Facility</td>
                                                                <td><c:out value="${reportDto.facName}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Address</td>
                                                                <td><c:out value="${TableDisplayUtil.getOneLineAddress(reportDto.blk, reportDto.street, reportDto.street, reportDto.unit, reportDto.postalCode)}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Facility Classification</td>
                                                                <td><iais:code code="${reportDto.classification}"/></td>
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

                                            <div class="alert alert-info report-sub-title" role="alert">
                                                <h4 style="border-bottom: none">Section B (Inspection Details)</h4>
                                            </div>
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tr>
                                                                <td class="col-4">Date of Inspection</td>
                                                                <td><c:out value="${reportDto.inspectionDate}"/></td>
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

                                            <div class="alert alert-info report-sub-title" role="alert">
                                                <h4 style="border-bottom: none">Section C (Inspection Findings)</h4>
                                            </div>

                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tr>
                                                                <td colspan="2" style="font-weight:bold">Part I: Inspection Checklist</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="col-4">Checklist Used</td>
                                                                <td><c:out value="${reportDto.checkListUsed}"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" style="font-weight:bold">Part II: Findings/Observations</td>
                                                            </tr>
                                                            <tr>
                                                                <td><label for="observation"></label>Observation</td>
                                                                <td>
                                                                    <textarea id="observation" name="observation" cols="200" rows="5" maxlength="300" data-type="reportInput" disabled="disabled"><c:out value="${reportDto.observation}"/></textarea>
                                                                    <span data-err-ind="observation" class="error-msg"></span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><label for="observationRemarks"></label>Remarks</td>
                                                                <td>
                                                                    <textarea id="observationRemarks" name="observationRemarks" cols="200" rows="5" maxlength="300" data-type="reportInput" disabled="disabled"><c:out value="${reportDto.observationRemarks}"/></textarea>
                                                                    <span data-err-ind="observationRemarks" class="error-msg"></span>
                                                                </td>
                                                            </tr>
                                                            <c:if test="${(reportDto.checkListItemGeneralList ne null && reportDto.checkListItemGeneralList.size() > 0) || (reportDto.checkListItemBsbList ne null && reportDto.checkListItemBsbList.size() > 0)}">
                                                                <tr>
                                                                    <td>Checklist Item</td>
                                                                    <td>
                                                                        <c:if test="${reportDto.checkListItemGeneralList ne null && reportDto.checkListItemGeneralList.size() > 0}">
                                                                            <div class="table-gp">
                                                                                <p>General Regulation</p>
                                                                                <table aria-describedby="" class="table">
                                                                                    <thead>
                                                                                    <tr>
                                                                                        <th scope="col" style="width:5%">S/N</th>
                                                                                        <th scope="col" style="width:20%">Item Description</th>
                                                                                        <th scope="col" style="width:20%">Findings/Non-Compliance</th>
                                                                                        <th scope="col" style="width:20%">Action Required</th>
                                                                                        <th scope="col" style="width:15%">Rectified?</th>
                                                                                        <th scope="col" style="width:20%">Applicant's Input</th>
                                                                                    </tr>
                                                                                    </thead>
                                                                                    <c:forEach var="checkList" items="${reportDto.checkListItemGeneralList}" varStatus="status">
                                                                                        <c:if test="${checkList.excludeFromApplicantVersion ne 'true'}">
                                                                                            <tr>
                                                                                                <td>1.${status.count}</td>
                                                                                                <td><c:out value="${checkList.itemDescription}"/></td>
                                                                                                <td>
                                                                                                    <textarea id="finding--v--${checkList.id}" name="finding--v--${checkList.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${checkList.finding}"/></textarea>
                                                                                                    <span data-err-ind="finding--v--${checkList.id}" class="error-msg"></span>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <textarea id="actionRequired--v--${checkList.id}" name="actionRequired--v--${checkList.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${checkList.actionRequired}"/></textarea>
                                                                                                    <span data-err-ind="actionRequired--v--${checkList.id}" class="error-msg"></span>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <c:choose>
                                                                                                        <c:when test="${checkList.rectified eq 'true'}">Yes</c:when>
                                                                                                        <c:otherwise>No</c:otherwise>
                                                                                                    </c:choose>
                                                                                                </td>
                                                                                                <td><c:out value="${checkList.applicantInput}"/></td>
                                                                                            </tr>
                                                                                        </c:if>
                                                                                    </c:forEach>
                                                                                </table>
                                                                            </div>
                                                                        </c:if>
                                                                        <c:if test="${reportDto.checkListItemBsbList ne null && reportDto.checkListItemBsbList.size() > 0}">
                                                                            <div class="table-gp">
                                                                                <p>BSB Regulation</p>
                                                                                <table aria-describedby="" class="table">
                                                                                    <thead>
                                                                                    <tr>
                                                                                        <th scope="col" style="width:5%">S/N</th>
                                                                                        <th scope="col" style="width:20%">Item Description</th>
                                                                                        <th scope="col" style="width:20%">Findings/Non-Compliance</th>
                                                                                        <th scope="col" style="width:20%">Action Required</th>
                                                                                        <th scope="col" style="width:15%">Rectified?</th>
                                                                                        <th scope="col" style="width:20%">Applicant's Input</th>
                                                                                    </tr>
                                                                                    </thead>
                                                                                    <c:forEach var="checkList" items="${reportDto.checkListItemBsbList}" varStatus="status">
                                                                                        <c:if test="${checkList.excludeFromApplicantVersion ne 'true'}">
                                                                                            <tr>
                                                                                                <td>1.${status.count}</td>
                                                                                                <td><c:out value="${checkList.itemDescription}"/></td>
                                                                                                <td>
                                                                                                    <textarea id="finding--v--${checkList.id}" name="finding--v--${checkList.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${checkList.finding}"/></textarea>
                                                                                                    <span data-err-ind="finding--v--${checkList.id}" class="error-msg"></span>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <textarea id="actionRequired--v--${checkList.id}" name="actionRequired--v--${checkList.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${checkList.actionRequired}"/></textarea>
                                                                                                    <span data-err-ind="actionRequired--v--${checkList.id}" class="error-msg"></span>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <c:choose>
                                                                                                        <c:when test="${checkList.rectified eq 'true'}">Yes</c:when>
                                                                                                        <c:otherwise>No</c:otherwise>
                                                                                                    </c:choose>
                                                                                                </td>
                                                                                                <td><c:out value="${checkList.applicantInput}"/></td>
                                                                                            </tr>
                                                                                        </c:if>
                                                                                    </c:forEach>
                                                                                </table>
                                                                            </div>
                                                                        </c:if>
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                            <c:if test="${reportDto.followUpItemGeneralList ne null && reportDto.followUpItemGeneralList.size() > 0}">
                                                                <tr>
                                                                    <td colspan="2" style="font-weight:bold">Part III: Follow-Up Item</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>Follow-up Items</td>
                                                                    <td>
                                                                        <div class="table-gp">
                                                                            <p>General Regulation</p>
                                                                            <table aria-describedby="" class="table">
                                                                                <thead>
                                                                                <tr>
                                                                                    <th scope="col" style="width:5%">S/N</th>
                                                                                    <th scope="col" style="width:25%">Item Description</th>
                                                                                    <th scope="col" style="width:25%">Observations for Follow-up</th>
                                                                                    <th scope="col" style="width:25%">Action Required</th>
                                                                                    <th scope="col" style="width:20%">Deadline</th>
                                                                                </tr>
                                                                                </thead>
                                                                                <c:forEach var="followUp" items="${reportDto.followUpItemGeneralList}" varStatus="status">
                                                                                    <tr>
                                                                                        <td>1.${status.count}</td>
                                                                                        <td><c:out value="${followUp.itemDescription}"/></td>
                                                                                        <td>
                                                                                            <textarea id="observation--v--${followUp.id}" name="observation--v--${followUp.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${followUp.observation}"/></textarea>
                                                                                            <span data-err-ind="observation--v--${followUp.id}" class="error-msg"></span>
                                                                                        </td>
                                                                                        <td>
                                                                                            <textarea id="actionRequired--v--${followUp.id}" name="actionRequired--v--${followUp.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${followUp.actionRequired}"/></textarea>
                                                                                            <span data-err-ind="actionRequired--v--${followUp.id}" class="error-msg"></span>
                                                                                        </td>
                                                                                        <td><c:out value="${followUp.dueDate}"/></td>
                                                                                    </tr>
                                                                                </c:forEach>
                                                                            </table>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="alert alert-info report-sub-title" role="alert">
                                                <h4 style="border-bottom: none">Section D (Recommendation)</h4>
                                            </div>
                                            <div class="row">
                                                <div class="col-12">
                                                    <div class="table-gp">
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tr>
                                                                <td class="col-4">Deficiency <span style="color: red">*</span></td>
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
                                                                    <span data-err-ind="deficiency" class="error-msg"></span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><label for="outcome"></label>Outcome <span style="color: red">*</span></td>
                                                                <td>
                                                                    <select name="outcome" id="outcome" class="outcomeDropdown" data-type="reportInput" disabled="disabled">
                                                                        <option value="">Please Select</option>
                                                                        <option value="${MasterCodeConstants.VALUE_OUTCOME_PASS}" <c:if test="${reportDto.outcome eq MasterCodeConstants.VALUE_OUTCOME_PASS}">selected="selected"</c:if>>Pass</option>
                                                                        <option value="${MasterCodeConstants.VALUE_OUTCOME_PASS_WITH_CONDITION}" <c:if test="${reportDto.outcome eq MasterCodeConstants.VALUE_OUTCOME_PASS_WITH_CONDITION}">selected="selected"</c:if>>Pass with condition</option>
                                                                        <option value="${MasterCodeConstants.VALUE_OUTCOME_FAIL}" <c:if test="${reportDto.outcome eq MasterCodeConstants.VALUE_OUTCOME_FAIL}">selected="selected"</c:if>>Fail</option>
                                                                    </select>
                                                                    <span data-err-ind="outcome" class="error-msg"></span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><label for="recommendationRemarks"></label>Remarks</td>
                                                                <td>
                                                                    <textarea id="recommendationRemarks" name="recommendationRemarks" cols="200" rows="5" maxlength="300" data-type="reportInput" disabled="disabled"><c:out value="${reportDto.recommendationRemarks}"/></textarea>
                                                                    <span data-err-ind="recommendationRemarks" class="error-msg"></span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><label for="applicantRemarks"></label>Applicant Remarks</td>
                                                                <td>
                                                                    <textarea id="applicantRemarks" name="applicantRemarks" cols="200" rows="5" maxlength="300"><c:out value="${reportDto.applicantRemarks}"/></textarea>
                                                                    <span data-err-ind="applicantRemarks" class="error-msg"></span>
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
                                            <a class="back" id="previous" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
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