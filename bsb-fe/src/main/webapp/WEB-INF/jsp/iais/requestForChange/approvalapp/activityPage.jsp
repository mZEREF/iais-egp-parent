<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfc-approval-app.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../common/dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8" style="margin-top:0px">
            <div class="row">
                <div class="col-xs-12 col-md-10">
                    <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                    <div class="form-group form-horizontal formgap">
                        <div class="col-sm-4 control-label formtext ">
                            <label class="control-label control-set-font control-font-label">Facility Name</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-sm-4 col-md-7 control-font-label">
                            <input type="hidden" id="facilityName" name="facilityName" value="${activity.facilityName}">
                            <select name="facilityId" id="facilityId">
                                <c:forEach items="${facilityIdSelect}" var="facSelect">
                                    <option value="${MaskUtil.maskValue('facilityId',facSelect.value)}" <c:if test="${activity.facilityId eq facSelect.value}">selected="selected"</c:if>>${facSelect.text}</option>
                                </c:forEach>
                            </select>
                            <span data-err-ind="facilityId" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group form-horizontal formgap">
                        <div class="col-sm-4 control-label formtext ">
                            <label class="control-label control-set-font control-font-label">Activity Type</label>
                            <span class="mandatory">*</span>
                        </div>
                        <div class="col-sm-4 col-md-7 control-font-label">
                            <input type="hidden" id="activityType" name="activityType" value="${activity.activityType}">
                            <select name="activityId" id="activityId">
                                <c:forEach items="${activityIdSelectDto}" var="selectDto">
                                    <c:forEach items="${selectDto.activityIdList}" var="selectList">
                                        <option value="${MaskUtil.maskValue('activityId',selectList.value)}" <c:if test="${activity.activityId eq selectList.value}">selected="selected"</c:if>><iais:code code="${selectList.text}"></iais:code></option>
                                    </c:forEach>
                                </c:forEach>
                            </select>
                            <span data-err-ind="activityType" class="error-msg"></span>
                        </div>
                    </div>
                    <div class=" form-group form-horizontal formgap">
                        <div class="col-sm-4 control-label formtext ">
                            <label class="control-label control-set-font control-font-label">Schedule</label>
                        </div>
                        <div class="col-sm-4 col-md-7 control-font-label">
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedules" id="schedule1" <c:if test="${activity.schedules.contains('SCHTYPE001')}">checked="checked"</c:if> value="SCHTYPE001"/>
                                <label for="schedule1" class="form-check-label"><span class="check-square"></span>First Schedule Part I</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedules" id="schedule2" <c:if test="${activity.schedules.contains('SCHTYPE002')}">checked="checked"</c:if> value="SCHTYPE002"/>
                                <label for="schedule2" class="form-check-label"><span class="check-square"></span>First Schedule Part II</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedules" id="schedule3" <c:if test="${activity.schedules.contains('SCHTYPE003')}">checked="checked"</c:if> value="SCHTYPE003"/>
                                <label for="schedule3" class="form-check-label"><span class="check-square"></span>Second Schedule</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedules" id="schedule4" <c:if test="${activity.schedules.contains('SCHTYPE004')}">checked="checked"</c:if> value="SCHTYPE004"/>
                                <label for="schedule4" class="form-check-label"><span class="check-square"></span>Third Schedule</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedules" id="schedule5" <c:if test="${activity.schedules.contains('SCHTYPE005')}">checked="checked"</c:if> value="SCHTYPE005"/>
                                <label for="schedule5" class="form-check-label"><span class="check-square"></span>Fourth Schedule</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" name="schedules" id="schedule6" <c:if test="${activity.schedules.contains('SCHTYPE006')}">checked="checked"</c:if> value="SCHTYPE006"/>
                                <label for="schedule6" class="form-check-label"><span class="check-square"></span>Fifth Schedule</label>
                            </div>
                            <div>
                                <span data-err-ind="schedules" class="error-msg"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%@ include file="../common/InnerFooter.jsp" %>
        </div>
    </div>
</form>