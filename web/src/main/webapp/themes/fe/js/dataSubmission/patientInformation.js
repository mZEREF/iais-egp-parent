function retrieveIdentification() {
    showWaiting();
    var idType = $('#preIdType').val();
    var idNo = $('input[name="preIdNumber"]').val();
    var nationality = $('#preNationality').val();
    var url = $('#_contextPath').val() + '/ar/retrieve-identification';
    var options = {
        idType: idType,
        idNo: idNo,
        nationality: nationality,
        url: url
    }
    callCommonAjax(options, previousPatientCallback);
}

function previousPatientCallback(data) {
    clearErrorMsg();
    if (isEmpty(data) || isEmpty(data.patient) || !isEmpty(data.errorMsg) || data.invalidType) {
        $('#preName').find('p').text('');
        $('#preBirthDate').find('p').text('');
        $('[name="retrievePrevious"]').val('0');
        if (!isEmpty(data.errorMsg)) {
            doValidationParse(data.errorMsg);
        } else if (data.invalidType) {
            showErrorMsg('error_preIdType', $('#_ERR0051').val());
        } else {
            $('#noFoundDiv').modal('show');
        }
        return;
    }
    $('[name="retrievePrevious"]').val('1');
    $('#preName').find('p').text(data.patient.name);
    $('#preBirthDate').find('p').text(data.patient.birthDate);
}