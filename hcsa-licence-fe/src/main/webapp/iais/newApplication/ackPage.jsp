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
    <div class="main-content">
        <div class="container">
            <div class="row center">
                <c:choose>
                    <c:when test="${'error' != AckMessage}">
                        <div class="col-xs-12">
                            <p class="ack-font-20"><strong>Submission successful</strong></p>
                        </div>

                        <c:forEach items="${hcsaServiceDtoList}" var="list">
                            <c:if test ="${list.svcCode==currentSvcCode}">
                            <div class="col-xs-12">
                                <p class="ack-font-20">-<strong><c:out value="${list.svcName}"/> </strong></p>
                            </div>
                            </c:if>
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
                        <div class="col-xs-12">
                            <a>Print</a>
                            <button type="button" style="background-color: #1F92FF"><a href="#" style="color:white">Indicate preferred Inspection Date</a></button>
                            <button type="button" style="background-color: #1F92FF"><a href="#" style="color:white">Submit Self-Assessment</a></button>
                            <button type="button" style="background-color: #1F92FF"><a href="/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu" style="color:white">Apply for Another Licence</a></button>
                            <button type="button" style="background-color: #1F92FF"><a href="/main-web/eservice/INTERNET/MohInternetInbox" style="color:white">Go to Dashboard</a></button>

                        </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h3>You have encountered some problems, please contact the administrator !!!</h3>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">


</script>



