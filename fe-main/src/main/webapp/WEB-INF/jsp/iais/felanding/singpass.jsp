<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%
  String openTestMode = ConfigHelper.getString("moh.halp.login.test.mode", "prod");
  request.setAttribute("openTestMode", openTestMode);
%>

<c:choose>
  <c:when test="${'Dummy.NoPass' eq openTestMode || 'Dummy.WithPass' eq openTestMode}">
    <%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
    <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
    <%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
    <%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
    <%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ page import="java.util.Date" %>
    <%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
    <%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
    <%
      sop.webflow.rt.api.BaseProcessClass process =
              (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    %>
    <webui:setLayout name="iais-internet"/>

    <div class="main-content">
      <div class="navigation-gp"></div>
      <form id="mainForm" method="post" action="/main-web/eservice/INTERNET/FE_Singpass_Landing/1/singpassCallBack">
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="prelogin" style="background-image: url('/web/themes/fe/img/prelogin-masthead-banner.jpg');">
          <div class="container">

            <div class="col-xs-5">
              <div class="center-content">
                <br>
                <div class="login-area">
                  <div id="errorMessage" class="error-login" style="display:block"></div>
                  <div class="linebreak"></div>
                </div>
                <div id="login" class="login-area" style="display:block;">
                  <div class="form-group">
                    <label class="sr-only" for="entityId">NRIC/ENTITY ID<</label>
                    <input id="entityId" maxlength="9" name="entityId" onkeypress="doKeyPress(event, 'login')" placeholder="NRIC/ENTITY ID" class="custom-tag-to-uppercase form-control" required="required" type="uenEnittyID" value="" autocomplete="off">
                    <a class="topposition tooltipclick" title="" data-placement="left" data-toggle="tooltip" data-trigger="click" tabindex="-1" href="#" data-original-title="Unique Entity Number (UEN) or system generated Entity ID registered to your CorpPass account."><span class="icon-info-login-main login-info-padding"></span></a>
                  </div>
              <c:if test="${openTestMode eq 'Dummy.WithPass'}">
                  <div style="display:none;">
                    <input type="password" id="pass" name="password"
                           minlength="8" required>
                  </div>


                  <div class="form-group">
                    <label class="sr-only" for="singpassPwdId">Password</label>
                    <input id="singpassPwdId" maxlength="15" type="password" name="loginPassword" onkeypress="doKeyPress(event, 'login')" placeholder="PASSWORD" class="form-control" required="required" type="corppassPwdId" value="" autocomplete="off">
                    <a class="topposition tooltipclick" title="" data-placement="left" data-toggle="tooltip" data-trigger="click" tabindex="-1" href="#" data-original-title="CorpPass ID registered to your account."><span class="icon-info-login-main login-info-padding"></span></a>
                  </div>
              </c:if>
                  <div class="form-group">
                    <button type="button" id="login" class="btn btn-primary btn-block" onclick="submitSingPass()" onkeypress="doKeyPress(event, 'login')">Login  <i class="fa fa-caret-right" aria-hidden="true"></i></button>
                  </div>
                  <div class="checkbox">
                    <label class="login-label">
                      <a class="remTooltip topposition tooltipclick" title="" data-placement="left" data-toggle="tooltip" data-trigger="click" tabindex="-1" href="#" data-original-title="This is for CorpPass to remember your Entity ID so you do not have to enter it the next time.">
                        <span class="icon-remEntityID icon-info-login-main login-info-padding"></span></a>
                    </label>

                  </div>
                  <div class="login__footer">
                    <div class="login-note text-center">
                      <div class="login-text text-left"> <label class="login-label"></label>Forgot <u><a class="corppassID-link" href="https://www.corppass.gov.sg/corppass/retrievecpid/enterinfo">Entity / CorpPass ID </a></u> or <u><a class="password-link" href="https://www.corppass.gov.sg/corppass/onlineresetpassword/enterinfo">Password</a></u></div>
                      <div class="login-text text-left"> <label class="login-label"></label>New to CorpPass? <u><a class="corppassID-link" href="https://www.corppass.gov.sg/cpauth/login/homepage?TAM_OP=login">Register</a></u> Now.</div>
                    </div>
                  </div>
                </div>
              </div>

            </div>

          </div>
        </div>
      </form>
    </div>
  </c:when>
  <c:otherwise>
    <%@ page import="com.ncs.secureconnect.sim.lite.SIMUtil" %>
    <%@ page import="com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper" %>
    <%@ page import="com.ecquaria.cloud.moh.iais.helper.FeLoginHelper" %>
    <%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
    <%!
      static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("singpass.jsp");
    %>
    <%
      try {
        SIMUtil.doSingPassLogin(request, response);
      } catch (Exception e) {
        logger.error(e.getMessage(),e);
        out.println("<br><b>Error initializing Login </b></br>");
        IaisEGPHelper.sendRedirect(request, response, FeLoginHelper.MAIN_WEB_URL);
      }
    %>
  </c:otherwise>
</c:choose>

<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script>
  function submitSingPass() {
    var entityId = $('#entityId').val();
    if (entityId == ''){
      return;
    }else {
      Utils.submit('mainForm')
    }

  }

</script>
