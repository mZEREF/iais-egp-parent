<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
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

<%--@elvariable id="insFindingList" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsFindingFormDto"--%>
<%--@elvariable id="itemSelection" type="java.lang.String"--%>
<%--@elvariable id="activeTab" type="java.lang.String"--%>
<%--@elvariable id="inspectionProcessDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InspectionProcessDto"--%>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">

        <input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(insFindingList.itemDtoList.size())}">
        <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
        <input type="hidden" id="section_repeat_section_id_prefix" value="findingTr" readonly disabled>
        <input type="hidden" id="section_repeat_section_group_id" value="findingTBody" readonly disabled>
        <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>
        <input type="hidden" id="chkl_item_selection" value="${itemSelection}" readonly disabled>

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
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_CHECKLIST}">class="active"</c:if> id="insDetails" role="presentation">
                                            <a href="#${InspectionConstants.TAB_CHECKLIST}" id="doChecklist" aria-controls="tagChecklist" role="tab" data-toggle="tab">Checklist</a>
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
                                                <a href="#${InspectionConstants.TAB_CHECKLIST}" aria-controls="tabInsDetails" role="tab" data-toggle="tab">Checklist</a>
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
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_CHECKLIST}">active</c:if>" id="${InspectionConstants.TAB_CHECKLIST}" role="tabpanel">
                                            <%@include file="checklist.jsp"%>
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
                                                                <label class="col-xs-12 col-md-4 control-label" for="decision">Processing Decision <span style="color: red">*</span></label>
                                                                <div class="col-sm-7 col-md-5 col-xs-10">
                                                                    <select name="decision" class="pending-ins-decision" id="decision">
                                                                        <option value="">Please Select</option>
                                                                        <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_PROCEED_TO_INSPECTION_REPORT_PREPARATION}" <c:if test="${inspectionProcessDto.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_PROCEED_TO_INSPECTION_REPORT_PREPARATION}">selected="selected"</c:if>>Proceed to Inspection Report Preparation</option>
                                                                    </select>
                                                                    <span data-err-ind="decision" class="error-msg" ></span>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="col-xs-12 col-md-4 control-label" for="remark">Remarks</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-10">
                                                                    <p>
                                                                        <textarea id="remark" name="remark" style="width: 100%;margin-bottom: 15px;" rows="3"  maxlength="1500"><c:out value="${inspectionProcessDto.remark}"/></textarea>
                                                                    </p>
                                                                </div>
                                                                <div class="clear"></div>
                                                            </div>

                                                            <span data-err-ind="error_message" class="error-msg"></span>

                                                        </div>

                                                        <iais:action>
                                                            <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                                                            <div style="text-align: right">
                                                                <button name="submitBtn" id="submitBtn" type="button" class="btn btn-primary">Submit</button>
                                                            </div>
                                                        </iais:action>
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