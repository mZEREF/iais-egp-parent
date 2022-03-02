$(function () {
    $("#submitButton").click(function () {
        showWaiting();
        $("#mainForm").submit();
    });
});