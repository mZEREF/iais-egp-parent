$(function () {
    $("#submitBtn").click(function (){
        showWaiting();
        $("[name='action_type']").val("doSave");
        $("#mainForm").submit();
    });

    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });
})