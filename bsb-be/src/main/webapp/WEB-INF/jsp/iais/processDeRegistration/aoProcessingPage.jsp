<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-process-deregistration.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>

<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="ifProcess" id="ifProcess" value="${processPageValidation}">
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
                                            <li id="dynamicContent" role="presentation">
                                                <a href="#tabDynamicContent" id="doContent" aria-controls="tabDynamicContent" role="tab" data-toggle="tab">Dynamic Content</a>
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
                                                    <a href="#tabDynamicContent" aria-controls="tabDynamicContent" role="tab" data-toggle="tab">Dynamic Content</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/processDeRegistration/common/applicationInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabDynamicContent" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/processDeRegistration/common/dynamicContent.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <strong>
                                                        <h4>Processing Status Update</h4>
                                                    </strong>
                                                </div>
                                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp" id="beInboxFilter">
                                                                <iais:section title="">
                                                                    <div>&nbsp</div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Current Status" required="false"/>
                                                                            <iais:value width="10"><p><iais:code code="${aoProcessDto.currentStatus}"/></p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="DO Remarks" required="false"/>
                                                                            <iais:value width="10"><p><c:out value="${aoProcessDto.doRemarks}"/></p></iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Final Remarks" required="false"></iais:field>
                                                                            <iais:value width="10">
                                                                                <div class="form-check">
                                                                                    <input type="radio" class="form-check-input" disabled <c:if test="${aoProcessDto.doFinalRemarks eq 'yes'}">checked="checked"</c:if> value="yes"/>
                                                                                    <label class="form-check-label"><span class="check-square"></span>Yes</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="radio" class="form-check-input" disabled <c:if test="${aoProcessDto.doFinalRemarks eq 'no'}">checked="checked"</c:if> value="no"/>
                                                                                    <label class="form-check-label"><span class="check-square"></span>No</label>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="AO Remarks" required="false" width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea name="aoRemarks" cols="70" rows="7" maxlength="300"><c:out value="${aoProcessDto.aoRemarks}"/></textarea>
                                                                                        <span data-err-ind="aoRemarks" class="error-msg"></span>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Final Remarks" required="false"/>
                                                                            <iais:value width="10">
                                                                                <div class="form-check">
                                                                                    <input type="radio" class="form-check-input" name="finalRemarks" id="finalRemarksYes" <c:if test="${aoProcessDto.finalRemarks eq 'yes'}">checked="checked"</c:if> value="yes"/>
                                                                                    <label for="finalRemarksYes" class="form-check-label"><span class="check-square"></span>Yes</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="radio" class="form-check-input" name="finalRemarks" id="finalRemarksNo" <c:if test="${aoProcessDto.finalRemarks eq 'no'}">checked="checked"</c:if> value="no"/>
                                                                                    <label for="finalRemarksNo" class="form-check-label"><span class="check-square"></span>No</label>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Processing Decision" required="true"/>
                                                                            <iais:value width="10">
                                                                                <select name="processingDecision" class="processingDecisionDropdown" id="processingDecision">
                                                                                    <option value="">Please Select</option>
                                                                                        <%--TODO: check these decision--%>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}" <c:if test="${aoProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE}">selected="selected"</c:if>>Approve</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}" <c:if test="${aoProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_REJECT}">selected="selected"</c:if>>Reject</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO}" <c:if test="${aoProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO}">selected="selected"</c:if>>Route back to Duty Officer</option>
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM}" <c:if test="${aoProcessDto.processingDecision eq MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM}">selected="selected"</c:if>>Route to Higher Manager</option>
                                                                                </select>
                                                                                <span data-err-ind="processingDecision" class="error-msg" ></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="reasonForRejection">
                                                                        <iais:row>
                                                                            <iais:field value="Reason for Rejection" required="true" width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea name="reasonForRejection" cols="70" rows="7" maxlength="500"><c:out value="${aoProcessDto.reasonForRejection}"/></textarea>
                                                                                        <span data-err-ind="reasonForRejection" class="error-msg"></span>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <div style="text-align: right">
                                                                    <button name="submitButton" id="submitButton" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
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
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>