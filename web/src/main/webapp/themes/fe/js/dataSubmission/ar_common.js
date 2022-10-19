$(document).ready(function() {
    if($('#saveDraftSuccess').val()=='success' && $('#saveDraft').length > 0){
        $('#saveDraft').modal('show');
        setTimeout(function() {
            $('#saveDraftSuccess').val('');
        }, 2000);
    }
    // draft modal
    var $draft = $("#_draftModal");
    if ($draft.length > 0) {
        $draft.modal('show');
    }
    // rfc
    showPopCommon('#rfcNoChangeShow','#rfcNoChangeModal',1);

    var currPage = $('input[name="ar_page"]').val();
    console.log('----- ' + currPage + ' -----');
    if (isEmpty(currPage)) {
        currPage = "";
    }
    if ($('#backBtn').length > 0) {
        $('#backBtn').click(function () {
            showWaiting();
            if ('preview' == currPage) {
                submit('page');
            } else if ('ar-submission' == currPage) {
                submit('back');
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
                submit('draft');
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

function submit(action,value,additional){
    $("[name='crud_type']").val(action);
    $("[name='crud_action_value']").val(value);
    $("[name='crud_action_additional']").val(additional);
    var mainForm = document.getElementById('mainForm');
    showWaiting();
    mainForm.submit();
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
};

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

function showPopCommon(controlId,showPopId,val){
     let haveControlJq = controlId.indexOf("#") >= 0;
     let haveShowPopIdJq = showPopId.indexOf("#") >= 0;
    if(!haveControlJq && $(controlId).length == 0 ){
        controlId = '#'+controlId;
    }
    if(!haveShowPopIdJq && $(showPopId).length == 0){
        showPopId = '#' + showPopId;
    }
    if($(controlId).val() == val){
        $(showPopId).modal('show');
    }
}