<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@include file="dashboard.jsp" %>
<%@include file="../common/dashboard.jsp" %>
<style>
    .ack-font-14{
        font-size: 14px;
    }
    .margin-bottom-10{
        margin-bottom:10px;
    }
</style>
<br/>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form_value" value="">
    <input type="hidden" name="crud_action_type_value" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <c:choose>
                    <c:when test="${AckMessage != null}">
                        ${AckMessage}
                    </c:when>
                    <c:otherwise>
                        <label style="font-size: 20px">Submission successful</label>
                        <c:forEach items="${appSubmissionDtos}" var="appSubmissionDto">
                            <p>-<strong><c:out value="${appSubmissionDto.appSvcRelatedInfoDtoList[0].serviceName}"></c:out></strong></p>
                        </c:forEach>
                        <p class="ack-font-14">A confirmation email will be sent to ${emailAddress}.</p>
                        <p class="ack-font-14"><iais:message key="NEW_ACK005" escape="false"></iais:message></p>
                        <c:if test="${dAmount!='$0.0'}">
                            <p class="ack-font-14">Transactional details:</p>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>Application No.</th>
                                    <c:if test="${'Credit'== payMethod}">
                                        <th>Transactional No.</th>
                                    </c:if>
                                    <th>Date & Time</th>
                                    <th>Amount Deducted</th>
                                    <th>Payment Method</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>${appSubmissionDtos.get(0).appGrpNo}</td>
                                    <c:if test="${'Credit'== payMethod}">
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty pmtRefNo}">N/A</c:when>
                                                <c:otherwise> ${pmtRefNo}</c:otherwise>
                                            </c:choose>
                                        </td>
                                    </c:if>
                                    <td><fmt:formatDate value="${createDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                    <td>${dAmount}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty payMethod}">N/A</c:when>
                                            <c:when test="${'Credit'== payMethod}">Credit Card</c:when>
                                            <c:otherwise> ${payMethod}</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </c:if>

                        <div class="col-xs-12 col-sm-12 margin-bottom-10">
                            <div class="button-group col-xs-12 col-sm-6">
                                <a class="btn btn-secondary next" id="Acknowledgement">Print this Acknowledgement</a>
                            </div>
                            <div class="button-group col-xs-12 col-sm-6 ">
                                <a class="btn btn-primary aMarginleft col-md-6 pull-right" id="GotoDashboard" >Go to Dashboard</a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</form>


<script>
$('#Acknowledgement').click(function () {
 window.print();
});
$('#GotoDashboard').click(function () {
    SOP.Crud.cfxSubmit("menuListForm", "dashboard");
});
</script>