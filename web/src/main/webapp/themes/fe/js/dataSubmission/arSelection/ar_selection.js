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
    let currPage = $('input[name="currentStage"]').val();
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
        $('#nextBtn').click(function () {
            if(!this.attr('disabled')){
                showWaiting();
                submit('submission');
            }
        });

        let currentStage = $('input[name="currentStage"]').val();
        if ('page' == currentStage) {
            $('#nextBtn').html('Submit');
        } else if ('confirm' == currPage){
            $('#nextBtn').html('Preview');
        }

        $('#nextBtn').attr('disabled', true);
    }
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
    const centreSelVal = $('#centreSel option:selected').val();
    const allContentDiv = $('#allContentDiv')
    if ($('#centreSel').length === 0 || !isEmpty(centreSelVal)){
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
    }
}

function showNextBtn(){
    let nextBtn = $('#nextBtn');
    const existedPatientVal = $('input[name="existedPatient"]').val();
    const submissionTypeVal = $('input[name="submissionType"]:checked').val();
    if (submissionTypeVal === 'AR_TP003' || !isEmpty(existedPatientVal)){
        nextBtn.attr('disabled', false)
    } else {
        $('#nextBtn').attr('disabled', true);
    }
}