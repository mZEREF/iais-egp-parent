<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
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
                                                                <td class="col-4">Date of Inspection</td>
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
                                                    <div class="table-gp">
                                                        <div class="text">
                                                            <p><h4><strong><span>Part I: Inspection Checklist</span></strong></h4></p>
                                                        </div>
                                                        <table aria-describedby="" class="table">
                                                            <thead style="display: none">
                                                            <tr>
                                                                <th scope="col"></th>
                                                            </tr>
                                                            </thead>
                                                            <tr>
                                                                <td class="col-xs-4">
                                                                    <p>Checklist Used</p>
                                                                </td>
                                                                <td class="col-xs-4">
                                                                    <p><c:out value="${reportDto.checkListUsed}"/></p>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <div class="text">
                                                            <p><h4><strong><span>Part II: Findings/Observations</span></strong></h4></p>
                                                        </div>
                                                        <div class="table-gp">
                                                            <table aria-describedby="" class="table">
                                                                <thead style="display: none">
                                                                <tr>
                                                                    <th scope="col"></th>
                                                                </tr>
                                                                </thead>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>Observation</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>
                                                                            <textarea name="observation" cols="100" rows="6" maxlength="300" data-type="reportInput" disabled="disabled"><c:out value="${reportDto.observation}"/></textarea>
                                                                        </p>
                                                                        <span data-err-ind="observation" class="error-msg"></span>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>Remarks</p>
                                                                    </td>
                                                                    <td class="col-xs-4">
                                                                        <p>
                                                                            <textarea name="observationRemarks" cols="100" rows="6" maxlength="300" data-type="reportInput" disabled="disabled"><c:out value="${reportDto.observationRemarks}"/></textarea>
                                                                        </p>
                                                                        <span data-err-ind="observationRemarks" class="error-msg"></span>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td class="col-xs-4">
                                                                        <p>Checklist Item</p>
                                                                    </td>
                                                                    <td class="col-xs-8">
                                                                        <p>BSB Regulation</p>
                                                                        <c:if test="${reportDto.checkListItemBsbList ne null && reportDto.checkListItemBsbList.size() > 0}">
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
                                                                                        <td><p>1.${status.count}</p></td>
                                                                                        <td><p><c:out value="${checkList.itemDescription}"/></p></td>
                                                                                        <td>
                                                                                            <p><textarea id="finding--v--${checkList.id}" name="finding--v--${checkList.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${checkList.finding}"/></textarea></p>
                                                                                            <span data-err-ind="finding--v--${checkList.id}" class="error-msg"></span>
                                                                                        </td>
                                                                                        <td>
                                                                                            <p><textarea id="actionRequired--v--${checkList.id}" name="actionRequired--v--${checkList.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${checkList.actionRequired}"/></textarea></p>
                                                                                            <span data-err-ind="actionRequired--v--${checkList.id}" class="error-msg"></span>
                                                                                        </td>
                                                                                        <td>
                                                                                            <p>
                                                                                                <c:choose>
                                                                                                    <c:when test="${checkList.rectified eq 'true'}">Yes</c:when>
                                                                                                    <c:otherwise>No</c:otherwise>
                                                                                                </c:choose>
                                                                                            </p>
                                                                                        </td>
                                                                                        <td><p><c:out value="${checkList.applicantInput}"/></p></td>
                                                                                        <td>
                                                                                            <p><input id="excludeFromApplicantVersion--v--${checkList.id}" name="excludeFromApplicantVersion--v--${checkList.id}" type="checkbox" <c:if test="${checkList.excludeFromApplicantVersion eq 'true'}">checked="checked"</c:if> data-type="reportInput" disabled="disabled"/></p>
                                                                                        </td>
                                                                                    </tr>
                                                                                </c:if>
                                                                            </c:forEach>
                                                                        </table>
                                                                        </c:if>
                                                                        <c:if test="${reportDto.checkListItemBsbList == null}">
                                                                            <p>0</p>
                                                                        </c:if>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </div>
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
                                                                    <td class="col-xs-4">
                                                                        <p>Follow-up Items</p>
                                                                    </td>
                                                                    <td class="col-xs-8">
                                                                        <p>BSB Regulation</p>
                                                                        <c:if test="${reportDto.followUpItemGeneralList ne null && reportDto.followUpItemGeneralList.size() > 0}">
                                                                        <table aria-describedby="" class="table">
                                                                            <thead>
                                                                            <tr>
                                                                                <th scope="col">S/N</th>
                                                                                <th scope="col">Item Description</th>
                                                                                <th scope="col">Observations for Follow-up</th>
                                                                                <th scope="col">Action Required</th>
                                                                                <th scope="col">Deadline</th>
                                                                            </tr>
                                                                            </thead>
                                                                            <c:forEach var="followUp" items="${reportDto.followUpItemGeneralList}" varStatus="status">
                                                                                <tr>
                                                                                    <td><p>1.${status.count}</p></td>
                                                                                    <td><p><c:out value="${followUp.itemDescription}"/></p></td>
                                                                                    <td>
                                                                                        <p><textarea id="observation--v--${followUp.id}" name="observation--v--${followUp.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${followUp.observation}"/></textarea></p>
                                                                                        <span data-err-ind="observation--v--${followUp.id}" class="error-msg"></span>
                                                                                    </td>
                                                                                    <td>
                                                                                        <p><textarea id="actionRequired--v--${followUp.id}" name="actionRequired--v--${followUp.id}" cols="20" rows="5" maxlength="500" data-type="reportInput" disabled="disabled"><c:out value="${followUp.actionRequired}"/></textarea></p>
                                                                                        <span data-err-ind="actionRequired--v--${followUp.id}" class="error-msg"></span>
                                                                                    </td>
                                                                                    <td><p><c:out value="${followUp.dueDt}"/></p></td>
                                                                                </tr>
                                                                            </c:forEach>
                                                                        </table>
                                                                        </c:if>
                                                                        <c:if test="${reportDto.followUpItemGeneralList == null}">
                                                                        <p>0</p>
                                                                        </c:if>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </div>
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
                                                                <td>Deficiency <span style="color: red">*</span></td>
                                                                <td>
                                                                    <p>
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
                                                                    </p>
                                                                    <span data-err-ind="deficiency" class="error-msg"></span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><label for="outcome"></label>Outcome <span style="color: red">*</span></td>
                                                                <td>
                                                                    <p>
                                                                    <select name="outcome" id="outcome" class="outcomeDropdown" data-type="reportInput" disabled="disabled">
                                                                        <option value="">Please Select</option>
                                                                        <option value="${MasterCodeConstants.VALUE_OUTCOME_PASS}" <c:if test="${reportDto.outcome eq MasterCodeConstants.VALUE_OUTCOME_PASS}">selected="selected"</c:if>>Pass</option>
                                                                        <option value="${MasterCodeConstants.VALUE_OUTCOME_PASS_WITH_CONDITION}" <c:if test="${reportDto.outcome eq MasterCodeConstants.VALUE_OUTCOME_PASS_WITH_CONDITION}">selected="selected"</c:if>>Pass with condition</option>
                                                                        <option value="${MasterCodeConstants.VALUE_OUTCOME_FAIL}" <c:if test="${reportDto.outcome eq MasterCodeConstants.VALUE_OUTCOME_FAIL}">selected="selected"</c:if>>Fail</option>
                                                                    </select>
                                                                    </p>
                                                                    <span data-err-ind="outcome" class="error-msg"></span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><label for="recommendationRemarks"></label>Remarks</td>
                                                                <td>
                                                                    <p>
                                                                    <textarea id="recommendationRemarks" name="recommendationRemarks" cols="200" rows="5" maxlength="300" data-type="reportInput" disabled="disabled"><c:out value="${reportDto.recommendationRemarks}"/></textarea>
                                                                    </p>
                                                                    <span data-err-ind="recommendationRemarks" class="error-msg"></span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><label for="applicantRemarks"></label>Applicant Remarks</td>
                                                                <td>
                                                                    <p>
                                                                    <textarea id="applicantRemarks" name="applicantRemarks" cols="200" rows="5" maxlength="300"><c:out value="${reportDto.applicantRemarks}"/></textarea>
                                                                    </p>
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