<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page import="com.ecquaria.cloud.mc.profile.UserProfile" %>
<%@ page import="com.ecquaria.cloud.mc.profile.xml.ProfileXmlEntity" %>
<%@ page import="com.ecquaria.cloud.mc.profile.xml.ProfileXmlHelper" %>
<%@ page import="com.ecquaria.cloud.mc.profile.xml.FieldAttribute" %>
<%@ page import="com.ecquaria.cloud.mc.profile.xml.field.ProfileField" %>
<%@ page import="com.ecquaria.cloud.mc.profile.xml.field.ProfileFieldHelper" %>
<%@ page import="sop.webflow.rt.api.BaseProcessClass" %>
<%@ page import="sop.config.ConfigUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	response.setHeader("Content-type", "text/html;charset=UTF-8");
	response.setCharacterEncoding("UTF-8");
%>

<script type="text/javascript">
	 function submitForm(action){
		 $("input[name='Action']").val(action);
		 document.viewForm.submit();
	 }

	 function uploadDocument(name){
		 $("input[name='FieldName']").val(name);
		 submitForm('upload');
	 }

	 function removeDocument(name){
		 $("input[name='FieldName']").val(name);
		 submitForm('remove');
	 }

	 function downloadDocument(name){
		 $("input[name='FieldName']").val(name);
		 submitForm('download');
	 }

	 function browseDocument(name){
		 var $target = $('input[type="file"][name="' + name + '"]');
		 if($target){
			 $target.click();
		 }
	 }

	 function fileChanged(id, name){
		 var val = $('input[type="file"][name="' + name + '"]').val();
		 val = getFileName(val);
		 var $span = $(document.getElementById(id));
		 if(val){
			 $span.html(val);
			 $span.addClass("select-file");
		 }
	 }

	 function getFileName(file){
		if(!file){
			return;
		}
		var pos1 = file.lastIndexOf('/');
		var pos2 = file.lastIndexOf('\\');
		var pos  = Math.max(pos1, pos2);
		if( pos < 0 ){
		    return file;
		}else{
		    return file.substring(pos+1);
		}
	 }

	 SOP.Common.load(function(){
			 SOP.Common.setupHint();
			SOP.Common.setupTooltip($('.icon-info'),{
					position: "bottom right",
					offset:[0, 0],
					effect: "fade",
					opacity: 0.7
				});
	});



	 $(document).ready(function(){
		//for 2.2 UI
		$("body>.container").addClass("profile-wrap");

		var msgDiv = $(".error,.success")
		if(msgDiv.length && msgDiv.parents(".tab-pane").length) {
			$jbs("#myTab li a[href='#"+msgDiv.parents(".tab-pane").attr("id")+"']").click();
		}

		$(".alert").click(function(){
			$(this).slideUp();
		});
	 });

</script>

<!--[if lt IE 10]>
<script type="text/javascript">
	 $(document).ready(function(){
	 	$("#myTab a").click(function(e){
		 	e.preventDefault()
		  	$jbs(this).tab('show')
	 		$(".placeholderDIV").remove();
	 		$('input[placeholder]:visible').placeholder();
	 	});
	 });
</script>
<![endif]-->

<style type="text/css">
	.action-buttons-panel{
		text-align: center;
	}

	#nav a[href*="MyProfile"]{
		color: #f8ca2c;
	}
	input[type="file"] {

		display: inline-block;
		border: 1px solid rgba(189, 195, 199,0.5);
		border-radius: 3px;
		-moz-border-radius: 3px;
		-webkit-border-radius: 3px;
		padding: 3px;
		margin-bottom: 5px;
		width: 80%;

	}
</style>

<webui:setAttribute name="title">
	<egov-smc:commonLabel><c:out value="Update Profile"/></egov-smc:commonLabel>
</webui:setAttribute>

<%
	Map<String,String> errorMap = (Map<String,String>)request.getAttribute("errorMap");
	if(errorMap == null){
		errorMap = new HashMap<String,String>();
	}
	UserProfile profile = (UserProfile)request.getAttribute("profile");
	ProfileXmlEntity xmlEntity = ProfileXmlHelper.getXmlEntity();
	ProfileXmlEntity.BaseEntity baEntity = xmlEntity.getBase();

	ProfileXmlEntity.ExternalEntity exEntity = xmlEntity.getExternal();
	List<ProfileXmlEntity.SectionEntity> sections = exEntity.getSections();

	BaseProcessClass process = (BaseProcessClass) request
			.getAttribute("process");
%>



<form  enctype="multipart/form-data" name="viewForm" cssClass="viewForm" cssStyle="overflow:visible;" action="<%=process.runtime.continueURL()%>" method="post">
	<section>
		<c:if test="${not empty errorMessage}">
			<div class="alert alert-error">
				<p>
					<c:out value="${errorMessage}"></c:out>
				</p>
			</div>
		</c:if>
		<c:if test="${not empty successMessage}">
			<div class="alert alert-success">
				<p>
					<c:out value="${successMessage}"></c:out>
				</p>
			</div>
		</c:if>

		<!-- Breadcumb -->
		<%
			String homeUrl = ConfigUtil.getString("egp.cms.home.url", "/cc");
		%>
		<!-- Breadcumb -->
		<div class="breadcumb">
			<ul>
				<li><a class="black" href="<%=homeUrl%>"><egov-smc:commonLabel>Home</egov-smc:commonLabel></a> </li>
				<li><span class="black"><egov-smc:commonLabel>My Profile</egov-smc:commonLabel></span> </li>
			</ul>
		</div>

		<div role="tabpanel">
			<ul role="tablist" class="nav nav-tabs" id="myTab">
				<li class="active"><a aria-controls="<%=baEntity.getId() %>" data-toggle="tab" role="tab" href="#<%=baEntity.getId() %>" aria-expanded="true"><i class="<%=baEntity.getIconClass()%>"></i> <span><egov-smc:commonLabel><%=baEntity.getName()%></egov-smc:commonLabel></span></a></li>
				<%for (int i = 0; i < sections.size(); i++) {%>
					<li class=""><a aria-controls="<%=sections.get(i).getId() %>" data-toggle="tab" role="tab" href="#<%=sections.get(i).getId() %>" aria-expanded="true"><i class="<%=sections.get(i).getIconClass()%>"></i> <span><egov-smc:commonLabel><%=sections.get(i).getName()%></egov-smc:commonLabel></span></a></li>
				<%}%>

			</ul>
		</div>


		<div class="tab-content" id="myTabContent">
			<div aria-labelledby="<%=baEntity.getId() %>" id="<%=baEntity.getId() %>" class="tab-pane fade active in" role="tabpanel">
				<br>
				<div class="control-grid form-wrap">
					<div class="formtitl white header" style="background-color: #aaaaaa;color: #fff;padding: 7px 5px;border: 1px solid #e1e1e1; border-top-left-radius: 3px; border-top-right-radius: 3px;">
						<h4><egov-smc:commonLabel><%=baEntity.getDesc() %></egov-smc:commonLabel></h4>
					</div>
					<div class="form-area" role="form">
						<article>
							<table class=" table table-condensed table-striped ">
								<tbody>
									<%
										List<FieldAttribute> baseChildren =  baEntity.getFields();
										String mandatoryMark = "<span class=\"mandatory\">*</span>";
										for (int i = 0; i < baseChildren.size(); i++) {
											FieldAttribute attr = baseChildren.get(i);
											String type = attr.getType();
											ProfileField field = ProfileFieldHelper.getProfileField(type);
											out.print("<tr>");

											if(attr.isRequired()){
												out.print("<td>");
												out.print(mandatoryMark);
											}else {
												out.print("<td class=\"text-muted\">");
											}
											out.print(attr.getDisplayLabel());
											out.print(":</td><td>");
											out.print(field.renderHtml(profile,attr.getName(), errorMap, pageContext));
											out.print("</td><tr>");
										}
									%>
								</tbody>
							</table>
						</article>
						<br/>
					</div>
				</div>
				<div class="alert alert-info" role="alert">
					<p style="margin-left: 0px;"><i class="fa fa-address-card"></i> <egov-smc:message escape="true" key="personaldetailtips">Your personal details displayed above were taken from National Database. To update any of these details, please visit the <a>Immigration Department.</a></egov-smc:message></p>
				</div>
			</div>

			<%
				for (int i = 0; i < sections.size(); i++) {
					ProfileXmlEntity.SectionEntity entity = sections.get(i);
			%>
					<div aria-labelledby="<%=entity.getId() %>" id="<%=entity.getId() %>" class="tab-pane fade " role="tabpanel">
						<br>
						<div class="formtitl white header" style="background-color: #aaaaaa;color: #fff;padding: 7px 5px;border: 1px solid #e1e1e1; border-top-left-radius: 3px; border-top-right-radius: 3px;">
							<h4><b><egov-smc:commonLabel><%=entity.getDesc() %></egov-smc:commonLabel></b></h4>
						</div>
						<div class="form-horizontal" role="form">
						<%
						List<FieldAttribute> fields = entity.getFields();
						for (int j = 0; j < fields.size(); j++) {
							FieldAttribute attr = fields.get(j);
							String type = attr.getType();
							ProfileField field = ProfileFieldHelper.getProfileField(type);
							%>
							<div class="form-group">
								<br>
	                            <label for="local-address" class="col-sm-3 control-label">
	                            	<%=attr.isRequired()?mandatoryMark:""%>
									<%=attr.getDisplayLabel() %>:
	                            </label>
	                            <div class="col-sm-8">
	                            	<%=field.renderHtml(profile, attr.getName(), errorMap, pageContext) %>
	                            </div>
                            </div>
						<%}%>
						</div>
					</div>
				<%}%>
			<input type="hidden" name ="Action"/>
			<input type="hidden" name ="FieldName"/>
			<div class="form text-center" style="border:none;">
				<button type="button" class="btn btn-round-lg btn-lg btn-oblue2 btn-st" onclick="location.href='<%=request.getContextPath()%>'"><i class="fa fa-arrow-left"></i>&nbsp;&nbsp;<egov-smc:commonLabel>Back</egov-smc:commonLabel></button>
				<button type="button" class="btn btn-round-lg btn-blue2 btn-st" onclick="submitForm('update')" ><i class="fa fa-user"></i>&nbsp;&nbsp;<egov-smc:commonLabel>Update Profile</egov-smc:commonLabel></button>
			</div>
		</div>
	</section>
</form>
