<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-pre-inspection.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%--@elvariable id="insDecision" type="sg.gov.moh.iais.egp.bsb.dto.rfi.RfiInspectionSaveDto"--%>
<%--@elvariable id="submissionDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo"--%>
<%--@elvariable id="activeTab" type="java.lang.String"--%>
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
                                            <li id="info" role="presentation" <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_SUBMIT_INTO}">class="active"</c:if>>
                                                <a href="#${InspectionConstants.TAB_SUBMIT_INTO}" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                            </li>
                                            <li id="documents" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">class="active"</c:if>>
                                                <a href="#${InspectionConstants.TAB_DOC}" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="checklist" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_CHECKLIST}">class="active"</c:if>>
                                                <a href="#${InspectionConstants.TAB_CHECKLIST}" id="doChecklist" aria-controls="tabChecklist" role="tab" data-toggle="tab">Checklist</a>
                                            </li>
                                            <li id="facility" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAIL}">class="active"</c:if>>
                                                <a href="#${InspectionConstants.TAB_FAC_DETAIL}" id="doFacility" aria-controls="tabFacility" role="tab" data-toggle="tab">Application Recommendations</a>
                                            </li>
                                            <li id="process" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_PROCESSING}">class="active"</c:if>>
                                                <a href="#${InspectionConstants.TAB_PROCESSING}" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-content">
                                            <div class="tab-pane <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_SUBMIT_INTO}">active</c:if>" id="${InspectionConstants.TAB_SUBMIT_INTO}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/submissionDetailsInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">active</c:if>" id="${InspectionConstants.TAB_DOC}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_CHECKLIST}">active</c:if>" id="${InspectionConstants.TAB_CHECKLIST}" role="tabpanel">
                                            <%@include file="../../common/checklistInfo.jsp" %>
                                                <iais:action>
                                                    <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                                                    <div style="float:right">
                                                        <button type="button" class="btn btn-primary">DOWNLOAD</button>
                                                        <button id="adhocBth" type="button" style="float:right" class="btn btn-primary" >EDIT</button>
                                                    </div>
                                                </iais:action>
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
                                                                        <p><iais:code code="${submissionDetailsInfo.applicationStatus}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="processingDecision" class="processingDecisionDropdown" id="processingDecision">
                                                                                <option value="">Please Select</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_MARK_INSPECTION_TASK_AS_READY}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_MARK_INSPECTION_TASK_AS_READY}">selected="selected"</c:if>>
                                                                                    Mark Inspection Task as Ready
                                                                                </option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}">selected="selected"</c:if>>
                                                                                    Request for Information
                                                                                </option>
                                                                            </select>
                                                                            <span data-err-ind="decision" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <common:rfi-inspection processingDecision="${insDecision.decision}" remarksToApplicant="${insDecision.remarksToApplicant}" pageAppEditSelectDto="${insDecision.pageAppEditSelectDto}" >
                                                                </common:rfi-inspection>
                                                                <div class="form-group">
                                                                    <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="1500"><c:out value="${insDecision.remark}"/></textarea>
                                                                            <span data-err-ind="remarks" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                            </div>
                                                            <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                                                            <div style="text-align: right"><button name="submitBtn" id="submitRfiBtn" type="button" class="btn btn-primary">Submit</button></div>
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
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/iais/doDocument/internalFileUploadModal.jsp" %>
</div>