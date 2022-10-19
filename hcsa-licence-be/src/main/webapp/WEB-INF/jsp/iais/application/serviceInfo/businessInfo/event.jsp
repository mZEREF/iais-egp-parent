<c:set var="eventList" value="${businessDto.eventDtoList}"/>

<div class="eventContent">

    <c:choose>
        <c:when test="${businessDto.eventDtoList != null && businessDto.eventDtoList.size()>1}">
            <input class="eventLength" type="hidden" name="eventLength${status.index}" value="${businessDto.eventDtoList.size()}"/>
            <c:set var="eventLength" value="${businessDto.eventDtoList.size()}"/>
        </c:when>
        <c:otherwise>
            <input class="eventLength" type="hidden" name="eventLength${status.index}" value="1"/>
            <c:set var="eventLength" value="1"/>
        </c:otherwise>
    </c:choose>

    <iais:row>
        <div class="col-md-12 col-xs-12">
            <label class="control-label">Event</label>
        </div>
    </iais:row>

    <c:forEach begin="0" end="${eventLength-1}" step="1" varStatus="eventStat">
        <c:set var="index" value="${eventStat.index}"/>
        <c:set var="event" value="${eventList[index]}"/>
        <div class="eventDiv">
            <iais:row>
                <div>
                    <div class="col-md-4 col-xs-4">
                        <div class="row">
                            <div class="col-md-12 col-xs-12">
                                <iais:input type="text" maxLength="100" cssClass="Event" name="onSiteEvent${status.index}${index}" value="${event.eventName}" />
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEvent${status.index}${index}"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 col-xs-3">
                        <iais:datePicker cssClass="EventStart" name="onSiteEventStart${status.index}${index}" value="${event.startDateStr}" />
                        <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEventStart${status.index}${index}"></span>
                    </div>
                    <div class="col-md-3 col-xs-3">
                        <iais:datePicker cssClass="EventEnd" name="onSiteEventEnd${status.index}${index}" value="${event.endDateStr}" />
                        <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEventEnd${status.index}${index}"></span>
                    </div>
                    <div class="col-md-2 col-xs-2">
                        <div class="row">
                            <div class="col-md-6 text-center col-xs-6">
                            </div>
                            <div class="col-md-6 col-xs-6 eventDelDiv <c:if test="${index == 0}">hidden</c:if>">
                                    <div class="fa fa-times-circle del-size-36 text-danger eventDel"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-8 col-xs-8">
                        <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEventDate${status.index}${index}"></span>
                    </div>
                </div>
            </iais:row>
        </div>
    </c:forEach>

    <div class="form-group addEventDiv <c:if test="${eventList.size() >= maxCount}">hidden</c:if>">
        <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
            <a class="addEvent" style="text-decoration:none;">+ Add Event</a>
        </iais:value>
    </div>
</div>