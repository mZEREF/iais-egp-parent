<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>

<%--@elvariable id="submissionDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo"--%>
<%--@elvariable id="itemSelection" type="java.lang.String"--%>
<%--@elvariable id="activeTab" type="java.lang.String"--%>
<%--@elvariable id="ncEmailDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCEmailDto"--%>

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
                                <div class="tab-gp dashboard-tab">
                                    <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                        <li <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_SUBMIT_INTO}">class="active"</c:if> id="info" role="presentation">
                                            <a href="#${InspectionConstants.TAB_SUBMIT_INTO}" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">class="active"</c:if> id="documents" role="presentation">
                                            <a href="#${InspectionConstants.TAB_DOC}" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_RECTIFICATION}">class="active"</c:if> id="rectification" role="presentation">
                                            <a href="#${InspectionConstants.TAB_APPROVAL_LETTER}" id="doRectification" aria-controls="tabEmail" role="tab" data-toggle="tab">Non-Compliance Notification</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAIL}">class="active"</c:if> id="facDetails" role="presentation">
                                            <a href="#${InspectionConstants.TAB_FAC_DETAIL}" id="doFacDetails" aria-controls="tabFacDetails" role="tab" data-toggle="tab">Application Recommendations</a>
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
                                                <a href="#${InspectionConstants.TAB_APPROVAL_LETTER}" aria-controls="tabEmail" role="tab" data-toggle="tab">Non-Compliance Notification</a>
                                            </div>
                                            <div class="swiper-slide">
                                                <a href="#${InspectionConstants.TAB_FAC_DETAIL}" aria-controls="tabDocuments" role="tab" data-toggle="tab">Application Recommendations</a>
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
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_APPROVAL_LETTER}">active</c:if>" id="${InspectionConstants.TAB_APPROVAL_LETTER}" role="tabpanel">
                                            <%@include file="ncEmailPage.jsp"%>
                                        </div>
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAIL}">active</c:if>" id="${InspectionConstants.TAB_FAC_DETAIL}" role="tabpanel">
                                            <%@include file="/WEB-INF/jsp/iais/common/insFacilityDetailsInfo.jsp"%>
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
                                                                    <p><iais:code code="${submissionDetailsInfo.applicationStatus}"/></p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-12 col-md-4 control-label" for="processingDecision">Processing Decision <span style="color: red">*</span></label>
                                                                <div class="col-sm-7 col-md-5 col-xs-10">
                                                                    <select name="processingDecision"  class="processingDecisionDropdown" id="processingDecision" data-app-status="${submissionDetailsInfo.applicationStatus}">
                                                                        <option value="">Please Select</option>
                                                                        <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_AO_FOR_REVIEW}" <c:if test="${ncEmailDto.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_AO_FOR_REVIEW}">selected="selected"</c:if>>Route to AO for Review</option>
                                                                        <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_APPLICANT}" <c:if test="${ncEmailDto.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_APPLICANT}">selected="selected"</c:if>>Route to Applicant</option>
                                                                    </select>
                                                                    <span data-err-ind="decision" class="error-msg" ></span>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group" id="selectMohUserDiv" <c:if test="${ncEmailDto.decision ne MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_AO_FOR_REVIEW}">style="display: none;"</c:if>>
                                                                <label for="selectMohUser" class="col-xs-12 col-md-4 control-label">Select Approving Officer<span style="color: red">*</span></label>
                                                                <div class="col-sm-7 col-md-5 col-xs-10">
                                                                    <div class="input-group">
                                                                        <select name="selectMohUser" class="selectMohUserDropdown" id="selectMohUser">
                                                                            <option value="">Please Select</option>
                                                                            <c:forEach var="selection" items="${selectRouteToMoh}">
                                                                                <option value="${selection.value}" <c:if test="${ncEmailDto.selectMohUser eq selection.value}">selected="selected"</c:if>>${selection.text}</option>
                                                                            </c:forEach>
                                                                        </select>
                                                                        <span data-err-ind="selectMohUser" class="error-msg" ></span>
                                                                    </div>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label class="col-xs-12 col-md-4 control-label" for="remarks">Remarks </label>
                                                                <div class="col-sm-7 col-md-5 col-xs-10">
                                                                    <textarea autocomplete="off" class="col-xs-12" name="remarks" id="remarks" maxlength="1500" rows="4"><c:out value="${ncEmailDto.remark}"/></textarea>
                                                                    <span data-err-ind="remarks" class="error-msg"></span>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>
                                                        </div>
                                                        <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                                                        <div style="text-align: right">
                                                            <button name="submitBtn" id="submitBtn" type="button" class="btn btn-primary">Submit</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
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
    </form>
    <%@include file="/WEB-INF/jsp/iais/doDocument/internalFileUploadModal.jsp"%>
</div>