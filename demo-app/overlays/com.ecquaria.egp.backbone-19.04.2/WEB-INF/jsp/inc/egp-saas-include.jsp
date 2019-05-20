<%@page import="sop.webflow.rt.engine5.flow.FlowConstants"%>
<%
    // This is to make sure commonInclude.jsp is only included 1 time
    if (request.getAttribute("egp-saas-include.jsp.INCLUDED") == null) {
%>
<!-- START of egp-saas-include.jsp  -->
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/jsp/inc/cache-header.jsp" %>

<%
    request.setAttribute("egp-saas-include.jsp.INCLUDED", Boolean.TRUE);
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

<%@page import="sop.iwe.SessionManager"%>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>

<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.1.4.1.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.3.0.0.js"></script>
<script type="text/javascript">
	var $jbs=jQuery.noConflict();
</script>
<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>
<script src="<%=webroot%>/javascripts/egov/egp.common.js" type="text/javascript"></script>
<script src="<%=webroot%>/sample/saas-new/assets/js/bootstrap.min.js" type="text/javascript"></script>
<script>
    bootstrapButton = $.fn.button.noConflict();
    $.fn.bootstrapBtn = bootstrapButton;
</script>
<script src="<%=webroot%>/sample/saas-new/assets/js/dropdown.js" type="text/javascript"></script>

<link href="<%=webroot%>/_statics/css/jquery-ui/smoothness/jquery-ui-custom.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=webroot%>/sample/saas-new/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=webroot%>/sample/saas-new/assets/css/font-awesome.min.css" rel="stylesheet">
<link href="<%=webroot%>/sample/saas-new/assets/css/general.css" rel="stylesheet">
<link href="<%=webroot%>/sample/saas-new/assets/css/responsive.css" rel="stylesheet">
<link href="<%=webroot%>/sample/saas-new/assets/css/sop.css" rel="stylesheet">

<!-- Custom styles for this template -->
<%--<link href="<%=webroot%>/sample/saas/assets/css/custom.css" rel="stylesheet">--%>
<%--<link media="screen and (min-device-width : 320px) and (max-device-width : 480px), (max-width: 720px), (max-width: 375px)" rel="stylesheet" href="<%=webroot%>/sample/saas/assets/css/mobile.css"/>--%>


<link rel="shortcut icon" href="<%=webroot%>/sample/saas-new/assets/ico/favicon.ico.png" type="image/x-icon">
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="<%=webroot%>/sample/saas-new/assets/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="<%=webroot%>/sample/saas-new/assets/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="<%=webroot%>/sample/saas-new/assets/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="<%=webroot%>/sample/saas-new/assets/ico/apple-touch-icon-57-precomposed.png">


<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>  
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>

<![endif]-->
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
<%
    	}
    } %>
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
})();


</script>
<script type="text/javascript">
  $(function(){
   $('button.close').click(function(){
     $(this).parent('div.alert').hide();
   }); 
  })
</script>
<%
	pageContext.setAttribute("egov_nextIsForm", null);
	pageContext.setAttribute("egov_componentType", null);
%>
<!-- END of egp-saas-include.jsp  -->
<%}%>