$(function () {
    $("#saveReportBtn").click(function () {
        showWaiting();
        $("input[name='action_type']").val("saveReport");
        $("#mainForm").submit();
    });

    if ($("#afterSaveAsReport").val() === 'true') {
        $('#afterSaveReport').modal('show');
    }
})

function cancelJumpAfterReport() {
    $('#afterSaveReport').modal('hide');
}

function jumpAfterReport() {
    window.location.href = "/bsb-web/eservice/INTRANET/MohBsbTaskList";
}