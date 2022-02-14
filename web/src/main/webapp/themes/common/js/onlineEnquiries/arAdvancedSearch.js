function doArBack() {
    showWaiting();
    $("[name='adv_action_type']").val('back');
    $('#mainForm').submit();
}

function doClear() {
    $('input[type="text"]').val("");
    $('input[type="number"]').val("");
    $('input[type="radio"]').prop("checked", false);
    $('input[type="checkbox"]').prop("checked", false);
    $("option:first").prop("selected", 'selected');
    $(".clearMultiSel").prop("selected", false);
    $(".clearSel").children(".current").text("Please Select");
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
    $("[name='adv_action_type']").val('search');
    $('#mainForm').submit();
}

function sortRecords(sortFieldName, sortType) {
    $("[name='crud_action_value']").val(sortFieldName);
    $("[name='crud_action_additional']").val(sortType);
    $("[name='adv_action_type']").val('search');
    $('#mainForm').submit();
}


var fullDetailsView = function (patientCode) {

    showWaiting();
    $("[name='crud_action_additional']").val('patient');
    $("[name='crud_action_value']").val(patientCode);
    $("[name='adv_action_type']").val('viewFull');
    $('#mainForm').submit();
}