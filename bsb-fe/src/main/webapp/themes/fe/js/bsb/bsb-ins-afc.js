$(function () {
    $("#saveBtn").click(function () {
        showWaiting();
        $("input[name='action_value']").val("submitDoc");
        $("#mainForm").submit();
    });
    var finalRound = $("input[name='finalRound']").val();
    if (finalRound === 'true') {
        $("#beforeSubmit").modal('show');
    }
});
function cancelBeforeSubmitModule(){
    $("input[name='haveConfirm']").val("Y");
    $('#beforeSubmit').modal('hide');
}