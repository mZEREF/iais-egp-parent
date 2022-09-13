<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pregnancyOutcomeStageDto" value="${arSuperDataSubmissionDto.pregnancyOutcomeStageDto}"/>
<c:set var="embryoTransferredOutcomeStageDto" value="${arSuperDataSubmissionDto.embryoTransferredOutcomeStageDto}"/>
<c:set var="cycle" value="${arSuperDataSubmissionDto.selectionDto.cycle}"/>

<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" href="#cycleDetails" data-toggle="collapse">
                <c:if test="${cycle == 'DSCL_008'}">Outcome</c:if>
                <c:if test="${cycle == 'DSCL_009'}">Outcome of IUI Cycle</c:if>
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal "><%--min-row--%>
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="6" value="Outcome of Embryo Transferred" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true" >
                        <iais:code code="${embryoTransferredOutcomeStageDto.transferedOutcome}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Order Shown in 1st Ultrasound (if Pregnancy confirmed)"
                                cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${pregnancyOutcomeStageDto.firstUltrasoundOrderShow}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Outcome of Pregnancy" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${pregnancyOutcomeStageDto.pregnancyOutcome}"/>
                    </iais:value>
                </iais:row>
                <div id="otherPregnancyOutcomeDiv"
                     <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'OUTOPRE004'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Outcome of Pregnancy (Others)" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.otherPregnancyOutcome}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="liveBirthNumSection"
                     <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome != 'OUTOPRE001'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. Live Birth (Male)" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.maleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. Live Birth (Female)" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.femaleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. Live Birth (Total)" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.maleLiveBirthNum + pregnancyOutcomeStageDto.femaleLiveBirthNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="stillBirthNumSection"
                     <c:if test="${not(
                        pregnancyOutcomeStageDto.pregnancyOutcome == 'OUTOPRE002' or
                      (pregnancyOutcomeStageDto.pregnancyOutcome == 'OUTOPRE001' && pregnancyOutcomeStageDto.firstUltrasoundOrderShow != 'OSIOU001'))
                      }">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. of Still Birth" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.stillBirthNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Spontaneous Abortion" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.spontAbortNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Intra-uterine Death" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.intraUterDeathNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="wasSelFoeReduCarryOutDiv"
                     <c:if test="${not (pregnancyOutcomeStageDto.firstUltrasoundOrderShow != 'OSIOU001' and (not empty pregnancyOutcomeStageDto.firstUltrasoundOrderShow))}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Was Selective foetal Reduction Carried Out?" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 0}">Yes</c:if>
                            <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 1}">No</c:if>
                            <c:if test="${pregnancyOutcomeStageDto.wasSelFoeReduCarryOut == 2}">Unknown</c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="deliverySection"
                     <c:if test="${pregnancyOutcomeStageDto.pregnancyOutcome == 'OUTOPRE003'}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Mode of Delivery"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <iais:code code="${pregnancyOutcomeStageDto.deliveryMode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Date of Delivery"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:if test="${pregnancyOutcomeStageDto.deliveryDateType == 'Unknown'}">Unknown</c:if>
                            <c:if test="${pregnancyOutcomeStageDto.deliveryDateType != 'Unknown'}">
                                <fmt:formatDate value="${pregnancyOutcomeStageDto.deliveryDate}"
                                                pattern="dd/MM/yyyy"/>
                            </c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Place of Birth"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <iais:code code="${pregnancyOutcomeStageDto.birthPlace}"/>
                        </iais:value>
                    </iais:row>
                    <div id="localBirthPlaceDiv"
                         <c:if test="${pregnancyOutcomeStageDto.birthPlace != 'POSBP001'}">style="display:none;"</c:if>>
                        <iais:row>
                            <iais:field width="6" value="Place of Local Birth"
                                        cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="${pregnancyOutcomeStageDto.localBirthPlace}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="6" value="Baby Details Unknown (Loss to Follow-up)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.babyDetailsUnknown?'Yes':'No'}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>

<div class="panel panel-default babyDetailsPageDiv"
     <c:if test="${(pregnancyOutcomeStageDto.maleLiveBirthNum + pregnancyOutcomeStageDto.femaleLiveBirthNum) < 1}">style="display:none;"</c:if>>
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" href="#babyDetals" data-toggle="collapse">
                Details of Babies
            </a>
        </h4>
    </div>
    <div class="panel-collapse collapse" id="babyDetals">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="previewPregnancyOutcomeStageBabySection.jsp" %>

                <div id="careBabyNumSection"
                     <c:if test="${(pregnancyOutcomeStageDto.maleLiveBirthNum + pregnancyOutcomeStageDto.femaleLiveBirthNum) < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Total No. of Baby Admitted to NICU Care"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l2CareBabyNum + pregnancyOutcomeStageDto.l3CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <div id="careBabyNumSection"
                     <c:if test="${pregnancyOutcomeStageDto.l2CareBabyNum + pregnancyOutcomeStageDto.l3CareBabyNum < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. of Baby Admitted to L2 Care"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l2CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Baby Admitted to L3 Care"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l3CareBabyNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l2CareBabyDaysDiv"
                     <c:if test="${pregnancyOutcomeStageDto.l2CareBabyNum < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. Days Baby Stay in L2 (Provide average if > one baby stayed)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l2CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="l3CareBabyDaysDiv"
                     <c:if test="${pregnancyOutcomeStageDto.l3CareBabyNum < 1}">style="display:none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. Days Baby Stay in L3 (Provide average if > one baby stayed)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${pregnancyOutcomeStageDto.l3CareBabyDays}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>