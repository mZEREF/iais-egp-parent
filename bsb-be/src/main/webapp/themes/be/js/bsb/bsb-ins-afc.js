$(function () {
    $("#submitBtn").click(function () {
        showWaiting();
        $("input[name='action_type']").val("submit");
        $('#mainForm').submit();
    })
    $("#saveReportBtn").click(function () {
        showWaiting();
        $("input[name='action_type']").val("saveReport");
        $("input[name='action_value']").val("savaDoc");
        $("#mainForm").submit();
    });
})