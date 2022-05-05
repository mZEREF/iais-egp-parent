$(function () {
    $("#saveBtn").click(function () {
        showWaiting();
        $("input[name='action_value']").val("submitDoc");
        $("#mainForm").submit();
    });
});