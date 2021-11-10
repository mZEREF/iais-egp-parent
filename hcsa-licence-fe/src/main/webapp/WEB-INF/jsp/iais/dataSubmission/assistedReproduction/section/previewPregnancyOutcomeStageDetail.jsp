<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pregnancyOutcomeStageDto" value="${arSuperDataSubmissionDto.pregnancyOutcomeStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading completed">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Outcome of Pregnancy
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal "><%--min-row--%>
                <iais:row>
                    <iais:field width="7" value="Order Shown in 1st Ultrasound (if Pregnancy confirmed)"
                                cssClass="col-md-7"/>
                    <iais:value width="5" cssClass="col-md-5">
                        <iais:code code="${pregnancyOutcomeStageDto.firstUltrasoundOrderShow}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="7" value="Outcome of Pregnancy" cssClass="col-md-7"/>
                    <iais:value width="5" cssClass="col-md-5">
                        <iais:code code="${pregnancyOutcomeStageDto.pregnancyOutcome}"/>
                    </iais:value>
                </iais:row>
                <div id="otherPregnancyOutcomeDiv"
                     <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'OUTOPRE004'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="7" value="Outcome of Pregnancy (Others)" cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.otherPregnancyOutcome}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="liveBirthNumSection"
                     <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'OUTOPRE001'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="7" value="No. Live Birth (Male)" cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.maleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="7" value="No. Live Birth (Female)" cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.femaleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="7" value="No. Live Birth (Total)" cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.maleLiveBirthNum + pregnancyOutcomeStageDto.femaleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="stillBirthNumSection"
                     <c:if test="${not(
                        pregnancyOutcomeStageDto.pregnancyOutcome == 'OUTOPRE002' or
                      (pregnancyOutcomeStageDto.pregnancyOutcome == 'Live Birth' && pregnancyOutcomeStageDto.firstUltrasoundOrderShow != 'OSIOU001'))
                      }">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="7" value="No. of Still Birth" cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.stillBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="7" value="No. of Spontaneous Abortion" cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.spontAbortNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="7" value="No. of Intra-uterine Death" cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.intraUterDeathNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="wasSelFoeReduCarryOutDiv"
                     <c:if test="${not (pregnancyOutcomeStageDto.pregnancyOutcome == 'OUTOPRE001' && pregnancyOutcomeStageDto.firstUltrasoundOrderShow != 'OSIOU001')}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="7" value="Was Selective foetal Reduction Carried Out?" cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <p class="col-12">
                                <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 0}">Yes</c:if>
                                <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 1}">No</c:if>
                                <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 0}">Unknown</c:if>
                            </p>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="deliverySection"
                     <c:if test="${pregnancyOutcomeStageDto.firstUltrasoundOrderShow == 'OSIOU005'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="7" value="Mode of Delivery"
                                    cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <iais:code code="${pregnancyOutcomeStageDto.deliveryMode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="7" value="Date of Delivery"
                                    cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:if test="${pregnancyOutcomeStageDto.deliveryDateType == 'Unknown'}">Unknown</c:if>
                            <c:if test="${pregnancyOutcomeStageDto.deliveryDateType != 'Unknown'}">
                                <fmt:formatDate value="${pregnancyOutcomeStageDto.deliveryDate}"
                                                pattern="dd/MM/yyyy"/>
                            </c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="7" value="Place of Birth"
                                    cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.birthPlace}"/>
                        </iais:value>
                    </iais:row>
                    <div id="localBirthPlaceDiv"
                         <c:if test="${pregnancyOutcomeStageDto.birthPlace != 'Local Birth'}">style="display:none;"</c:if>>
                        <iais:row>
                            <iais:field width="7" value="Place of Local Birth"
                                        cssClass="col-md-7"/>
                            <iais:value width="5" cssClass="col-md-5">
                                <c:out value="${pregnancyOutcomeStageDto.localBirthPlace}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="7" value="Baby Details Unknown (Loss to Follow-up)"
                                    cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.babyDetailsUnknown?'yes':'no'}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <%@include file="previewPregnancyOutcomeStageBabySection.jsp" %>

                <iais:row>
                    <iais:field width="7" value="Total No. of Baby Admitted to NICU Care"
                                cssClass="col-md-7"/>
                    <iais:value width="5" cssClass="col-md-5">
                        <c:out value="${pregnancyOutcomeStageDto.l2CareBabyNum + pregnancyOutcomeStageDto.l3CareBabyNum}"/>
                    </iais:value>
                </iais:row>
                <div id="careBabyNumSection"
                     <c:if test="${pregnancyOutcomeStageDto.l2CareBabyNum + pregnancyOutcomeStageDto.l3CareBabyNum < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="7" value="No. of Baby Admitted to L2 Care"
                                    cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.l2CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="7" value="No. of Baby Admitted to L3 Care"
                                    cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.l3CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l2CareBabyDaysDiv"
                     <c:if test="${pregnancyOutcomeStageDto.l2CareBabyNum < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="7" value="No. Days Baby Stay in L2 (Provide average if > one baby stayed)"
                                    cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.l2CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l3CareBabyDaysDiv"
                     <c:if test="${pregnancyOutcomeStageDto.l3CareBabyNum < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="7" value="No. Days Baby Stay in L3 (Provide average if > one baby stayed)"
                                    cssClass="col-md-7"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <c:out value="${pregnancyOutcomeStageDto.l3CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>