<%@page import="com.ecquaria.egp.core.helper.CurrencyHelper"%>
<%@page import="com.ecquaria.egp.core.payment.PaymentTransaction"%>
<%@page import="com.ecquaria.egp.core.payment.PaymentReceipt"%>
<%@page import="com.ecquaria.egp.core.payment.PaymentData"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="com.ecquaria.egp.core.helper.EGPCommonHelper"%>
<%@page import="java.util.Date"%>
<%@page import="ecq.crud.helper.View"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<webui:setLayout name="none" />
<%@ include file="/WEB-INF/jsp/inc/egp-common-include.jsp"%>
<link
	href="<egov-core:webURL source='sample/service4/css/bootstrap.css'/>"
	rel="stylesheet" type="text/css" media="all" />
<link
	href="<egov-core:webURL source='sample/service4/css/navbar-static-top.css'/>"
	rel="stylesheet" type="text/css" media="all" />
	
<script type="text/javascript">
	$(function(){
		window.print();
	});
	
</script>

<%
	response.setContentType("text/html;charset=UTF-8");
	sop.webflow.rt.api.BaseProcessClass process =
	(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
	PaymentTransaction trans = (PaymentTransaction)request.getAttribute("paymentTrans");
	PaymentReceipt receipt = trans.getRefRcpt();
	Date createDate = receipt.getCreatedDate();
	String createDateStr = EGPCommonHelper.formatDateTime(createDate);
	
%>

<webui:setAttribute name="header-ext">
</webui:setAttribute>

<title>
<egov-smc:commonLabel>Payment Receipt Print</egov-smc:commonLabel>
</title>

	<!-- Begin page content -->
	<div class="payment container bg">

			<input type="hidden" name="sopEngineTabRef"
				value="<%=process.rtStatus.getTabRef()%>">
			<%=View.outputCrudHiddenAction() %>
		<!-- Personal Details -->
		<div class="bs-callout bs-callout-info">
			<h3><egov-smc:commonLabel>Payment Receipt</egov-smc:commonLabel></h3>

			<!--  -->
			<table
				class="table table-bordered table-condensed table-striped table-bg">
				<tr>
					<td width="20%" class="text-muted"><egov-smc:commonLabel>Receipt No.</egov-smc:commonLabel>:</td>
					<td><%=receipt.getRcptNo() %></td>
				</tr>
				<tr>
					<td class="text-muted"><egov-smc:commonLabel>Date</egov-smc:commonLabel> :</td>
					<td><%=ConsistencyHelper.formatDate(createDate)%></td>
				</tr>
				<tr>
					<td class="text-muted"><egov-smc:commonLabel>Payment Method</egov-smc:commonLabel> :</td>
					<td><egov-smc:commonLabel><%=trans.getPymtMechanism() %></egov-smc:commonLabel></td>
				</tr>
				<tr>
					<td class="text-muted"><egov-smc:commonLabel>Payment Description</egov-smc:commonLabel> :</td>
					<td><egov-smc:commonLabel><%=trans.getPaymentDescription() %></egov-smc:commonLabel></td>
				</tr>
				<tr>
					<td class="text-muted"><egov-smc:commonLabel>Payment Amount</egov-smc:commonLabel> :</td>
					<td><%=CurrencyHelper.formatCurrency(trans.getTransAmount()) %></td>
				</tr>
			</table>
		</div>
	</div>
