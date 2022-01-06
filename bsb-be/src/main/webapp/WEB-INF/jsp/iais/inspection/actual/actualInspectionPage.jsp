<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>

<%--@elvariable id="insInfo" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsInfoDto"--%>
<%--@elvariable id="processDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto"--%>
<%--@elvariable id="insFindingList" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsFindingFormDto"--%>
<%--@elvariable id="itemSelection" type="java.lang.String"--%>
<%--@elvariable id="activeTab" type="java.lang.String"--%>

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
                                        <li <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_FAC_INFO}">class="active"</c:if> id="info" role="presentation">
                                            <a href="#${InspectionConstants.TAB_FAC_INFO}" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Facility Info</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_INS_DETAIL}">class="active"</c:if> id="insDetails" role="presentation">
                                            <a href="#${InspectionConstants.TAB_INS_DETAIL}" id="doInsDetails" aria-controls="tabInsDetails" role="tab" data-toggle="tab">Inspection Details</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">class="active"</c:if> id="documents" role="presentation">
                                            <a href="#${InspectionConstants.TAB_DOC}" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                        </li>
                                        <li <c:if test="${activeTab eq InspectionConstants.TAB_INS_FINDING}">class="active"</c:if> id="insFinding" role="presentation">
                                            <a href="#${InspectionConstants.TAB_INS_FINDING}" id="doInsFinding" aria-controls="tagInsFinding" role="tab" data-toggle="tab">Inspection Finding(s)</a>
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
                                                <a href="#${InspectionConstants.TAB_INS_DETAIL}" aria-controls="tabInsDetails" role="tab" data-toggle="tab">Inspection Details</a>
                                            </div>
                                            <div class="swiper-slide">
                                                <a href="#${InspectionConstants.TAB_DOC}" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </div>
                                            <div class="swiper-slide">
                                                <a href="#${InspectionConstants.TAB_INS_FINDING}" aria-controls="tagInsFinding" role="tab" data-toggle="tab">Inspection Finding(s)</a>
                                            </div>
                                            <div class="swiper-slide">
                                                <a href="#${InspectionConstants.TAB_PROCESSING}" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="tab-content">
                                        <div class="tab-pane <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_FAC_INFO}">active</c:if>" id="${InspectionConstants.TAB_FAC_INFO}" role="tabpanel">
                                            <%@include file="../pre/facilityInfo.jsp" %>
                                        </div>
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_INS_DETAIL}">active</c:if>" id="${InspectionConstants.TAB_INS_DETAIL}" role="tabpanel">
                                            <%@include file="inspectionDetail.jsp"%>
                                        </div>
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">active</c:if>" id="${InspectionConstants.TAB_DOC}" role="tabpanel">
                                            <%@include file="/WEB-INF/jsp/iais/process/common/tabDocuments.jsp"%>
                                        </div>
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_INS_FINDING}">active</c:if>" id="${InspectionConstants.TAB_INS_FINDING}" role="tabpanel">
                                            <%@include file="inspectionFinding.jsp"%>
                                        </div>
                                        <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_PROCESSING}">active</c:if>" id="${InspectionConstants.TAB_PROCESSING}" role="tabpanel">
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
                                                            <div class="form-group">
                                                                <label class="col-xs-12 col-md-4 control-label">Current Status</label>
                                                                <div class="col-sm-7 col-md-5 col-xs-10">
                                                                    <p><iais:code code="${insInfo.appStatus}"/></p>
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
                                                                        <select name="processingDecision" id="processingDecision">
                                                                            <option value="">Please Select</option>
                                                                            <option value="MOHPRO022" <c:if test="${processDto.decision eq 'MOHPRO022'}">selected="selected"</c:if>>Submit report to AO</option>
                                                                            <c:if test="${insInfo.appStatus eq 'BSBAPST029'}">
                                                                            <option value="MOHPRO023" <c:if test="${processDto.decision eq 'MOHPRO023'}">selected="selected"</c:if>>Route report to applicant</option>
                                                                            <option value="MOHPRO024" <c:if test="${processDto.decision eq 'MOHPRO024'}">selected="selected"</c:if>>Mark report as final</option>
                                                                            </c:if>
                                                                        </select>
                                                                        <span data-err-ind="decision" class="error-msg" ></span>
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
</div>