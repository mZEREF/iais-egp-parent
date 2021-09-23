<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<h2>Payment Method</h2>
<input type="hidden" name="reloadPayMethod" value="${reloadPaymentMethod}"/>
<div class="col-xs-12">
    <div class="col-md-4 col-xs-12" style="margin-bottom: 20px;">
        <input id="Mastercard" class=" premTypeRadio paymentInput"  type="radio" name="payMethod" value="PMDE002">
        <label class="form-check-label" for="Mastercard" ><span class="check-circle"></span><iais:code code="PMDE002"/></label>
        <br>
        <label class="form-check-label" for="Mastercard" >
            <img src="<%=webroot1%>img/Mastercard.svg" width="66" height="25" alt="CARD">
            &nbsp;
            <%--<img src="/hcsa-licence-web/img/visa.svg" width="66" height="25" alt="GIRO">--%>
            <img src="<%=webroot1%>img/visa.svg" width="66" height="25" alt="CARD">
        </label>

    </div>
    <div class="col-md-4 col-xs-12" style="margin-bottom: 20px;">
        <input id="paymentNets" class=" premTypeRadio paymentInput"  type="radio" name="payMethod" value="PMDE003">
        <label class="form-check-label" for="paymentNets" ><span class="check-circle"></span><iais:code code="PMDE003"/></label>
        <br>
        <label class="form-check-label" for="paymentNets" ><img src="<%=webroot1%>img/paymentNets.png" width="66" height="30" alt="NETS"></label>
    </div>
    <div class="col-md-4 col-xs-12" style="margin-bottom: 20px;">
        <input id="paymentPayNow" class=" premTypeRadio paymentInput"  type="radio" name="payMethod" value="${ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW}">
        <label class="form-check-label" for="paymentPayNow" ><span class="check-circle"></span><iais:code code="${ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW}"/></label>
        <br>
        <label class="form-check-label" for="paymentPayNow" ><img src="<%=webroot1%>img/paymentPayNow.png" width="66" height="30" alt="PayNow"></label>
    </div>
    <c:if test="${empty RetriggerGiro && IsGiroAcc}">
        <div class="col-md-3 col-xs-12">
            <input id="PereOceanGIRO" class=" premTypeRadio paymentInput"  type="radio" name="payMethod" value="PMDE001">
            <label class="form-check-label" for="PereOceanGIRO" ><span class="check-circle"></span><iais:code code="PMDE001"/></label>
            <br>
            <label class="form-check-label" for="PereOceanGIRO" ><img src="<%=webroot1%>img/PereOceanGIRO.png" width="66" height="30" alt="GIRO"></label>
        </div>
    </c:if>
</div>
<c:if test="${empty RetriggerGiro && IsGiroAcc}">
    <div class="col-xs-12 giro-acct-div hidden">
        <iais:field value="Giro Account "  mandatory="true" width="12"/>
        <iais:value cssClass="col-md-5">
            <iais:select name="giroAccount" options="giroAccSel"  value="${AppSubmissionDto.giroAcctNum}" />
        </iais:value>
    </div>
</c:if>
<div class="col-xs-12">
    <div class="col-xs-12">
        <c:choose>
            <c:when test="${'APTY004' == AppSubmissionDto.appType}">
                <span name="iaisErrorMsg" id="error_payMethod" class="error-msg"></span>
            </c:when>
            <c:otherwise>
                <span name="iaisErrorMsg" id="error_pay" class="error-msg"></span>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="row">
    <div class="col-xs-12 col-md-12">
        <iais:message key="NEW_ACK017" escape="false"/>
    </div>
</div>

<div class="row">
    <div class="col-xs-12 col-md-4">
        <c:if test="${('APTY002' == AppSubmissionDto.appType || 'APTY005' == AppSubmissionDto.appType || 'APTY004' == AppSubmissionDto.appType ) && requestInformationConfig == null}">
            <a id="BACK" href="#" class="back"><em class="fa fa-angle-left"></em> Back</a>
        </c:if>
    </div>
    <div class="col-xs-12 col-md-8">
        <p class="text-right text-center-mobile"><iais:input type="button" id="proceed" cssClass="proceed btn btn-primary" value="Pay"></iais:input></p>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        var reloadPaymentMethod = $('input[name="reloadPayMethod"]').val();
        if(reloadPaymentMethod != null || reloadPaymentMethod != ''){
            $('.paymentInput[value="'+reloadPaymentMethod+'"]').prop('checked',true);
            if('PMDE001' == reloadPaymentMethod){
                $('.giro-acct-div').removeClass('hidden');
            }
        }
        $('.paymentInput').click(function () {
            var thisVal = $(this).val();
            if('PMDE001' == thisVal){
                $('.giro-acct-div').removeClass('hidden');
            }else{
                $('.giro-acct-div').addClass('hidden');
            }
        });
    });
</script>

