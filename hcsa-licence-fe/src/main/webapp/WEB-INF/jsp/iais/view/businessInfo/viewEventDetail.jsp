<c:choose>
    <c:when test="${businessDto.eventDtoList.size()>0}">
        <c:set var="eventSize" value="${businessDto.eventDtoList.size()-1}"/>
    </c:when>
    <c:otherwise>
        <c:set var="eventSize" value="0"/>
    </c:otherwise>
</c:choose>
<c:forEach begin="0" end="${eventSize}" step="1" varStatus="stat">
    <c:set var="eventDto" value="${businessDto.eventDtoList[stat.index]}"/>
    <c:if test="${stat.first}">
        <div class="row">
            <div class="col-md-12 hidden-xs">
                <p class="form-check-label" aria-label="premise-1-cytology"><strong>Event</strong></p>
            </div>
        </div>
    </c:if>

    <div class="amend-preview-info form-horizontal min-row">
        <iais:row>
            <div class="col-xs-12 visible-xs">
                <p class="form-check-label" aria-label="premise-1-cytology"><strong>Event</strong></p>
            </div>
            <div class="col-md-4 col-xs-4">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span>
                        <c:out value="${eventDto.eventName}"/>
                    </span>
                </p>
            </div>
            <div class="col-md-8 col-xs-8">
                    <div class="col-xs-12 visible-xs time-padding-left">
                        <label class="control-label"><strong>Start</strong></label>
                    </div>
                    <div class="col-md-4 col-xs-4 time-padding-left" style="padding-left: 0;">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.startDateStr}"/></span></p>
                    </div>
                    <div class="col-xs-12 visible-xs time-padding-left">
                        <label class="control-label"><strong>End</strong></label>
                    </div>
                    <div class="col-md-4 col-xs-4 time-padding-left">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.endDateStr}"/></span></p>
                    </div>
                    <div class="col-md-3">
                    </div>
            </div>
        </iais:row>
    </div>
</c:forEach>