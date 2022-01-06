<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-report-event.js"></script>

<div class="dashboard">
    <form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
        <%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-md-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="active" id="info" role="presentation">
                                                <a href="#tabInfo" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                            </li>
                                            <li id="personnel" role="presentation">
                                                <a href="#tabPersonnel" id="doPersonnel" aria-controls="tabPersonnel" role="tab" data-toggle="tab">Biosafety Personnel</a>
                                            </li>
                                            <li id="bat" role="presentation">
                                                <a href="#tabBat" id="doBat" aria-controls="tabBat" role="tab" data-toggle="tab">BA/T</a>
                                            </li>
                                            <li id="documents" role="presentation">
                                                <a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
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
                                                    <a href="#tabPersonnel" aria-controls="tabPersonnel" role="tab" data-toggle="tab">Biosafety Personnel</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabBat" aria-controls="tabBat" role="tab" data-toggle="tab">BA/T</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/view/common/applicationInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabPersonnel" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/view/common/biosafetyPersonnel.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabBat" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/view/common/batPage.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/view/common/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                                <div class="alert alert-info" role="alert" style="margin-top: 15px">
                                                    <h4>Processing Status Update</h4>
                                                </div>
                                                <div class="row" style="margin: 20px 0">
                                                    <div class="col-xs-12">
                                                        <div class="form-horizontal">
                                                            <c:set var="dto" value="${processDto.processingDto}"/>
                                                            <c:forEach var="role" items="${mohRoles}">
                                                                <c:set var="item" value="${dto.newRemarkMap.get(role)}"/>
                                                                <c:if test="${item ne null}">
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="remarks${role}">${role} Remarks</label>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <label id="remarks${role}" class="label-normal"><c:out value="${item.remark}"/></label>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="decision${role}">${role} Decision</label>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <label id="decision${role}" class="label-normal"><iais:code code="${item.decision}"/></label>                                                                        </div>
                                                                    </div>
                                                                </c:if>
                                                            </c:forEach>
                                                            <div class="form-group ">
                                                                <div class="col-sm-5 control-label">
                                                                    <label for="status">Current Status</label>
                                                                </div>
                                                                <div class="col-sm-6 col-md-7">
                                                                    <label id="status" class="label-normal"><iais:code code="${dto.currentStatus}"/></label>
                                                                    <span data-err-ind="status" class="error-msg"></span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group ">
                                                                <div class="col-sm-5 control-label">
                                                                    <label for="remarks">Remarks</label>
                                                                </div>
                                                                <div class="col-sm-6 col-md-7">
                                                                    <textarea autocomplete="off" class="col-xs-12" name="remarks" id="remarks" maxlength="1000" style="width: 100%"><c:out value="${dto.remarks}"/></textarea>
                                                                    <span data-err-ind="remarks" class="error-msg"></span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group ">
                                                                <div class="col-sm-5 control-label">
                                                                    <label for="decision">Processing Decision</label>
                                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                                </div>
                                                                <div class="col-sm-6 col-md-7">
                                                                    <select name="decision" id="decision">
                                                                        <c:forEach var="ops" items="${decisionOps}">
                                                                            <option value="${ops.value}" <c:if test="${dto.decision eq ops.value}">selected="selected"</c:if>>${ops.text}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                    <span data-err-ind="decision" class="error-msg"></span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group ">
                                                                <div class="col-sm-5 control-label">
                                                                    <label for="approvalOfficer">Select Approval Officer</label>
                                                                </div>
                                                                <div class="col-sm-6 col-md-7">
                                                                    <select name="approvalOfficer" id="approvalOfficer">
                                                                        <option>Please Select</option>
                                                                        <option>Alice</option>
                                                                        <option>Bob</option>
                                                                    </select>
                                                                    <span data-err-ind="approvalOfficer" class="error-msg"></span>
                                                                </div>
                                                            </div>
                                                            <a style=" float:left;padding-top: 1.1%;text-decoration:none;" id="back"><em class="fa fa-angle-left"> </em> Back</a>
                                                            <button class="btn btn-primary" type="button" id="submitButton" style="float: right">Submit</button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="alert alert-info" role="alert" style="margin-top: 15px">
                                                    <h4>Processing History</h4>
                                                </div>
                                                <div class="row" style="margin-top: 20px">
                                                    <div class="col-xs-12">
                                                        <table aria-describedby="" class="table">
                                                            <thead>
                                                            <tr>
                                                                <th scope="col" style="width: 15%">Username</th>
                                                                <th scope="col" style="width: 15%">Status Update</th>
                                                                <th scope="col" style="width: 15%">Remarks</th>
                                                                <th scope="col" style="width: 15%">Last Updated</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody id="tbodyFileListId">
                                                            <c:forEach var="history" items="${dto.processHistoryList}">
                                                                <tr>
                                                                    <td><c:out value="${history.userName}"/></td>
                                                                    <td><c:out value="${history.status}"/></td>
                                                                    <td><c:out value="${history.remarks}"/></td>
                                                                    <td><c:out value="${history.updateDate}"/></td>
                                                                </tr>
                                                            </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
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