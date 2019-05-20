<%@page import="com.ecquaria.cloud.ServerConfig"%>
<%@page import="com.ecquaria.cloud.RedirectUtil"%>
<%@page import="com.ecquaria.cloud.client.rbac.UserService"%>
<%@page import="com.ecquaria.cloud.client.task.Task"%>
<%@page import="com.ecquaria.cloud.entity.application.AppProcessingHistoryEntity"%>
<%@page import="com.ecquaria.cloud.entity.application.MessageHistoryService"%>
<%@page import="com.ecquaria.cloud.entity.svcreg.ServiceRegistryService"%>
<%@page import="com.ecquaria.egov.core.helper.SupportedDocHelper"%>
<%@page import="com.ecquaria.egov.core.svcreg.DocumentSetup"%>
<%@page import="com.ecquaria.egov.core.svcreg.ServiceRegistry"%>
<%@page import="com.ecquaria.egp.api.AppStatus"%>
<%@page import="com.ecquaria.egp.core.application.AppInParam"%>
<%@page import="com.ecquaria.egp.core.application.AppProcessingHistory"%>
<%@page import="com.ecquaria.egp.core.application.Application"%>
<%@page import="com.ecquaria.egp.core.application.MessageHistory"%>
<%@page import="com.ecquaria.egp.core.application.controller.ApplicationViewController"%>
<%@page import="com.ecquaria.egp.core.bat.AppStatusHelper"%>
<%@page import="com.ecquaria.egp.core.bat.pendingcallback.PendingCallbackHelper"%>
<%@page import="com.ecquaria.egp.core.common.constants.MessageTemplateConstants"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@page
		import="com.ecquaria.egp.core.docUpload.DocUpload"%>
<%@page import="com.ecquaria.egp.core.helper.ConsistencyHelper"%>
<%@page import="com.ecquaria.egp.core.helper.EGPCommonHelper"%>
<%@page
		import="com.ecquaria.egp.core.helper.ServiceRegistryHelper"%>
<%@page import="com.ecquaria.egp.core.internaldoc.InternalDocUpload"%>
<%@page import="com.ecquaria.egp.core.payment.Payment"%>
<%@page import="ecq.commons.helper.ArrayHelper"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="ecq.crud.helper.View"%>
<%@page import="sop.config.ConfigUtil"%>
<%@page import="sop.i18n.MultiLangUtil"%>
<%@page import="sop.usergroup.UserGroup"%>
<%@page import="sop.util.MessageTemplateHelper"%>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ecquaria.cloud.endorsement.EndorsementTask" %>
<%@ page import="egov.application.flow.ProcessEndorseTask" %>
<%@ page import="com.ecquaria.egov.core.common.constants.AppConstants" %>
<%@page isELIgnored="true"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt_rt"%>
<%@ taglib uri="ecquaria/sop/sop-htmlform" prefix="sop-htmlform"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="ecquaria/sop/sop-smc" prefix="sop-smc"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<%
	response.setContentType("text/html;charset=UTF-8");
%>
<%
	String webContext = ConfigUtil.getWebBathPath(request);
%>
<script
		src="<%=webContext %>/javascripts/egov/bootstrap.js"
		type="text/javascript"></script>
<script
		src="<%=webContext %>/javascripts/egov/tinymce/js/tinymce/tinymce.min.js"
		type="text/javascript"></script>
<script src="<%=webContext %>/javascripts/egov/tinymce/js/tinymce/jquery.tinymce.min.js" type="text/javascript"></script>

<%
	Application app = (Application) request.getAttribute("entity");
	String appNo = app.getAppNo();
	String currentStatus = app.getAppStatus();
	String displayCurrentStatus = AppStatusHelper.getInstance().getAppStatus(currentStatus).getLabel();
	pageContext.setAttribute("displayCurrentStatus", displayCurrentStatus);
	String formInstId = app.getFormInstId();
	String continueCode = (String)request.getAttribute("continueCode");
	pageContext.setAttribute("contextPath", request.getContextPath());
	long appId = app.getAppId();
	PendingCallbackHelper.getCallbackUrl(appId);
	String callbackUrl = PendingCallbackHelper.getCallbackUrl(appId);
	String viewFormUrl = (String)request.getAttribute("viewFormUrl");
	if(StringHelper.isEmpty(viewFormUrl)){//current user is counter staff
		//viewFormUrl = request.getContextPath() + "/process/EGPCLOUD/AppViewFormLoad?appNo=" + appNo;
		viewFormUrl = app.getFormDetailsUrlInAppView();
	}else{
		viewFormUrl = request.getContextPath() + viewFormUrl + "?appNo=" + appNo;
	}

	viewFormUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(viewFormUrl, request);
%>

<%! static String MSG_COUNT_LEFT = "{0} days left";
	static String MSG_COUNT_OVERDUE = "{0} days overdue";
%>

<script type="text/javascript">
    var LIMIT_CHARS = 160;
    $(function(){
        initTinyMCE();
    });

    function initTinyMCE(){
        tinymce.init({
            selector: ".sm-editable",
            plugins: ["advlist autolink lists"],
            inline: true
        });
    }

    function reInitTinyMCE(){
        tinymce.remove();
        initTinyMCE();
    }

    function getSelectedPage(){
        var selectedPageEle = document.getElementById('selectedPage');
        var selectedPage;
        if(selectedPageEle == null)
            selectedPage == 0;
        else
            selectedPage = 	selectedPageEle.value;
        return selectedPage>0? selectedPage : 0;
    }


    SOP.Common.load(function(){
        SOP.Common.setupTabs('rotate',{selected: getSelectedPage()});
        initDialog();
    });

    var ckeditorOptions = {
        resize_enabled : false,
        toolbar_Full:[['Bold','Italic','Underline'],['NumberedList','BulletedList'],['Link']],
        toolbarCanCollapse : false,
        toolbarLocation : 'bottom',
        entities : false,
        autoUpdateElement : true,
        height:100,
        removePlugins:'elementspath'
    };
    <egov-smc:messageTemplate var="messagingServiceMsg" key="egp.application.view.title.MessagingService." default="Messaging Service" />
    <egov-smc:messageTemplate var="supportDocServiceMsg" key="egp.application.view.title.SupportDocService." default="SupportDoc History" />
    var initDialog = function(){
        //CKEDITOR.replace('test', ckeditorOptions);
        $('#last_content').dialog({
            'title':'<egov-core:escapeJavaScript value="${messagingServiceMsg}" />',
            'resizable': false,
            'dialogClass':'appMessageDialog form-content',
            'modal': true,
            'show': 'blind',
            'autoOpen': false,
            'open':function(){
                $('#last_content').tabs({selected:0});
                $(this).find('.alert-success, .alert-fail').hide();
                $(this).find('small.error').html('').hide();
            },
            'width':650,
            close: closeEditor
        });
        $('#last_supportDoc').dialog({
            'title':'<egov-core:escapeJavaScript value="${supportDocServiceMsg}" />',
            'resizable': false,
            'dialogClass':'appMessageDialog form-content',
            'modal': true,
            'show': 'blind',
            'autoOpen': false,
            'open':function(){
                $('#last_content').tabs({selected:0});
                $(this).find('.alert-success, .alert-fail').hide();
                $(this).find('small.error').html('').hide();
            },
            'width':650,
            close: closeEditor
        });
        <egov-smc:messageTemplate var="confirmMsg" key="egp.application.view.title.confirm" default="Confirm" />
        <egov-smc:messageTemplate var="cancelMsg" key="egp.common.cancel" default="Cancel" />
        <egov-smc:messageTemplate var="okMsg" key="egp.common.ok" default="OK" />
        <egov-smc:messageTemplate var="uploadMsg" key="egp.common.upload" default="Upload" />
        <egov-smc:messageTemplate var="updateMsg" key="egp.common.update" default="Update" />
        $('#confirm_content').dialog({
            'title':'<egov-core:escapeJavaScript value="${confirmMsg}" />',
            'resizable': false,
            'modal': true,
            'show': 'blind',
            'width':650,
            'autoOpen': false,
            'open':function(){
                $('#confirm_content').tabs({selected:0});
                $(this).find('.alert-success, .alert-fail').hide();

                var updateStCode = $("#selectedStatus").val();
                var continueCodeStr = '<%=continueCode%>';

                if(continueCodeStr.indexOf(updateStCode) != -1){
                    $("#updateStatusComments").hide();
                    $(this).find('ul').hide();
                }else{
                    $("#updateStatusComments").show();
                    $(this).find('ul').show();
                };
                $(this).find('small.error').html('').hide();
            },
            'dialogClass':'form-content appMessageDialog',
            "buttons": [ { text: '<egov-core:escapeJavaScript value="${cancelMsg}" />', click: function() { displayConfirmContent(); }, 'class': 'search-reset' },
                { text: '<egov-core:escapeJavaScript value="${okMsg}" />', click: function() {submitConfirmContent();}, 'class': 'dialog-button'}
            ],
            close: closeEditor
        });

    };

    <%if(!StringHelper.isEmpty(formInstId)) {%>
    $(document).ready(function() {
        EGP.Common.setupFormDialog({
            url:'<%=viewFormUrl%>',
            selector: '#openViewContent',
            refresh: true
        })

        $("#downFormPdf").click(function() {
            var pdfFormIframe = $("#pdfFormIframe");
            if(window.downloadPDFURL) {
                downloadPDF(window.downloadPDFURL);
                return;
            }
            if("processing" == $(this).data("status")){
                return;
            }
            $(this).data("status","processing");
            pdfFormIframe.attr("src", '<%=request.getContextPath() %>/process/EGPCLOUD/AppViewFormLoad?appNo=<%=appNo%>&pfMode=true');
            runAfterReady(pdfFormIframe, getPDF);
        });
    });
    <%}%>
    function getPDF() {
        var pdfFormIframe = $("iframe#pdfFormIframe")[0];
        //pdfFormIframe.contentWindow.$(".error_placements").remove();
        pdfFormIframe.contentWindow.$("script").remove();
        $.post('<%=request.getContextPath()%>/egovforms/RenderPDF.jsp',
            'content=' + encodeURIComponent(pdfFormIframe.contentWindow.document.documentElement.outerHTML)+'&url='+pdfFormIframe.contentWindow.location.href,
            function(data) {
                downloadPDF($(data).find('result').text());
            }, 'xml');
    }
    function downloadPDF(url) {
        window.downloadPDFURL = url;
        window.location = '<%=request.getContextPath() %>/process/EGPCLOUD/ApplicationView/DownLoadFormPDF?downloadPDFURL='+encodeURIComponent(url)+'&appNo=<%=appNo%>';
        $("#downFormPdf").data("status","complete");
    }
    function runAfterReady(iframe, func) {
        window.__eGovFormPfModeReady = func;
    }

    function openLastContent(){
        $('#last_content').dialog("open");
    };



    function closeEditor(){
        if(tinymce.focusedEditor){
            reInitTinyMCE();
        }
    }

    function updateStatusConfirm(){
        if(validateInternalComments()){
            $("[name='selectedPage']").val(4);

            var action = $("#selectedStatus").val();
            var appNo = '<%=appNo%>';
            var currentSt = '<%=displayCurrentStatus%>';
            var updateSt = $("#selectedStatus").find("option:selected").text();

            var updateStCode = $("#selectedStatus").val();
            var continueCodeStr = '<%=continueCode%>';


            //when select Pending Endorsement, checkbox need to select at least one;
            var status = $("#selectedStatus").find("option:selected").text();
            if (status == 'Pending Endorsement') {
                var endorseIds = $('#innerAccordiion9 :checkbox[name="endorsementConfigIds"]:checked');
                var checked = endorseIds.length == 0? false:true;
                if (!checked) {
                    SOP.Common.alert("Please select officer first!");
                } else {
                    if(continueCodeStr.indexOf(updateStCode) != -1){
                        $("#updateStatusComments").show();
                    }else{
                        $("#updateStatusComments").hide();
                    }

                    $("#updateStatusDis").html(updateSt);

                    $('#confirm_content').dialog("open");
                }
            } else {
                if(continueCodeStr.indexOf(updateStCode) != -1){
                    $("#updateStatusComments").show();
                }else{
                    $("#updateStatusComments").hide();
                }

                $("#updateStatusDis").html(updateSt);

                $('#confirm_content').dialog("open");
            }

        }
    }

    function validateInternalComments(){
        var length = <%=AppProcessingHistoryEntity.FIELD_INTERNAL_COMMENTS_LEN%>;
        if($("textarea[name='internalComments']").val().length > length){
            $("textarea[name='internalComments']").nextAll('small.error').html('Input value exceeds max. size ['+length+']');
            $("textarea[name='internalComments']").nextAll('small.error').show();
            return false;
        }else{
            $("textarea[name='internalComments']").nextAll('small.error').hide();
            return true;
        }
    }


    function displayConfirmContent(){
        $('#confirm_content').dialog('close');
    }

    function submitConfirmContent(){
        $("[name='selectedPage']").val(3);
        var action = $("#selectedStatus").val();
        $("#applicationForm [name='comments']").val($('#comments').val());
        performAction(action);
    }

    function updateStatus(){
        $("[name='selectedPage']").val(3);

        var action = $("#selectedStatus").val();
        var appNo = '<%=appNo%>';
        var currentSt = '<%=displayCurrentStatus%>';
        var updateSt = $("#selectedStatus").find("option:selected").text();
        var messageStr = "<egov-smc:commonLabel>Application No.</egov-smc:commonLabel>:"+appNo +"<br>"+"<egov-smc:commonLabel>Current Status</egov-smc:commonLabel>:"+currentSt+"<br>"+"<egov-smc:commonLabel>Update Status</egov-smc:commonLabel>:"+updateSt+"<br>"+'<egov-smc:messageTemplate key="egp.application.view.update.confirm" default="Are you sure you want to update status?" />';
        SOP.Common.confirm({
            message: messageStr,
            func: function(){
                performAction(action);
            }
        });
    }

    function performAction(action) {
        document.applicationForm.Action.value = action;
        document.applicationForm.submit();
        return true;
    }


    $(function(){
        SOP.Common.setupAccordion('innerAccordiion1');
        SOP.Common.setupAccordion('innerAccordiion2');
        SOP.Common.setupAccordion('innerAccordiion3');
        SOP.Common.setupAccordion('innerAccordiion4');
        SOP.Common.setupAccordion('innerAccordiion5');
        SOP.Common.setupAccordion('innerAccordiion6');
        SOP.Common.setupAccordion('innerAccordiion7');
        SOP.Common.setupAccordion('innerAccordiion8');
        SOP.Common.setupAccordion('innerAccordiion10')
    });

    function changeMsgServiceTab(o) {
        // activate/deactivate tabs
        var sib = $(o).siblings();

        $(o).addClass("msgTabActive");
        for (var i = 0; i < sib.length; i++) {
            $(sib[i]).addClass("msgTabInactive");
            $(sib[i]).removeClass("msgTabActive");
        }
        // show/hide panel
        href = $(o).attr("href");
        idx = href.split("-")[1];
        c = $(".msgTabNavigation").children();
        for ( var i = 0; i < c.length; i++ ) {
            if ( i != idx )
                $("#msgTab-" + i).hide();
        }
        $("#msgTab-" + idx).show();
    }

    function continueUrl(){
        var continueUrl = '<%=callbackUrl%>';
        window.location.href=continueUrl;
    }
</script>

<style type="text/css">
	#formDialogSpace{
		overflow-y: hidden !important;
	}
	#process_content, #url_content {
		display: none;
	}

	.ui-widget-content a {
		color: #1F92FF;
	}

	#info_content a {
		font-size: 120%;
	}

	#last_content a {
		font-size: 120%;
	}

	h2.fontClass {
		font-weight: inherit;
		font-size: 22px;
	}

	h4.fontClass {
		font-weight: inherit;
		font-size: 18px;
	}

	.test-class {
		margin-top: 1px;
	}

	.cke_skin_kama .cke_wrapper {
		background-color: white;
		padding: 0px;
	}

	#declaration_content_url, #tc_content_url, #glossary_content_url {
		display: none;
	}

	#msgTab-0 #cke_contents_declaration {
		border: 1px solid;
		border-color: #d0d0d0;
		width: 370px;
		height: 100px;
		float: left;
	}

	#cke_declaration {
		border: 0px;
		padding: 0px;
	}

	.subcontainer {
		background-color: #f9f9f9;
		padding: 10px;
	}

	.subcontainer h3 {

	}

	#send-msg-content table.form tr td:first-child, table.form tr td:first-child
	{
		width: 20%;
	}

	#msgTab-0 table.form tr td:first-child, table.form tr td:first-child {
		width: 20%;
	}

	.msgTabNavigation a {
		margin-right: 5px;
		color: #eee;
		padding: 5px;
	}

	.msgTabActive {
		color: #333 !important;
		border: 1px solid #999;
	}

	.msgTabInactive {
		font-weight: normal;
	}

	.msgTabNavigation {
		font-size: 20px;
	}

	#simplemodal-container a {
		color: #999;
	}

	.alert-success1 {
		display: none;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 20px;
		position: auto;
		border-radius: 5px 5px 5px 5px;
		box-shadow: 0 1px 2px #CCCCCC;
		cursor: pointer;
		font-weight: bold;
		text-shadow: 0 0 0 transparent;
		background-color: #9FE882;
		border: 1px solid #81C069;
		color: #333333;
		width: 90%;
	}

	.alert-success1>p:first-child {
		background-image:
				url("common_files/blue/images/general/icon-accept.png");
		background-repeat: no-repeat;
		text-indent: 20px;
	}

	#msgTab-0 table.form tr td:first-child, table.form tr td:first-child {
		width: 20%;
	}

	.batContent table.form tr td:first-child, table.form tr td:first-child {
		color: #2D3D85;
		font-weight: bold;
		text-align: right;
		vertical-align: top;
		width: 30%;
	}

	.batContent table.invisible tr td, table.invisible tr td {
		background-color: rgba(0, 0, 0, 0);
		border: 0 none;
		padding: 5px;
	}

	.batContent table.invisible tr td, table.invisible tr td {
		background-color: rgba(0, 0, 0, 0);
		border: 0 none;
		padding: 5px;
	}

	.appMessageDialog input.button[type="button"] {
		border-radius: 3px;
	}

	.btn-wrap {
		border-top: 1px solid #DDDDDD;
		padding: 10px 0 0;
		width: 100%;
	}

	.btn-wrap input.button {
		float: right;
		margin: 0 2px;
	}

	.tinymce-content {
		border: 1px solid #D0D0D0;
		height: 200px;
		overflow-y: scroll;
		padding: 10px;
		width: 420px;
		font-weight: normal;
	}

	#tinymce-content, #tinymce-content2 {
		clear: right;
	}

	div.form-content .tinymce-content .text {
		float: none;
		padding: 0;
		font-weight: normal;
	}

	.form-content div.history-content span.text {
		background: none;
		padding: 0;
	}

	.pop-up-header {
		border-bottom: 1px solid #EEEEEE;
		padding: 9px 15px;
	}

	.pop-up-header .close {
		margin-top: 2px;
	}

	button.close {
		background: none repeat scroll 0 0 rgba(0, 0, 0, 0);
		border: 0 none;
		cursor: pointer;
		padding: 0;
	}

	.close {
		color: #000000;
		float: right;
		font-size: 20px;
		font-weight: bold;
		line-height: 20px;
		opacity: 0.2;
		text-shadow: 0 1px 0 #FFFFFF;
	}

	.pop-up-body {
		max-height: 400px;
		overflow-y: auto;
		padding: 15px;
		position: relative;
	}

	.pop-up-footer {
		background-color: #F5F5F5;
		border-radius: 0 0 6px 6px;
		border-top: 1px solid #DDDDDD;
		box-shadow: 0 1px 0 #FFFFFF inset;
		margin-bottom: 0;
		padding: 14px 15px 15px;
		text-align: center;
	}

	.pop-up-footer {
		box-shadow: none;
	}

	.pop-up-footer:before, .pop-up-footer:after {
		content: "";
		display: table;
		line-height: 0;
	}

	.pop-up .content-body {
		background: none repeat scroll 0 0 #E2E2E2;
		margin-top: 20px;
		border-radius: 6px;
	}

	.content-body div.field {
		float: left;
		padding-top: 10px;
		text-align: right;
		width: 180px;
		font-size: 14px;
	}

	select, input[type="file"] {
		height: 30px;
		line-height: 30px;
	}

	.content-body div.control {
		padding-top: 5px;
		font-size: 14px;
		margin-left: 200px;
	}

	span.filename {
		display: block;
	}

	span.filename a img {
		margin-top: 3px;
	}

	a.red {
		background-color: #c91a1a;
		background-image: -moz-linear-gradient(center top, #c91a1a, #820f0f);
		border-color: #820f0f #bc1f1f #d93131;
		border-radius: 5px 5px 5px 5px;
		border-style: solid;
		border-width: 1px;
		box-shadow: 0 1px 0 0 #f1abab inset;
		color: #fff;
		cursor: pointer;
		font: 13px Tahoma, Arial, Verdana, sans-serif;
		margin: 5px 2px;
		outline: medium none;
		padding: 3px 12px;
		text-align: center;
		vertical-align: middle;
		white-space: nowrap;
	}
</style>
<%
	sop.webflow.rt.api.BaseProcessClass process = (sop.webflow.rt.api.BaseProcessClass) request
			.getAttribute("process");
%>


<webui:setAttribute name="title">
	<egov-smc:titleLabel>Application Details</egov-smc:titleLabel>
</webui:setAttribute>
<egov-core:breadcrumb>
	<egov-core:breadcrumbEntry label="Home" url="../egov/process/EGPCLOUD/Home"/>
	<egov-core:breadcrumbEntry label="Applications" url="../egov/process/EGPCLOUD/ApplicationList"/>
	<egov-core:breadcrumbEntry label="Application Details"></egov-core:breadcrumbEntry>
</egov-core:breadcrumb>
<h1>
	<egov-smc:commonLabel>Application Details</egov-smc:commonLabel>
</h1>
<c:if test="${not empty errorMessage}">
	<div class="alert-error">
		<p>
			<c:out value="${errorMessage}" escapeXml="false"></c:out>
		</p>
	</div>
</c:if>
<c:if test="${not empty message}">
	<div class="alert-success">
		<p>
			<c:out value="${message}" escapeXml="false"></c:out>
		</p>
	</div>
</c:if>
<div class="onecolumn">
	<div id="rotate">
		<div class="header">
			<div class="tab-header">
				<ul>
					<li><a href="#fragment-1"><span><egov-smc:commonLabel>Info</egov-smc:commonLabel></span></a></li>
					<li><a href="#fragment-2"><span><egov-smc:commonLabel>Documents</egov-smc:commonLabel></span></a></li>
					<li><a href="#fragment-3"><span><egov-smc:commonLabel>Payment</egov-smc:commonLabel></span></a></li>
					<li><a href="#fragment-4"><span><egov-smc:commonLabel>Processing</egov-smc:commonLabel></span></a></li>
				</ul>
			</div>
			<!-- TODO DQ download pdf -->
			<%-- <img style="float: right; margin-top: 10px; cursor: pointer;"
				title="Download pdf"
				src="<egov-core:webURL with_theme='true' source='images/pdf-icon-24.png'/>"
				id="downFormPdf"> --%>
			<c:set value="false" var="needHistory"></c:set>
			<c:set value="false" var="needEmail"></c:set>
			<c:set value="false" var="needSMS"></c:set>
			<c:set value="display:none;" var="cssHistory"></c:set>
			<c:set value="display:none;" var="cssEmail"></c:set>
			<c:set value="display:none;" var="cssSMS"></c:set>
			<c:if test="${not empty entity.applicantEmail}">
				<c:set value="true" var="needHistory"></c:set>
				<c:set value="true" var="needEmail"></c:set>
				<c:set value="" var="cssHistory"></c:set>
				<c:set value="" var="cssEmail"></c:set>
			</c:if>
			<c:if test="${not empty entity.applicantMobile}">
				<c:set value="true" var="needHistory"></c:set>
				<c:set value="true" var="needSMS"></c:set>
				<c:set value="" var="cssHistory"></c:set>
				<c:set value="" var="cssSMS"></c:set>
			</c:if>
			<c:if test="${needHistory }">
				<img style="float: right; margin-top: 10px; cursor: pointer;"
					 title="Messaging Service"
					 src="<egov-core:webURL with_theme='true' source='images/message-icon-24.png'/>"
					 onclick="openLastContent();">
			</c:if>
			<%if(!StringHelper.isEmpty(callbackUrl)){ %>
			<img style="float: right; cursor: pointer;" title="Continue"
				 src="<egov-core:webURL with_theme='true' source='images/but-edit.png'/>"
				 onclick="continueUrl();">
			<%} %>
		</div>


		<div>
			<form id = "applicationForm" class = "form" name="applicationForm" method = "post" action = "<%=process.runtime.continueURL()%>" enctype="multipart/form-data">
				<sop-htmlform:hidden name="selectedPage" elementId="selectedPage"
									 value="${page}" />
				<sop-htmlform:hidden name="stageName" value="${stageName}" />
				<sop-htmlform:hidden name="appNo" value="${appNo }" />
				<sop-htmlform:hidden name="comments" />
				<input type="hidden" name="Action" />
				<input type="hidden" name="sopEngineTabRef"
					   value="<%=process.rtStatus.getTabRef()%>" />
				<sop-htmlform:hidden name="crud_action_type" value="" />
				<sop-htmlform:hidden name="crud_action_value" value="" />
				<sop-htmlform:hidden name="crud_action_additional" value="" />

				<div id="fragment-1" class="tabFrame">
					<div class="inner-accordion" id="innerAccordiion1">
						<div class="header">
							<span class="collapse" style="height: 20px;"><egov-smc:commonLabel>Submission Details</egov-smc:commonLabel></span>
						</div>
						<div class="content">
							<input type="hidden"
								   value='<c:out value="${entity.appId}"></c:out>'
								   name="entityUids" /> <input type="hidden" value=""
															   name="taskid">
							<table class="table2 view-table"
								   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
								<tbody>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; width: 50%; border-color: #d0d0d0;">
										<egov-smc:commonLabel>No.</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${entity.appNo}"></c:out>
									</td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<egov-smc:commonLabel>Digital Service</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${entity.svcName}"></c:out>
									</td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<egov-smc:commonLabel>Submission Date</egov-smc:commonLabel>
									</td>
									<%
										String date = "";
										if (app != null) {
											Date dateSubmitted = app.getDateSubmitted();
											if (dateSubmitted != null) {
												date = ConsistencyHelper.formatDateTime(dateSubmitted);
											}
										}
										pageContext.setAttribute("date", date);
									%>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${date}"></c:out>
									</td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<egov-smc:commonLabel>Current Status</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<egov-smc:commonLabel><c:out value="${displayCurrentStatus}"></c:out></egov-smc:commonLabel>
									</td>
								</tr>
								<%if(app.getLapseDate()!=null){ %>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<egov-smc:commonLabel>Grace Expiry Date</egov-smc:commonLabel>
									</td>
									<%
										String lapseDateStr = "";
										if (app != null) {
											Date lapseDate = app.getLapseDate();
											if (lapseDate != null) {
												lapseDateStr = ConsistencyHelper.formatDate(lapseDate);
											}
										}
										pageContext.setAttribute("lapseDateStr", lapseDateStr);
									%>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${lapseDateStr}"></c:out>
									</td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<egov-smc:commonLabel>Days Lapsed</egov-smc:commonLabel>
									</td>
									<%
										Date now = new Date();
										int lapsedDays = 0;
										if (app != null) {
											Date lapseDate = app.getLapseDate();
											if (lapseDate != null) {
												if(now.after(lapseDate)){
													lapsedDays = ConsistencyHelper.daysBetween(lapseDate, now);
												}
											}
										}
										pageContext.setAttribute("lapsedDays", lapsedDays);
									%>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${lapsedDays}"></c:out>
									</td>
								</tr>
								<%} %>
								<%	Date dateCreated = null;
									int slaInDays = -1;
									int slaAlertInDays = -1;
									int remainCount = -1;
									Date slaDateCompleted = null;
									long daysDifference = -1;
									long daysDifferenceRatio = -1;

									Task task = (Task)request.getAttribute("taskEntity");
									String displaySLA = "";
									if(task!=null)// != null ? task.getSlaInDays() : slaInDays
										slaInDays = task.getSlaInDays() != null ? task.getSlaInDays() : 0;
									if(task!=null && slaInDays != 0){
										if(!task.isCompleted()){
											dateCreated = task.getDateCreated();
											slaAlertInDays = task.getSlaAlertInDays() != null ? task.getSlaAlertInDays() : 0;
											slaDateCompleted = task.getSlaDateCompleted();
											//slaDateCompleted = slaDateCompleted==null?new Date():slaDateCompleted;

											remainCount = task.getSlaRemainInDays() != null ? task.getSlaRemainInDays() : (slaInDays -1);
											daysDifference = slaInDays - remainCount;

											String slaPanelClass = "";
											if (daysDifference < slaAlertInDays || slaAlertInDays > slaInDays){slaPanelClass = "progress-bar-green";}// -- GREEN
											else if(daysDifference > slaInDays){slaPanelClass = "progress-bar-red";}// -- RED
											else {slaPanelClass = "progress-bar-yellow";}// -- YELLOW

											String countStr = null;
											if(remainCount >= 0) {
												countStr = MessageTemplateHelper.getMessage(
														"MSG_COUNT_LEFT",
														new String[] { String.valueOf(remainCount) },
														MSG_COUNT_LEFT);
											} else {
												countStr = MessageTemplateHelper.getMessage(
														"MSG_COUNT_OVERDUE",
														new String[] { String.valueOf(-remainCount) },
														MSG_COUNT_OVERDUE);
											}

											//    sb.append("<td><div class=\"").append(slaPanelClass).append("\">").append(countStr).append(" (").append(daysDifference).append(" / ").append(slaInDays).append(")</div></td>");
											displaySLA = countStr + "("+daysDifference + "/" + slaInDays + ")";
								%>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										SLA
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><div
											style="width: 50%;" class="<%=slaPanelClass%>">
										<%=displaySLA%></div></td>
								</tr>
								<%
								}else{
								%>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										SLA
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">N/A</td>
								</tr>
								<%
									}
								}else{
								%>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										SLA
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">N/A</td>
								</tr>
								<%
									}

								%>
								</tbody>
							</table>
							<%if(!StringHelper.isEmpty(formInstId)) {%>
							<div class="action-buttons" align="center"
								 style="margin-left: 0;">
								<button style="width: 150px;" id="openViewContent">
									<egov-smc:commonLabel>View Form</egov-smc:commonLabel>
								</button>
							</div>
							<%}%>
							<br class="clear" />
						</div>
					</div>

					<div class="inner-accordion" id="innerAccordiion2">
						<div class="header">
							<span class="collapse" style="height: 20px;"><egov-smc:commonLabel>Applicant Details</egov-smc:commonLabel></span>
						</div>

						<div class="content">
							<table class="table2 view-table"
								   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
								<tbody>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; width: 50%; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Applicant  ID" /> --%>
										<egov-smc:commonLabel>Applicant ID</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
											value="${entity.applicantId}"></c:out></td>
								</tr>
								<tr>
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Name" /> --%>
										<egov-smc:commonLabel>Name</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
											value="${entity.applicantName}"></c:out></td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Address" /> --%>
										<egov-smc:commonLabel>Address</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
											value="${entity.applicantAddress}"></c:out></td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Mailing Address" /> --%>
										<egov-smc:commonLabel>Mailing Adress</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
											value="${entity.applicantMailAddress }"></c:out></td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<egov-smc:commonLabel>E-mail</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
											value="${entity.applicantEmail}"></c:out></td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Mobile No." /> --%>
										<egov-smc:commonLabel>Mobile No.</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
											value="${entity.applicantMobile}"></c:out></td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<egov-smc:commonLabel>Phone No.</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
											value="${entity.applicantTel}"></c:out></td>
								</tr>
								</tbody>
							</table>
							<br class="clear" />
						</div>

					</div>


					<div class="inner-accordion" id="innerAccordiion3">
						<div class="header">
							<span class="collapse" style="height: 20px;"><egov-smc:commonLabel>Submitter Details</egov-smc:commonLabel></span>
						</div>


						<div class="content">
							<table class="table2 view-table"
								   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
								<tbody>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; width: 50%; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Submitter ID" /> --%>
										<egov-smc:commonLabel>Submitter ID</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${entity.submitterId}"></c:out>
									</td>
								</tr>
								<tr>
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Name" /> --%>
										<egov-smc:commonLabel>Name</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${entity.submitterName}"></c:out>
									</td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Address" /> --%>
										<egov-smc:commonLabel>Address</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
											value="${entity.submitterAddress }"></c:out></td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Mailing Address" /> --%>
										<egov-smc:commonLabel>Mailing Address</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
											value="${entity.submitterMailAddress}"></c:out></td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="E-mail" /> --%>
										<egov-smc:commonLabel>E-mail</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${entity.submitterEmail}"></c:out>
									</td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Mobile No." /> --%>
										<egov-smc:commonLabel>Mobile No.</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${entity.submitterMobile}"></c:out>
									</td>
								</tr>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
										<%-- <egov-smc:fieldLabel
                                            name="Phone No." /> --%>
										<egov-smc:commonLabel>Phone No.</egov-smc:commonLabel>
									</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<c:out value="${entity.submitterTel}"></c:out>
									</td>
								</tr>
								</tbody>
							</table>
							<br class="clear" />
						</div>

					</div>
					<br class="clear" />
				</div>


				<div id="fragment-2" class="tabFrame">


					<div class="inner-accordion" id="innerAccordiion4">
						<script type="text/javascript">
                            function changeSort_resultForm1(sort, isAsc) {
                                var sortBy = sort;
                                var sortDir;
                                if(isAsc)
                                    sortDir='';
                                else
                                    sortDir='descending';
                                SOP.Crud.cfxSubmit('applicationForm', 'sortSupport', sortBy, sortDir);
                            }
						</script>
						<div class="header"><span class="collapse" style="height: 20px;"><egov-smc:commonLabel>Supporting Documents</egov-smc:commonLabel></span></div>
						<div class="content">
							<%--<egov-core:ifPrivilegeAccessible privilegeId="<%=RbacConstatns.EGOV_USER_HANDLE_SUPPORT_DOC_PRIVILEGE %>" var="isUserCanHandleSupport"/>
                           <egov-core:ifPrivilegeAccessible privilegeId="<%=RbacConstatns.EGOV_USER_HANDLE_INTERNAL_DOC_PRIVILEGE%>" var="isUserCanHandleInternal"/>--%>
							<p class="text">
								<egov-smc:message key="supportingDocumentsDefine">These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed documents are those defined for this digital service only.</egov-smc:message>
							</p>
							<table class="table2 view-table" style="border: 1px; border-style:solid;border-color:#d0d0d0;">
								<tbody>
								<tr style="border:0px;background-color: #A5A5A5;">
									<td class="th" align="left" width="16%">
										<div class="sort-label"><egov-smc:commonLabel>Document</egov-smc:commonLabel></div></td>
									<td class="th" align="left" width="20%">
										<div class="sort-label"><egov-smc:commonLabel>File</egov-smc:commonLabel></div></td>
									<td class="th" align="left" width="16%">
										<div class="sort-label"><egov-smc:commonLabel>Size</egov-smc:commonLabel></div></td>
									<td class="th" align="left" width="16%">
										<div class="sort-label"><egov-smc:commonLabel>Submitted By</egov-smc:commonLabel></div></td>
									<td class="th" align="left" width="20%">
										<div class="sort-label"><egov-smc:commonLabel>Data Submitted</egov-smc:commonLabel></div></td>
									<td class="th" align="left" width="12%">
										<div class="sort-label"><egov-smc:commonLabel>Action</egov-smc:commonLabel></div></td>
								</tr>

								<%

									DocUpload[] docs = (DocUpload[])request.getAttribute("supportDocs");
									String languageCode = (String)request.getAttribute("languageCode");
									String batchId = app.getSupportDocBatchId();
									ServiceRegistry svc = null;
									if(process.currentCase!= null){
										svc = ServiceRegistryHelper.getServiceRegistryFromCase(process.currentCase);
									}else{
										long metaId = app.getSvcMetaId();
										svc =  ServiceRegistryService.getInstance().retrieveByMetaId(metaId);
									}

									String tagStr = app.getSupportDocTag();
									String[] tags = null;
									if(!StringHelper.isEmpty(tagStr)){
										tags = SupportedDocHelper.parseTags(tagStr);
									}

									List<DocumentSetup> dss = svc.getDocumentSetups(tags);
									if(dss != null && dss.size()>0){
										for(int i = 0;i<dss.size();i++){
											String uploadDate = "";
											DocumentSetup ds = dss.get(i);
											ds.setLanguage(languageCode);
											int size = 0;
											String s = "";
											String docName = ds.getName();
											String displayDocName = ds.getLabelMultiLang();
											if(StringHelper.isEmpty(displayDocName)){
												displayDocName = ds.getName();
											}
											String updateUserId = "";
											String downloadLink = "";
											DocUpload doc = null;
											String actionType = "";
											for(int j=0; j<docs.length;j++){
												if(StringHelper.equals(dss.get(i).getName(), docs[j].getDocName())){
													doc = docs[j];
													updateUserId = doc.getUpdatedUserId();
													uploadDate = ConsistencyHelper.formatDateTime(doc.getCreatedDate());
													size = doc.getDocFileSize();
													downloadLink = "<a href=\""+ request.getContextPath() + "/process/EGOV/DownloadSupportDocument?docId="+doc.getDocId()+"&appNo="+appNo+"\">"+doc.getDocFilename() +"</a>";
													s = EGPCommonHelper.formatFileSize(size);
													break;
												}
											}

											if(doc==null ){
												actionType = "<a  style=\"cursor: pointer;\" onclick=\"openSupportDoc('"+docName+"','"+ds.getFileType()+"');\" href=\"javascript:void(0);\" title=\"Upload\"><img alt=\"Upload\" src=\""+EgpcloudPortFactory.webContext+"/_themes/egov/images/general/arrow-090.png\"/></a>"+"&nbsp;&nbsp;&nbsp;&nbsp;"+
														"<a style=\"cursor: pointer;\" onclick=\"openSupportDocHistory('"+displayDocName+"','"+batchId+"','"+request.getContextPath()+"','"+appNo+"','"+docName+"');\" href=\"javascript:void(0);\" title=\"History\"><img alt=\"History\" src=\""+EgpcloudPortFactory.webContext+"/_themes/egov/images/general/view.png\"/></a>";
											}else{
//									if(UserDomainHelper.isCounterStaffDomain(doc.getCreatedUserDomain())){
												actionType = "<a  style=\"cursor: pointer;width:16px\" onclick=\"deleteSupportDoc('"+doc.getDocId()+"');\" href=\"javascript:void(0);\" title=\"Remove\"><img alt=\"Remove\" src=\""+EgpcloudPortFactory.webContext+"/_themes/egov/images/general/delete.png\"/></a>"+"&nbsp;&nbsp;&nbsp;&nbsp;"+
														"<a  style=\"cursor: pointer;\" onclick=\"openUpdateSupportDoc('"+doc.getDocId()+"','"+docName+"','"+ds.getFileType()+"');\" href=\"javascript:void(0);\" title=\"Update\"><img alt=\"Update\" src=\""+EgpcloudPortFactory.webContext+"/_themes/egov/images/general/edit.png\"/></a>"+"&nbsp;&nbsp;&nbsp;&nbsp;"+
														"<a style=\"cursor: pointer;\" onclick=\"openSupportDocHistory('"+displayDocName+"','"+batchId+"','"+request.getContextPath()+"','"+appNo+"','"+docName+"');\" href=\"javascript:void(0);\" title=\"History\"><img alt=\"History\" src=\""+EgpcloudPortFactory.webContext+"/_themes/egov/images/general/view.png\"/></a>";
//									}
											}
								%>
								<tr style="width:50%">
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><%=displayDocName %></td>
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><%=downloadLink %></td>
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><%=s %></td>
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><%=updateUserId!=null?updateUserId:"" %></td>
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><%=uploadDate %></td>
									<td style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;">
										<c:if test="${isUserCanUploadDoc == true or isUserCanUpdateStatus == true}">
											<c:if test="${isUserCanHandleSupport}">
												<%=actionType%>
											</c:if>
										</c:if>
									</td>
								</tr>
								<%}}else{ %>
								<tr><td colspan="6"><center><egov-smc:commonLabel>No records found</egov-smc:commonLabel></center></td></tr>
								<%} %>
								</tbody>
							</table>
							<br class="clear"/>
						</div>

					</div>



					<div class="inner-accordion" id="innerAccordiion5">
						<div class="header"><span class="collapse" style="height: 20px;"><egov-smc:commonLabel>Internal Documents</egov-smc:commonLabel></span></div>


						<div class="content">
							<p class="text">
								<egov-smc:message key="internalDocumentsDefine">These are documents uploaded by an agency officer to support back office processing.</egov-smc:message>
							</p>
							<script type="text/javascript">
                                function changeSort_resultForm(sort, isAsc) {
                                    var sortBy = sort;
                                    var sortDir;
                                    if(isAsc)
                                        sortDir='';
                                    else
                                        sortDir='descending';
                                    SOP.Crud.cfxSubmit('applicationForm', 'sortInternal', sortBy, sortDir);
                                }
							</script>
							<table class="table2 view-table" style="border: 1px; border-style:solid;border-color:#d0d0d0;">
								<tbody>
								<%
									String nameDesc = "";
									String nameAsc = "";
									String fileNameDesc = "";
									String fileNameAsc = "";
									String sizeDesc = "";
									String sizeAsc = "";
									String userDesc = "";
									String userAsc = "";
									String dateDesc = "";
									String dateAsc = "";
									String sortBy = (String) session
											.getAttribute(ApplicationViewController.INTERNAL_DOC_UPLOAD_ORDER_BY);
									String sortType = (String) session
											.getAttribute(ApplicationViewController.INTERNAL_DOC_UPLOAD_ORDER_TYPE);
									if("docName".equals(sortBy)){
										if("descending".equals(sortType)){
											nameDesc = "active";
										}else{
											nameAsc = "active";
										}
									}else if("docFilename".equals(sortBy)){
										if("descending".equals(sortType)){
											fileNameDesc = "active";
										}else{
											fileNameAsc = "active";
										}
									}else if("docFileSize".equals(sortBy)){
										if("descending".equals(sortType)){
											sizeDesc = "active";
										}else{
											sizeAsc = "active";
										}
									}else if("createdUserId".equals(sortBy)){
										if("descending".equals(sortType)){
											userDesc = "active";
										}else{
											userAsc = "active";
										}
									}else if("createdDate".equals(sortBy)){
										if("descending".equals(sortType)){
											dateDesc = "active";
										}else{
											dateAsc = "active";
										}
									}
								%>
								<tr style="border:0px;background-color: #A5A5A5;">
									<td class="th" align="left" width="16%">
										<span class="column-sort"> <a class="sort-up <%=nameAsc %>" title="Sort up" onclick="changeSort_resultForm('docName', true)" href="javascript:void(0);"></a> <a class="sort-down <%=nameDesc %>" title="Sort down" onclick="changeSort_resultForm('docName', false)" href="javascript:void(0);"></a></span>
										<div class="sort-label" style="padding-left:20px;"><egov-smc:commonLabel>Document</egov-smc:commonLabel></div></td>
									<td class="th" align="left" width="16%">
										<span class="column-sort"> <a class="sort-up <%=fileNameAsc %>" title="Sort up" onclick="changeSort_resultForm('docFilename', true)" href="javascript:void(0);"></a> <a class="sort-down <%=fileNameDesc %>" title="Sort down" onclick="changeSort_resultForm('docFilename', false)" href="javascript:void(0);"></a></span>
										<div class="sort-label" style="padding-left:20px;"><egov-smc:commonLabel>File</egov-smc:commonLabel></div></td>
									<td class="th" align="left" width="16%"><span class="column-sort"> <a class="sort-up <%=sizeAsc %>" title="Sort up" onclick="changeSort_resultForm('docFileSize', true)" href="javascript:void(0);"></a> <a class="sort-down <%=sizeDesc %>" title="Sort down" onclick="changeSort_resultForm('docFileSize', false)" href="javascript:void(0);"></a></span>
										<div class="sort-label" style="padding-left:20px;"><egov-smc:commonLabel>Size</egov-smc:commonLabel></div></td>
									<td class="th" align="left" width="16%"><span class="column-sort"> <a class="sort-up <%=userAsc %>" title="Sort up" onclick="changeSort_resultForm('createdUserId', true)" href="javascript:void(0);"></a> <a class="sort-down  <%=userDesc %>" title="Sort down" onclick="changeSort_resultForm('createdUserId', false)" href="javascript:void(0);"></a></span>
										<div class="sort-label" style="padding-left:20px;"><egov-smc:commonLabel>Uploaded By</egov-smc:commonLabel></div></td>
									<td class="th" align="left" width="16%"><span class="column-sort"> <a class="sort-up <%=dateAsc %>" title="Sort up" onclick="changeSort_resultForm('createdDate', true)" href="javascript:void(0);"></a> <a class="sort-down  <%=dateDesc %>" title="Sort down" onclick="changeSort_resultForm('createdDate', false)" href="javascript:void(0);"></a></span>
										<div class="sort-label" style="padding-left:20px;"><egov-smc:commonLabel>Date Uploaded</egov-smc:commonLabel></div></td>
									<c:if test="${isUserCanUploadDoc == true or isUserCanUpdateStatus == true}">
										<c:if test="${isUserCanHandleInternal}">
											<td class="th" align="left" width="16%"></td>
										</c:if>
									</c:if>

								</tr>
								<%
									InternalDocUpload[] internalDocs = (InternalDocUpload[])request.getAttribute("internalDocs");
									if(!ArrayHelper.isEmpty(internalDocs)){
										for(int i = 0;i<internalDocs.length;i++){
											InternalDocUpload doc = internalDocs[i];

											String uploadDate = ConsistencyHelper.formatDateTime(doc.getCreatedDate());
											int size = doc.getDocFileSize();
											String s = EGPCommonHelper.formatFileSize(size);
								%>
								<tr style="width:50%">
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><%=doc.getDocName() %></td>
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><a href="<%=request.getContextPath()%>/process/EGOV/DownloadInternalDocument?docId=<%=doc.getDocId()%>&appNo=<%=appNo%>"><%=doc.getDocFilename() %></a></td>
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><%=s %></td>
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><%=doc.getUpdatedUserId() %></td>
									<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><%=uploadDate %></td>
									<c:if test="${isUserCanUploadDoc == true or isUserCanUpdateStatus == true}">
										<c:if test="${isUserCanHandleInternal}">
											<td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><a href="javascript:void(0);" onclick="deleteInternalDoc('<%=doc.getDocId() %>');" style="cursor: pointer;"><egov-smc:commonLabel>Remove</egov-smc:commonLabel></a></td>
										</c:if>
									</c:if>
								</tr>
								<script type="text/javascript">
                                    $(function(){
                                        if(!internalDoc['<%=doc.getDocName()%>'.toLowerCase()]){
                                            internalDoc['<%=doc.getDocName()%>'.toLowerCase()] = "<%=doc.getDocFilename()%>";
                                        }
                                    })
								</script>
								<%}}else{ %>
								<tr><td colspan="6"><center><egov-smc:commonLabel>No records found</egov-smc:commonLabel></center></td></tr>
								<%} %>
								</tbody>
							</table>
							<%
								// judge user is the counter staff of this application (an attribute of  service registry)
								boolean isUserCanUploadDoc = (Boolean)request.getAttribute("isUserCanUploadDoc");
								// judge user is the assigned user (user of task is current user)
								boolean isUserIsCurrent = (Boolean)request.getAttribute("isUserCanUpdateStatus");
								boolean isHmodeBackendAndNoOfficer = false;
								if (ServerConfig.getInstance().isHModeEnable() && !ServerConfig.getInstance().isFrontend()) {
									if (StringHelper.isEmpty(app.getOfficerDomain()) || StringHelper.isEmpty(app.getOfficerId())) {
										isHmodeBackendAndNoOfficer = true;
									}
								}
								if(!isHmodeBackendAndNoOfficer && (isUserCanUploadDoc || isUserIsCurrent)){
							%>
							<c:if test="${isUserCanHandleInternal}">
								<div class="action-buttons" align="right">
									<button type="button"  style="width:150px;" onclick="openUploadDoc();"><egov-smc:commonLabel>Upload Document</egov-smc:commonLabel></button>
								</div>
							</c:if>
							<%
								}
							%>

							<br class="clear"/>
						</div>

					</div>

					<br class="clear"/>

				</div>

				<div id="fragment-3" class="tabFrame">
					<div class="inner-accordion" id="innerAccordiion6">
						<div class="header">
							<span class="collapse" style="height: 20px;"><egov-smc:commonLabel>Payment Details</egov-smc:commonLabel></span>
						</div>

						<div class="content">
							<table class="table2 view-table"
								   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
								<tbody>
								<tr style="border: 0px; background-color: #A5A5A5;">
									<td align="left" width="16%"><egov-smc:commonLabel>Payment</egov-smc:commonLabel></td>
									<td align="left" width="16%"><egov-smc:commonLabel>Amount</egov-smc:commonLabel></td>
									<td align="left" width="16%"><egov-smc:commonLabel>Date</egov-smc:commonLabel></td>
									<td align="left" width="16%"><egov-smc:commonLabel>Status</egov-smc:commonLabel></td>
									<td align="left" width="16%"><egov-smc:commonLabel>Reference No.</egov-smc:commonLabel></td>
									<td align="left" width="16%"><egov-smc:commonLabel>Payment Type</egov-smc:commonLabel></td>
								</tr>
								<%
									Payment[] payments = (Payment[]) request.getAttribute("payments");
									if (payments != null && payments.length != 0) {
										for (int i = 0; i < payments.length; i++) {
											Payment payment = payments[i];
											Date paymentCreated = payment.getCreatedDate();
											String paymentCreatedStr = "";
											if (paymentCreated != null)
												paymentCreatedStr = ConsistencyHelper.formatDate(paymentCreated);
								%>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><egov-smc:commonLabel><%=payment.getPaymentDesc() %></egov-smc:commonLabel></td>
									<td
											style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=payment.getDisplayPayAmount() %></td>
									<td
											style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=paymentCreatedStr%></td>
									<td
											style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><egov-smc:commonLabel><%=payment.getPaymentStatus() %></egov-smc:commonLabel></td>
									<td
											style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=payment.getTxRefNo() %></td>
									<td
											style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><egov-smc:commonLabel><%=payment.getPaymentType()%></egov-smc:commonLabel></td>
								</tr>
								<%
									}
								}else{
								%>
								<tr style="width: 50%">
									<td colspan="99" style="text-align: center"><egov-smc:commonLabel>No records found</egov-smc:commonLabel></td>
								</tr>
								<%
									}
								%>

								</tbody>
							</table>
							<br class="clear" />
						</div>
					</div>
				</div>
				<div id="fragment-4" class="tabFrame">
					<div class="inner-accordion" id="innerAccordiion7">
						<div class="header">
							<span class="collapse" style="height: 25px;"><egov-smc:commonLabel>Processing Status Update</egov-smc:commonLabel></span>
						</div>

						<div class="content">
							<table class="table2 view-table"
								   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
								<tbody>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; width: 50%; border-color: #d0d0d0;"><egov-smc:commonLabel>Current Status</egov-smc:commonLabel>:</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<egov-smc:commonLabel><c:out value="${displayCurrentStatus}"></c:out></egov-smc:commonLabel>
									</td>
								</tr>
								<%
									boolean isUserCanUpdateStatus = (Boolean)request.getAttribute("isUserCanUpdateStatus");

									if(isUserCanUpdateStatus){
								%>
								<tr style="width: 50%">
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;"><egov-smc:commonLabel>Internal Comments</egov-smc:commonLabel>:</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<%
											String internalComementsValue = (String)request.getAttribute("internalCommentsValue");
											boolean isCompleted = false;
											if (task != null) {
												isCompleted = task.isEndState();
											} else {
												isCompleted = true;
											}

											if (isCompleted) {
												AppInParam inParam = new AppInParam();
												Application appTemp = com.ecquaria.cloud.entity.application.ApplicationService.getInstance().retrieveByAppNo(
														appNo);
												inParam.setApplication(appTemp);
												inParam.setSelectedPage("0");
												if (task != null)
													inParam.setTaskId(task.getId());
												else
													inParam.setTaskId(null);
												request.setAttribute(AppInParam.APP_VIEW_INPUT_OBJ, inParam);
											}
											if(!StringHelper.isEmpty(internalComementsValue)){
										%> <textarea rows="" cols="25" name="internalComments"><%=internalComementsValue %></textarea>
										<br /> <small class="error"></small> <%
									}else{
									%> <textarea rows="" cols="25" name="internalComments"></textarea>
										<br /> <small class="error"></small> <%
										}
									%>

									</td>
								</tr>
								<tr>
									<td
											style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;"><egov-smc:commonLabel>Status</egov-smc:commonLabel>:</td>
									<td align="left"
										style="border: 1px; border-style: solid; border-color: #d0d0d0;">
										<%
											String selectUpdateOption = (String)request.getAttribute("selectUpdateOption");
											String[][] opts = (String[][])request.getAttribute("updateStatusOpt");
											if(opts != null && opts.length!=0){
										%> <select id="selectedStatus" name="selectedStatus" onchange="changeStatus()">
										<%
											for(int i = 0 ; i< opts.length; i++){
												String key = opts[i][0];
												String value = opts[i][1];
												if(StringHelper.equals(selectUpdateOption, key)){
										%>
										<option value="<%=key%>" selected="selected"><egov-smc:commonLabel><%=value %></egov-smc:commonLabel></option>
										<%
										}else{
										%>
										<option value="<%=key%>"><egov-smc:commonLabel><%=value %></egov-smc:commonLabel></option>
										<%
												}
											}
										%>
									</select> <%
										}
									%>

									</td>
								</tr>
								<%--<tr>--%>
									<%--<td--%>
											<%--style="border: 1px; border-style: solid; text-align: center; border-color: #d0d0d0;"--%>
											<%--colspan="2">--%>
										<%--<button type="button" style= "width:100px;" onclick="updateStatusConfirm();"><egov-smc:commonLabel>Update</egov-smc:commonLabel></button>--%>
									<%--</td>--%>
								<%--</tr>--%>
								<%
									}
								%>
								</tbody>
							</table>

                            <div class="inner-accordion" id="innerAccordiion9" style="margin-left: 0px;margin-right: 0px;padding-left: 0px;padding-right: 0px;width: 100%">
                                <div class="header">
                                    <span class="collapse" style="height: 20px;"><egov-smc:commonLabel>Endorsement Routing</egov-smc:commonLabel></span>
                                </div>

                                <div class="content">
                                    <table class="table2 view-table" style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <tbody>
                                        <tr style="border: 0px; background-color: #A5A5A5;">
                                            <td align="left" width="5%">
                                                <input id="selectAllEndorsement" type="checkbox" onclick="selectAllEndorsements(this.checked)"/>
                                            </td>
                                            <td align="left" width="5%"><egov-smc:commonLabel>No</egov-smc:commonLabel></td>
                                            <td align="left" width="16%"><egov-smc:commonLabel>Agency</egov-smc:commonLabel></td>
                                            <td align="left" width="16%"><egov-smc:commonLabel>Working Group</egov-smc:commonLabel></td>
                                            <td align="left" width="16%"><egov-smc:commonLabel>Assignment Type</egov-smc:commonLabel></td>
                                            <td align="left" width="16%"><egov-smc:commonLabel>Officer</egov-smc:commonLabel></td>
                                        </tr>
                                        <%
                                            List<ProcessEndorseTask> endorseConfigs = (List<ProcessEndorseTask>) request.getSession().getAttribute("endorseConfigsList");
                                            if (endorseConfigs != null && endorseConfigs.size() > 0) {
                                                for (ProcessEndorseTask processEndorseTask : endorseConfigs) {
                                                    String configEndorseId = processEndorseTask.getId();
                                                    String configEndorseAgencyName = processEndorseTask.getAgencyName();
                                                    long configEndorseWorkingGroupNo = processEndorseTask.getWorkingGroup();
                                                    UserGroup ConfigEndorseGroup = UserService.getInstance().getGroupsByGroupNo(configEndorseWorkingGroupNo);
                                                    String configEndorseWorkingGroupName = "-";
                                                    if(ConfigEndorseGroup != null){
                                                        configEndorseWorkingGroupName = ConfigEndorseGroup.getGroupFullName();
                                                    }
                                                    String configEndorseAssignType = processEndorseTask.getAssignmentType();
                                                    String configEndorseOfficer = "-";
                                                    if (StringHelper.equals(AppConstants.TASK_ASSIGNMENTTYPE_DIRECT_TO_USER, configEndorseAssignType)) {
                                                        configEndorseOfficer = processEndorseTask.getUserId();

                                                    } else if (StringHelper.equals(AppConstants.TASK_ASSIGNMENTTYPE_GROUP_ROUND_ROBIN, configEndorseAssignType)) {
                                                        configEndorseOfficer = "-";

                                                    } else if (StringHelper.equals(AppConstants.TASK_ASSIGNMENTTYPE_GROUP_PICKUP, configEndorseAssignType)) {
                                                        configEndorseOfficer = "-";

                                                    }
                                        %>
                                                    <tr style="width: 50%">
                                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;">
                                                            <input type="checkbox" name="endorsementConfigIds" value="<%=configEndorseId%>" onclick="selectEndorsement(this.checked)">
                                                        </td>
                                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><egov-smc:commonLabel><%=configEndorseId%></egov-smc:commonLabel></td>
                                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><egov-smc:commonLabel><%=configEndorseAgencyName%></egov-smc:commonLabel></td>
                                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><egov-smc:commonLabel><%=configEndorseWorkingGroupName%></egov-smc:commonLabel></td>
                                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><egov-smc:commonLabel><%=configEndorseAssignType%></egov-smc:commonLabel></td>
                                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><egov-smc:commonLabel><%=configEndorseOfficer%></egov-smc:commonLabel></td>
                                                    </tr>
                                        <%
                                                }
                                            }
                                        %>
                                        </tbody>
                                    </table>
                                </div>
                            </div>


                            <%
                                if(isUserCanUpdateStatus){
                            %>
                                <div class="action-buttons" align="center" style="margin-left: 0;">
                                    <button type="button" style= "width:100px;" onclick="updateStatusConfirm();"><egov-smc:commonLabel>Update</egov-smc:commonLabel></button>
                                </div>
                            <%
                                }
                            %>
                        </div>

					</div>
					<br class="clear" />

                    <%
                        List<EndorsementTask> endorsementHistories = (List<EndorsementTask>) request.getAttribute("endorsementHistories");
                        if (endorsementHistories != null && endorsementHistories.size() > 0) {
                    %>
                    <div class="inner-accordion" id="innerAccordiion10">
                        <div class="header">
                            <span class="collapse" style="height: 20px;"><egov-smc:commonLabel>Endorsement History</egov-smc:commonLabel></span>
                        </div>

                        <div class="content">
                            <table class="table2 view-table" style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                <tbody>
                                <tr style="border: 0px; background-color: #A5A5A5;">
                                    <td align="left" width="3%"><egov-smc:commonLabel>No</egov-smc:commonLabel></td>
                                    <td align="left" width="8%"><egov-smc:commonLabel>Stage</egov-smc:commonLabel></td>
                                    <td align="left" width="8%"><egov-smc:commonLabel>Agency</egov-smc:commonLabel></td>
                                    <td align="left" width="8%"><egov-smc:commonLabel>Officer</egov-smc:commonLabel></td>
                                    <td align="left" width="16%"><egov-smc:commonLabel>Working Group</egov-smc:commonLabel></td>
                                    <td align="left" width="12%"><egov-smc:commonLabel>Status</egov-smc:commonLabel></td>
                                    <td align="left" width="16%"><egov-smc:commonLabel>Internal Comments</egov-smc:commonLabel></td>
                                    <td align="left" width="16%"><egov-smc:commonLabel>Last Updated</egov-smc:commonLabel></td>
                                </tr>
                                <%
                                    int historyNo = 1;
                                    for (EndorsementTask endorsementTask : endorsementHistories) {
                                        String endorseStage = endorsementTask.getName();
                                        String endorseAgency = endorsementTask.getAgencyName();
                                        String endorseOfficer = endorsementTask.getOfficerId();
                                        if (StringHelper.isEmpty(endorseOfficer)) {
                                            endorseOfficer = "-";
                                        }

                                        long endorseGroupNo = endorsementTask.getWorkingGroup();
                                        UserGroup endorseGroup = UserService.getInstance().getGroupsByGroupNo(endorseGroupNo);
                                        String endorseWorkingGroupName = "-";
                                        if(endorseGroup != null){
                                            endorseWorkingGroupName = endorseGroup.getGroupFullName();
                                        }
                                        String endorseStatus = endorsementTask.getEndorseStatus();
                                        if (StringHelper.equals(endorseStatus, EndorsementTask.STATUS_APPROVE)) {
                                            endorseStatus = "Approved";

                                        } else if (StringHelper.equals(endorseStatus, EndorsementTask.STATUS_REJECT)) {
                                            endorseStatus = "Rejected";

                                        } else if (StringHelper.equals(endorseStatus, EndorsementTask.STATUS_PENDING)) {
                                            endorseStatus = "Pending Endorsement";

                                        }

                                        Date endorseDate = endorsementTask.getDateUpdated();
                                        String endorseHisDate = "-";
                                        if (endorseDate != null) {
                                            endorseHisDate = ConsistencyHelper.formatDateTime(endorseDate);
                                        }
                                        String endorseInternalComments = endorsementTask.getInternalComments();
                                        if (StringHelper.isEmpty(endorseInternalComments)) {
                                            endorseInternalComments = "-";
                                        }
                                %>
                                    <tr style="width: 50%">
                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=historyNo%></td>
                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=endorseStage%></td>
                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=endorseAgency%></td>
                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=endorseOfficer%></td>
                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=endorseWorkingGroupName%></td>
                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=endorseStatus%></td>
                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=endorseInternalComments%></td>
                                        <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=endorseHisDate%></td>
                                    </tr>
                                <%
                                        historyNo++;
                                    }

                                %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <br class="clear" />
                    <%
                        }
                    %>



                   <%-- <%
                        boolean hideProcessingHis = (boolean) request.getAttribute("hideProcessingHis");
                        if (!hideProcessingHis){
                    %>--%>
                    <div class="inner-accordion" id="innerAccordiion8">
                        <div class="header">
                            <span class="collapse" style="height: 20px;"><egov-smc:commonLabel>Processing History</egov-smc:commonLabel></span>
                        </div>

                        <div class="content">
                            <table class="table2 view-table"
                                   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                <tbody>
                                <tr style="border: 0px; background-color: #A5A5A5;">
                                    <td align="left" width="16%"><egov-smc:commonLabel>Officer</egov-smc:commonLabel>
                                    </td>
                                    <td align="left" width="16%"><egov-smc:commonLabel>Working Group</egov-smc:commonLabel></td>
                                    <td align="left" width="16%"><egov-smc:commonLabel>Status Update</egov-smc:commonLabel></td>
                                    <td align="left" width="16%"><egov-smc:commonLabel>Internal Comments</egov-smc:commonLabel></td>
                                    <td align="left" width="17%"><egov-smc:commonLabel>Last Updated</egov-smc:commonLabel></td>
                                </tr>
                                <%
                                    AppProcessingHistory[] histories = (AppProcessingHistory[]) request
                                            .getAttribute("histories");

                                    if (histories != null && histories.length != 0) {
                                        for (int i = 0; i < histories.length; i++) {
                                            AppProcessingHistory history = histories[i];
                                            String statusCode = history.getAppStatus();
                                            AppStatus appStatus =  AppStatusHelper.getInstance().getAppStatus(statusCode);
                                            String displayStatus = appStatus.getLabel();
                                            Date hisDateCreated = history.getCreatedDate();
                                            String hisDate = "-";
                                            if (hisDateCreated != null)
                                                hisDate = ConsistencyHelper.formatDateTime(hisDateCreated);
                                            String workingGroupName = "-";
                                            long groupNo = history.getWorkingGroup();
                                            UserGroup group = UserService.getInstance().getGroupsByGroupNo(groupNo);

                                            if(group != null){
                                                workingGroupName = group.getGroupFullName();
                                            }
                                            String internalComments = "";
                                            if(!StringHelper.isEmpty(history.getInternalComments())){
                                                internalComments = history.getInternalComments();
                                            }

                                            String officer = "-";
                                            if(!StringHelper.isEmpty(history.getOfficerName())){
                                                officer = history.getOfficerName();
                                            }

                                %>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=officer%></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=workingGroupName %></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><egov-smc:commonLabel><%=displayStatus%></egov-smc:commonLabel></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=internalComments%></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"><%=hisDate%></td>
                                </tr>
                                <%
                                    }
                                }else{
                                %>
                                <tr style="width: 50%">
                                    <td colspan="5" style="text-align: center"><egov-smc:commonLabel>No records found</egov-smc:commonLabel></td>
                                </tr>
                                <%
                                    }
                                %>



                                </tbody>
                            </table>
                            <br class="clear" />
                        </div>

                    </div>
                   <%-- <%
                        }
                    %>--%>



				</div>
			</form>
		</div>

	</div>


	<br class="clear">
	<script type="text/javascript">

        $(function () {
            var status = $("#selectedStatus").find("option:selected").text();
            if (status != "Pending Endorsement") {
                $("#innerAccordiion9").css("display", "none")
            };
        })

        function changeStatus(){
            var status = $("#selectedStatus").find("option:selected").text();
            if (status == 'Pending Endorsement') {
                $("#innerAccordiion9").css("display", "block");

            }else {
                $("#innerAccordiion9").css("display", "none");
            }
        }

        function selectAllEndorsements(checkValue) {
            var endorseIds = $('#innerAccordiion9 :checkbox[name="endorsementConfigIds"]');
            for (var i = 0; i < endorseIds.length; i++){
                endorseIds[i].checked = checkValue;
            }
        }

        function selectEndorsement(checkValue) {
            var endorseIds = $('#innerAccordiion9 :checkbox[name="endorsementConfigIds"]');
            var checked = endorseIds.length == 0? false:true;

            for (var i = 0; i < endorseIds.length; i++){
                if (!endorseIds[i].checked) {
                    checked = false;
                    break;
                }
            }

            $("#selectAllEndorsement").prop("checked", checked);
        }

        $(function(){
            $('#confirm_content').tabs({select: function(event, ui){
                var index = ui.tab;
                var href = $(ui.tab).attr('href');
                var title = $(index).html();
                if('#msgTab2-0' != href){
                    closeEditor();
                }

                updateMessage(ui.panel);
            }
            });

            $('#last_content').tabs({select: function(event, ui){
                var index = ui.tab;
                var href = $(ui.tab).attr('href');
                var title = $(index).html();
                if(href != "#msgTab-0"){
                    closeEditor();
                }

                updateMessage(ui.panel);

            }
            });
            $('#last_supportDoc').tabs({select: function(event, ui){
                var index = ui.tab;
                var href = $(ui.tab).attr('href');
                var title = $(index).html();
            }
            });
            $('#smsInputForm #smsContent').limit(LIMIT_CHARS, $('#smsInputForm #smsChars'));
            $('#smsInputForm2 #smsContent2').limit(LIMIT_CHARS, $('#smsInputForm2 #smsChars'));
        })
	</script>

	<div id="last_supportDoc" class="batContent">
		<div id=""></div>
		<%--<ul>
            <li><a href="#docTab"><egov-smc:commonLabel>History</egov-smc:commonLabel></a></li>
        </ul>--%>
		<div id="docTab" style="<c:out value='${cssHistory }'/>">
			<div class="history-content">
				<table width="100%" cellspacing="0"
					   class="table ju-processing-history">
					<!-- Begin Table Head -->
					<thead>
					<tr style="background-color: rgb(246, 246, 246);">
						<th scope="col"><egov-smc:commonLabel>Document</egov-smc:commonLabel></th>
						<th scope="col"><egov-smc:commonLabel>File</egov-smc:commonLabel></th>
						<th scope="col"><egov-smc:commonLabel>Size</egov-smc:commonLabel></th>
						<th scope="col"><egov-smc:commonLabel>Submitted By</egov-smc:commonLabel></th>
						<th scope="col"><egov-smc:commonLabel>Date Submitted</egov-smc:commonLabel></th>
					</tr>
					</thead>
					<!-- End Table Head -->
					<tbody id="support_histort_tr">
					</tbody>
				</table>
			</div>
			<!--End Table Listing-->
		</div>
	</div>

	<div id="last_content" class="batContent">
		<div id="message"></div>
		<c:if test="${needHistory }">
			<ul>
				<c:if test="${needEmail}">
					<li><a href="#msgTab-0"><egov-smc:commonLabel>E-mail</egov-smc:commonLabel></a></li>
				</c:if>
				<c:if test="${needSMS}">
					<li><a href="#msgTab-1"><egov-smc:commonLabel>SMS</egov-smc:commonLabel></a></li>
				</c:if>
				<c:if test="${needHistory}">
					<li><a href="#msgTab-2"><egov-smc:commonLabel>History</egov-smc:commonLabel></a></li>
				</c:if>
			</ul>
		</c:if>
		<div id="msgTab-0" style="<c:out value='${cssEmail }'/>">
			<form id="emailInputForm">
				<sop-htmlform:hidden name="comments" />
				<script type="text/javascript">
                    var updateMessage = function(panel){
                        var _sHandler = function(data, textStatus){
                            var $data = eval(data);
                            $(panel).find('.ju-processing-history').find('tbody').empty();
                            for(var i=0;i<$data.length;i++){
                                var $tr = $('<tr><td>'+$data[i].dateSent+'</td><td>'+$data[i].officer+'</td><td>'+$data[i].type+'</td><td style="padding-left: 23px">'+ Base64.decode($data[i].content) +'</td></tr>');
                                $(panel).find('.ju-processing-history').find('tbody').append($tr);
                            }
                        };

                        var _eHandler = function(data, textStatus){
                        };

                        $.ajax({
                            type: "POST",
                            url: '<%=request.getContextPath()%>/process/EGPCLOUD/ApplicationView/UpdateMessage',
                            data: {appId:$('[name="entityUids"]').val() },
                            success: _sHandler,
                            error: _eHandler
                        });
                    }

                    var sendEmail = function(){
                        $('#smsInputForm').find('small.error').css("display","none");
                        $('#emailInputForm small.error').each(function(){
                            var html = $(this).html('');
                            $(this).hide();
                        });

                        var to = $('[name="emailTo"]').val();
                        var subject = $('[name="emailSubject"]').val();
                        var content = $('#emailContent').text();
                        var hasError = false;
                        if(!to){
                            $('[name="emailTo"]').parent('.text').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                            hasError = true;
                        }

                        if(!subject){
                            $('[name="emailSubject"]').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                            hasError = true;
                        }

                        if(!content){
                            $('[name="emailContent"]').parent().nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                            hasError = true;
                        }

                        if(hasError){
                            $('#emailInputForm small.error').each(function(){
                                var html = $(this).html();
                                if(html){
                                    $(this).css("background","#f5b9b9");
                                    $(this).css('display','block');
                                }
                            });

                            return;
                        }


                        SOP.Common.showMask();
                        var _sHandler = function(data, textStatus){
                            var status = eval(data)[0].status;
                            $('#message').find('div').hide();
                            if(status == "success"){
                                var $success = $('#message').find(".alert-success");
                                if($success.length==0){
                                    $('#message').append('<div class="alert-success"><p>'+eval(data)[0].message+'</p></div>');
                                }else{
                                    var $p = $success.find('p');
                                    $p.html(eval(data)[0].message);
                                }
                                $success.show();
                                $('#emailInputForm').trigger('reset');
                                $('#emailContent').html('');
                                // $('[name="smsContent"]').val('');
                                $("a[href='#msgTab-2']").click();
                            }else if(status == "fail"){
                                var $fail = $('#message').find(".alert-fail");
                                if($fail.length==0){
                                    $fail = $('<div class="alert-fail"><p></p></div>');
                                    $('#message').append($fail);
                                }
                                var $p = $fail.find('p');
                                $p.html(eval(data)[0].message);
                                $fail.show();
                            }

                            $('#message').find('.alert-success, .alert-fail').click(function(){$(this).hide();});
                            SOP.Common.hideMask();
                        }

                        var _eHandler = function(data, textStatus){
                            SOP.Common.hideMask();
                        }

                        var emailContent = $('#emailContent').html();
                        if(tinymce.activeEditor){
                            emailContent = tinymce.editors[0].getContent();
                        }
                        emailContent = Base64.encode(emailContent);

                        $.ajax({
                            type: "POST",
                            url: '<%=request.getContextPath()%>/process/EGPCLOUD/ApplicationView/SendEmail',
                            data: {emailTo: $('[name="emailTo"]').val(), emailSubject: $('[name="emailSubject"]').val(), emailContent:  emailContent, appId:$('[name="entityUids"]').val() },
                            success: _sHandler,
                            error: _eHandler
                        });
                    }

                    var sendSMS = function(){
                        $('#emailInputForm').find('small.error').css("display","none");
                        $('#smsInputForm small.error').each(function(){
                            var html = $(this).html('');
                            $(this).hide();
                        });

                        var to = $("[name='smsTo']").val();
                        var content = $("[name='smsContent']").val();

                        var hasError = false;
                        if(!to){
                            $('[name="smsTo"]').parent('.text').find('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                            hasError = true;
                        }

                        if(!content){
                            $('[name="smsContent"]').parent('.text').find('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                            hasError = true;
                        }

                        if(hasError){
                            $('#smsInputForm small.error').each(function(){
                                var html = $(this).html();
                                if(html){
                                    $(this).css("background","#f5b9b9");
                                    $(this).css('display','block');
                                }
                            });

                            return;
                        }

                        SOP.Common.showMask();
                        var _sHandler1 = function(data, textStatus){
                            var status = eval(data)[0].status;
                            $('#message').find('div').hide();
                            // $('#emailInputForm').trigger('reset');
                            // $('[name="emailContent"]').html('');
                            if(status == "success"){
                                var $success = $('#message').find(".alert-success");
                                if($success.length==0){
                                    $('#message').append('<div class="alert-success"><p>'+eval(data)[0].message+'</p></div>');
                                }else{
                                    var $p = $success.find('p');
                                    $p.html(eval(data)[0].message);
                                }
                                $success.show();
                                $('[name="smsContent"]').val('');
                                $('#last_content').tabs({selected:2});
                            }else if(status == "fail"){
                                var $fail = $('#message').find(".alert-fail");
                                if($fail.length==0){
                                    $('#message').append('<div class="alert-fail"><p></p></div>');
                                }else{
                                    var $p = $fail.find('p');
                                    $p.html(eval(data)[0].message);
                                }
                                $fail.show();
                            }
                            $('#message').find('.alert-success, .alert-fail').click(function(){$(this).hide();});
                            SOP.Common.hideMask();
                        }

                        var _eHandler1 = function(data, textStatus){
                            SOP.Common.hideMask();
                        }

                        $.ajax({
                            type: "POST",
                            url: '<%=request.getContextPath()%>/process/EGPCLOUD/ApplicationView/SendSMS',
                            data: {smsTo: $('[name="smsTo"]').val(), smsContent: $('[name="smsContent"]').val(), appId:$('[name="entityUids"]').val() },
                            success: _sHandler1,
                            error: _eHandler1
                        });
                    }
                    <%-- var sendEmail = function(){
                        $('#emailInputForm small.error').each(function(){
                            var html = $(this).html('');
                            $(this).hide();
                        });

                        var to = $('[name="emailTo"]').val();
                        var subject = $('[name="emailSubject"]').val();
                        var content = $('#emailContent').text();
                        var hasError = false;
                        if(!to){
                            $('[name="emailTo"]').parent('.text').nextAll('small.error').html('Required Value');
                            hasError = true;
                        }

                        if(!subject){
                            $('[name="emailSubject"]').nextAll('small.error').html('Required Value');
                            hasError = true;
                        }

                        if(!content){
                            $('[name="emailContent"]').parents('.text').find('small.error').html('Required Value');
                            hasError = true;
                        }

                        if(hasError){
                            $('#emailInputForm small.error').each(function(){
                                var html = $(this).html();
                                if(html){
                                    $(this).css("background","#f5b9b9");
                                    $(this).css('display','block');
                                }
                            });

                            return;
                        }


                        SOP.Common.showMask();
                        var _sHandler = function(data, textStatus){
                            var status = eval(data)[0].status;
                            $('#message').find('div').hide();
                            if(status == "success"){
                                var $success = $('#message').find(".alert-success");
                                if($success.length==0){
                                    $('#message').append('<div class="alert-success"><p>'+eval(data)[0].message+'</p></div>');
                                }else{
                                    var $p = $success.find('p');
                                    $p.html(eval(data)[0].message);
                                }
                                $success.show();
                                //$('#emailContent').html('');
                                $('#emailInputForm').trigger('reset')
                                $("a[href='#msgTab-2']").click();
                            }else if(status == "fail"){
                                var $fail = $('#message').find(".alert-fail");
                                if($fail.length==0){
                                    $fail = $('<div class="alert-fail"><p></p></div>');
                                    $('#message').append($fail);
                                }
                                var $p = $fail.find('p');
                                $p.html(eval(data)[0].message);
                                $fail.show();
                            }

                            $('#message').find('.alert-success, .alert-fail').click(function(){$(this).hide();});
                            SOP.Common.hideMask();
                        }

                        var _eHandler = function(data, textStatus){
                            SOP.Common.hideMask();
                        }

                        var emailContent = $('#emailContent').html();
                        if(tinymce.activeEditor){
                            emailContent = tinymce.activeEditor.getContent();
                        }
                        emailContent = Base64.encode(emailContent);

                        $.ajax({
                            type: "POST",
                            url: '<%=request.getContextPath()%>/process/EGOV/ApplicationView/SendEmail',
                            data: {emailTo: $('[name="emailTo"]').val(), emailSubject: $('[name="emailSubject"]').val(), emailContent: emailContent, appId:$('[name="entityUids"]').val() },
                            success: _sHandler,
                            error: _eHandler
                        });
                    }
                     --%>

				</script>
				<div class="text-normal">
					<%-- <egov-smc:fieldLabel name="To" /> --%><label><egov-smc:message key="sendTo">To</egov-smc:message></label> <span
						class="text"> <c:out value="${entity.applicantEmail }" />
						<sop-htmlform:hidden name="emailTo"
											 value="${entity.applicantEmail }" /> <small class="error"></small>
					</span>

				</div>
				<div class="text-normal">
					<%-- <egov-smc:fieldLabel name="Title" /> --%><label><egov-smc:commonLabel>Title</egov-smc:commonLabel></label>
					<sop-htmlform:text name="emailSubject"
									   value="Application for ${entity.svcName } (Application No. ${entity.appNo })"
									   cssStyle="width:420px;padding-left: 5px;" />
					<small class="error"></small>
				</div>
				<div class="text-normal">
					<%-- <egov-smc:fieldLabel name="Content" /> --%><label><egov-smc:commonLabel>Content</egov-smc:commonLabel></label>
					<div id="tinymce-content" class="tinymce-content">
						<div id="emailContent" name="emailContent"
							 class="fade sm-editable"></div>

					</div>
					<small class="error" style="margin-top: 5px; margin-left:140px;"></small>
				</div>
			</form>
			<script>
                <egov-smc:messageTemplate var="resetFormMsg" key="egp.application.view.reset.form.confirm" default="Are you sure you want to reset your form?" />
                var resetInputForm = function(formId){
                    $('#'+formId).find('small.error').css("display","none");
                    SOP.Common.confirm({message:'<egov-core:escapeJavaScript value="${resetFormMsg}" />', func:function(){
                            $('#'+formId).trigger('reset');
                            if($('#'+formId).find('#emailContent').length > 0){

                                $("#emailContent").html('');
							}
                        if($('#'+formId).find('#emailContent2').length > 0){

                            $("#emailContent2").html('');
                        }
							$('#' + formId + ' #smsChars').html(LIMIT_CHARS);
                    	}
                    });
                }
			</script>
			<!--/begin btn-wrap  -->
			<div class="float-rgh" style="margin-right: 6px;">
				<button type="button" onclick="resetInputForm('emailInputForm');"
						class="button" style="width: 112px;">
					<egov-smc:commonLabel>Clear</egov-smc:commonLabel>
				</button>
				<button type="button" onclick="sendEmail();" class="button"
						style="width: 110px;">
					<egov-smc:commonLabel>Send E-mail</egov-smc:commonLabel>
				</button>
			</div>
			<!--/end btn-wrap -->
			<br class="clear"> <br class="clear">
		</div>
		<div id="msgTab-1" style="<c:out value='${cssSMS }'/>">
			<form id="smsInputForm">
				<div class="text-normal">
					<label><%-- <egov-smc:fieldLabel name="To" /> --%><egov-smc:message key="sendTo">To</egov-smc:message></label> <span
						class="text"><c:out value="${entity.applicantMobile }" />
						<sop-htmlform:hidden name="smsTo"
											 value="${entity.applicantMobile }" /> <small class="error"></small>
					</span>
				</div>
				<div class="text-normal">
					<label><%-- <egov-smc:fieldLabel name="Content" /> --%><egov-smc:commonLabel>Content</egov-smc:commonLabel></label> <span
						class="text"> <textarea rows="4" class="width-350px"
												cols="350" id="smsContent" name="smsContent"></textarea> <br>
						<div style="margin-top: 5px; color: #666;">
							<egov-smc:commonLabel>Characters left</egov-smc:commonLabel> :
							<span id="smsChars">160</span>
						</div> <small class="error"></small>
					</span>
				</div>
			</form>
			<!--/begin btn-wrap  -->
			<div class="float-rgh" style="margin-right: 6px;">
				<button type="button" onclick="resetInputForm('smsInputForm')"
						class="button" style="width: 112px;">
					<egov-smc:commonLabel>Clear</egov-smc:commonLabel>
				</button>
				<button type="button" onclick="sendSMS();" class="button"
						style="width: 110px;">
					<egov-smc:commonLabel>Send SMS</egov-smc:commonLabel>
				</button>
			</div>
			<!--/end btn-wrap -->
			<br class="clear"> <br class="clear">
		</div>
		<div id="msgTab-2" style="<c:out value='${cssHistory }'/>">
			<div class="history-content">
				<table width="100%" cellspacing="0"
					   class="table ju-processing-history">
					<!-- Begin Table Head -->
					<thead>
					<tr style="background-color: rgb(246, 246, 246);">
						<th scope="col"><egov-smc:commonLabel>Date Sent</egov-smc:commonLabel></th>
						<th scope="col"><egov-smc:commonLabel>Officer</egov-smc:commonLabel></th>
						<th scope="col"><egov-smc:commonLabel>Type</egov-smc:commonLabel></th>
						<th scope="col"><egov-smc:commonLabel>Message</egov-smc:commonLabel></th>
					</tr>
					</thead>
					<!-- End Table Head -->
					<tbody>
					<c:forEach items="${appMessages }" var="msg">
						<%
							MessageHistory his = (MessageHistory)pageContext.getAttribute("msg");
							String type = MessageHistoryService.getInstance().getSenderTypeLabel(his.getType());
							String dateFormat = ConsistencyHelper.getDateFormat();
							DateFormat df = new SimpleDateFormat(dateFormat, MultiLangUtil.getSiteLocale());
						%>
						<tr>
							<td><%=df.format(his.getCreatedDate())%></td>
							<td><c:out value="${msg.officer }" /></td>
							<td><egov-smc:commonLabel><%=type %></egov-smc:commonLabel></td>
							<td style="padding-left: 23px"><c:out value="${msg.content }" escapeXml="false" /></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<!--End Table Listing-->
		</div>
	</div>

	<br class="clear">

	<div id="confirm_content">
		<script type="text/javascript">
            var sendEmail2 = function(){
                $('#smsInputForm2').find('small.error').css("display","none");
                $('#emailInputForm2 small.error').each(function(){
                    var html = $(this).html('');
                    $(this).hide();
                });

                var to = $('[name="emailTo2"]').val();
                var subject = $('[name="emailSubject2"]').val();
                var content = $('#emailContent2').text();
                var hasError = false;
                if(!to){
                    $('[name="emailTo2"]').parent('.text').find('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                    hasError = true;
                }

                if(!subject){
                    $('[name="emailSubject2"]').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                    hasError = true;
                }

                if(!content){
                    $('[name="emailContent2"]').parent('#tinymce-content2').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                    hasError = true;
                }
                console.debug(hasError);
                if(hasError){
                    $('#emailInputForm2 small.error').each(function(){
                        var html = $(this).html();
                        if(html){
                            $(this).css("background","#f5b9b9");
                            $(this).css('display','block');
                        }
                    });
                    return;
                }

                SOP.Common.showMask();
                var _sHandler = function(data, textStatus){
                    var status = eval(data)[0].status;
                    $('#message2').find('div').hide();
                    if(status == "success"){
                        var $success = $('#message2').find(".alert-success");
                        if($success.length==0){
                            $('#message2').append('<div class="alert-success"><p>'+eval(data)[0].message+'</p></div>');
                        }else{
                            var $p = $success.find('p');
                            $p.html(eval(data)[0].message);
                        }
                        $success.show();
                        //$('#emailContent2').html('');
                        $('#emailInputForm2').trigger('reset');
                        $('#emailContent2').html('');
                        // $('[name="smsContent2"]').val('');
                        $('#confirm_content').tabs({selected:3});
                    }else if(status == "fail"){
                        var $fail = $('#message2').find(".alert-fail");
                        if($fail.length==0){
                            $('#message2').append('<div class="alert-fail"><p></p></div>');
                        }else{
                            var $p = $fail.find('p');
                            $p.html(eval(data)[0].message);
                        }
                        $fail.show();
                    }
                    $('#message2').find('.alert-success, .alert-fail').click(function(){$(this).hide();});
                    SOP.Common.hideMask();
                }

                var _eHandler = function(data, textStatus){
                    SOP.Common.hideMask();
                }

                var emailContent = $('#emailContent2').html();
                if(tinymce.activeEditor){
                    emailContent = tinymce.editors[1].getContent();
                }
                emailContent = Base64.encode(emailContent);

                $.ajax({
                    type: "POST",
                    url: '<%=request.getContextPath()%>/process/EGPCLOUD/ApplicationView/SendEmail',
                    data: {emailTo: $('[name="emailTo2"]').val(), emailSubject: $('[name="emailSubject2"]').val(), emailContent: emailContent, appId:$('[name="entityUids"]').val() },
                    success: _sHandler,
                    error: _eHandler
                });
            }

            var sendSMS2 = function(){
                $('#emailInputForm2').find('small.error').css("display","none");
                $('#smsInputForm2 small.error').each(function(){
                    var html = $(this).html('');
                    $(this).hide();
                });

                var to = $("[name='smsTo2']").val();
                var content = $("[name='smsContent2']").val();

                var hasError = false;
                if(!to){
                    $('[name="smsTo2"]').parent('.text').find('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                    hasError = true;
                }

                if(!content){
                    $('[name="smsContent2"]').parent('.text').find('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                    hasError = true;
                }

                if(hasError){
                    $('#smsInputForm2 small.error').each(function(){
                        var html = $(this).html();
                        if(html){
                            $(this).css("background","#f5b9b9");
                            $(this).css('display','block');
                        }
                    });

                    return;
                }

                SOP.Common.showMask();
                var _sHandler1 = function(data, textStatus){
                    var status = eval(data)[0].status;
                    $('#message2').find('div').hide();
                    if(status == "success"){
                        var $success = $('#message2').find(".alert-success");
                        if($success.length==0){
                            $('#message2').append('<div class="alert-success"><p>'+eval(data)[0].message+'</p></div>');
                        }else{
                            var $p = $success.find('p');
                            $p.html(eval(data)[0].message);
                        }
                        $success.show();
                        $('[name="smsContent2"]').val('');
                        // $('#emailInputForm2').trigger('reset');
                        // $('[name="emailContent2"]').html('');
                        $('#confirm_content').tabs({selected:3});
                    }else if(status == "fail"){
                        var $fail = $('#message2').find(".alert-fail");
                        if($fail.length==0){
                            $('#message2').append('<div class="alert-fail"><p></p></div>');
                        }else{
                            var $p = $fail.find('p');
                            $p.html(eval(data)[0].message);
                        }
                        $fail.show();
                    }
                    $('#message2').find('.alert-success, .alert-fail').click(function(){$(this).hide();});
                    SOP.Common.hideMask();
                }

                var _eHandler1 = function(data, textStatus){
                    SOP.Common.hideMask();
                }

                $.ajax({
                    type: "POST",
                    url: '<%=request.getContextPath()%>/process/EGPCLOUD/ApplicationView/SendSMS',
                    data: {smsTo: $('[name="smsTo2"]').val(), smsContent: $('[name="smsContent2"]').val(), appId:$('[name="entityUids"]').val() },
                    success: _sHandler1,
                    error: _eHandler1
                });
            }
		</script>
		<div class="batContent">
			<div id="message2"></div>
			<c:if test="${needHistory }">
				<ul>
					<li><a href="#msgTab2-0"><egov-smc:commonLabel>Confirm</egov-smc:commonLabel></a></li>
					<c:if test="${needEmail }">
						<li><a href="#msgTab2-1"><egov-smc:commonLabel>E-mail</egov-smc:commonLabel></a></li>
					</c:if>
					<c:if test="${needSMS }">
						<li><a href="#msgTab2-2"><egov-smc:commonLabel>SMS</egov-smc:commonLabel></a></li>
					</c:if>
					<c:if test="${needHistory }">
						<li><a href="#msgTab2-3"><egov-smc:commonLabel>History</egov-smc:commonLabel></a></li>
					</c:if>
				</ul>
			</c:if>
			<div id="msgTab2-0">
				<div class="text-normal">
					<%-- <egov-smc:fieldLabel name="Application No."
							compulsory="true" />  --%><label><egov-smc:commonLabel>No.</egov-smc:commonLabel></label><span class="text"><%=appNo%></span> <small
						class="error"></small>
				</div>

				<div class="text-normal">
					<%-- <egov-smc:fieldLabel name="Current Status"
							compulsory="true" /> --%><label><egov-smc:commonLabel>Current Status</egov-smc:commonLabel></label> <span class="text"><egov-smc:commonLabel><%=displayCurrentStatus%></egov-smc:commonLabel></span>
					<small class="error"></small>
				</div>

				<div class="text-normal">
					<%-- <egov-smc:fieldLabel name="Update Status"
							compulsory="true" /> --%><label><egov-smc:commonLabel>Update Status</egov-smc:commonLabel></label> <span class="text" id="updateStatusDis"></span>
					<small class="error"></small>
				</div>

				<div class="text-normal" id="updateStatusComments">
					<%-- <egov-smc:fieldLabel name="Comments"
							compulsory="true" /> --%><label><egov-smc:commonLabel>Comments</egov-smc:commonLabel></label>
					<textarea rows="6" cols="40" id="comments" class="width-400px"></textarea>
					<small class="error"></small>
				</div>
			</div>
			<div id="msgTab2-1" style="<c:out value='${cssEmail }'/>">
				<form id="emailInputForm2">
					<div class="text-normal">
						<%-- <egov-smc:fieldLabel name="To" compulsory="true" /> --%><label><egov-smc:message key="sendTo">To</egov-smc:message></label>
						<span class="text"><c:out value="${entity.applicantEmail }" />
							<sop-htmlform:hidden name="emailTo2"
												 value="${entity.applicantEmail }" /><small class="error"></small></span>

					</div>
					<div class="text-normal">
						<%-- <egov-smc:fieldLabel name="Title" compulsory="true" /> --%><label><egov-smc:commonLabel>Title</egov-smc:commonLabel></label>
						<sop-htmlform:text name="emailSubject2"
										   value="Application for ${entity.svcName } (Application No. ${entity.appNo })"
										   cssStyle="width:420px;padding-left: 5px;" />
						<small class="error"></small>
					</div>
					<div class="text-normal">
						<%-- <egov-smc:fieldLabel name="Content"
								compulsory="true" /> --%><label><egov-smc:commonLabel>Content</egov-smc:commonLabel></label>
						<div class="tinymce-content" id="tinymce-content2">
							<div class="fade sm-editable" name="emailContent2"
								 id="emailContent2"></div>

						</div><small class="error" style="margin-top: 5px; margin-left:140px;"></small>
					</div>
				</form>
				<br class="clear" />
				<!--/begin btn-wrap  -->
				<div class="float-rgh" style="margin-right: 6px; margin-top: 20px;">
					<button type="button" onclick="resetInputForm('emailInputForm2')"
							class="button" style="width: 110px;">
						<egov-smc:commonLabel>Clear</egov-smc:commonLabel>
					</button>
					<button type="button" onclick="sendEmail2();" class="button"
							style="width: 110px;">
						<egov-smc:commonLabel>Send E-mail</egov-smc:commonLabel>
					</button>
				</div>
				<!--/end btn-wrap -->
				<br class="clear"> <br class="clear">
			</div>
			<div id="msgTab2-2" style="<c:out value='${cssSMS }'/>">
				<form id="smsInputForm2">
					<div class="text-normal">
						<%-- <egov-smc:fieldLabel name="To" compulsory="true" /> --%><label><egov-smc:message key="sendTo">To</egov-smc:message></label>
						<span class="text"><c:out
								value="${entity.applicantMobile }" /> <sop-htmlform:hidden
								name="smsTo2" value="${entity.applicantMobile }" /><small
								class="error"></small></span>

					</div>
					<div class="text-normal">
						<%-- <egov-smc:fieldLabel name="Content"
								compulsory="true" /> --%><label><egov-smc:commonLabel>Content</egov-smc:commonLabel></label> <span class="text"><textarea
							rows="4" class="width-350px" cols="45" id="smsContent2"
							name="smsContent2"></textarea> <br> <span
							style="margin-top: 5px; color: #666;"><egov-smc:commonLabel>Characters left</egov-smc:commonLabel> : <span
							id="smsChars">160</span></span><small class="error"></small></span>

					</div>
				</form>
				<br class="clear" />
				<!--/begin btn-wrap  -->
				<div class="float-rgh" style="margin-right: 6px;">
					<button type="button" onclick="resetInputForm('smsInputForm2')"
							class="button" style="width: 110px;">
						<egov-smc:commonLabel>Clear</egov-smc:commonLabel>
					</button>
					<button type="button" onclick="sendSMS2();" class="button"
							style="width: 110px;">
						<egov-smc:commonLabel>Send SMS</egov-smc:commonLabel>
					</button>
				</div>
				<!--/end btn-wrap -->
				<br class="clear"> <br class="clear">
			</div>
			<div id="msgTab2-3" style="<c:out value='${cssHistory }'/>">
				<div class="history-content">
					<table width="100%" cellspacing="0"
						   class="table ju-processing-history">
						<!-- Begin Table Head -->
						<thead>
						<tr style="background-color: rgb(246, 246, 246);">
							<th scope="col"><egov-smc:commonLabel>Date Sent</egov-smc:commonLabel></th>
							<th scope="col"><egov-smc:commonLabel>Officer</egov-smc:commonLabel></th>
							<th scope="col"><egov-smc:commonLabel>Type</egov-smc:commonLabel></th>
							<th scope="col"><egov-smc:commonLabel>Message</egov-smc:commonLabel></th>
						</tr>
						</thead>
						<!-- End Table Head -->
						<tbody>
						<c:forEach items="${appMessages }" var="msg">
							<%
								MessageHistory his = (MessageHistory)pageContext.getAttribute("msg");
								String type = MessageHistoryService.getInstance().getSenderTypeLabel(his.getType());
								String dateFormat = ConsistencyHelper.getDateFormat();
								DateFormat df = new SimpleDateFormat(dateFormat, MultiLangUtil.getSiteLocale());
							%>
							<tr>
								<td><%=df.format(his.getCreatedDate())%></td>
								<td><c:out value="${msg.officer }" /></td>
								<td><egov-smc:commonLabel><%=type %></egov-smc:commonLabel></td>
								<td><c:out value="${msg.content }" escapeXml="false" /></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<!--End Table Listing-->
			</div>
		</div>
	</div>
	<script type="text/javascript"
			src="<%=webContext %>/_statics/js/egovforms/browserplus-min.js"></script>
	<script type="text/javascript"
			src="<%=webContext %>/_statics/js/egovforms/plupload.full.min.js"></script>
	<script type="text/javascript">
        var internalDoc = {};
        function openUploadDoc(){
            $('#uploadDoc').dialog('open');
        };
        function openSupportDoc(docname,doctype){
            $('#supportDoc input[name="documentName"]').val(docname);
            $('#supportDoc span[name="documentName"]').html(docname);
            var type =  "(<egov-smc:commonLabel>Only support file type</egov-smc:commonLabel>:"+doctype+")";
            $('.documentType').html(type);
            $('#supportDoc').dialog('open');
        };
        function openUpdateSupportDoc(id,docname,doctype){
            $('#updateSupportDoc input[name="documentName"]').val(docname);
            $('#updateSupportDoc span[name="documentName"]').html(docname);
            $('#updateSupportDoc input[name="docId"]').val(id);
            var type =  "(<egov-smc:commonLabel>Only support file type</egov-smc:commonLabel>:"+doctype+")";
            $('.documentType').html(type);
            $('#updateSupportDoc').dialog('open');
        }
        var nameLength = <%=InternalDocUpload.FIELD_DOC_NAME_LEN%>;
        var fileNameLength = <%=InternalDocUpload.FIELD_DOC_FILENAME_LEN%>;

        function validateUploadInternal(){
            var flag = true;
            $('#uploadDoc small.error').html('').hide();
            var name = $('#uploadDoc').find('[name="internalDocName"]').val();
            if(name==""){
                $('[name="internalDocName"]').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                flag = flag && false;
            }else if(name.length>nameLength){
                $('[name="internalDocName"]').nextAll('small.error').html("<egov-smc:message key="lengthMustLessThan"><egov-smc:message key="lengthMustLessThan">The length must less than</egov-smc:message></egov-smc:message> ["+nameLength+"]");
                flag = flag && false;
            }else if(internalDoc[name.toLowerCase()]){
                $('[name="internalDocName"]').nextAll('small.error').html("<egov-smc:message key="fileName">Name</egov-smc:message>("+name+") <egov-smc:commonLabel>is duplicated</egov-smc:commonLabel>");
                flag = flag && false;
            }

            var internalFile = $('#uploadDoc').find('[name="internalFile"]').val();
            if(internalFile==""){
                $('[name="internalFile"]').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                flag = flag && false;
            }else if(internalFile.length>fileNameLength){
                $('[name="internalFile"]').nextAll('small.error').html("<egov-smc:message key="fileNameMustLessThan">The file name must less than</egov-smc:message> ["+fileNameLength+"]");
                flag = flag && false;
            }

            if(!internalDoc[name.toLowerCase()]){
                internalDoc[name.toLowerCase()] = internalFile;
            }

            if(!flag){
                $('#uploadDoc small.error').each(function(){
                    var html = $(this).html();
                    if(html){
                        $(this).css("background","#f5b9b9");
                        $(this).show();
                    }
                });
            }

            return flag;
        }

        function validateUploadSupportDoc(){
            var flag = true;
            $('#supportDoc small.error').html('').hide();
            var name = $('#supportDoc').find('input[name="documentName"]').val();
            if(name==""){
                $('[name="documentName"]').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                flag = flag && false;
            }else if(name.length>nameLength){
                $('[name="documentName"]').nextAll('small.error').html("<egov-smc:message key="lengthMustLessThan">The length must less than</egov-smc:message> ["+nameLength+"]");
                flag = flag && false;
            }

            //else if(internalDoc[name.toLowerCase()]){
            //	$('[name="documentName"]').nextAll('small.error').html("Name("+name+") is duplicated");
            //	flag = flag && false;
            //}

            var supportFile = $('#supportDoc').find('[name="supportFile"]').val();
            if(supportFile==""){
                $('[name="supportFile"]').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                flag = flag && false;
            }else if(supportFile.length>fileNameLength){
                $('[name="supportFile"]').nextAll('small.error').html("<egov-smc:message key="fileNameMustLessThan">The file name must less than</egov-smc:message> ["+fileNameLength+"]");
                flag = flag && false;
            }

            //if(!internalDoc[name.toLowerCase()]){
            //	internalDoc[name.toLowerCase()] = supportFile;
            //}

            if(!flag){
                $('#supportDoc small.error').each(function(){
                    var html = $(this).html();
                    if(html){
                        $(this).css("background","#f5b9b9");
                        $(this).show();
                    }
                })
            }

            return flag;
        }

        function validateUpdateSupportDoc(){
            var flag = true;
            $('#updateSupportDoc small.error').html('').hide();
            var name = $('#updateSupportDoc').find('input[name="documentName"]').val();
            if(name==""){
                $('[name="documentName"]').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                flag = flag && false;
            }else if(name.length>nameLength){
                $('[name="documentName"]').nextAll('small.error').html("<egov-smc:message key="lengthMustLessThan">The length must less than</egov-smc:message> ["+nameLength+"]");
                flag = flag && false;
            }

            //else if(internalDoc[name.toLowerCase()]){
            //	$('[name="documentName"]').nextAll('small.error').html("Name("+name+") is duplicated");
            //	flag = flag && false;
            //}

            var supportFile = $('#updateSupportDoc').find('[name="supportFile"]').val();
            if(supportFile==""){
                $('[name="supportFile"]').nextAll('small.error').html('<egov-smc:messageTemplate key="<%=MessageTemplateConstants.EGP_MSG_COMMON_REQUIRED_VALUE%>" default="Required Value" />');
                flag = flag && false;
            }else if(supportFile.length>fileNameLength){
                $('[name="supportFile"]').nextAll('small.error').html("<egov-smc:message key="fileNameMustLessThan">The file name must less than</egov-smc:message> ["+fileNameLength+"]");
                flag = flag && false;
            }

            //if(!internalDoc[name.toLowerCase()]){
            //	internalDoc[name.toLowerCase()] = supportFile;
            //}

            if(!flag){
                $('#updateSupportDoc small.error').each(function(){
                    var html = $(this).html();
                    if(html){
                        $(this).css("background","#f5b9b9");
                        $(this).show();
                    }
                })
            }

            return flag;
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

        function uploadInternalDoc(){
            if(validateUploadInternal()){
                SOP.Crud.cfxSubmit("fileUploadForm", "uploadInternal");
                $('#uploadDoc').dialog('close');
            }
        };

        function uploadSupportDoc(){
            if(validateUploadSupportDoc()){
                SOP.Crud.cfxSubmit("fileUploadSupportForm", "uploadSupportDoc");
                $('#supportDoc').dialog('close');
            }
        };

        function updateSupportDoc(){
            if(validateUpdateSupportDoc()){
                var docId = $('#updateSupportDoc input[name="docId"]').val();
                SOP.Crud.cfxSubmit("fileUpdateSupportForm", "updateSupportDoc",docId);
                $('#updateSupportDoc').dialog('close');
            }
        };

        function deleteInternalDoc(id){
            SOP.Common.confirm({"message":'<egov-smc:messageTemplate key="egp.application.view.delete.confirm" default="Are you sure you want to delete this file from server?" />', func: function(){SOP.Crud.cfxSubmit("applicationForm", "deleteInternal", id);}});
        };

        function deleteSupportDoc(id){
            SOP.Common.confirm({"message":'<egov-smc:messageTemplate key="egp.application.view.delete.confirm" default="Are you sure you want to delete this file from server?" />', func: function(){SOP.Crud.cfxSubmit("applicationForm", "deleteSupportDoc", id);}});
        };

        <egov-smc:messageTemplate var="uploadInternalDocumentMsg" key="egp.application.view.title.uploadInternalDocument" default="Upload Internal Document" />
        <egov-smc:messageTemplate var="uploadSupportDocumentMsg" key="egp.application.view.title.uploadSupportDocument" default="Upload Support Document" />
        <egov-smc:messageTemplate var="updateSupportDocumentMsg" key="egp.application.view.title.updateSupportDocument" default="Update Support Document" />

        $(function(){
            $('#uploadDoc').dialog({
                'title':' <egov-core:escapeJavaScript value="${uploadInternalDocumentMsg}" />',
                'resizable': false,
                'dialogClass':'form-content',
                'modal': true,
                'autoOpen': false,
                'open':function(){
                    $('#uploadDoc small.error').html('').hide();
                    $('[name="internalDocName"]').focus();
                },
                'width':650,
                "buttons": [ { text: "<egov-core:escapeJavaScript value="${cancelMsg}" />", click: function() { $('#uploadDoc').dialog('close'); }, 'class': 'search-reset' },
                    { text: "<egov-core:escapeJavaScript value="${uploadMsg}" />", click: function() { uploadInternalDoc();}, 'class': 'dialog-button'}
                ]
            });

            $('#supportDoc').dialog({
                'title':' <egov-core:escapeJavaScript value="${uploadSupportDocumentMsg}" />',
                'resizable': false,
                'dialogClass':'form-content',
                'modal': true,
                'autoOpen': false,
                'open':function(){
                    $('#supportDoc small.error').html('').hide();
                },
                'width':650,
                "buttons": [ { text: "<egov-core:escapeJavaScript value="${cancelMsg}" />", click: function() { $('#supportDoc').dialog('close'); }, 'class': 'search-reset' },
                    { text: "<egov-core:escapeJavaScript value="${uploadMsg}" />", click: function() { uploadSupportDoc();}, 'class': 'dialog-button'}
                ]
            });

            $('#updateSupportDoc').dialog({
                'title':' <egov-core:escapeJavaScript value="${updateSupportDocumentMsg}" />',
                'resizable': false,
                'dialogClass':'form-content',
                'modal': true,
                'autoOpen': false,
                'open':function(){
                    $('#updateSupportDoc small.error').html('').hide();
                },
                'width':650,
                "buttons": [ { text: "<egov-core:escapeJavaScript value="${cancelMsg}" />", click: function() { $('#updateSupportDoc').dialog('close'); }, 'class': 'search-reset' },
                    { text: "<egov-core:escapeJavaScript value="${updateMsg}" />", click: function() { updateSupportDoc();}, 'class': 'dialog-button'}
                ]
            });

            $('[name="internalFile"]').change(function(){
                // if(!$('[name="internalDocName"]').val()){
                    var fileName = $(this).val();
                    if (fileName) {
                        fileName = getFileName(fileName);
                    }
                    $('[name="internalDocName"]').val(fileName);
                // }
            });

        });

        function openSupportDocHistory(displayDocName,batchId,rootUrl,appNo,docName){
            getSupportDocHistory(displayDocName,batchId,rootUrl,appNo,docName);
            $('#last_supportDoc').dialog("open");
        };

        function getSupportDocHistory(displayDocName,batchId,rootUrl,appNo,docName){
            $.ajax({
                type: "post",
                url:  "<%=request.getContextPath()%>/getSupportDocHistory",
                data: {batchId:batchId,docName:docName},
                async:false,
                dataType: "json",
                success: function (data) {
                    var trStr ="";

                    $.each(data,function(key,value){
                        var userId =(value.createdUserId!=null)?value.createdUserId:"";
                        trStr +="<tr><td>"+displayDocName+"</td>";
                        var downLink =  rootUrl + "/process/EGOV/DownloadSupportDocument?docId="+value.docId+"&appNo="+appNo;
                        trStr +="<td>"+"<a href=\""+ downLink+"\">"+value.docFilename +"</a>"+"</td>";
                        trStr +="<td>"+formatFileSize(value.docFileSize)+"</td>";
                        trStr +="<td>"+userId+"</td>";
                        var dateTimeFormat = "<%=ConsistencyHelper.getDateTimeFormat()%>"
                        trStr +="<td>"+value.createdDate+"</td></tr>";
                    })
                    $("#support_histort_tr").html(trStr);
                },
                error: function (msg) {
                    alert("error");
                }
            });
        }

        function   formatDate(now)   {
            var now = new Date(now);
            var year = now.getFullYear();
            var month = now.getMonth();
            var monthStr = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

            var date = now.getDate();
            var hour = now.getHours();
            var minute = now.getMinutes();
            var second = now.getSeconds();
            var tt = "AM";
            if (hour >= 12) {
                tt = "PM";
                hour -= 12
            }
            return fixZero(date, 2) + "-" + monthStr[month] + "-" + year + " " + fixZero(hour, 2) + ":" + fixZero(minute, 2) + ":" + fixZero(second, 2) + " " + tt;
        }


        function fixZero(num,length){
            var str=""+num;
            var len=str.length;     var s="";
            for(var i=length;i-->len;){
                s+="0";
            }
            return s+str;
        }

        function formatFileSize(fileSize) {
            if (fileSize < 1024) {
                return fileSize + 'B';
            } else if (fileSize < (1024*1024)) {
                var temp = fileSize / 1024;
                temp = temp.toFixed(2);
                return temp + 'K';
            } else if (fileSize < (1024*1024*1024)) {
                var temp = fileSize / (1024*1024);
                temp = temp.toFixed(2);
                return temp + 'M';
            } else {
                var temp = fileSize / (1024*1024*1024);
                temp = temp.toFixed(2);
                return temp + 'G';
            }
        }

	</script>
	<div id="uploadDoc" class="pop-up-body fileUploadContainer">
		<form name="fileUploadForm" enctype="multipart/form-data"
			  action="<%=process.runtime.continueURL()%>" method="post">
			<%=View.outputCrudHiddenAction()%>
			<div class="required">
				<label><sop-smc:fieldLabel name="Document"
										   compulsory="true" /></label>
				<sop-htmlform:text name="internalDocName"
								   value="${internalDocName }" cssClass="inputtext-required" />
				<br /> <small class="error" style="margin: 0 0 0 140px;"></small>
				<sop-htmlform:hidden name="appNo" value="${appNo }" />
				<sop-htmlform:hidden name="selectedPage" value="${page}" />
				<sop-htmlform:hidden name="stageName" value="${stageName}" />
				<input type="hidden" name="sopEngineTabRef"
					   value="<%=process.rtStatus.getTabRef()%>">
			</div>
			<div class="required">
				<label><sop-smc:fieldLabel name="Upload your files"
										   compulsory="true" /></label>
				<input class = "inputtext-required" name = "internalFile" type="file"/>
				<%-- <sop-htmlform:file name="internalFile" cssClass="inputtext-required" /> --%>
				<br /> <small class="error" style="margin: 0 0 0 140px;"></small>
			</div>
			<br class="clear" />
		</form>
	</div>

	<div id="supportDoc" class="pop-up-body fileUploadContainer">
		<form name="fileUploadSupportForm" enctype="multipart/form-data"
			  action="<%=process.runtime.continueURL()%>" method="post">
			<%=View.outputCrudHiddenAction() %>

			<div class="required">
				<label><sop-smc:fieldLabel name="Document"
										   compulsory="true" /></label> <span name="documentName" class="text"></span>
				<input type="hidden" class="documentName" name="documentName" />
				<sop-htmlform:hidden name="appNo" value="${appNo }" />
				<sop-htmlform:hidden name="selectedPage" value="${page}" />
				<sop-htmlform:hidden name="stageName" value="${stageName}" />
				<input type="hidden" name="sopEngineTabRef"
					   value="<%=process.rtStatus.getTabRef()%>"> <br /> <small
					class="error" style="margin: 0"></small>
			</div>
			<div class="required">
				<label><sop-smc:fieldLabel name="Upload your files"
										   compulsory="true" /></label>
				<input type="file" name = "supportFile" class="inputtext-required"/>
				<%-- <sop-htmlform:file name="supportFile" cssClass="inputtext-required" /> --%>
				<span class="documentType"></span> <br /> <small class="error"
																 style="margin: 0 0 0 140px;"></small>
			</div>
			<br class="clear" />
		</form>
	</div>
	<div id="updateSupportDoc" class="pop-up-body fileUploadContainer">
		<form name="fileUpdateSupportForm" enctype="multipart/form-data"
			  action="<%=process.runtime.continueURL()%>" method="post">
			<%=View.outputCrudHiddenAction() %>

			<div class="required">
				<label><sop-smc:fieldLabel name="Document"
										   compulsory="true" /></label> <span name="documentName" class="text"></span>
				<input type="hidden" class="documentName" name="documentName" />
				<sop-htmlform:hidden name="appNo" value="${appNo }" />
				<sop-htmlform:hidden name="selectedPage" value="${page}" />
				<sop-htmlform:hidden name="stageName" value="${stageName}" />
				<sop-htmlform:hidden name="docId" value="" />
				<input type="hidden" name="sopEngineTabRef"
					   value="<%=process.rtStatus.getTabRef()%>"> <br /> <small
					class="error" style="margin: 0"></small>
			</div>
			<div class="required">
				<label><sop-smc:fieldLabel name="Upload your files"
										   compulsory="true" /></label>
				<input type="file" name = "supportFile" class="inputtext-required"/>
				<%-- <sop-htmlform:file name="supportFile" cssClass="inputtext-required" /> --%>
				<span class="documentType"></span> <br /> <small class="error"
																 style="margin: 0 0 0 140px;"></small>
			</div>
			<br class="clear" />
		</form>
	</div>
</div>
<iframe id="pdfFormIframe" height="0" width="1"></iframe>