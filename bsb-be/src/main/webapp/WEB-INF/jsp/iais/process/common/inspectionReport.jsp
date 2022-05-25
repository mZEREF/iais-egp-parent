<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
<%--@elvariable id="reportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto"--%>
<c:choose>
    <c:when test="${reportDto eq null}">
<%--        <iais:message key="GENERAL_ACK018" escape="true"/>--%>
        <p>No inspection by MOH</p>
    </c:when>
    <c:otherwise>
        <div>
            <button type="button" class="btn btn-secondary">DOWNLOAD</button>
        </div>
        <br/>
        <br/>
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
                                <td>Observation</td>
                                <td><c:out value="${reportDto.observation}"/></td>
                            </tr>
                            <tr>
                                <td>Remarks</td>
                                <td><c:out value="${reportDto.observationRemarks}"/></td>
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
                                                <th scope="col" style="width:15%">Item Description</th>
                                                <th scope="col" style="width:15%">Findings/Non-Compliance</th>
                                                <th scope="col" style="width:15%">Action Required</th>
                                                <th scope="col" style="width:15%">Rectified?</th>
                                                <th scope="col" style="width:15%">Applicant's Input</th>
                                                <th scope="col" style="width:20%">Exclude from Applicant Version</th>
                                            </tr>
                                            </thead>
                                            <c:forEach var="checkList" items="${reportDto.checkListItemGeneralList}" varStatus="status">
                                                <tr>
                                                    <td>1.${status.count}</td>
                                                    <td><c:out value="${checkList.itemDescription}"/></td>
                                                    <td><c:out value="${checkList.finding}"/></td>
                                                    <td><c:out value="${checkList.actionRequired}"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${checkList.rectified eq 'true'}">Yes</c:when>
                                                            <c:otherwise>No</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td><c:out value="${checkList.applicantInput}"/></td>
                                                    <td>
                                                        <input id="excludeFromApplicantVersion--v--${checkList.id}" name="excludeFromApplicantVersion--v--${checkList.id}" type="checkbox" <c:if test="${checkList.excludeFromApplicantVersion eq 'true'}">checked="checked"</c:if> data-type="reportInput" disabled="disabled">
                                                    </td>
                                                </tr>
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
                                                <th scope="col" style="width:15%">Item Description</th>
                                                <th scope="col" style="width:15%">Findings/Non-Compliance</th>
                                                <th scope="col" style="width:15%">Action Required</th>
                                                <th scope="col" style="width:15%">Rectified?</th>
                                                <th scope="col" style="width:15%">Applicant's Input</th>
                                                <th scope="col" style="width:20%">Exclude from Applicant Version</th>
                                            </tr>
                                            </thead>
                                            <c:forEach var="checkList" items="${reportDto.checkListItemBsbList}" varStatus="status">
                                                <tr>
                                                    <td>1.${status.count}</td>
                                                    <td><c:out value="${checkList.itemDescription}"/></td>
                                                    <td><c:out value="${checkList.finding}"/></td>
                                                    <td><c:out value="${checkList.actionRequired}"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${checkList.rectified eq 'true'}">Yes</c:when>
                                                            <c:otherwise>No</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td><c:out value="${checkList.applicantInput}"/></td>
                                                    <td>
                                                        <input id="excludeFromApplicantVersion--v--${checkList.id}" name="excludeFromApplicantVersion--v--${checkList.id}" type="checkbox" <c:if test="${checkList.excludeFromApplicantVersion eq 'true'}">checked="checked"</c:if> data-type="reportInput" disabled="disabled"/>
                                                    </td>
                                                </tr>
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
                                                    <td><c:out value="${followUp.observation}"/></td>
                                                    <td><c:out value="${followUp.actionRequired}"/></td>
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
                                <td class="col-4">Deficiency </td>
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
                                <td><label for="outcome"></label>Outcome </td>
                                <td>
                                    <select name="outcome" id="outcome" class="outcomeDropdown" data-type="reportInput" disabled="disabled">
                                        <option value="">Please Select</option>
                                        <option value="${MasterCodeConstants.VALUE_OUTCOME_PASS}" <c:if test="${reportDto.outcome eq MasterCodeConstants.VALUE_OUTCOME_PASS}">selected="selected"</c:if>>Pass</option>
                                        <option value="${MasterCodeConstants.VALUE_OUTCOME_PASS_WITH_CONDITION}" <c:if test="${reportDto.outcome eq MasterCodeConstants.VALUE_OUTCOME_PASS_WITH_CONDITION}">selected="selected"</c:if>>Pass with condition</option>
                                        <option value="${MasterCodeConstants.VALUE_OUTCOME_FAIL}" <c:if test="${reportDto.outcome eq MasterCodeConstants.VALUE_OUTCOME_FAIL}">selected="selected"</c:if>>Fail</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td>Remarks</td>
                                <td><c:out value="${reportDto.recommendationRemarks}"/></td>
                            </tr>
                            <tr>
                                <td>Facility Validity Date</td>
                                <td><c:out value="${reportDto.facilityValidityDate}"/></td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<br><br>
<div style="text-align: right">
    <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
</div>