function doClear() {
    $('input[type="text"]').val("");
    $('input[type="checkbox"]').prop("checked", false);
    $("option:first").prop("selected", 'selected');
    $(".current").text("Please Select");
    $('.date_picker').val("");
    $(".multi-select-button").html("-- Select --");
    $('#cycleStageDisplay').attr("style","display: none");
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
    $("[name='crud_action_type']").val('search');
    $('#mainForm').submit();
}

function sortRecords(sortFieldName, sortType) {
    $("[name='crud_action_value']").val(sortFieldName);
    $("[name='crud_action_additional']").val(sortType);
    $("[name='crud_action_type']").val('search');
    $('#mainForm').submit();
}


var fullDetailsView = function (submissionNo) {

    showWaiting();
    $("[name='crud_action_value']").val(submissionNo);
    $("[name='crud_action_type']").val('viewInfo');
    $('#mainForm').submit();
}