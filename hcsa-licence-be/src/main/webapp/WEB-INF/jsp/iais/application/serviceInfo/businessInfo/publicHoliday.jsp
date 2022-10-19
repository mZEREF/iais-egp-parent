<c:set var="phList" value="${businessDto.phDtoList}"/>

<div class="pubHolDayContent">

    <c:choose>
        <c:when test="${businessDto.phDtoList != null && businessDto.phDtoList.size()>1}">
            <input class="phLength" type="hidden" name="phLength${status.index}" value="${businessDto.phDtoList.size()}"/>
            <c:set var="phLength" value="${businessDto.phDtoList.size()}"/>
        </c:when>
        <c:otherwise>
            <input class="phLength" type="hidden" name="phLength${status.index}" value="1"/>
            <c:set var="phLength" value="1"/>
        </c:otherwise>
    </c:choose>

    <iais:row>
        <div class="col-md-12">
            <label class="control-label">Public Holiday</label>
        </div>
    </iais:row>

    <c:forEach begin="0" end="${phLength-1}" step="1" varStatus="phyStat">
        <c:set var="index" value="${phyStat.index}"/>
        <c:set var="ph" value="${phList[index]}"/>
        <div class="pubHolidayDiv">
            <iais:row>
                <div>
                    <div class="col-md-4 multi-sel-padding">
                        <div class="row d-flex">
                            <div class="col-md-12 multi-select col-xs-12">
                                <iais:select cssClass="onSitePubHoliday" name="onSitePubHoliday${status.index}${index}" multiValues="${ph.selectValList}" codeCategory="CATE_ID_PUBLIC_HOLIDAY" needErrorSpan="false" multiSelect="true"/>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_onSitePubHoliday${status.index}${index}"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 col-xs-3 start-div">
                        <div class="row d-flex">
                            <div class="col-sm-12 visible-xs visible-sm">
                                <label class="control-label">Start</label>
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="PhStartHH" name="onSitePhStartHH${status.index}${index}" options="premiseHours" value="${ph.startFromHH}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (HH)
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="PhStartMM" name="onSitePhStartMM${status.index}${index}" options="premiseMinute" value="${ph.startFromMM}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (MM)
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_onSitePhStart${status.index}${index}"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 col-xs-3 end-div">
                        <div class="row d-flex">
                            <div class="col-sm-12 visible-xs visible-sm">
                                <label class="control-label">End</label>
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="PhEndHH" name="onSitePhEndHH${status.index}${index}" options="premiseHours" value="${ph.endToHH}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (HH)
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="PhEndMM" name="onSitePhEndMM${status.index}${index}" options="premiseMinute" value="${ph.endToMM}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (MM)
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_onSitePhEnd${status.index}${index}"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2 col-xs-2 all-day-div">
                        <div class="row d-flex">
                            <div class="col-sm-12 visible-xs visible-sm">
                                <label class="control-label">24 Hours</label>
                            </div>
                            <div class="col-md-5 col-xs-5 text-center all-day-position">
                                <input class="form-check-input allDay" name="onSitePhAllDay${status.index}${index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${ph.selectAllDay}">checked="checked"</c:if> >
                            </div>
                            <div class="col-md-4 col-xs-4 delpubHolidayDiv <c:if test="${index == 0}">hidden</c:if>">
                                <div class="fa fa-times-circle del-size-36 text-danger pubHolidayDel"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 col-xs-4">
                    </div>
                    <div class="col-md-8 col-xs-8">
                        <span class="error-msg " name="iaisErrorMsg" id="error_onSitePhTime${status.index}${index}"></span>
                    </div>
                </div>
            </iais:row>
        </div>
    </c:forEach>

    <div class="form-group addPhDiv <c:if test="${phList.size() >= maxCount}">hidden</c:if>">
        <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
            <a class="addPubHolDay" style="text-decoration:none;">+ Add</a>
        </iais:value>
    </div>
</div>