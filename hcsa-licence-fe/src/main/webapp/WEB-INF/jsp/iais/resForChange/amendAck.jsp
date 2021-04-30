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
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
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
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Application No.</th>
                                            <%--<c:if test="${'Credit'== payMethod or 'NETS'== payMethod}">
                                                <th>Transactional No.</th>
                                            </c:if>--%>
                                        <th>Transactional No.</th>
                                        <th>Date & Time</th>
                                        <th>Amount Deducted</th>
                                        <th>Payment Method</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>${appSubmissionDtos.get(0).appGrpNo}</td>
                                            <%--<c:if test="${'Credit'== payMethod or 'NETS'== payMethod}">
                                                <td><c:out value="${pmtRefNo}"/></td>
                                            </c:if>--%>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty txnRefNo}">
                                                    N/A
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${txnRefNo}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><fmt:formatDate value="${createDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                        <td>${dAmount}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty payMethod}">N/A</c:when>
                                                <c:otherwise> <iais:code code="${payMethod}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>

                        <div class="col-xs-12 col-sm-12 margin-bottom-10">
                            <div class="button-group col-xs-12 col-sm-6">
                                <p class="print"><div style="font-size: 16px;"><a id="Acknowledgement" href="${pageContext.request.contextPath}/rfc-app-ack-print"> <em class="fa fa-print"></em>Print</a></div></p>
                            </div>
                            <div class="button-group col-xs-12 col-sm-6 ">
                                <a class="btn btn-primary aMarginleft col-md-6 pull-right" id="GotoDashboard" href="javascript:void(0);">Go to Dashboard</a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</form>


<script>
<%--$('#Acknowledgement').click(function () {
 window.print();
});--%>
$('#GotoDashboard').click(function () {
    Utils.submit('menuListForm','dashboard','','','');
});
</script>