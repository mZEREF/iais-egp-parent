<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-adhoc-inspection.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%--@elvariable id="facility" type="sg.gov.moh.iais.egp.bsb.dto.adhocInspection.AdhocInspectionDisplayDto"--%>
<div class="dashboard">
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="facId" value="<iais:mask name="facId" value="${facility.id}"/>">

    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Assignment of Task</span>
                </h2>
              </div>
              <iais:body >
                <iais:section title="" id = "assign_Task">
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility No.</label>
                    </div>
                    <div class="col-md-6">
                      <label style="font-size: 16px">
                        <a onclick="viewFacility('<iais:mask name="facId" value="${facility.id}"/>')"><c:out value="${facility.facilityNo}"/></a>
                      </label>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility Name</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${facility.name}"/></span>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility Classification</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${facility.classification}"/></span>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility Activity Type</label>
                    </div>
                    <div class="col-md-6">
                      <c:forEach var="activityType" items="${facility.facilityActivityTypes}">
                        <span style="font-size: 16px">
                            <iais:code code="${activityType}"/>
                        </span>
                        <br/>
                      </c:forEach>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility Status</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${facility.status}"/></span>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility Address</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${facility.facilityAddress}"/></span>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Inspection Date</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${facility.inspectionDate}"/></span>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Validity End Date</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${facility.validityEndDate}"/></span>
                    </div>
                  </div>

                  <br><br>
                  <div style="text-align:right;">
                    <a class="back" style="float:left" id="backBtn"><em class="fa fa-angle-left"></em>Previous</a>
                    <button id="submitBtn" class="btn btn-primary" style="float:right" type="button">Confirm</button>
                  </div>
                </iais:section>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>

