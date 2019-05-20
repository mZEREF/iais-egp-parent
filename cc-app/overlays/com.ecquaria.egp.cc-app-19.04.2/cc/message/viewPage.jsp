<%@page import="com.ecquaria.cloud.mc.api.MessageConstants"%>
<%@page import="com.ecquaria.cloud.mc.api.MessageHelper"%>
<%@page import="com.ecquaria.cloud.mc.base.SearchParam"%>
<%@page import="com.ecquaria.cloud.mc.common.constants.AppConstants"%>
<%@page import="com.ecquaria.cloud.mc.message.Message"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@ page import="java.util.Locale" %>
<%@ page import="org.springframework.web.servlet.i18n.SessionLocaleResolver" %>
<%@ page import="com.ecquaria.cloud.mc.api.ConsistencyHelper" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="ecquaria/sop/sop-smc" prefix="sop-smc"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@ page import="java.io.Serializable" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<webui:setLayout name="cc"/>
<%
	response.setContentType("text/html;charset=UTF-8");
	sop.webflow.rt.api.BaseProcessClass process =
	(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
	Message message = (Message)request.getAttribute("entity");
	String content = message.getContent();
	content = content.replaceAll("\n","");
	String base64String = new String(Base64.encodeBase64(content.getBytes()));
	Locale locale= MultiLangUtil.getSiteLocale();
	if (locale==null)
		locale=AppConstants.DEFAULT_LOCAL;
%>
<script type="text/javascript" charset="utf-8">

	function onDelete(val){
		if(MGOV.Common.checkSelected('auditUids') || val)
			SOP.Common.confirm({message:"<egov-smc:message key='deleterecordqns'>Are you sure you want to delete the selected record(s)</egov-smc:message>", func:function(){SOP.Crud.cfxSubmit('','delete', val);}})
		else
			SOP.Common.alert('<egov-smc:message key="noauditselected">No Audit selected.</egov-smc:message>');
	}

	$(function(){
		EGOV.Common.initSearchAdvance('slick-toggle', 'slickbox');
		//EGOV.Common.holdSearchAdvance('slickbox');
		$('[name="mainSearch"]').change(initSearchAdvance);
		//initSearchAdvance();
	});
	
	function printContent(){
		//window.print();
		window.location.href = "<%=request.getContextPath()%>/process/EGPCLOUD/MessagePrinterFriendly?id=" + '<c:out value="${entity.id}"></c:out>';
		//SOP.Crud.cfxSubmit('', 'print', '<c:out value="${entity.id}"></c:out>');
	}
	
	
	function submitMainSearch(){
		initSearchAdvance();
		SOP.Crud.cfxSubmit('', 'mainSearch');
	}
	
	function parseStr(cs){
		var brace = 0;
		var flag = false;
		var cs1 = '';
		for (var i = 0; i < cs.length; i++) {
			switch(cs.charAt(i)){
			case '(':
				brace++;
				if(brace<=1){
					if(i>0 && cs.charAt(i-1)==':'){
						cs1 += cs.charAt(i);
					}else
						brace--;
				}else
					brace--;
					
				break;
			case ')':
				brace--;
				if(brace==0){
					if(i<cs.length-1 && cs.charAt(i+1)==' '){
						cs1 += cs.charAt(i);
					}else if(i==cs.length-1)
						cs1 += cs.charAt(i);
					else
						brace++;
				}else
					brace++;
					
				//else if(brace>0)
					
				break;
			case ' '://kongge
				if(!flag && brace>0){
					//cs.charAt(i) = '_';
					cs1 += '@';
					cs1 += '_';
					cs1 += '@';
				}else
					cs1 += cs.charAt(i);
				break;
			default:
				cs1 += cs.charAt(i);
			}
		}
		
		return cs1;
	}
	
	function initSearchAdvance(){
		var PREFIX_SENDER_NAME = "from:";
		var PREFIX_SENDER_AGENCY = "agency:";
		var PREFIX_SENDER_SUBJECT = "subject:";
		var PREFIX_SENDER_AFTER = "after:";
		var PREFIX_SENDER_BEFORE = "before:";
		var PREFIX_SENDER_MODE = "mode:";
		var PREFIX_SENDER_IMPORTANT = "priority:";
		var value = $('[name="mainSearch"]').val();
		value = parseStr(value);//.replace(new RegExp("(:\\([^\\s]*)\\s*([^\\)]*\\))","g"), "$1@_@$2");
		var vals = value.split(' ');
		if(vals.length>0){
			$(vals).each(function(index){
				if(index==0){
					$('[name="containKey"]').val('');
					$('[name="senderName"]').val('');
					$('[name="senderAgency"]').val('');
					$('[name="subject"]').val('');
					$('[name="fromDate"]').val('');
					$('[name="toDate"]').val('');
					$('[name="mode"]').val('');
					$('[name="important"]').val('');
				}
				
				var obj = this;
				if(obj.StartWith(PREFIX_SENDER_NAME)){
					var o = obj.substring(PREFIX_SENDER_NAME.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="senderName"]').val(o);
				}else if(obj.StartWith(PREFIX_SENDER_AGENCY)){
					var o = obj.substring(PREFIX_SENDER_AGENCY.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="senderAgency"]').val(o);
				}else if(obj.StartWith(PREFIX_SENDER_SUBJECT)){
					var o = obj.substring(PREFIX_SENDER_SUBJECT.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="subject"]').val(o);
				}else if(obj.StartWith(PREFIX_SENDER_AFTER)){
					var o = obj.substring(PREFIX_SENDER_AFTER.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="fromDate"]').val(o);
				}else if(obj.StartWith(PREFIX_SENDER_BEFORE)){
					var o = obj.substring(PREFIX_SENDER_BEFORE.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="toDate"]').val(o);
				}else if(obj.StartWith(PREFIX_SENDER_MODE)){
					var o = obj.substring(PREFIX_SENDER_MODE.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					if($.trim(o))
						$('[name="mode"] :containsIgnoreCase("'+o+'")').attr("selected","");
				}else if(obj.StartWith(PREFIX_SENDER_IMPORTANT)){
					var o = obj.substring(PREFIX_SENDER_IMPORTANT.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					if($.trim(o))
						$('[name="important"] :containsIgnoreCase("'+o+'")').attr("selected","");
				}else{
					var o = obj;
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					
					$('[name="containKey"]').val(($('[name="containKey"]').val().length>0?$('[name="containKey"]').val() + " ": "") + o);
				}
				
			});
		}else{
			$('[name="containKey"]').val('');
			$('[name="senderName"]').val('');
			$('[name="senderAgency"]').val('');
			$('[name="subject"]').val('');
			$('[name="fromDate"]').val('');
			$('[name="toDate"]').val('');
			$('[name="mode"]').val('');
			$('[name="important"]').val('');
		}
	}
	
	$(document).ready(function(){
		$(".element").tooltip({
			position:"bottom center"
		});
		
		
		$("#email-content").load(function() {
			
			$(this).height($(this).contents().height() + 100);
		});
		initEmailContent();
	});

	function initEmailContent() {
		var base64Content = '<%=base64String%>';
		console.log(base64Content);
//		var content = Base64.decode(base64Content);
		var content='<%=content%>'
		var iframe = document.getElementById("email-content");
		console.log(content);
		var doc = getIframeDocument(iframe);
		doc.designMode = "on";
		doc.open();
		doc.write(content);
		doc.close();
		doc.designMode ="off";
		
	}

	function getIframeDocument(element) {
		return element.contentDocument || element.contentWindow.document;
	}
</script>

<style type="text/css">
	.viewForm{
	}
	
	input[type="text"].m{
		line-height: 30px\9;
	}

	#nav>:first-child>a{
		color: #f8ca2c;
	}
</style>

<%
	SearchParam param = (SearchParam)request.getSession().getAttribute("p_searchParam");
	if(param != null){
		Map<String,Serializable> filters = param.getFilters();
		pageContext.setAttribute("filters",filters);
	}
 
	String subject = message.getSubject();
	if(StringHelper.equals(message.getType(), MessageConstants.MODE_TYPE_SMS)){
		subject = MessageConstants.MODE_SUBJECT_LABEL_SMS;
	}
	
//	SimpleDateFormat sd = new SimpleDateFormat("dd MMM yyyy hh:mm a",Locale.US);
	if(message != null){
		Date dateTime = message.getReceivedDate();
	//	pageContext.setAttribute("dateTime", sd.format(dateTime));
		pageContext.setAttribute("dateTime", ConsistencyHelper.formatDateTime(dateTime));
	}
	String interval = MessageHelper.getDisplayIntervalTime(message.getReceivedDate());//msg.getDisplayIntervalTime();

%>
<webui:setAttribute name="title">
	<c:out value="${title }"/>
</webui:setAttribute>


<div>
    <ul class="nav nav-tabs" id="myTab">
      <li class="active">
          <a data-toggle="tab" href="#messages" title="<egov-smc:commonLabel>My Messages</egov-smc:commonLabel>" name="My Messages">
              <i class="fa fa-envelope-o"></i>
              <span><egov-smc:commonLabel>My Messages</egov-smc:commonLabel></span>
          </a>
      </li>
      <li class="">
          <a data-toggle="tab" href="#applications" onClick="SOP.Crud.cfxSubmit('', 'myApp')" title="<egov-smc:commonLabel>My Applications</egov-smc:commonLabel>" name="My Applications">
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
      <!-- 
      <c:if test="${ifPrivMyCase }">
			<li class="">
				<a data-toggle="tab" href="#myCase" onClick="SOP.Crud.cfxSubmit('', 'myCase')" title="<egov-smc:commonLabel>My Cases</egov-smc:commonLabel>" name="My Cases"> <i class="fa fa-file-text-o"></i> <span><egov-smc:commonLabel>My Cases</egov-smc:commonLabel></span>
			</a></li>
	</c:if>
	 -->
    </ul>


<form action=<%=process.runtime.continueURL()%>  name="view" Class="viewForm" method="post">
	<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
	<input type="hidden" name="crud_action_type" value="">
	<input type="hidden" name="crud_action_value" value="">
	<input type="hidden" name="crud_action_additional" value="" >
 	<div class="tab-content" id="myTabContent">
 		<div id="messages" class="tab-pane fade active in">
			<%
				String displayLabel="";
				String styleClass="";
				switch (message.getImportance()){
					case "L":
						displayLabel="Low";
						styleClass="label-success";
						break;
					case "H":
						displayLabel="High";
						styleClass="label-danger";
						break;
					case "N":
						displayLabel="Normal";
						styleClass="label-success";
						break;
					default:
						break;
				}
			%>
			<section class="form-wrap">
				<div class="formtitl white header" style="background-color: #aaaaaa;color: #fff;padding: 7px 5px;border: 1px solid #e1e1e1; border-top-left-radius: 3px; border-top-right-radius: 3px;">
					<h4><%=subject%> <span class="label <%=styleClass%>"><egov-smc:commonLabel><%=displayLabel%></egov-smc:commonLabel></span></h4>
				</div>
				<div class="form-area">
					<div class="message-area">
						<div class="msg-avator">
							<img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/message-avator.png"/>
						</div>
						<div class="msg-avator-name"><%=MessageHelper.getSenderLabel(message) %> </div>
						<div class="msg-datetime">
							<span class="msg-datecolor"><i class="fa fa-clock-o"></i> <c:out value="${dateTime}"></c:out></span>
							<br>
							<span class="msg-datecolor"><%=interval%></span>
							<br>
						</div>
						<div class="textbox">
							<iframe onload="setIframeTextStyle(this)" id="email-content" width="100%" height="100%" class="mail-mesgbox" frameborder="0"></iframe>
						</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</section>
	 		<div class="form-group-four text-center">
	 			<a href="javascript:void(0);" class="btn btn-round-lg btn-lg btn-oblue2 btn-st" onclick="SOP.Crud.cfxSubmit('', 'cancel')"><i class="fa fa-arrow-left"></i> <span><egov-smc:commonLabel>Back</egov-smc:commonLabel></span></a>
	 			<%-- <p><%=subject%></p>
	 			<egov-smc:commonLabel><%=MessageHelper.getImportanceLabel(message.getImportance()) %></egov-smc:commonLabel> --%>

				<%if(StringHelper.equals(message.getType(), MessageConstants.MODE_TYPE_EMAIL)){ %>
				<a target="_blank" href="<%=request.getContextPath()%>/process/EGPCLOUD/MessagePrinterFriendly?id=<c:out value="${entity.id}"></c:out>" class="btn btn-round-lg btn-lg btn-oblue2 btn-st"><i class="fa fa-print"></i> <span><egov-smc:commonLabel>Print</egov-smc:commonLabel></span></a>
	    		<% } %>
	 		</div>
			<br>
	 		</div>
 		</div>
</form>

</div>
<br><br><br><br><br>

<script>
    function setIframeTextStyle(i) {
        var html = i.contentWindow.document.getElementsByTagName('html');
        html[0].setAttribute('style', 'color: #404040;font-family: Arial,Helvetica,sans-serif;font-size: 14px;line-height: 1.3em;');
    }
</script>