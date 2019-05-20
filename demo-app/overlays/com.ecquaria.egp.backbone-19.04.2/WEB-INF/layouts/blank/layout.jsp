<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- start of /WEB-INF/layouts/blank/layout.jsp -->
<%@ page errorPage="/SystemErrorPage.jsp"%>

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
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title><tiles:insertAttribute name="title" ignore="true" /></title>

<%@ include file="/WEB-INF/jsp/inc/common-include.jsp" %>

<%-- BEGIN additional header --%>
<tiles:insertAttribute name="header-ext" ignore="true" />
<%-- END additional header --%>
</head>
<body class="nobg">
	<tiles:insertAttribute name="body" ignore="true" />
</body>
</html>
<!-- end of /WEB-INF/layouts/blank/layout.jsp -->