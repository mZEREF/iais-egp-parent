$(function (){
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


    var currPage = $('input[name="ar_page"]').val();
    console.log('----- ' + currPage + ' -----');
    if (isEmpty(currPage)) {
        currPage = "";
    }


    $("#backBtn").click(function (){
        submit("return");
    });

    $("#pt-amend").click(function (){
        submit("amend");
    });

    $("#saveDraftBtn").click(function (){
        submit("draft");
    });


    var $nextBtn = $("#nextBtn");
    if ($nextBtn.length > 0) {
        if('ar-submission' === currPage){
            $nextBtn.html('Submit');
        } else if('amend-patient' === currPage){
            $nextBtn.html('Amend');
        }

        $nextBtn.click(function () {
            showWaiting();
            if ('amend-patient' === currPage || 'ar-submission' === currPage) {
                submit('submission');
            }
        });
    }
});


function cancelDraft() {
    $('#saveDraft').modal('hide');
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