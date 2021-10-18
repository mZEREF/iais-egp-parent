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
<%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
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
                <table aria-describedby="" class="table">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
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
                            <p>Clinical Governance Officer(s)</p>
                        </td>
                        <td class="col-xs-4">
                            <c:if test="${insRepDto.clinicalGovernanceOfficer != null && not empty insRepDto.clinicalGovernanceOfficer}">
                                <p><c:forEach items="${insRepDto.clinicalGovernanceOfficer}" var="cgoName">
                                    <c:out value="${cgoName}"/><br>
                                </c:forEach></p>
                            </c:if>
                        </td>
                        <td class="col-xs-4"/>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Principal Officer(s)</p>
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
                <table aria-describedby="" class="table">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
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
                            <c:out value="${insRepDto.inspectionStartTime}"></c:out>-<c:out
                                value="${insRepDto.inspectionEndTime}"></c:out>
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
                            <p>Other Inspection Officer(s)</p>
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
                            <p>${insRepDto.serviceName}</p>
                        </td>
                        <td class="col-xs-4"></td>
                    </tr>
                </table>
                <div class="text">
                    <p><h4><strong><span>Part II: Findings</span></strong></h4></p>
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
                                <p><c:out value="${insRepDto.markedForAudit}"/>&nbsp;&nbsp;<fmt:formatDate
                                        value="${insRepDto.tcuDate}" pattern="dd/MM/yyyy"/></p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Recommendation</p>
                            </td>
                            <td class="col-xs-4">
                                <p>${insRepDto.bestPractice}</p>
                            </td>
                            <td class="col-xs-4"/>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Observation</p>
                            </td>
                            <td class="col-xs-4">
                                <p>${insRepDto.observation}</p>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Non-Compliances</p>
                            </td>
                            <td colspan="2" class="col-xs-8">
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
                                        <c:forEach items="${insRepDto.ncRegulation}" var="ncRegulations"
                                                   varStatus="status">
                                            <tr>
                                                <td>
                                                    <p><c:out value="${status.count}"></c:out></p>
                                                </td>
                                                <c:if test="${specialServiceForChecklistDecide == '1'}">
                                                    <td><c:out value="${ncRegulations.vehicleName}"></c:out></td>
                                                </c:if>
                                                <td>
                                                    <p><c:out value="${ncRegulations.nc}"></c:out></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${ncRegulations.regulation}"></c:out></p>
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
                                <p>Risk Level</p>
                            </td>
                            <td class="col-xs-4">
                                <p><iais:code code="${insRepDto.riskLevel}"></iais:code></p>
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
                <table aria-describedby="" class="table">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                    <tr>
                        <td class="col-xs-4">
                            <p>Rectified</p>
                        </td>
                        <td colspan="2" class="col-xs-8">
                            <c:if test="${insRepDto.ncRectification != null}">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col" >SN</th>
                                        <c:if test="${specialServiceForChecklistDecide == '1'}"><th scope="col" >Vehicle Name</th></c:if>
                                        <th scope="col" >Checklist Item</th>
                                        <th scope="col" >Findings/NCs</th>
                                        <th scope="col" >Rectified?</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${insRepDto.ncRectification}" var="ncRectification"
                                               varStatus="status">
                                        <tr>
                                            <td>
                                                <p><c:out value="${status.count}"></c:out></p>
                                            </td>
                                            <c:if test="${specialServiceForChecklistDecide == '1'}">
                                                <td><c:out value="${ncRectification.vehicleName}"></c:out></td>
                                            </c:if>
                                            <td>
                                                <p><c:out value="${ncRectification.nc}"></c:out></p>
                                            </td>
                                            <td>
                                                <p><c:out value="${ncRectification.ncs}"></c:out></p>
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
                            <p>Remarks</p>
                        </td>
                        <div>
                            <td class="col-xs-4">
                                <p><c:out value="${insRepDto.inspectypeRemarks}"></c:out></p>
                            </td>
                        </div>
                        <td class="col-xs-4">
                        </td>
                    </tr>

                    <tr>
                        <td class="col-xs-4">
                            <p>Rectified Within KPI? <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip"
                                                        data-html="true"
                                                        data-original-title="${kpiInfo}">i</a></p>
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
    <c:if test="${appType!='APTY007' && appType!='APTY009'}">
        <div id="recommendationTitle" class="alert alert-info" role="alert">
            <strong>
                <h4 style="border-bottom: none">Section E (Recommendations)</h4>
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
                        <c:if test="${appType!='APTY005' && appType!='APTY006' && appType!='APTY008' && appType!='APTY001'}">
                            <tr>
                                <td class="col-xs-4">
                                    <p>Recommendation <strong style="color:#ff0000;"> *</strong></p>
                                </td>
                                <td class="col-xs-4">
                                    <iais:select cssClass="nice-select recommendation" id="recommendation"
                                                 name="recommendation"
                                                 options="recommendationOption"
                                                 firstOption="Please Select"
                                                 value="${appPremisesRecommendationDto.recommendation}"
                                                 onchange="javascirpt:changeRecommendation(this.value);"/>
                                </td>
                                <td class="col-xs-4"></td>
                            </tr>
                            <tr id="period" >
                                <td class="col-xs-4">
                                    <p>Period <strong style="color:#ff0000;"> *</strong></p>
                                </td>
                                <td class="col-xs-4">
                                    <iais:select cssClass="nice-select periods" name="periods" options="riskOption"
                                                 firstOption="Please Select"
                                                 onchange="javascirpt:changePeriod();"
                                                 value="${appPremisesRecommendationDto.period}"/>
                                    <span id="error_period" name="iaisErrorMsg" class="error-msg"></span>
                                </td>
                                <td class="col-xs-4"></td>
                            </tr>
                            <tr id="selfPeriod">
                                <td class="col-xs-4">
                                    <p>Other Period <strong style="color:#ff0000;"> *</strong></p>
                                </td>
                                <td class="col-xs-4">
                                    <input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                                           onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                                           id=recomInNumber type="text" name="number" maxlength="2"
                                           value="${appPremisesRecommendationDto.recomInNumber}">
                                    <iais:select cssClass="nice-select chronoUnit" id="chronoUnit" name="chrono"
                                                 options="chronoOption"
                                                 value="${appPremisesRecommendationDto.chronoUnit}"/>
                                    <span id="error_recomInNumber" name="iaisErrorMsg" class="error-msg"></span>
                                    <span id="error_chronoUnit" name="iaisErrorMsg" class="error-msg"></span>
                                </td>
                                <td class="col-xs-4"></td>
                            </tr>
                        </c:if>
                        <c:if test="${appType=='APTY005' || appType=='APTY006' || appType=='APTY008' || appType=='APTY001'}">
                            <tr>
                                <td class="col-xs-4">
                                    <p>Recommendation <strong style="color:#ff0000;"> *</strong></p>
                                </td>
                                <td class="col-xs-4">
                                    <iais:select cssClass="nice-select recommendationRfc" id="recommendationRfc"
                                                 name="recommendationRfc" options="recommendationOption"
                                                 firstOption="Please Select"
                                                 value="${appPremisesRecommendationDto.recommendation}" onchange="doChangeVehicleShow(this.value)"/>
                                </td>
                                <td class="col-xs-4"></td>
                            </tr>
                        </c:if>
                        <%@include file="/WEB-INF/jsp/iais/report/reportSvcVehicleShow.jsp"%>
                    </table>
                </div>
            </div>
        </div>
        <div id="sectionF" class="alert alert-info" role="alert">
            <strong>
                <h4>Section F (After Action)</h4>
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
                            <td class="col-xs-4">
                                <p>Follow up Action <strong style="color:#ff0000;"> *</strong></p>
                            </td>
                            <td class="col-xs-4">
                                <p><textarea style="resize:none" name="followUpAction" cols="50" rows="6"
                                             title="content"
                                             maxlength="8000"><c:out
                                        value="${appPremisesRecommendationDto.followUpAction}"/></textarea></p>
                                <span id="error_followUpAction" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>To Engage Enforcement?</p>
                            </td>
                            <td class="col-xs-4">
                                <input type="checkbox" id="enforcement" name="engageEnforcement"
                                       onchange="javascirpt:changeEngage();"
                                       <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if> >
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr id="engageRemarks" style="display: none;">
                            <td class="col-xs-4">
                                <p>Enforcement Remarks <strong style="color:#ff0000;"> *</strong></p>
                            </td>
                            <td class="col-xs-4">
                            <textarea style="resize:none" name="enforcementRemarks" cols="50" rows="6" title="content"
                                      MAXLENGTH="4000"><c:out
                                    value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/></textarea>
                                <span id="error_enforcementRemarks" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${appType=='APTY007'|| appType=='APTY009'}">
        <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section E (After Action)</h4>
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
                            <td class="col-xs-4">
                                <p>Follow up Action <strong style="color:#ff0000;"> *</strong></p>
                            </td>
                            <td class="col-xs-4">
                                <p><textarea style="resize:none" name="followUpAction" cols="50" rows="6"
                                             title="content"
                                             maxlength="8000"><c:out
                                        value="${appPremisesRecommendationDto.followUpAction}"/></textarea></p>
                                <span id="error_followUpAction" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>To Engage Enforcement?</p>
                            </td>
                            <td class="col-xs-4">
                                <input type="checkbox" id="enforcement" name="engageEnforcement"
                                       onchange="javascirpt:changeEngage();"
                                       <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if> >
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr id="engageRemarks" style="display: none;">
                            <td class="col-xs-4">
                                <p>Enforcement Remarks <strong style="color:#ff0000;"> *</strong></p>
                            </td>
                            <td class="col-xs-4">
                            <textarea style="resize:none" name="enforcementRemarks" cols="50" rows="6" title="content"
                                      MAXLENGTH="4000"><c:out
                                    value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/></textarea>
                                <span id="error_enforcementRemarks" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </c:if>
</div>
<a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>

<script type="text/javascript">

    $(function () {
        console.log('----' + '${RECOMMENDATION_DTO.recommendation}');
    })

    function insRepsubmit() {
        $("#mainForm").submit();
    }

    function changePeriod() {
        const val = $("#periods").val();
        const recommation = $("#recommendation").val();
        const recommendationRfc = $("#recommendationRfc").val();
        if (val == "Others" && (recommation == 'IRE001' || recommation == 'IRE002')) {
            $("#selfPeriod").show();
            const reg = /^[0-9]+.?[0-9]*$/;
            const num = $("#recomInNumber").val();
            const period = $("#chronoUnit").val();
            if (reg.test(num)) {
                if (period.match("DTPE001")) {
                    $("#periodValue").html(num + ' ' + 'Year(s)');
                }
                if (period.match("DTPE002")) {
                    $("#periodValue").html(num + ' ' + 'Month(s)');
                }
            }
        } else if (val != "Others" && (recommation == 'IRE001' || recommation == 'IRE002')) {
            $("#selfPeriod").hide();
            const value = $("#periods").find("option:selected").text();
            $("#periodValue").html(value);
        } else if (recommation == 'IRE003') {
            $("#periodValue").html('Reject');
        } else if (recommendationRfc == 'IRE007') {
            $("#periodValue").html('Approve');
        } else if (recommendationRfc == 'IRE008') {
            $("#periodValue").html('Reject');
        } else {
            $("#periodValue").html('');
        }
    }

    function changeRecommendation(obj) {
        if (obj == "IRE001" || obj == "IRE002") {
            $("#period").show();
        } else {
            $("#period").hide();
            $("#selfPeriod").hide();
        }
        doChangeVehicleShow(obj);
    }


    function changeEngage() {
        if ($('#enforcement').is(':checked')) {
            $("#engageRemarks").show();
        } else {
            $("#engageRemarks").hide();
        }
    }


    $(document).ready(function () {
        var type = $('input[name="appType"]').val();
        var recommendation = (type === 'APTY005' ? $("#recommendationRfc").val() : $("#recommendation").val());
        changeRecommendation(recommendation);
        if ($("#periods").val() == "Others" && type != "APTY005" && type != "APTY007"&& type != "APTY009") {
            changePeriod("Others");
        } else {
            $("#selfPeriod").hide();
        }
        if ($('#enforcement').is(':checked')) {
            $("#engageRemarks").show();
        }
    });

</script>

