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
      <%@ include file="/include/formHidden.jsp" %>
      <input type="hidden" name="switch_action_type" value="">
      <input type="hidden" name="crud_action_value" value="">
      <input type="hidden" name="crud_action_additional" value="">
      <br>
          <div class="navigation-gp">
              <div class="col-xs-10 col-xs-offset-1 col-lg-offset-9 col-lg-3">
                <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" href="javascript:;">Tan Mei Ling Joyce</a>
                  <ul class="dropdown-menu" aria-labelledby="profileBtn">
                    <li class="dashboard-icon"><a href="#">Dashboard</a></li>
                    <li class="management-account"><a href="#">Manage Account</a></li>
                    <li class="logout"><a href="#">Logout</a></li>
                  </ul>
                </div>
              </div>
              <div class="col-xs-12">
                <div class="dashboard-page-title">
                  <h1>New Licence Application</h1>
                </div>
              </div>
      </div>


      <div class="instruction-content center-content">
        <h2>SERVICES SELECTED</h2>
        <ul class="service-list">
          <c:forEach var="baseItem" items="${baseService}" varStatus="status">
            <li><span><iais:service value="${baseItem}"></iais:service></span> (Base Service)</li>
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
                <p>Licensee: <b>Greenwood Clinic</b> </p>
              </div>
              <div class="license-edit">
                <p><a href="#">View</a></p>
              </div>
            </div>
            <div class="license-info-row">
              <div class="licnese-info">
                <p>Authorised User 1: <b>Mo Delan</b> </p>
              </div>
              <div class="license-edit">
                <p><a href="#">View</a></p>
              </div>
            </div>
            <div class="license-info-row">
              <div class="licnese-info">
                <p>Authorised User 2: <b>Linda Tan MedAlert</b> </p>
              </div>
              <div class="license-edit">
                <p><a href="#">View</a></p>
              </div>
            </div>
            <div class="license-info-row">
              <div class="licnese-info">
                <p>Contact Person: <b>Shun Qiu</b></p>
              </div>
              <div class="license-edit">
                <p><a href="#">View</a></p>
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
              <p><a class="back" href="#" onclick="doBack()"><i class="fa fa-angle-left"></i> Back</a></p>
            </div>
            <div class="col-xs-12 col-sm-6">
              <div class="text-right text-center-mobile"><a class="btn btn-primary next" onclick="doNext()" >Start Application</a></div>
            </div>
          </div>
        </div>
      </div>

    </form>
  </div>
</div>

<script>
  function doNext() {
      $("input[name='switch_action_type']").val("startApplication");
      $("#mainForm").submit();
  }

  function doBack() {
      SOP.Crud.cfxSubmit("mainForm", "doBack");
  }
  
</script>