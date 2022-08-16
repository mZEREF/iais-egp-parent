<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-pre-inspection.js"></script>

<%--@elvariable id="insDecision" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto"--%>
<%--@elvariable id="submissionDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo"--%>

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
                                            <li class="active" id="info" role="presentation">
                                                <a href="#tabInfo" id="doInfo" aria-controls="tabInfo" role="tab"
                                                   data-toggle="tab">Information</a>
                                            </li>
                                            <li id="documents" role="presentation">
                                                <a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments"
                                                   role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="facility" role="presentation">
                                                <a href="#tabFacility" id="doFacility" aria-controls="tabFacility"
                                                   role="tab" data-toggle="tab">Application Recommendations</a>
                                            </li>
                                            <li id="checklist" role="presentation">
                                                <a href="#tabChecklist" id="doChecklist" aria-controls="tabChecklist"
                                                   role="tab" data-toggle="tab">Checklist</a>
                                            </li>
                                            <li id="process" role="presentation">
                                                <a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing"
                                                   role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                       data-toggle="tab">Information</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                       data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabFacility" aria-controls="tabFacility" role="tab"
                                                       data-toggle="tab">Application Recommendations</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabChecklist" aria-controls="tabChecklist" role="tab"
                                                       data-toggle="tab">Checklist</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabProcessing" aria-controls="tabProcessing" role="tab"
                                                       data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/submissionDetailsInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabFacility" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/common/facilityDetailsInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabChecklist" role="tabpanel">
                                                <%@include file="../../common/checklistInfo.jsp" %>
                                                <iais:action>
                                                    <c:choose>
                                                        <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
                                                        <c:when test="${goBackUrl ne null}">
                                                            <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em
                                                                    class="fa fa-angle-left"></em> Previous</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <div style="float:right">
                                                        <button type="button" class="btn btn-primary">
                                                            DOWNLOAD
                                                        </button>
                                                        <button id="adhocBth" type="button" style="float:right" class="btn btn-primary" >
                                                            EDIT
                                                        </button>
                                                    </div>
                                                </iais:action>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
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
                                                                    <label for="remarks" class="col-xs-12 col-md-4 control-label">Remarks</label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <textarea id="remarks" name="remarks" cols="70" rows="7" maxlength="300"><c:out value="${insDecision.remark}"/></textarea>
                                                                            <span data-err-ind="remarks" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="processingDecision" class="col-xs-12 col-md-4 control-label">Processing Decision <span style="color: red">*</span></label>
                                                                    <div class="col-sm-7 col-md-5 col-xs-10">
                                                                        <div class="input-group">
                                                                            <select name="processingDecision" class="processingDecisionDropdown" id="processingDecision">
                                                                                <option value="">Please Select</option>
                                                                                <%--TODO: check all decision--%>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_MARK_INSPECTION_TASK_AS_READY}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_MARK_INSPECTION_TASK_AS_READY}">selected="selected"</c:if>>
                                                                                    Mark as ready
                                                                                </option>
                                                                                <%--@elvariable id="canRfi" type="java.lang.Boolean"--%>
                                                                                <c:if test="${canRfi}">
                                                                                    <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}">selected="selected"</c:if>>
                                                                                        Request for information
                                                                                    </option>
                                                                                </c:if>
                                                                                <option value="${MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION}" <c:if test="${insDecision.decision eq MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION}">selected="selected"</c:if>>Skip Inspection</option>
                                                                            </select>
                                                                            <span data-err-ind="processingDecision" class="error-msg"></span>
                                                                        </div>
                                                                    </div>

                                                                    <div id="rfiCheckBox" <c:if test="${insDecision.decision ne MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION}">style="display: none"</c:if>>
                                                                        <iais:field value="Request For Information" mandatory="true"/>
                                                                        <iais:value width="7">
                                                                            <p>
                                                                                <input type="checkbox" name="AppPreInspRfiCheck" id="AppPreInspRfiCheck" value="true" <c:if test="${AppPreInspRfiCheck}">checked="checked"</c:if>/>
                                                                                <span style="font-size: 16px"><c:out value="Application"/></span>
                                                                            </p>
                                                                            <p>
                                                                                <input type="checkbox" name="SelfPreInspRfiCheck" id="SelfPreInspRfiCheck" value="true" <c:if test="${SelfPreInspRfiCheck}">checked="checked"</c:if>/>
                                                                                <span style="font-size: 16px"><c:out value="Pre-Inspection Checklist"/></span>
                                                                            </p>
                                                                            <span class="error-msg" name="iaisErrorMsg" id="error_preInspRfiCheck"></span>
                                                                        </iais:value>
                                                                    </div>

                                                                    <div class="clear"></div>
                                                                </div>
                                                            </div>
                                                            <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                                                            <div style="text-align: right"><button name="submitBtn" id="submitBtn" type="button" class="btn btn-primary">Submit</button></div>
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
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>