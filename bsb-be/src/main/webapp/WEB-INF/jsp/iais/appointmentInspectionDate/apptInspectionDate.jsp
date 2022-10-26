<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-appointment-confirmation.js"></script>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <%--@elvariable id="apptReviewData" type="sg.gov.moh.iais.egp.bsb.dto.inspection.AppointmentReviewDataDto"--%>
    <%--@elvariable id="activeTab" type="java.lang.String"--%>
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
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
                        <li id="apptInspTabInfo" role="presentation" <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_SUBMIT_INTO}">class="active"</c:if>>
                          <a href="#${InspectionConstants.TAB_SUBMIT_INTO}" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                        </li>
                        <li id="apptInspTabDocuments" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">class="active"</c:if>>
                          <a href="#${InspectionConstants.TAB_DOC}" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                        </li>
                        <li id="facilityDetails" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAIL}">class="active"</c:if>>
                          <a href="#${InspectionConstants.TAB_FAC_DETAIL}" aria-controls="tabFacilityDetails" role="tab" data-toggle="tab">Application Recommendations</a>
                        </li>
                        <li id="apptInspTabProcessing" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_PROCESSING}">class="active"</c:if>>
                          <a href="#${InspectionConstants.TAB_PROCESSING}" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                        </li>
                      </ul>
                      <div class="tab-content">
                        <div class="tab-pane <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_SUBMIT_INTO}">active</c:if>" id="${InspectionConstants.TAB_SUBMIT_INTO}" role="tabpanel">
                          <%@include file="/WEB-INF/jsp/iais/common/submissionDetailsInfo.jsp" %>
                        </div>
                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">active</c:if>" id="${InspectionConstants.TAB_DOC}" role="tabpanel">
                          <%@ include file="../doDocument/tabDocuments.jsp" %>
                        </div>
                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAIL}">active</c:if>" id="${InspectionConstants.TAB_FAC_DETAIL}" role="tabpanel">
                          <%@include file="/WEB-INF/jsp/iais/common/insFacilityDetailsInfo.jsp"%>
                        </div>
                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_PROCESSING}">active</c:if>" id="${InspectionConstants.TAB_PROCESSING}" role="tabpanel">
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
                                  <th scope="col" >Preferred Inspection Date (by Facility)</th>
                                  <th scope="col" >Assigned MOH Officer</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                  <td><c:out value="${apptReviewData.applicationNo}"/></td>
                                  <td><iais:code code="${apptReviewData.applicationStatus}"/></td>
                                  <td><c:out value="${apptReviewData.preferredInspectionDate}"/></td>
                                  <td>
                                    <c:if test="${apptReviewData.assignedMohOfficerList != null && apptReviewData.assignedMohOfficerList.size() > 0}">
                                      <c:forEach var="mohOfficer" items="${apptReviewData.assignedMohOfficerList}">
                                        <c:out value="${mohOfficer}"/><br>
                                      </c:forEach>
                                    </c:if>
                                  </td>
                                </tr>
                                </tbody>
                              </table>
                            </div>
                            <div class="form-horizontal">
                              <div class="form-group" <c:if test="${apptReviewData.applicationStatus ne MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_CONFIRMATION}">style="display: none" </c:if>>
                                <label class="col-xs-12 col-md-12 control-label">Proposed Date</label>
                                <li><c:out value="${apptReviewData.proposedDate}"/></li>
                                <div class="clear"></div>
                              </div>
                              <div class="form-group" <c:if test="${apptReviewData.applicationStatus ne MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_SCHEDULING && apptReviewData.applicationStatus ne MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_CONFIRMATION}">style="display: none" </c:if>>
                                <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision/Recommendation <span style="color: red">*</span></label>
                                <div class="col-sm-7 col-md-5 col-xs-10">
                                  <div class="input-group">
                                    <select name="processingDecision" class="processingDecisionDropDown" id="processingDecision">
                                      <option value="">Please Select</option>
                                      <c:if test="${apptReviewData.applicationStatus eq MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_CONFIRMATION}">
                                        <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_CONFIRM_PROPOSED_DATE}" <c:if test="${MasterCodeConstants.MOH_PROCESS_DECISION_CONFIRM_PROPOSED_DATE eq apptReviewData.processingDecision}">selected="selected"</c:if>>Confirm Proposed Date</option>
                                        <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_SELECT_ANOTHER_DATE}" <c:if test="${MasterCodeConstants.MOH_PROCESS_DECISION_SELECT_ANOTHER_DATE eq apptReviewData.processingDecision}">selected="selected"</c:if>>Select Another Date</option>
                                      </c:if>
                                      <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}" <c:if test="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION eq apptReviewData.processingDecision}">selected="selected"</c:if>>Request for Information</option>
                                    </select>
                                    <span data-err-ind="processingDecision" class="error-msg" ></span>
                                  </div>
                                </div>
                                <div class="clear"></div>
                              </div>
                              <div id="selectAnotherDate" <c:if test="${MasterCodeConstants.MOH_PROCESS_DECISION_SELECT_ANOTHER_DATE ne apptReviewData.processingDecision}">style="display: none" </c:if>>
                                <div class="form-group">
                                  <label class="col-xs-12 col-md-4 control-label">Date of Inspection (From & To) <span style="color: red">*</span></label>
                                  <div class="col-sm-7 col-md-5 col-xs-10">
                                    <div class="col-md-6" style="padding-left: 0;">
                                      <input type="text" autocomplete="off" name="specifyStartDt" id="specifyStartDt" data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${apptReviewData.specifyStartDt}"/>
                                      <span data-err-ind="specifyStartDt" class="error-msg" ></span>
                                    </div>
                                    <div class="col-md-6" style="padding-right: 0">
                                      <iais:select name="specifyStartHour" cssClass="specifyStartHourDropdown" options="startHoursOption" firstOption="--:--" value="${apptReviewData.specifyStartHour}"/>
                                      <span data-err-ind="specifyStartHour" class="error-msg" ></span>
                                    </div>
                                  </div>
                                </div>
                                <span data-err-ind="startDate" class="error-msg"></span>
                                <div class="form-group">
                                  <label class="col-xs-12 col-md-4 control-label"></label>
                                  <div class="col-sm-7 col-md-5 col-xs-10">To</div>
                                </div>
                                <div class="form-group">
                                  <label class="col-xs-12 col-md-4 control-label"></label>
                                  <div class="col-sm-7 col-md-5 col-xs-10">
                                    <div class="col-md-6" style="padding-left: 0">
                                      <input type="text" autocomplete="off" name="specifyEndDt" id="specifyEndDt" data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${apptReviewData.specifyEndDt}"/>
                                      <span data-err-ind="specifyEndDt" class="error-msg" ></span>
                                    </div>
                                    <div class="col-md-6" style="padding-right: 0">
                                      <iais:select name="specifyEndHour" cssClass="specifyEndHourDropdown" options="endHoursOption" firstOption="--:--" value="${apptReviewData.specifyEndHour}"/>
                                      <span data-err-ind="specifyEndHour" class="error-msg" ></span>
                                    </div>
                                  </div>
                                </div>
                                  <span data-err-ind="endDate" class="error-msg"></span>
                              </div>
                              <common:rfi-inspection processingDecision="${apptReviewData.processingDecision}" remarksToApplicant="${apptReviewData.remarksToApplicant}" pageAppEditSelectDto="${apptReviewData.pageAppEditSelectDto}" >
                              </common:rfi-inspection>
                            </div>
                            <span data-err-ind="apptRefNo" class="error-msg" ></span>
                            <iais:action>
                              <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                              <div style="text-align: right">
                                <button name="submitBtn" id="submitRfiBtn" type="button" class="btn btn-primary" <c:if test="${apptReviewData.applicationStatus ne MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_SCHEDULING && apptReviewData.applicationStatus ne MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_CONFIRMATION}">style="display: none" </c:if>>Submit</button>
                              </div>
                            </iais:action>
                          </iais:section>
                          <br/>
                          <br/>
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
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>