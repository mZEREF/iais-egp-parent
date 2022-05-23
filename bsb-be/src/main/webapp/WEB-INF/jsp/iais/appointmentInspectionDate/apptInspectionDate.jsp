<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>

<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-appointment.js"></script>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <%--@elvariable id="apptReviewData" type="sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto"--%>
  <%--@elvariable id="apptInspectionDateDto" type="sg.gov.moh.iais.egp.bsb.dto.appointment.BsbApptInspectionDateDto"--%>
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="apptInspectionDateType" value="">
    <input type="hidden" id="processDec" name="processDec" value="">
    <input type="hidden" id="sysInspDateFlag" name="sysInspDateFlag" value="${apptInspectionDateDto.sysInspDateFlag}">
    <input type="hidden" id="sysSpecDateFlag" name="sysSpecDateFlag" value="${apptInspectionDateDto.sysSpecDateFlag}">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body >
                <div class="">
                  <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                      <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <li id="apptInspTabInfo" class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                                       data-toggle="tab">Info</a></li>
                        <li id="apptInspTabDocuments" class="complete" role="presentation"><a href="#tabDocuments"
                                                                                              aria-controls="tabDocuments" role="tab"
                                                                                              data-toggle="tab">Documents</a></li>
                        <li id="apptInspTabProcessing" class="incomplete" role="presentation"><a href="#tabProcessing" id = "apptInspectionDateProcess"
                                                                                                 aria-controls="tabProcessing" role="tab"
                                                                                                 data-toggle="tab" onclick="javascript:apptInspectionDateGetDate()">Processing</a></li>
                      </ul>
                      <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                          <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                       data-toggle="tab">Info</a></div>
                          <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments"
                                                       role="tab" data-toggle="tab">Documents</a></div>
                          <div class="swiper-slide"><a id="apptInspectionDate" href="#tabProcessing" aria-controls="tabProcessing"
                                                       role="tab" data-toggle="tab">Processing</a></div>
                        </div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                      </div>
                      <div class="tab-content">
                        <div class="tab-pane active" id="tabInfo" role="tabpanel">
                          <%@ include file="applicationInfo.jsp" %>
                        </div>
                        <div class="tab-pane" id="tabDocuments" role="tabpanel">
                          <%@ include file="../doDocument/tabDocuments.jsp" %>
                        </div>

                        <div class="tab-pane" id="tabProcessing" role="tabpanel">
                          <div class="alert alert-info" role="alert">
                            <strong>
                              <h4>Appointment Scheduling (Inspection)</h4>
                            </strong>
                          </div>
                          <iais:section title="" id = "inspection_date">
                            <div class="table-gp">
                              <table aria-describedby="" class="apptApp table">
                                <thead>
                                <tr>
                                  <th scope="col" >Application No</th>
                                  <th scope="col" >Application Status</th>
                                  <th scope="col" >MOH Officer(s)</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                  <c:when test="${apptInspectionDateDto.applicationInfoShow eq null}">
                                    <tr>
                                      <td colspan="7">
                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                      </td>
                                    </tr>
                                  </c:when>
                                  <c:otherwise>
                                    <c:forEach var="appInfoShow" items="${apptInspectionDateDto.applicationInfoShow}">
                                      <tr>
                                        <td><c:out value="${apptReviewData.submissionDetailsDto.applicationNo}"/></td>
                                        <td><iais:code code="${apptReviewData.submissionDetailsDto.currentStatus}"/></td>
                                        <td>
                                          <c:if test="${appInfoShow.userDisName != null}">
                                            <c:forEach var="worker" items="${appInfoShow.userDisName}" varStatus="status">
                                              <c:out value="${worker}"/><br>
                                            </c:forEach>
                                          </c:if>
                                        </td>
                                      </tr>
                                    </c:forEach>
                                  </c:otherwise>
                                </c:choose>
                                </tbody>
                              </table>
                            </div>
                            <c:if test="${'SUCCESS' eq apptInspectionDateDto.actionButtonFlag}">
                              <div id="apptThreeInspDate">
                                <div class="row" id = "apptDateTitle">
                                  <div class="col-md-4">
                                    <label style="font-size: 16px">Available Appointment Date</label>
                                  </div>
                                </div>
                              </div>
                              <c:if test="${not empty apptInspectionDateDto.inspectionDate}">
                                <div class="row">
                                  <div class="col-md-6">
                                    <ul>
                                      <c:forEach var="insepctionDate" items="${apptInspectionDateDto.inspectionDate}">
                                        <li class="apptInspScheduleUl"><span style="font-size: 16px"><c:out value="${insepctionDate}"/></span></li>
                                      </c:forEach>
                                    </ul>
                                  </div>
                                </div>
                              </c:if>
                              <iais:action>
                                <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
                                <button id="apptSpecInspDate" class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateSpecific()">Assign Specific Date</button>
                                <button id="disApptSpecInspDate" class="btn btn-primary disabled" style="float:right" type="button">Assign Specific Date</button>
                                <span style="float:right">&nbsp;</span>
                                <button id="disApptSysInspDate" class="btn btn-primary disabled" disabled style="float:right" type="button">Confirm System-proposed Date</button>
                                <button id="apptSysInspDate" class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateConfirm()">Confirm System-proposed Date</button>
                              </iais:action>
                            </c:if>
                            <c:if test="${'SUCCESS' ne apptInspectionDateDto.actionButtonFlag}">
                              <iais:action>
                                <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
                              </iais:action>
                            </c:if>
                            <button id="skipInsFlow" class="btn btn-md" style="float:right" type="button" onclick="javascript:skipInspection()">Skip Inspection</button>
                            <br><br><br>
                          </iais:section>
                          <%@include file="/WEB-INF/jsp/iais/common/processHistory.jsp"%>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<%@include file="/WEB-INF/jsp/iais/doDocument/internalFileUploadModal.jsp"%>