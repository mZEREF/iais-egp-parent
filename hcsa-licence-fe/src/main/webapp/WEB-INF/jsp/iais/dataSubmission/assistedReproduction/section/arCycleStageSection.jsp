<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title"  >
            <a href="#arStageDetails" data-toggle="collapse">
                Assisted Reproduction Cycle
            </a>
        </h4>
    </div>
    <div id="arStageDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%--@elvariable id="arSuperDataSubmissionDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto"--%>
                <c:set var="arCycleStageDto" value="${arSuperDataSubmissionDto.arCycleStageDto}" />
                <input type="hidden" id="startYear"  name="startYear" value="${arCycleStageDto.cycleAgeYear}">
                <input type="hidden" id="startMonth" name="startMonth" value="${arCycleStageDto.cycleAgeMonth}">
                <input type="hidden" id="cycleUnderLocal" name="cycleUnderLocal" value="${arCycleStageDto.numberOfCyclesUndergoneLocally}">
                <%@include file="patientCommon.jsp"%>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Premises where AR is performed" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:optionText value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-4 col-md-6 control-label">Date Started <span class="mandatory">*</span>
                        <a id="dateStartTooltip1" class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="First day of menstrual cycle"
                           style="z-index: 10"
                           data-original-title="">i</a>
                        <a id="dateStartTooltip2" class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="First day of stimulation"
                           style="z-index: 10"
                           data-original-title="">i</a>
                        <a id="dateStartTooltip3" class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="Day of embryo thawing"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:datePicker id="startDate" name="startDate" value="${arCycleStageDto.startDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Patient's Age as of This Cycle" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <span style="display: block"><span id="cycleAgeYear" name="cycleAgeYear">${arCycleStageDto.cycleAgeYear}</span> Years and <span id="cycleAgeMonth" name="cycleAgeYear">${arCycleStageDto.cycleAgeMonth}</span> Month</span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Main Indication" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="mainIndication" firstOption="Please Select" codeCategory="AR_MAIN_INDICATION"
                                     cssClass="mainIndicationSel" value="${arCycleStageDto.mainIndication}"
                                     onchange="toggleOnSelect(this, 'AR_MI_013', 'mainIndicationOtherRow')"/>
                </iais:value>
                </iais:row>

                <iais:row id="mainIndicationOtherRow">
                    <iais:field width="6" cssClass="col-md-6" value="Main Indication (Others)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input maxLength="100" type="text" name="mainIndicationOthers" id="mainIndicationOthers" value="${arCycleStageDto.mainIndicationOthers}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Other Indication" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="otherIndication" multiSelect="true" codeCategory="AR_OTHER_INDICATION"
                                     cssClass="otherIndicationSel" multiValues="${arCycleStageDto.otherIndicationValues}"
                                     onchange="toggleOnCheck('#otherIndication_12','otherIndicationOthersRow')"/>
                    </iais:value>
                </iais:row>

                <iais:row id="otherIndicationOthersRow" style="${StringUtil.stringContain(arCycleStageDto.otherIndication,DataSubmissionConsts.AR_OTHER_INDICATION_OTHERS) ? '' :'display: none;' }">
                    <iais:field width="6" cssClass="col-md-6" value="Other Indication (Others)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input maxLength="100" type="text" name="otherIndicationOthers" id="otherIndicationOthers" value="${arCycleStageDto.otherIndicationOthers}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="In-Vitro Maturation" mandatory="false"/>
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
                    <iais:value width="3" cssClass="col-md-3" >
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
                    <iais:field width="6" cssClass="col-md-6" value="Current AR Cycle" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                            <c:forEach items="${currentArTreatments}" var="currentArTreatment">
                                <c:set var="currentArTreatmentCode" value="${currentArTreatment.code}"/>
                                <div class="form-check col-xs-7"  style="padding-left: 0px;">
                                    <input class="form-check-input" type="checkbox"
                                           name="currentArTreatment"
                                           value="${currentArTreatmentCode}"
                                           id="currentArTreatmentCheck${currentArTreatmentCode}"
                                           <c:if test="${StringUtil.stringContain(arCycleStageDto.currentArTreatment,currentArTreatmentCode)}">checked</c:if>
                                           aria-invalid="false"  onclick="showDataStartTooltip()"  <c:if test="${currentArTreatmentCode eq DataSubmissionConsts.CURRENT_AR_TREATMENT_FRESH_CYCLE_NATURAL
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
                    <iais:value width="6" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <span id="error_currentArTreatment" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="No. of Children from Current Marriage" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="currentMarriageChildren" options="noChildrenDropDown" firstOption="Please Select"
                                         cssClass="currentMarriageChildrenSel" value="${arCycleStageDto.currentMarriageChildren}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="No. of Children from Previous Marriage" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="previousMarriageChildren" options="noChildrenDropDown" firstOption="Please Select"
                                         cssClass="previousMarriageChildrenSel" value="${arCycleStageDto.previousMarriageChildren}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="No. of Children conceived through AR" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="deliveredThroughChildren" options="noChildrenDropDown" firstOption="Please Select"
                                         cssClass="deliveredThroughChildrenSel" value="${arCycleStageDto.deliveredThroughChildren}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <label class="col-xs-4 col-md-6 control-label">Total No. of AR cycles previously undergone by patient <span class="mandatory">*</span>
                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="${DSACK002Message}"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="totalPreviouslyPreviously" options="numberArcPreviouslyDropDown"
                                         firstOption="Please Select" value="${arCycleStageDto.totalPreviouslyPreviously}"
                                         cssClass="totalPreviouslyPreviouslySel"
                                         onchange ="toggleOnSelectNoSelect(this, '21', 'totalNumberARCOtherRow')"/>
                        </iais:value>
                </iais:row>

                <iais:row id="totalNumberARCOtherRow">
                    <iais:field width="6" cssClass="col-md-6" value="No. of AR Cycles undergone Overseas" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="cyclesUndergoneOverseas" id="cyclesUndergoneOverseas"
                                        value="${arCycleStageDto.cyclesUndergoneOverseas}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="No. of AR Cycles undergone Locally" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6" display="true" id='cyclesUndergoneLocal'>
                           <c:out value="${arCycleStageDto.numberOfCyclesUndergoneLocally}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <label class="col-xs-5 col-md-6 control-label">Enhanced Counselling
                        <span id="enhancedCounsellingFieldMandatory" class="mandatory">*</span>
                    </label>
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
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check" style="padding-left: 0px;">
                            <input class="form-check-input" type="radio"
                                   name="enhancedCounselling"
                                   value="0"
                                   id="enhancedCounsellingRadioNo"
                                   <c:if test="${arCycleStageDto.enhancedCounselling != null && !arCycleStageDto.enhancedCounselling}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="enhancedCounsellingRadioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                    <iais:value width="6" cssClass="col-md-6"/>
                    <iais:value width="3" cssClass="col-md-3">
                    <span id="error_enhancedCounselling" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="AR Practitioner" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="practitioner" options="practitionerDropDown" firstOption="Please Select"
                                         cssClass="practitionerSel" value="${arCycleStageDto.practitioner}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Embryologist" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="embryologist" options="embryologistDropDown" firstOption="Please Select"
                                         cssClass="embryologistSel" value="${arCycleStageDto.embryologist}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Was a donor's Oocyte(s)/Embryo(s)/Sperms used in this cycle?" mandatory="true"/>
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
                    <iais:value width="3" cssClass="col-md-3" >
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
<iais:confirm msg="${comPareStartAge}" callBack="startAge()" popupOrder="startAgeMsgDiv" needCancel="false"
              yesBtnCls="btn btn-primary" yesBtnDesc="ok"
              needFungDuoJi="false" />