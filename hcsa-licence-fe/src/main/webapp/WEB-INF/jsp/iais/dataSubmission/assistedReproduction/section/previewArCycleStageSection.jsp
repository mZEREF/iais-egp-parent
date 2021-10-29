<c:set var="headingSign" value="completed"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title" data-toggle="collapse" href="#viewArCycleStage">
            <strong>
                Assisted Reproduction Submission
            </strong>
        </h4>
    </div>
    <div id="viewArCycleStage" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="arCycleStageDto" value="${arSuperDataSubmissionDto.arCycleStageDto}" />
                <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
                <h3>
                    <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${patientDto.patientName}"/></label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;"><c:out value="${patientDto.patientIdNO}"/></label></p>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Premises where AR is performed" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value=""/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date Started" />
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value="${arCycleStageDto.startDate}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age as of This Cycle" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value="${arCycleStageDto.cycleAge}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Main Indication" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7"  label="true">
                       <iais:code code="${arCycleStageDto.mainIndication}"/>
                    </iais:value>
                </iais:row>

                <iais:row id="mainIndicationOtherRow">
                    <iais:field width="5" value="Main Indication (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7"  label="true">
                        <c:out value="${arCycleStageDto.mainIndicationOthers}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Other Indication" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:forEach items="${arCycleStageDto.otherIndicationValues}" var="otherIndicationValue" varStatus="status">
                            <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${otherIndicationValue}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>

                <iais:row id="otherIndicationOthersRow">
                    <iais:field width="5" value="Other Indication (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value="${arCycleStageDto.otherIndicationOthers}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="In-Vitro Maturation" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-3" label="true">
                        <c:out value="${arCycleStageDto.inVitroMaturation ? Yes : No}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Current AR Treatment" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                            <c:forEach items="${arCycleStageDto.currentARTreatmentValues}" var="currentAR" varStatus="status">
                                <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${currentAR}"/>
                            </c:forEach>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children from Current Marriage" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${arCycleStageDto.noChildrenCurrentMarriage}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children from Previous Marriage" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${arCycleStageDto.noChildrenPreviousMarriage}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children conceived through AR" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${arCycleStageDto.noChildrenConceivedAR}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children conceived through AR" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${arCycleStageDto.totalNumberARCPreviouslyUndergonePatient}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row id="totalNumberARCOtherRow">
                    <iais:field width="5" value="Number of Cycles undergone Overseas" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${arCycleStageDto.numberCyclesUndergoneOverseas}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Number of Cycles undergone Locally" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value=""/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Enhanced Counselling" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${arCycleStageDto.enhancedCounselling ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="AR Practitioner" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${arCycleStageDto.practitioner}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Embryologist" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${arCycleStageDto.embryologist}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Was a donor's Oocyte(s)/Embryo(s)/Sperms used in this cycle?" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${arCycleStageDto.oocyteEmbryoSpermsUsed ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
    <%@include file="previewDonorSection.jsp"%>
</div>

<%@include file="donorSection.jsp"%>