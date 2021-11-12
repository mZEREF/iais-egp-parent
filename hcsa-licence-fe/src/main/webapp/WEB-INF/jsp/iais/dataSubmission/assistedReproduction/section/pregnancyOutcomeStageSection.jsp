<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/pregnancyOutcomeStage.js"></script>
<c:set var="pregnancyOutcomeStageDto" value="${arSuperDataSubmissionDto.pregnancyOutcomeStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Outcome of Pregnancy
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="6" value="Order Shown in 1st Ultrasound (if Pregnancy confirmed)"
                                cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="firstUltrasoundOrderShow" firstOption="Please Select"
                                     id="firstUltrasoundOrderShow" options="firstUltrasoundOrderShowSelectOption"
                                     value="${pregnancyOutcomeStageDto.firstUltrasoundOrderShow}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Outcome of Pregnancy" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="pregnancyOutcome" firstOption="Please Select" id="pregnancyOutcome"
                                     options="pregnancyOutcomeSelectOption"
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
                        <iais:field width="6" value="No. Live Birth (Male)" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="1" type="text" name="maleLiveBirthNum" id="maleLiveBirthNum"
                                        value="${pregnancyOutcomeStageDto.maleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. Live Birth (Female)" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="1" type="text" name="femaleLiveBirthNum" id="femaleLiveBirthNum"
                                        value="${pregnancyOutcomeStageDto.femaleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. Live Birth (Total)" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <span id="totalLiveBirthNum">${pregnancyOutcomeStageDto.maleLiveBirthNum + pregnancyOutcomeStageDto.femaleLiveBirthNum}</span>
                            <span id="error_totalLiveBirthNum" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="stillBirthNumSection">
                    <iais:row>
                        <iais:field width="6" value="No. of Still Birth" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="1" type="text" name="stillBirthNum" id="stillBirthNum"
                                        value="${pregnancyOutcomeStageDto.stillBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Spontaneous Abortion" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="1" type="text" name="spontAbortNum" id="spontAbortNum"
                                        value="${pregnancyOutcomeStageDto.spontAbortNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Intra-uterine Death" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="1" type="text" name="intraUterDeathNum" id="intraUterDeathNum"
                                        value="${pregnancyOutcomeStageDto.intraUterDeathNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="wasSelFoeReduCarryOutDiv">
                    <iais:row>
                        <iais:field width="6" value="Was Selective foetal Reduction Carried Out?" cssClass="col-md-6"/>
                        <div class="col-md-6">
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="wasSelFoeReduCarryOut"
                                       value="0"
                                       id="wasSelFoeReduCarryOutYes"
                                       <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 0}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="wasSelFoeReduCarryOutYes"><span
                                        class="check-circle"></span>Yes</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="wasSelFoeReduCarryOut"
                                       value="1"
                                       id="wasSelFoeReduCarryOutNo"
                                       <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 1}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="wasSelFoeReduCarryOutNo"><span
                                        class="check-circle"></span>No</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="wasSelFoeReduCarryOut"
                                       value="2"
                                       id="wasSelFoeReduCarryOutUnknown"
                                       <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 2}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="wasSelFoeReduCarryOutUnknown"><span
                                        class="check-circle"></span>Unknown</label>
                            </div>
                            <span id="error_wasSelFoeReduCarryOut" name="iaisErrorMsg" class="error-msg"></span>
                        </div>
                    </iais:row>
                </div>
                <div id="deliverySection">
                    <iais:row>
                        <iais:field width="6" value="Mode of Delivery" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="deliveryMode" firstOption="Please Select"
                                         options="deliveryModeSelectOption"
                                         value="${pregnancyOutcomeStageDto.deliveryMode}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <label class="col-xs-5 col-md-6 control-label">Date of Delivery&nbsp;
                            <span id="deliveryDateFieldMandatory" class="mandatory">*</span>
                        </label>
                        <div class="col-md-6">
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
                            <div class="col-12" style="padding: 0px">
                                <iais:datePicker id="deliveryDate" name="deliveryDate"
                                                 dateVal="${pregnancyOutcomeStageDto.deliveryDate}"/>
                            </div>
                        </div>
                    </iais:row>

                    <iais:row>
                        <iais:field width="6" value="Place of Birth" cssClass="col-md-6"/>
                        <div class="col-md-6">
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="birthPlace"
                                       value="Local Birth"
                                       id="birthPlaceLocal"
                                       <c:if test="${pregnancyOutcomeStageDto.birthPlace == 'Local Birth'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="birthPlaceLocal"><span
                                        class="check-circle"></span>Local Birth</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="birthPlace"
                                       value="Overseas Birth"
                                       id="birthPlaceOverseas"
                                       <c:if test="${pregnancyOutcomeStageDto.birthPlace == 'Overseas Birth'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="birthPlaceOverseas"><span
                                        class="check-circle"></span>Overseas Birth</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="birthPlace"
                                       value="Unknown"
                                       id="birthPlaceUnknown"
                                       <c:if test="${pregnancyOutcomeStageDto.birthPlace == 'Unknown'}">checked</c:if>
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

                <%@include file="pregnancyOutcomeStageBabySection.jsp" %>

                <iais:row cssClass="NICUCareBabyNumRow">
                    <iais:field width="6" value="Total No. of Baby Admitted to NICU Care"
                                cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="NICUCareBabyNum" id="NICUCareBabyNum" options="NICUCareBabyNumSelectOption"
                                     value="${pregnancyOutcomeStageDto.nicuCareBabyNum}"/>
                    </iais:value>
                </iais:row>
                <div id="careBabyNumSection">
                    <iais:row>
                        <iais:field width="6" value="No. of Baby Admitted to L2 Care"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="l2CareBabyNum" id="l2CareBabyNum" options="l2CareBabyNumSelectOption"
                                         value="${pregnancyOutcomeStageDto.l2CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Baby Admitted to L3 Care"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="l3CareBabyNum" id="l3CareBabyNum" options="l3CareBabyNumSelectOption"
                                         value="${pregnancyOutcomeStageDto.l3CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l2CareBabyDaysDiv">
                    <iais:row>
                        <iais:field width="6" value="No. Days Baby Stay in L2 (Provide average if > one baby stayed)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="4" type="text" name="l2CareBabyDays" id="l2CareBabyDays"
                                        value="${pregnancyOutcomeStageDto.l2CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l3CareBabyDaysDiv">
                    <iais:row>
                        <iais:field width="6" value="No. Days Baby Stay in L3 (Provide average if > one baby stayed)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="4" type="text" name="l3CareBabyDays" id="l3CareBabyDays"
                                        value="${pregnancyOutcomeStageDto.l3CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>