function retrieveIdentification() {
    var idNo = $('input[name="preIdNumber"]').val();
    var nationality = $('#preNationality').val();
    var options = {
        idNo: idNo,
        nationality: nationality
    }
    callCommonAjax(options, previousPatientCallback);
}

function retrieveIdentification() {
    var idType = $('#patientIdType').val();
    var idNo = $('input[name="preIdNumber"]').val();
    var nationality = $('#preNationality').val();
    var options = {
        idType: idType,
        idNo: idNo,
        nationality: nationality,
        url: '${pageContext.request.contextPath}/ar/retrieve-identification'
    }
    callCommonAjax(options, previousPatientCallback);
}

function previousPatientCallback(data) {
    if (isEmpty(data)) {
        $('#preName').find('p').text('');
        $('#preBirthDate').find('p').text('');
        $('[name="retrievePrevious"]').val('0');
        $('#noFoundDiv').modal('show');
        return;
    }
    $('[name="retrievePrevious"]').val('1');
    $('#preName').find('p').text(data.name);
    $('#preBirthDate').find('p').text(data.birthDate);
}