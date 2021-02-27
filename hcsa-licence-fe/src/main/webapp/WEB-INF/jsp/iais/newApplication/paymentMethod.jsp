<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<h2>Payment Method</h2>
<div class="col-xs-12">
    <div class="col-xs-3">
        <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="PMDE002">
        <label class="form-check-label" ><span class="check-circle"></span><iais:code code="PMDE002"/></label>
    </div>
    <div class="col-xs-2">
        <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="PMDE003">
        <label class="form-check-label" ><span class="check-circle"></span><iais:code code="PMDE003"/></label>
    </div>
    <c:if test="${empty RetriggerGiro && IsGiroAcc}">
        <div class="col-xs-3">
            <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="PMDE001">
            <label class="form-check-label" ><span class="check-circle"></span><iais:code code="PMDE001"/></label>
        </div>
    </c:if>


</div>
<div class="col-xs-12">
    <div class="col-xs-3">
        <img src="https://upload.wikimedia.org/wikipedia/commons/a/a4/Mastercard_2019_logo.svg" width="66" height="35" alt="GIRO">
        &nbsp;
        <img src="https://upload.wikimedia.org/wikipedia/commons/5/5e/Visa_Inc._logo.svg" width="66" height="35" alt="GIRO">
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


