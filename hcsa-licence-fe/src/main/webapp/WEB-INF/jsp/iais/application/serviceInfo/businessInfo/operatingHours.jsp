<c:choose>
    <c:when test="${appGrpPremisesDto.phDtoList != null && appGrpPremisesDto.phDtoList.size()>1}">
        <input class="phLength" type="hidden" name="phLength" value="${appGrpPremisesDto.phDtoList.size()}"/>
    </c:when>
    <c:otherwise>
        <input class="phLength" type="hidden" name="phLength" value="1"/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${appGrpPremisesDto.weeklyDtoList != null && appGrpPremisesDto.weeklyDtoList.size()>1}">
        <input class="weeklyLength" type="hidden" name="weeklyLength" value="${appGrpPremisesDto.weeklyDtoList.size()}"/>
    </c:when>
    <c:otherwise>
        <input class="weeklyLength" type="hidden" name="weeklyLength" value="1"/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${appGrpPremisesDto.eventDtoList != null && appGrpPremisesDto.eventDtoList.size()>1}">
        <input class="eventLength" type="hidden" name="eventLength" value="${appGrpPremisesDto.eventDtoList.size()}"/>
    </c:when>
    <c:otherwise>
        <input class="eventLength" type="hidden" name="eventLength" value="1"/>
    </c:otherwise>
</c:choose>

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