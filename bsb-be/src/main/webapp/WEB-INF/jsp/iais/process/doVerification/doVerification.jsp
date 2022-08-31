<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-process-do-verification.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%--@elvariable id="mohProcessPageValidation" type="java.lang.String"--%>
        <input type="hidden" name="ifProcess" id="ifProcess" value="${mohProcessPageValidation}">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="active" id="info" role="presentation">
                                                <a href="#tabInfo" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                            </li>
                                            <li id="documents" role="presentation">
                                                <a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="facilityDetails" role="presentation">
                                                <a href="#tabFacilityDetails" id="doFacilityDetails" aria-controls="tabFacilityDetails" role="tab" data-toggle="tab">Application Recommendations</a>
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
                                                    <a href="#tabFacilityDetails" aria-controls="tabFacilityDetails" role="tab" data-toggle="tab">Application Recommendations</a>
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
                                            <div class="tab-pane" id="tabFacilityDetails" role="tabpanel">
                                                <%@include file="appRecommendations.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <strong>
                                                        <h4>Processing Status Update</h4>
                                                    </strong>
                                                </div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <div class="form-horizontal">
                                                                <%--@elvariable id="doVerificationDto" type="sg.gov.moh.iais.egp.bsb.dto.process.DOVerificationDto"--%>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Current Status</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${doVerificationDto.submissionDetailsInfo.applicationStatus}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision / Recommendation <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="processingDecision" class="processingDecisionDropDown" id="processingDecision">
                                                                                <option value="">Please Select</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ACCEPT}" <c:if test="${MasterCodeConstants.MOH_PROCESS_DECISION_ACCEPT eq doVerificationDto.processingDecision}">selected="selected"</c:if>>Accept</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}" <c:if test="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT eq doVerificationDto.processingDecision}">selected="selected"</c:if>>Reject</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}" <c:if test="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION eq doVerificationDto.processingDecision}">selected="selected"</c:if>>Request For Information</option>
                                                                            </select>
                                                                            <span data-err-ind="processingDecision" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                        <%--@elvariable id="rfiProcessDto" type="sg.gov.moh.iais.egp.bsb.dto.rfi.RfiProcessDto"--%>
                                                                    <common:rfi processingDecision="${doVerificationDto.processingDecision}" commentsToApplicant="${doVerificationDto.commentsToApplicant}" rfiProcessDto="${rfiProcessDto}">
                                                                    </common:rfi>
                                                                <div class="form-group">
                                                                    <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="1000"><c:out value="${doVerificationDto.remarks}"/></textarea>
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