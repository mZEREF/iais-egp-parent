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

    <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Part I: Inspection Checklist</p>
    <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Part II: Findings/Observations</p>
    <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Part III: Follow-Up Item</p>

    <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Transferring Facility:</p>

    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <th scope="col" style="width:5%">S/N</th>
                        <th scope="col" style="width:15%">Category</th>
                        <th scope="col" style="width:25%">Item Description</th>
                        <th scope="col" style="width:15%">Finding Type</th>
                        <th scope="col" style="width:30%">MOH Remarks</th>
                        <th scope="col" style="width:10%">Deadline</th>
                    </tr>
                    </thead>
                    <c:forEach var="finding" items="${insFindingList}" varStatus="status">
                    <tr>
                        <td>${status.count}</td>
                        <td>${finding.section}</td>
                        <td>${finding.item}</td>
                        <td>${finding.findingType}</td>
                        <td>${finding.remarks}</td>
                        <td>${finding.deadline}</td>
                    </tr>
                    </c:forEach>
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