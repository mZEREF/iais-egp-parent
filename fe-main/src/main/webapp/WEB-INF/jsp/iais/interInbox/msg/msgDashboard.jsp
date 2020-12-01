<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.service.client.SystemAdminMainFeClient" %>
<%@ page import="java.util.List" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;

    String alertFlag = (String) ParamUtil.getSessionAttr(request, "AlERt__Msg_FLAg_attr");
    if (alertFlag == null) {
        SystemAdminMainFeClient emc = SpringContextHelper.getContext().getBean(SystemAdminMainFeClient.class);
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
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <%@include file="msgMenuPage.jsp" %>
        </div>
        <div class="row">
            <c:if test="${not empty bAnner_AlERt_Msg__atTR || not empty schEdule_AlERt_Msg__atTR}">
                <div class="col-md-12">
                    <c:if test="${not empty schEdule_AlERt_Msg__atTR}">
                        <div class="dashalert alert-info dash-announce">
                            <button aria-label="Close" data-dismiss="alert" class="close" type="button"><span aria-hidden="true">x</span></button>
                            <h3 style="margin-top:0;"><i class="fa fa-wrench"></i> Upcoming Scheduled Maintainace</h3>
                            <c:out value="${schEdule_AlERt_Msg__atTR}" escapeXml="false"/>
                        </div>
                    </c:if>
                    <c:if test="${not empty bAnner_AlERt_Msg__atTR}">
                        <div class="dashalert alert-info dash-announce">
                            <button aria-label="Close" data-dismiss="alert" class="close" type="button"><span aria-hidden="true">x</span></button>
                            <h3 style="margin-top:0;"><i class="fa fa-bell"></i> Announcement</h3>
                            <c:out value="${bAnner_AlERt_Msg__atTR}" escapeXml="false"/>
                        </div>
                    </c:if>
                </div>
            </c:if>
            <div class="col-xs-12">
                <div class="dashboard-gp">
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabInbox" href="#">
                            <p class="dashboard-txt">New Messages</p>
                            <h1 class="dashboard-count">${unreadAndresponseNum}</h1>
                        </a></div>
                    </div>
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabApp" href="#" onclick="msgToAppPage()">
                            <p class="dashboard-txt">Application Drafts</p>
                            <h1 class="dashboard-count">${appDraftNum}</h1>
                        </a></div>
                    </div>
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabLic" href="#" onclick="msgToLicPage()">
                            <p class="dashboard-txt">Active Licences</p>
                            <h1 class="dashboard-count">${licActiveNum}</h1>
                        </a></div>
                    </div>
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile txt-only">
                            <a href="/main-web/eservice/INTERNET/MohAccessmentGuide">
                                <p class="dashboard-txt" style="line-height: 27px;">
                                    Not sure what to do? Let us guide you
                                    <em class="fa fa-angle-right"></em>
                                </p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-footernote">
                    <p class="dashboard-small-txt"><strong>Last Login:</strong> <fmt:formatDate value="${INTER_INBOX_USER_INFO.lastLogin}" pattern="dd/MM/yyyy HH:mm"/> |
                        <strong>Last Activity:</strong>
                        <c:choose>
                            <c:when test="${INTER_INBOX_USER_INFO.functionName != null}">
                                ${INTER_INBOX_USER_INFO.functionName}
                            </c:when>
                            <c:otherwise>
                                N/A
                            </c:otherwise>
                        </c:choose> - Licence No.
                        <c:choose>
                            <c:when test="${INTER_INBOX_USER_INFO.licenseNo != null}">
                                ${INTER_INBOX_USER_INFO.licenseNo}
                            </c:when>
                            <c:otherwise>
                                N/A
                            </c:otherwise>
                        </c:choose> , On <fmt:formatDate value="${INTER_INBOX_USER_INFO.lastLogin}" pattern="dd/MM/yyyy"/></p>
                </div>
            </div>
        </div>
    </div>
</div>
