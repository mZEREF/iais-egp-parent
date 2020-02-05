
var Utils = {
    getFileName: function(o){
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    },

    submit: function (form, action, value) {
       SOP.Crud.cfxSubmit(form, action, value);
    },

    clearClickStatus: function () {

        $("input[type='radio']").removeAttr('checked')
        $("input[type='checkbox']").removeAttr('checked')
        $("input[type='text']").val("");
        $(".current").text("Please select");

    }
}

$(".btn-login-search").click(function () {
    var val = $(".btn-login-submit").value;
    console.log("==>>>>")
    SOP.Crud.cfxSubmit("mainForm", 'doQuery', val);
})

function jumpToPagechangePage(){
    SOP.Crud.cfxSubmit("mainForm", "changePage");
}

function sortRecords(sortFieldName,sortType){
    SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
}