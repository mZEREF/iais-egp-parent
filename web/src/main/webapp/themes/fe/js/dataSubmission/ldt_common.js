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
    if($('#isRfc').val() === 'true'){
        disabledPartPage($('#disabledPart'))
        // if($('#testStatus').val() === '0'){
        //     disabledPartPage($('#testStatusRow'))
        // }
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

function disabledPartPage($Ele) {
    $Ele.find('div.nice-select').addClass('disabled');
    $Ele.find('input[type="text"]').css('border-color','#ededed');
    $Ele.find('input[type="text"]').css('color','#999');
    $Ele.find('input[type="radio"]').prop('disabled',true);
    $Ele.find('textarea').prop('disabled',true);
    $Ele.find('textarea').css('color','#999');
    $Ele.find('textarea').css('border-color','#ededed');
    $Ele.find('input[type="text"]').prop('disabled',true);
    $Ele.find('input[type="file"]').prop('disabled',true);
    $Ele.find('input[type="checkbox"]').prop('disabled',true);
}

function unDisabledPage(){
    $('input[type="radio"]').prop('disabled',false);
    $('input[type="text"]').prop('disabled',false);
    $('input[type="file"]').prop('disabled',false);
    $('input[type="checkbox"]').prop('disabled',false);
    $('textarea').prop('disabled',false);
    $('div.nice-select').removeClass('disabled');
    $('input[type="text"]').css('border-color','');
    $('input[type="text"]').css('color','');
}

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
    unDisabledPage();
    $("[name='crud_action_type']").val(action);
    var mainForm = document.getElementById('mainForm');
    showWaiting();
    mainForm.submit();
}

function printData() {
    clearErrorMsg();
    var url = $('#_contextPath').val() + '/eservice/INTERNET/MohDsPrint';
    var token = $('input[name="OWASP_CSRFTOKEN"]').val();
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