<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pregnancyOutcomeStageDto" value="${arSuperDataSubmissionDto.pregnancyOutcomeStageDto}"/>
<c:set var="pregnancyOutcomeStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.pregnancyOutcomeStageDto}"/>

<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                Outcome of Pregnancy
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal "><%--min-row--%>
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="4" cssClass="col-md-4"  value="" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                    <c:if test="${not empty arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}">
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <iais:select  id="oldDsSelect" name="oldDsSelect" options="versionOptions" value="${arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}"/>
                        </iais:value>
                    </c:if>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Order Shown in 1st Ultrasound (if Pregnancy confirmed)"
                                cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${pregnancyOutcomeStageDto.firstUltrasoundOrderShow}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${pregnancyOutcomeStageDtoVersion.firstUltrasoundOrderShow}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Outcome of Pregnancy" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${pregnancyOutcomeStageDto.pregnancyOutcome}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${pregnancyOutcomeStageDtoVersion.pregnancyOutcome}"/>
                    </iais:value>
                </iais:row>
                <div id="otherPregnancyOutcomeDiv"
                     <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'OUTOPRE004' && pregnancyOutcomeStageDtoVersion.pregnancyOutcome != 'OUTOPRE004'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Outcome of Pregnancy (Others)" cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.otherPregnancyOutcome}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.otherPregnancyOutcome}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="liveBirthNumSection"
                     <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'OUTOPRE001' || pregnancyOutcomeStageDtoVersion.pregnancyOutcome != 'OUTOPRE001'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="No. Live Birth (Male)" cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.maleLiveBirthNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.maleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="No. Live Birth (Female)" cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.femaleLiveBirthNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.femaleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="No. Live Birth (Total)" cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.maleLiveBirthNum + pregnancyOutcomeStageDto.femaleLiveBirthNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.maleLiveBirthNum + pregnancyOutcomeStageDtoVersion.femaleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="stillBirthNumSection"
                     <c:if test="${not(pregnancyOutcomeStageDto.pregnancyOutcome == 'OUTOPRE002' or
                      (pregnancyOutcomeStageDto.pregnancyOutcome == 'OUTOPRE001' && pregnancyOutcomeStageDto.firstUltrasoundOrderShow != 'OSIOU001')) and
                      not(pregnancyOutcomeStageDto.pregnancyOutcome == 'OUTOPRE002' or
                      (pregnancyOutcomeStageDtoVersion.pregnancyOutcome == 'OUTOPRE001' && pregnancyOutcomeStageDtoVersion.firstUltrasoundOrderShow != 'OSIOU001'))
                      }">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="No. of Still Birth" cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.stillBirthNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.stillBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="No. of Spontaneous Abortion" cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.spontAbortNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.spontAbortNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="No. of Intra-uterine Death" cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.intraUterDeathNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.intraUterDeathNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="wasSelFoeReduCarryOutDiv"
                     <c:if test="${not (pregnancyOutcomeStageDto.firstUltrasoundOrderShow != 'OSIOU001' and (not empty pregnancyOutcomeStageDto.firstUltrasoundOrderShow)) && not (pregnancyOutcomeStageDtoVersion.firstUltrasoundOrderShow != 'OSIOU001' and (not empty pregnancyOutcomeStageDtoVersion.firstUltrasoundOrderShow))}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Was Selective foetal Reduction Carried Out?" cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 0}">Yes</c:if>
                            <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 1}">No</c:if>
                            <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 2}">Unknown</c:if>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${pregnancyOutcomeStageDtoVersion.wasSelFoeReduCarryOut == 0}">Yes</c:if>
                            <c:if test="${pregnancyOutcomeStageDtoVersion.wasSelFoeReduCarryOut == 1}">No</c:if>
                            <c:if test="${pregnancyOutcomeStageDtoVersion.wasSelFoeReduCarryOut == 2}">Unknown</c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="deliverySection"
                     <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome == 'OUTOPRE003' || pregnancyOutcomeStageDtoVersion.pregnancyOutcome == 'OUTOPRE003'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Mode of Delivery"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <iais:code code="${pregnancyOutcomeStageDto.deliveryMode}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <iais:code code="${pregnancyOutcomeStageDtoVersion.deliveryMode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Date of Delivery"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${pregnancyOutcomeStageDto.deliveryDateType == 'Unknown'}">Unknown</c:if>
                            <c:if test="${pregnancyOutcomeStageDto.deliveryDateType != 'Unknown'}">
                                <fmt:formatDate value="${pregnancyOutcomeStageDto.deliveryDate}"
                                                pattern="dd/MM/yyyy"/>
                            </c:if>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${pregnancyOutcomeStageDtoVersion.deliveryDateType == 'Unknown'}">Unknown</c:if>
                            <c:if test="${pregnancyOutcomeStageDtoVersion.deliveryDateType != 'Unknown'}">
                                <fmt:formatDate value="${pregnancyOutcomeStageDtoVersion.deliveryDate}"
                                                pattern="dd/MM/yyyy"/>
                            </c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Place of Birth"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <iais:code code="${pregnancyOutcomeStageDto.birthPlace}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <iais:code code="${pregnancyOutcomeStageDtoVersion.birthPlace}"/>
                        </iais:value>
                    </iais:row>
                    <div id="localBirthPlaceDiv"
                         <c:if test="${pregnancyOutcomeStageDto.birthPlace != 'POSBP001' && pregnancyOutcomeStageDtoVersion.birthPlace != 'POSBP001'}">style="display:none;"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="Place of Local Birth"
                                        cssClass="col-md-4"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${pregnancyOutcomeStageDto.localBirthPlace}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${pregnancyOutcomeStageDtoVersion.localBirthPlace}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="4" value="Baby Details Unknown (Loss to Follow-up)"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.babyDetailsUnknown?'Yes':'No'}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.babyDetailsUnknown?'Yes':'No'}"/>
                        </iais:value>
                    </iais:row>
                </div>

            </div>
        </div>
    </div>
</div>

<div class="panel panel-default babyDetailsPageDiv"
     <c:if test="${(pregnancyOutcomeStageDto.maleLiveBirthNum + pregnancyOutcomeStageDto.femaleLiveBirthNum) < 1 || (pregnancyOutcomeStageDtoVersion.maleLiveBirthNum + pregnancyOutcomeStageDtoVersion.femaleLiveBirthNum) < 1}">style="display:none;"</c:if>>
    <div class="panel-heading">
        <h4 class="panel-title">
            <a href="#babyDetals" data-toggle="collapse">
                Details of Babies
            </a>
        </h4>
    </div>
    <div class="panel-collapse collapse in" id="babyDetals">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">


                <div id="careBabyNumSection"
                     <c:if test="${(pregnancyOutcomeStageDto.maleLiveBirthNum + pregnancyOutcomeStageDto.femaleLiveBirthNum) < 1 || (pregnancyOutcomeStageDtoVersion.maleLiveBirthNum + pregnancyOutcomeStageDtoVersion.femaleLiveBirthNum) < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Total No. of Baby Admitted to NICU Care"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l2CareBabyNum + pregnancyOutcomeStageDto.l3CareBabyNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.l2CareBabyNum + pregnancyOutcomeStageDtoVersion.l3CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <div id="careBabyNumSection2"
                     <c:if test="${pregnancyOutcomeStageDto.l2CareBabyNum + pregnancyOutcomeStageDto.l3CareBabyNum < 1 || pregnancyOutcomeStageDtoVersion.l2CareBabyNum + pregnancyOutcomeStageDtoVersion.l3CareBabyNum < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="No. of Baby Admitted to L2 Care"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l2CareBabyNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.l2CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="No. of Baby Admitted to L3 Care"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l3CareBabyNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.l3CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l2CareBabyDaysDiv"
                     <c:if test="${pregnancyOutcomeStageDto.l2CareBabyNum < 1 || pregnancyOutcomeStageDtoVersion.l2CareBabyNum < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="No. Days Baby Stay in L2 (Provide average if > one baby stayed)"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l2CareBabyDays}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.l2CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l3CareBabyDaysDiv"
                     <c:if test="${pregnancyOutcomeStageDto.l3CareBabyNum < 1 || pregnancyOutcomeStageDtoVersion.l3CareBabyNum < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="No. Days Baby Stay in L3 (Provide average if > one baby stayed)"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l3CareBabyDays}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${pregnancyOutcomeStageDtoVersion.l3CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>