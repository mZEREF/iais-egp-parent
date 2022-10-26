$(function () {
    var processingDecisionObj = $("#processingDecision");
    if (processingDecisionObj.val() === "MOHPRO002") {
        $("#rfiSubContent").show();
    } else {
        $("#rfiSubContent").hide();
    }
    processingDecisionObj.change(function (){
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO002") {
            $("#rfiSubContent").show();
            viewRfiApplication();
        } else {
            $("#rfiSubContent").hide();
        }
    })
    $("#submitBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("submit");
        $('#mainForm').submit();
    })
})