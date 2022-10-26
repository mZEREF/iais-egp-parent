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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-task.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%--@elvariable id="role" type="java.lang.String"--%>
<div class="dashboard">
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Assignment of task from Common Pool to DO's Tasklist</span>
                </h2>
              </div>
              <%--@elvariable id="taskDetailDto" type="sg.gov.moh.iais.egp.bsb.dto.task.TaskDetailDto"--%>
              <iais:body >
                <iais:section title="" id = "assign_Task">
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Application No.</label>
                    </div>
                    <div class="col-md-6">
                        <%--@elvariable id="appId" type="java.lang.String"--%>
                        <%--@elvariable id="appViewModuleType" type="java.lang.String"--%>
                      <c:set var="maskedAppId"><iais:mask name="appId" value="${appId}"/></c:set>
                      <c:set var="maskedAppViewModuleType"><iais:mask name="appViewModuleType" value="${appViewModuleType}"/></c:set>
                      <c:choose>
                        <%--@elvariable id="appViewUrl" type="java.lang.String"--%>
                        <%--@elvariable id="taskType" type="java.lang.String"--%>
                        <c:when test="${appViewUrl ne null}">
                        <a href="javascript:void(0);" onclick="viewApplication()">
                          <c:out value="${taskDetailDto.applicationNo}"/>
                        </a>
                        </c:when>
                        <c:otherwise>
                          <a href="javascript:void(0);" onclick="viewApplication()">
                            <c:out value="${taskDetailDto.applicationNo}"/>
                          </a>
                        </c:otherwise>
                      </c:choose>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Application Status</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${taskDetailDto.applicationStatus}"/></span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility Classification</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${taskDetailDto.facilityClassification}"/></span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility Activity Type</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${taskDetailDto.facilityActivityType}"/></span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility Name</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${taskDetailDto.facilityName}"/></span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Address</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${taskDetailDto.address}"/></span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <c:set value="Duty Officer" var="officer"/>
                      <c:if test="${role eq 'BSB_DO'}">
                        <c:set value="Duty Officer" var="officer"/>
                      </c:if>
                      <c:if test="${role eq 'BSB_AO'}">
                        <c:set value="Approval Officer" var="officer"/>
                      </c:if>
                      <c:if test="${role eq 'BSB_HM'}">
                        <c:set value="Higer Management" var="officer"/>
                      </c:if>
                      <c:if test="${role eq 'BSB_INSPECTOR'}">
                        <c:set value="Inspector" var="officer"/>
                      </c:if>
                      <label style="font-size: 16px">${officer}</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${taskDetailDto.officer}"/></span><p></p>
                    </div>
                  </div>
                  <br><br>
                  <div style="text-align:right;">
                    <a href="/bsb-web/eservice/INTRANET/MohBsbTaskCommonPool" class="back" style="float:left"><em class="fa fa-angle-left"></em>Previous</a>
                    <button id="submitBtn" class="btn btn-primary" style="float:right" type="button">SUBMIT</button>
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

