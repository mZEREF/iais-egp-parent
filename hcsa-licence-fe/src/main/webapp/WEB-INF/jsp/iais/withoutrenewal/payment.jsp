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
    <input type="hidden" id="paymentMessageValidateMessage" value="${paymentMessageValidateMessage}">
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
<%--                                    <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}"  varStatus="status">--%>
<%--                                        <c:set var="detailFeeDto" value="${AppSubmissionDto.detailFeeDto}"/>--%>
<%--                                            <tr>--%>
<%--                                                <td>--%>
<%--                                                    <p><c:out value="${AppSubmissionDto.serviceName}"></c:out></p>--%>
<%--                                                </td>--%>
<%--                                                <td>--%>
<%--                                                    Renewal--%>
<%--                                                </td>--%>
<%--                                                <td>--%>
<%--                                                    <p>${AppSubmissionDto.appGrpNo}</p>--%>
<%--                                                </td>--%>
<%--                                                <td>--%>
<%--                                                    <p>${AppSubmissionDto.amountStr}</p>--%>
<%--                                                </td>--%>
<%--                                            </tr>--%>
<%--                                    </c:forEach>--%>
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
                                    <c:forEach items="${laterFeeDetailsMap}" var="laterFeeDetailMap">
                                        <c:set var="laterFeeType" value='${laterFeeDetailMap.key}' />
                                        <tr>
                                            <td colspan="4">
                                                <p>${laterFeeType}</p>
                                                <p><em>Applicable to the following licence(s):</em></p>
                                            </td>
                                        </tr>
                                        <c:forEach items="${laterFeeDetailMap.value}" var="laterFeeDetail">
                                            <tr>
                                                <td style="border-top: none;">
                                                    <p>-&nbsp;${laterFeeDetail.svcNames.get(0)}</p>
                                                </td>
                                                <td style="border-top: none;">N/A</td>
                                                <td style="border-top: none;">N/A</td>
                                                <td style="border-top: none;"><p>${laterFeeDetail.lateFeeAmoumtStr}</p></td>
                                            </tr>
                                        </c:forEach>
                                    </c:forEach>

                                    <c:if test="${!empty gradualFeeList}">
                                        <p><em>To facilitate the transition from PHMCA to HCSA, the following licence(s) in your application are eligible for fee reductionin this transition period:</em></p>

                                        <c:forEach items="${gradualFeeList}" var="gradualFee">
                                            <tr>
                                                <td style="border-top: none;">
                                                    <p><u>Fees for eliqible HCSA licences</u></p>
                                                </td>
                                                <td style="border-top: none;"></td>
                                                <td style="border-top: none;"></td>
                                                <td style="border-top: none;"></td>
                                            </tr>
                                            <tr>
                                                <td style="border-top: none;">
                                                    <c:forEach var="serviceName" items="${gradualFee.svcNames}">
                                                        <p>${serviceName}</p>
                                                    </c:forEach>
                                                </td>
                                                <td style="border-top: none;">Renewal</td>
                                                <td style="border-top: none;">${gradualFee.appGroupNo}</td>
                                                <td style="border-top: none;"><p>${gradualFee.gradualAndOldStr}</p></td>
                                            </tr>

                                            <tr>
                                                <td>
                                                    <p><u>Previous Fees under PHMCA</u></p>
                                                </td>
                                                <td style="border-top: none;"></td>
                                                <td style="border-top: none;"></td>
                                                <td style="border-top: none;"><p>${gradualFee.oldAmountStr}</p></td>
                                            </tr>

                                            <tr>
                                                <td style="border-top: none;">
                                                    <p><u>Total Increase in Fees</u></p>
                                                </td>
                                                <td style="border-top: none;"></td>
                                                <td style="border-top: none;"></td>
                                                <td style="border-top: none;"><p>${gradualFee.amountStr}</p></td>
                                            </tr>

                                            <tr>
                                                <td style="border-top: none;">
                                                    <p><u>${gradualFee.reduction}</u></p>
                                                </td>
                                                <td style="border-top: none;"></td>
                                                <td style="border-top: none;"></td>
                                                <td style="border-top: none;"><p>${gradualFee.gradualSpreadStr}</p></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>

                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td><p>Total amount due:</p></td>
                                        <td><p><strong><c:out value="${totalStr}"></c:out></strong></p></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <c:choose>
                                    <c:when test="${totalStr=='$0'}">
                                        <input type="hidden" value="${totalStr}" id="renewNoNeedPay" name="renewNoNeedPay">
                                        <%@include file="../newApplication/noNeedPayment.jsp.jsp"%>
                                    </c:when>
                                    <c:otherwise>
                                        <%@include file="../newApplication/paymentMethod.jsp"%>
                                    </c:otherwise>
                                </c:choose>
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
        var val = $('#renewNoNeedPay').val();
        if('$0'==val){
            $('[name="switch_value"]').val('doAcknowledgement');
            $('#menuListForm').submit();
        }else{
            if(validatePayment()){
                $('[name="switch_value"]').val('doPayment');
                $('#menuListForm').submit();
            }
        }

    });

    function validatePayment(){
        var flag=false;
        var paymentMessageValidateMessage = $('#paymentMessageValidateMessage').val();
        if($("input[name='payMethod']").size()<=0){
            flag=true;
        }else {
            $("input[name='payMethod']").each(function () {
                if ( $(this).prop("checked")){
                    flag=true;
                }
            });
        }
        if(!flag){
            $('#error_payMethod').html(paymentMessageValidateMessage);
        }
        return flag;
    }

    $("[name='payMethod']").change(function selectChange() {
        $("input[name='payMethod']").each(function () {
            if ( $(this).prop("checked")){
                $('#error_payMethod').html('');
            }
        });
    });

</script>