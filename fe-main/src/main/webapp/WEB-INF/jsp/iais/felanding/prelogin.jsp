<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/28/2020
  Time: 10:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.client.ErrorMsgClient" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<%@ page import="com.ecquaria.cloud.moh.iais.service.client.SystemAdminMainFeClient" %>
<%@ page import="com.ncs.secureconnect.sim.lite.SIMConfig" %>
<%@ page import="java.util.List" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

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
<webui:setLayout name="iais-internet"/>

<div class="main-content">
<c:if test="${not empty bAnner_AlERt_Msg__atTR || not empty schEdule_AlERt_Msg__atTR}">
  <div class="col-md-12" style="margin-top:10px;">
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
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="prelogin" style="background-image: url('/web/themes/fe/img/prelogin-masthead-banner.jpg');">
      <div class="container">
        <div class="row">
          <div class="col-xs-12">
            <div class="prelogin-title">
              <h1>Integrated Application and <br class="hidden-xs"> Inspection System (IAIS)</h1>
              <p class="component-desc">Manage all licence-related matters associated with your healthcare services.</p>
            </div>
              <div class="modal fade" id="confirmTemplateModal" tabindex="-1" role="dialog" aria-labelledby="confirmTemplateModal" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                <div class="modal-dialog" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                      <h5 class="modal-title" id="gridSystemModalLabel">Confirmation Box</h5>
                    </div>
                    <div class="modal-body">
                      <div class="row">
                        <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem">You already have UEN, please login with corppass</span></div>
                      </div>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-primary" onclick="" >Confirm</button>
                    </div>
                  </div>
                </div>
              </div>
            <div class="prelogin-content">
              <div class="white-content-box login-IAIS" style="height: 274px;">
                <h3>Login to IAIS</h3>
                <div class="left-content">
                  <ul>
                    <li>
                      <p>Apply for a new licence</p>
                    </li>
                    <li>
                      <p>Check the status of your applications</p>
                    </li>
                    <li>
                      <p>Manage your existing licences</p>
                    </li>
                    <li>
                      <p>Manage your account profile</p>
                    </li>
                    <li>
                      <p>View messages &amp; notifications from MOH</p>
                    </li>
                  </ul>
                </div>


                <div class="right-content login-btns">
                  <%String testMode = ConfigHelper.getString("moh.halp.login.test.mode", "prod");
                    if ("prod".equalsIgnoreCase(testMode)){%>
                  <a class="btn btn-primary" href="<%=SIMConfig.getInstance().getIdpCorpassInitiatedUrl()%>" >LOGIN USING CorpPass</a>
                  <%}else{%>
                  <a class="btn btn-primary" href="javascript:void(0)" onclick="Utils.submit('mainForm','corppassLogin')">LOGIN USING CorpPass</a>
                  <%} %>

                  <p class="text-center"><a href="javascript:void(0)" onclick="Utils.submit('mainForm','registry')">Don't have a CorpPass?</a></p>
                </div>
              </div>
              <div class="white-content-box hcsa" style="height: 274px;">
                <h3>Healthcare Services Act (HCSA)</h3>
                <ul>
                  <li>
                    <p><a href="#">About HCSA</a></p>
                  </li>
                  <li>
                    <p><a href="#">Bill phases &amp; timelines</a></p>
                  </li>
                  <li>
                    <p><a href="#">Services under IAIS today</a></p>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>

<%@include file="/WEB-INF/jsp/include/utils.jsp"%>