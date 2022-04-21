function doBack() {
    showWaiting();
    $("[name='crud_action_type']").val("back");
    $("#mainForm").submit();
    dismissWaiting();
}

function doSubmit() {
    showWaiting();
    $("[name='crud_action_type']").val("submit");
    $("#mainForm").submit();
    dismissWaiting();
}

