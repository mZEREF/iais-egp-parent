<%@page import="sop.webflow.rt.engine5.flow.FlowConstants"%>
<%@ page import="sop.iwe.SessionManager" %>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>

<%
    // This is to make sure commonInclude.jsp is only included 1 time
    if (request.getAttribute("common-include.jsp.INCLUDED") == null) {
%>
<!-- START of common-include.jsp  -->
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
    request.setAttribute("common-include.jsp.INCLUDED", Boolean.TRUE);
    SessionManager.LoginInformation loginInfo
            = SessionManager.getInstance(request).getLoginInfo();

    String charset = "UTF-8";
    String textDirection = null;
    String langCode = "";
    String countryCode = "";
    String localeForDatePicker = "";

    /*if (loginInfo != null) {
        Language lang = loginInfo.getLanguage();
        charset = lang.getCharset();
        textDirection = lang.getTextDirection();

        langCode = lang.getLanguageCode();
        countryCode = lang.getCountryCode();
        if(StringHelper.isEmpty(langCode)){
            localeForDatePicker = "en_GB";
        }else{
            localeForDatePicker = langCode + "_" + countryCode;
        }
    }*/

    pageContext.setAttribute("charset", charset);
    pageContext.setAttribute("textDirection", textDirection);
    response.setContentType("text/html; charset=" + charset);
    String webroot = EngineHelper.getResourcePath();
%>

<meta http-equiv="Content-Type" content="text/html; charset=<c:out value="${charset}" />" />
<c:if test="${not empty textDirection}">
    <script type="text/javascript"><!-- document.getElementsByTagName('html')[0].dir = '<c:out value="${textDirection}" />'; --></script>
</c:if>

<link rel="shortcut icon" href="<%=webroot%>/_themes/egov/images/ecq.ico" type="image/x-icon" />

<!-- START: CSS --> 
<link href="<%=webroot%>/_statics/css/jquery-ui/smoothness/jquery-ui-custom.css" rel="stylesheet" type="text/css" />
<link href="<%=webroot%>/_statics/css/jquery-ui/form-viewer/jquery-ui-all.css" rel="stylesheet" type="text/css" />
<link href="<%=webroot%>/_statics/css/core/core.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webroot%>/_themes/egov/css/template.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webroot%>/_themes/egov/css/custom.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webroot%>/_themes/egov/css/jquery.treeview.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webroot%>/_themes/egov/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" media="all" />
<!-- END: CSS -->   

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
        jQuery(window).load(function() {
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
    (function() {
        EGP.Common.setAutoWidth4Selector();
        EGP.Common.getLocation();
    })();
</script>
<%
    pageContext.setAttribute("egov_nextIsForm", null);
    pageContext.setAttribute("egov_componentType", null);
%>
<!-- END of common-include.jsp  -->
<%}%>