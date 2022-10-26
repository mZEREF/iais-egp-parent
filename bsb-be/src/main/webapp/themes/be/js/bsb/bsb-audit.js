//audit date
function doSpecifyDt(id) {
    showWaiting();
    $("#auditId").val(id);
    $("[name='action_type']").val("specifyDt");
    $("#mainForm").submit();
}

function dochangeDt(id) {
    showWaiting();
    $("#auditId").val(id);
    $("[name='action_type']").val("changeDt");
    $("#mainForm").submit();
}

function submitReport(id) {
    showWaiting();
    $("#auditId").val(id);
    $("[name='action_type']").val("doSelfAudit");
    $("#mainForm").submit();
}

function decisionChange(obj) {
    var id = $(obj).attr("id");
    var option = $("#" + id).val();
    if (option == "AUDTDO002" || option == "AUDTAO002") {// DOTO  the master code is deleted
        $("#rejectReason").show();
    } else {
        $("#rejectReason").hide();
    }
}

function isHidden() {
    var option1 = $("#doDecision").val();
    var option2 = $("#aoDecision").val();
    if (option1 == "AUDTDO002" || option2 == "AUDTAO002") {// DOTO  the master code is deleted
        $("#rejectReason").show();
    } else {
        $("#rejectReason").hide();
    }
}

$(function () {
    isHidden();
    //Manual Audit Creation List
    $("#createList").click(function () {
        $("[name='action_type']").val("createList");
        if ($("input:checkbox:checked").length > 0) {
            showWaiting();
            $("#mainForm").submit();
        } else {
            alert("Select at least one audit task for confirmation");
        }
    });

    $("#searchBtn").click(function () {
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
        $("#facilityClassification option:first").prop("selected", 'selected');
        $("#facilityType option:first").prop("selected", 'selected');
        $("#auditType option:first").prop("selected", 'selected');
        $("#facilityName option:first").prop("selected", 'selected');
        $("#beInboxFilter .current").text("All");
        // $("#auditType .current").text("All");
    });

    $("#nextBtn").click(function () {
        showWaiting();
        $("#mainForm").submit();
    });

    $("#submitAudit").click(function () {
        showWaiting();
        $("[name='action_type']").val("doSubmit");
        $("#mainForm").submit();
    });

    //specify And Change Date
    $("#submitChangeAuditDt").click(function () {
        $("[name='action_type']").val("doSubmit");
        var auditDate = $("#auditDate").val();
        var reason = $("#reasonForChange").val();
        if (auditDate == null || auditDate == "") {
            $("#auditDateError").html("This is Mandatory");
        } else if (reason == null || reason == "") {
            $("#auditDateError").html("");
            $("#reasonError").html("This is Mandatory");
        } else {
            showWaiting();
            $("#mainForm").submit();
        }
    });

    $("#submitSpecifyAuditDt").click(function () {
        $("[name='action_type']").val("doSubmit");
        var auditDate = $("#auditDate").val();
        if (auditDate == null || auditDate == "") {
            $("#auditDateError").html("This is Mandatory");
        } else {
            showWaiting();
            $("#mainForm").submit();
        }
    });
    //AO And DO Process Audit Date
    $("#submitButton").click(function () {
        var optionValue = $("#decision").val();
        if (optionValue == "AUDTAO001" || optionValue == "AUDTDO001") {// DOTO  the master code is deleted
            showWaiting();
            $("[name='action_type']").val("doApprove");
            $("#mainForm").submit();
        }
        if (optionValue == "AUDTAO002" || optionValue == "AUDTDO002") {// DOTO  the master code is deleted
            showWaiting();
            $("[name='action_type']").val("doReject");
            $("#mainForm").submit();
        }
    })

    //self-audit
    $("#submitReportButton").click(function () {
        $("[name='action_type']").val("doSubmit");
        var optionValue = $("#scenarioCategory option:selected").val();
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_scenarioCategory").html("This is Mandatory");
        } else {
            showWaiting();
            $("#mainForm").submit();
        }
    })

    // TODO master code has been removed, remove this js
    //DO decision self-audit
    $("#doProcessButton").click(function () {
        var optionValue = $("#decision").val();
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
    })

    // TODO master code has been removed, remove this js
    //AO decision self-audit
    $("#aoProcessButton").click(function () {
        var optionValue = $("#decision").val();
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
    })

    //cancel audit
    $("#doCancel").click(function () {
        $("[name='action_type']").val("doCancel");
        if ($("input:checkbox:checked").length > 0) {
            showWaiting();
            $("#mainForm").submit();
        } else {
            alert("Select at least one audit task for confirmation");
        }
    });

    $("#submitCancelAudit").click(function () {
        showWaiting();
        $("[name='action_type']").val("doSubmit");
        $("#mainForm").submit();
    });

    $("#AOProcessCancelAudit").click(function () {
        var optionValue = $("#decision").val();
        if (optionValue == "AUDTAO001") {
            showWaiting();
            $("[name='action_type']").val("doApprove");
            $("#mainForm").submit();
        }
        if (optionValue == "AUDTAO002") {
            showWaiting();
            $("[name='action_type']").val("doReject");
            $("#mainForm").submit();
        }
    })

    //back
    $("#back").click(function () {
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    $("#backFromAckPage").click(function () {
        showWaiting();
        $("#mainForm").submit();
    });

    $("#backFromDoc").click(function () {
        $('#documenta').removeClass("active");
        $('#infoa').click();
        $('#infoa').addClass("active");
    })

    $("#backFromProcess").click(function () {
        $('#processa').removeClass("active");
        $('#documenta').click();
        $('#documenta').addClass("active");
    })

    $("#backFromProcessAuditDt").click(function () {
        $('#processa').removeClass("active");
        $('#infoa').click();
        $('#infoa').addClass("active");
    })
});