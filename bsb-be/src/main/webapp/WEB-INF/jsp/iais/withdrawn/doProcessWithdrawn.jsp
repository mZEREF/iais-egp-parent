<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-withdrawn.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>

<%--@elvariable id="activeTab" type="java.lang.String"--%>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">

        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li <c:if test="${empty activeTab or activeTab eq ModuleCommonConstants.TAB_SUBMIT_INTO}">class="active"</c:if> id="info" role="presentation">
                                                <a href="#${ModuleCommonConstants.TAB_SUBMIT_INTO}" id="infoa" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                            </li>
                                            <li <c:if test="${activeTab eq ModuleCommonConstants.TAB_DOC}">class="active"</c:if> class="complete" id="document" role="presentation">
                                                <a href="#${ModuleCommonConstants.TAB_DOC}" id="documenta" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li <c:if test="${activeTab eq ModuleCommonConstants.TAB_PROCESSING}">class="active"</c:if> class="incomplete" id="process" role="presentation">
                                                <a href="#${ModuleCommonConstants.TAB_PROCESSING}" id="processa" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#${ModuleCommonConstants.TAB_SUBMIT_INTO}" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#${ModuleCommonConstants.TAB_DOC}" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#${ModuleCommonConstants.TAB_PROCESSING}" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane <c:if test="${empty activeTab or activeTab eq ModuleCommonConstants.TAB_SUBMIT_INTO}">active</c:if>" id="${ModuleCommonConstants.TAB_SUBMIT_INTO}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/submissionDetailsInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq ModuleCommonConstants.TAB_DOC}">active</c:if>" id="${ModuleCommonConstants.TAB_DOC}" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane <c:if test="${activeTab eq ModuleCommonConstants.TAB_PROCESSING}">active</c:if>" id="${ModuleCommonConstants.TAB_PROCESSING}" role="tabpanel">
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <h4>Process Status Update</h4>
                                                </div>
                                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">
                                                                <%--@elvariable id="withdrawnDto" type="sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto"--%>
                                                                <div class="form-horizontal">
                                                                    <div class="form-group">
                                                                        <label class="col-xs-12 col-md-4 control-label">Current Status</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10">
                                                                            <p><iais:code code="${withdrawnDto.currentStatus}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label for="doDecision" class="col-xs-12 col-md-4 control-label">Processing Decision / Recommendation <span style="color: red">*</span></label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10">
                                                                            <div class="input-group">
                                                                                <select name="doDecision" class="doDecisionDropdown" id="doDecision">
                                                                                    <option value="">Please Select</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ACCEPT}" <c:if test="${withdrawnDto.doDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_ACCEPT}">selected = 'selected'</c:if>>Accept</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}" <c:if test="${withdrawnDto.doDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}">selected = 'selected'</c:if>>Request for Information</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}" <c:if test="${withdrawnDto.doDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">selected = 'selected'</c:if>>Reject</option>
                                                                                </select>
                                                                                <span data-err-ind="doDecision" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label for="doRemarks" class="col-xs-12 col-md-4 control-label">Remarks</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10">
                                                                            <div class="input-group">
                                                                                <textarea id="doRemarks" name="doRemarks" cols="70" rows="7" maxlength="1000">${withdrawnDto.doRemarks}</textarea>
                                                                            </div>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group" id="commentForApplicantDiv" <c:if test="${withdrawnDto.doDecision ne MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}">style="display: none;"</c:if>>
                                                                        <label for="commentForApplicant" class="col-xs-12 col-md-4 control-label">Comments to Applicant <span style="color: red">*</span></label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10">
                                                                            <div class="input-group">
                                                                                <textarea id="commentForApplicant" name="commentForApplicant" cols="70" rows="7" maxlength="1000"><c:out value="${withdrawnDto.commentForApplicant}"/></textarea>
                                                                                <span data-err-ind="reasonForRejection" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                </div>
                                                                <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
                                                                <div style="text-align: right">
                                                                    <button name="submitBtn" id="submitBtn" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                                <div>&nbsp;</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
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