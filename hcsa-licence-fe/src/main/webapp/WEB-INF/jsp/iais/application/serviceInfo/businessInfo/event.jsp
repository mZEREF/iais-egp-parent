<c:set var="eventList" value="${appGrpPremisesDto.eventDtoList}"/>
<div class="eventContent">
    <c:choose>
        <c:when test="${eventList.size()>0 && 'ONSITE' == appGrpPremisesDto.premisesType}">
            <c:forEach begin="0" end="${eventList.size()-1}" step="1" varStatus="eventStat">
                <c:set var="event" value="${eventList[eventStat.index]}"/>
                <iais:row cssClass="eventDiv">
                    <div class="col-md-12 col-xs-12">
                        <label class="control-label">Event</label>
                    </div>
                    <div>
                        <div class="col-md-4 col-xs-4">
                            <div class="row">
                                <div class="col-md-12 col-xs-12">
                                    <iais:input type="text" maxLength="100" cssClass="Event" name="${premValue}onSiteEvent${eventStat.index}" value="${event.eventName}" />
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEvent${status.index}${eventStat.index}"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-xs-3">
                            <iais:datePicker cssClass="EventStart" name="${premValue}onSiteEventStart${eventStat.index}" value="${event.startDateStr}" />
                            <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEventStart${status.index}${eventStat.index}"></span>
                        </div>
                        <div class="col-md-3 col-xs-3">
                            <iais:datePicker cssClass="EventEnd" name="${premValue}onSiteEventEnd${eventStat.index}" value="${event.endDateStr}" />
                            <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEventEnd${status.index}${eventStat.index}"></span>
                        </div>
                        <div class="col-md-2 col-xs-2">
                            <div class="row">
                                <div class="col-md-6 text-center col-xs-6">
                                </div>
                                <div class="col-md-6 col-xs-6">
                                    <c:if test="${eventStat.index>0}">
                                        <div class="fa fa-times-circle del-size-36 text-danger eventDel"></div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="">
                        </div>
                        <div class="col-md-8 col-xs-8">
                            <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEventDate${status.index}${eventStat.index}"></span>
                        </div>
                    </div>
                </iais:row>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:set var="suffix" value="0"/>
            <iais:row cssClass="eventDiv">
                <div class="col-md-12 col-xs-12">
                    <label class="control-label">Event</label>
                </div>
                <div>
                    <div class="col-md-4 col-xs-4">
                        <div class="row">
                            <div class="col-md-12 col-xs-12">
                                <iais:input maxLength="100" type="text" cssClass="Event" name="${premValue}onSiteEvent${suffix}" value="" />
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 col-xs-3">
                        <iais:datePicker cssClass="EventStart" name="${premValue}onSiteEventStart${suffix}" value="" />
                    </div>
                    <div class="col-md-3 col-xs-3">
                        <iais:datePicker cssClass="EventEnd" name="${premValue}onSiteEventEnd${suffix}" value="" />
                    </div>
                    <div class="col-md-2 col-xs-2">
                        <div class="row">
                            <div class="col-md-6 text-center col-xs-6">
                            </div>
                            <div class="col-md-6 col-xs-6">
                                <c:if test="${eventStat.index>0}">
                                    <div class="fa fa-times-circle del-size-36 text-danger eventDel"></div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </iais:row>
        </c:otherwise>
    </c:choose>

    <div class="form-group addEventDiv <c:if test="${eventList.size() >= eventCount}">hidden</c:if>">
        <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
            <a class="addEvent" style="text-decoration:none;">+ Add</a>
        </iais:value>
        <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

        </iais:value>
    </div>
</div>
