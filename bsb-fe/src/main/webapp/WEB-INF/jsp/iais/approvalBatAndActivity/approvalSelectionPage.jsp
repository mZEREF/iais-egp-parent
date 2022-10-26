<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approval-bat-and-activity.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-edit.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%@include file="dashboard.jsp"%>
<%--@elvariable id="editJudge" type="java.lang.Boolean"--%>
<%--@elvariable id="hasError" type="java.lang.Boolean"--%>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_load_draft" value="">
    <input type="hidden" id="editJudge" value="${editJudge}" readonly disabled>
    <input type="hidden" id="hasError" value="${hasError}" readonly disabled>
    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <%--@elvariable id="editApp" type="java.lang.Boolean"--%>
            <div class="col-xs-12 col-sm-12" style="padding: 0;margin-bottom: 50px">
                <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Please select the type of approval</p>
                <div class="form-check-gp" style="margin-top: 30px">
                    <div class="form-check">
                        <input type="radio" class="form-check-input" name="processType" id="approvalFacilityActivity" <c:if test="${approvalSelectionDto.processType eq MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE}"/>
                        <label for="approvalFacilityActivity" class="form-check-label"><span class="check-circle"></span>Approval for Facility Activity Type</label>
                    </div>
                    <div class="form-check">
                        <input type="radio" class="form-check-input" name="processType" id="approvalPossess" <c:if test="${approvalSelectionDto.processType eq MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS}"/>
                        <label for="approvalPossess" class="form-check-label"><span class="check-circle"></span>Approval to Possess</label>
                    </div>
                    <div class="form-check">
                        <input type="radio" class="form-check-input" name="processType" id="approvalLarge" <c:if test="${approvalSelectionDto.processType eq MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP}"/>
                        <label for="approvalLarge" class="form-check-label"><span class="check-circle"></span>Approval to Large Scale Produce</label>
                    </div>
                    <div class="form-check">
                        <input type="radio" class="form-check-input" name="processType" id="approvalSpecial" <c:if test="${approvalSelectionDto.processType eq MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE}"/>
                        <label for="approvalSpecial" class="form-check-label"><span class="check-circle"></span>Special Approval to Handle</label>
                    </div>
                    <div>
                        <span data-err-ind="processType" class="error-msg"></span>
                    </div>
                </div>
            </div>
            <span data-err-ind="approvalTypeSelection" class="error-msg"></span>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-3">
                    <a class="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                </div>
                <div class="col-xs-12 col-md-9">
                    <div class="text-right button-group">
                        <a class="btn btn-primary next" id="continueB" >Next</a>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="judgeResumeFromDraft.jsp"%>
    </div>
</form>