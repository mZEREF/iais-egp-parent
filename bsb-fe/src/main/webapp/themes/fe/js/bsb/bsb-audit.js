//audit date
function doSpecifyDt(id,type){
    showWaiting();
    $("#auditId").val(id);
    $("#moduleType").val(type);
    $("[name='action_type']").val("specifyDt");
    $("#mainForm").submit();
}

function doChangeDt(id,type){
    showWaiting();
    $("#auditId").val(id);
    $("#moduleType").val(type);
    $("[name='action_type']").val("changeDt");
    $("#mainForm").submit();
}

function submitReport(id){
    showWaiting();
    $("#auditId").val(id);
    $("[name='action_type']").val("doSelfAudit");
    $("#mainForm").submit();
}

$(function () {
    //Manual Audit Creation List
    $("#createList").click(function (){
        $("[name='action_type']").val("createList");
        if ($("input:checkbox:checked").length > 0) {
            showWaiting();
            $("#mainForm").submit();
        }else{
            alert("Select at least one audit task for confirmation");
        }
    });

    $("#searchBtn").click(function (){
        // var optionValue = $("#auditType option:selected").val();
        // if (optionValue == "Please Select" || optionValue == "") {
        //     $("#error_auditType").html("This is Mandatory");
        // }else {
            showWaiting();
            $("#error_auditType").html("");
            $("[name='action_type']").val("doSearch");
            $("#mainForm").submit();
        // }
    });

    $("#clearBtn").click(function () {
        $("#facilityClassification option:first").prop("selected",'selected');
        $("#facilityType option:first").prop("selected",'selected');
        $("#auditType option:first").prop("selected",'selected');
        $("#facilityName option:first").prop("selected",'selected');
        $("#beInboxFilter .current").text("Please Select");
    });

    $("#submitAudit").click(function () {
        var optionValue = $("#auditType option:selected").val();
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_auditType").html("This is Mandatory");
        }else {
            showWaiting();
            $("#error_auditType").html("");
            $("[name='action_type']").val("doSubmit");
            $("#mainForm").submit();
        }
    });

    //specify And Change Date
    $("#submitBtn").click(function (){
        showWaiting();
        $("[name='action_type']").val("doSubmit");
        $("#mainForm").submit();
    });

    $("#nextBtn").click(function (){
        showWaiting();
        $("#mainForm").submit();
    });
    //AO And DO Process Audit Date
    $("#rejectReason").hide();
    $("#decision").change(function (){
        var optionValue1 = $("#decision option:selected").val();
        if (optionValue1 == "AUDTAO002" || optionValue1 == "AUDTDO002") {// DOTO  the master code is deleted
            $("#rejectReason").show();
        }else{
            $("#rejectReason").hide();
        }
    })
    $("#submitButton").click(function () {
        var optionValue = $("#decision option:selected").val();
        if (optionValue == "AUDTAO001" || optionValue == "AUDTDO001") { // DOTO  the master code is deleted
            $("#error_decision").html("");
            showWaiting();
            $("[name='action_type']").val("doApprove");
            $("#mainForm").submit();
        }
        if (optionValue == "AUDTAO002" || optionValue == "AUDTDO002") {// DOTO  the master code is deleted
            var reasonValue = $("#reason").val();
            if (reasonValue == "" || reasonValue == null) {
                $("#error_decision").html("");
                $("#error_reason").html("This is Mandatory");
            } else {
                $("#error_decision").html("");
                $("#error_reason").html("");
                showWaiting();
                $("[name='action_type']").val("doReject");
                $("#mainForm").submit();
            }
        }
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_decision").html("This is Mandatory");
        }
    })

    //self-audit
    $("#submitReportButton").click(function (){
        $("[name='action_type']").val("doSubmit");
        var optionValue = $("#scenarioCategory option:selected").val();
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_scenarioCategory").html("This is Mandatory");
        }else {
            showWaiting();
            $("#mainForm").submit();
        }
    })

    // TODO master code has been removed, remove this js
    //DO decision self-audit
    $("#doProcessButton").click(function (){
        var optionValue = $("#decision option:selected").val();
        if (optionValue == "DOAUDO001") {
            showWaiting();
            $("[name='action_type']").val("doVerified");
            $("#mainForm").submit();
        }
        if (optionValue == "DOAUDO002") {
            showWaiting();
            $("[name='action_type']").val("doRequestInfo");
            $("#mainForm").submit();
        }
        if (optionValue == "DOAUDO003") {
            showWaiting();
            $("[name='action_type']").val("doReject");
            $("#mainForm").submit();
        }
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_decision").html("This is Mandatory");
        }
    })

    // TODO master code has been removed, remove this js
    //AO decision self-audit
    $("#aoProcessButton").click(function (){
        var optionValue = $("#decision option:selected").val();
        if (optionValue == "DOAUAO001") {
            showWaiting();
            $("[name='action_type']").val("doApproved");
            $("#mainForm").submit();
        }
        if (optionValue == "DOAUAO002") {
            showWaiting();
            $("[name='action_type']").val("doRouteBack");
            $("#mainForm").submit();
        }
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_decision").html("This is Mandatory");
        }
    })

    //cancel audit
    $("#doCancel").click(function (){
        $("[name='action_type']").val("doCancel");
        if ($("input:checkbox:checked").length > 0) {
            showWaiting();
            $("#mainForm").submit();
        }else{
            alert("Select at least one audit task for confirmation");
        }
    });

    $("#submitCancelAudit").click(function (){
        var cancellationReasons = $("#reasons").val();
        if (cancellationReasons == null || cancellationReasons == ""){
            $("#error_reasons").html("This is Mandatory");
        }else{
            showWaiting();
            $("[name='action_type']").val("doSubmit");
            $("#mainForm").submit();
        }
    });

    $("#AOProcessCancelAudit").click(function () {
        var optionValue = $("#decision option:selected").val();
        if (optionValue == "AUDTAO001") {
            $("#error_decision").html("");
            showWaiting();
            $("[name='action_type']").val("doApprove");
            $("#mainForm").submit();
        }
        if (optionValue == "AUDTAO002") {
                $("#error_decision").html("");
                showWaiting();
                $("[name='action_type']").val("doReject");
                $("#mainForm").submit();
        }
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_decision").html("This is Mandatory");
        }
    })

    //back
    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    $("#backFromAckPage").click(function (){
        showWaiting();
        $("#mainForm").submit();
    });

    $("#backFromDoc").click(function (){
        $('#documenta').removeClass("active");
        $('#infoa').click();
        $('#infoa').addClass("active");
    })

    $("#backFromProcess").click(function (){
        $('#processa').removeClass("active");
        $('#documenta').click();
        $('#documenta').addClass("active");
    })

    $("#backFromProcessAuditDt").click(function (){
        $('#processa').removeClass("active");
        $('#infoa').click();
        $('#infoa').addClass("active");
    })
});