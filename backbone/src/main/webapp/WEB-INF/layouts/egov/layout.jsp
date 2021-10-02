<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- start of /_themes/sop6/jsp/layout.jsp -->
<%@ page errorPage="/SystemErrorPage.jsp"%>

<%-- BEGIN imports --%>
<%-- END imports --%>

<%-- BEGIN taglib --%>
<%@ taglib uri="ecquaria/sop/layout" prefix="layout"%>
<%-- END taglib --%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><layout:insertAttribute name="title" ignore="true" /></title>
<%@ include file="/WEB-INF/jsp/inc/egp-common-include.jsp" %>
<%-- BEGIN additional header --%>
<layout:insertAttribute name="header-ext" ignore="true" />
<%-- END additional header --%>

<script type="text/javascript">
<!--
(function() {
	var test = SOP.Common.load(function(){
		SOP.Common.initSmcContent();
		$(window).resize(function(){
			SOP.Common.initSmcContent(true);
			SOP.Common.initSmcContent();
		});
		SOP.Common.setupMainMenu('main-menu', {disableLinks: false});
		SOP.Common.setupMessageDisplay();
		SOP.Common.setupHint();
		SOP.Common.toggleMainMenu('sop-left');
		SOP.Common.setupPopupMenu('shortcut-listing-task', 'click');
		SOP.Common.setupPopupMenu('shortcut-listing-alert', 'click');
		SOP.Common.setupPopupMenu('shortcut-listing-announce', 'click');
		SOP.Crud.initCrudChangePageSize();
		SOP.Common.initTextArea();
	});
})();
-->
</script>
	<%
		Locale locale = MultiLangUtil.getSiteLocale();
		if(locale.getLanguage().equals("zh")){
			%>
			<script src="<%=EngineHelper.getResourcePath()%>/sds/js/jqui/i18n/jquery-ui-datepicker-<%=locale.toString()%>.js"></script>
			<%
		}
	%>

</head>
<body>
<div class="content-wrapper">
		<div class="sop-top">
			<%@ include file="header.jsp" %>
		</div>
		<a href="javascript:;" id="show-menu">&#xBB;</a>
		<div id="sop-left">
			<%@ include file="user-info.jsp" %>
			<%@ include file="left-menu.jsp" %>
		</div>
    
		<div id="content">
			<div class="inner">
				<egov-core:breadcrumb-trail />
				<egov-core:eServiceTitleTrail />
				<%@ include file="../stage-indicator.jsp" %>
				<layout:insertAttribute name="body" ignore="true" />

				<br class="clear"/>
				<%@ include file="footer.jsp" %>
			</div>
		</div> 
	</div>
</body>
</html>
<!-- end of /_themes/sop6/jsp/layout.jsp -->