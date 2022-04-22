$(function () {
    $("[data-type='reportInput']").removeAttr("disabled");

    $(".nice-select").removeClass("disabled");

    $("#saveReportBtn").click(function () {
        showWaiting();
        $("[data-type='reportInput']").removeAttr("disabled");
        $(".nice-select").removeClass("disabled");
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
    window.location.href = "/bsb-be/eservice/INTRANET/MohBsbTaskList";
}