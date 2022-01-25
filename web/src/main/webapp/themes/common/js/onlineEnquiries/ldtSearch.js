function doClear() {
    $('input[type="text"]').val("");
    $('input[type="radio"]').prop("checked", false);
    $('.date_picker').val("");
}


function jumpToPagechangePage() {
    search();
}

function doSearch() {
    $('input[name="pageJumpNoTextchangePage"]').val(1);
    search();
}

function search() {
    showWaiting();
    $('#mainForm').submit();
}

function sortRecords(sortFieldName, sortType) {
    $("[name='crud_action_value']").val(sortFieldName);
    $("[name='crud_action_additional']").val(sortType);
    $('#mainForm').submit();
}