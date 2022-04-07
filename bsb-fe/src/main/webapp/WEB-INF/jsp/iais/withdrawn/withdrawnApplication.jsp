<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-withdrawn.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-withdrawn-file.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="doNext">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <%--<div class="main-content">--%>
    <div class="container">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <%--<div class="center-content">--%>
                <div class="internet-content">
                    <iais:body>
                        <div class="col-xs-12">
                            <div class="panel panel-default">
                                <div class="panel-heading"><strong>Withdrawal Submission</strong></div>
                                <div class="row form-horizontal">
                                        <%--@elvariable id="withdrawnDto" type="sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto"--%>
                                    <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Current Status</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <iais:code code="${withdrawnDto.currentStatus}"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Application No</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <c:out value="${withdrawnDto.appNo}"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Application Type</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <iais:code code="${withdrawnDto.appType}"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Process Type</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <iais:code code="${withdrawnDto.processType}"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label for="reason">Reason for Withdrawn</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <iais:select name="reason" id="reason" value="${withdrawnDto.reason}" codeCategory="CATE_ID_BSB_REASON_FOR_WITHDRAWN" firstOption="Please Select" onchange="isRemarksMandatory()"/>
                                                <span data-err-ind="reason" class="error-msg"></span>
                                            </div>
                                        </div>
                                            <%--If the Reason for Withdrawal is indicated as "Others", please provide details under Supporting Remarks.--%>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label for="remarks">Supporting Remarks</label>
                                                <span id="remarksSpan" class="mandatory otherQualificationSpan" style="display: none">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <textarea name="remarks" id="remarks" maxlength="1000" cols="70" rows="7">${withdrawnDto.remarks}</textarea>
                                                <span data-err-ind="remarks" class="error-msg"></span>
                                            </div>
                                        </div>
                                            <%--upload file--%>
                                        <div class="form-group">
                                            <%@include file="document.jsp" %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </iais:body>
                </div>
                <%--                </div>--%>
            </div>
        </div>
    </div>
    <%--    </div>--%>
    <div class="row">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-left">
                <%--get href from delegator--%>
                <a class="back" href="${backUrl}"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="form-group">
                <div class="col-xs-12 col-md-6 text-right">
                    <a href="${backUrl}" type="button" class="btn btn-secondary save">CANCEL</a>
                    <button class="btn btn-primary save" id="nextBtn">NEXT</button>
                </div>
            </div>
        </div>
    </div>
</form>