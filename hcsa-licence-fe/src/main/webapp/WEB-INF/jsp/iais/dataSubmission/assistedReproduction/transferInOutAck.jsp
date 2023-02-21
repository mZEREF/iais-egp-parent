<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<style>
    .margin-btm {
        margin-bottom: 30px;
    }

    .ack-font-16 {
        font-size: 16px;
    }
</style>

<%-- current page: ack --%>

<%@ include file="common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="common/formHidden.jsp" %>
    <div class="main-content">
        <div class="container center-content">
            <div style="margin-top:-30px;">
                <div class="col-xs-12 text-right">
                    <%@include file="common/headStepNavTab.jsp" %>

                    <p class="print ack-font-16">
                        <label class="fa fa-print" style="color: #147aab;" onclick="printData()"></label> <a onclick="printData()" href="javascript:void(0);">Print</a>
                    </p>
                </div>
                <label class="col-xs-12" style="font-size: 20px">Submission Successful</label>
                <p class="col-xs-12 margin-btm">-<strong>
                    <%--<iais:code code="${arSuperDataSubmissionDto.submissionType}"/>--%>
                        Submit Transfer In/Out Information
                </strong></p>
                <div class="ack-font-16">
                    <p class="col-xs-12">A notification email will be sent to ${emailAddress}.</p>
                    <p class="col-xs-12 margin-btm"><iais:message key="DS_MSG004" escape="false"></iais:message></p>
                </div>
                <div class="ack-font-16">
                    <p class="col-xs-12">Submission details:</p>
                    <div class="col-xs-12 col-sm-12 margin-btm table-responsive">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" >Submission ID</th>
                                <th scope="col" >Submitted By</th>
                                <th scope="col" >Submission Date and Time</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${not empty arSuperList}" var="hasArSuperList">
                                <c:forEach var="arSuper" items="${arSuperList}">
                                <tr>
                                    <td>${arSuper.dataSubmissionDto.submissionNo}</td>
                                    <td>${submittedBy}</td>
                                    <td><fmt:formatDate value="${arSuper.dataSubmissionDto.submitDt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${!hasArSuperList}">
                                <tr>
                                    <td>${arSuperDataSubmissionDto.dataSubmissionDto.submissionNo}</td>
                                    <td>${submittedBy}</td>
                                    <td><fmt:formatDate value="${arSuperDataSubmissionDto.dataSubmissionDto.submitDt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="col-xs-12 col-md-2 text-left">
                    <a style="padding-left: 5px;" class="back" href="/main-web/eservice/INTERNET/MohDataSubmissionsInbox">
                        <em class="fa fa-angle-left">&nbsp;</em> Back
                    </a>
                </div>
                <div class="col-xs-12 col-md-10 margin-bottom-10">
                    <div class="text-right">
                        <a class="btn btn-secondary" href="/hcsa-licence-web/eservice/INTERNET/MohARAndIUIDataSubmission">Start Another Submission</a>
                        <a class="btn btn-primary" href="/main-web/eservice/INTERNET/MohInternetInbox">Go to DashBoard</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
