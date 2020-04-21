<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<h2>Payment Method</h2>

<div class="col-xs-12">
    <div class="col-xs-3">
        <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="Credit">
        <label class="form-check-label" ><span class="check-circle"></span>Credit/Debit Card</label>
    </div>
    <div class="col-xs-2">
        <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="Credit">
        <label class="form-check-label" ><span class="check-circle"></span>NETS</label>
    </div>
    <div class="col-xs-2">
        <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="Credit">
        <label class="form-check-label" ><span class="check-circle"></span>PayNow</label>
    </div>

</div>
<div class="col-xs-12">
    <div class="col-xs-3">
        <img src="<%=webroot1%>img/mastercard.png" width="40" height="25" alt="mastercard">
        &nbsp;
        <img src="<%=webroot1%>img/paymentVISA.png" width="66" height="25" alt="VISA">
    </div>
    <div class="col-xs-2">
        <img src="<%=webroot1%>img/paymentNets.png" width="66" height="25" alt="NETS">
        <!--<img src="/hcsa-licence-web/iais/newApplication/paymentNets.png" width="66" height="25" alt="NETS"> -->
    </div>
    <div class="col-xs-2">
        <img src="<%=webroot1%>img/paymentPayNow.png" width="66" height="25" alt="PayNow">
        <!--<img src="/hcsa-licence-web/iais/newApplication/paymentPayNow.png" width="66" height="25" alt="PayNow"> -->
    </div>
</div>
<div class="col-xs-12">
    <div class="col-xs-3">
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
<%--<input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="GIRO">
<label class="form-check-label" ><span class="check-circle"></span>GIRO</label>--%>
<%--<img src="<%=webroot1%>img/payments.png" width="36" height="30" alt="GIRO">--%>

<div class="row">
    <c:if test="${'APTY004' == AppSubmissionDto.appType}">
        <div class="col-xs-12 col-sm-6" style="margin-top: 17px;">
            <p><a id="BACK" class="back"><em class="fa fa-angle-left"></em> Back</a></p>
        </div>
    </c:if>
    <div class="col-xs-12 col-sm-6">
        <p class="text-right text-center-mobile"><iais:input type="button" id="proceed" cssClass="proceed btn btn-primary" value="Proceed"></iais:input></p>
    </div>
</div>

<%--<p class="visible-xs visible-sm table-row-title">Proceed</p>--%>


