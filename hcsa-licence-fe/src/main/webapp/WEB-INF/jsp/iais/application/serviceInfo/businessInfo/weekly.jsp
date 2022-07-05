
<c:set var="weeklyList" value="${appGrpPremisesDto.weeklyDtoList}"/>
<div class="weeklyContent">
    <c:choose>
        <c:when test="${weeklyList.size()>0}">
            <c:forEach begin="0" end="${weeklyList.size()-1}" step="1" varStatus="weeklyStat">
                <c:set var="weekly" value="${weeklyList[weeklyStat.index]}"/>
                <iais:row cssClass="weeklyDiv">
                    <div class="col-md-12 col-xs-12 ${weeklyStat.index>0 ? '' : 'hidden'}">
                        <label class="control-label">Weekly <span class="mandatory">*</span></label>
                    </div>
                    <div>
                        <div class="col-md-4 col-xs-4 multi-sel-padding">
                            <div class="row d-flex">
                                <div class="col-xs-12 visible-xs visible-sm">
                                    <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                </div>
                                <div class="col-md-12 multi-select col-xs-12">
                                    <iais:select cssClass="weekly" name="${premValue}Weekly${weeklyStat.index}" multiValues="${weekly.selectValList}" options="weeklyOpList"  multiSelect="true"/>
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_Weekly${status.index}${weeklyStat.index}"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-xs-3 start-div">
                            <div class="row d-flex">
                                <div class="col-sm-12 visible-xs visible-sm">
                                    <label class="control-label">Start</label>
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="WeeklyStartHH" name="${premValue}WeeklyStartHH${weeklyStat.index}" options="premiseHours" value="${weekly.startFromHH}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (HH)
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="WeeklyStartMM" name="${premValue}WeeklyStartMM${weeklyStat.index}" options="premiseMinute" value="${weekly.startFromMM}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (MM)
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_WeeklyStart${status.index}${weeklyStat.index}"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-xs-3 end-div">
                            <div class="row d-flex">
                                <div class="col-xs-12 visible-xs visible-sm">
                                    <label class="control-label">End</label>
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding" >
                                    <iais:select cssClass="WeeklyEndHH" name="${premValue}WeeklyEndHH${weeklyStat.index}" options="premiseHours" value="${weekly.endToHH}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (HH)
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="WeeklyEndMM" name="${premValue}WeeklyEndMM${weeklyStat.index}" options="premiseMinute" value="${weekly.endToMM}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (MM)
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_WeeklyEnd${status.index}${weeklyStat.index}"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2 col-xs-2 all-day-div">
                            <div class="row d-flex">
                                <div class="col-xs-12 visible-xs visible-sm">
                                    <label class="control-label">24 Hours</label>
                                </div>
                                <div class="col-md-5 text-center col-xs-5 all-day-position">
                                    <input class="form-check-input allDay" name="${premValue}WeeklyAllDay${weeklyStat.index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${weekly.selectAllDay}">checked="checked"</c:if> >
                                </div>
                                <div class="col-md-5 col-xs-5">
                                    <c:if test="${weeklyStat.index>0}">
                                        <div class="fa fa-times-circle del-size-36 text-danger weeklyDel"></div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 col-xs-4">
                        </div>
                        <div class="col-md-8 col-xs-8">
                            <span class="error-msg " name="iaisErrorMsg" id="error_WeeklyTime${status.index}${weeklyStat.index}"></span>
                        </div>
                    </div>
                </iais:row>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:set var="suffix" value="0"/>
            <iais:row cssClass="weeklyDiv">
                <div>
                    <div class="col-md-4 col-xs-4 multi-sel-padding">
                        <div class="row d-flex">
                            <div class="col-xs-12 visible-xs visible-sm">
                                <label class="control-label">Weekly <span class="mandatory">*</span></label>
                            </div>
                            <div class="col-md-12 multi-select col-xs-12">
                                <iais:select name="${premValue}Weekly${suffix}"  options="weeklyOpList" multiSelect="true" multiValues="" />
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_Weekly${status.index}${suffix}"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 col-xs-3 start-div">
                        <div class="row d-flex">
                            <div class="col-sm-12 visible-xs visible-sm">
                                <label class="control-label">Start</label>
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="WeeklyStartHH" name="${premValue}WeeklyStartHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (HH)
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="WeeklyStartMM" name="${premValue}WeeklyStartMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (MM)
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_WeeklyStart${status.index}${suffix}"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 col-xs-3 end-div">
                        <div class="row d-flex">
                            <div class="col-sm-12 visible-xs visible-sm">
                                <label class="control-label">End</label>
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="WeeklyEndHH" name="${premValue}WeeklyEndHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (HH)
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="WeeklyEndMM" name="${premValue}WeeklyEndMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (MM)
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_WeeklyEnd${status.index}${suffix}"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2 col-xs-2 all-day-div">
                        <div class="row d-flex">
                            <div class="col-xs-12 visible-xs visible-sm">
                                <label class="control-label">24 Hours</label>
                            </div>
                            <div class="col-md-5 col-xs-5 text-center all-day-position">
                                <input class="form-check-input allDay" name="${premValue}WeeklyAllDay${suffix}"  type="checkbox" aria-invalid="false" value="true"  >
                            </div>
                            <div class="col-md-5 col-xs-5">
                            </div>
                        </div>
                    </div>
                </div>
            </iais:row>
        </c:otherwise>
    </c:choose>

    <div class="form-group addWeeklyDiv <c:if test="${weeklyList.size() >= weeklyCount}">hidden</c:if>">
        <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
            <a class="addWeekly" style="text-decoration:none;">+ Add</a>
        </iais:value>
        <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

        </iais:value>
    </div>
</div>
