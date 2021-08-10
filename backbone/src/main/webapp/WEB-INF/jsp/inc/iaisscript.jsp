<!-- START: javascript -->
<%@page import="com.ecquaria.cloud.helper.ConfigHelper"%>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="com.ecquaria.egp.core.common.constants.AppConstants" %>
<%@ page import="com.ecquaria.egp.core.helper.ConsistencyHelper" %>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@ page import="sop.i18n.MultiLangUtil" %>

<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/reset.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-upgrade-patch.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/jquery.dimensions.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/jquery.positionBy.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/jquery.bgiframe-2.1.1.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/jquery.jdMenu.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/hint.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/jquery-ui-custom.min.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/sop.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getContextPath()%>/javascripts/sop.js.jsp"></script>
<%
    String dateFormatJava =  ConsistencyHelper.getDateFormat();
    request.getSession().setAttribute("dateFormatJava",dateFormatJava);
%>
<script type="text/javascript">
    var webContextPath = "<%=EgpcloudPortFactory.webContext%>";
    var dateFormatJava = "${dateFormatJava}";
    var dateFormatCommon = SOP.javaDateFormatToJsDateFormat(dateFormatJava);
    var pleaseWaitStr = "<%=MultiLangUtil.translate(request,AppConstants.KEY_TRANSLATION_MODULE_LABEL,"Please Wait")%>";
</script>
<script type="text/javascript" src="<%=EngineHelper.getContextPath()%>/javascripts/sop/sop.message.js.jsp"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/sop.util.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/sop.common.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/sop.crud.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/sopAjax.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/jquery.treeview.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/jquery.dd.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/base64.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/sopweb-base.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getContextPath()%>/javascripts/sop/sopweb-base.js.jsp"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/tooltip.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/sop/timepicker.js"></script>
<!-- END: javascript -->
<%
    boolean aBoolean = ConfigHelper.getBoolean("owasp.csrf.enable", false);
    if(aBoolean){
%>

<script src="<%=request.getContextPath()%>/JavaScriptServlet"></script>
<%
    }
%>