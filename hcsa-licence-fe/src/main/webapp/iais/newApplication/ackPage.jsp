<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<style>

</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type"  value=""/>
    <div class="main-content">
        <div class="container">
            <div class="row center">
                <c:choose>
                    <c:when test="${'error' != AckMessage}">
                        <div class="col-xs-12">
                            <p class="ack-font-20"><strong>Submission successful</strong></p>
                        </div>

                        <c:forEach items="${hcsaServiceDtoList}" var="list">
                            <div class="col-xs-12">
                                <p class="ack-font-20">-<strong><c:out value="${list.svcName}"/> </strong></p>
                            </div>
                        </c:forEach>
                        <div class="ack-font-16">
                        <div class="col-xs-12">
                            A confirmation email will be sent to XXXXX.
                            <br/>
                            <br/>
                        </div>
                        <div class="col-xs-12">
                            We will review apllication and notify you if any change are required.
                        </div>
                        <div class="col-xs-12">
                            An in inspection date will be arranged if necessary.
                            <br/>
                            <br/>
                        </div>
                        <div class="col-xs-12">
                            Transactional details:
                        </div>
                        <div class="col-xs-12">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>Application No.</th>
                                    <c:if test="${'Credit'==AppSubmissionDto.paymentMethod}">
                                        <th>Transactional No.</th>
                                    </c:if>
                                    <th>Date & Time</th>
                                    <th>Amount Deducted</th>
                                    <th>Payment Method</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td><c:out value="${AppSubmissionDto.appGrpNo}"/></td>
                                    <c:if test="${'Credit'==AppSubmissionDto.paymentMethod}">
                                        <td><c:out value="${txnRefNo}"/></td>
                                    </c:if>
                                    <td><c:out value="${txnDt}"/></td>
                                    <td><c:out value="${AppSubmissionDto.amountStr}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${'Credit'==AppSubmissionDto.paymentMethod}">
                                                Credit Card
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${AppSubmissionDto.paymentMethod}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h3>You have encountered some problems, please contact the administrator !!!</h3>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="row right align">
                <%--<a class="btn btn-secondary" href="#">Indicate preferred Inspection Date</a>
                <a class="btn btn-secondary" href="#">Indicate preferred Inspection Date</a>--%>
                <div class="col-xs-12 col-md-2">
                    <a>Print</a>
                </div>
                <div class="col-xs-12 col-md-4">
                    <a class="btn btn-secondary" href="#">Indicate preferred Inspection Date</a>
                </div>
                <div class="col-xs-12 col-md-3">
                    <a class="btn btn-secondary" id="doSelfAssessment">Submit Self-Assessment</a>
                </div>
                <div class="col-xs-12 col-md-3">
                    <a class="btn btn-secondary" href="/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu">Apply for Another Licence</a>
                </div>
                <div class="col-xs-12 col-md-2">
                    <a class="btn btn-primary" href="/main-web/eservice/INTERNET/MohInternetInbox" >Go to Dashboard</a>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">

    $('#doSelfAssessment').click(function () {
        $("[name='crud_action_type']").val('MohAppPremSelfDecl');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });
</script>



