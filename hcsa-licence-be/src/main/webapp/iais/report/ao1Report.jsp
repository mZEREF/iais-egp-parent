<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
<form method="post" id="aomainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="action_type" value="">
    <div class="row">
    <div class="alert alert-info" role="alert">
        <p><span><strong>Section A (HCI Details)</strong></span></p>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table">
                    <tr>
                        <td class="col-xs-4">
                            <p>Licence.No:</p>
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
                            <p>Principal Officer:</p>
                        </td>
                        <td class="col-xs-8">
                            <p>${insRepDto.principalOfficer}</p>
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
                            <fmt:formatDate value="${insRepDto.inspectionDate}" pattern = "dd/MM/yyyy"></fmt:formatDate>
                        </td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Time of Inspection:</p>
                        </td>
                        <td class="col-xs-8">
                            <fmt:formatDate value="${insRepDto.inspectionDate}" pattern = "dd/MM/yyyy"></fmt:formatDate>
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
                            <%--                                <p>${insRepDto.reportedBy}</p>--%>
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
                            <p>${insRepDto.reportedBy}</p>
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
                            <c:forEach var="sec" items="${insRepDto.otherCheckList.sectionDtos}">
                                <p>Section: &nbsp;<strong>${sec.section}</strong></p>
                                <p>Description: &nbsp;${sec.description}</p>
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Regulation Clause Number</th>
                                        <th>Regulations</th>
                                        <th>Checklist Item</th>
                                        <th>Risk Level</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="chklitem" items="${sec.checklistItemDtos}" varStatus="status">
                                        <tr>
                                            <td>
                                                <p>${chklitem.regulationClauseNo}</p>
                                            </td>
                                            <td>
                                                <p>${chklitem.regulationClause}</p>
                                            </td>
                                            <td>
                                                <p>${chklitem.checklistItem}</p>
                                            </td>
                                            <td>
                                                <p>${chklitem.riskLevel}</p>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:forEach>
                        </td>
                    </tr>
                </table>
                <div class="text ">
                    <p><span>Part II: Findings</span></p>
                </div>
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
                            <p>${insRepDto.markedForAudit}</p>
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
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>SN</th>
                                    <th>Checklist Item</th>
                                    <th>Regulation Clause</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${insRepDto.ncRegulation}" var="ncRegulations" varStatus="status">
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
                </table>
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
                        </td>
                    </tr>
                    <tr>
                        <td class="col-xs-4">
                            <p>Remarks:</p>
                        </td>
                        <div>
                            <td class="col-xs-4">
                                <p>${appPremisesRecommendationDto.remarks}</p>
                            </td>
                        </div>
                        <td class="col-xs-4">
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="alert alert-info" role="alert">
            <strong>
                <h4>Section E (Recommendations) Not Applicable for Post Licensing Inspection</h4>
            </strong>
        </div>
    <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table class="table">
                        <tr>
                            <td class="col-xs-4">
                                <p>Recommendation:</p>
                            </td>
                            <td class="col-xs-4">
                                <p>${option}</p>
                                <input type="hidden" name="inspectorRecommendation" value="${option}">
                                <iais:select name="recommendation" options="riskOption" firstOption="Please select"
                                             onchange="x(this)" value="appPremisesRecommendationDto.recommendation"/>
                                <input type="text" name="otherRecommendation" id="recom" value=""
                                       style="display: none"/>
                                <span id="error_recommendation" name="iaisErrorMsg" class="error-msg"></span>
                            </td>
                            <td class="col-xs-4"></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button id="backButton" type="submit" onclick="doBack()" class="btn btn-primary">Back</button>
        <button id="approveButton" type="submit" class="btn btn-primary" onclick="doSubmit()">Approve</button>
    </div>
</form>
    <%@include file="/include/validation.jsp"%>
</div>
<script type="text/javascript">
    function doBack() {
        submit('back');
    }

    function doSubmit() {
        submit('approve');
    }

    function submit(action) {
        $("[name='action_type']").val(action);
        var mainPoolForm = document.getElementById('aomainForm');
        mainPoolForm.submit();

    }

    function x(obj) {
        if (obj.options[obj.selectedIndex].value == "Others")
            document.getElementById("recom").style.display = "";
        else
            document.getElementById("recom").style.display = "none";

    }
</script>

