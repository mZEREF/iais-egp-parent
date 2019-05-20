<%@page import="sop.config.ConfigUtil"%>
<%
    // This is to make sure commonInclude.jsp is only included 1 time
    if (request.getAttribute("egp-payment-include.jsp.INCLUDED") == null) {
%>
<!-- START of egp-payment-include.jsp  -->
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/jsp/inc/cache-header.jsp" %>

<%
    request.setAttribute("egp-payment-include.jsp.INCLUDED", Boolean.TRUE);
    //Set Language
    LoginInformation loginInfo 
        = SessionManager.getInstance(request).getLoginInfo();
    String charset = "UTF-8";
    String textDirection = null;
    //TODO CDQ
  /*   if (loginInfo != null) {
        Language lang = loginInfo.getLanguage();
        charset = lang.getCharset();
        textDirection = lang.getTextDirection();
    } */
    
    pageContext.setAttribute("charset", charset);
    pageContext.setAttribute("textDirection", textDirection);

    response.setContentType("text/html; charset=" + charset);
    String webContext = ConfigUtil.getWebBathPath(request);
%>

<meta http-equiv="Content-Type" content="text/html; charset=<c:out value="${charset}" />" />
<c:if test="${not empty textDirection}">
<script type="text/javascript"><!-- document.getElementsByTagName('html')[0].dir = '<c:out value="${textDirection}" />'; --></script>
</c:if>
<!--  TODO CDQ-->
<%-- 
<%@page import="sop.i18n.Language"%> --%>
<%@page import="sop.iwe.SessionManager"%>
<%@page import="sop.iwe.SessionManager.LoginInformation"%>

<link rel="shortcut icon" href="<%=webContext %>/_statics/images/ecq.ico" type="image/x-icon" />

<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>
<script src="<%=webContext %>/javascripts/egov/egp.common.js" type="text/javascript"></script>
<link href="<%=webContext %>/_statics/otc/css/bootstrap.min.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webContext %>/_statics/otc/css/callout.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webContext %>/_statics/css/jquery-ui/smoothness/jquery-ui-custom.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webContext %>/_statics/css/jquery-ui/form-viewer/jquery-ui-all.css" rel="stylesheet" type="text/css" />
<link href="<%=webContext %>/_themes/egov/css/custom.css" rel="stylesheet" type="text/css" media="all" />

<script type="text/javascript">

    (function() {
        EGP.Common.getLocation();
    })();


</script>

<%
	pageContext.setAttribute("egov_nextIsForm", null);
	pageContext.setAttribute("egov_componentType", null);
%>
<!-- END of egp-payment-include.jsp  -->
<%}%>