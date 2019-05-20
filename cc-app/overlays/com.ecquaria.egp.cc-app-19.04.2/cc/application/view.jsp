<%@page import="com.ecquaria.cloud.RedirectUtil"%>
<%@page import="com.ecquaria.cloud.ServerConfig"%>
<%@page import="com.ecquaria.cloud.entity.tinyurl.TinyUrlService"%>
<%@page import="com.ecquaria.cloud.mc.api.ConsistencyHelper"%>
<%@page import="com.ecquaria.cloud.mc.application.Application"%>
<%@page import="com.ecquaria.cloud.mc.application.delegator.ApplicationDelegator"%>
<%@page import="com.ecquaria.cloud.mc.base.SearchParam"%>
<%@page import="com.ecquaria.cloud.mc.common.constants.AppConstants"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="sop.config.ConfigUtil"%>
<%@page import="sop.i18n.MultiLangUtil"%>
<%@ page import="java.io.Serializable" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/sop-smc" prefix="sop-smc"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@page pageEncoding="UTF-8" %>
<webui:setLayout name="cc"/>
<%
	response.setContentType("text/html;charset=UTF-8");
	//String lang = SessionManager.getInstance(request).getLoginInfo().getLanguage().getID();
	//String langParams = "";
	//if(!StringHelper.isEmpty(lang)){
		//langParams = "&language=" + lang;
	//}
	//pageContext.setAttribute("langParams", langParams);

	Application app = (Application)request.getAttribute("entity");
	String viewFormUrl = null;
	boolean nativeForm = ConfigUtil.getBoolean("cc.egpforms.render.native", true);
	if(app.getFormHtml() == null || !nativeForm){
		viewFormUrl = app.getFormDetailsUrl();
	}else{
		viewFormUrl = request.getContextPath() + "/process/EGPCLOUD/ViewForm?appId="+app.getId();
		viewFormUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(viewFormUrl, request);
	}
%>
<%
/*
  You can customize this default file:
  /D:/softwares/eclipse/plugins/com.ecquaria.eclipse.sit_6.1.1/WebPage.jsp.default
*/

//handle to the Engine APIs
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<script type="text/javascript">
	var serviceName = "${entity.serviceName }";

	$(function(){
		EGOV.Common.initSearchAdvance('slick-toggle', 'slickbox');
	});

	function submitMainSearch(){
		SOP.Crud.cfxSubmit('', 'mainSearch');
	}
	function doAction(callbackUrl){
		window.open(callbackUrl);
	}

	$(document).ready(function(){
		$(".element").tooltip({
			position:"bottom center"
		});

		var title = serviceName;
 		EGOV.Common.setupFormDialog({
            url: '<%=viewFormUrl%><c:out value="${langParams}" escapeXml="false"/>',
			selector: '#viewSubmittedForm',
			refresh: true,
			title: title
		});
 		EGOV.Common.setupFormDialog({
            url: '<c:out value="${entity.supportDocUrl}" escapeXml="false"/><c:out value="${langParams}" escapeXml="false"/>',
			selector: '#viewSupportDocument',
			refresh: true,
			title: title
		});
	})

	function cancel(){
		document.view.crud_action_type.value = 'cancel';
		$(document.view).submit();
	}



</script>

<style type="text/css">
	.viewForm{
	}
	h4{
		margin-top: 10px;
		margin-bottom: 10px;
	}
	 #nav>:first-child>a{
		 color: #f8ca2c;
	 }
</style>

<%
	SearchParam param = (SearchParam)request.getSession().getAttribute(ApplicationDelegator.KEY_SEARCH_FORM);
	if(param != null){
		Map<String,Serializable> filters = param.getFilters();
		pageContext.setAttribute("filters",filters);
	}
	Locale locale= MultiLangUtil.getSiteLocale();
	if (locale==null)
		locale=AppConstants.DEFAULT_LOCAL;
	String submittedDate = ConsistencyHelper.formatDateTime(app.getSubmittedDate());

	Application entity = (Application)request.getAttribute("entity");
	String url = entity.getSvcCallbackUrl();
	String originUrl = null;
	if(!StringHelper.isEmpty(url)){
		originUrl = TinyUrlService.getInstance().getOriginUrl(url.substring(url.lastIndexOf("/")+1));
		originUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(originUrl, request);
	}
	//String submittedDate = ConsistencyHelper.formatDateTime(app.getSubmittedDate());
%>
<webui:setAttribute name="title">
	<c:out value="${title }"/>
</webui:setAttribute>

<div>
	<!-- begin Nav Tabs -->
	<ul class="nav nav-tabs" id="myTab">
   		<li class="">
       		<a data-toggle="tab" href="#messages" onclick="SOP.Crud.cfxSubmit('', 'myMsg')" title="<egov-smc:commonLabel>My Messages</egov-smc:commonLabel>" name="My Messages">
				<i class="fa fa-envelope-o"></i>
				
				<span><egov-smc:commonLabel>My Messages</egov-smc:commonLabel></span>
    		</a>
		</li>
		<li class="active">
    		<a data-toggle="tab" href="#applications" title="<egov-smc:commonLabel>My Applications</egov-smc:commonLabel>" name="My Applications">
     			<i class="fa fa-pencil-square-o"></i>
      			<span><egov-smc:commonLabel>My Applications</egov-smc:commonLabel></span>
    		</a>
		</li>
		<li class="">
    		<a data-toggle="tab" href="#documents" onClick="SOP.Crud.cfxSubmit('', 'myDoc')" title="<egov-smc:commonLabel>My Documents</egov-smc:commonLabel>" name="My Documents">
				<i class="fa fa-file-text-o"></i>
				<span><egov-smc:commonLabel>My Documents</egov-smc:commonLabel></span>
       		</a>
   		</li>
	</ul>
	<!-- end Nav Tabs -->
	<div class="tab-content" id="myTabContent">
		<form name="view" class="viewForm" method="post" action=<%=process.runtime.continueURL()%> >
			<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
			<input type="hidden" name="crud_action_type" value="">
			<input name="crud_action_value" value="" type="hidden">
			<input name="crud_action_additional" value="" type="hidden">
			<input type="hidden" name="pageRecord" value="${pageNumber }" >
	      <div id="applicatons" class="tab-pane fade active in">
		    	        <!-- Form Section -->
	        <section class="form-wrap">
	            <div class="formtitle white header" style="background-color: #aaaaaa;color: #fff;padding: 7px 5px;border: 1px solid #e1e1e1; border-top-left-radius: 3px; border-top-right-radius: 3px;">
	              <h4><egov-smc:commonLabel><c:out value="${entity.serviceName }"></c:out></egov-smc:commonLabel></h4>
	            </div>
	            <div class="form-area">
			        <article>
		              <table class="table table-striped table-condensed">
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Application No.</egov-smc:commonLabel>:</td>
		                  <td class="emailfix"><c:out value="${entity.no }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Submitted Date/Time</egov-smc:commonLabel>:</td>
		                  <td><%=submittedDate %></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Status</egov-smc:commonLabel>:</td>
		                  <td><div class="statuslabel label-warning"><egov-smc:commonLabel><c:out value="${entity.status }"/></egov-smc:commonLabel></div></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Service</egov-smc:commonLabel>:</td>
		                  <td class="emailfix"><egov-smc:commonLabel><c:out value="${entity.serviceName }"/></egov-smc:commonLabel></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Agency</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.agencyName }"/></td>
		                </tr>
		              </table>
		            </article>
			        <article>
		              <h3><egov-smc:commonLabel>Applicant Particular</egov-smc:commonLabel></h3>
		              <table class="table table-striped table-condensed">
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Applicant ID</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.applicantId }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Name</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.applicantName }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Address</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.applicantAddress }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Mailing Address</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.applicantMailAddress }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Email</egov-smc:commonLabel>:</td>
		                  <td class="emailfix"><c:out value="${entity.applicantEmail }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Mobile No.</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.applicantMobile}"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Telephone No.</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.applicantTel}"/></td>
		                </tr>
		              </table>
		            </article>
			        <article>
		              <h3><egov-smc:commonLabel>Submitter Particular</egov-smc:commonLabel></h3>
		              <table class="table table-striped table-condensed">
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Submitter ID</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.submitterId }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Name</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.submitterName }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Address</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.submitterAddress }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Mailing Address</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.submitterMailAddress }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Email</egov-smc:commonLabel>:</td>
		                  <td class="emailfix"><c:out value="${entity.submitterEmail }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Mobile No.</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.submitterMobile }"/></td>
		                </tr>
		                <tr>
		                  <td class="text-muted col-sm-5 col-sm-offset-1 col-xs-3"><egov-smc:commonLabel>Telephone No.</egov-smc:commonLabel>:</td>
		                  <td><c:out value="${entity.submitterTel }"/></td>
		                </tr>
		              </table>
		            </article>
	            </div>
	        </section>
			  <div class="form-group-four text-center">
				  <a href="javascript:void(0);" class="btn btn-round-lg btn-lg btn-oblue2 btn-st" onclick="cancel()"><i class="fa fa-chevron-left"></i> <span><egov-smc:commonLabel>Back</egov-smc:commonLabel></span></a>
				  <a href="<%=request.getContextPath()%>/process/EGPCLOUD/ApplicationPrinterFriendly?id=<c:out value="${entity.id}"/>" target="_blank" class="btn btn-round-lg btn-lg btn-oblue2 btn-st"><i class="fa fa-print"></i> <span><egov-smc:commonLabel>Print</egov-smc:commonLabel></span></a>
				  <% if(!ServerConfig.getInstance().isHModeEnable()||(ServerConfig.getInstance().isHModeEnable()&&(app.getEditEnable() != null && app.getEditEnable() !=Application.IS_EDIT_ENABLE_NO))){%>
				  <c:if test="${not empty entity.svcCallbackUrl && (entity.status != 'Approved')}">
					  <c:set var="actionValue" value="Edit Application"/>
					  <c:if test="${not empty entity.action}">
						  <c:set var="actionValue" value="${entity.action}"/>
					  </c:if>
					  <a href="javascript:void(0);" class="btn btn-round-lg btn-lg btn-oblue2 btn-st" title='<egov-smc:commonLabel>${actionValue}</egov-smc:commonLabel>' onclick="doAction('<%=originUrl %>')"><i class="fa fa-pencil-square-o"></i> <span><egov-smc:commonLabel>${actionValue}</egov-smc:commonLabel></span></a>
				  </c:if>
				  <%}%>
				  <c:if test="${not empty entity.supportDocUrl }">
					  <a href="javascript:void(0);" id="viewSupportDocument" class="btn btn-round-lg btn-lg btn-oblue2 btn-st"><i class="fa fa-file-text-o"></i> <span><egov-smc:commonLabel>View Documents</egov-smc:commonLabel></span></a>
				  </c:if>
				  <%if(!StringHelper.isEmpty(viewFormUrl)){ %>
				  <a href="javascript:void(0);" id="viewSubmittedForm" class="btn btn-round-lg btn-blue2 btn-st"><i class="fa fa-list-alt"></i> <span><egov-smc:commonLabel>View Form</egov-smc:commonLabel></span></a>
				  <%} %>
			  </div>
			  <br>
	      </div>
		</form>
	</div>
</div>
<br><br><br><br><br><br>