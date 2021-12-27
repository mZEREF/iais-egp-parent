<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title"  >
            <a href="#arStageDetails" data-toggle="collapse">
                Assisted Reproduction Submission
            </a>
        </h4>
    </div>
    <div id="arStageDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="arCycleStageDto" value="${arSuperDataSubmissionDto.arCycleStageDto}" />
                <c:set var="enhancedCounsellingMandatory" value="${(arCycleStageDto.cycleAgeYear > 45 || arCycleStageDto.cycleAgeYear ==45 && arCycleStageDto.cycleAgeMonth>0 ) ? 'true' : 'false'}" />
                <%@include file="patientCommon.jsp"%>
                <iais:row>
                    <iais:field width="5" value="Premises where AR is performed" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date Started" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker id="startDate" name="startDate" value="${arCycleStageDto.startDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age as of This Cycle" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arCycleStageDto.cycleAge}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Main Indication" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="mainIndication"  firstOption="Please Select" codeCategory="AR_MAIN_INDICATION" value="${arCycleStageDto.mainIndication}"  onchange ="toggleOnSelect(this, 'AR_MI_013', 'mainIndicationOtherRow')"/>
                </iais:value>
                </iais:row>

                <iais:row id="mainIndicationOtherRow">
                    <iais:field width="5" value="Main Indication (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" name="mainIndicationOthers" id="mainIndicationOthers" value="${arCycleStageDto.mainIndicationOthers}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Other Indication" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="otherIndication" multiSelect="true"  codeCategory="AR_OTHER_INDICATION"  multiValues="${arCycleStageDto.otherIndicationValues}" onchange="toggleOnCheck('#otherIndication_12','otherIndicationOthersRow')"/>
                    </iais:value>
                </iais:row>

                <iais:row id="otherIndicationOthersRow" style="${StringUtil.stringContain(arCycleStageDto.otherIndication,DataSubmissionConsts.AR_OTHER_INDICATION_OTHERS) ? '' :'display: none;' }">
                    <iais:field width="5" value="Other Indication (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" name="otherIndicationOthers" id="otherIndicationOthers" value="${arCycleStageDto.otherIndicationOthers}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="In-Vitro Maturation" mandatory="false"/>
                    <iais:value width="3" cssClass="col-md-3" >
                        <div class="form-check" style="padding-left: 0px;" >
                            <input class="form-check-input"
                                   type="radio"
                                   name="inVitroMaturation"
                                   value="1"
                                   id="inVitroMaturationRadioYes"
                                   <c:if test="${arCycleStageDto.inVitroMaturation}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="inVitroMaturationRadioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" >
                        <div class="form-check" style="padding-left: 0px;">
                            <input class="form-check-input" type="radio"
                                   name="inVitroMaturation"
                                   value="0"
                                   id="inVitroMaturationRadioNo"
                                   <c:if test="${!arCycleStageDto.inVitroMaturation}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="inVitroMaturationRadioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Current AR Treatment" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                            <c:forEach items="${currentArTreatments}" var="currentArTreatment">
                                <c:set var="currentArTreatmentCode" value="${currentArTreatment.code}"/>
                                <div class="form-check col-xs-7"  style="padding-left: 0px;">
                                    <input class="form-check-input" type="checkbox"
                                           name="currentArTreatment"
                                           value="${currentArTreatmentCode}"
                                           id="currentArTreatmentCheck${currentArTreatmentCode}"
                                           <c:if test="${StringUtil.stringContain(arCycleStageDto.currentArTreatment,currentArTreatmentCode)}">checked</c:if>
                                           aria-invalid="false"    <c:if test="${currentArTreatmentCode eq DataSubmissionConsts.CURRENT_AR_TREATMENT_FRESH_CYCLE_NATURAL
                                           || currentArTreatmentCode eq DataSubmissionConsts.CURRENT_AR_TREATMENT_FRESH_CYCLE_STIMULATED}">
                                            onchange="doInactiveCurrentArTreatment('${currentArTreatmentCode}')"
                                    </c:if> >
                                    <label class="form-check-label"
                                           for="currentArTreatmentCheck${currentArTreatmentCode}"><span
                                            class="check-square"></span>
                                        <c:out value="${currentArTreatment.codeValue}"/></label>
                                </div>
                            </c:forEach>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <span id="error_currentArTreatment" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children from Current Marriage" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="currentMarriageChildren" options="noChildrenDropDown" firstOption="Please Select" value="${arCycleStageDto.currentMarriageChildren}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children from Previous Marriage" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="previousMarriageChildren" options="noChildrenDropDown" firstOption="Please Select" value="${arCycleStageDto.previousMarriageChildren}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children conceived through AR" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="deliveredThroughChildren" options="noChildrenDropDown" firstOption="Please Select" value="${arCycleStageDto.deliveredThroughChildren}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <label class="col-xs-4 col-md-4 control-label">Total No. of AR cycles previously undergone by patient <span class="mandatory">*</span>
                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="${DSACK002Message}"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="totalPreviouslyPreviously" options="numberArcPreviouslyDropDown" firstOption="Please Select" value="${arCycleStageDto.totalPreviouslyPreviously}"  onchange ="toggleOnSelect(this, '21', 'totalNumberARCOtherRow')"/>
                        </iais:value>
                </iais:row>

                <iais:row id="totalNumberARCOtherRow">
                    <iais:field width="5" value="No. of AR Cycles undergone Overseas" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="cyclesUndergoneOverseas" options="cyclesUndergoneOverseasDropDown" firstOption="Please Select" value="${arCycleStageDto.cyclesUndergoneOverseas}"  onchange="doEnhancedCounsellingMandatory('${enhancedCounsellingMandatory}')"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of AR Cycles undergone Locally" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" >
                           <c:out value="${arCycleStageDto.numberOfCyclesUndergoneLocally}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Enhanced Counselling" id="enhancedCounsellingTitle" mandatory="${enhancedCounsellingMandatory == 'true' ? enhancedCounsellingMandatory :
                      (!empty arCycleStageDto.cyclesUndergoneOverseas && arCycleStageDto.cyclesUndergoneOverseas >10  ? 'true' : 'false')}"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check" style="padding-left: 0px;">
                            <input class="form-check-input"
                                   type="radio"
                                   name="enhancedCounselling"
                                   value="1"
                                   id="enhancedCounsellingRadioYes"
                                   <c:if test="${arCycleStageDto.enhancedCounselling}">checked</c:if>
                                   aria-invalid="false" >
                            <label class="form-check-label"
                                   for="enhancedCounsellingRadioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check" style="padding-left: 0px;">
                            <input class="form-check-input" type="radio"
                                   name="enhancedCounselling"
                                   value="0"
                                   id="enhancedCounsellingRadioNo"
                                   <c:if test="${arCycleStageDto.enhancedCounselling != null && !arCycleStageDto.enhancedCounselling}">checked</c:if>
                                   aria-invalid="false" <c:if test="${enhancedCounsellingNoShow == 1}"> onchange="showEnhancedCounsellingTipNo()" </c:if>>
                            <label class="form-check-label"
                                   for="enhancedCounsellingRadioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4"/>
                    <iais:value width="3" cssClass="col-md-3">
                    <span id="error_enhancedCounselling" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="AR Practitioner" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="practitioner" options="practitionerDropDown" firstOption="Please Select" value="${arCycleStageDto.practitioner}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Embryologist" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="embryologist" options="embryologistDropDown" firstOption="Please Select" value="${arCycleStageDto.embryologist}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Was a donor's Oocyte(s)/Embryo(s)/Sperms used in this cycle?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3" >
                        <div class="form-check" style="padding-left: 0px;">
                            <input class="form-check-input"
                                   type="radio"
                                   name="usedDonorOocyte"
                                   value="1"
                                   id="usedDonorOocyteRadioYes"
                                   <c:if test="${arCycleStageDto.usedDonorOocyte}">checked</c:if>
                                   aria-invalid="false" onchange="showUsedDonorOocyteControlClass(1)">
                            <label class="form-check-label"
                                   for="usedDonorOocyteRadioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" >
                        <div class="form-check" style="padding-left: 0px;">
                            <input class="form-check-input" type="radio"
                                   name="usedDonorOocyte"
                                   value="0"
                                   id="usedDonorOocyteRadioNo"
                                   <c:if test="${!arCycleStageDto.usedDonorOocyte}">checked</c:if>
                                   aria-invalid="false" onchange="hideUsedDonorOocyteControlClass(1)">
                            <label class="form-check-label"
                                   for="usedDonorOocyteRadioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<c:set var="donorFrom" value="ar"/>
<c:set var="donorDtos" value="${arCycleStageDto.donorDtos}"/>
<%@include file="donorSection.jsp"%>
<input type="hidden" name="enhancedCounsellingTipShow" value="${enhancedCounsellingTipShow}" id="enhancedCounsellingTipShow">
<iais:confirm msg="DS_ERR018" needCancel="false" popupOrder="enhancedCounsellingTip"  yesBtnDesc="ok"  needFungDuoJi="false" yesBtnCls="btn btn-primary"  callBack="enhancedCounsellingTipClose()" />