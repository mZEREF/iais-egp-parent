<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-ins-afc-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-ins-afc-do.js"></script>
<%--@elvariable id="reviewFollowUpDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto"--%>
<%--@elvariable id="insDecision" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto"--%>
<%--@elvariable id="activeTab" type="java.lang.String"--%>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<div class="dashboard">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
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
                                                <a href="#${InspectionConstants.TAB_SUBMIT_INTO}" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                            </li>
                                            <li id="documents" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">class="active"</c:if>>
                                                <a href="#${InspectionConstants.TAB_DOC}" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="certificationLi" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_CERTIFICATION}">class="active"</c:if>>
                                                <a href="#${InspectionConstants.TAB_CERTIFICATION}" aria-controls="tabCertification" role="tab" data-toggle="tab">Certification</a>
                                            </li>
                                            <li id="facilityDetail" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAIL}">class="active"</c:if>>
                                                <a href="#${InspectionConstants.TAB_FAC_DETAIL}" aria-controls="tabFacilityDetail" role="tab" data-toggle="tab">Application Recommendations</a>
                                            </li>
                                            <li id="process" role="presentation" <c:if test="${activeTab eq InspectionConstants.TAB_PROCESSING}">class="active"</c:if>>
                                                <a href="#${InspectionConstants.TAB_PROCESSING}" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide <c:if test="${empty activeTab or activeTab eq InspectionConstants.TAB_SUBMIT_INTO}">active</c:if>">
                                                    <a href="#${InspectionConstants.TAB_SUBMIT_INTO}" aria-controls="tabInfo" role="tab" data-toggle="tab">Information</a>
                                                </div>
                                                <div class="swiper-slide <c:if test="${activeTab eq InspectionConstants.TAB_DOC}">active</c:if>">
                                                    <a href="#${InspectionConstants.TAB_DOC}" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide <c:if test="${activeTab eq InspectionConstants.TAB_CERTIFICATION}">active</c:if>">
                                                    <a href="#${InspectionConstants.TAB_CERTIFICATION}" aria-controls="tabCertification" role="tab" data-toggle="tab">Certification</a>
                                                </div>
                                                <div class="swiper-slide <c:if test="${activeTab eq InspectionConstants.TAB_FAC_DETAIL}">active</c:if>">
                                                    <a href="#${InspectionConstants.TAB_FAC_DETAIL}" aria-controls="tabFacilityDetail" role="tab" data-toggle="tab">Application Recommendations</a>
                                                </div>
                                                <div class="swiper-slide <c:if test="${activeTab eq InspectionConstants.TAB_PROCESSING}">active</c:if>">
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
                                            <div class="tab-pane <c:if test="${activeTab eq InspectionConstants.TAB_CERTIFICATION}">active</c:if>" id="${InspectionConstants.TAB_CERTIFICATION}" role="tabpanel">
                                                <%@include file="certificationDocumentsPage.jsp"%>
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
                                                                        <p><iais:code code="${reviewAFCReportDto.appStatus}"/></p>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="processingDecision" class="ao-cert-decision" id="processingDecision">
                                                                                <option value="">Please Select</option>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_AO}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_AO}">selected="selected"</c:if>>Route to AO</option>
                                                                            </select>
                                                                            <span data-err-ind="decision" class="error-msg" ></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group" id="selectMohUserDiv"  <c:if test="${insDecision.decision ne MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_AO}">style="display: none;"</c:if>>
                                                                    <label for="selectMohUser" class="col-xs-12 col-md-4 control-label">Select Approving Officer <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="selectMohUser" class="selectMohUserDropdown" id="selectMohUser">
                                                                                <option value="">Please Select</option>
                                                                                <c:forEach var="selection" items="${selectRouteToMoh}">
                                                                                    <option value="${selection.value}" <c:if test="${insDecision.selectMohUser eq selection.value}">selected="selected"</c:if>>${selection.text}</option>
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
                                                                            <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="1500"><c:out value="${insDecision.remark}"/></textarea>
                                                                            <span data-err-ind="remarks" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <span data-err-ind="chooseOne" class="error-msg"></span>
                                                            </div>
                                                            <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Previous</a>
                                                            <div style="text-align: right">
                                                                <button name="submitBtn" id="submitBtn" type="button" class="btn btn-primary">Submit</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <br>
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
        <%@include file="fifthRoundInformPage.jsp"%>
    </form>
    <%@include file="/WEB-INF/jsp/iais/doDocument/fileUploadModal.jsp"%>
</div>