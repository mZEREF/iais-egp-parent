$(function () {
    $("#searchBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("#mainForm").submit();
    });


    $("#clearBtn").click(function() {
        $('input[name="searchFacName"]').val("");
        $('input[name="searchFacAddr"]').val("");
        $('input[name="searchAppNo"]').val("");
        $('input[name="searchAppDateFrom"]').val("");
        $('input[name="searchAppDateTo"]').val("");
        $("#searchFacType option:first").prop("selected", 'selected');
        $("#searchProcessType option:first").prop("selected", 'selected');
        $("#searchAppType option:first").prop("selected", 'selected');
        $("#searchAppStatus option:first").prop("selected", 'selected');
    });


    $("#commonRoleId").change(function () {
        showWaiting();
        $("[name='action_type']").val("changeRole");
        $("[name='action_value']").val(this.value);
        $("#mainForm").submit();
    });
});


function pickUpTask(id,appId) {
    showWaiting();
    $("[name='action_type']").val("assign");
    $("[name='action_value']").val(id);
    $("[name='appId']").val(appId);
    $("#mainForm").submit();
}

function pickUpMultiTask() {
    if ($("input:checkbox:checked").length > 0) {
        showWaiting();
        $("[name='action_type']").val("multiassign");
        $("#mainForm").submit();
    } else {
        $('#multiAssignAlert').modal('show');
    }
}

function multiAssignCancel() {
    $('#multiAssignAlert').modal('hide');
}

function viewTaskDetail(id) {
    showWaiting();
    $("[name='action_type']").val("detail");
    $("[name='action_value']").val(id);
    $("#mainForm").submit();
}