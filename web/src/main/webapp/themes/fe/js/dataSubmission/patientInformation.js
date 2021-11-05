$(document).ready(function() {
    $('input[name="birthDate"]').on('blur, change', function () {
        showWaiting();
        var birthDate = $(this).val();
        var url = $('#_contextPath').val() + '/ar/patient-age';
        var options = {
            birthDate: birthDate,
            url: url
        }
        callCommonAjax(options, checkBirthDateCallback);
    });
});

function checkBirthDateCallback(data) {
    if (isEmpty(data) || isEmpty(data.showAgeMsg) || !data.showAgeMsg) {
        return;
    }
    $('#ageMsgDiv').modal('show');
}

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
    if (isEmpty(data) || isEmpty(data.patient) || !isEmpty(data.errorMsg)) {
        $('#preName').find('p').text('');
        $('#preBirthDate').find('p').text('');
        $('[name="retrievePrevious"]').val('0');
        if (!isEmpty(data.errorMsg)) {
            doValidationParse(data.errorMsg);
        } else {
            $('#noPatientDiv').modal('show');
        }
        return;
    }
    $('[name="retrievePrevious"]').val('1');
    $('#preName').find('p').text(data.patient.name);
    $('#preBirthDate').find('p').text(data.patient.birthDate);
}

function getDataForPrinting() {
    var declaration = $('input[name="declaration"]:checked').val();
    if (isEmpty(declaration)) {
        return null;
    }
    var printflag = $('#printflag').val();
    if (isEmpty(printflag)) {
        printflag = '';
    }
    return {declaration: declaration, printflag: printflag};
}