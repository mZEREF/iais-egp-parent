<%@page import="sop.util.WebDataUtil"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt_rt"%>
<%@ taglib uri="ecquaria/sop/sop-htmlform" prefix="sop-htmlform"%>
<%-- <%@ taglib uri="ecquaria/sop/sop-errors" prefix="sop-errors"%> --%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>

<%
	//handle to the Engine APIs
	BaseProcessClass process = (BaseProcessClass) request
			.getAttribute("process");
	String title = "Represent Citizen";

	pageContext.setAttribute("title", title);

	////
	Object properties = request.getAttribute("properties");

	if (properties != null) {
		pageContext.setAttribute("propertyMap", BeanUtils
				.describe(properties));
	}
	
    String callbackUrl = WebDataUtil.getString(request, "callback");
	if(StringHelper.isEmpty(callbackUrl)) {
		callbackUrl = "";
	}

%>

<%@page import="sop.webflow.rt.api.BaseProcessClass"%>

<%@include file="/allHeader.jspf"%>
<%@page import="sop.web.WebConstants"%>

<%@page import="org.apache.commons.beanutils.BeanUtils"%>

<webui:setAttribute name="title">
	<egov-smc:titleLabel id="title"><c:out value="${title}" /></egov-smc:titleLabel>
	<c:out value="${title}" />
</webui:setAttribute>
<webui:setAttribute name="header-ext">
	
	<script type="text/javascript">
		function clearForm(){
			<egov-smc:message id="clearConfirm" key="clearConfirm"><%=WebConstants.MSG_CLEAR_CONFIRM%></egov-smc:message>
			
			SOP.Common.confirm({message:'<egov-core:escapeJavaScript value="${clearConfirm}"/>',func: clearFormData});
			
		}
		function clearFormData(){
			var inputs = ["id","name","remarks","tags"];
			for(var i = 0;i < inputs.length;i ++){
				var e = document.getElementById(inputs[i]);
				if(null != e ){
					e.value = "";
				}
			}
			
			var userDomainsElement = document.getElementsByName("userDomains");
			for(var i=0;i<userDomainsElement.length;i++){
				userDomainsElement[i].checked = false ;
			}
		}
		
		function check(){		
			var domains = document.getElementsByName("userDomains");
			var checked = false;
			for(var i = 0;i<domains.length;i++){
				if(domains[i].checked){
					checked = true;
				}
			}
			if(!checked){
				<egov-smc:message id="domainRequre" key="domainRequre"><%=WebConstants.MSG_REQUIRE_DOMAIN%></egov-smc:message>
				SOP.Common.alert('<egov-core:escapeJavaScript value="${domainRequre}" />');
			}
			return checked;
		}
		
		function performAction(action) {
			$("form").submit();
		}

		function checkSave() {
			if(check()) {
				performAction("Save");
			}
		}
		SOP.Common.load(function(){
			SOP.Common.initFieldPanel();
	      });
		
		var initSmallError = function(){
			$('#dialogform small.error').each(function(){
				var html = $(this).html();
				if(html){
					$(this).show();
				}else{
					$(this).hide();
					$(this).prevAll('input').removeClass('inputtext-error');
				}
			});
		};
		
		$(function(){
			$('#dialogform').dialog({
				'title':'<c:out value="${title}" />',
				'resizable': false,
				'dialogClass':'form-content', 
				'modal': true,
				'show': 'blind', 
				'autoOpen': true,
				'width':650,
				'position':['center',200],
				'open':function(){
					$(this).parents('.ui-dialog').find('.ui-dialog-titlebar').find('.ui-dialog-titlebar-close').remove();
				}
			});
			initSmallError();
		})
	</script>

</webui:setAttribute>
	<div id="dialogform">
		<form class="form" action="<%=process.runtime.continueURL()%>" method="post">
			<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
			<sop-htmlform:hidden name="callback" value="<%=callbackUrl %>" />
			<br class="clear"/>
			<div class="required">
				<label><egov-smc:commonLabel>Applicant Id</egov-smc:commonLabel></label>
				<sop-htmlform:text name="applicantId" elementId="applicantId" value="${applicantId}" cssClass="inputtext-required"/>					
				<small class="error"><c:out value="${errorMap.applicantId }"/></small>
			</div>
			<div class="action-buttons">
				<button onclick="performAction();">
					<egov-smc:commonLabel>Submit</egov-smc:commonLabel>
				</button>
			</div>
		</form>
	</div>
	 <br class="clear"/>  

