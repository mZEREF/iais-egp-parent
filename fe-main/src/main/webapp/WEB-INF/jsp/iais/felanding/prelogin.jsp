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
<style>
  h3 {
    font-size: 2rem;
    font-family: "Open Sans", sans-serif;
    border-bottom: 0 solid #D1D1D1;
    margin-top: 0;
    margin-bottom: 15px;
    padding-bottom: 20px;
    font-weight: 600;
  }

</style>
<div class="main-content">
<c:if test="${not empty bAnner_AlERt_Msg__atTR || not empty schEdule_AlERt_Msg__atTR}">
  <div class="col-md-12" style="margin-top:10px;">
  <c:if test="${not empty schEdule_AlERt_Msg__atTR}">
    <div class="dashalert alert-info dash-announce alertMaintainace">
      <button aria-label="Close" data-dismiss="dashalert" class="close" type="button" onclick="javascript:$('.alertMaintainace').hide();"><span aria-hidden="true">x</span></button>
      <h3 style="margin-top:0;"><i class="fa fa-wrench"></i> Upcoming Scheduled Maintenance</h3> <%--NOSONAR--%>
                            <c:out value="${schEdule_AlERt_Msg__atTR}" escapeXml="false"/></div>
  </c:if>
  <c:if test="${not empty bAnner_AlERt_Msg__atTR}">
    <div class="dashalert alert-info dash-announce alertBanner">
      <button aria-label="Close" data-dismiss="alert" class="close" type="button" onclick="javascript:$('.alertBanner').hide();"><span aria-hidden="true">x</span></button>
      <h3 style="margin-top:0;"><i class="fa fa-bell"></i> Announcement</h3><%--NOSONAR--%>
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
              <p class="component-desc">Manage all licence-related matters associated with your healthcare services.</p>
            </div>
            <!-- Revised Pre-login Start Here -->
            <div class="prelogin-content">
              <div class="row">
                <div class="col-xs-12 col-lg-8  user-selection-login white-content-box" style="margin-right: 30px;">
                  <p> <strong>Step 1:</strong> Healthcare Services Act (HCSA) licences will be managed under the new IT system, Healthcare Applications and Licensing Portal (HALP). All other Private Hospitals and Medical Clinics Act (PHMCA) licences and other services will be managed under the existing eLis. Please select accordingly based on the licences/services you wish to manage. Should you wish to manage both HCSA and PHMCA licences/services, please select both options.
                  </p>
                  <div class="pre-login-selection">
                    <div class="row">
                      <div class="col-xs-12 col-lg-8">
                        <div class="form-check-gp">
                          <div class="form-check">
                            <input class="form-check-input" id="clinicalToxicology" name="serviceGroup" onclick="javascript:landing();" type="checkbox" aria-invalid="false">
                            <label class="form-check-label"   for="clinicalToxicology"><span class="check-square"></span> <strong>Healthcare Application and Licensing Portal (HALP)</strong></label>
                          </div>
                        </div>
                      </div>
                      <div class="col-xs-12 col-lg-4 text-right">
                        <a data-toggle="collapse" data-target="#halp-info" class="btn btn-secondary btn-sm" href="javascript:void(0)" onclick="$('.prelogin-content .white-content-box').css('height', 'auto');"> More Info</a>
                      </div>
                      <div class="col-xs-12 col-lg-12" >
                        <div id="halp-info" class="collapse halp-infobox" style="height: 100px">
                          <div class="col-xs-4 col-md-11 col-sm-4">Clinical Laboratory</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Radiological</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Tissue Banking (Cord Blood)</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Nuclear Medicine Imaging</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Nuclear Medicine Assay</div><br>
                        </div>
                      </div>
                      <!--------------------->

                      <div class="col-xs-12 col-lg-8" style="padding-top:15px">
                        <div class="form-check-gp">
                          <div class="form-check">
                            <input class="form-check-input" id="oldSystem" name="serviceGroup" onclick="javascript:landing();" type="checkbox" aria-invalid="false">
                            <label class="form-check-label"  for="oldSystem"><span class="check-square"></span><strong>E-licensing For Healthcare (eLis)</strong></label>
                          </div>
                        </div>
                      </div>
                      <div class="col-xs-12 col-lg-4 text-right" style="padding-top:15px">
                        <a data-toggle="collapse" data-target="#elis-info" class="btn btn-secondary btn-sm" href="javascript:void(0)" onclick="$('.prelogin-content .white-content-box').css('height', 'auto');"> More Info</a>
                      </div>
                      <div class="col-xs-12 col-lg-12" >
                        <div id="elis-info" class="collapse halp-infobox"  style="height: 100px">
                          <div class="col-xs-4 col-md-11 col-sm-4">Hospital Licences</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Medical Clinic Licences</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Dental Clinic Licences</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Nursing Home Licences</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Data Submission for Termination of Pregnancy (TOP) returns, Voluntary Sterilisation (VS) returns and Drug Practices</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Realtime Database for Assisted Reproduction (RDAR)</div>
                        </div>
                      </div>
                      <!---------------------->
                      <div class="col-xs-12 col-lg-8" style="padding-top:15px">
                        <div class="form-check-gp">
                          <div class="form-check">
                            <input class="form-check-input" id="bsb" value="bsb" name="serviceGroup" onclick="javascript:landing();" type="checkbox" aria-invalid="false">
                            <label class="form-check-label"  for="oldSystem"><span class="check-square"></span><strong>Biosafety Branch (BSB)</strong></label>
                          </div>
                        </div>
                      </div>
                      <div class="col-xs-12 col-lg-4 text-right" style="padding-top:15px">
                        <a data-toggle="collapse" data-target="#bsb-info" class="btn btn-secondary btn-sm" href="javascript:void(0)" onclick="$('.prelogin-content .white-content-box').css('height', 'auto');"> More Info</a>
                      </div>
                      <div class="col-xs-12 col-lg-12" >
                        <div id="bsb-info" class="collapse halp-infobox"  style="height: 100px">
                          <div class="col-xs-4 col-md-11 col-sm-4">Register New Facility</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Approval for  Approval</div><br>
                          <div class="col-xs-4 col-md-11 col-sm-4">Apply to New Facility Certifier</div>
                        </div>
                      </div>
                      <!---------------------->
                    </div>
                  </div>
                  <div class="col-xs-12" style="margin-bottom: 20px;">
                    <p> <strong>Step 2:</strong> Please click on "Login with Singpass" for your respective entity type to proceed. </p>
                  </div>
                  <div class="col-xs-12 col-lg-6 user-selection">
                    <h3 style="color: #3B2A85;" >For Business Users</h3>
                    <p>For corporate users with registered UEN to access and transact on behalf of their licensee.</p>
                    <a class="btn btn-primary disabled" id="corppass" href="javascript:void(0)">Login with CorpPass</a>
                  </div>
                  <div class="col-xs-12 col-lg-6 user-selection">
                    <h3 style="color:#B8271E" > For Individual Users</h3>
                    <p> For individual without registered UEN</p> <br>
                    <a  class="btn btn-primary disabled" id="singpass" href="javascript:void(0)">Login with SingPass</a>
                  </div>

                </div>
                <div class="col-xs-12 col-lg-3 user-selection-login white-content-box">
                  <h3>Healthcare Services Act (HCSA)</h3>
                  <ul>
                    <li>
                      <p><a href="<iais:code code="RELURL001"></iais:code>">About HCSA</a></p>
                    </li>
                    <li>
                      <p><a href="<iais:code code="RELURL003"></iais:code>">FAQ</a></p>
                    </li>
                    <li>
                      <p><a href="<iais:code code="RELURL002"></iais:code>">Services under HALP today</a></p>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <!-- Revised Pre-login End here -->

          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<iais:confirm msg="Please select service licence to login" popupOrder="loginModal" callBack="$('#loginModal').modal('hide');" needCancel="false" needFungDuoJi="false"/>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">
  $(document).ready(landing);

  function landing() {
    var checkOne = false;
    var checkBox = $("input[name='serviceGroup']");
    for (var i = 0; i < checkBox.length; i++) {
      if (checkBox[i].checked) {
        checkOne = true;
      }
      ;
    }
    ;
    if (checkOne) {
      $('#corppass').removeClass("disabled");
      $('#singpass').removeClass("disabled");
    } else {
      $('#corppass').addClass("disabled");
      $('#singpass').addClass("disabled");
    }

    <%String testMode = ConfigHelper.getString("moh.halp.login.test.mode", "prod");
    if (testMode.toUpperCase().equals("PROD")){%>
    var prd = "<%=SIMConfig.getInstance().getIdpCorpassInitiatedUrl()%>";
    var prdSing = "<%=SIMConfig.getInstance().getIdpSingpassInitiatedUrl()%>";
    <%}else{%>
    var prd = "";
    var prdSing = "";
    <%}%>
    var oldSysSingpass = "<%=ConfigHelper.getString("moh.elis.internet.singpass.web", "#")%>";
    var oldSysCorpass = "<%=ConfigHelper.getString("moh.elis.internet.corpass.web", "#")%>";

    var serviceGroups = $('input[name="serviceGroup"]:checked');
    if (serviceGroups.length == 0) {
        $('#corppass').attr("href", "javascript:void(0);");
        $('#corppass').attr("onclick", "javascripts:$('#loginModal').modal('show');");
        $('#singpass').attr("href", "javascript:void(0);");
        $('#singpass').attr("onclick", "javascripts:$('#loginModal').modal('show');");
    } else if ($('#oldSystem').is(':checked')) {
        $('#corppass').attr("href", oldSysCorpass);
        $('#corppass').removeAttr("onclick");
        $('#singpass').attr("href", oldSysSingpass);
        $('#singpass').removeAttr("onclick");
    } else if (isEmpty(prd)) {
        $('#corppass').attr("href", "javascript:void(0);");
        $('#corppass').attr("onclick", "Utils.submit('mainForm','corppassLogin');");
        $('#singpass').attr("href", "javascript:void(0);");
        $('#singpass').attr("onclick", "Utils.submit('mainForm','singpassLogin');");
    } else {
        $('#corppass').attr("href", prd);
        $('#corppass').removeAttr("onclick");
        $('#singpass').attr("href", prdSing);
        $('#singpass').removeAttr("onclick");
    }

    if ($("input[id='bsb']")[0].checked) {
      $('#singpass').addClass("disabled");
      $.cookie('service_bsb', 'Y');
    }
    else {
      $.cookie('service_bsb', '');
    }
  }
</script>