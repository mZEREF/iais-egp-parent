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

function validateOtherDocType() {
    var allSelected = true;
    $("#upload-other-doc-gp").find("select.other-doc-type-dropdown").each(function () {
        var docId = this.id.substring("docType".length);
        if (this.value) {
            $("span[data-err-ind=" + docId + "]").text("");
        } else {
            allSelected = false;
            $("span[data-err-ind=" + docId + "]").text("Please select one document type");
        }
    });
    if (!allSelected) {
        dismissWaiting();
    }
    return allSelected;
}

