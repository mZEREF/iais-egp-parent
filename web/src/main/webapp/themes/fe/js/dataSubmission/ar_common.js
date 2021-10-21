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
    if ($('#backBtn').length > 0) {
        $('#backBtn').click(function () {
            showWaiting();
            var currPage = $('input[name="ar_page"]').val();
            if ('preview' == currPage) {
                submit('page');
            } else {
                submit('return');
            }
        });
    }

    if ($('#saveDraftBtn').length > 0) {
        $('#saveDraftBtn').click(function () {
            showWaiting();
            submit('saveDraft');
        });
    }

    if ($('#nextBtn').length > 0) {
        $('#nextBtn').click(function () {
            showWaiting();
            var currPage = $('input[name="ar_page"]').val();
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