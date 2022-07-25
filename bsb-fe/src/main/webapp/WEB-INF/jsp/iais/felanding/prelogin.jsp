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
    <div class="dashalert alert-info dash-announce alertMaintainace">
      <button aria-label="Close" data-dismiss="dashalert" class="close" type="button" onclick="javascript:$('.alertMaintainace').hide();"><span aria-hidden="true">x</span></button>
      <h3 style="margin-top:0;"><em class="fa fa-wrench"></em> Upcoming Scheduled Maintainace</h3> <%--NOSONAR--%>
                            <c:out value="${schEdule_AlERt_Msg__atTR}" escapeXml="false"/></div>
  </c:if>
  <c:if test="${not empty bAnner_AlERt_Msg__atTR}">
    <div class="dashalert alert-info dash-announce alertBanner">
      <button aria-label="Close" data-dismiss="alert" class="close" type="button" onclick="javascript:$('.alertBanner').hide();"><span aria-hidden="true">x</span></button>
      <h3 style="margin-top:0;"><em class="fa fa-bell"></em> Announcement</h3><%--NOSONAR--%>
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
              <h1>Healthcare Application and <br class="hidden-xs"> Licensing Portal (HALP)</h1>
              <p class="component-desc">Manage all Facility-related matters associated with your healthcare services.</p>
            </div>

            <div class="prelogin-content text-center">
              <div class="white-content-box login-IAIS" style="height: 274px;">
                <div class="left-content text-left">
                  <div class="col-xs-12 col-lg-8" style="padding-top:15px">
                    <div class="form-check-gp">
                      <div class="form-check">
                        <input class="form-check-input" id="bsb" value="bsb" name="serviceGroup" onclick="landing();" type="checkbox" aria-invalid="false">
                        <label class="form-check-label" for="bsb"><span class="check-square"></span><strong>Biosafety Branch (BSB) (Login as a facility user)</strong></label>
                      </div>
                    </div>
                  </div>
                  <div class="col-xs-12 col-lg-4 text-right" style="padding-top:15px">
                    <a data-toggle="collapse" data-target="#bsb-info" class="btn btn-secondary btn-sm" href="javascript:void(0)" onclick="$('.prelogin-content .white-content-box').css('height', 'auto');"> More Info</a>
                  </div>
                  <div class="col-xs-12 col-lg-12" >
                    <div id="bsb-info" class="collapse halp-infobox"  style="height: 100px">
                      <div class="col-xs-4 col-md-11 col-sm-4">Apply New Facility</div><br>
                      <div class="col-xs-4 col-md-11 col-sm-4">Apply New Approval to Possess</div><br>
                      <div class="col-xs-4 col-md-11 col-sm-4">Apply New Approval to Large-scale Production</div>
                      <div class="col-xs-4 col-md-11 col-sm-4">Apply New Special Approval to Handle</div>
                    </div>
                  </div>

                  <div class="col-xs-12 col-lg-8" style="padding-top:15px">
                    <div class="form-check-gp">
                      <div class="form-check">
                        <input class="form-check-input" id="bsb-afc" value="bsb-afc" name="serviceGroup" onclick="landing();" type="checkbox" aria-invalid="false">
                        <label class="form-check-label" for="bsb-afc"><span class="check-square"></span><strong>Biosafety Branch (BSB) (Login as a MOH-AFC user)</strong></label>
                      </div>
                    </div>
                  </div>
                  <div class="col-xs-12 col-lg-4 text-right" style="padding-top:15px">
                    <a data-toggle="collapse" data-target="#bsb-afc-info" class="btn btn-secondary btn-sm" href="javascript:void(0)" onclick="$('.prelogin-content .white-content-box').css('height', 'auto');"> More Info</a>
                  </div>
                  <div class="col-xs-12 col-lg-12" >
                    <div id="bsb-afc-info" class="collapse halp-infobox"  style="height: 100px">
                      <div class="col-xs-4 col-md-11 col-sm-4">Apply New Approved Facility Certifier</div><br>
                      <div class="col-xs-4 col-md-11 col-sm-4">Submit Certification Report</div><br>
                    </div>
                  </div>
                </div>

                <div class="right-content login-btns">
                  <a class="btn btn-primary" href="javascript:void(0)" id="corppass">LOGIN USING CorpPass</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<iais:confirm msg="Please select one service to login" popupOrder="loginModal" callBack="$('#loginModal').modal('hide');" needCancel="false" needFungDuoJi="false"/>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">
  $(document).ready(landing);

  function landing() {
    var serviceGroups = $('input[name="serviceGroup"]:checked');
    if (serviceGroups.length === 0 || serviceGroups.length > 1) {
      $('#corppass').attr("href", "javascript:void(0);");
      $('#corppass').attr("onclick", "javascripts:$('#loginModal').modal('show');");
    } else {
        $('#corppass').attr("href", "javascript:void(0);");
        $('#corppass').attr("onclick", "Utils.submit('mainForm','corppassLogin');");
    }
  }
</script>