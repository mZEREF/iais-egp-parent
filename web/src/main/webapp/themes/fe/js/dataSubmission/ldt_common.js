$(document).ready(function () {
    if ($('#saveDraftSuccess').val() == 'success') {
        $('#saveDraft').modal('show');
    }

    if ($('#_draftModal').length > 0) {
        $('#_draftModal').modal('show');
    }

    let currPage = $('input[name="current_page"]').val();
    if (currPage === "confirm") {
        $('#nextBtn').html('Submit');
    }

    $('.back').click(function () {
        if (currPage === "confirm") {
            submit('page');
        } else {
            submit('return');
        }
    })

    $('#nextBtn').click(function () {
        if (currPage == "confirm") {
            submit('submit');
        } else {
            submit('confirm');
        }
    })

    $('#saveDraftBtn').click(function () {
        submit('draft');
    })
})

function cancelDraft() {
    $('#saveDraft').modal('hide');
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

function submit(action) {
    $("[name='crud_action_type']").val(action);
    var mainForm = document.getElementById('mainForm');
    showWaiting();
    mainForm.submit();
}