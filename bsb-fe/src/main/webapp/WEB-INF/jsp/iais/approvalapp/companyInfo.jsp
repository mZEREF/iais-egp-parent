<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approval-app.js"></script>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="action_type" value="">
  <input type="hidden" name="action_value" value="">
  <div class="container">
    <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
      <br/>
      <div class="col-xs-12 col-sm-12" style="background-color: rgba(242, 242, 242, 1); padding: 20px 30px 10px 30px; border-radius: 15px;">
        <p class="assessment-title" style="border-bottom: 1px solid black;">Licensable Healthcare Services</p>
        <div class="col-xs-12 col-md-5">
          <ul>
            <li>UEN Number:</li>
            <li>Company Name:</li>
            <li>Block:</li>
            <li>Street Name:</li>
            <li>Floor and Unit No.:</li>
            <li>Postal Code:</li>
          </ul>
        </div>
        <div class="col-xs-12 col-md-6">
          <ul>
            <li>T07CX0118D</li>
            <li>SGH LABORATORY</li>
            <li>212</li>
            <li>BEDOK NORTH ST 1</li>
            <li>03-147</li>
            <li>460212</li>
          </ul>
        </div>
      </div>
      <br/>
      <div class="col-xs-12 col-sm-12">
        <p class="assessment-title" style="border-bottom: 1px solid black;">Before You Begin</p>
        <div>
          <ul>
            <li>In the next page, you will select the classification of the facility which you intend to register and
              the type of activities which will be conducted in the facility. Before proceeding, you are advised
              to refer to the information that is available on the <a href="#">MOH Biosafety website</a> to understand the
              different options, to ensure selection of the correct facility classification. Please note that
              selection of an incorrect facility classification may result in rejection of the application.
            </li>
            <li>This form will take approximately 10 mins to complete. You may save your progress at any
              time and resume your application later
            </li>
          </ul>
        </div>
      </div>
      <br/>
      <div class="row">
        <div class="col-xs-12 col-md-3">
          <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Back</a>
        </div>
        <div class="col-xs-12 col-md-9">
          <div class="text-right text-center-mobile">
            <a class="btn btn-primary next" id="next" href="javascript:void(0);">START APPLICATION</a>
          </div>
        </div>
      </div>

    </div>
  </div>
</form>