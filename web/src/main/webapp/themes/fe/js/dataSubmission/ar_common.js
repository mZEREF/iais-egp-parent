$(document).ready(function() {
    if($('#saveDraftSuccess').val()=='success'){
        $('#saveDraft').modal('show');
    }
    function cancel() {
        $('#saveDraft').modal('hide');
    }

    function jumpPage() {
        submit('premises','saveDraft','jumpPage');
    }
    var currPage = $('input[name="ar_page"]').val();
    console.log('----- ' + currPage + ' -----');
    if (isEmpty(currPage)) {
        currPage = "";
    } else if ('ar-submission' == currPage) {
        clearFields('#accordion');
    }
    if ($('#backBtn').length > 0) {
        $('#backBtn').click(function () {
            showWaiting();
            if ('confirm' == currPage) {
                submit('page');
            } else {
                submit('return');
            }
        });
    }

    if ($('#saveDraftBtn').length > 0) {
        if ('ar-submission' == currPage) {
            $('#saveDraftBtn').remove();
        } else {
            $('#saveDraftBtn').click(function () {
                showWaiting();
                submit('saveDraft');
            });
        }
    }

    if ($('#nextBtn').length > 0) {
        if ('preview' == currPage) {
            $('#nextBtn').html('Submit');
        } else if ('stage' == currPage){
            $('#nextBtn').html('Preview');
        } else if ('ar-submission' == currPage){
            $('#nextBtn').html('Proceed');
        } else if ('cycle-stage-selection' == currPage){
            $('#nextBtn').html('Next');
        }
        $('#nextBtn').click(function () {
            showWaiting();
            if ('preview' == currPage) {
                submit('submission');
            } else {
                submit('confirm');
            }
        });
    }
});

function submit(action,value,additional){
    $("[name='crud_type']").val(action);
    $("[name='crud_action_value']").val(value);
    $("[name='crud_action_additional']").val(additional);
    var mainForm = document.getElementById('mainForm');
    showWaiting();
    mainForm.submit();
}

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
    var idNo = $('input[name="patientIdNumber"]').val();
    var nationality = $('#patientNationality').val();
    var options = {
        idNo: idNo,
        nationality: nationality,
        url: '/ar/retrieve-valid-selection'
    }
    callCommonAjax(options, validatePatientCallback);
}

function checkEthinicGroupMantory(nationTag, ethinicGroupLabel) {
    console.log("checkEthinicGroupMantory");
    var $selector = $(nationTag);
    var $target = $(ethinicGroupLabel);
    if ($selector.length <= 0 || $target.length <= 0) {
        return;
    }
    $target.find('.mandatory').remove();
    if ('NAT0001' == $selector.val()) {
        $target.append('<span class="mandatory">*</span>');
    }
}
