<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.client.ErrorMsgClient" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>

<%-- BEGIN taglib --%>
<%@ taglib uri="ecquaria/sop/layout" prefix="layout"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- END taglib --%>

<html lang="en">
<head>
    <title><c:out value="${iais_Audit_Trail_dto_Attr.functionName}" default="HALP"/></title>
    <%
        String alertFlag = (String) ParamUtil.getSessionAttr(request, "AlERt__Msg_FLAg_attr");
        if (alertFlag == null) {
            ErrorMsgClient emc = SpringContextHelper.getContext().getBean(ErrorMsgClient.class);
            List<MsgTemplateDto> msgTemplateDtoList = emc.getAlertMsgTemplate(AppConsts.DOMAIN_INTERNET).getEntity();
            if (IaisCommonUtils.isEmpty(msgTemplateDtoList)) {
                ParamUtil.setSessionAttr(request, "AlERt__Msg_FLAg_attr", "noneed");
            } else {
                for (MsgTemplateDto mt : msgTemplateDtoList) {
                    String msgContent = mt.getMessageContent().replaceAll("\r", "");
                    msgContent = msgContent.replaceAll("\n", "");
                    msgContent = msgContent.replaceAll("'", "&apos;");
                    if (MsgTemplateConstants.MSG_TEMPLATE_BANNER_ALERT_FE.equals(mt.getId())) {
                        ParamUtil.setSessionAttr(request, "bAnner_AlERt_Msg__atTR", msgContent);
                    } else if (MsgTemplateConstants.MSG_TEMPLATE_SCHEDULE_MAINTENANCE_FE.equals(mt.getId())) {
                        ParamUtil.setSessionAttr(request, "schEdule_AlERt_Msg__atTR", msgContent);
                    }
                }
                ParamUtil.setSessionAttr(request, "AlERt__Msg_FLAg_attr", "fetched");
            }
        }
    %>
    <%@ include file="/WEB-INF/jsp/inc/iais-internet-common-include.jsp" %>

    <%-- BEGIN additional header --%>
    <layout:insertAttribute name="header-ext" ignore="true" />
    <%-- END additional header --%>

</head>
<body style="font-size:16px;">
<jsp:include page="header.jsp" />
<div class="">
    <layout:insertAttribute name="body" ignore="true" />
</div>
<jsp:include page="footer.jsp" />
</body>
</html>