var Utils = {
    getFileName: function(o){
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    },

    submit: function (form, action, value) {
        SOP.Crud.cfxSubmit(form, action, value);
    },

    clearClickStatus: function () {
        $(".form-horizontal input").val("");
        $(".form-horizontal input[type='checkbox']").removeAttr('checked');
        $(".form-horizontal input[type='radio']").removeAttr('checked');
        $(".form-horizontal option[text = 'Please select']").val("selected", "selected");
        $(".current").text("Please select");
        $(".form-horizontal option").val("");
    },

    doExport: function(url){
        showWaiting();
        window.location.href = url;
        dismissWaiting();
    }
}

$(".btn-login-search").click(function () {
    var val = $(".btn-login-search").attr("value");
    if (val != null && val != ''){
        SOP.Crud.cfxSubmit("mainForm", val);
    }
})

$(".btn-login-cancel").click(function () {
    var val = $(".btn-login-cancel").attr("value");
    if (val != null && val != '') {
        SOP.Crud.cfxSubmit("mainForm", val);
    }
})

$(".btn-login-clear").click(function () {
    $(".form-horizontal input").val("");
    $(".form-horizontal input[type='checkbox']").removeAttr('checked');
    $(".form-horizontal input[type='radio']").removeAttr('checked');
    $(".form-horizontal option[text = 'Please select']").val("selected", "selected");
    $(".current").text("Please select");
    $(".form-horizontal option").val("");
})

$(".btn btn-primary cencel").click(function () {
    var val = $(".btn-login-cancel").value;
    SOP.Crud.cfxSubmit("mainForm", 'doCancel', val);
})


function jumpToPagechangePage(){
    SOP.Crud.cfxSubmit("mainForm", "changePage");
}

function sortRecords(sortFieldName,sortType){
    SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
}