<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="switch_value" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">

                            <div class="tab-pane active" id="paymentTab" role="tabpanel">
                                <br/>
                                <ul class="progress-tracker" style="margin-top: 50px">
                                    <li class="tracker-item active disabled">Instructions</li>
                                    <li class="tracker-item active disabled">Licence Review</li>
                                    <li class="tracker-item active">Payment</li>
                                    <li class="tracker-item disabled">Acknowledgement</li>
                                </ul>
                                <h2 style="margin-top: 20px;">Payment Summary</h2>
                                <p >
                                    Total amount due:
                                    <c:out value="${totalStr}"></c:out>
                                </p>
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Service</th>
                                        <th>Late Fee Type</th>
                                        <th>Application Type</th>
                                        <th>Application No.</th>
                                        <th>Late Fee Amount</th>
                                        <th>Amount</th>
<%--                                        <th>Amount</th>--%>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}"  varStatus="status">
                                        <c:set var="detailFeeDto" value="${AppSubmissionDto.detailFeeDto}"/>
                                        <c:forEach var="svc" items="${AppSubmissionDto.appSvcRelatedInfoDtoList}">
                                            <tr>
                                                <td>
                                                    <p><c:out value="${svc.serviceName}"></c:out></p>
                                                </td>
                                                <td>
                                                    <p>${detailFeeDto.lateFeeType}</p>
                                                </td>
                                                <td>
                                                    <p>Renewal</p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${AppSubmissionDto.appGrpNo}-${(status.index+1) > 9 ? '' : '0'}${status.index + 1}"></c:out></p>
                                                </td>
                                                <td>
                                                    <p>${detailFeeDto.lateFeeAmoumtStr}</p>
                                                </td>
                                                <td>
                                                    <p>${svc.amount}</p>
                                                </td>
<%--                                                <td>--%>
<%--                                                    <p><c:out value="${AppSubmissionDto.amountStr}"></c:out></p>--%>
<%--                                                </td>--%>
                                            </tr>
                                        </c:forEach>
                                    </c:forEach>

                                    </tbody>
                                </table>
<%--                                <h2>Payment Method</h2>--%>
<%--                                <input class="form-check-input premTypeRadio"  type="radio" checked name="payMethod" value="Credit">--%>
<%--                                <label class="form-check-label" ><span class="check-circle"></span>Credit/Debit Card</label>&nbsp&nbsp&nbsp&nbsp--%>
<%--                                <span name="iaisErrorMsg" id="error_payMethod" class="error-msg"></span>--%>
<%--                                <br>--%>

<%--                                &nbsp&nbsp&nbsp&nbsp<img src="<%=webroot1%>img/mastercard.png" width="40" height="25" alt="mastercard">&nbsp--%>
<%--                                <img src="<%=webroot1%>img/paymentVISA.png" width="66" height="25" alt="VISA">--%>
                                <%@include file="../newApplication/paymentMethod.jsp"%>
                            </div>

<%--                            <div class="application-tab-footer">--%>
<%--                                <div class="row">--%>
<%--                                    <div class="col-xs-12 col-sm-6">--%>
<%--                                        <p><a id="BACK" class="back"><em class="fa fa-angle-left"></em> Back</a></p>--%>
<%--                                    </div>--%>
<%--                                    <div class="col-xs-12 col-sm-6">--%>
<%--                                        <div id="proceed" class="text-right text-center-mobile"><a class="btn btn-primary">Proceed</a></div>--%>
<%--                                    </div>--%>
<%--                                </div>--%>
<%--                            </div>--%>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>

<script>
    $('#previewAndSub').click(function () {
        doSubmitForm('prePayment','', '');
    });

    $('#BACK').click(function () {
        $('[name="switch_value"]').val('licenceReview');
        $('#menuListForm').submit();
    });

    $('#proceed').click(function () {
        $('[name="switch_value"]').val('doPayment');
        $('#menuListForm').submit();
    });

</script>