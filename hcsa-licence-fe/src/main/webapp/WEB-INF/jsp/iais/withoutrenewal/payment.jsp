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
<form method="post" class="table-responsive" id="menuListForm" action=<%=process.runtime.continueURL()%>>
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
                                <%@include file="../common/renewPayment.jsp"%>
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
        showWaiting();
        $('[name="switch_value"]').val('paymentBack');
        $('#menuListForm').submit();
    });

    $('#proceed').click(function () {
        showWaiting();
        var val = $('#renewNoNeedPay').val();
        if('$0'==val){
            $('[name="switch_value"]').val('doAcknowledgement');
            $('#menuListForm').submit();
        }else{
            if(validatePayment()){
                $('[name="switch_value"]').val('doPayment');
                $('#menuListForm').submit();
            }else {
                dismissWaiting();
            }
        }

    });

    function validatePayment(){
        var flag=false;
        var paymentMessageValidateMessage = $('#paymentMessageValidateMessage').val();
        if($("input[name='payMethod']").length<=0){
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