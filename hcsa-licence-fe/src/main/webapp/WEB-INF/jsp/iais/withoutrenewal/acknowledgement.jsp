<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<style>
    .margin-bottom-10{
        margin-bottom:10px;
    }
    .aMarginleft{
        margin-left: 9px;
    }
</style>
<form method="post" class="table-responsive" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type"  value=""/>
    <div class="main-content">
        <div class="container">
            <div class="row">

                <div class="col-xs-12">
                    <div class="center-content">
                        <div class="licence-renewal-content">
                            <ul class="progress-tracker">
                                <li class="tracker-item active">Instructions</li>
                                <li class="tracker-item active">Licence Review</li>
                                <li class="tracker-item active">Payment</li>
                                <li class="tracker-item active">Acknowledgement</li>
                            </ul>

                            <c:choose>
                                <c:when test="${'error' != AckStatus}">
                                    <div class="col-xs-12">
                                        <p class="ack-font-20"><strong>Submission successful</strong></p>
                                    </div>

                                    <c:forEach items="${serviceNamesAck}" var="serviceName">
                                        <div class="col-xs-12">
                                            <p class="ack-font-20">- <strong><c:out value="${serviceName}"/> </strong></p>
                                        </div>
                                    </c:forEach>
                                    <br/>
                                    <div class="ack-font-16">
                                        <div class="col-xs-12">
                                            A confirmation email will be sent to ${emailAddress}.
                                            <br/>
                                            <br/>
                                        </div>
                                        <div class="col-xs-12">
                                            <iais:message key="NEW_ACK005" escape="false"></iais:message>
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
                                                    <%--<th>Application No.</th>--%>
                                                    <%--<c:if test="${'Credit' == renewDto.appSubmissionDtos.get(0).paymentMethod or 'NETS'==renewDto.appSubmissionDtos.get(0).paymentMethod}">
                                                        <th>Transactional No.</th>
                                                    </c:if>--%>
                                                    <th>Transactional No.</th>
                                                    <th>Date & Time</th>
                                                    <th>Amount Deducted</th>
                                                    <th>Payment Method</th>
                                                </tr>
                                                </thead>
                                                <tbody>
            <%--                                        <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}">--%>
                                                        <c:set var="AppSubmissionDto" value="${renewDto.appSubmissionDtos.get(0)}" scope="request"/>
                                                        <tr>
                                                            <%--<td><c:out value="${AppSubmissionDto.appGrpNo}"/></td>--%>
                                                            <%--<c:if test="${'Credit'== renewDto.appSubmissionDtos.get(0).paymentMethod or 'NETS'== renewDto.appSubmissionDtos.get(0).paymentMethod}">
                                                                <td><c:out value="${txnRefNo}"/></td>
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
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${empty txnDt}">
                                                                        N/A
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:out value="${txnDt}"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                             </td>
                                                            <td><c:out value="${totalStr}"/></td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${empty AppSubmissionDto.paymentMethod}">
                                                                        N/A
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <iais:code code="${AppSubmissionDto.paymentMethod}"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
            <%--                                        </c:forEach>--%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <h3>${AckMessage}</h3>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>


            <div class="row margin-bottom-10 text-right">
                <div class="col-xs-12 col-md-1">
                    <p class="print"><a href="#" id="print-ack"> <em class="fa fa-print"></em>Print</a></p>
                </div>
                <div class="col-xs-11 col-md-11">

                    <c:if test="${requestInformationConfig == null}">
                        <a class="btn btn-primary aMarginleft col-md-2 pull-right" href="/main-web/eservice/INTERNET/MohInternetInbox" >Go to <br>Dashboard</a>
                        <%--<a class="btn btn-secondary aMarginleft col-md-3 pull-right" href="/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu">Apply for <br>Another Licence</a>--%>
                        <%--<a class="btn btn-secondary aMarginleft col-md-3 pull-right" id="doSelfAssessment">Submit <br>Self-Assessment</a>--%>
                    </c:if>
                        <%--<a class="btn btn-secondary aMarginleft col-md-3 pull-right" id="doPrefInsDate">Indicate preferred<br>Inspection Date</a>--%>
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

    // $('#doPrefInsDate').click(function () {
    //     $("[name='crud_action_type']").val('MohSubmitInspectionDate');
    //     var mainForm = document.getElementById("mainForm");
    //     mainForm.submit();
    // });

    $("#print-ack").click(function () {
        window.print();
    })
</script>



