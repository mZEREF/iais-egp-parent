<%@page import="sop.iwe.SessionManager"%>
<%@page import="com.ecquaria.egp.core.common.constants.MessageTemplateConstants"%>
<%@page import="ecq.crud.helper.View"%>
<%@page import="com.ecquaria.egp.core.payment.PaymentData"%>
<%@page import="com.ecquaria.egp.api.EGPCaseHelper"%>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ page import="com.ecquaria.egov.core.common.constants.AppConstants" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<webui:setLayout name="egp-payment"/>
<%
	response.setContentType("text/html;charset=UTF-8");
	String title = "Payment Selection";
	pageContext.setAttribute("title", title);
	sop.webflow.rt.api.BaseProcessClass process =
	(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
	PaymentData data = EGPCaseHelper.getPaymentCaseData(process.currentCase);
	String paymentDescription = "";
	if(data!=null){
		paymentDescription = data.getPaymentDescription();
	}
	

	SessionManager.LoginInformation loginInfo = SessionManager
			.getInstance(request).getLoginInfo();
	
	//TODO DQ
	/* Language language = loginInfo == null ? null : loginInfo
			.getLanguage();
	
	String langCode = language.getID();
	
	String siteLanguage = MultiLang.getSiteLanguage(); 

	if(StringHelper.isEmpty(langCode)){
		langCode = siteLanguage;
	}
	
	if(StringHelper.isEmpty(langCode)){
		langCode = ConfigUtil.getString("sop.site.default.language");
	}
	
	if(!StringHelper.isEmpty(langCode)){
		Language lang = Language.getLanguage(langCode);
		if(lang!=null){
			MultiLang.setSiteLanguage(langCode);
		}else{
			langCode = null;
		}
	}
	
	response.setContentType("text/html; charset="
			+ sop.i18n.LangUtil.getCharSetForLocale(sop.i18n.LangUtil
					.getLocaleForLanguage(langCode)));*/
%>

<webui:setAttribute name="title"><egov-smc:titleLabel id="oriTitle"><c:out value="${title}"></c:out></egov-smc:titleLabel><c:out value="${oriTitle}"></c:out></webui:setAttribute>
<egov-smc:messageTemplate key="egp.common.not.select" var="noselect" paramArray='<%=new String[]{MultiLangUtil.translate(request,AppConstants.KEY_TRANSLATION_MODULE_LABEL,"payment method")}%>' default="<%=MessageTemplateConstants.MSG_NO_SELECT%>"/>
<egov-smc:messageTemplate key="egp.common.payment.confirm" var="confirmPayment" paramArray='<%=new String[]{""}%>' default="<%=MessageTemplateConstants.MSG_COMMON_PAYMENT_CONFIRM%>"/>
<script type="text/javascript">
 function submitform(){
	 if($("[name='paymentMech']:checked").length>0){
	 	SOP.Common.confirm({"message":'<egov-core:escapeJavaScript value="${confirmPayment}" />', func:function(){SOP.Crud.cfxSubmit('','payform');}});
	 }else{
		 SOP.Common.alert('<egov-core:escapeJavaScript value="${noselect}" />');
	 }
 }
 
</script>

<div class="container">
	<div>
		<h1><c:out value="${oriTitle}"></c:out></h1>  
		<hr />
	</div>
	<div class="alert alert-info">
    	<egov-smc:commonLabel>Select your payment method</egov-smc:commonLabel>:
	</div>
	<form method="post" action=<%=process.runtime.continueURL()%> name="payform">
		<%=View.outputCrudHiddenAction() %>
		<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
		<div class="bs-callout">  
			<h3><egov-smc:commonLabel>Payment Methods</egov-smc:commonLabel></h3>
			<table class="table table table-hover">
				<tbody>
					<c:forEach items="${mechs }" var="mech" varStatus="">
						<tr>
							<td><label> <input type="radio" name="paymentMech" value='<c:out value="${mech.mechId }"/>'/><egov-smc:commonLabel><c:out value="${mech.mechName }"></c:out></egov-smc:commonLabel>
							</label></td>
						</tr>
					</c:forEach>
				</tbody>
				</table>
		 </div>
		<div>
			<button type="button" onclick="submitform();"><egov-smc:commonLabel>Next</egov-smc:commonLabel></button>
		</div>
	</form>
</div>
