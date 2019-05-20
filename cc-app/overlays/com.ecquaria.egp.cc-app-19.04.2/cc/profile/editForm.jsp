<%@page import="org.apache.commons.beanutils.BeanUtils"%>
<%@page import="sop.rbac.user.User"%>
<%@page import="sop.webflow.rt.api.BaseProcessClass"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page import="sop.config.ConfigUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/sop-smc" prefix="sop-smc"%>
<%@ taglib uri="ecquaria/sop/sop-htmlform" prefix="sop-htmlform"%>
<%@page isELIgnored="false" %>
<%
	response.setContentType("text/html;charset=UTF-8");
BaseProcessClass process = (BaseProcessClass) request
.getAttribute("process");
Object properties = request.getAttribute("properties");

if (properties != null) {
	pageContext.setAttribute("propertyMap", BeanUtils
			.describe(properties));
}
User user = (User)request.getAttribute("model");
SimpleDateFormat dateFormat = (SimpleDateFormat)request.getAttribute("dateFormat");
//out.print(dateFormat);
String dob = null;
if (user != null && dateFormat != null) {
	if (user.getDateOfBirth() != null){
		dob = dateFormat.format(user.getDateOfBirth());
		//out.print(dob);
	}	
} 
pageContext.setAttribute("dob", dob);
Character cGender = user.getGender();
String gender ="";
if(cGender != null){
	if(cGender == 'M'){
		gender = "Male";
	}else if(cGender=='F'){
		gender = "Female";
	}else if(cGender == 'U'){
		gender = "Unknow";
	}	
}
%>
<script type="text/javascript">
	 function submitForm(action){
		 $("input[name='Action']").val(action);
		 document.viewForm.submit();
	 }
	 
	 function performAction(action) {
			$("input[name='Action']").val(action);
			$("form")[0].submit();
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
//		$("body>.container").addClass("profile-wrap");
		
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

body {
 	 background-color: #fff;
    color: #333;
    line-height: 1.42857;
    margin-bottom: 60px;
}
.profile-wrap div.tab-pane > h4 {
    margin-bottom: 20px;
}
h4, .h4 {
    font-size: 18px;
}
h4, .h4, h5, .h5, h6, .h6 {
    margin-bottom: 10px;
    margin-top: 10px;
}
h1, h2, h3, h4, h5, h6, .h1, .h2, .h3, .h4, .h5, .h6 {
    color: inherit;
    font-family: inherit;
    font-weight: 500;
    line-height: 1.1;
}

#content {
	color:#000;
}
.tab-div, .mesg-top {
	display:none; 
}
.formtitle {
	color:#000;
	border-bottom:1px dotted #333;
}
.message-area .textbox {
	letter-spacing:normal;
}
	.alert-error {
		background-color:#ce132d;
		border:1px solid #a7182c;
		border-radius:5px;
	}

	#nav a[href*="MyProfile"]{
		color: #f8ca2c;
	}
</style>

<webui:setAttribute name="title">
	<egov-smc:titleLabel><c:out value='Update Profile'/></egov-smc:titleLabel>
</webui:setAttribute>

<%
	Map<String,String> errorMap = (Map<String,String>)request.getAttribute("errorMap");
	if(errorMap == null){
		errorMap = new HashMap<String,String>();
	}
/* 	UserProfile profile = (UserProfile)request.getAttribute("profile");
	ProfileXmlEntity xmlEntity = ProfileXmlHelper.getXmlEntity();
	BaseEntity baEntity = xmlEntity.getBase();
	
	ExternalEntity exEntity = xmlEntity.getExternal();
	List<SectionEntity> sections = exEntity.getSections(); */
%>



<form  enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>" name="viewForm" class="viewForm" style="overflow:visible;" method="post">
<!-- 	<input name="sopEngineTabRef" value="14822154697648597" type="hidden">
	<input name="pageId" value="-1764960553" type="hidden">
	<input name="crud_action_type" value="" type="hidden">
	<input name="crud_action_value" value="" type="hidden">
	<input name="crud_action_additional" value="" type="hidden"> -->
	<%-- <c:forEach items="${propertyMap}" var="mapEntry">
		<input type="hidden" name="${mapEntry.key}" value="${entryValue}" />
	</c:forEach>
	<input type="hidden" name="update" value="${model.update}"/>
	<input type="hidden" name="userDomain" value="${model.userDomain}" />
	<input type="hidden" name="id" value="${model.id}" />
	<input type="hidden" name="UserFromSession" value="${UserFromSession}" /> --%>
	<c:forEach items="${propertyMap}" var="mapEntry">
		<sop-htmlform:hidden name="${mapEntry.key}" value="${entryValue}" />
	</c:forEach>
	<sop-htmlform:hidden name="update" value="${model.update}"/>
	<sop-htmlform:hidden name="userIdentifier.userDomain" value="${model.userDomain}" />
	<sop-htmlform:hidden name="userIdentifier.id" value="${model.id}" />
	<sop-htmlform:hidden name="UserFromSession" value="${UserFromSession}" />
	<section>
		<c:if test="${not empty errorMessage}">
			<div class="alert alert-error">
				<p>
					<c:out value="${errorMessage}"/>
				</p>
			</div>
		</c:if>
		<c:if test="${not empty successMessage}">
			<div class="alert alert-success">
				<p>
					<c:out value="${successMessage}"/>
				</p>
			</div>
		</c:if>
		
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
				<li class="active">
					<a aria-controls="profile" data-toggle="tab" role="tab" href="#profile" aria-expanded="true">
					<i class="fa fa-user"></i>
					<span><egov-smc:commonLabel>Profile</egov-smc:commonLabel></span>
					</a>
				</li>
				<li class="">
					<a aria-controls="language" data-toggle="tab" role="tab" href="#language" aria-expanded="true">
						<i class="fa fa-language"></i>
						<span><egov-smc:commonLabel>Language</egov-smc:commonLabel></span>
					</a>
				</li>
				<li class="">
					<a aria-controls="address" data-toggle="tab" role="tab" href="#address" aria-expanded="true">
					<i class="fa fa-location-arrow"></i>
					<span><egov-smc:commonLabel>Address</egov-smc:commonLabel></span>
					</a>
				</li>
				<li class="">
					<a aria-controls="contact" data-toggle="tab" role="tab" href="#contact" aria-expanded="true">
					<i class="fa fa-phone"></i>
					<span><egov-smc:commonLabel>Contact</egov-smc:commonLabel></span>
				</a>
				</li>
			</ul>
		</div>
		<div id="myTabContent" class="tab-content" style="padding-top: 0px;">
			<div id="profile" class="tab-pane fade active in" aria-labelledby="profile" role="tabpanel">
                <br>
                <div class="control-grid form-wrap">
                    <div class="formtitl white header" style="background-color: #aaaaaa;color: #fff;padding: 7px 5px;border: 1px solid #e1e1e1; border-top-left-radius: 3px; border-top-right-radius: 3px;">
				        <h4><egov-smc:commonLabel>Personal Profiles</egov-smc:commonLabel></h4>
                    </div>
				<div class="form-area" role="form">
                    <article>
                        <table class="table table-striped table-condensed">
                            <tbody>
                                <tr>
                                    <td class="profiletitle black text-muted col-sm-3 col-sm-offset-1 col-xs-3 col-xs-offset-1"><egov-smc:commonLabel>IC Number</egov-smc:commonLabel></td>
                                    <td>${model.icNumber}</td>
                                    <input type="hidden" name="icNumber" value="${model.icNumber}">
                                </tr>
                                <tr>
                                    <td class="profiletitle black text-muted col-sm-3 col-sm-offset-1 col-xs-3 col-xs-offset-1"><egov-smc:commonLabel>Name</egov-smc:commonLabel></td>
                                    <td>${model.displayName}</td>
                                    <input type="hidden" name="displayName" value="${model.displayName}">
                                </tr>
                                <tr>
                                    <td class="profiletitle black text-muted col-sm-3 col-sm-offset-1 col-xs-3 col-xs-offset-1"><egov-smc:commonLabel>Postcode</egov-smc:commonLabel></td>
                                    <td>${model.addressPostalCode}</td>
                                </tr>
                                <tr>
                                    <td class="profiletitle black text-muted col-sm-3 col-sm-offset-1 col-xs-3 col-xs-offset-1"><egov-smc:commonLabel>Country</egov-smc:commonLabel></td>
                                    <td>${myCountry}</td>
                                </tr>
                                <tr>
                                    <td class="profiletitle black text-muted col-sm-3 col-sm-offset-1 col-xs-3 col-xs-offset-1"><egov-smc:commonLabel>Date of Birth</egov-smc:commonLabel></td>
                                    <td>${dob}</td>
                                    <input type="hidden" name="dateOfBirth" value="${model.dateOfBirth}">
                                </tr>
                                <tr>
                                    <td class="profiletitle black text-muted col-sm-3 col-sm-offset-1 col-xs-3 col-xs-offset-1"><egov-smc:commonLabel>Gender</egov-smc:commonLabel></td>
                                    <td><%=gender %></td>
                                    <input type="hidden" name="gender" value="${model.gender}">
                                </tr>
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
			<div id="language" class="tab-pane fade " aria-labelledby="language" role="tabpanel">
                <br>
                <div class="formtitl white header" style="background-color: #aaaaaa;color: #fff;padding: 7px 5px;border: 1px solid #e1e1e1; border-top-left-radius: 3px; border-top-right-radius: 3px;">
                    <h4><egov-smc:commonLabel>Language</egov-smc:commonLabel></h4>
                </div>
                <div class="profile-wrap">
				<div class="form-horizontal form-area" role="form">
					<div class="form-group">
                        <br>
						<label class="col-sm-3 control-label" for="local-language"> <egov-smc:commonLabel>Language</egov-smc:commonLabel><span class="mandatory"> *</span></label>
						<div class="col-sm-7">
							<select class="form-control" name="language" value="" id="local-language">
								<c:if test="${empty model.language}">
									<option value="" selected></option>
								</c:if>
								<c:if test="${!empty model.language}">
									<option value=""></option>
								</c:if>
								<c:forEach var="language" items="${lookupLanguages }">
									<c:if test="${model.language == language }">
										<option value="<c:out value='${language }'/>" selected="selected"><egov-smc:commonLabel><c:out value="${language }"/></egov-smc:commonLabel></option>
									</c:if>
									<c:if test="${model.language != language }">
										<option value="<c:out value='${language }'/>"><egov-smc:commonLabel><c:out value="${language }"/></egov-smc:commonLabel></option>
									</c:if>
								</c:forEach>
							</select>
							<c:if test="${!empty modelErrors.getFieldError('language') }">
								<div class="textmandatory error">
									<c:out value="${modelErrors.getFieldError('language').getDefaultMessage() }" />
								</div>
							</c:if>
							<div class="textmandatory"><egov-smc:commonLabel>* Indicates required field</egov-smc:commonLabel></div>
						</div>
					</div>
				</div>
                </div>
			</div>
			<div id="address" class="tab-pane fade" aria-labelledby="address" role="tabpanel">
                <br>
                <div class="formtitl white header" style="background-color: #aaaaaa;color: #fff;padding: 7px 5px;border: 1px solid #e1e1e1; border-top-left-radius: 3px; border-top-right-radius: 3px;">
                    <h4><egov-smc:commonLabel>Correspondence Address</egov-smc:commonLabel></h4>
                </div>
                <div class="profile-wrap">
				<div class="form-horizontal form-area" role="form">
					<div class="form-group">
                        <br>
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>Address</egov-smc:commonLabel> </label>
						<div class="col-sm-7">
						<input class="form-control" value="${model.address }" name="address" placeholder="<egov-smc:commonLabel>Your Local Address</egov-smc:commonLabel>" type="text">
						<c:if test="${!empty modelErrors.getFieldError('address') }">
							<div class="textmandatory error"> <c:out
									value="${modelErrors.getFieldError('address').getDefaultMessage() }" />
							</div>
						</c:if>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>Postal Code</egov-smc:commonLabel> </label>
						<div class="col-sm-7">
						<input class="form-control" value="${model.addressPostalCode }" name="addressPostalCode" placeholder="<egov-smc:commonLabel>Your Local Postal code</egov-smc:commonLabel>" type="text">
							<c:if test="${!empty modelErrors.getFieldError('addressPostalCode') }">
								<div class="textmandatory error">
									<c:out value="${modelErrors.getFieldError('addressPostalCode').getDefaultMessage() }" />
								</div>
							</c:if>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>Country</egov-smc:commonLabel> </label>
						<div class="col-sm-7">
						<select class="form-control" name="addressCountryCode" value="">
							<option value=""></option>
								<c:forEach var="countryCode" items="${lookupCountry }">
								<c:if test="${model.addressCountryCode == countryCode.value }">
									<option value="<c:out value='${countryCode.value }'/>" selected="selected"><egov-smc:commonLabel><c:out value="${countryCode.displayName }"/></egov-smc:commonLabel></option>
								</c:if>
								<c:if test="${model.addressCountryCode != countryCode.value }">
									<option value="<c:out value='${countryCode.value }'/>"><egov-smc:commonLabel><c:out value="${countryCode.displayName }"/></egov-smc:commonLabel></option>
								</c:if>	
								</c:forEach>
						</select>
						<c:if test="${!empty modelErrors.getFieldError('addressCountryCode') }">
							<div class="textmandatory error">
								<c:out value="${modelErrors.getFieldError('addressCountryCode').getDefaultMessage() }" />
							</div>
						</c:if>
						</div>
					</div>
				</div>
                </div>
			</div>
			<div id="contact" class="tab-pane fade" aria-labelledby="contact" role="tabpanel">
                <br>
                <div class="formtitl white header" style="background-color: #aaaaaa;color: #fff;padding: 7px 5px;border: 1px solid #e1e1e1; border-top-left-radius: 3px; border-top-right-radius: 3px;">
                    <h4><egov-smc:commonLabel>Contact Details</egov-smc:commonLabel></h4>
                </div>
                <div class="profile-wrap form-area">
				<div class="form-horizontal" role="form">
					<div class="form-group">
                        <br>
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>Mobile No.</egov-smc:commonLabel><span class="mandatory"> *</span> </label>
						<div class="col-sm-7">
						<input class="form-control" value="${model.mobileNo }" name="mobileNo" placeholder="<egov-smc:commonLabel>Your Mobile No.</egov-smc:commonLabel>" type="text">
						<c:if test="${!empty modelErrors.getFieldError('mobileNo') }">
							<div class="textmandatory error">
								<c:out value="${modelErrors.getFieldError('mobileNo').getDefaultMessage() }" />
							</div>
						</c:if>		
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>Other Contact No.</egov-smc:commonLabel> </label>
						<div class="col-sm-7">
						<input class="form-control" value="${model.contactNo }" name="contactNo" placeholder="<egov-smc:commonLabel>Your Home Phone No.</egov-smc:commonLabel>" type="text">
						<c:if test="${!empty modelErrors.getFieldError('contactNo') }">
							<div class="textmandatory error"> <c:out
									value="${modelErrors.getFieldError('contactNo').getDefaultMessage() }" />
							</div>
						</c:if>
						</div>
					</div>
					<div class="form-group">
						
						<label class="col-sm-3 control-label" for="local-address"> <egov-smc:commonLabel>E-mail</egov-smc:commonLabel><span class="mandatory"> *</span> </label>
						<div class="col-sm-7">
						<input class="form-control" value="${model.email }" name="email" placeholder="<egov-smc:commonLabel>Your E-mail</egov-smc:commonLabel>" type="text">
						<c:if test="${!empty modelErrors.getFieldError('email') }">
								<div class="textmandatory error">
									<c:out value="${modelErrors.getFieldError('email').getDefaultMessage() }" />
								</div>
							</c:if>
							<div class="textmandatory"><egov-smc:commonLabel>* Indicates required field</egov-smc:commonLabel></div>
						</div>
					</div>
				</div>
			</div>
            </div>

				<!--
			<div id="social-media" class="tab-pane fade " aria-labelledby="social-media" role="tabpanel">
			<div id="address" class="tab-pane fade " aria-labelledby="address" role="tabpanel">
			<div id="contact" class="tab-pane fade " aria-labelledby="contact" role="tabpanel">
			<div id="documents" class="tab-pane fade " aria-labelledby="documents" role="tabpanel"> -->
			<input name="Action" type="hidden">
			<input name="FieldName" type="hidden">
            <div class="form text-center" style="border:none;">
                <button type="button" class="btn btn-round-lg btn-lg btn-oblue2 btn-st" onclick="location.href='<%=request.getContextPath()%>'"><i class="fa fa-arrow-left"></i>&nbsp;&nbsp;<egov-smc:commonLabel>Back</egov-smc:commonLabel></button>
                <button type="submit" class="btn btn-round-lg btn-blue2 btn-st" onclick="submitForm('Save')" ><i class="fa fa-user"></i>&nbsp;&nbsp;<egov-smc:commonLabel>Update Profile</egov-smc:commonLabel></button>
            </div>
        </div>
	</section>
</form>
