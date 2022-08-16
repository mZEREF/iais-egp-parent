$(function () {
    // TODO: check these decision
    var processingDecisionObj = $("#processingDecision");
    var processingDecisionVal =  processingDecisionObj.val();
    if (processingDecisionVal === "MOHPRO022") {
        $("#selectMohUserDiv").show();
    } else {
        $("#selectMohUserDiv").hide();
    }
    processingDecisionObj.change(function () {
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO022") {
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