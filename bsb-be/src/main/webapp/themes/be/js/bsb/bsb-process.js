$(function () {
    $("#submitButton").click(function () {
        var selectValue = $("#processingDecision").val();
        $(".error-msg").html("");
        // if (selectValue == "DOSPD001") {
        //     showWaiting();
        //     $("[name='action_type']").val("screenedByDO");
        //     $('#mainForm').submit();
        // } else if (selectValue == "DOSPD002" || selectValue == "DOPPD003") {
        //     showWaiting();
        //     $("[name='action_type']").val("requestForInformation");
        //     $('#mainForm').submit();
        // } else if (selectValue == "DOSPD003") {
        //     showWaiting();
        //     $("[name='action_type']").val("doReject");
        //     $('#mainForm').submit();
        // } else if (selectValue == "DOPPD001") {
        //     showWaiting();
        //     $("[name='action_type']").val("recommendApproval");
        //     $('#mainForm').submit();
        // } else if (selectValue == "DOPPD002") {
        //     showWaiting();
        //     $("[name='action_type']").val("recommendRejection");
        //     $('#mainForm').submit();
        // } else if (selectValue == "AOSPD001") {
        //     showWaiting();
        //     $("[name='action_type']").val("approvalForInspection");
        //     $('#mainForm').submit();
        // } else if (selectValue == "AOSPD002" || selectValue == "AOPPD002") {
        //     showWaiting();
        //     $("[name='action_type']").val("aoReject");
        //     $('#mainForm').submit();
        // } else if (selectValue == "AOSPD003" || selectValue == "AOPPD003") {
        //     showWaiting();
        //     $("[name='action_type']").val("routeBackToDO");
        //     $('#mainForm').submit();
        // } else if (selectValue == "AOSPD004" || selectValue == "AOPPD004") {
        //     showWaiting();
        //     $("[name='action_type']").val("routeToHM");
        //     $('#mainForm').submit();
        // } else if (selectValue == "AOPPD001") {
        //     showWaiting();
        //     $("[name='action_type']").val("aoApproved");
        //     $('#mainForm').submit();
        // } else if (selectValue == "HMSPD001") {
        //     showWaiting();
        //     $("[name='action_type']").val("hmApprove");
        //     $('#mainForm').submit();
        // } else if (selectValue == "HMSPD002") {
        //     showWaiting();
        //     $("[name='action_type']").val("hmReject");
        //     $('#mainForm').submit();
        // }else {
        //     $("[name='action_type']").val("prepareData");
        //     $('#mainForm').submit();
        // }
        $('#mainForm').submit();
    })
    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("back");
        $('#mainForm').submit();
    })
    $("#clearButton").click(function (){
        $("div,textarea").val("");
        $(".date_picker").val("");
        $("#riskLevel option:first").prop("selected",'selected');
        $("#processingDecision option:first").prop("selected",'selected');
        $("#selectedApprovedFacilityCertifier option:first").prop("selected",'selected');
        $("#beInboxFilter .current").text("Please Select");
    })
})