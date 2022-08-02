<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>

<c:choose>
    <c:when test="${businessDto.weeklyDtoList != null && businessDto.weeklyDtoList.size()>1}">
        <input class="weeklyLength" type="hidden" name="weeklyLength" value="${businessDto.weeklyDtoList.size()}"/>
    </c:when>
    <c:otherwise>
        <input class="weeklyLength" type="hidden" name="weeklyLength" value="1"/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${businessDto.phDtoList != null && businessDto.phDtoList.size()>1}">
        <input class="phLength" type="hidden" name="phLength" value="${businessDto.phDtoList.size()}"/>
    </c:when>
    <c:otherwise>
        <input class="phLength" type="hidden" name="phLength" value="1"/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${businessDto.eventDtoList != null && businessDto.eventDtoList.size()>1}">
        <input class="eventLength" type="hidden" name="eventLength" value="${businessDto.eventDtoList.size()}"/>
    </c:when>
    <c:otherwise>
        <input class="eventLength" type="hidden" name="eventLength" value="1"/>
    </c:otherwise>
</c:choose>

<c:set var="isSpecialService" value="${serviceCode==AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL||serviceCode==AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}"></c:set>
<input class="isSpecialService" type="hidden" name="isSpecialService" value="${isSpecialService}"/>
<c:if test="${!isSpecialService}">
    <iais:row>
        <div class="col-md-12 col-xs-12">
            <label class="control-label">Operating Hours</label>
        </div>
        <div class="col-md-4 col-xs-4  hidden-xs hidden-sm">
            <label class="control-label">Weekly <span class="mandatory">*</span></label>
        </div>
        <div class="col-md-3 col-xs-3 input-padding hidden-xs hidden-sm">
            <label class="control-label">Start</label>
        </div>
        <div class="col-md-3 col-xs-3 input-padding hidden-xs hidden-sm">
            <label class="control-label">End</label>
        </div>
        <div class="col-md-2 col-xs-2 hidden-xs hidden-sm">
            <label class="control-label">24 Hours</label>
        </div>
    </iais:row>

    <%@include file="weekly.jsp"%>
    <%@include file="publicHoliday.jsp"%>
    <%@include file="event.jsp"%>
</c:if>


