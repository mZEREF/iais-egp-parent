<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>

<%--@elvariable id="activeTab" type="java.lang.String"--%>
<%--@elvariable id="reviewFollowUpDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto"--%>
<%--@elvariable id="insDecision" type="sg.gov.moh.iais.egp.bsb.dto.inspection.followup.InsFollowUpExtensionProcessDto"--%>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <div class="subcontent col-12">
                                <div class="col-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_SUBMIT_INTO}">class="active"</c:if> id="info" role="presentation">
                                                <a href="#${InspectionConstants.TAB_SUBMIT_INTO}" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                            </li>
                                            <li <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">class="active"</c:if> id="documents" role="presentation">
                                                <a href="#${InspectionConstants.TAB_DOC}" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li <c:if test="${activeTab eq InspectionConstants.TAB_INS_FOLLOW_UP}">class="active"</c:if> id="followUpLi" role="presentation">
                                                <a href="#${InspectionConstants.TAB_INS_FOLLOW_UP}" id="doFollowUp" aria-controls="tabFollowUp" role="tab" data-toggle="tab">Follow-Up</a>
                                            </li>
                                            <li <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAIL}">class="active"</c:if> id="facilityDetail" role="presentation">
                                                <a href="#${InspectionConstants.TAB_FAC_DETAIL}" id="doFacilityDetail" aria-controls="tabFacilityDetail" role="tab" data-toggle="tab">Application Recommendations</a>
                                            </li>
                                            <li <c:if test="${activeTab eq InspectionConstants.TAB_PROCESSING}">class="active"</c:if> id="process" role="presentation">
                                                <a href="#${InspectionConstants.TAB_PROCESSING}" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#${InspectionConstants.TAB_SUBMIT_INTO}" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#${InspectionConstants.TAB_DOC}" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#${InspectionConstants.TAB_INS_FOLLOW_UP}" aria-controls="tabFollowUp" role="tab" data-toggle="tab">Follow-Up</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#${InspectionConstants.TAB_FAC_DETAIL}" aria-controls="tabFacilityDetail" role="tab" data-toggle="tab">Application Recommendations</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#${InspectionConstants.TAB_PROCESSING}" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_SUBMIT_INTO}">active</c:if>" id="${InspectionConstants.TAB_SUBMIT_INTO}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/submissionDetailsInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">active</c:if>" id="${InspectionConstants.TAB_DOC}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_INS_FOLLOW_UP}">active</c:if>" id="${InspectionConstants.TAB_INS_FOLLOW_UP}" role="tabpanel">
                                                <%@include file="followUpPage.jsp"%>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAIL}">active</c:if>" id="${InspectionConstants.TAB_FAC_DETAIL}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/insFacilityDetailsInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_PROCESSING}">active</c:if>" id="${InspectionConstants.TAB_PROCESSING}" role="tabpanel">
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <h4>Processing Status Update</h4>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <div class="form-horizontal">
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Current Status</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${reviewFollowUpDto.currentStatus}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <c:if test="${requestExtension ne 'Y'}">
                                                                    <div class="form-group" id="submitDecisionDiv">
                                                                        <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision/Recommendation <span style="color: red">*</span></label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10">
                                                                            <div class="input-group">
                                                                                <select name="processingDecision" class="processingDecisionDropdown" id="processingDecision" data-app-status="${reviewFollowUpDto.currentStatus}">
                                                                                    <option value="">Please Select</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}">selected="selected"</c:if>>Approve</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">selected="selected"</c:if>>Reject</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_APPLICANT}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_APPLICANT}">selected="selected"</c:if>>Route back to Applicant</option>
                                                                                </select>
                                                                                <span data-err-ind="decision" class="error-msg" ></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                </c:if>

                                                                <c:if test="${requestExtension eq 'Y'}">
                                                                    <div class="form-group" id="extensionDecisionDiv">
                                                                        <label for="processingExtensionDecision" class="col-xs-12 col-md-4 control-label">Processing Decision <span style="color: red">*</span></label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10">
                                                                            <div class="input-group">
                                                                                <select name="processingExtensionDecision" class="processingExtensionDecisionDropdown" id="processingExtensionDecision" >
                                                                                    <option value="">Please Select</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}">selected="selected"</c:if>>Approve</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">selected="selected"</c:if>>Reject</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}">selected="selected"</c:if>>Request for information</option>
                                                                                </select>
                                                                                <span data-err-ind="decision" class="error-msg" ></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                </c:if>

                                                                <div class="form-group">
                                                                    <label for="remarksToFacility" class="col-xs-12 col-md-4 control-label">Remarks (To Facility)</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <textarea id="remarksToFacility" name="remarksToFacility" cols="70" rows="7" maxlength="1500"><c:out value="${insDecision.remarkToFacility}"/></textarea>
                                                                            <span data-err-ind="remarksToFacility" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <c:if test="${requestExtension ne 'Y'}">
                                                                    <div class="form-group">
                                                                        <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks (Internal)</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10">
                                                                            <div class="input-group">
                                                                                <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="1500"><c:out value="${insDecision.remark}"/></textarea>
                                                                                <span data-err-ind="remarks" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                </c:if>

                                                                <c:if test="${requestExtension ne 'Y'}">
                                                                    <div class="form-group" id="selectMohUserDiv"  <c:if test="${insDecision.decision ne MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE and insDecision.decision ne MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">style="display: none;"</c:if>>
                                                                        <label for="selectMohUser" class="col-xs-12 col-md-4 control-label">Select Approving Officer <span style="color: red">*</span></label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10">
                                                                            <div class="input-group">
                                                                                <select name="selectMohUser" class="selectMohUserDropdown" id="selectMohUser">
                                                                                    <option value="">Please Select</option>
                                                                                    <c:forEach var="selection" items="${selectRouteToMoh}">
                                                                                        <option value="${selection.value}" <c:if test="${insDecision.selectMohUser eq selection.value}">selected="selected"</c:if>>${selection.text}</option>
                                                                                    </c:forEach>
                                                                                </select>
                                                                                <span data-err-ind="selectMohUser" class="error-msg" ></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                </c:if>

                                                                <c:if test="${requestExtension eq 'Y'}">
                                                                    <div class="form-group" id="newDueDateDiv" <c:if test="${insDecision.decision ne MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}">style="display: none;"</c:if>>
                                                                        <label for="newDueDate" class="col-xs-12 col-md-4 control-label">Assign New Due Date <span style="color: red">*</span></label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10">
                                                                            <iais:datePicker id="newDueDate" name="newDueDate" value="${insDecision.newDueDate}"/>
                                                                            <span data-err-ind="newDueDate" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                </c:if>
                                                            </div>
                                                            <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Previous</a>
                                                            <div style="text-align: right">
                                                                <button name="submitBtn" id="submitBtn" type="button" class="btn btn-primary">Submit</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <br>
                                                <%@include file="/WEB-INF/jsp/iais/common/processHistory.jsp" %>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/iais/doDocument/internalFileUploadModal.jsp"%>
</div>