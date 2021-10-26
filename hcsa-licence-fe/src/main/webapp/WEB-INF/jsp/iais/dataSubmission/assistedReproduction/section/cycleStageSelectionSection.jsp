<c:set var="selectionDto" value="${AR_DATA_SUBMISSION.selectionDto}" />

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Cycle Stage Selection
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Patient ID No." mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:select name="patientIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                     value="${selectionDto.patientIdType}" cssClass="idTypeSel"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:input maxLength="20" type="text" name="patientIdNumber" value="${selectionDto.patientIdNumber}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient Nationality" mandatory="true"/>
                    <iais:value width="4" cssClass="col-md-7">
                        <iais:select name="patientNationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                     value="${selectionDto.patientNationality}" cssClass="nationalitySel"/>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3" display="true">
                        <a class="retrieveIdentification" onclick="retrieveIdentification('patientIdNumber', 'patientNationality',
                        'validatePatientCallback', {'flag': 'stageSlection'})">
                            Validate Patient
                        </a>
                        <input type="hidden" name="retrievePrevious" value="${not empty previous ? '1' : '0'}"/>
                    </iais:value>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_retrievePrevious"></span>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="patientName">
                        <input type="hidden" name="patientName" id="patientNameHidden" value="${patientNationality.patientName}">
                        ${selectionDto.patientName}
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is patient undergoing cycle currently?"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="undergoingCycleCycle">
                        <input type="hidden" name="undergoingCycle" id="undergoingCycleHidden" value="${selectionDto.undergoingCycle ? '1' : '0'}">
                        ${selectionDto.undergoingCycle ? 'Yes' : 'No'}
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Last Stage Submitted"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="lastStage">
                        <input type="hidden" name="lastStage" id="lastStageHidden" value="${selectionDto.lastStage}">
                        <iais:code code="${selectionDto.lastStage}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Stage" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="stage" options="stage_options"
                                     value="${selectionDto.stage}" cssClass="stageSel"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<iais:confirm msg="GENERAL_ACK018" callBack="$('#noFoundDiv').modal('hide');" popupOrder="noFoundDiv" />
<script type="text/javascript">
    function retrieveIdentification(idTag, nationalityTag, callback, options) {
        var idNo = '';
        if ($('#' + idTag).length > 0) {
            idNo = $('#' + idTag).val();
        } else if ($('[name="' + idTag + '"]').length > 0) {
            idNo = $('#' + idTag).val();
        }
        var nationality = '';
        if ($('#' + nationalityTag).length > 0) {
            nationality = $('#' + nationalityTag).val();
        } else if ($('[name="' + nationalityTag + '"]').length > 0) {
            nationality = $('#' + nationalityTag).val();
        }
        if (isEmpty(options)) {
            options = {};
        }
        options.idNo = idNo;
        options.nationality = nationality;

        $.ajax({
            url: '${pageContext.request.contextPath}/ar/retrieve-identification',
            dataType: 'json',
            data: options,
            type: 'POST',
            success: function (data) {
                if (typeof callback === 'function') {
                    callback(data);
                } else if (!isEmpty(callback)) {
                    callFunc(callback, data);
                }
                dismissWaiting();
            },
            error: function (data) {
                console.log("err");
                console.log(data);
                dismissWaiting();
            }
        });
    }

    function validatePatientCallback(data){
        if (isEmpty(data)) {
            $('patientName').find('p').html('');
            $('preBirthDate').find('p').html('');
            $('[name="retrievePrevious"]').val('0');
            $('#noFoundDiv').modal('show');
            return;
        }
    }
</script>