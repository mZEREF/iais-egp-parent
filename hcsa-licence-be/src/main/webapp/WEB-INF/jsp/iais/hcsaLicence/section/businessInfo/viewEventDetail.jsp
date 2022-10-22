<c:forEach var="eventDto" items="${businessDto.eventDtoList}" varStatus="stat">
    <c:set var="oldeventDto" value="${oldBusiness.eventDtoList[stat.index]}"/>
    <table  class="col-xs-12" aria-describedby="">
        <thead style="display: none">
        <tr>
            <th scope="col"></th>
        </tr>
        </thead>
        <c:if test="${stat.first}">
            <tr>
                <div class="col-xs-12">
                    <label class="control-label">Event</label>
                </div>
            </tr>
        </c:if>
        <tr>
            <td class="col-xs-4">
                <div class="col-xs-12 row">
                    <div class="newVal" attr="${eventDto.eventName}">
                        <c:out value="${eventDto.eventName}"/>
                    </div>
                </div>
                <div class="col-xs-12 row">
                    <div class="oldVal" style="display: none" attr="${oldeventDto.eventName}">
                        <c:out value="${oldeventDto.eventName}"/>
                    </div>
                </div>
            </td>
            <td>
                <div class="col-xs-4">
                    <div class="col-xs-12 row">
                        <div class="newVal" attr="${eventDto.startDateStr}">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.startDateStr}"/></span></p>
                        </div>
                    </div>
                    <div class="col-xs-12 row">
                        <div class="oldVal" style="display: none" attr="${oldeventDto.startDateStr}">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${oldeventDto.startDateStr}"/></span></p>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="col-xs-12 row">
                        <div class="newVal" attr="${eventDto.endDateStr}">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.endDateStr}"/></span></p>
                        </div>
                    </div>
                    <div class="col-xs-12 row">
                        <div class="oldVal" style="display: none" attr="${oldeventDto.endDateStr}">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${oldeventDto.endDateStr}"/></span></p>
                        </div>
                    </div>
                </div>
                <div class="col-xs-3">
                </div>
            </td>
        </tr>
    </table>
</c:forEach>