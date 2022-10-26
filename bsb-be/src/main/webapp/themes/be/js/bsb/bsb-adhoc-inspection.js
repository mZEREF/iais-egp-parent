$(function () {
    $("#searchBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("#mainForm").submit();
    });


    $("#clearBtn").click(function() {
        $('input[name="searchKeyword"]').val("");
        $('input[name="searchKeywordType"]').prop("checked",false);
        $("#searchFacilityClassification option:first").prop("selected", 'selected');
        $("#searchFacilityActivityType option:first").prop("selected", 'selected');
        $("#searchFacilityStatus option:first").prop("selected", 'selected');
        $("#taskListSearchFilter .current").text("All");
    });

    $("#adhocInspection").click(function (){
        if ($("input:radio:checked").length > 0) {
            showWaiting();
            $("[name='action_type']").val("inspection");
            $("#mainForm").submit();
        } else {
            $('#adhocInspectionAlert').modal('show');
        }
    });

    // inspection page
    $("#backBtn").click(function (){
        showWaiting();
        $("[name='action_type']").val("list");
        $("#mainForm").submit();
    });

    $("#submitBtn").click(function (){
        showWaiting();
        $("[name='action_type']").val("submit");
        $("#mainForm").submit();
    });
});

function adhocInspectionCancel() {
    $('#adhocInspectionAlert').modal('hide');
}