<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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

<div class="dashboard">
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Task Details</span>
                </h2>
              </div>
              <%--@elvariable id="multiAssignInsDto" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.multiassign.MultiAssignInsDto>"--%>
              <%--@elvariable id="appId" type="java.lang.String"--%>
              <%--@elvariable id="appViewModuleType" type="java.lang.String"--%>
              <iais:body >
                <iais:section title="">
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Application Number</label>
                    </div>
                    <div class="col-md-6">
                      <a href="javascript:void(0);" onclick="viewApplication('<iais:mask name="appId" value="${appId}"/>', '<iais:mask name="appViewModuleType" value="${appViewModuleType}"/>')">
                        <c:out value="${multiAssignInsDto.applicationNo}"/>
                      </a>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Application Status</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${multiAssignInsDto.applicationStatus}"/></span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Facility Classification</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${multiAssignInsDto.facClassification}"/></span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Activity Type</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${multiAssignInsDto.activityType}"/></span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Name/Address</label>
                    </div>
                    <div class="col-md-6">
                      <c:if test="${multiAssignInsDto.canMultiAssign}">
                        <span style="font-size: 16px"><c:out value="${multiAssignInsDto.facName}"/>/<c:out value="${multiAssignInsDto.facAddress}"/></span>
                      </c:if>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Inspector Leader</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${multiAssignInsDto.insLeader}"/></span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Inspector<span style="color: red"> *</span></label>
                    </div>
                    <div class="col-md-6">
                        <%--@elvariable id="userOption" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                      <c:if test="${null ne userOption  && multiAssignInsDto.canMultiAssign}">
                        <c:forEach items="${userOption}" var="options">
                          <input type="checkbox" name="inspectorCheck" id="inspectorCheck" <c:if test="${multiAssignInsDto.inspectors.contains(options.value)}">checked="checked"</c:if> value="<c:out value="${options.value}"/>"/>
                          <span style="font-size: 16px"><c:out value="${options.text}"/></span><p></p>
                        </c:forEach>
                      </c:if>
                          <span data-err-ind="inspectors" class="error-msg"></span>
                    </div>
                  </div>
                  <br><br>
                  <iais:action >
                    <a href="/bsb-web/eservicecontinue/INTRANET/MohBsbSupervisorAssignmentPool" class="back" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                    <c:if test="${multiAssignInsDto.canMultiAssign}">
                      <button id="submitBtn" class="btn btn-primary" style="float:right" type="button">Next</button>
                    </c:if>
                  </iais:action>
                </iais:section>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>

