<c:forEach var="eventDto" items="${businessDto.eventDtoList}" varStatus="stat">
    <c:set var="oldeventDto" value="${oldBusiness.eventDtoList[stat.index]}"/>
    <c:if test="${stat.first}">
        <tr>
            <div class="col-md-12">
                <p class="form-check-label" aria-label="premise-1-cytology"><span>Event</span></p>
            </div>
        </tr>
    </c:if>

    <tr>
        <td class="col-md-6 col-sm-6 col-xs-6">
            <div class="col-md-6 col-sm-6 col-xs-6">
                <div class="newVal" attr="${eventDto.eventName}">
                    <c:out value="${eventDto.eventName}"/>
                </div>
            </div>
            <div class="col-md-6 col-sm-6 col-xs-6">
                <div class="oldVal" style="display: none" attr="${oldeventDto.eventName}">
                    <c:out value="${oldeventDto.eventName}"/>
                </div>
            </div>
        </td>
        <td class="col-md-6 col-sm-6 col-xs-6">
            <div class="col-md-4 col-sm-4 col-xs-4">
                <div class="col-md-6 col-sm-6 col-xs-6">
                    <div class="newVal" attr="${eventDto.startDateStr}">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.startDateStr}"/></span></p>
                    </div>
                </div>
                <div class="col-md-6 col-sm-6 col-xs-6">
                    <div class="oldVal" style="display: none" attr="${oldeventDto.startDateStr}">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${oldeventDto.startDateStr}"/></span></p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 col-sm-4 col-xs-4">
                <div class="col-md-6 col-sm-6 col-xs-6">
                    <div class="newVal" attr="${eventDto.endDateStr}">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.endDateStr}"/></span></p>
                    </div>
                </div>
                <div class="col-md-6 col-sm-6 col-xs-6">
                    <div class="oldVal" style="display: none" attr="${oldeventDto.endDateStr}">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${oldeventDto.endDateStr}"/></span></p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
            </div>
        </td>
    </tr>
</c:forEach>