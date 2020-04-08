<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="java.util.Locale" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<!-- start of /_themes/sop6/jsp/layout.jsp -->
<%@ page errorPage="/SystemErrorPage.jsp"%>

<%-- BEGIN imports --%>
<%-- END imports --%>

<%-- BEGIN taglib --%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%-- END taglib --%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/WEB-INF/jsp/inc/iais-intranet-common-include.jsp" %>
	<%-- BEGIN additional header --%>
	<tiles:insertAttribute name="header-ext" ignore="true" />
	<%-- END additional header --%>


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
<div class="wrapper">
		<jsp:include page="header.jsp" flush="true"/>
	<tiles:insertAttribute name="body" ignore="true" />
</div>
<br class="clear"/>
<jsp:include page="footer.jsp" />
</body>
</html>
<!-- end of /_themes/sop6/jsp/layout.jsp -->