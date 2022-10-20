<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>

<c:set var="isSpecialService" value="${serviceCode==AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL||serviceCode==AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}"/>

<c:if test="${!isSpecialService}">
    <iais:row>
        <div class="col-md-12 col-xs-12">
            <label class="control-label"><strong>Operating Hours</strong></label>
        </div>
        <div class="col-md-4 col-xs-4  hidden-xs hidden-sm">
            <label class="control-label"><strong>Weekly </strong><span class="mandatory">*</span></label>
        </div>
        <div class="col-md-3 col-xs-3 input-padding hidden-xs hidden-sm">
            <label class="control-label"><strong>Start</strong></label>
        </div>
        <div class="col-md-3 col-xs-3 input-padding hidden-xs hidden-sm">
            <label class="control-label"><strong>End</strong></label>
        </div>
        <div class="col-md-2 col-xs-2 hidden-xs hidden-sm">
            <label class="control-label"><strong>24 Hours</strong></label>
        </div>
    </iais:row>

    <%@include file="weekly.jsp"%>
    <%@include file="publicHoliday.jsp"%>
    <%@include file="event.jsp"%>
</c:if>


