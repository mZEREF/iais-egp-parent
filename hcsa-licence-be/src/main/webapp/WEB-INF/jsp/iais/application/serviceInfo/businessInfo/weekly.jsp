<c:set var="weeklyList" value="${businessDto.weeklyDtoList}"/>

<div class="weeklyContent">
    <c:choose>
        <c:when test="${businessDto.weeklyDtoList != null && businessDto.weeklyDtoList.size()>1}">
            <input class="weeklyLength" type="hidden" name="weeklyLength${status.index}" value="${businessDto.weeklyDtoList.size()}"/>
            <c:set var="weeklyLength" value="${businessDto.weeklyDtoList.size()}"/>
        </c:when>
        <c:otherwise>
            <input class="weeklyLength" type="hidden" name="weeklyLength${status.index}" value="1"/>
            <c:set var="weeklyLength" value="1"/>
        </c:otherwise>
    </c:choose>
    <c:forEach begin="0" end="${weeklyLength-1}" step="1" varStatus="weeklyStat">
        <c:set var="index" value="${weeklyStat.index}"/>
        <c:set var="weekly" value="${weeklyList[index]}"/>
        <div class="weeklyDiv">
            <iais:row>
                <div>
                    <div class="col-md-3 col-xs-3 multi-sel-padding">
                        <div class="row d-flex">
                            <div class="col-xs-12 visible-xs visible-sm">
                                <label class="control-label">Weekly <span class="mandatory">*</span></label>
                            </div>
                            <div class="col-md-12 multi-select col-xs-12">
                                <iais:select cssClass="onSiteWeekly" name="${status.index}onSiteWeekly${index}" multiValues="${weekly.selectValList}" codeCategory="CATE_ID_DAY_NAMES" needErrorSpan="false" multiSelect="true"/>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_${status.index}onSiteWeekly${index}"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-9 col-xs-9">
                        <div class="col-md-5 col-xs-5 start-div">
                            <div class="row d-flex">
                                <div class="col-sm-12 visible-xs visible-sm">
                                    <label class="control-label">Start</label>
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="WeeklyStartHH" name="${status.index}onSiteWeeklyStartHH${index}" options="premiseHours" value="${weekly.startFromHH}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (HH)
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="WeeklyStartMM" name="${status.index}onSiteWeeklyStartMM${index}" options="premiseMinute" value="${weekly.startFromMM}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (MM)
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_${status.index}onSiteWeeklyStart${index}"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-5 col-xs-5 end-div">
                            <div class="row d-flex">
                                <div class="col-xs-12 visible-xs visible-sm">
                                    <label class="control-label">End</label>
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding" >
                                    <iais:select cssClass="WeeklyEndHH" name="${status.index}onSiteWeeklyEndHH${index}" options="premiseHours" value="${weekly.endToHH}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (HH)
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="WeeklyEndMM" name="${status.index}onSiteWeeklyEndMM${index}" options="premiseMinute" value="${weekly.endToMM}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (MM)
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_${status.index}onSiteWeeklyEnd${index}"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2 col-xs-2 all-day-div">
                            <div class="row d-flex">
                                <div class="col-xs-12 visible-xs visible-sm">
                                    <label class="control-label">24 Hours</label>
                                </div>
                                <div class="col-md-5 text-center col-xs-5 all-day-position">
                                    <input class="form-check-input allDay" name="${status.index}onSiteWeeklyAllDay${index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${weekly.selectAllDay}">checked="checked"</c:if> >
                                </div>
                                <div class="col-md-4 col-xs-4 weeklyDelDiv <c:if test="${index == 0}">hidden</c:if>">
                                    <div class="fa fa-times-circle del-size-36 text-danger weeklyDel"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 col-xs-4">
                    </div>
                    <div class="col-md-8 col-xs-8">
                        <span class="error-msg " name="iaisErrorMsg" id="error_${status.index}onSiteWeeklyTime${index}"></span>
                    </div>
                </div>
            </iais:row>
        </div>
    </c:forEach>
    <div class="form-group addWeeklyDiv <c:if test="${weeklyList.size() >= maxCount}">hidden</c:if>">
        <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
            <a class="addWeekly" style="text-decoration:none;">+ Add</a>
        </iais:value>
    </div>
</div>
