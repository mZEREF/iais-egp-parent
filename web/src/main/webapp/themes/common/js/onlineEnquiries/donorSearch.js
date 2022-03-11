function doClear() {
    $('input[type="text"]').val("");
    $('input[type="checkbox"]').prop("checked", false);
    $("select option").prop("selected", false);
    $(".clearSel").children(".current").text("Please Select");
    $('#othersSampleHciCode').attr("style","display: none");

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


var fullDetailsView = function (submissionNo,sampleHciCode) {

    showWaiting();
    $("[name='crud_action_additional']").val(sampleHciCode);
    $("[name='crud_action_value']").val(submissionNo);
    $("[name='crud_action_type']").val('viewInfo');
    $('#mainForm').submit();
}

$(document).ready(function () {
    $('#sampleHciCode').change(function () {

        var reason= $('#sampleHciCode option:selected').val();

        if("AR_SC_001"==reason){
            $('#othersDisplay').attr("style","display: block");
        }else {
            $('#othersDisplay').attr("style","display: none");
        }

    });
})