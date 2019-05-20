<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- 
	start of /WEB-INF/layouts/egp-blank/layout.jsp 
	this layout is used by egp form and file upload component, didn't include the stage indicate.
-->
<%@ page errorPage="/SystemErrorPage.jsp"%>

<%-- BEGIN imports --%>
<%@ page import="ecq.commons.helper.StringHelper"%>
<%@ page import="ecq.commons.helper.HttpHelper"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%-- END imports --%>

<%-- BEGIN taglib --%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%-- END taglib --%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title><tiles:insertAttribute name="title" ignore="true" /></title>

<%@ include file="/WEB-INF/jsp/inc/egp-payment-include.jsp" %>

<%-- BEGIN additional header --%>
<tiles:insertAttribute name="header-ext" ignore="true" />
<%-- END additional header --%>
</head>
<body class="nobg">
	<tiles:insertAttribute name="body" ignore="true" />
</body>
</html>
<!-- end of /WEB-INF/layouts/egp-blank/layout.jsp -->