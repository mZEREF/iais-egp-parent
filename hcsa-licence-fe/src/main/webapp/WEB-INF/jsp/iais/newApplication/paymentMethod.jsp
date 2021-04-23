<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<h2>Payment Method</h2>
<input type="hidden" name="reloadPayMethod" value="${reloadPaymentMethod}"/>
<div class="col-xs-12">
    <div class="col-xs-3">
        <input class="form-check-input premTypeRadio paymentInput"  type="radio" name="payMethod" value="PMDE002">
        <label class="form-check-label" ><span class="check-circle"></span><iais:code code="PMDE002"/></label>
    </div>
    <div class="col-xs-2">
        <input class="form-check-input premTypeRadio paymentInput"  type="radio" name="payMethod" value="PMDE003">
        <label class="form-check-label" ><span class="check-circle"></span><iais:code code="PMDE003"/></label>
    </div>
    <c:if test="${empty RetriggerGiro && IsGiroAcc}">
        <div class="col-xs-3">
            <input class="form-check-input premTypeRadio paymentInput"  type="radio" name="payMethod" value="PMDE001">
            <label class="form-check-label" ><span class="check-circle"></span><iais:code code="PMDE001"/></label>
        </div>
    </c:if>


</div>
<div class="col-xs-12">
    <div class="col-xs-3">
        <%--<img src="/hcsa-licence-web/img/Mastercard.svg" width="66" height="25" alt="GIRO">--%>
        <img src="<%=webroot1%>img/Mastercard.svg" width="66" height="25" alt="CARD">
        &nbsp;
        <%--<img src="/hcsa-licence-web/img/visa.svg" width="66" height="25" alt="GIRO">--%>
        <img src="<%=webroot1%>img/visa.svg" width="66" height="25" alt="CARD">
    </div>
    <div class="col-xs-2">
        <img src="<%=webroot1%>img/paymentNets.png" width="66" height="30" alt="NETS">
    </div>
    <c:if test="${empty RetriggerGiro && IsGiroAcc}">
        <div class="col-xs-3">
                <%--<img src="/hcsa-licence-web/img/PereOceanGIRO.png" width="66" height="25" alt="GIRO">--%>
            <img src="<%=webroot1%>img/PereOceanGIRO.png" width="66" height="30" alt="GIRO">
        </div>
    </c:if>
</div>
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
<%--    <c:choose>--%>
<%--        <c:when test="${'APTY004' == AppSubmissionDto.appType}">--%>
<%--            <div class="col-xs-12 col-sm-6" style="margin-top: 17px;">--%>
<%--                <a id="BACK" class="back"><em class="fa fa-angle-left"></em> Back</a>--%>
<%--            </div>--%>
<%--        </c:when>--%>
<%--        <c:otherwise>--%>
<%--            <div class="col-xs-12 col-sm-6">--%>
<%--            </div>--%>
<%--        </c:otherwise>--%>
<%--    </c:choose>--%>
    <div class="col-xs-12 col-sm-6">
        <c:if test="${('APTY002' == AppSubmissionDto.appType || 'APTY005' == AppSubmissionDto.appType || 'APTY004' == AppSubmissionDto.appType ) && requestInformationConfig == null}">
            <a id="BACK" class="back"><em class="fa fa-angle-left"></em> Back</a>
        </c:if>
    </div>
    <div class="col-xs-12 col-sm-6">
        <p class="text-right text-center-mobile"><iais:input type="button" id="proceed" cssClass="proceed btn btn-primary" value="Proceed"></iais:input></p>
    </div>
</div>

<%--<p class="visible-xs visible-sm table-row-title">Proceed</p>--%>
<script type="text/javascript">
    $(document).ready(function () {
        var reloadPaymentMethod = $('input[name="reloadPayMethod"]').val();
        if(reloadPaymentMethod != null || reloadPaymentMethod != ''){
            $('.paymentInput[value="'+reloadPaymentMethod+'"]').prop('checked',true);
        }
    });
</script>

