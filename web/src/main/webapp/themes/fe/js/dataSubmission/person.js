function retrieveIdentification() {
    var idNo = $('input[name="preIdNumber"]').val();
    var nationality = $('#preNationality').val();
    var options = {
        idNo: idNo,
        nationality: nationality
    }
    callCommonAjax(options, previousPatientCallback);
}

function retrieveValidatePatient() {
    showWaiting();
    var idType = $('#patientIdType').val();
    var idNo = $('input[name="patientIdNumber"]').val();
    var nationality = $('#patientNationality').val();
    var options = {
        idType: idType,
        idNo: idNo,
        nationality: nationality,
        url: '${pageContext.request.contextPath}/ar/retrieve-valid-selection'
    }
    callCommonAjax(options, validatePatientCallback);
}

function validatePatientCallback(data){
    clearErrorMsg();
    if (!isEmpty(data.errorMsg)) {
        doValidationParse(data.errorMsg);
        dismissWaiting();
        return;
    }
    $('[name="retrieveData"]').val('1');
    // stage options
    $('#stage').html(data.stagHtmls);
    $('#stage').niceSelect("update");
    // re-set other values
    if (isEmpty(data.selection)) {
        $('#patientName').find('p').text('');
        clearFields('#patientNameHidden');
        $('#undergoingCycleCycle').find('p').text('');
        clearFields('#undergoingCycleHidden');
        $('#lastStage').find('p').text('');
        clearFields('#lastStageHidden');
        $('#noFoundDiv').modal('show');
        dismissWaiting();
        return;
    }
    fillValue('#patientIdType', data.selection.patientIdType);
    $('#patientName').find('p').text(data.selection.patientName);
    $('#patientNameHidden').val(data.selection.patientName);
    if (data.selection.undergoingCycle) {
        $('#undergoingCycleCycle').find('p').text('Yes');
        $('#undergoingCycleHidden').val('1');
    } else {
        $('#undergoingCycleCycle').find('p').text('No');
        $('#undergoingCycleHidden').val('0');
    }
    $('#lastStage').find('p').text(data.selection.lastStageDesc);
    $('#lastStageHidden').val(data.selection.lastStage);
    dismissWaiting();
}