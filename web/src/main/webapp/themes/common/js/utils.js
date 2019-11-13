function showWaiting() {
    $.blockUI({message: '<div style="padding:3px;">We are processing your request now, please do not click the Back or Refresh buttons in the browser.</div>',
        css: {width: '25%', border: '1px solid #aaa'},
        overlayCSS: {opacity: 0.2}});
}

function dismissWaiting() {
    $.unblockUI();
}

function doValidation(){
    var baseContextPath = $("#baseContextPath").val();
    clearErrorMsg();
    $.ajax({
        async: false,
        type: 'POST',
        url: baseContextPath + "/validation.do",
        data:$('form').serialize(),
        success: function(data){
            doValidationParse(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("error: " + textStatus);
            alert("error: " + errorThrown);
            $("#emsErrorFlag").val('Error:Exception');
        }
    });
}

function showErrorMsg(errorKey,errorMsg) {
    $('form').find("[id*="+errorKey.replace(/\./g,'\\.')+"]").html(formatHTMLEnCode(errorMsg));
}

function formatHTMLEnCode(s1){
    s1 = s1.replace(/\>/g,"&gt;");
    s1 = s1.replace(/\</g,"&lt;");
    s1 = s1.replace(/\"/g,"&quot;");
    s1 = s1.replace(/\'/g,"&acute;");
    return s1;
}