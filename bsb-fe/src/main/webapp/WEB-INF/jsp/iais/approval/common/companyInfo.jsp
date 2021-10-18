<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approvalapplication.js"></script>
<div class="main-content">
  <div class="container">
    <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
      <input type="hidden" name="switch_action_type" value="">
      <input type="hidden" name="crud_action_value" value="">
      <input type="hidden" name="crud_action_additional" value="">
      <c:set var="loginContext" value="${iais_Login_User_Info_Attr}"/>
      <br>
          <div class="navigation-gp">
            <%@ include file="../common/dashboardDropDown.jsp" %>
              <div class="col-xs-12">
                <div class="dashboard-page-title">
                  <h1>Application for Types of Approval</h1>
                </div>
              </div>
      </div>

      <div class="instruction-content center-content">
	    <h2>SERVICES SELECTED</h2>
		<ul class="service-list">          
            <li><iais:code code="${taskList}"></iais:code></li>
        </ul>
        <h3>Company Info</h3>
        <ul class="service-list">
			<li>UEN Number: T07CX0118D</li>
			<li>Company Name: SGH LABORATORY</li>
			<li>Block: 212</li>
			<li>Street Name: BEDOK NORTH ST 1</li>
			<li>Floor and Unit No.: 03-147</li>
			<li>Postal Code: 460212</li>
        </ul>
      
        <h3>Before You Begin</h3>
        <ul class="">
          <li>
            <p>In the next page, you will select the classification of the facility which you intend to register and the type of activities which will be conducted in the facility. Before proceeding, you are advised to refer to the information that is available on the MOH Biosafety website to understand the different options, to ensure selection of the correct facility classification. Please note that selection of an incorrect facility classification may result in rejection of the application. </p>
          </li>
          <li>
            <p>This form will take approximately 10 mins to complete. You may save your progress at any time and resume your application later</p>
          </li>
        </ul>
        <div class="application-tab-footer">
          <div class="row">
            <div class="col-xs-12 col-sm-6">
              <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-sm-6">
              <%--<input type="text" style="display: none; " id="selectDraftNo" value="${selectDraftNo}">--%>
              <div class="text-right text-center-mobile"><a class="btn btn-primary next" id="nextBtn" data-toggle="modal" data-target= "#saveDraft">START APPLICATION</a></div>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</div>