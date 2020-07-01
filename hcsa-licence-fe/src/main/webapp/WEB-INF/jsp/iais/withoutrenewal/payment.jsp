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
                                    <li class="tracker-item active">Instructions</li>
                                    <li class="tracker-item active">Licence Review</li>
                                    <li class="tracker-item active">Payment</li>
                                    <li class="tracker-item disabled">Acknowledgement</li>
                                </ul>
                                <h2 style="margin-top: 20px; border-bottom: none;">Payment Summary</h2>
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Service</th>
                                        <th>Application Type</th>
                                        <th>Application No.</th>
                                        <th>Amount</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}"  varStatus="status">
                                        <c:set var="detailFeeDto" value="${AppSubmissionDto.detailFeeDto}"/>
                                            <tr>
                                                <td>
                                                    <p><c:out value="${AppSubmissionDto.serviceName}"></c:out></p>
                                                </td>
                                                <td>
                                                    Renewal
                                                </td>
                                                <td>
                                                    <p>${AppSubmissionDto.appGrpNo}</p>
                                                </td>
                                                <td>
                                                    <p>${AppSubmissionDto.amountStr}</p>
                                                </td>
                                            </tr>
                                    </c:forEach>
                                    <c:forEach items="${rfcAppSubmissionDtos}" var="rfcAppSubmissionDto">
                                        <tr>
                                            <td>
                                                <p><c:out value="${rfcAppSubmissionDto.serviceName}"></c:out></p>
                                            </td>
                                            <td>
                                                Amendment
                                            </td>
                                            <td>
                                                <p>${rfcAppSubmissionDto.appGrpNo}</p>
                                            </td>
                                            <td>
                                                <p>${rfcAppSubmissionDto.amountStr}</p>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}"  varStatus="status">
                                        <c:set var="detailFeeDto" value="${AppSubmissionDto.detailFeeDto}"/>
                                        <c:if test="${detailFeeDto != null && detailFeeDto.lateFeeType != '' && detailFeeDto.lateFeeAmoumtStr != '' && detailFeeDto.lateFeeType != null && detailFeeDto.lateFeeAmoumtStr != null}">
                                            <c:forEach var="svc" items="${AppSubmissionDto.appSvcRelatedInfoDtoList}">
                                                <tr>
                                                    <td>
                                                        <p>${detailFeeDto.lateFeeType}</p>
                                                        <p>Applicable to the following licence(s):</p>
                                                        <p><c:out value="${svc.serviceName}"></c:out></p>
                                                    </td>
                                                    <td>NA</td>
                                                    <td>NA</td>
                                                    <td><p>${detailFeeDto.lateFeeAmoumtStr}</p></td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                    </c:forEach>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td>Total amount due:</td>
                                        <td><p><strong><c:out value="${totalStr}"></c:out></strong></p></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <%@include file="../newApplication/paymentMethod.jsp"%>
                            </div>
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