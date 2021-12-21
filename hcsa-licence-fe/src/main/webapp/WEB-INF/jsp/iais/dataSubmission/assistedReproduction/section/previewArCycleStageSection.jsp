<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts" %>
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
                <c:set var="donorDtos" value="${arCycleStageDto.donorDtos}"/>
                <%@include file="patientCommon.jsp"%>
                <iais:row>
                    <iais:field width="5" value="Premises where AR is performed" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date Started" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                    <c:out value="${arCycleStageDto.startDate}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age as of This Cycle" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                   <c:out value="${arCycleStageDto.cycleAge}"/>
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
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.currentMarriageChildren}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children from Previous Marriage" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.previousMarriageChildren}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children conceived through AR" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.deliveredThroughChildren}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <label class="col-xs-4 col-md-4 control-label">Total No. of AR cycles previously undergone by patient
                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="${DSACK002Message}"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:optionText value="${arCycleStageDto.totalPreviouslyPreviously}" selectionOptions="numberArcPreviouslyDropDown"/>
                        </iais:value>
                </iais:row>

                <iais:row id="totalNumberARCOtherRow" style="${ arCycleStageDto.totalPreviouslyPreviously == 21 ? '' : 'display: none'}">
                    <iais:field width="5" value="No. of AR Cycles undergone Overseas" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arCycleStageDto.cyclesUndergoneOverseas}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of AR Cycles undergone Locally" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.numberOfCyclesUndergoneLocally}"/>
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
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.practitioner}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Embryologist" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arCycleStageDto.embryologist}"/>
                        </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Was a donor's Oocyte(s)/Embryo(s)/Sperms used in this cycle?" />
                    <iais:value width="7" cssClass="col-md-7"  display="true">
                        <c:out value="${arCycleStageDto.usedDonorOocyte ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>
            </div>
            <c:if test="${empty donorDtos}">
                <%@include file="../common/patientInventoryTable.jsp" %>
            </c:if>
        </div>
    </div>
</div>
<%@include file="previewDonorSection.jsp"%>

