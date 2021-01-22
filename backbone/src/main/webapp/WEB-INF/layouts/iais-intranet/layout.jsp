<%@ page import="java.util.Locale" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<!-- start of /_themes/sop6/jsp/layout.jsp -->
<%@ page errorPage="/SystemErrorPage.jsp"%>

<%-- BEGIN imports --%>
<%-- END imports --%>

<%-- BEGIN taglib --%>
<%@ taglib uri="ecquaria/sop/layout" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- END taglib --%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title><c:out value="${iais_Audit_Trail_dto_Attr.functionName}" default="HALP"/></title>
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
	<c:if test="${not empty iais_Login_User_Info_Attr}">
		<%@ include file="/WEB-INF/layouts/time-out/sessionTimeOut.jsp" %>
	</c:if>
</head>
<body>
<div class="wrapper">
		<jsp:include page="header.jsp" flush="true"/>
	<nav id="sidebar">
	  <jsp:include page="user-info.jsp" flush="true"/>
		<jsp:include page="left-menu.jsp" />
	</nav>
	<layout:insertAttribute name="body" ignore="true" />
</div>
<br class="clear"/>
<jsp:include page="footer.jsp" />
</body>
</html>
<!-- end of /_themes/sop6/jsp/layout.jsp -->