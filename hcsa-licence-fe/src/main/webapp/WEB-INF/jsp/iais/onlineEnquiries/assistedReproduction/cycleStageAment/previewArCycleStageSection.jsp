<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts" %>
<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title" >
            <a class="collapsed" href="#viewArCycleStage" data-toggle="collapse" >
                Assisted Reproduction Submission
            </a>
        </h4>
    </div>
    <div id="viewArCycleStage" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="arCycleStageDto" value="${arSuperDataSubmissionDto.arCycleStageDto}" />
                <c:set var="arCycleStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.arCycleStageDto}" />
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
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <select id="oldDsSelect" name="oldDsSelect">
                            <c:forEach items="${arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}" var="oldDs" varStatus="index">
                                <option  <c:if test="${oldDs.dataSubmissionDto.id ==arSuperDataSubmissionDtoVersion.dataSubmissionDto.id }">checked</c:if> value ="${oldDs.dataSubmissionDto.id}">V ${oldDs.dataSubmissionDto.version}</option>
                            </c:forEach>
                        </select>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="Premises where AR is performed" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.premisesDto.premiseLabel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Date Started" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.startDate}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.startDate}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Patient's Age as of This Cycle" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.cycleAge}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.cycleAge}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="Main Indication" />
                    <iais:value width="4" cssClass="col-md-4"  display="true">
                        <iais:code code="${arCycleStageDto.mainIndication}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arCycleStageDtoVersion.mainIndication}"/>
                    </iais:value>
                </iais:row>

                <iais:row >
                    <iais:field width="4" value="Main Indication (Others)" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.mainIndicationOthers}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.mainIndicationOthers}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="Other Indication" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:forEach items="${arCycleStageDto.otherIndicationValues}" var="otherIndicationValue" varStatus="status">
                            <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${otherIndicationValue}"/>
                        </c:forEach>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:forEach items="${arCycleStageDtoVersion.otherIndicationValues}" var="otherIndicationValue" varStatus="status">
                            <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${otherIndicationValue}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>
                <iais:row >
                    <iais:field width="4" value="Other Indication (Others)" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.otherIndicationOthers}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.otherIndicationOthers}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="In-Vitro Maturation" />
                    <iais:value width="4" cssClass="col-md-3" display="true">
                        <c:out value="${arCycleStageDto.inVitroMaturation ? 'Yes' : 'No'}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.inVitroMaturation ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="Current AR Treatment" />
                    <iais:value width="4" cssClass="col-md-4"  display="true">
                        <c:forEach items="${arCycleStageDto.currentArTreatmentValues}" var="currentAR" varStatus="status">
                            <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${currentAR}"/>
                        </c:forEach>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:forEach items="${arCycleStageDtoVersion.currentArTreatmentValues}" var="currentAR" varStatus="status">
                            <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${currentAR}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="No. of Children from Current Marriage" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.currentMarriageChildren}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.currentMarriageChildren}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="No. of Children from Previous Marriage" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.previousMarriageChildren}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.previousMarriageChildren}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="No. of Children conceived through AR" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.deliveredThroughChildren}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.deliveredThroughChildren}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <label class="col-xs-4 col-md-4 control-label">Total No. of AR cycles previously undergone by patient
                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="${MessageUtil.getMessageDesc("DS_ACK002")}"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:optionText value="${arCycleStageDto.totalPreviouslyPreviously}" selectionOptions="numberArcPreviouslyDropDown"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:optionText value="${arCycleStageDtoVersion.totalPreviouslyPreviously}" selectionOptions="numberArcPreviouslyDropDown"/>
                    </iais:value>
                </iais:row>

                <iais:row >
                    <iais:field width="4" value="No. of AR Cycles undergone Overseas" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.cyclesUndergoneOverseas}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.cyclesUndergoneOverseas}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of AR Cycles undergone Locally" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.numberOfCyclesUndergoneLocally}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.numberOfCyclesUndergoneLocally}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <c:if test="${empty DS_ERR018Tip}">
                        <iais:field width="4" value="Enhanced Counselling" />
                    </c:if>
                    <c:if test="${!empty DS_ERR018Tip}">
                        <label class="col-xs-4 col-md-4 control-label">Enhanced Counselling
                            <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                               title="${DS_ERR018Tip}"
                               style="z-index: 10"
                               data-original-title="">i</a>
                        </label>
                    </c:if>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.enhancedCounselling == null ? '' : arCycleStageDto.enhancedCounselling ? 'Yes' : 'No'}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.enhancedCounselling == null ? '' : arCycleStageDtoVersion.enhancedCounselling ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="AR Practitioner" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.practitioner}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.practitioner}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="Embryologist" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDto.embryologist}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.embryologist}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="4" value="Was a donor's Oocyte(s)/Embryo(s)/Sperms used in this cycle?" />
                    <iais:value width="4" cssClass="col-md-4"  display="true">
                        <c:out value="${arCycleStageDto.usedDonorOocyte ? 'Yes' : 'No'}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arCycleStageDtoVersion.usedDonorOocyte ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>


            </div>
        </div>
    </div>
</div>
<c:set var="donorDtos" value="${arCycleStageDto.donorDtos}"/>
<%@include file="previewDonorSection.jsp"%>

