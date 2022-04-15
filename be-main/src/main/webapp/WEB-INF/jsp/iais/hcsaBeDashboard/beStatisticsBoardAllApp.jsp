<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/6/28
  Time: 13:55
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/4/2
  Time: 15:21
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.service.client.MsgTemplateMainClient" %>
<%@ page import="java.util.List" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot = IaisEGPConstant.BE_CSS_ROOT;

  String alertFlag = (String) ParamUtil.getSessionAttr(request, "AlERt__Msg_FLAg_attr");
  if (alertFlag == null) {
    MsgTemplateMainClient emc = SpringContextHelper.getContext().getBean(MsgTemplateMainClient.class);
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

<div class="main-content" style="min-height: 73vh;">
  <c:if test="${not empty bAnner_AlERt_Msg__atTR || not empty schEdule_AlERt_Msg__atTR}">
    <div class="col-md-12">
      <c:if test="${not empty schEdule_AlERt_Msg__atTR}">
        <div class="dashalert alert-info dash-announce alertMaintainace">
          <button aria-label="Close" data-dismiss="alert" class="close" type="button" onclick="javascript:closeMaintainace();"><span aria-hidden="true">x</span></button>
          <h3 style="margin-top:0;"><i class="fa fa-wrench" aria-hidden="true"></i> Upcoming Scheduled Maintenance</h3> <%--NOSONAR--%>
          <c:out value="${schEdule_AlERt_Msg__atTR}" escapeXml="false"/></div>
      </c:if>
      <c:if test="${not empty bAnner_AlERt_Msg__atTR}">
        <div class="dashalert alert-info dash-announce alertBanner">
          <button aria-label="Close" data-dismiss="alert" class="close" type="button" onclick="javascript:closeBanner();"><span aria-hidden="true">x</span></button>
          <h3 style="margin-top:0;"><i class="fa fa-bell" aria-hidden="true"></i> Announcement</h3><%--NOSONAR--%>
          <c:out value="${bAnner_AlERt_Msg__atTR}" escapeXml="false"/>
        </div>
      </c:if>
    </div>
  </c:if>
  <form method="post" id="beDashboardForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="hcsaBeDashboardSwitchType" value="">
    <input type="hidden" id="action" name="action" value="">
    <input type="hidden" id="switchAction" name="switchAction" value="${dashSwitchActionValue}">
    <input type="hidden" id="chkIdList" name="chkIdList" value="">
    <input type="hidden" id="taskId" name="taskId" value="">
    <div class="col-xs-12">
      <div class="center-content">
        <div class="intranet-content">
          <iais:body>
            <iais:section title="" id = "Statistics board">
              <%@ include file="/WEB-INF/jsp/iais/hcsaBeDashboard/beDashboardSystem.jsp" %>
            </iais:section>
          </iais:body>
        </div>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript">

    function intraDashboardSubmit(action) {
        showWaiting();
        $("[name='hcsaBeDashboardSwitchType']").val(action);
        var mainPoolForm = document.getElementById('beDashboardForm');
        mainPoolForm.submit();
    }

    function closeBanner() {
        $('.alertBanner').hide();
        $.ajax({
            data:{},
            type:"POST",
            dataType: 'json',
            url: '/main-web/backend/closeBanner.do',
            error:function(data){},
            success:function(data){}
        });
    }

    function closeMaintainace() {
        $('.alertMaintainace').hide();
        $.ajax({
            data:{},
            type:"POST",
            dataType: 'json',
            url: '/main-web/backend/closeMaintenance.do',
            error:function(data){},
            success:function(data){}
        });
    }
</script>