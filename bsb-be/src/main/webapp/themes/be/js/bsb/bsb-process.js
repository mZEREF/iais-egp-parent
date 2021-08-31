$(function () {
    $("#doScreeningSubmitButton").click(function (){
        var selectValue = $("#processingDecision").val();
        $(".error-msg").html("");
        if(selectValue == "DOSPD001"){
            showWaiting();
            $("[name='action_type']").val("screenedByDO");
            $('#mainForm').submit();
        }else if(selectValue == "DOSPD002"){
            showWaiting();
            $("[name='action_type']").val("requestForInformation");
            $('#mainForm').submit();
        }else if(selectValue == "DOSPD003"){
            showWaiting();
            $("[name='action_type']").val("reject");
            $('#mainForm').submit();
        }else{
            $("#error_processingDecision").html("This Is Mandatory")
        }
    })
    $("#aoScreeningSubmitButton").click(function (){
        var selectValue = $("#processingDecision").val();
        $(".error-msg").html("");
        if(selectValue == "AOSPDD001"){
            showWaiting();
            $("[name='action_type']").val("approvalForInspection");
            $('#mainForm').submit();
        }else if(selectValue == "AOSPDD002"){
            showWaiting();
            $("[name='action_type']").val("reject");
            $('#mainForm').submit();
        }else if(selectValue == "AOSPDD003"){
            showWaiting();
            $("[name='action_type']").val("routeBackToDO");
            $('#mainForm').submit();
        }else if(selectValue == "AOSPDD004"){
            showWaiting();
            $("[name='action_type']").val("routeToHM");
            $('#mainForm').submit();
        }else{
            $("#error_processingDecision").html("This Is Mandatory")
        }
    })
    $("#hmScreeningSubmitButton").click(function (){
        var selectValue = $("#processingDecision").val();
        $(".error-msg").html("");
        if(selectValue == "AOSPDD001"){
            showWaiting();
            $("[name='action_type']").val("approvalForInspection");
            $('#mainForm').submit();
        }else if(selectValue == "AOSPDD002"){
            showWaiting();
            $("[name='action_type']").val("reject");
            $('#mainForm').submit();
        }else if(selectValue == "AOSPDD003"){
            showWaiting();
            $("[name='action_type']").val("routeBackToDO");
            $('#mainForm').submit();
        }else if(selectValue == "AOSPDD004"){
            showWaiting();
            $("[name='action_type']").val("routeToHM");
            $('#mainForm').submit();
        }else{
            $("#error_processingDecision").html("This Is Mandatory")
        }
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