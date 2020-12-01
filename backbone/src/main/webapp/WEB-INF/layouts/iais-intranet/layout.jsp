<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.client.ErrorMsgClient" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
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
	<%
		String alertFlag = (String) ParamUtil.getSessionAttr(request, "AlERt__Msg_FLAg_attr");
		if (alertFlag == null) {
			ErrorMsgClient emc = SpringContextHelper.getContext().getBean(ErrorMsgClient.class);
			List<MsgTemplateDto> msgTemplateDtoList = emc.getAlertMsgTemplate(AppConsts.DOMAIN_INTRANET).getEntity();
			if (IaisCommonUtils.isEmpty(msgTemplateDtoList)) {
				ParamUtil.setSessionAttr(request, "AlERt__Msg_FLAg_attr", "noneed");
			} else {
				for (MsgTemplateDto mt : msgTemplateDtoList) {
					String msgContent = mt.getMessageContent().replaceAll("\r", "");
					msgContent = msgContent.replaceAll("\n", "");
					msgContent = msgContent.replaceAll("'", "&apos;");
					if (MsgTemplateConstants.MSG_TEMPLATE_BANNER_ALERT_BE.equals(mt.getId())) {
						ParamUtil.setSessionAttr(request, "bAnner_AlERt_Msg__atTR", msgContent);
					} else if (MsgTemplateConstants.MSG_TEMPLATE_SCHEDULE_MAINTENANCE_BE.equals(mt.getId())) {
						ParamUtil.setSessionAttr(request, "schEdule_AlERt_Msg__atTR", msgContent);
					}
				}
				ParamUtil.setSessionAttr(request, "AlERt__Msg_FLAg_attr", "fetched");
			}
		}
	%>
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