$(document).ready(function(){
    $(':input', '#accordion').prop('disabled', true);
});

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
            url += '?';
        } else {
            url += '&';
        }
        url += 'printflag=' + printflag;
    }
    window.open(url,'_preview');
}