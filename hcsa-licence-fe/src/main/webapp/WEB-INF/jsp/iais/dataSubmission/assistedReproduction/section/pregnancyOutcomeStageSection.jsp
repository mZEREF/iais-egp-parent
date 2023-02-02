<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/pregnancyOutcomeStage.js"></script>
<c:set var="pregnancyOutcomeStageDto" value="${arSuperDataSubmissionDto.pregnancyOutcomeStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Outcome of Pregnancy
            </strong>
        </h4>
    </div>

    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">

                <iais:row>
                    <iais:field width="6" value="Order Shown in 1st Ultrasound (if Pregnancy confirmed)"
                                cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="firstUltrasoundOrderShow" firstOption="Please Select"
                                     id="firstUltrasoundOrderShow" codeCategory="CATE_ID_ORDER_IN_ULTRASOUND"
                                     cssClass="firstUltrasoundOrderShowSel"
                                     value="${pregnancyOutcomeStageDto.firstUltrasoundOrderShow}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Outcome of Pregnancy" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="pregnancyOutcome" firstOption="Please Select" id="pregnancyOutcome"
                                     codeCategory="CATE_ID_OUTCOME_OFPREGNANCY" cssClass="pregnancyOutcomeSel"
                                     value="${pregnancyOutcomeStageDto.pregnancyOutcome}"/>
                    </iais:value>
                </iais:row>
                <div id="otherPregnancyOutcomeDiv">
                    <iais:row id="pregnancyOutcomeField">
                        <iais:field width="6" value="Outcome of Pregnancy (Others)" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="50" type="text" name="otherPregnancyOutcome"
                                        id="otherPregnancyOutcome"
                                        value="${pregnancyOutcomeStageDto.otherPregnancyOutcome}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="liveBirthNumSection">
                    <iais:row>
                        <iais:field width="6" value="No. Live Birth (Male)" mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="maleLiveBirthNum" id="maleLiveBirthNum"
                                        value="${pregnancyOutcomeStageDto.maleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. Live Birth (Female)" mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="femaleLiveBirthNum" id="femaleLiveBirthNum"
                                        value="${pregnancyOutcomeStageDto.femaleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. Live Birth (Total)" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <p id="totalLiveBirthNum">${totalLiveBirthNum}</p>
                            <span id="error_totalLiveBirthNum" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="stillBirthNumSection">
                    <iais:row>
                        <iais:field width="6" value="No. of Still Birth" mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="stillBirthNum" id="stillBirthNum"
                                        value="${pregnancyOutcomeStageDto.stillBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Spontaneous Abortion" mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="spontAbortNum" id="spontAbortNum"
                                        value="${pregnancyOutcomeStageDto.spontAbortNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Intra-uterine Death" mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="intraUterDeathNum" id="intraUterDeathNum"
                                        value="${pregnancyOutcomeStageDto.intraUterDeathNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="wasSelFoeReduCarryOutDiv">
                    <iais:row>
                        <iais:field width="6" value="Was Selective foetal Reduction Carried Out?" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="wasSelFoeReduCarryOut" id="wasSelFoeReduCarryOut"
                                         options="wasSelFoeReduCarryOutOptions" cssClass="wasSelFoeReduCarryOutSel"
                                         value="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="deliverySection">
                    <iais:row>
                        <iais:field width="6" value="Mode of Delivery" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="deliveryMode" firstOption="Please Select"
                                         codeCategory="CATE_ID_MODE_OF_DELIVERY" cssClass="deliveryModeSel"
                                         value="${pregnancyOutcomeStageDto.deliveryMode}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <label class="col-xs-5 col-md-6 control-label">Date of Delivery&nbsp;
                            <span id="deliveryDateFieldMandatory" class="mandatory">*</span>
                        </label>
                        <iais:value width="3" cssClass="col-md-3">
                        <div class="col-12" style="padding: 0px">
                            <iais:datePicker id="deliveryDate" name="deliveryDate"
                                             dateVal="${pregnancyOutcomeStageDto.deliveryDate}"/>
                        </div>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check col-12" style="padding: 0px">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="deliveryDateCheckbox"
                                       value="Unknown"
                                       id="deliveryDateUnknown"
                                       <c:if test="${pregnancyOutcomeStageDto.deliveryDateType == 'Unknown'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="deliveryDateUnknown"><span
                                        class="check-square"></span>Unknown</label>
                            </div>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="6" value="Place of Birth" cssClass="col-md-6"/>
                        <div class="col-md-6">
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="birthPlace"
                                       value="POSBP001"
                                       id="birthPlaceLocal"
                                       <c:if test="${pregnancyOutcomeStageDto.birthPlace == 'POSBP001'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="birthPlaceLocal"><span
                                        class="check-circle"></span>Local Birth</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="birthPlace"
                                       value="POSBP002"
                                       id="birthPlaceOverseas"
                                       <c:if test="${pregnancyOutcomeStageDto.birthPlace == 'POSBP002'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="birthPlaceOverseas"><span
                                        class="check-circle"></span>Overseas Birth</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="birthPlace"
                                       value="POSBP003"
                                       id="birthPlaceUnknown"
                                       <c:if test="${pregnancyOutcomeStageDto.birthPlace == 'POSBP003'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="birthPlaceUnknown"><span
                                        class="check-circle"></span>Unknown</label>
                            </div>
                            <span id="error_birthPlace" name="iaisErrorMsg" class="error-msg"></span>
                        </div>
                    </iais:row>
                    <div id="localBirthPlaceDiv">
                        <iais:row>
                            <iais:field width="6" value="Place of Local Birth" cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6">
                                <iais:input maxLength="100" type="text" name="localBirthPlace" id="localBirthPlace"
                                            value="${pregnancyOutcomeStageDto.localBirthPlace}"/>
                            </iais:value>
                        </iais:row>
                    </div>

                    <iais:row>
                        <label class="col-xs-5 col-md-6 control-label">Baby Details Unknown (Loss to Follow-up)
                            &nbsp;<span id="babyDetailsUnknownFieldMandatory" class="mandatory">*</span>
                        </label>
                        <div class="col-md-6">
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="babyDetailsUnknown"
                                       value="true"
                                       id="babyDetailsUnknownYes"
                                       <c:if test="${pregnancyOutcomeStageDto.babyDetailsUnknown}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="babyDetailsUnknownYes"><span
                                        class="check-circle"></span>Yes</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="babyDetailsUnknown"
                                       value="false"
                                       id="babyDetailsUnknownNo"
                                       <c:if test="${! pregnancyOutcomeStageDto.babyDetailsUnknown}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="babyDetailsUnknownNo"><span
                                        class="check-circle"></span>No</label>
                            </div>
                            <span id="error_babyDetailsUnknown" name="iaisErrorMsg" class="error-msg"></span>
                        </div>
                    </iais:row>
                </div>
                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>

<div class="panel panel-default babyDetailsPageDiv">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Details of Babies
            </strong>
        </h4>
    </div>
    <div class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="pregnancyOutcomeStageBabySection.jsp" %>

                <iais:row cssClass="NICUCareBabyNumRow">
                    <iais:field width="6" value="Total No. of Baby Admitted to NICU Care"
                                cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="NICUCareBabyNum" id="NICUCareBabyNum" options="transferNumSelectOption"
                                     cssClass="NICUCareBabyNumSel" value="${pregnancyOutcomeStageDto.nicuCareBabyNum}"/>
                    </iais:value>
                </iais:row>
                <div id="careBabyNumSection">
                    <iais:row>
                        <iais:field width="6" value="No. of Baby Admitted to L2 Care"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="l2CareBabyNum" id="l2CareBabyNum" options="transferNumSelectOption"
                                         cssClass="l2CareBabyNumSel" value="${pregnancyOutcomeStageDto.l2CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Baby Admitted to L3 Care"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="l3CareBabyNum" id="l3CareBabyNum" options="transferNumSelectOption"
                                         cssClass="l3CareBabyNumSel" value="${pregnancyOutcomeStageDto.l3CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l2CareBabyDaysDiv">
                    <iais:row>
                        <iais:field width="6" value="No. Days Baby Stay in L2 (Provide average if > one baby stayed)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="l2CareBabyDays" id="l2CareBabyDays"
                                        value="${pregnancyOutcomeStageDto.l2CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l3CareBabyDaysDiv">
                    <iais:row>
                        <iais:field width="6" value="No. Days Baby Stay in L3 (Provide average if > one baby stayed)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="l3CareBabyDays" id="l3CareBabyDays"
                                        value="${pregnancyOutcomeStageDto.l3CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>