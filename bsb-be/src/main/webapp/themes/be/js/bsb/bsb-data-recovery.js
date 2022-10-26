$(function () {
    $("#searchBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("#mainForm").submit();
    });

    $("#clearBtn").click(function() {
        $('input[name="searchModuleName"]').attr("value","");
        $('input[name="searchFunctionName"]').attr("value","");
        $('input[name="searchUserName"]').attr("value","");
        $('input[name="searchCreateDateFrom"]').attr("value","");
        $('input[name="searchCreateDateTo"]').attr("value","");
    });
});

function viewDataRecovery(maskedDataRecoveryId) {
    showWaiting();
    $("[name='maskedDataRecoveryId']").val(maskedDataRecoveryId);
    $("[name='action_type']").val("view");
    $("#mainForm").submit();
}

function recoverData(maskedDataRecoveryId) {
    showWaiting();
    $("[name='maskedDataRecoveryId']").val(maskedDataRecoveryId);
    $("[name='action_type']").val("recoverData");
    $("#mainForm").submit();
}