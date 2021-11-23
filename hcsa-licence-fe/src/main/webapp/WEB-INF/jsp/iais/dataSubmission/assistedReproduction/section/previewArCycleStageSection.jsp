<c:set var="headingSign" value="completed"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title" >
            <a href="#viewArCycleStage" data-toggle="collapse" >
                Assisted Reproduction Submission
            </a>
        </h4>
    </div>
    <div id="viewArCycleStage" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="arCycleStageDto" value="${arSuperDataSubmissionDto.arCycleStageDto}" />
                <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
                <h3>
                    <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${patientDto.name}"/>&nbsp</label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;">${empty patientDto.idNumber ? "" : "("}<c:out value="${patientDto.idNumber}"/>${empty patientDto.idNumber ? "" : ")"} </label></p>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Premises where AR is performed" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date Started" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                    <p><c:out value="${arCycleStageDto.startDate}" /></p>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age as of This Cycle" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                   <p><c:out value="${arCycleStageDto.cycleAge}"/></p>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Main Indication" />
                    <iais:value width="7" cssClass="col-md-7"  display="true">
                       <iais:code code="${arCycleStageDto.mainIndication}"/>
                    </iais:value>
                </iais:row>

                <iais:row style="${arCycleStageDto.mainIndication eq DataSubmissionConsts.AR_MAIN_INDICATION_OTHERS ? '' : 'display: none'}">
                    <iais:field width="5" value="Main Indication (Others)" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arCycleStageDto.mainIndicationOthers}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Other Indication" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:forEach items="${arCycleStageDto.otherIndicationValues}" var="otherIndicationValue" varStatus="status">
                            <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${otherIndicationValue}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>

                <iais:row style="${StringUtil.stringContain(arCycleStageDto.otherIndication,DataSubmissionConsts.AR_OTHER_INDICATION_OTHERS) ? '' : 'display: none'}">
                    <iais:field width="5" value="Other Indication (Others)" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arCycleStageDto.otherIndicationOthers}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="In-Vitro Maturation" />
                    <iais:value width="7" cssClass="col-md-3" display="true">
                        <c:out value="${arCycleStageDto.inVitroMaturation ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Current AR Treatment" />
                    <iais:value width="7" cssClass="col-md-7">
                            <c:forEach items="${arCycleStageDto.currentArTreatmentValues}" var="currentAR" varStatus="status">
                                <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${currentAR}"/>
                            </c:forEach>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children from Current Marriage" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.currentMarriageChildren}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children from Previous Marriage" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.previousMarriageChildren}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children conceived through AR" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.deliveredThroughChildren}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children conceived through AR" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.totalPreviouslyPreviously}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row id="totalNumberARCOtherRow" style="${ arCycleStageDto.totalPreviouslyPreviously == 21 ? '' : 'display: none'}">
                    <iais:field width="5" value="Number of Cycles undergone Overseas" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arCycleStageDto.cyclesUndergoneOverseas}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Number of Cycles undergone Locally" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.numberOfCyclesUndergoneLocally}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Enhanced Counselling" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arCycleStageDto.enhancedCounselling == null ? 'NA' : arCycleStageDto.enhancedCounselling ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="AR Practitioner" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.practitioner}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Embryologist" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.embryologist}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Was a donor's Oocyte(s)/Embryo(s)/Sperms used in this cycle?" />
                    <iais:value width="7" cssClass="col-md-7"  display="true">
                        <c:out value="${arCycleStageDto.usedDonorOocyte ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<c:set var="donorDtos" value="${arCycleStageDto.donorDtos}"/>
<%@include file="previewDonorSection.jsp"%>
