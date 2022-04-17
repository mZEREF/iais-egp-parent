$(document).ready(function() {
    // tab click
    var controlLi = $('#controlLi').val();
    var $tarSel = $('#'+controlLi+'li');
    if ($tarSel.length > 0) {
        $tarSel.addClass('active');
        if ($tarSel.attr("class").match("active")){
            $tarSel.removeClass("incomplete");
            $tarSel.removeClass("complete");
        }
    }
    $('#nav-tabs-ul a').on('click', function() {
        var $target = $(this);
        var currId = $target.attr('id');
        var canClick = $target.data('config-click');
        if (isEmpty(canClick)) {
            canClick = $target.attr('data-config-click');
        }
        var configCode = $target.data('config-code');
        if (isEmpty(configCode)) {
            configCode = $target.attr('data-config-code');
        }
        console.info(currId);
        if (controlLi == currId) {
            return;
        } else if ('true' == canClick) {
            showWaiting();
            submit(configCode);
        }
    });

    if($('#saveDraftSuccess').val()=='success' && $('#saveDraft').length > 0) {
        $('#saveDraft').modal('show');
    }
    // draft modal
    var $draft = $("#_draftModal");
    if ($draft.length > 0) {
        $draft.modal('show');
    }
    var currPage = $('input[name="top_page"]').val();
    console.log('----- ' + currPage + ' -----');
    if (isEmpty(currPage)) {
        currPage = "";
    }
    if ($('#backBtn').length > 0) {
        $('#backBtn').click(function () {
            showWaiting();
            submit('previous');
        });
    }

    if ($('#saveDraftBtn').length > 0) {
            $('#saveDraftBtn').click(function () {
                showWaiting();
                submit(currPage, 'draft');
            });
    }

    if ($('#nextBtn').length > 0) {
        if ('preview' == currPage) {
            $('#nextBtn').html('Submit');
        } else if ('confim' == currPage) {
            $('#nextBtn').html('Preview');
        } else if ('top-submission' == currPage){
            $('#nextBtn').html('Proceed');
            $('#saveDraftBtn').remove();
        }
        $('#nextBtn').click(function () {
            showWaiting();
            submit('next');
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
}
