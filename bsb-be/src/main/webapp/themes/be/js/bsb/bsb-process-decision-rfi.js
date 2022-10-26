$(function (){
    $("#submitAppRfiBtn").click(function () {
        if(validateCheckBox()){
            // var mainForm = document.getElementById("mainForm");
            // mainForm.submit();
            callAjaxSubmit();
        }else{
            $('#errorMessage').removeClass("hidden");
        }
    });
})

function callAjaxSubmit(){
    $('#errorMessage').addClass("hidden");
    var form = $('#mainForm').serialize();
    $.ajax({
        type: "post",
        url:  "/bsb-web/callRfiSubmit",
        data: form,
        dataType: "text",
        success: function (data) {
            $('#selectDetail',window.opener.document).html(data);
            $('#rfiSelect',window.opener.document).show();
            window.close();
        },
        error: function (msg) {
        }
    });
}

function validateCheckBox(){
    var flag = false;
    if($("input:checkbox[name='selectCheckbox']:checked").length > 0){
        flag = true;
    }
    return flag;
}