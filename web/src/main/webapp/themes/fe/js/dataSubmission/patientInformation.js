$(document).ready(function() {
    $('input[name="birthDateHbd"]').on('blur, change', function () {
        checkAge($(this).val(), 'hbdAgeMsgDiv');
    });

    $('input[name="birthDate"]').on('blur, change', function () {
        checkAge($(this).val(), 'ageMsgDiv');
    });
    if($('#saveDraftSuccess').val() != 'success') {
        $('input[name="birthDateHbd"]').trigger('change');
        $('input[name="birthDate"]').trigger('change');
    }
});

function checkAge(birthDate, modalId) {
    if (isEmpty(birthDate) || isEmpty(modalId) ) {
        console.log(modalId + " - " + birthDate);
        return;
    }
    showWaiting();
    var url = $('#_contextPath').val() + '/ar/patient-age';
    var options = {
        modalId: modalId,
        birthDate: birthDate,
        url: url
    }
    callCommonAjax(options, checkBirthDateCallback);
}

function checkBirthDateCallback(data) {
    if (isEmpty(data) || isEmpty(data.showAgeMsg) || isEmpty(data.modalId) || !data.showAgeMsg) {
        console.log("Data - " + JSON.stringify(data, undefined, 2));
        return;
    }
    $('.modal').modal('hide');
    $('#' + data.modalId).modal('show');
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