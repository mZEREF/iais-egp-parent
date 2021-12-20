$(document).ready(function() {
    if ("1" == $('#showValidatePT').val()) {
        $('#validatePT').modal('show');
    } else if ("1" == $('#showValidatePT').val()) {
        $('#noFoundDiv').modal('show');
    }
});

function clearSelection(){
    clearErrorMsg();
    $('#patientName').find('p').text('');
    $('#undergoingCycleCycle').find('p').text('');
    $('#lastStage').find('p').text('');
    clearFields('.selectionHidden');
    clearFields('#stage');
}

function retrieveValidatePatient() {
    showWaiting();
    var idType = $('#patientIdType').val();
    var idNo = $('input[name="patientIdNumber"]').val();
    var nationality = $('#patientNationality').val();
    var url = $('#_contextPath').val() + '/ar/retrieve-valid-selection';
    var options = {
        idType: idType,
        idNo: idNo,
        nationality: nationality,
        url: url
    }
    callCommonAjax(options, validatePatientCallback);
}

function validatePatientCallback(data){
    clearErrorMsg();
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
    $('#lastStatusHidden').val(data.selection.lastStatus);
    if (!isEmpty(data.selection.lastCycleDto)) {
        $('#lastStatusHidden').val(data.selection.lastCycleDto.cycleType);
        $('#cycleIdHidden').val(data.selection.lastCycleDto.id);
    } else {
        $('#lastStatusHidden').val('');
        $('#cycleIdHidden').val('');
    }
}