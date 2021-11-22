<c:set var="selectionDto" value="${arSuperDataSubmissionDto.selectionDto}" />

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Cycle Stage Selection
            </strong>
        </h4>
    </div>
    <div id="cycleStageSectionPanel" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Patient ID No." mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:select name="patientIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                     value="${selectionDto.patientIdType}" cssClass="idTypeSel" onchange="clearSelection()"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:input maxLength="20" type="text" name="patientIdNumber" value="${selectionDto.patientIdNumber}"
                                    onchange="clearSelection()"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient Nationality" mandatory="true"/>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:select name="patientNationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                     value="${selectionDto.patientNationality}" cssClass="nationalitySel"
                                     onchange="clearSelection()"/>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3 patientData" display="true" id="retrieveDataDiv">
                        <a class="retrieveIdentification" onclick="retrieveValidatePatient()">
                            Validate Patient
                        </a>
                        <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_retrieveData"></span>
                        <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_patientName"></span>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="patientData">
                    <iais:field width="5" value="Patient Name"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="patientName">
                        ${selectionDto.patientName}
                    </iais:value>
                </iais:row>
                <iais:row cssClass="patientData">
                    <iais:field width="5" value="Is patient undergoing cycle currently?"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="undergoingCycleCycle">
                        <c:if test="${not empty selectionDto.patientName}">
                            ${selectionDto.undergoingCycle ? 'Yes' : 'No'}
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="patientData">
                    <iais:field width="5" value="Last Stage Submitted"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="lastStage">
                        <c:if test="${not empty selectionDto.lastStage && selectionDto.undergoingCycle}" var="hasLastStage">
                            <iais:code code="${selectionDto.lastStage}" />
                        </c:if>
                        <c:if test="${not empty selectionDto.patientName && !hasLastStage}" >
                            -
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Stage" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="stage" options="stage_options"
                                     value="${selectionDto.stage}" cssClass="stageSel"/>
                    </iais:value>
                </iais:row>
                <div class="selectionHidden">
                    <input type="hidden" name="retrieveData" value="${selectionDto.retrieveData}"/>
                    <input type="hidden" name="patientCode" value="${selectionDto.patientCode}"/>
                    <input type="hidden" name="patientName" id="patientNameHidden" value="${selectionDto.patientName}">
                    <input type="hidden" name="undergoingCycle" id="undergoingCycleHidden" value="${selectionDto.undergoingCycle ? '1' : '0'}">
                    <input type="hidden" name="lastCycle" id="lastCycleHidden" value="${selectionDto.lastCycle}">
                    <input type="hidden" name="lastStage" id="lastStageHidden" value="${selectionDto.lastStage}">
                    <input type="hidden" name="latestCycle" id="latestCycleHidden" value="${selectionDto.latestCycle}">
                    <input type="hidden" name="lastStatus" id="lastStatusHidden" value="${selectionDto.lastStatus}">
                </div>
            </div>
        </div>
    </div>
</div>
