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
<%--<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>--%>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="confirmAction" value="">
    <div class="tab-pane" id="tabInspection" role="tabpanel">
        <%--        <div class="row">--%>
        <div class="alert alert-info" role="alert">
            <p><span><strong>Section A (HCI Details)</strong></span></p>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Licence No:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.licenceNo}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Service Name:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.serviceName}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>HCI Code:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.hciCode}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>HCI Name:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.hciName}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>HCI Address:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.hciAddress}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Licensee Name:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.licenseeName}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Principal Officers:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.principalOfficers}</p>
                                <c:forEach items="${insRepDto.principalOfficers}" var="po">
                                    <c:out value="${po}"/>
                                </c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Subsumed Services:</p>
                            </td>
                            <td class="col-xs-8">
                                <c:forEach var="service" items="${insRepDto.subsumedServices}">
                                    <p><c:out value="${service}"></c:out></p>
                                </c:forEach>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="alert alert-info" role="alert">
            <p><span><strong>Section B (Type of Inspection)</strong></span></p>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Date of Inspection:</p>
                            </td>
                            <td class="col-xs-8">
                                <fmt:formatDate value="${insRepDto.inspectionDate}"
                                                pattern="dd/MM/yyyy"></fmt:formatDate>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Time of Inspection:</p>
                            </td>
                            <td class="col-xs-8">
                                <fmt:formatDate value="${insRepDto.inspectionDate}"
                                                pattern="dd/MM/yyyy"></fmt:formatDate>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Reason for Visit:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.reasonForVisit}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Inspected By:</p>
                            </td>
                            <td class="col-xs-8">
                                <c:forEach items="${insRepDto.inspectors}" var="inspector" varStatus="status">
                                    <p><c:out value="${inspector}"></c:out></p>
                                </c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Other Inspection Officer:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.inspectOffices}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Reported By:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.reportedBy}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Report Noted By:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.reportNoteBy}</p>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="alert alert-info" role="alert">
            <p><span><strong>Section C (Inspection Findings)</strong></span></p>
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
                                <p>Checklist Used:</p>
                            </td>

                            <td class="col-xs-8">
                                <p>${insRepDto.serviceName}</p>
                                <c:if test="${insRepDto.otherCheckList.adItemList != null}">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>No.</th>
                                            <th>Regulation Clause Number</th>
                                            <th>Item</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="item" items="${insRepDto.otherCheckList.adItemList}"
                                                   varStatus="status">
                                            <tr>
                                                <td class="row_no">${(index.count) }</td>
                                                <td><c:out value="${item.question}"/></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </c:if>
                                <%--                                <c:if test="${insRepDto.otherCheckList.adItemList == null}">--%>
                                <%--                                    NO RESULT !--%>
                                <%--                                </c:if>--%>
                            </td>
                        </tr>
                    </table>
                    <div class="text ">
                        <p><span>Part II: Findings</span></p>
                    </div>
                    <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Remarks:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.taskRemarks}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Marked for Audit:</p>
                            </td>
                            <td class="col-xs-8">
                                <c:if test="${insRepDto.markedForAudit}">
                                    <p>Yes</p>
                                </c:if>
                                <c:if test="${!insRepDto.markedForAudit}">
                                    <p>No</p>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Recommended Best Practices:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.bestPractice}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Non-Compliances:</p>
                            </td>
                            <td class="col-xs-8">
                                <c:if test="${not empty insRepDto.ncRegulation}">
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
                                <p>Status:</p>
                            </td>
                            <td class="col-xs-8">
                                <p>${insRepDto.status}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Risk Level:</p>
                            </td>
                            <td class="col-xs-4">
                                <iais:select name="riskLevel" options="riskLevelOptions" firstOption="Please select"
                                             value="${appPremisesRecommendationDto.recommendation}"/>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section D (Rectification)</h4>
            </strong>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Rectified:</p>
                            </td>
                            <td class="col-xs-8">
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
                                <p>Remarks:</p>
                            </td>
                            <div>
                                <td class="col-xs-4">
                                    <input name="remarks" type="text" value="${appPremisesRecommendationDto.remarks}"
                                           MAXLENGTH="4000">
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
                                <p>YES</p>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section E (Recommendations) </h4>
            </strong>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>TCU needed:</p>
                            </td>
                            <td class="col-xs-4">
                                <input type="checkbox" id="tcuNeeded" name="tcuNeed" onchange="javascirpt:changeTcu();"
                                <c:if test="${appPremisesRecommendationDto.tcuNeeded =='on'}">checked</c:if>
                                >
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr id="tcuDate" hidden>
                            <td class="col-xs-4">
                                <p>TCU Date:</p>
                            </td>
                            <td class="col-xs-4">
                                <iais:datePicker id="tcuDate" name="tcuDate" dateVal=""/>
                                <span id="error_tcuDate" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>Recommendation:</p>
                            </td>
                            <td class="col-xs-4">
                                <iais:select name="recommendation" options="recommendationOption"
                                             firstOption="Please select"
                                             value="${appPremisesRecommendationDto.recommendation}"
                                             onchange="javascirpt:changeRecommendation(this.value);"/>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr id="period" hidden>
                            <td class="col-xs-4">
                                <p>Period:</p>
                            </td>
                            <td class="col-xs-4">
                                <iais:select name="periods" options="riskOption" firstOption="Please select"
                                             onchange="javascirpt:changePeriod(this.value);"
                                             value="${appPremisesRecommendationDto.period}"/>
                                <span id="error_period" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr id="selfPeriod" hidden>
                            <td class="col-xs-4">
                                <p>Other Period:</p>
                            </td>
                            <td class="col-xs-4">
                                <input id=recomInNumber type="text" name="number" value="${number}">
                                <span id="error_recomInNumber" name="iaisErrorMsg" class="error-msg"></span>
                                <iais:select id="chronoUnit" name="chrono" options="chronoOption"
                                             firstOption="Please select" value="${chrono}"/>
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
                                <p>Follow up Action:</p>
                            </td>
                            <td class="col-xs-4">
                                <input name="followUpAction" type="text" value="" MAXLENGTH="4000">
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr>
                            <td class="col-xs-4">
                                <p>To Engage Enforcement?:</p>
                            </td>
                            <td class="col-xs-4">
                                <input type="checkbox" id="enforcement" name="engageEnforcement" onchange="javascirpt:changeEngage();"
                                       <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if>
                                >
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                        <tr id ="engageRemarks" hidden >
                            <td class="col-xs-4">
                                <p>Enforcement Remarks</p>
                            </td>
                            <td class="col-xs-4">
                                <input name="enforcementRemarks" type="text" value="" MAXLENGTH="4000">
                                <span id="error_enforcementRemarks" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
<%--    <div align="right">--%>
<%--        <button id="submitButton" type="submit" class="btn btn-primary">--%>
<%--            Submit--%>
<%--        </button>--%>
<%--    </div>--%>
    <%@include file="/include/validation.jsp" %>
<%--</form>--%>

<script type="text/javascript">

    function submit() {
        if($("#processingDecision").val() == "submit"){
            $("#mainForm").submit();
        }
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
        if (obj == "Approval") {
            document.getElementById("period").style.display = "";
            $("#period").show();
        }else{
            document.getElementById("period").style.display = "none";
        }
    }

    function changeTcu() {
        if ($('#tcuNeeded').is(':checked')) {
            document.getElementById("tcuDate").style.display = "";
            $("#tcuDate").show();
        }else {
            document.getElementById("tcuDate").style.display = "none";
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
        if ($("#recommendation").val() == "Approval") {
            changeRecommendation("Approval");
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

