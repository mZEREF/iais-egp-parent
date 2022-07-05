<c:set var="phList" value="${appGrpPremisesDto.phDtoList}"/>
<div class="pubHolDayContent">
    <c:choose>
        <c:when test="${phList.size()>0 && 'ONSITE' == appGrpPremisesDto.premisesType}">
            <c:forEach begin="0" end="${phList.size()-1}" step="1" varStatus="phyStat">
                <c:set var="ph" value="${phList[phyStat.index]}"/>
                <iais:row cssClass="pubHolidayDiv">
                    <div class="col-md-12">
                        <label class="control-label">Public Holiday</label>
                    </div>
                    <div>
                        <div class="col-md-4 multi-sel-padding">
                            <div class="row d-flex">
                                <div class="col-md-12 multi-select col-xs-12">
                                    <iais:select name="${premValue}onSitePubHoliday${phyStat.index}" multiValues="${ph.selectValList}" options="phOpList" multiSelect="true"/>
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_onSitePubHoliday${status.index}${phyStat.index}"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-xs-3 start-div">
                            <div class="row d-flex">
                                <div class="col-sm-12 visible-xs visible-sm">
                                    <label class="control-label">Start</label>
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="PhStartHH" name="${premValue}onSitePhStartHH${phyStat.index}" options="premiseHours" value="${ph.startFromHH}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (HH)
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="PhStartMM" name="${premValue}onSitePhStartMM${phyStat.index}" options="premiseMinute" value="${ph.startFromMM}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (MM)
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_onSitePhStart${status.index}${phyStat.index}"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-xs-3 end-div">
                            <div class="row d-flex">
                                <div class="col-sm-12 visible-xs visible-sm">
                                    <label class="control-label">End</label>
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="PhEndHH" name="${premValue}onSitePhEndHH${phyStat.index}" options="premiseHours" value="${ph.endToHH}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (HH)
                                </div>
                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                    <iais:select cssClass="PhEndMM" name="${premValue}onSitePhEndMM${phyStat.index}" options="premiseMinute" value="${ph.endToMM}" firstOption="--"></iais:select>
                                </div>
                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                    (MM)
                                </div>
                                <div class="col-md-12 col-xs-12">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_onSitePhEnd${status.index}${phyStat.index}"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2 col-xs-2 all-day-div">
                            <div class="row d-flex">
                                <div class="col-sm-12 visible-xs visible-sm">
                                    <label class="control-label">24 Hours</label>
                                </div>
                                <div class="col-md-5 col-xs-5 text-center all-day-position">
                                    <input class="form-check-input allDay" name="${premValue}onSitePhAllDay${phyStat.index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${ph.selectAllDay}">checked="checked"</c:if> >
                                </div>
                                <div class="col-md-5 col-xs-5">
                                    <c:if test="${phyStat.index>0}">
                                        <div class="fa fa-times-circle del-size-36 text-danger pubHolidayDel"></div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 col-xs-4">
                        </div>
                        <div class="col-md-8 col-xs-8">
                            <span class="error-msg " name="iaisErrorMsg" id="error_onSitePhTime${status.index}${phyStat.index}"></span>
                        </div>
                    </div>
                </iais:row>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:set var="suffix" value="0"/>
            <iais:row cssClass="pubHolidayDiv">
                <div class="col-md-12 col-xs-12">
                    <label class="control-label">Public Holiday</label>
                </div>
                <div>
                    <div class="col-md-4 col-xs-4 multi-sel-padding">
                        <div class="row d-flex">
                            <div class="col-md-12 multi-select col-xs-12">
                                <iais:select name="${premValue}onSitePubHoliday${suffix}" options="phOpList"  multiSelect="true"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 col-xs-3 start-div">
                        <div class="row d-flex">
                            <div class="col-sm-12 visible-xs visible-sm">
                                <label class="control-label">Start</label>
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="PhStartHH" name="${premValue}onSitePhStartHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (HH)
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="PhStartMM" name="${premValue}onSitePhStartMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (MM)
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 col-xs-3 end-div">
                        <div class="row d-flex">
                            <div class="col-sm-12 visible-xs visible-sm">
                                <label class="control-label">End</label>
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="PhEndHH" name="${premValue}onSitePhEndHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (HH)
                            </div>
                            <div class="col-md-10 col-lg-5 col-9 input-padding">
                                <iais:select cssClass="PhEndMM" name="${premValue}onSitePhEndMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-2 col-lg-1 col-3 label-padding">
                                (MM)
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2 col-xs-2 all-day-div">
                        <div class="row d-flex">
                            <div class="col-sm-12 visible-xs visible-sm">
                                <label class="control-label">24 Hours</label>
                            </div>
                            <div class="col-md-5 col-xs-5 text-center all-day-position">
                                <input class="form-check-input allDay" name="${premValue}onSitePhAllDay${suffix}"  type="checkbox" aria-invalid="false" value="true" >
                            </div>
                            <div class="col-md-5 col-xs-5">
                            </div>
                        </div>
                    </div>
                </div>
            </iais:row>
        </c:otherwise>
    </c:choose>

    <div class="form-group addPhDiv <c:if test="${phList.size() >= phCount}">hidden</c:if>">
        <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
            <a class="addPubHolDay" style="text-decoration:none;">+ Add</a>
        </iais:value>
        <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

        </iais:value>
    </div>
</div>
