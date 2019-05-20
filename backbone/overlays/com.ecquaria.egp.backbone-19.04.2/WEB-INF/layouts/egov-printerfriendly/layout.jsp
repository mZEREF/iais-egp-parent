<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!-- start of /WEB-INF/layouts/blank/layout.jsp -->
<%@ page errorPage="/SystemErrorPage.jsp"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%-- BEGIN imports --%>
<%@ page import="ecq.commons.helper.StringHelper"%>
<%@ page import="ecq.commons.helper.HttpHelper"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="sop.i18n.*"%>
<%-- END imports --%>

<%-- BEGIN taglib --%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>

<%-- END taglib --%>

<html>
<head>
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<%@ include file="/WEB-INF/jsp/inc/egov-common-include.jsp" %>

<link href="<%=EgpcloudPortFactory.webContext%>/_themes/cc/css/print.css" rel="stylesheet" type="text/css" media="all" />
<%-- BEGIN additional header --%>
<tiles:insertAttribute name="header-ext" ignore="true" />
<%-- END additional header --%>
</head>
<body class="mesgcenter">
	<tiles:insertAttribute name="body" ignore="true" />
</body>
</html>
<!-- end of /WEB-INF/layouts/blank/layout.jsp -->