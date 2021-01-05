<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<style>
    .margin-bottom-10{
        margin-bottom:10px;
    }
    .aMarginleft{
        margin-left: 9px;
    }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type"  value=""/>
    <input type="hidden" name="appGroupId" value="<iais:mask name="appGroupId" value="${AppSubmissionDto.appGrpId}"/>"/>
    <input type="hidden" name="selfDeclAction" value="new"/>
    <div class="main-content">
        <div class="container">
            <div class="row center">
                <c:choose>
                    <c:when test="${'error' != AckStatus}">
                        <div class="col-xs-12">
                            <br/>
                            <p class="ack-font-20"><strong>Submission successful</strong></p>
                        </div>

                        <c:forEach items="${hcsaServiceDtoList}" var="list">
                            <div class="col-xs-12">
                                <p class="ack-font-20">- <strong><c:out value="${list.svcName}"/> </strong></p>
                            </div>
                        </c:forEach>
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
                                        <th>Application No.</th>
                                        <%--<c:if test="${'Credit'==AppSubmissionDto.paymentMethod or 'NETS'==AppSubmissionDto.paymentMethod}">
                                            <th>Transactional No.</th>
                                        </c:if>--%>
                                        <c:if test="${requestInformationConfig == null}">
                                            <th>Transactional No.</th>
                                        </c:if>
                                        <th>Date & Time</th>
                                        <th>Amount Deducted</th>
                                        <th>Payment Method</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${'APTY005' ==AppSubmissionDto.appType}">
                                            <c:forEach var="ackPageAppSubmission" items="${ackPageAppSubmissionDto}">
                                                <tr>
                                                    <td><c:out value="${ackPageAppSubmission.appGrpNo}"/></td>
                                                    <%--<c:if test="${'Credit'==AppSubmissionDto.paymentMethod or 'NETS'==AppSubmissionDto.paymentMethod}">
                                                        <td><c:out value="${txnRefNo}"/></td>
                                                    </c:if>--%>
                                                    <c:if test="${requestInformationConfig == null}">
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
                                                    </c:if>
                                                    <td><c:out value="${txnDt}"/></td>
                                                    <td><c:if test="${ackPageAppSubmission.amount==null}">N/A</c:if>
                                                        <c:if test="${ackPageAppSubmission.amount!=null}">
                                                            <c:out value="${ackPageAppSubmission.amountStr}"/>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${ackPageAppSubmission.paymentMethod==null}">
                                                                N/A
                                                            </c:when>
                                                            <c:otherwise>
                                                                <iais:code code="${ackPageAppSubmission.paymentMethod}"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td><c:out value="${AppSubmissionDto.appGrpNo}"/></td>
                                                <%--<c:if test="${'Credit'==AppSubmissionDto.paymentMethod or 'NETS'==AppSubmissionDto.paymentMethod}">
                                                    <td><c:out value="${txnRefNo}"/></td>
                                                </c:if>--%>
                                                <c:if test="${requestInformationConfig == null}">
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
                                                </c:if>
                                                <td><c:out value="${txnDt}"/></td>
                                                <td><c:if test="${AppSubmissionDto.amountStr==null}">N/A</c:if>
                                                    <c:if test="${AppSubmissionDto.amountStr!=null}">
                                                        <c:out value="${AppSubmissionDto.amountStr}"/>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${AppSubmissionDto.paymentMethod==null}">
                                                            N/A
                                                        </c:when>
                                                        <c:otherwise>
                                                            <iais:code code="${AppSubmissionDto.paymentMethod}"/>

                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
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
            <div class="row margin-bottom-10 text-right">
                <div class="col-xs-12 col-md-1">
                    <c:if test="${requestInformationConfig == null && 'error' != AckStatus}">
                        <p class="print"><a href="#" id="print-ack"> <em class="fa fa-print"></em>Print</a></p>
                    </c:if>
                </div>
                <div class="col-xs-11 col-md-11">
                    <c:choose>
                    <c:when test="${requestInformationConfig == null && 'error' != AckStatus}">
                        <a class="btn btn-primary aMarginleft col-md-2 pull-right" id="toDashBoard" href="javascript:void(0);">Go to <br>Dashboard</a>
                        <c:if test="${AppSubmissionDto.appType!='APTY005'}">
                            <a class="btn btn-secondary aMarginleft col-md-3 pull-right" href="javascript:void(0);" id="toChooseSvc" >Apply for <br>Another Licence</a>
                            <a class="btn btn-secondary aMarginleft col-md-3 pull-right" id="doSelfAssessment" href="javascript:void(0);">Submit <br>Self-Assessment</a>
                            <a class="btn btn-secondary aMarginleft col-md-3 pull-right" id="doPrefInsDate" href="javascript:void(0);">Indicate preferred<br>Inspection Date</a>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-primary aMarginleft col-md-2 pull-right" id="toDashBoard" href="javascript:void(0);">Go to <br>Dashboard</a>
                    </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">

    $(document).ready(function () {
        $('#newSvc').before('<br/><br/>');
        $('#dashboard').css('padding-bottom','0px');

    });

    $('#doSelfAssessment').click(function () {
        showWaiting();
        $("[name='crud_action_type']").val('MohAppPremSelfDecl');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    $('#doPrefInsDate').click(function () {
        showWaiting();
        $("[name='crud_action_type']").val('MohSubmitInspectionDate');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    $('#toDashBoard').click(function () {
        showWaiting();
        $("[name='crud_action_type']").val('DashBoard');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    $('#toChooseSvc').click(function () {
        showWaiting();
        $("[name='crud_action_type']").val('ChooseSvc');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });

    $("#print-ack").click(function () {
        window.print();
    })
</script>



