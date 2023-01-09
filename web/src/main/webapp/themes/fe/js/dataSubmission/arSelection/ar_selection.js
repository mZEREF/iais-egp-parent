$(function () {
    bindButton();

    $("#centreSel").change(showAllContentDiv).trigger('change');

    $("input[name='submissionMethod']").change(function () {
        showFormEntryDiv();
        showBatchUploadDiv();
    }).trigger('change');

    // checkbox donor sample
    $("input[name='submissionType']").change(function () {
        showDonorSampleDiv();
        showPatientDiv();
        showNextBtn();
    }).trigger('change');

    showDraftModal();
    showNextBtn();

    // only new patient and amend patient use
    $('input[name="birthDateHbd"]').on('blur, change', function () {
        checkHusbandAge($(this).val(), 'hbdAgeMsgDiv');
    });

    $('input[name="birthDate"]').on('blur, change', function () {
        checkAge($(this).val(), 'ageMsgDiv');
    });

    $('input[name="dateBirth"]').on('blur, change', function () {
        preCheckAge($(this).val(), 'preAgeMsgDiv');
    });
})

function reloadSection(selector) {
    $(selector).find("input[type = 'radio']").prop("checked", false).change();
    $(selector).find("input[type = 'text']").val("").change();
    $(selector).find("input[type = 'checkbox']").prop("checked", false).change();
    $(selector).find('select').val(null).change();
    $(selector).find('select').niceSelect("update");
}

function submit(action, value, additional) {
    $("[name='crud_type']").val(action);
    $("[name='crud_action_value']").val(value);
    $("[name='crud_action_additional']").val(additional);
    var mainForm = document.getElementById('mainForm');
    showWaiting();
    mainForm.submit();
}

function bindButton() {
    let currPage = $('input[name="currentPageStage"]').val();
    console.log('----- ' + currPage + ' -----');
    if (isEmpty(currPage)) {
        currPage = "";
    }
    if ($('#backBtn').length > 0) {
        $('#backBtn').click(function () {
            showWaiting();
            if ('confirm' === currPage) {
                submit('page');
            } else if ('ack' === currPage) {
                submit('back');
            } else {
                submit('return');
            }
        });
    }

    if ($('#saveDraftBtn').length > 0) {
        if ('ack' === currPage) {
            $('#saveDraftBtn').remove();
        } else {
            $('#saveDraftBtn').click(function () {
                showWaiting();
                submit('draft');
            });
        }
    }

    if ($('#nextBtn').length > 0) {
        $('#nextBtn').attr('disabled', true);
        let currentPageStage = $('input[name="currentPageStage"]').val();
        if ('page' == currentPageStage) {
            $('#nextBtn').html('Preview');
        } else if ('confirm' == currPage){
            $('#nextBtn').html('Submit');
        }
    }
}

function printData() {
    // window.print();
    clearErrorMsg();
    var url = $('#_contextPath').val() + '/eservice/INTERNET/MohDsPrint';
    var token = $('input[name="OWASP_CSRFTOKEN"]').val();
    const isRfc = $('input[name="isRfc"]').val() === 'true';
    const templateId = isRfc?'0CD6EB3F-B966-4B8A-8F3D-B5F3DB684650':'0CD6EB3F-B966-4B8A-8F3D-B5F3DB684650'
    if (!isEmpty(token)) {
        url += '?OWASP_CSRFTOKEN=' + token;
    }
    var printflag = $('#printflag').val();
    if (!isEmpty(printflag)) {
        if (url.indexOf('?') < 0) {
            url += '?printflag=' + printflag;
        } else {
            url += '&printflag=' + printflag;
        }
    }
    if (url.indexOf('?') < 0) {
        url += '?templateId=' + templateId;
    } else {
        url += '&templateId=' + templateId;
    }
    var data = getDataForPrinting();
    if (isEmpty(data)) {
        window.open(url,'_blank');
    } else {
        $.ajax({
            'url': $('#_contextPath').val() + '/ds/init-print',
            'dataType': 'json',
            'data': data,
            'type': 'POST',
            'success': function (data) {
                window.open(url,'_blank');
            },
            'error':function (data) {
                console.log("err: " + data);
            }
        });
    }
}

function getDataForPrinting() {
    var declaration = $('input[name="declaration"]:checked').val();
    if (isEmpty(declaration)) {
        declaration='';
    }
    var printflag = $('#printflag').val();
    if (isEmpty(printflag)) {
        printflag = '';
    }
    return {declaration: declaration, printflag: printflag};
}

function jumpToInbox() {
    showWaiting();
    var token = $('input[name="OWASP_CSRFTOKEN"]').val();
    var url = "/main-web/eservice/INTERNET/MohInternetInbox";
    if (!isEmpty(token)) {
        url += '?OWASP_CSRFTOKEN=' + token;
    }
    document.location = url;
}

function showDraftModal() {
    const saveDraft = $('#saveDraft');
    if ($('#saveDraftSuccess').val() === 'success' && saveDraft.length > 0) {
        saveDraft.modal('show');
        setTimeout(function () {
            $('#saveDraftSuccess').val('');
        }, 2000);
    }
    // draft modal
    var $draft = $("#_draftModal");
    if ($draft.length > 0) {
        $draft.modal('show');
    }
}

function showAllContentDiv() {
    let centreSelVal = $('#centreSel option:selected').val();
    if (!centreSelVal){
        centreSelVal = $('#centreSel').val();
    }
    const allContentDiv = $('#allContentDiv')
    if (!isEmpty(centreSelVal)){
        allContentDiv.show();
    } else {
        allContentDiv.hide();
        clearFields(allContentDiv);
        showFormEntryDiv();
        showBatchUploadDiv();
    }
}

function showFormEntryDiv() {
    const formEntryDiv = $('#formEntryDiv')
    const submissionMethodVal = $('input[name="submissionMethod"]:checked').val();
    if (submissionMethodVal === 'DS_MTD001') {
        formEntryDiv.show();
    } else {
        formEntryDiv.hide();
        clearFields(formEntryDiv);
        showDonorSampleDiv();
        showPatientDiv();
    }
}

function showBatchUploadDiv() {
    const batchUploadDiv = $('#batchUploadDiv')
    const submissionMethodVal = $('input[name="submissionMethod"]:checked').val();
    if (submissionMethodVal === 'DS_MTD002') {
        batchUploadDiv.show();
    } else {
        batchUploadDiv.hide();
        clearFields(batchUploadDiv);
    }
}

function showDonorSampleDiv() {
    const donorSampleDiv = $('#donorSampleDiv')
    const submissionTypeVal = $('input[name="submissionType"]:checked').val();
    if (submissionTypeVal === 'AR_TP003') {
        donorSampleDiv.show();
    } else {
        donorSampleDiv.hide();
        reloadSection(donorSampleDiv);
    }
}

function showPatientDiv() {
    const patientDiv = $('#patientDiv')
    const submissionTypeVal = $('input[name="submissionType"]:checked').val();
    if (submissionTypeVal === 'AR_TP001') {
        patientDiv.show();
    } else {
        patientDiv.hide();
        reloadSection(patientDiv);
        $('input[name="existedPatient"]').val(null).trigger('change');
    }
}

function showNextBtn(){
    let nextBtn = $('#nextBtn');
    let currPage = $('input[name="currentPageStage"]').val();
    const existedPatientVal = $('input[name="existedPatient"]').val();
    const submissionTypeVal = $('input[name="submissionType"]:checked').val();
    nextBtn.unbind('click')
    if ('page' !== currPage || submissionTypeVal === 'AR_TP003' || !isEmpty(existedPatientVal)) {
        nextBtn.attr('disabled', false)
        nextBtn.click(function () {
            showWaiting();
            submit('submission');
        });
    } else {
        nextBtn.attr('disabled', true);
    }
}

function preCheckAge(birthDate, modalId) {
    if (isEmpty(birthDate) || isEmpty(modalId) ) {
        console.log(modalId + " - " + birthDate);
        return;
    }
    var date=$('#dateBirth').val();
    let reg = /^(0?[1-9]|([1-2][0-9])|30|31)\/(1[0-2]|0?[1-9])\/(\d{4})$/;
    let validA = reg.test(date);
    if (validA) {
        showWaiting();
        var url = $('#_contextPath').val() + '/ar/patient-age';
        var options = {
            modalId: modalId,
            birthDate: birthDate,
            url: url
        }
        callCommonAjax(options, checkBirthDateCallback);
    }
}

function checkAge(birthDate, modalId) {
    if (isEmpty(birthDate) || isEmpty(modalId) ) {
        console.log(modalId + " - " + birthDate);
        return;
    }
    var date=$('#birthDate').val();
    let reg = /^(0?[1-9]|([1-2][0-9])|30|31)\/(1[0-2]|0?[1-9])\/(\d{4})$/;
    let validA = reg.test(date);
    if (validA) {
        showWaiting();
        var url = $('#_contextPath').val() + '/ar/patient-age';
        var options = {
            modalId: modalId,
            birthDate: birthDate,
            url: url
        }
        callCommonAjax(options, checkBirthDateCallback);
    }
}

function checkHusbandAge(birthDate, modalId) {
    if (isEmpty(birthDate) || isEmpty(modalId) ) {
        console.log(modalId + " - " + birthDate);
        return;
    }
    var date=$('#birthDateHbd').val();
    let reg = /^(0?[1-9]|([1-2][0-9])|30|31)\/(1[0-2]|0?[1-9])\/(\d{4})$/;
    let validA = reg.test(date);
    if (validA) {
        showWaiting();
        var url = $('#_contextPath').val() + '/ar/patient-age';
        var options = {
            modalId: modalId,
            birthDate: birthDate,
            url: url
        }
        callCommonAjax(options, checkBirthDateCallback);
    }
}

function checkBirthDateCallback(data) {
    if (isEmpty(data) || isEmpty(data.showAgeMsg) || isEmpty(data.modalId) || !data.showAgeMsg) {
        console.log("Data - " + JSON.stringify(data, undefined, 2));
        return;
    }
    $('.modal').modal('hide');
    $('#' + data.modalId).modal('show');
}