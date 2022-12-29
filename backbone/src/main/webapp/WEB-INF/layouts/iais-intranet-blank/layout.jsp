<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="java.util.Locale" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=iso-8859-1" language="java"%>
<!-- start of /_themes/sop6/jsp/layout.jsp -->
<%@ page errorPage="/SystemErrorPage.jsp"%>

<%-- BEGIN imports --%>
<%-- END imports --%>

<%-- BEGIN taglib --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/layout" prefix="layout"%>
<%-- END taglib --%>

<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Pragma" content="no-cache"/>
	<meta http-equiv="Cache Control" content="no-store"/>
	<meta http-equiv="Expires" content="0"/>
	<meta name="description" content="HALP e-licensing portal for healthcare services, data submission, Inspection"/>
	<title><c:out value="${iais_Audit_Trail_dto_Attr.functionName}" default="Healthcare Application and Licensing Portal"/></title>
	<link rel="shortcut icon" href="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT%>img/favicon.ico"/>
	<%@ include file="/WEB-INF/jsp/inc/iais-intranet-common-include.jsp" %>
	<%-- BEGIN additional header --%>
	<layout:insertAttribute name="header-ext" ignore="true" />
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
<jsp:include page="header.jsp" flush="true"/>
<div class="">
	<layout:insertAttribute name="body"  ignore="true" />
</div>
<br class="clear"/>
<jsp:include page="footer.jsp" />
</body>
</html>
<!-- end of /_themes/sop6/jsp/layout.jsp -->