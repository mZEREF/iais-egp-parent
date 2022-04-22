<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants" %>
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


<%--@elvariable id="submissionDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo"--%>
<%--@elvariable id="activeTab" type="java.lang.String"--%>
<%--@elvariable id="processDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto"--%>


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
                                        <li <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_FAC_INFO}">class="active"</c:if> id="info" role="presentation">
                                            <a href="#${InspectionConstants.TAB_FAC_INFO}" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Facility Info</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">class="active"</c:if> id="documents" role="presentation">
                                            <a href="#${InspectionConstants.TAB_DOC}" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_INS_REPORT}">class="active"</c:if> id="insReport" role="presentation">
                                            <a href="#${InspectionConstants.TAB_INS_REPORT}" id="doInsReport" aria-controls="tabInsReport" role="tab" data-toggle="tab">Inspection Report</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAILS}">class="active"</c:if> id="facDetails" role="presentation">
                                            <a href="#${InspectionConstants.TAB_FAC_DETAILS}" id="doFacDetails" aria-controls="tabFacDetails" role="tab" data-toggle="tab">Facility Details</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_PROCESSING}">class="active"</c:if> id="process" role="presentation">
                                            <a href="#${InspectionConstants.TAB_PROCESSING}" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                        </li>
                                    </ul>
                                    <div class="tab-nav-mobile visible-xs visible-sm">
                                        <div class="swiper-wrapper" role="tablist">
                                            <div class="swiper-slide">
                                                <a href="#${InspectionConstants.TAB_FAC_INFO}" aria-controls="tabInfo" role="tab" data-toggle="tab">Facility Info</a>
                                            </div>
                                            <div class="swiper-slide">
                                                <a href="#${InspectionConstants.TAB_DOC}" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </div>
                                            <div class="swiper-slide">
                                                <a href="#${InspectionConstants.TAB_INS_REPORT}" aria-controls="tabInsReport" role="tab" data-toggle="tab">Inspection Report</a>
                                            </div>
                                            <div class="swiper-slide">
                                                <a href="#${InspectionConstants.TAB_FAC_DETAILS}" aria-controls="tabFacDetails" role="tab" data-toggle="tab">Facility Details</a>
                                            </div>
                                            <div class="swiper-slide">
                                                <a href="#${InspectionConstants.TAB_PROCESSING}" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="tab-content">
                                        <div class="tab-pane <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_FAC_INFO}">active</c:if>" id="${InspectionConstants.TAB_FAC_INFO}" role="tabpanel">
                                            <%@include file="/WEB-INF/jsp/iais/common/submissionDetailsInfo.jsp" %>
                                        </div>
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">active</c:if>" id="${InspectionConstants.TAB_DOC}" role="tabpanel">
                                            <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp"%>
                                        </div>
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_INS_REPORT}">active</c:if>" id="${InspectionConstants.TAB_INS_REPORT}" role="tabpanel">
                                            <%@include file="../report/inspectionReport.jsp"%>
                                        </div>
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAILS}">active</c:if>" id="${InspectionConstants.TAB_FAC_DETAILS}" role="tabpanel">
                                            <%@include file="/WEB-INF/jsp/iais/common/facilityDetailsInfo.jsp"%>
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
                                                                <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-10">
                                                                    <div class="input-group">
                                                                        <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="300"><c:out value="${processDto.remark}"/></textarea>
                                                                        <span data-err-ind="remark" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <span data-err-ind="error_message" class="error-msg"></span>
                                                            <div class="form-group">
                                                                <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision <span style="color: red">*</span></label>
                                                                <div class="col-sm-7 col-md-5 col-xs-10">
                                                                    <div class="input-group">
                                                                        <select name="processingDecision" class="pro-drop" id="processingDecision">
                                                                            <option value="">Please Select</option>
                                                                            <option value="${MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE}" <c:if test="${processDto.decision eq MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE}">selected="selected"</c:if>>Approve Inspection Report</option>
                                                                            <option value="${MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO}" <c:if test="${processDto.decision eq MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO}">selected="selected"</c:if>>Route back Draft Report</option>
                                                                            <option value="${MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION}" <c:if test="${processDto.decision eq MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION}">selected="selected"</c:if>>Skip Inspection</option>
                                                                        </select>
                                                                        <span data-err-ind="decision" class="error-msg" ></span>
                                                                    </div>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>
                                                        </div>
                                                        <div style="text-align: right">
                                                            <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
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