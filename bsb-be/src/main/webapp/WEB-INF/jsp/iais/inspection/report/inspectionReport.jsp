<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%@page import="sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants" %>

<%--@elvariable id="insInfo" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsFacInfoDto"--%>
<%--@elvariable id="insFindingList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.inspection.InsFindingDisplayDto>"--%>
<%--@elvariable id="insOutcome" type="sg.gov.moh.iais.egp.bsb.dto.entity.InspectionOutcomeDto"--%>
<style>
    .report-sub-title > h4 {
        font-size: 1.8rem;
        font-weight: 700;
        color: black;
        margin: 0;
    }
</style>

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
                        <td><c:out value="${insInfo.facName}"/></td>
                    </tr>
                    <tr>
                        <td>Address</td>
                        <td><c:out value="${TableDisplayUtil.getOneLineAddress(insInfo.blk, insInfo.street, insInfo.street, insInfo.unit, insInfo.postalCode)}"/></td>
                    </tr>
                    <tr>
                        <td>Facility Classification</td>
                        <td><iais:code code="${insInfo.classification}"/></td>
                    </tr>
                    <tr>
                        <td>Person-in-Charge</td>
                        <td><c:out value="${insInfo.adminName}"/></td>
                    </tr>
                    <tr>
                        <td>Contact information of person-in-charge</td>
                        <td><c:out value="${insInfo.adminMobileNo} / ${insInfo.adminEmail}"/></td>
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
                        <td>07/03/2022</td>
                    </tr>
                    <tr>
                        <td>Purpose of Inspection</td>
                        <td>To Inspect BSL 3 Facility</td>
                    </tr>
                    <tr>
                        <td>Facility Representative</td>
                        <td>Abe Shino</td>
                    </tr>
                    <tr>
                        <td>MOH Inspector</td>
                        <td>DO1, DO2</td>
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
                        <td class="col-6">Checklist Used</td>
                        <td>Biosafety Regulation</td>
                    </tr>
                    <tr>
                        <td colspan="2" style="font-weight:bold">Part II: Findings/Observations</td>
                    </tr>
                    <tr>
                        <td class="col-6"><label for="observation"></label>Observation</td>
                        <td>
                            <div class="col-sm-7 col-md-5 col-xs-10">
                                <div class="input-group">
                                    <textarea id="observation" name="observation" cols="200" rows="5" maxlength="300"><c:out value="${processDto.observation}"/></textarea>
                                    <span data-err-ind="observation" class="error-msg"></span>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="col-6"><label for="remarks"></label>Remarks</td>
                        <td>
                            <div class="col-sm-7 col-md-5 col-xs-10">
                                <div class="input-group">
                                    <textarea id="remarks" name="remarks" cols="200" rows="5" maxlength="300"><c:out value="${processDto.remark}"/></textarea>
                                    <span data-err-ind="remark" class="error-msg"></span>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="col-6" rowspan="2" style="vertical-align: top">Checklist Item</td>
                        <td>
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
                                    <c:forEach var="finding" items="${insFindingList}" varStatus="status">
                                    <tr>
                                        <td>${status.count}</td>
                                    </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
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
                                    <c:forEach var="finding" items="${insFindingList}" varStatus="status">
                                        <tr>
                                            <td>${status.count}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="font-weight:bold">Part III: Follow-Up Item</td>
                    </tr>
                    <tr>
                        <td class="col-6">Checklist Used</td>
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
                                    <c:forEach var="finding" items="${insFindingList}" varStatus="status">
                                        <tr>
                                            <td>${status.count}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </td>
                    </tr>
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
                        <td class="col-4">Deficiency</td>
                        <td>
                            <span class="fa <c:choose><c:when test="${InspectionConstants.VALUE_OUTCOME_DEFICIENCY_MAJOR eq insOutcome.deficiency}">fa-check-square-o</c:when><c:otherwise>fa-square-o</c:otherwise></c:choose>"> Major</span>&nbsp;&nbsp;&nbsp;
                            <span class="fa <c:choose><c:when test="${InspectionConstants.VALUE_OUTCOME_DEFICIENCY_MINOR eq insOutcome.deficiency}">fa-check-square-o</c:when><c:otherwise>fa-square-o</c:otherwise></c:choose>"> Minor</span>&nbsp;&nbsp;&nbsp;
                            <span class="fa <c:choose><c:when test="${InspectionConstants.VALUE_OUTCOME_DEFICIENCY_NIL eq insOutcome.deficiency}">fa-check-square-o</c:when><c:otherwise>fa-square-o</c:otherwise></c:choose>"> Nil</span>
                        </td>
                    </tr>
                    <tr>
                        <td>Outcome</td>
                        <td>
                            <span class="fa <c:choose><c:when test="${InspectionConstants.VALUE_OUTCOME_OUTCOME_PASS eq insOutcome.outcome}">fa-check-square-o</c:when><c:otherwise>fa-square-o</c:otherwise></c:choose>"> Pass</span>&nbsp;&nbsp;&nbsp;
                            <span class="fa <c:choose><c:when test="${InspectionConstants.VALUE_OUTCOME_OUTCOME_PASS_WITH_CONDITION eq insOutcome.outcome}">fa-check-square-o</c:when><c:otherwise>fa-square-o</c:otherwise></c:choose>"> Pass with condition(s)</span>&nbsp;&nbsp;&nbsp;
                            <span class="fa <c:choose><c:when test="${InspectionConstants.VALUE_OUTCOME_OUTCOME_FAIL eq insOutcome.outcome}">fa-check-square-o</c:when><c:otherwise>fa-square-o</c:otherwise></c:choose>"> Fail</span>
                        </td>
                    </tr>
                    <tr>
                        <td>Remarks</td>
                        <td><c:out value="${insOutcome.remarks}"/></td>
                    </tr>
                    <tr>
                        <td>Facility Validity Date</td>
                        <td><c:out value="${insOutcome.facilityValidityDate}"/></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>