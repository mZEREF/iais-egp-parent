<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<br/><br/>
<%--@elvariable id="submissionDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo"--%>
<div class="panel panel-default">
    <div class="panel-heading" style="text-align: center"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td class="col-xs-6" style="font-weight: bold; text-align: right">Application No.</td>
                        <td class="col-xs-6" style="padding-left : 20px"><c:out value="${submissionDetailsInfo.applicationNo}"/></td>
                    </tr>
                    <tr>
                        <td style="font-weight: bold; text-align: right">Application Type</td>
                        <td style="padding-left : 20px"><iais:code code="${submissionDetailsInfo.applicationType}"/></td>
                    </tr>
                    <tr>
                        <td style="font-weight: bold; text-align: right">Application Sub-Type</td>
                        <td style="padding-left : 20px"><iais:code code="${submissionDetailsInfo.applicationSubType}"/></td>
                    </tr>
                    <tr>
                        <td style="font-weight: bold; text-align: right">SubmissionType</td>
                        <c:choose>
                            <c:when test="${submissionDetailsInfo.submissionType ne null && submissionDetailsInfo.submissionType ne ''}">
                                <td style="padding-left : 20px"><iais:code code="${submissionDetailsInfo.submissionType}"/></td>
                            </c:when>
                            <c:otherwise>
                                <td style="padding-left : 20px">N/A</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                    <tr>
                        <td style="font-weight: bold; text-align: right">Application Status</td>
                        <td style="padding-left : 20px"><iais:code code="${submissionDetailsInfo.applicationStatus}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<%--@elvariable id="appId" type="java.lang.String"--%>
<c:set var="maskedAppId"><iais:mask name="appId" value="${appId}"/></c:set>
<div style="text-align: center">
    <a href="javascript:void(0);" onclick="viewApplication()">
        <button type="button" class="btn btn-primary">
            View Application
        </button>
    </a>
    <%--perInspection--%>
    <c:if test="${not empty selfAssessmentAvailable}">
        <button id="viewSelfAssessmt" type="button" class="btn btn-primary" href="javascript:void(0);" onclick="viewPreInspectionChecklist('${maskedAppId}')" <c:if test="${selfAssessmentAvailable eq false}">disabled</c:if>>
            Pre-Inspection Checklist
        </button>
    </c:if>
</div>
<c:choose>
    <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
    <c:when test="${goBackUrl ne null}">
        <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:when>
    <c:otherwise>
        <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:otherwise>
</c:choose>