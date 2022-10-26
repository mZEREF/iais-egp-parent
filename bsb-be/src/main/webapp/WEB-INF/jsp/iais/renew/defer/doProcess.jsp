<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-process-do-renewdefer.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-details-info.js"></script>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <%--@elvariable id="mohProcessDto" type="sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto"--%>
        <%--@elvariable id="facilityDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo"--%>
        <%--@elvariable id="reportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto"--%>
        <%--@elvariable id="reviewAFCReportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto"--%>
        <%--@elvariable id="submissionDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo"--%>
        <%--@elvariable id="activeTab" type="java.lang.String"--%>
        <input type="hidden" name="appType" id="appType" value="${mohProcessDto.submissionDetailsInfo.applicationType}">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <c:set var="appSubType" value="${submissionDetailsInfo.applicationSubType}" />
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li <c:if test="${empty activeTab or activeTab eq ModuleCommonConstants.TAB_SUBMIT_INTO}">class="active"</c:if> id="info" role="presentation">
                                                <a href="#${ModuleCommonConstants.TAB_SUBMIT_INTO}" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                            </li>
                                            <li <c:if test="${activeTab eq ModuleCommonConstants.TAB_DOC}">class="active"</c:if> id="documents" role="presentation">
                                                <a href="#${ModuleCommonConstants.TAB_DOC}" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li <c:if test="${activeTab eq ModuleCommonConstants.TAB_FAC_DETAILS}">class="active"</c:if> id="facilityDetails" role="presentation">
                                                <a href="#${ModuleCommonConstants.TAB_FAC_DETAILS}" id="doFacilityDetails" aria-controls="tabFacilityDetails" role="tab" data-toggle="tab">Application Recommendations</a>
                                            </li>
                                            <li <c:if test="${activeTab eq ModuleCommonConstants.TAB_INS_CER_REPORT}">class="active"</c:if>>
                                                <a href="#${ModuleCommonConstants.TAB_INS_CER_REPORT}" id="doInsCertReport" aria-controls="tabInsCertReport" role="tab" data-toggle="tab">Inspection/Certification Report</a>
                                            </li>
                                            <li <c:if test="${activeTab eq ModuleCommonConstants.TAB_PROCESSING}">class="active"</c:if> id="process" role="presentation">
                                                <a href="#${ModuleCommonConstants.TAB_PROCESSING}" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#${ModuleCommonConstants.TAB_SUBMIT_INTO}" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#${ModuleCommonConstants.TAB_DOC}" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#${ModuleCommonConstants.TAB_FAC_DETAILS}" aria-controls="tabFacilityDetails" role="tab" data-toggle="tab">Application Recommendations</a>
                                                </div>
                                                    <div class="swiper-slide">
                                                        <a href="#${ModuleCommonConstants.TAB_INS_CER_REPORT}" aria-controls="tabInsCertReport" role="tab" data-toggle="tab">Inspection/Certification Report</a>
                                                    </div>
                                                <div class="swiper-slide">
                                                    <a href="#${ModuleCommonConstants.TAB_PROCESSING}" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane <c:if test="${empty activeTab or activeTab eq ModuleCommonConstants.TAB_SUBMIT_INTO}">active</c:if>" id="${ModuleCommonConstants.TAB_SUBMIT_INTO}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/submissionDetailsInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq ModuleCommonConstants.TAB_DOC}">active</c:if>" id="${ModuleCommonConstants.TAB_DOC}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq ModuleCommonConstants.TAB_FAC_DETAILS}">active</c:if>" id="${ModuleCommonConstants.TAB_FAC_DETAILS}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/facilityDetailsInfo.jsp"%>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq ModuleCommonConstants.TAB_INS_CER_REPORT}">active</c:if>" id="${ModuleCommonConstants.TAB_INS_CER_REPORT}" role="tabpanel">
                                                <div class="col-xs-12">
                                                    <div class="tab-gp dashboard-tab">
                                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                                            <li id="InspectionReport" role="presentation" class="active">
                                                                <a href="#tabInspectionReport" id="doInspectionReport" aria-controls="tabInspectionReport" role="tab" data-toggle="tab">Inspection Report</a>
                                                            </li>
                                                            <li id="CertificationReport" role="presentation">
                                                                <a href="#tabCertificationReport" id="doCertificationReport" aria-controls="tabCertificationReport" role="tab" data-toggle="tab">Certification Documents</a>
                                                            </li>
                                                        </ul>
                                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                                            <div class="swiper-wrapper" role="tablist">
                                                                <div class="swiper-slide active">
                                                                    <a href="#tabInspectionReport" aria-controls="tabInspectionReport" role="tab" data-toggle="tab">Inspection Report</a>
                                                                </div>
                                                                <div class="swiper-slide">
                                                                    <a href="#tabCertificationReport" aria-controls="tabCertificationReport" role="tab" data-toggle="tab">Certification Documents</a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="tab-content">
                                                            <div class="tab-pane active" id="tabInspectionReport" role="tabpanel">
                                                                <%@include file="/WEB-INF/jsp/iais/common/inspectionReport.jsp"%>
                                                            </div>
                                                            <div class="tab-pane" id="tabCertificationReport" role="tabpanel">
                                                                <%@include file="/WEB-INF/jsp/iais/common/certificationDocumentsPage.jsp"%>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq ModuleCommonConstants.TAB_PROCESSING}">active</c:if>" id="${ModuleCommonConstants.TAB_PROCESSING}" role="tabpanel">
                                                <div class="alert alert-info" role="alert">
                                                    <strong>
                                                        <h4>Processing Status Update</h4>
                                                    </strong>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <div class="form-horizontal">
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Current Status</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${mohProcessDto.submissionDetailsInfo.applicationStatus}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Facility Validity End Date</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${mohProcessDto.facilityDetailsInfo.validityEndDate}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Expected Date of Renewal</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${mohProcessDto.deferDate}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Reason for Deferment Request</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${mohProcessDto.deferReason}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision / Recommendation <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="processingDecision" class="processingDecisionDropDown" id="processingDecision">
                                                                                <option value="">Please Select</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}">selected="selected"</c:if>>Approve</option>
<%--                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}">selected="selected"</c:if>>Request for Information</option>--%>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">selected="selected"</c:if>>Reject</option>
                                                                            </select>
                                                                            <span data-err-ind="processingDecision" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <common:rfi-new processingDecision="${mohProcessDto.processingDecision}" commentsToApplicant="${mohProcessDto.commentsToApplicant}">
                                                                </common:rfi-new>
                                                                <div class="form-group" id="reasonForRejectionDiv" <c:if test="${mohProcessDto.processingDecision ne MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">style="display: none;"</c:if>>
                                                                    <label for="reasonForRejection" class="col-xs-12 col-md-4 control-label">Reason for rejection <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <textarea id="reasonForRejection" name="reasonForRejection" cols="70" rows="7" maxlength="1000"><c:out value="${mohProcessDto.reasonForRejection}"/></textarea>
                                                                            <span data-err-ind="reasonForRejection" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group" id="selectMohUserDiv" <c:if test="${mohProcessDto.processingDecision ne MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE and mohProcessDto.processingDecision ne MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">style="display: none;"</c:if>>
                                                                    <label for="selectMohUser" class="col-xs-12 col-md-4 control-label">Select Approving Officer <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="selectMohUser" class="selectMohUserDropdown" id="selectMohUser">
                                                                                <option value="">Please Select</option>
                                                                                <c:forEach var="selection" items="${mohProcessDto.selectRouteToMoh}">
                                                                                    <option value="${selection.value}" <c:if test="${mohProcessDto.selectMohUser eq selection.value}">selected="selected"</c:if>>${selection.text}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                            <span data-err-ind="selectMohUser" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="1000"><c:out value="${mohProcessDto.remarks}"/></textarea>
                                                                            <span data-err-ind="remark" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                            </div>
                                                            <%@include file="../common/footer.jsp" %>
                                                        </div>
                                                    </div>
                                                </div>
                                                <%@include file="/WEB-INF/jsp/iais/common/processHistory.jsp" %>
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