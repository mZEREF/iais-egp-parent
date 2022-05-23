$(function () {
    $("#submitBtn").click(function () {
        showWaiting();
        $("input[name='action_value']").val("submitDoc");
        $('#mainForm').submit();
    })
    $("#saveReportBtn").click(function () {
        showWaiting();
        $("input[name='action_value']").val("savaDoc");
        $("#mainForm").submit();
    });
})