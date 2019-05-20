<%@page import="com.ecquaria.egp.core.helper.CurrencyHelper"%>
<%@page import="sop.config.ConfigUtil"%>
<%@page import="com.ecquaria.egp.core.payment.PaymentTransaction"%>
<%@page import="com.ecquaria.egp.core.helper.EGPCommonHelper"%>
<%@page import="com.ecquaria.egp.core.payment.PaymentData"%>
<%@page import="java.util.Date"%>
<%@page import="com.ecquaria.egp.core.payment.PaymentReceipt"%>
<%@page import="ecq.crud.helper.View"%>
<%@ page import="com.ecquaria.egp.core.helper.ConsistencyHelper" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<webui:setLayout name="saas"/>
<%
	response.setContentType("text/html;charset=UTF-8");
/*
  You can customize this default file:
  /E:/Eclipse4.3-EGP-SIT-win32/eclipse/plugins/com.ecquaria.eclipse.sit_6.1.1/WebPage.jsp.default
*/

//handle to the Engine APIs
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
PaymentData paymentData = (PaymentData)request.getAttribute("paymentData");
PaymentReceipt receipt = (PaymentReceipt)request.getAttribute("receipt");
PaymentTransaction paymentTrans = (PaymentTransaction)request.getAttribute("paymentTrans");

Date createDate = receipt.getCreatedDate();
String createDateStr = EGPCommonHelper.formatDateTime(createDate);
String webContext = ConfigUtil.getWebBathPath(request);
%>

<webui:setAttribute name="header-ext">
<%
/* You can add additional content (SCRIPT, STYLE elements)
 * which need to be placed inside HEAD element here.
 */
%>
</webui:setAttribute>

<webui:setAttribute name="title">
<egov-smc:commonLabel>Payment Receipt</egov-smc:commonLabel>
</webui:setAttribute>
<egov-core:eServiceTitle title = "Payment Receipt"></egov-core:eServiceTitle>
<style>
    .wrapper-content>h4{
        display: none;
    }
</style>
	<!-- Begin page content -->
<br>
	<div class="panel">


		<form method="post" action=<%=process.runtime.continueURL()%>>
			<input type="hidden" name="sopEngineTabRef"
				value="<%=process.rtStatus.getTabRef()%>">
			<%=View.outputCrudHiddenAction() %>
		<!-- Personal Details -->
		<div class="panel-body">
			<!-- Alert -->
			<div class="alert alert-success fade in alert-dismissable">
				<%--<egov-smc:message id="successMsg" key="successMsg">--%>
				<strong><i class="fa fa-smile-o"></i>&nbsp;<egov-smc:message key="thankForPayment">Thank you for your payment.</egov-smc:message> </strong> <egov-smc:message key="plsClickPrint">Please click the Print button to print this receipt for your own reference.</egov-smc:message>
				<c:out value="${successMsg }" escapeXml="false"/>
			</div>
			<!-- end Alert -->
			<h5><egov-smc:commonLabel>Payment Receipt</egov-smc:commonLabel></h5>

			<!--  -->
			<table
				class="table table-bordered table-responsive">
				<tr>
					<td width="23%"><b><egov-smc:commonLabel>Receipt No.</egov-smc:commonLabel>:</b></td>
					<td><%=receipt.getRcptNo() %></td>
				</tr>
				<tr>
					<td><b><egov-smc:commonLabel>Payment Date</egov-smc:commonLabel> :</b></td>
					<td><%=ConsistencyHelper.formatDate(createDate)%></td>
				</tr>
				<tr>
					<td><b><egov-smc:commonLabel>Payment Method</egov-smc:commonLabel> :</b></td>
					<td><egov-smc:commonLabel><%=paymentData.getMech().getMechName() %></egov-smc:commonLabel></td>
				</tr>
				<tr>
					<td><b><egov-smc:commonLabel>Payment Description</egov-smc:commonLabel> :</b></td>
					<td><egov-smc:commonLabel><%=paymentData.getPaymentDescription() %></egov-smc:commonLabel></td>
				</tr>
				<tr>
					<td><b><egov-smc:commonLabel>Payment Amount</egov-smc:commonLabel> :</b></td>
					<td><%=CurrencyHelper.formatCurrency(paymentData.getAmount())%>
					</td>
				</tr>
			</table>
		</div>
		<input type="hidden" name="tinyKey" value="<c:out value='${tinyKey }'/>">
		<div class="form-group text-center">
            <button type="button" onclick="print();" class='btn btn-round-lg btn-lg btn-oblue2 btn-st'><i class="fa fa-print"></i>&nbsp;<egov-smc:commonLabel>Print</egov-smc:commonLabel></button>
            <button onclick="next();" class='btn btn-round-lg btn-blue2 btn-st'><i class="fa fa-arrow-right"></i>&nbsp;<egov-smc:commonLabel>Next</egov-smc:commonLabel></button>
		</div>
        </br>
        </br>
		</form>
		<script type="text/javascript">
			function next(){
				SOP.Crud.cfxSubmit("", "next");
			}

			function print(){
				window.open("<%=request.getContextPath() %>/process/EGPCLOUD/PaymentCallBack/Print?id=<%=paymentTrans.getTransId()%>");
			}
		</script>
	</div>
