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

    // rfc
    showPopCommon('#rfcNoChangeShow','#rfcNoChangeModal',1);

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

function printData() {
    clearErrorMsg();
    var url = $('#_contextPath').val() + '/eservice/INTERNET/MohDsPrint';
    var token = $('input[name="OWASP_CSRFTOKEN"]').val();
    var title = $('input[name="title"]').val();
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
    if (!isEmpty(title)) {
        if (url.indexOf('?') < 0) {
            url += '?title=' + title;
        } else {
            url += '&title=' + title;
        }
    }
    var data = getDataForPrinting();
    if (isEmpty(data)) {
        window.open(url, '_blank');
    } else {
        $.ajax({
            'url': $('#_contextPath').val() + '/ds/init-print',
            'dataType': 'json',
            'data': data,
            'type': 'POST',
            'success': function (data) {
                window.open(url, '_blank');
            },
            'error': function (data) {
                console.log("err: " + data);
            }
        });
    }
}

function getDataForPrinting() {
    var declaration = $('input[name="declaration"]:checked').val();
    if (isEmpty(declaration)) {
        declaration = '';
    }
    var printflag = $('#printflag').val();
    if (isEmpty(printflag)) {
        printflag = '';
    }
    return {declaration: declaration, printflag: printflag};
}

function showPopCommon(controlId,showPopId,val){
    if($(controlId).length == 0){
        controlId = '#'+controlId;
    }
    if($(showPopId).length == 0){
        showPopId = '#' + showPopId;
    }
    if($(controlId).val() == val){
        $(showPopId).modal('show');
    }
}