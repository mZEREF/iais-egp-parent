function showWaiting() {
    $.blockUI({message: '<div style="padding:3px;">We are processing your request now, please do not click the Back or Refresh buttons in the browser.</div>',
        css: {width: '25%', border: '1px solid #aaa'},
        overlayCSS: {opacity: 0.2}});
}

function dismissWaiting() {
    $.unblockUI();
}

function doValidation(){
    clearErrorMsg();
    $.ajax({
        async: false,
        type: 'POST',
        url: BASE_CONTEXT_PATH + "/validation.do",
        data:$('form').serialize(),
        success: function(data){
            doValidationParse(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("error: " + textStatus);
            alert("error: " + errorThrown);
            $("#iaisErrorFlag").val('Error:Exception');
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

function clearErrorMsg(){
    $("span[name='iaisErrorMsg']").each(function(){
        $(this).html("");
    });
}

function doValidationParse(data){
    if(data != "[]"){
        $("#iaisErrorFlag").val("BLOCK");
        var results = jQuery.parseJSON(data);

        for(var i= 0 ; i< results.length ; i ++){
            for(var key in results[i]){
                var error_key="error_" + key.replace(/\./g,'\\.');
                if (document.getElementById(error_key)) {
                    $("#"+error_key).show();
                    if (error_key == 'error_topErrorDiv'
                        || error_key.indexOf('noEscapeXml') > 0) {
                        document.getElementById(error_key).innerHTML = results[i][key];
                    } else {
                        document.getElementById(error_key).innerHTML = formatHTMLEnCode(results[i][key]);
                    }
                }
            }
        }
    }
}

function getErrorMsg(){
    var msgStatus = false;
    if($("#iaisErrorFlag").val() != null && $("#iaisErrorFlag").val() != ""){
        msgStatus =  true;
    }

    return msgStatus;
}