<%@page import="sop.webflow.rt.engine5.flow.FlowConstants"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<script type="text/javascript">
    var webContextPath="<%=EgpcloudPortFactory.webContext%>";
</script>
<%
    // This is to make sure commonInclude.jsp is only included 1 time
    if (request.getAttribute("common-include.jsp.INCLUDED") == null) {
%>
<!-- START of common-include.jsp  -->
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<%
    request.setAttribute("common-include.jsp.INCLUDED", Boolean.TRUE);
%>

<meta http-equiv="Content-Type" content="text/html;" />

<link rel="shortcut icon" href="/_themes/sop6/images/ecq.ico" type="image/x-icon" />

<!-- START: CSS --> 
<link href="<%=EgpcloudPortFactory.webContext%>/_statics/css/jquery-ui/smoothness/jquery-ui-custom.css" />
<link href="<%=EgpcloudPortFactory.webContext%>/_statics/css/core/core.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=EgpcloudPortFactory.webContext%>/_themes/sop6/css/template.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=EgpcloudPortFactory.webContext%>/_themes/sop6/css/custom.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=EgpcloudPortFactory.webContext%>/_themes/sop6/css/jquery.treeview.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=EgpcloudPortFactory.webContext%>/_themes/sop6/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" media="all" />
<!-- END: CSS -->   

<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>

<%
    Object nextIsForm = request.getAttribute(FlowConstants.ATTR_NEXT_IS_FORM);
    pageContext.setAttribute("egov_nextIsForm", nextIsForm);
    
    Object componentType = request.getAttribute(FlowConstants.ATTR_COMPONENT_TYPE);
    pageContext.setAttribute("egov_componentType", componentType);
%>

<%
	pageContext.setAttribute("egov_nextIsForm", null);
	pageContext.setAttribute("egov_componentType", null);
%>
<!-- END of common-include.jsp  -->
<%}%>