<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String continueURL = "";
  if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
    continueURL = process.runtime.continueURL();
  }
%>
<webui:setLayout name="iais-internet"/>

<c:set var="title" value="Amendment" />
<c:set var="smallTitle" value="You are Amending for Assisted Reproduction" />

<%@ include file="section/arHeader.jsp" %>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/ar_selection.js"></script>
<form method="post" id="mainForm" action=<%=continueURL%>>
  <div class="main-content">
    <div class="container center-content">
      <div class="col-xs-12">
        <div class="row form-group" style="border-bottom: 1px solid #D1D1D1;">
          <div class="col-xs-12 col-md-10">
            <strong style="font-size: 2rem;">Please provide details of the patient below</strong>
          </div>
          <div class="col-xs-12 col-md-2 text-right">
            <p class="print" style="font-size: 16px;">
              <label class="fa fa-print" style="color: #147aab;" onclick="printData()"></label> <a onclick="printData()" href="javascript:void(0);">Print</a>
            </p>
          </div>
          <br>
        </div>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <strong>Patient</strong>
              </h4>
            </div>
            <div id="arDataSubmission" class="panel-collapse collapse in">
              <div class="panel-body">
                <div class="panel-main-content form-horizontal">

                  <c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
                  <c:set var="patient" value="${patientInfoDto.patient}" />
                  <c:set var="previous" value="${patientInfoDto.previous}" />
                  <c:set var="husband" value="${patientInfoDto.husband}" />


                  <c:set var="suffix" value="WIFE" />
                  <c:set var="person" value="${patient}" />
                  <p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Details of Patient</p>
                  <%@include file="section/personSection.jsp" %>
                  <div <c:if test="${!patient.previousIdentification}">style="display: none"</c:if>>
                    <p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Patient's identification details used for previous AR/IUI treatment:</p>
                    <%@include file="section/previousPatient.jsp" %>
                  </div>
                  <%@include file="section/husbandPatientDetail.jsp" %>
                </div>
              </div>
            </div>
          </div>
        </div>
        <%@include file="../common/arFooter.jsp" %>
      </div>
    </div>
  </div>
</form>
