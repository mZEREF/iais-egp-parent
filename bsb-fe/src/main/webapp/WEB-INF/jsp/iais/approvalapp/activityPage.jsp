<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approval-app.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approval-select.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8" style="margin-top:0px">
            <div class="row">
                <div class="col-xs-12 col-md-10">
                    <div class=" form-group form-horizontal formgap">
                        <div class="col-sm-4 control-label formtext ">
                            <label class="control-label control-set-font control-font-label">Facility Name</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-sm-4 col-md-7 control-font-label">
                            <input type="hidden" id="facilityName" name="facilityName" value="">
                            <iais:select name="facilityId" id="facilityId" disabled="false" options="facilityNameSelect" firstOption="Please Select" value=""></iais:select>
                        </div>
                    </div>
                    <div class=" form-group form-horizontal formgap">
                        <div class="col-sm-4 control-label formtext ">
                            <label class="control-label control-set-font control-font-label">Activity Type</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-sm-4 col-md-7 control-font-label">
                            <iais:select name="activityType" id="activityType" disabled="false" options="activityTypeSelect" firstOption="Please Select" value=""></iais:select>
                        </div>
                    </div>
                    <div class=" form-group form-horizontal formgap">
                        <div class="col-sm-4 control-label formtext ">
                            <label class="control-label control-set-font control-font-label">Schedule</label>
                        </div>
                        <div class="col-sm-4 col-md-7 control-font-label">
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedule1" id="schedule1" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY001')}">checked="checked"</c:if> value="ACTVITY001"/>
                                <label for="schedule1" class="form-check-label"><span class="check-square"></span>First Schedule Part I</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedule2" id="schedule2" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY005')}">checked="checked"</c:if> value="ACTVITY005"/>
                                <label for="schedule2" class="form-check-label"><span class="check-square"></span>First Schedule Part II</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedule3" id="schedule3" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY007')}">checked="checked"</c:if> value="ACTVITY007"/>
                                <label for="schedule3" class="form-check-label"><span class="check-square"></span>Second Schedule</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedule4" id="schedule4" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY009')}">checked="checked"</c:if> value="ACTVITY009"/>
                                <label for="schedule4" class="form-check-label"><span class="check-square"></span>Third Schedule</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedule5" id="schedule5" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY009')}">checked="checked"</c:if> value="ACTVITY009"/>
                                <label for="schedule5" class="form-check-label"><span class="check-square"></span>Fourth Schedule</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedule6" id="schedule6" <c:if test="${serviceSelection.facClassification eq 'FACCLA001' and serviceSelection.activityTypes.contains('ACTVITY009')}">checked="checked"</c:if> value="ACTVITY009"/>
                                <label for="schedule6" class="form-check-label"><span class="check-square"></span>Fifth Schedule</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%@ include file="InnerFooter.jsp" %>
        </div>
    </div>
</form>