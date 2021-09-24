//audit date
function doSpecifyDt(id){
    showWaiting();
    $("#auditId").val(id);
    $("[name='action_type']").val("specifyDt");
    $("#mainForm").submit();
}

function dochangeDt(id){
    showWaiting();
    $("#auditId").val(id);
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
        var optionValue = $("#auditType option:selected").val();
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_auditType").html("Please select valid options!");
        }else {
            showWaiting();
            $("#error_auditType").html("");
            $("[name='action_type']").val("doSearch");
            $("#mainForm").submit();
        }
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
            $("#error_auditType").html("Please select valid options!");
        }else {
            showWaiting();
            $("#error_auditType").html("");
            $("[name='action_type']").val("doSubmit");
            $("#mainForm").submit();
        }
    });

    //specify And Change Date
    $("#submitChangeAuditDt").click(function (){
        $("[name='action_type']").val("doSubmit");
        var auditDate = $("#auditDate").val();
        var reason = $("#reasonForChange").val();
        if (auditDate==null || auditDate == ""){
            $("#auditDateError").html("Please Select Date");
        }else if (reason==null||reason==""){
            $("#auditDateError").html("");
            $("#reasonError").html("Please enter change reason");
        }else{
            showWaiting();
            $("#mainForm").submit();
        }
    });

    $("#submitSpecifyAuditDt").click(function (){
        $("[name='action_type']").val("doSubmit");
        var auditDate = $("#auditDate").val();
        if (auditDate==null || auditDate == ""){
            $("#auditDateError").html("Please Select Date");
        }else{
            showWaiting();
            $("#mainForm").submit();
        }
    });
    //AO And DO Process Audit Date
    $("#rejectReason").hide();
    $("#decision").change(function (){
        var optionValue1 = $("#decision option:selected").val();
        if (optionValue1 == "AUDTAO002" || optionValue1 == "AUDTDO002") {
            $("#rejectReason").show();
        }else{
            $("#rejectReason").hide();
        }
    })
    $("#submitButton").click(function () {
        var optionValue = $("#decision option:selected").val();
        if (optionValue == "AUDTAO001" || optionValue == "AUDTDO001") {
            $("#error_decision").html("");
            showWaiting();
            $("[name='action_type']").val("doApprove");
            $("#mainForm").submit();
        }
        if (optionValue == "AUDTAO002" || optionValue == "AUDTDO002") {
            var reasonValue = $("#reason").val();
            if (reasonValue == "" || reasonValue == null) {
                $("#error_decision").html("");
                $("#error_reason").html("Please enter the reason");
            } else {
                $("#error_decision").html("");
                $("#error_reason").html("");
                showWaiting();
                $("[name='action_type']").val("doReject");
                $("#mainForm").submit();
            }
        }
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_decision").html("Please select valid options");
        }
    })

    //self-audit
    $("#submitReportButton").click(function (){
        $("[name='action_type']").val("doSubmit");
        var optionValue = $("#scenarioCategory option:selected").val();
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_scenarioCategory").html("Please select valid options!");
        }else {
            showWaiting();
            $("#mainForm").submit();
        }
    })

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
            $("#error_decision").html("Please select valid options!");
        }
    })

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
            $("#error_decision").html("Please select valid options!");
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