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
                  <span>Task Details</span>
                </h2>
              </div>
              <%--@elvariable id="multiAssignInsDto" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.multiassign.MultiAssignInsDto>"--%>
              <iais:body >
                <iais:section title="" id = "assign_Task">
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Application Number</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${multiAssignInsDto.applicationNo}"/></span>
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
                      <span style="font-size: 16px"><c:out value="${multiAssignInsDto.facName}"/>/<c:out value="${multiAssignInsDto.facAddress}"/></span>
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
                      <c:if test="${null ne multiAssignInsDto.inspectorsDisplayName}">
                        <c:forEach items="${multiAssignInsDto.inspectorsDisplayName}" var="user">
                          <span style="font-size: 16px"><c:out value="${user}"/></span><p></p>
                        </c:forEach>
                      </c:if>
                      <span data-err-ind="inspectors" class="error-msg"></span>
                    </div>
                  </div>
                  <br><br>
                  <iais:action >
                    <a href="#" id="back" class="back" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                    <button id="submitMultiAssignTask" class="btn btn-primary" style="float:right" type="button">SUBMIT</button>
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

