$(function () {
    var processingDecisionObj = $("#processingDecision");
    var processingDecisionVal =  processingDecisionObj.val();
    if (processingDecisionVal === "MOHPRO030") {
        $("#selectMohUserDiv").show();
    } else {
        $("#selectMohUserDiv").hide();
    }
    processingDecisionObj.change(function () {
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO030") {
            $("#selectMohUserDiv").show();
        } else {
            $("#selectMohUserDiv").hide();
        }
    })
    $("#submitBtn").click(function () {
        showWaiting();
        $("input[name='action_value']").val("submitDoc");
        $('#mainForm').submit();
    })
    $("#saveReportBtn").click(function () {
        showWaiting();
        $("input[name='action_value']").val("savaDoc");
        $("#mainForm").submit();
    });
})