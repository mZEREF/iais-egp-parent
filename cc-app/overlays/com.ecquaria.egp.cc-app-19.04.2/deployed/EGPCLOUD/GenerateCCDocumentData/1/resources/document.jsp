<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%


	//handle to the Engine APIs
	sop.webflow.rt.api.BaseProcessClass process =
			(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setAttribute name="header-ext">
	<%
		/* You can add additional content (SCRIPT, STYLE elements)
        * which need to be placed inside HEAD element here.
        */
	%>
</webui:setAttribute>
<%--<webui:setLayout name="saas" />--%>

<%--<webui:setLayout name="saas"/>--%>
<webui:setAttribute name="title">
	<%--<egov-smc:commonLabel>	Init My Document Data</egov-smc:commonLabel>--%>
</webui:setAttribute>

<script type="text/javascript">
	function performAction(action) {
        $("input[name='Action']").val(action);
        document.documentForm.submit();
		return true;
	}

    $(document).ready(function(){

        $(".message").click(function(){
            $(this).slideUp();
        });
    });
</script>

<%--<h1>--%>
	<%--<c:out value="Init My Document Data" />--%>
<%--</h1>--%>
<style type="text/css">
	p{
		margin:0px !important;
	}
	.message{
		margin:0px 28.5px !important;
		padding:15px;
		border-radius:5px;
	}
	.alert-error {
		background-color:#ce132d;
		border:1px solid #a7182c;
		border-radius:5px;
	}
	.error{
		color:red;
	}
</style>
<div class="onecolumn">
	<div class="header"></div>

	<div>
		<c:if test="${not empty message}">
			<div class="alert-success message">
				<p>
					<c:out value="${message}"></c:out>
				</p>
			</div>
		</c:if>

		<c:if test="${not empty errorMessage}">
			<div class="alert-error message">
				<p>
					<c:out value="${errorMessage}"></c:out>
				</p>
			</div>
		</c:if>
		<form name="documentForm" style="padding:20px;" class="form" enctype="multipart/form-data" method="post" action=<%=process.runtime.continueURL()%>>
		<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">

			<div>
				<h4><egov-smc:commonLabel>Init My Document Data</egov-smc:commonLabel></h4>
				<br/>
				<div class="form-horizontal" role="form">
					<div class="form-group">
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>User Id</egov-smc:commonLabel>: </label>
						<div class="col-sm-8">
							<input class="form-control" value="${userId}" name="userId" type="text">
							<c:if test="${not empty errorUserId }">
								<small class="error" style="display: block;"><c:out value="${ errorUserId}"/></small>
							</c:if>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>Agency Name</egov-smc:commonLabel>: </label>
						<div class="col-sm-8">
							<input class="form-control" value="${agency}" name="agency" type="text">
							<c:if test="${not empty errorAgency }">
								<small class="error" style="display: block;"><c:out value="${ errorAgency}"/></small>
							</c:if>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>Subject</egov-smc:commonLabel>: </label>
						<div class="col-sm-8">
							<input class="form-control" value="${subject}" name="subject" type="text">
							<c:if test="${not empty errorSubject }">
								<small class="error" style="display: block;"><c:out value="${ errorSubject}"/></small>
							</c:if>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>File</egov-smc:commonLabel>: </label>
						<div class="col-sm-8">
							<input type="file" onkeydown="return false" size="50"
								   name="file"
								   style="ime-mode: disabled; background-color: #E0E0E0;" />
							<c:if test="${not empty errorFile }">
								<small class="error" style="display: block;"><c:out value="${ errorFile}"/></small>
							</c:if>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>Amount</egov-smc:commonLabel>: </label>
						<div class="col-sm-8">
							<input class="form-control" value="${amount}" name="amount" type="text" title="Default: 10">
							<c:if test="${not empty errorAmount }">
								<small class="error" style="display: block;"><c:out value="${errorAmount}" /></small>
							</c:if>
						</div>
					</div>
				</div>
			</div>

			<div class="action-buttons" style="width:135px;margin:auto;">
				<button type="button" class="btn btn-primary" onclick="performAction('submit');" ><i class="fa fa-user"></i><egov-smc:commonLabel>Save</egov-smc:commonLabel></button>

			</div>
		</form>
	</div>
	<br class="clear" />
</div>


