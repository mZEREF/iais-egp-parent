<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<h2>Payment Method</h2>
<input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="Credit">
<label class="form-check-label" ><span class="check-circle"></span>Credit/Debit Card</label>&nbsp&nbsp&nbsp&nbsp
<input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="GIRO">
<label class="form-check-label" ><span class="check-circle"></span>GIRO</label>
<span name="iaisErrorMsg" id="error_pay" class="error-msg"></span>
<br>

&nbsp&nbsp&nbsp&nbsp<img src="<%=webroot1%>img/mastercard.png" width="40" height="25" alt="mastercard">&nbsp
<img src="<%=webroot1%>img/paymentVISA.png" width="66" height="25" alt="VISA">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<img src="<%=webroot1%>img/payments.png" width="36" height="30" alt="GIRO">
<p class="visible-xs visible-sm table-row-title">Proceed</p>
<p class="text-right text-center-mobile"><iais:input type="button" id="proceed" cssClass="proceed btn btn-primary" value="Proceed"></iais:input></p>

