<%@page import="sop.web.WebConstants"%>
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
	String title = "Applicant Authentication";

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
		
		function run(){
			var timespace = $('#timespace').html().substring(1,$('#timespace').html().length-1);
			if(timespace == 0){
				$('#timespace').html("")
				$('#sendBtn').removeClass('search-reset');
			   return false;
			}
			$('#timespace').html("("+(timespace*1 - 1)+")");
		}
		window.setInterval("run();", 1000);
			
		var sendOTP = function(){
			var timespace = $('#timespace').html().substring(1,$('#timespace').html().length-1);
			
			if(timespace*1>0){
				return;
			}
			
			var applicantId = $('#applicantId').val();
			if(applicantId){
				var eHandler = function(XMLHttpRequest, textStatus, errorThrown){
					console.warn("status[", textStatus,"] errorThrown[", errorThrown,"]");
				}
				
				var sHandler1 = function(data, textStatus){
					var status = eval(data)[0].status;
					if(status == "success"){
						$('#timespace').html("(30)");
						$('#sendBtn').addClass('search-reset');
					}else if(status == "fail"){
						SOP.Common.alert(eval(data)[0].message);
					}
				}
				
				$.ajax({
					type: "POST",
					url: '<%=request.getContextPath()%>/process/EGOV/RepresentCitizenProcess' + '/Step3',
					data: {applicantId: $('#applicantId').val()},
					success: sHandler1,
					error: eHandler,
					async: false
				});
			}else{
				SOP.Common.alert("The applicant id cannot be empty.");
			}
		}
	</script>

</webui:setAttribute>
<egov-core:breadcrumb>
	<egov-core:breadcrumbEntry label="Home" url="/process/EGPCLOUD/Home"/>
	<egov-core:breadcrumbEntry label="Applicant Authentication" />
</egov-core:breadcrumb>
<h1><c:out value="${title}" /></h1>
<div class="onecolumn">  <!-- Begin Main Content Container -->
	<div class="header"></div>
	<div>
		<form class="form" action="<%=process.runtime.continueURL()%>" method="post">
			<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
			<sop-htmlform:hidden name="callback" value="<%=callbackUrl %>" />
			<br class="clear"/>
			<div class="required">
				<label><egov-smc:commonLabel>Applicant Id</egov-smc:commonLabel></label>
				<sop-htmlform:text name="applicantId" elementId="applicantId" value="${applicantId}" cssClass="inputtext-required"/>
				<button style="padding:2px;width:100px;" type="button" onclick="sendOTP();" id="sendBtn"><egov-smc:commonLabel>Send OTP</egov-smc:commonLabel><span style="background:none;padding-left:3px;" id="timespace"></span></button>
				<small class="error"><c:out value="${errorMap.applicantId }"/></small>
			</div>
			<div class="required">
				<label><egov-smc:commonLabel>OTP No.</egov-smc:commonLabel></label>
				<sop-htmlform:text name="otp" elementId="otp" value="${otp}" cssClass="inputtext-required"/>
				<small class="error"><c:out value="${errorMap.otp }"/></small>
			</div>
			<div class="action-buttons">
				<button onclick="performAction();">
					<egov-smc:commonLabel>Submit</egov-smc:commonLabel>
				</button>
			</div>
		</form>
	</div>
	 <br class="clear"/>  
</div>

