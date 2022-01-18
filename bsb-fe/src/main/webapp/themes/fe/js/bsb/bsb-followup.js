$(function (){
    $("#next").click(function () {
        showWaiting();
        $("input[name='action_type']").val("next");
        $("#mainForm").submit();

    });

    $("#submit").click(function () {
        showWaiting();
        $("input[name='action_type']").val("submit");
        $("#mainForm").submit();

    });

    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val("back");
        $("#mainForm").submit();
    });

    $("#saveDraft").click(function () {
        showWaiting();
        $("input[name='action_type']").val("draft");
        $("#mainForm").submit();
    });

})

function showImplement(cause){
    $("#isAllIdentified--v--"+cause).show();
}
function hideImplement(cause){
    $("#isAllIdentified--v--"+cause).hide();
}
