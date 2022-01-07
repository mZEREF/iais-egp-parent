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
                <iais:row>
                    <iais:field width="5" value="Cycle Start Date" mandatory="true" id="cycleStartLabel"/>
                    <iais:value width="7" cssClass="col-md-7" id="cycleStartDiv">
                        <c:if test="${not empty cycleStart_options}" var="hasCycleStartOpts">
                            <iais:select name="cycleStart" options="cycleStart_options" cssClass="cycleStartSel"
                                         value="${selectionDto.cycleStart}" onchange="retriveCycleStageSelection()" />
                        </c:if>
                        <c:if test="${not hasCycleStartOpts}">
                            -
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="patientData">
                    <iais:field width="5" value="Last Stage Submitted"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="lastStage">
                        <iais:code code="${selectionDto.lastStage}" needEscapHtml="false" viewEmptyStr="true"/>
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
                    <input type="hidden" name="latestStage" id="latestStageHidden" value="${selectionDto.latestStage}">
                    <input type="hidden" name="additionalStage" id="additionalStageHidden" value="${selectionDto.additionalStage}">
                    <input type="hidden" name="lastStatus" id="lastStatusHidden" value="${selectionDto.lastStatus}">
                    <input type="hidden" name="cycleId" id="cycleIdHidden" value="${selectionDto.lastCycleDto.id}">
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        checkCycleStart();
    });

    function validatePatientCallback(data){
        clearErrorMsg();
        // Cycle Start Date
        $('#cycleStartDiv').html(data.cycleStartHtmls);
        $('#cycleStartDiv').find('select').niceSelect();
        checkCycleStart();
        // stage options
        $('#stage').html(data.stagHtmls);
        $('#stage').niceSelect("update");
        // check
        if (isEmpty(data) || isEmpty(data.selection) || isEmpty(data.selection.patientName) || !isEmpty(data.errorMsg)) {
            clearSelection();
            if (!isEmpty(data.errorMsg)) {
                doValidationParse(data.errorMsg);
            } else {
                $('#noFoundDiv').modal('show');
            }
            return;
        }
        $('#patientName').find('p').text(data.selection.patientName);
        if (data.selection.undergoingCycle) {
            $('#undergoingCycleCycle').find('p').text('Yes');
            $('#undergoingCycleHidden').val('1');
        } else {
            $('#undergoingCycleCycle').find('p').text('No');
            $('#undergoingCycleHidden').val('0');
        }
        $('#lastStage').find('p').text(data.selection.lastStageDesc);
        $('[name="retrieveData"]').val('1');
        $('[name="patientCode"]').val(data.selection.patientCode);
        $('#patientNameHidden').val(data.selection.patientName);
        $('#lastCycleHidden').val(data.selection.lastCycle);
        $('#lastStageHidden').val(data.selection.lastStage);
        $('#latestCycleHidden').val(data.selection.latestCycle);
        $('#latestStageHidden').val(data.selection.latestStage);
        $('#additionalStageHidden').val(data.selection.additionalStage);
        $('#lastStatusHidden').val(data.selection.lastStatus);
        if (!isEmpty(data.selection.lastCycleDto)) {
            $('#cycleIdHidden').val(data.selection.lastCycleDto.id);
        } else {
            $('#cycleIdHidden').val('');
        }
    }

    function checkCycleStart() {
        var $target = $('#cycleStartLabel');
        $target.find('.mandatory').remove();
        if ($('#cycleStartDiv').find('select').length > 0) {
            $target.append('<span class="mandatory">*</span>');
        }
    }

    function retriveCycleStageSelection(){
        showWaiting();
        var cycleStart = $('#cycleStart').val();
        var patientCode = $('[name="patientCode"]').val();
        var url = $('#_contextPath').val() + '/ar/retrieve-cycle-selection';
        var options = {
            cycleStart: cycleStart,
            patientCode: patientCode,
            url: url
        }
        callCommonAjax(options, retriveCycleStageSelectionCallback);
    }

    function retriveCycleStageSelectionCallback(data) {
        clearErrorMsg();
        // stage options
        $('#stage').html(data.stagHtmls);
        $('#stage').niceSelect("update");
        $('#lastStage').find('p').text(data.selection.lastStageDesc);
        $('[name="retrieveData"]').val('1');
        $('[name="patientCode"]').val(data.selection.patientCode);
        $('#patientNameHidden').val(data.selection.patientName);
        $('#lastCycleHidden').val(data.selection.lastCycle);
        $('#lastStageHidden').val(data.selection.lastStage);
        $('#additionalStageHidden').val(data.selection.additionalStage);
        $('#lastStatusHidden').val(data.selection.lastStatus);
    }

</script>