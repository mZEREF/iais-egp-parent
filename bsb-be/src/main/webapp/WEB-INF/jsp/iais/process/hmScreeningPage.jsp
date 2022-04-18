<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-process-hm-screening.js"></script>
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
                                                <a href="#tabInfo" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                            </li>
                                            <li id="documents" role="presentation">
                                                <a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="facilityDetails" role="presentation">
                                                <a href="#tabFacilityDetails" id="doFacilityDetails" aria-controls="tabFacilityDetails" role="tab" data-toggle="tab">Facility Details</a>
                                            </li>
                                            <li id="process" role="presentation">
                                                <a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabFacilityDetails" aria-controls="tabFacilityDetails" role="tab" data-toggle="tab">Facility Details</a>
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
                                                <%@include file="/WEB-INF/jsp/iais/common/facilityDetailsInfo.jsp"%>
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
                                                                <%--@elvariable id="mohProcessDto" type="sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto"--%>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Current Status</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${mohProcessDto.submissionDetailsInfo.applicationStatus}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">AO Recommendation</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><iais:code code="${mohProcessDto.lastRecommendation}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">Will MOH conduct on-site inspection?</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10 control-label">
                                                                        <div class="input-group">
                                                                            <label>
                                                                                <input type="radio" name="inspectionRequired" <c:if test="${mohProcessDto.inspectionRequired eq 'Y'}">checked="checked"</c:if> value="Y" disabled="disabled"/>
                                                                            </label>
                                                                            <span class="check-circle">Yes</span>
                                                                            <label>
                                                                                <input type="radio" name="inspectionRequired" <c:if test="${mohProcessDto.inspectionRequired eq 'N'}">checked="checked"</c:if> value="N" disabled="disabled"/>
                                                                            </label>
                                                                            <span class="check-circle">No</span>
                                                                            <span data-err-ind="inspectionRequired" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <c:if test="${mohProcessDto.facilityDetailsInfo.classification eq 'FACCLA001' or mohProcessDto.facilityDetailsInfo.classification eq 'FACCLA002'}">
                                                                    <div class="form-group">
                                                                        <label class="col-xs-12 col-md-4 control-label">Will MOH-AFC conduct the certification?</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-10 control-label">
                                                                            <div class="input-group">
                                                                                <label>
                                                                                    <input type="radio" name="certificationRequired" <c:if test="${mohProcessDto.certificationRequired eq 'Y'}">checked="checked"</c:if> value="Y" disabled="disabled"/>
                                                                                </label>
                                                                                <span class="check-circle">Yes</span>
                                                                                <label>
                                                                                    <input type="radio" name="certificationRequired" <c:if test="${mohProcessDto.certificationRequired eq 'N'}">checked="checked"</c:if> value="N" disabled="disabled"/>
                                                                                </label>
                                                                                <span class="check-circle">No</span>
                                                                                <span data-err-ind="certificationRequired" class="error-msg" ></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </c:if>
                                                                <div class="form-group">
                                                                    <label class="col-xs-12 col-md-4 control-label">AO Remarks</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <p><c:out value="${mohProcessDto.lastRemarks}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="processingDecision" class="decisionDrop" id="processingDecision">
                                                                                <option value="">Please Select</option>
                                                                                <option value="MOHPRO007" <c:if test="${mohProcessDto.processingDecision eq 'MOHPRO007'}">selected="selected"</c:if>>Approve</option>
                                                                                <option value="MOHPRO003" <c:if test="${mohProcessDto.processingDecision eq 'MOHPRO003'}">selected="selected"</c:if>>Reject</option>
                                                                            </select>
                                                                            <span data-err-ind="processingDecision" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="300"><c:out value="${mohProcessDto.remarks}"/></textarea>
                                                                            <span data-err-ind="remark" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                            </div>
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