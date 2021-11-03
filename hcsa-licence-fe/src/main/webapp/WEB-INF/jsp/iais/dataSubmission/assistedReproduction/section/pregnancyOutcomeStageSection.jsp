<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/oocyteRectrievalSection.js"></script>
<c:set var="pregnancyOutcomeStageDto" value="${arSuperDataSubmissionDto.pregnancyOutcomeStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Oocyte Retrieval
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Order Shown in 1st Ultrasound (if Pregnancy confirmed)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="firstUltrasoundOrderShow" options="firstUltrasoundOrderShowSelectOption" value="${pregnancyOutcomeStageDto.firstUltrasoundOrderShow}"></iais:select>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Outcome of Pregnancy" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="pregnancyOutcome" options="pregnancyOutcomeSelectOption" value="${pregnancyOutcomeStageDto.pregnancyOutcome}"></iais:select>
                    </iais:value>
                </iais:row>
                <div <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'Others'}">style="display:none;"</c:if>>
                    <iais:row id="pregnancyOutcomeField">
                        <iais:field width="5" value="Outcome of Pregnancy (Others)" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="50" type="text" name="otherPregnancyOutcome" id="otherPregnancyOutcome" value="${pregnancyOutcomeStageDto.otherPregnancyOutcome}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'Live Birth'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="No. Live Birth (Male)" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="1" type="text" name="maleLiveBirthNum" id="maleLiveBirthNum" value="${pregnancyOutcomeStageDto.maleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. Live Birth (Female)" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="1" type="text" name="femaleLiveBirthNum" id="femaleLiveBirthNum" value="${pregnancyOutcomeStageDto.femaleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. Live Birth (Total)"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <span id="totalLiveBirthNum"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'No Live Birth'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="No. of Still Birth"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="1" type="text" name="stillBirthNum" id="stillBirthNum" value="${pregnancyOutcomeStageDto.stillBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. of Spontaneous Abortion"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="1" type="text" name="spontAbortNum" id="spontAbortNum" value="${pregnancyOutcomeStageDto.spontAbortNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. of Intra-uterine Death"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="1" type="text" name="intraUterDeathNum" id="intraUterDeathNum" value="${pregnancyOutcomeStageDto.intraUterDeathNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${not (pregnancyOutcomeStageDto.pregnancyOutcome == 'Live Birth' && pregnancyOutcomeStageDto.firstUltrasoundOrderShow == 'Singleton')}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Was Selective foetal Reduction Carried Out?"/>
                        <div class="col-md-7">
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="wasSelFoeReduCarryOut"
                                       value="Yes"
                                       id="wasSelFoeReduCarryOutYes"
                                       <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 'Yes'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="wasSelFoeReduCarryOutYes"><span
                                        class="check-circle"></span>Yes</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="wasSelFoeReduCarryOut"
                                       value="No"
                                       id="wasSelFoeReduCarryOutNo"
                                       <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 'No'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="wasSelFoeReduCarryOutNo"><span
                                        class="check-circle"></span>No</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="wasSelFoeReduCarryOut"
                                       value="Unknown"
                                       id="wasSelFoeReduCarryOutUnknown"
                                       <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 'Unknown'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="wasSelFoeReduCarryOutUnknown"><span
                                        class="check-circle"></span>Unknown</label>
                            </div>
                        </div>
                    </iais:row>
                </div>
                <div <c:if test="${pregnancyOutcomeStageDto.firstUltrasoundOrderShow == 'Unknown'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Mode of Delivery" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="deliveryMode" options="deliveryModeSelectOption" value="${pregnancyOutcomeStageDto.deliveryMode}"></iais:select>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <div class="col-sm-6 control-label formtext col-md-5">
                            <label  class="control-label control-set-font control-font-label">Date of Delivery</label>
                            <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'Live Birth'}"><span class="mandatory">*</span></c:if>
                        </div>
                        <div class="col-md-7">
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="deliveryDate"
                                       value="Unknown"
                                       id="deliveryModeUnknown"
                                       <c:if test="${pregnancyOutcomeStageDto.deliveryDate == 'Unknown'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="deliveryModeUnknown"><span
                                        class="check-square"></span>Unknown</label>
                            </div>
                            <div class="col-12">
                                <iais:datePicker id="deliveryDate" name="deliveryDate" dateVal="${pregnancyOutcomeStageDto.deliveryDate}"/>
                            </div>
                        </div>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Place of Birth"/>
                        <div class="col-md-7">
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="birthPlace"
                                       value="Local"
                                       id="birthPlaceLocal"
                                       <c:if test="${pregnancyOutcomeStageDto.birthPlace == 'Local'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="birthPlaceLocal"><span
                                        class="check-circle"></span>Local Birth</label>
                            </div>
                            <div class="form-check col-12">
                                <input class="form-check-input"
                                       type="radio"
                                       name="birthPlace"
                                       value="Overseas"
                                       id="birthPlaceOverseas"
                                       <c:if test="${pregnancyOutcomeStageDto.birthPlace == 'Overseas'}">checked</c:if>
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
                        </div>
                    </iais:row>
                    <div <c:if test="${pregnancyOutcomeStageDto.birthPlace != 'Local'}">style="display:none;"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Place of Local Birth"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="100" type="text" name="localBirthPlace" id="localBirthPlace" value="${pregnancyOutcomeStageDto.localBirthPlace}"/>
                            </iais:value>
                        </iais:row>
                    </div>

                    <iais:row>
                        <div class="col-sm-6 control-label formtext col-md-5">
                            <label  class="control-label control-set-font control-font-label">Baby Details Unknown (Loss to Follow-up)</label>
                            <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'Live Birth'}"><span class="mandatory">*</span></c:if>
                        </div>
                        <div class="col-md-7">
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
                        </div>
                    </iais:row>
                </div>

                <%@include file="pregnancyOutcomeStageBabySection.jsp" %>

                <iais:row>
                    <iais:field width="5" value="Total No. of Baby Admitted to NICU Care" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="l2CareBabyNum" options="l2CareBabyNumSelectOption" value="${pregnancyOutcomeStageDto.l2CareBabyNum}"></iais:select>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Baby Admitted to L2 Care" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="l2CareBabyNum" options="l2CareBabyNumSelectOption" value="${pregnancyOutcomeStageDto.l2CareBabyNum}"></iais:select>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Baby Admitted to L3 Care" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="l3CareBabyNum" options="l3CareBabyNumSelectOption" value="${pregnancyOutcomeStageDto.l3CareBabyNum}"></iais:select>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Days Baby Stay in L2 (Provide average if > one baby stayed)"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="4" type="text" name="l2CareBabyDays" id="l2CareBabyDays" value="${pregnancyOutcomeStageDto.l2CareBabyDays}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Days Baby Stay in L3 (Provide average if > one baby stayed)"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="4" type="text" name="l2CareBl3CareBabyDaysabyDays" id="l3CareBabyDays" value="${pregnancyOutcomeStageDto.l3CareBabyDays}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>