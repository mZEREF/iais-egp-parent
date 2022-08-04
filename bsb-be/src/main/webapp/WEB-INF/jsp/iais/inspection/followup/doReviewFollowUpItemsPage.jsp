<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>

<%--@elvariable id="reviewFollowUpDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto"--%>
<%--@elvariable id="insDecision" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto"--%>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%@include file="/WEB-INF/jsp/iais/inspection/followup/judgePageDisplay.jsp"%>
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
                                            <li class="active" id="info" role="presentation">
                                                <a href="#tabInfo" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                            </li>
                                            <li id="documents" role="presentation">
                                                <a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="facilityDetail" role="presentation">
                                                <a href="#tabFacilityDetail" id="doFacilityDetail" aria-controls="tabFacilityDetail" role="tab" data-toggle="tab">Application Recommendations</a>
                                            </li>
                                            <li id="followUpLi" role="presentation">
                                                <a href="#tabFollowUp" id="doFollowUp" aria-controls="tabFollowUp" role="tab" data-toggle="tab">Follow-Up</a>
                                            </li>
                                            <li id="process" role="presentation">
                                                <a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabFacilityDetail" aria-controls="tabFacilityDetail" role="tab" data-toggle="tab">Application Recommendations</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabFollowUp" aria-controls="tabFollowUp" role="tab" data-toggle="tab">Follow-Up</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/submissionDetailsInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabFacilityDetail" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/facilityDetailsInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabFollowUp" role="tabpanel">
                                                <%@include file="followUpPage.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
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
                                                                <div class="form-group">
                                                                    <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="300"><c:out value="${insDecision.remark}"/></textarea>
                                                                            <span data-err-ind="remarks" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>


                                                                <div class="form-group" id="submitDecisionDiv" style="display: none">
                                                                    <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision/Recommendation <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="processingDecision" class="processingDecisionDropdown" id="processingDecision">
                                                                                <option value="">Please Select</option>
                                                                                <%--TODO: check these decision--%>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}">selected="selected"</c:if>>Approve</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">selected="selected"</c:if>>Reject</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_APPLICANT}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_APPLICANT}">selected="selected"</c:if>>Route back to applicant</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION}">selected="selected"</c:if>>Skip Inspection</option>
                                                                            </select>
                                                                            <span data-err-ind="decision" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <div class="form-group" id="extensionDecisionDiv" style="display: none">
                                                                    <label for="processingExtensionDecision" class="col-xs-12 col-md-4 control-label">Processing Decision/Recommendation <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="processingExtensionDecision" class="processingExtensionDecisionDropdown" id="processingExtensionDecision">
                                                                                <option value="">Please Select</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}">selected="selected"</c:if>>Approve</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">selected="selected"</c:if>>Reject</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION}">selected="selected"</c:if>>Skip Inspection</option>
                                                                            </select>
                                                                            <span data-err-ind="decision" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

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