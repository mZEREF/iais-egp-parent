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
            <div class="col-md-12">
                <p class="form-check-label" aria-label="premise-1-cytology"><span>Event</span></p>
            </div>
        </div>
    </c:if>

    <div class="row">
        <div class="col-md-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span>
                    <c:out value="${eventDto.eventName}"/>
                </span>
            </p>
        </div>
        <div class="col-md-6">
            <div class="row">
                <div class="col-md-4">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.startDateStr}"/></span></p>
                </div>
                <div class="col-md-4">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.endDateStr}"/></span></p>
                </div>
                <div class="col-md-3">
                </div>
            </div>
        </div>
    </div>
</c:forEach>