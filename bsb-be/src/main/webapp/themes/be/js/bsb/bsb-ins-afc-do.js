$(function () {
    $("#processingDecision").change(function () {
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO022") {
            $("#selectMohUserDiv").show();
        } else {
            $("#selectMohUserDiv").hide();
        }
    })
    $("#submitBtn").click(function () {
        showWaiting();
        $("input[name='action_type']").val("submit");
        $('#mainForm').submit();
    })
    $("#saveReportBtn").click(function () {
        showWaiting();
        $("input[name='action_type']").val("saveReport");
        $("#mainForm").submit();
    });
    var finalRound = $("input[name='finalRound']").val();
    if (finalRound === 'true') {
        $("#beforeSubmit").modal('show');
    }
})

function cancelBeforeSubmitModule(){
    $("input[name='haveConfirm']").val("Y");
    $('#beforeSubmit').modal('hide');
}