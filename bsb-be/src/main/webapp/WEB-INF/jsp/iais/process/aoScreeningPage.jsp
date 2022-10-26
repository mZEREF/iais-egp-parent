<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.StageConstants" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-process-ao-screening.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<div class="dashboard">
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
                            <iais:body>
                                <div class="col-xs-12">
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
                                            <div class="tab-pane <c:if test="${activeTab eq ModuleCommonConstants.TAB_PROCESSING}">active</c:if>" id="${ModuleCommonConstants.TAB_PROCESSING}" role="tabpanel">
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
                                                                <%--@elvariable id="mohProcessDto" type="sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto"--%>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Current Status</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${mohProcessDto.submissionDetailsInfo.applicationStatus}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <c:if test="${StageConstants.ROLE_DO eq mohProcessDto.lastRole}">
                                                                        <label class="col-xs-12 col-md-4 control-label">Processing Decision/Recommendation by DO</label>
                                                                    </c:if>
                                                                    <c:if test="${StageConstants.ROLE_HM eq mohProcessDto.lastRole}">
                                                                        <label class="col-xs-12 col-md-4 control-label">HM's Decision</label>
                                                                    </c:if>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${mohProcessDto.lastRecommendation}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Will MOH conduct on-site inspection?</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10 control-label">
                                                                        <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                                                            <input class="form-check-input" type="radio" id="inspectionRequiredY" name="inspectionRequired" value = "${MasterCodeConstants.YES}" <c:if test="${MasterCodeConstants.YES eq mohProcessDto.inspectionRequired}">checked="checked"</c:if> disabled="disabled"/>
                                                                            <label for="inspectionRequiredY" class="form-check-label"><span class="check-circle"></span>Yes</label>
                                                                        </div>
                                                                        <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                                                            <input class="form-check-input" type="radio" id="inspectionRequiredN" name="inspectionRequired" value = "${MasterCodeConstants.NO}" <c:if test="${MasterCodeConstants.NO eq mohProcessDto.inspectionRequired}">checked="checked"</c:if> disabled="disabled"/>
                                                                            <label for="inspectionRequiredN" class="form-check-label"><span class="check-circle"></span>No</label>
                                                                        </div>
                                                                        <span data-err-ind="inspectionRequired" class="error-msg" ></span>
                                                                    </div>
                                                                </div>
                                                                <c:if test="${mohProcessDto.facilityDetailsInfo.classification eq 'FACCLA001' or mohProcessDto.facilityDetailsInfo.classification eq 'FACCLA002'}">
                                                                    <div class="form-group">
                                                                        <label class="col-xs-12 col-md-4 control-label">Will MOH-AFC conduct the certification?</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10 control-label">
                                                                            <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                                                                <input class="form-check-input" type="radio" id="certificationRequiredY" name="certificationRequired" value = "${MasterCodeConstants.YES}" <c:if test="${MasterCodeConstants.YES eq mohProcessDto.certificationRequired}">checked="checked"</c:if> disabled="disabled"/>
                                                                                <label for="certificationRequiredY" class="form-check-label"><span class="check-circle"></span>Yes</label>
                                                                            </div>
                                                                            <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                                                                <input class="form-check-input" type="radio" id="certificationRequiredN" name="certificationRequired" value = "${MasterCodeConstants.NO}" <c:if test="${MasterCodeConstants.NO eq mohProcessDto.certificationRequired}">checked="checked"</c:if> disabled="disabled"/>
                                                                                <label for="certificationRequiredN" class="form-check-label"><span class="check-circle"></span>No</label>
                                                                            </div>
                                                                            <span data-err-ind="certificationRequired" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                </c:if>
                                                                <div class="form-group">
                                                                    <c:if test="${StageConstants.ROLE_DO eq mohProcessDto.lastRole}">
                                                                        <label class="col-xs-12 col-md-4 control-label">DO Remarks</label>
                                                                    </c:if>
                                                                    <c:if test="${StageConstants.ROLE_HM eq mohProcessDto.lastRole}">
                                                                        <label class="col-xs-12 col-md-4 control-label">HM Remarks</label>
                                                                    </c:if>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><c:out value="${mohProcessDto.lastRemarks}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="processingDecision" class="processingDecisionDropdown" id="processingDecision">
                                                                                <option value="">Please Select</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE_TO_PROCEED_TO_NEXT_STAGE}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE_TO_PROCEED_TO_NEXT_STAGE}">selected="selected"</c:if>>Approve to proceed to next stage</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT_APPLICATION}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_REJECT_APPLICATION}">selected="selected"</c:if>>Reject application</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO}">selected="selected"</c:if>>Route back to DO</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM}" <c:if test="${mohProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM}">selected="selected"</c:if>>Route to HM</option>
                                                                            </select>
                                                                            <span data-err-ind="processingDecision" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group" id="aoRecommendationDiv" <c:if test="${mohProcessDto.processingDecision ne MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM}">style="display: none;"</c:if>>
                                                                    <label class="col-xs-12 col-md-4 control-label">AO Recommendation <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10 control-label">
                                                                        <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                                                            <input class="form-check-input" type="radio" id="aoRecommendationY" name="aoRecommendation" value = "Approve" <c:if test="${mohProcessDto.aoRecommendation eq 'Approve'}">checked="checked"</c:if>/>
                                                                            <label for="aoRecommendationY" class="form-check-label"><span class="check-circle"></span>Approve</label>
                                                                        </div>
                                                                        <div class="form-check col-xs-4 col-sm-5 col-lg-3" style="margin-top: 6px; padding: 0;">
                                                                            <input class="form-check-input" type="radio" id="aoRecommendationN" name="aoRecommendation" value = "Reject" <c:if test="${mohProcessDto.aoRecommendation eq 'Reject'}">checked="checked"</c:if>/>
                                                                            <label for="aoRecommendationN" class="form-check-label"><span class="check-circle"></span>Reject</label>
                                                                        </div>
                                                                        <span data-err-ind="aoRecommendation" class="error-msg" ></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group" id="selectMohUserDiv" <c:if test="${mohProcessDto.processingDecision ne MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM}">style="display: none;"</c:if>>
                                                                    <label for="selectMohUser" class="col-xs-12 col-md-4 control-label">Select HM <span style="color: red">*</span></label>
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

                                                                <%@include file="common/internalDocAssessment.jsp"%>

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
                                                                <div class="form-group" id="reasonForRejectionDiv" <c:if test="${mohProcessDto.processingDecision ne MasterCodeConstants.MOH_PROCESS_DECISION_REJECT_APPLICATION}">style="display: none;"</c:if>>
                                                                    <label for="reasonForRejection" class="col-xs-12 col-md-4 control-label">Reason for rejection <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <textarea id="reasonForRejection" name="reasonForRejection" cols="70" rows="7" maxlength="1000"><c:out value="${mohProcessDto.reasonForRejection}"/></textarea>
                                                                            <span data-err-ind="reasonForRejection" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                            </div>
                                                            <%@include file="common/footer.jsp" %>
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