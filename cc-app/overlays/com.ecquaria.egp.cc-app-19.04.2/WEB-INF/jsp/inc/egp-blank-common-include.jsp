<%@page import="com.ecquaria.egp.core.bat.FormHelper"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="sop.webflow.rt.engine5.flow.FlowConstants"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%
    // This is to make sure commonInclude.jsp is only included 1 time
    if (request.getAttribute("egp-blank-common-include.jsp.INCLUDED") == null) {
%>
<!-- START of egp-blank-common-include.jsp  -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/jsp/inc/cache-header.jsp" %>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%
    request.setAttribute("egp-blank-common-include.jsp.INCLUDED", Boolean.TRUE);
    //Set Language
    String charset = "UTF-8";
    
    pageContext.setAttribute("charset", charset);

    response.setContentType("text/html; charset=" + charset);
	String webroot = EngineHelper.getResourcePath();
%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<%@page import="sop.iwe.SessionManager"%>
<%@page import="sop.iwe.SessionManager.LoginInformation"%>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>


<link rel="shortcut icon" href="<%=webroot%>/_themes/egov/images/ecq.ico" type="image/x-icon" />

<!-- START: CSS -->
<link href="<%=webroot%>/_statics/css/jquery-ui/smoothness/jquery-ui-custom.css" type="text/css" rel="stylesheet" media="all"/>
<link href="<%=webroot%>/_statics/css/jquery-ui/form-viewer/jquery-ui-all.css" type="text/css" rel="stylesheet" media="all"/>
<link href="<%=webroot%>/_statics/css/core/core.css" rel="stylesheet" type="text/css" media="all" />

<link href="<%=webroot%>/_themes/egov/css/template.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webroot%>/_themes/egov/css/custom.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webroot%>/_themes/egov/css/jquery.treeview.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webroot%>/_themes/egov/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" media="all" />
<!-- END: CSS -->

<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.1.4.1.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.3.0.0.js"></script>
<script type="text/javascript">
	var $jbs=jQuery.noConflict();
</script>

<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>
<script src="<%=webroot%>/javascripts/egov/egp.common.js" type="text/javascript"></script>
<%
	Object nextIsForm = request.getAttribute(FlowConstants.ATTR_NEXT_IS_FORM);
	pageContext.setAttribute("egov_nextIsForm", nextIsForm);

	sop.webflow.rt.api.BaseProcessClass process = (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
	if(process.rtStatus!=null){
		if(process.rtStatus.getCurrentComponent() != null){
			String formSetupMode = process.rtStatus.getCurrentComponent().getElement().getAttribute("formSetupMode");
			pageContext.setAttribute("formSetupMode", formSetupMode);
			String formSetupSelector =  process.rtStatus.getCurrentComponent().getElement().getAttribute("formSetupSelector");
			String formSetupWidth =  process.rtStatus.getCurrentComponent().getElement().getAttribute("formSetupWidth");
			String formSetupHeight =  process.rtStatus.getCurrentComponent().getElement().getAttribute("formSetupHeight");

			Object componentType = request.getAttribute(FlowConstants.ATTR_COMPONENT_TYPE);
			pageContext.setAttribute("egov_componentType", componentType);
%>

<c:if test="${egov_nextIsForm && egov_componentType == 'WebPage'}">
	<script type="text/javascript">
		jQuery(document).ready(function() {
			<c:choose>
			<c:when test="${formSetupMode == 'DIV'}">
			EGP.Common.setupFormDIV($(".__egovform")[0], '<%= formSetupSelector %>', {
				width: '<%= formSetupWidth %>',
				height: '<%= formSetupHeight %>'
			});
			</c:when>
			<c:otherwise>
			EGP.Common.setupFormDialog({
				form: $(".__egovform")[0],
				selector: "input:submit"
			});
			</c:otherwise>
			</c:choose>
		});
	</script>
</c:if>
<%}
}%>

<c:if test="${!egov_nextIsForm && (egov_componentType == 'Form' || egov_componentType == 'FormLink' || egov_componentType == 'FileUpload')}">
	<script type="text/javascript">
		jQuery(document).ready(function() {

			$("form").attr("target", "_parent");

		});
	</script>
</c:if>

<script type="text/javascript">
    var webContextPath= "<%=EgpcloudPortFactory.webContext%>";
	(function() {
		EGP.Common.setAutoWidth4Selector();
        EGP.Common.getLocation();
	})();
</script>
<%
	pageContext.setAttribute("egov_nextIsForm", null);
	pageContext.setAttribute("egov_componentType", null);
%>
<!-- END of egp-blank-common-include.jsp  -->
<%}%>