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
        $('[name="retrieveData"]').val('0');
        $('[name="patientCode"]').val('');
        $('#patientName').find('p').text('');
        clearFields('#patientNameHidden');
        $('#undergoingCycleCycle').find('p').text('');
        clearFields('#undergoingCycleHidden');
        $('#lastStage').find('p').text('');
        clearFields('#lastStageHidden');
        if (!isEmpty(data.errorMsg)) {
            doValidationParse(data.errorMsg);
        } else {
            $('#noFoundDiv').modal('show');
        }
        return;
    }
    $('[name="retrieveData"]').val('1');
    $('[name="patientCode"]').val(data.selection.patientCode);
    $('#patientName').find('p').text(data.selection.patientName);
    $('#patientNameHidden').val(data.selection.patientName);
    if (data.selection.undergoingCycle) {
        $('#undergoingCycleCycle').find('p').text('Yes');
        $('#undergoingCycleHidden').val('1');
    } else {
        $('#undergoingCycleCycle').find('p').text('No');
        $('#undergoingCycleHidden').val('0');
    }
    if (isEmpty(data.selection.lastStageDesc)) {
        $('#lastStage').find('p').text('-');
    } else {
        $('#lastStage').find('p').text(data.selection.lastStageDesc);
    }
    $('#lastStageHidden').val(data.selection.lastStage);
}