<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 1/20/2020
  Time: 10:24 AM
  To change this template use File | Settings | File Templates.
--%>


<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="main-content">
  <div class="container">
    <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="switch_action_type" value="">
      <input type="hidden" name="crud_action_value" value="">
      <input type="hidden" name="crud_action_additional" value="">
      <c:set var="loginContext" value="${iais_Login_User_Info_Attr}"/>
      <br>
          <div class="navigation-gp">
            <%@ include file="../common/dashboardDropDown.jsp" %>
              <div class="col-xs-12">
                <div class="dashboard-page-title">
                  <h1>New Licence Application</h1>
                </div>
              </div>
      </div>


      <div class="instruction-content center-content">
        <h2>SERVICES SELECTED</h2>
        <ul class="service-list">
          <c:forEach var="baseItem" items="${baseSvcIdList}" varStatus="status">
            <li><span><iais:service value="${baseItem}"></iais:service></span> (Base Service)</li>
          </c:forEach>
          <c:forEach var="specifiedItem" items="${speSvcIdList}" varStatus="status">
            <li><span><iais:service value="${specifiedItem}"></iais:service></span> (Specified Service)</li>
          </c:forEach>
        </ul>
        <div class="gray-content-box">
          <div class="h3-with-desc">
            <h3>Licensee and Key Personnel</h3>
            <p>The following details are common to all services in your healthcare organisation. To make any changes, please contact your company administrator.</p>
          </div>
          <div class="license-info-gp">
            <div class="license-info-row">
              <div class="licnese-info">
                <p>Licensee: <strong>Greenwood Clinic</strong> </p>
              </div>
              <div class="license-edit">
                <p><a class="license-view">View</a></p>
              </div>
            </div>
            <div class="license-info-row">
              <div class="licnese-info">
                <p>Authorised User 1: <strong>Mo Delan</strong> </p>
              </div>
              <div class="license-edit">
                <p><a class="authorise-view">View</a></p>
              </div>
            </div>
            <div class="license-info-row">
              <div class="licnese-info">
                <p>Authorised User 2: <strong>Linda Tan</strong> </p>
              </div>
              <div class="license-edit">
                <p><a class="authorise-view">View</a></p>
              </div>
            </div>

          </div>
        </div>
        <h3>Before You Begin</h3>
        <ul class="short-content">
          <li>
            <p>This form will take approximately 30 minutes to complete. You may save your progress at any time and resume your application later. </p>
          </li>
          <li>
            <p>Payment may be made using a credit card, debit card or via GIRO.</p>
          </li>
        </ul>
        <div class="application-tab-footer">
          <div class="row">
            <div class="col-xs-12 col-sm-6">
              <a class="back" href="#" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-sm-6">
              <input type="text" style="display: none; " id="selectDraftNo" value="${selectDraftNo}">
              <div class="text-right text-center-mobile"><a class="btn btn-primary next" onclick="doNext()" data-toggle="modal" data-target= "#saveDraft"  >Start Application</a></div>
            </div>
          </div>
        </div>
      </div>
    <%--  <c:if test="${ not empty selectDraftNo }">
          <iais:confirm msg="There is an existing draft for the chosen service, if you choose to continue, the draft application will be discarded." callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
      </c:if>--%>
    </form>
  </div>
</div>

<script>
  function doNext() {
      $("input[name='switch_action_type']").val("startApplication");
      $("#mainForm").submit();
  }

  function doBack() {
      $("input[name='switch_action_type']").val("doBack");
      $("#mainForm").submit();
  }

  function saveDraft() {

  }

  function cancelSaveDraft() {

  }

  $(".license-view").click(function () {
    $("input[name='switch_action_type']").val("showlicense");
    $("input[name='crud_action_additional']").val("Licensee");
    $("#mainForm").submit();
  })

  $(".authorise-view").click(function () {
    $("input[name='switch_action_type']").val("showlicense");
    $("input[name='crud_action_additional']").val("Authorised");
    $("#mainForm").submit();
  })

  $(".medAlert-view").click(function () {
    $("input[name='switch_action_type']").val("showlicense");
    $("input[name='crud_action_additional']").val("MedAlert");
    $("#mainForm").submit();
  })
</script>