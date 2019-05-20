<%@page import="com.ecquaria.cloud.payment.PaymentMechanismService"%>
<%@page import="com.ecquaria.egp.core.payment.runtime.PaymentProxy"%>
<%@page import="com.ecquaria.egp.api.EGPCaseHelper"%>
<%@page import="com.ecquaria.egp.core.payment.PaymentData"%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setAttribute name="title">
	Do Payment
</webui:setAttribute>
<%
	sop.webflow.rt.api.BaseProcessClass process =
	(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
	PaymentData data = EGPCaseHelper.getPaymentCaseData(process.currentCase);
	PaymentProxy proxy = PaymentMechanismService.getInstance().getPaymentProxy(data.getMech());
	proxy.doPayment(process);
%>