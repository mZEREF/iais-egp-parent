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
                            <p>-<c:out value="${appSubmissionDto.appSvcRelatedInfoDtoList[0].serviceName}"></c:out></p>
                        </c:forEach>
                        <p>A confirmation email will be sent to XXXXX.</p>
                        <p>
                            We will review your application and notify you if any changes are required.
                            An inspection date will be arranged if necessary.</p>
                        <p>Transactional details:</p>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Transactional No.</th>
                                <th>Date & Time</th>
                                <th>Amount Deducted</th>
                                <th>Payment Method</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <th>${pmtRefNo}</th>
                                <th><fmt:formatDate value="${createDate}" pattern="dd/MM/yyyy"></fmt:formatDate></th>
                                <th>${dAmount}</th>
                                <th>${payMethod}</th>
                            </tr>
                            </tbody>
                        </table>
                        <div class="col-xs-12 col-sm-12">
                            <div class="button-group col-xs-12 col-sm-6">
                                <a class="btn btn-secondary next" id="Acknowledgement">Print this Acknowledgement</a>
                            </div>
                            <div class="button-group col-xs-12 col-sm-6 ">
                                <a class="btn btn-secondary next" id="GotoDashboard" >Go to Dashboard</a>
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