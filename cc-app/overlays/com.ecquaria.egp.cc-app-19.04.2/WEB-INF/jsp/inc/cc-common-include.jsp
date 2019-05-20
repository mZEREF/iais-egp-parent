<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<script type="text/javascript">
    var webContextPath="<%=EgpcloudPortFactory.webContext%>";
</script>
<%
	// This is to make sure commonInclude.jsp is only included 1 time
	if (request.getAttribute("common-include.jsp.INCLUDED") == null) {
%>

<!-- START of common-include.jsp  -->
<%@ taglib uri="ecquaria/sop/sop-smc" prefix="sop-smc"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/jsp/inc/cache-header.jsp" %>

<%@page import="sop.i18n.Language"%>
<%@page import="sop.iwe.SessionManager"%>
<%@page import="sop.iwe.SessionManager.LoginInformation"%>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.springframework.web.servlet.i18n.SessionLocaleResolver" %>
<%@ page import="com.ecquaria.cloud.mc.api.ConsistencyHelper" %>
<%@ page import="sop.i18n.MultiLangUtil" %>

<%
	request.setAttribute("common-include.jsp.INCLUDED", Boolean.TRUE);
	//Set Language
	LoginInformation loginInfo 
		= SessionManager.getInstance(request).getLoginInfo();
	String charset = "UTF-8";
	String textDirection = null;
	/*if (loginInfo != null) {
		Language lang = loginInfo.getLanguage();
		charset = lang.getCharset();
		textDirection = lang.getTextDirection();
	}*/
	
	pageContext.setAttribute("charset", charset);
	pageContext.setAttribute("textDirection", textDirection);

	response.setContentType("text/html; charset=" + charset);
	String webroot = EngineHelper.getResourcePath();
%>

<meta http-equiv="Content-Type" content="text/html; charset=<c:out value="${charset}" />" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="Ecquaria Government Platform">
<meta name="author" content="Ecquaria Government Platform">
<c:if test="${not empty textDirection}">
<script type="text/javascript"><!-- document.getElementsByTagName('html')[0].dir = '<c:out value="${textDirection}" />'; --></script>
</c:if>



<link rel="shortcut icon" href="<%=webroot%>/_statics/images/ecq.ico" type="image/x-icon" />

<!-- START: CSS -->	
<link href="<%=webroot%>/_statics/css/jquery-ui/smoothness/jquery-ui-custom.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webroot%>/_statics/css/jquery-ui/form-viewer/jquery-ui-all.css" rel="stylesheet" type="text/css" media="all" />

<link href="<%=webroot%>/_themes/cc/css/bootstrap.min.css" rel="stylesheet">
<%--<link href="<%=webroot%>/_themes/cc/css/custom.css" rel="stylesheet">--%>
<link href="<%=webroot%>/_themes/cc/css/general.css" rel="stylesheet">
<link href="<%=webroot%>/_themes/cc/css/responsive.css" rel="stylesheet">
<%--<link href="<%=webroot%>/_themes/cc/css/ecit-style.css" rel="stylesheet">--%>
<link href="<%=webroot%>/_themes/cc/css/font-awesome.min.css" rel="stylesheet">
<link href="<%=webroot%>/_themes/cc/css/datepicker.css" rel="stylesheet">
<link href="<%=webroot%>/_themes/cc/css/printerfriendly.css" rel="stylesheet">

<%--<link media="screen and (min-device-width : 320px) and (max-device-width : 480px), (max-width: 720px),(max-width: 768px)" rel="stylesheet" href="<%=webroot%>/_themes/cc/css/mobile.css"/>--%>

<!--[if IE 6]><link rel="stylesheet" type="text/css" href="<%=webroot%>/_themes/cc/css/ie6.css" media="screen" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" type="text/css" href="<%=webroot%>/_themes/cc/css/ie.css" media="screen" /><![endif]-->

<!-- END: CSS -->

<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.1.4.1.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.3.0.0.js"></script>
<script src="<%=webroot%>/_themes/cc/js/bootstrap.min.js"></script>
<script src="<%=webroot%>/_themes/cc/js/bootstrap-datepicker.js"></script>
<script src="<%=webroot%>/_themes/cc/js/dropdown.js"></script>


<script type="text/javascript">
var $jbs=jQuery.noConflict();
</script>


<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>
<script src="<%=EngineHelper.getContextPath()%>/javascripts/cc/egov.message.js.jsp" type="text/javascript"></script>
<script src="<%=webroot%>/javascripts/egov/egp.common.js" type="text/javascript"></script>

<!-- fyq -->
<script src="<%=webroot%>/javascripts/cc/egov.common.js" type="text/javascript"></script>


<!--[if lt IE 10]>
<script src="<%=webroot%>/_themes/cc/js/jquery-placeholder.js"></script>
<![endif]-->
<%
	String lang=MultiLangUtil.getSiteLanguage();
%>
<%
	String dateFormat= ConsistencyHelper.getDateFormat();
    if (dateFormat.contains("MMMM"))
        dateFormat=dateFormat.replace("MMMM","MM");
	else if (dateFormat.contains("MMM"))
		dateFormat=dateFormat.replace("MMM","M");
	else if (dateFormat.contains("MM"))
		dateFormat=dateFormat.replace("MM","mm");

%>
<script type="text/javascript" charset="UTF-8">

$(document).ready(function() {
	$('.date input').each(function(){
		var $this = $jbs(this);
		$this.datepicker({
			language:"<%=lang%>",
		    calendarWeeks: false,
		    autoclose: true,
		    todayHighlight: true,
		    format: "<%=dateFormat%>",
		}); 
		
		$this.bind("changeDate", function(e) {
			var index = $jbs(".date input").index(e.target);
			if (index) {
				$jbs(".date input:eq(0)").datepicker("setEndDate", e.dates[0] ? e.dates[0] : "");
			}else {
				$jbs(".date input:eq(1)").datepicker("setStartDate", e.dates[0] ? e.dates[0] : "");
			}
		});
		
		
		$this.next(".input-group-addon").click(function(){
			$this.datepicker("show");
		}).css("cursor", "pointer");
	});
	
	$("form").submit(function(){
		$("input[name='searchPanelStatus']").val($("#slickbox").is(":visible"));
	});
});


</script>


<!-- END of common-include.jsp  -->
<%}%>