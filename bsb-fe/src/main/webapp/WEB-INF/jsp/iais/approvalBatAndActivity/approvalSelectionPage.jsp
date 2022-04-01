<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approval-bat-and-activity.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%@include file="dashboard.jsp"%>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <!--todo: The facility value will be obtained from another method, and the JSP will be deleted -->
            <div class="row">
                <div class="form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label class="control-label control-set-font control-font-label">Facility Name</label>
                        <span class="mandatory">*</span>
                    </div>
                    <div class="col-sm-4 col-md-7 control-font-label" style="z-index: 30">
                        <input type="hidden" id="facilityName" name="facilityName" value="${approvalSelectionDto.facilityName}">
                        <select name="facilityId" id="facilityId">
                            <option value="">Please Select</option>
                            <c:forEach items="${selectionFacilityId}" var="facSelect">
                                <c:set var="maskedFacilityId"><iais:mask name="facilityId" value="${facSelect.value}"/></c:set>
                                <option value="${maskedFacilityId}" <c:if test="${approvalSelectionDto.facilityId eq facSelect.value}">selected="selected"</c:if>>${facSelect.text}</option>
                            </c:forEach>
                        </select>
                        <span data-err-ind="facilityId" class="error-msg"></span>
                    </div>
                </div>
            </div>
            <br/>
            <div class="col-xs-12 col-sm-12" style="padding-top: 30px">
                <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Please select the type of approval</p>
                <div class="form-check-gp">
                    <div class="form-check">
                        <input type="radio" class="form-check-input" name="processType" <c:if test="${approvalSelectionDto.processType eq 'PROTYPE012'}">checked="checked"</c:if> value="PROTYPE012"/>
                        <label class="form-check-label"><span class="check-circle"></span>Approval for Facility Activity Type</label>
                    </div>
                    <div class="form-check">
                        <input type="radio" class="form-check-input" name="processType" <c:if test="${approvalSelectionDto.processType eq 'PROTYPE002'}">checked="checked"</c:if> value="PROTYPE002"/>
                        <label class="form-check-label"><span class="check-circle"></span>Approval to Possess</label>
                    </div>
                    <div class="form-check">
                        <input type="radio" class="form-check-input" name="processType" <c:if test="${approvalSelectionDto.processType eq 'PROTYPE003'}">checked="checked"</c:if> value="PROTYPE003"/>
                        <label class="form-check-label"><span class="check-circle"></span>Approval to Large Scale Produce</label>
                    </div>
                    <div class="form-check">
                        <input type="radio" class="form-check-input" name="processType" <c:if test="${approvalSelectionDto.processType eq 'PROTYPE004'}">checked="checked"</c:if> value="PROTYPE004"/>
                        <label class="form-check-label"><span class="check-circle"></span>Special Approval to Handle</label>
                    </div>
                    <div>
                        <span data-err-ind="processType" class="error-msg"></span>
                    </div>
                </div>
            </div>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-3">
                    <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Previous</a>
                </div>
                <div class="col-xs-12 col-md-9">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" id="next" href="javascript:void(0);">NEXT</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>