$(function () {
    $("#submitBtn").click(function () {
        showWaiting();
        var decisionVal = $("#processingDecision").val();
        if (decisionVal === "MOHPRO003" || decisionVal === "MOHPRO007") {
            var countApproved = 0;
            var activityRadioYesList = $("input[data-radio-type='facilityActivityYes']").valueOf();
            var batRadioYesList = $("input[data-radio-type='facilityAgentRadioYes']").valueOf();
            $.each(activityRadioYesList, function (n, value){
                if (value.checked === true){
                    countApproved ++;
                }
            })
            $.each(batRadioYesList, function (n, value){
                if (value.checked === true){
                    countApproved ++;
                }
            })
            if (countApproved <= 0) {
                $("#errorApprovalNone").show();
                dismissWaiting();
            } else {
                $("[name='action_type']").val("submit");
                $('#mainForm').submit();
            }
        } else {
            $("[name='action_type']").val("submit");
            $('#mainForm').submit();
        }
    })

    if ($("#canSubmit").val() === 'N') {
        $('#afterCanNotSubmit').modal('show');
    }

    var appType = $("#appType").val();
    $("#processingDecision").change(function () {
        var selectValue = $(this).val();
        if (selectValue === 'MOHPRO003' && appType !== 'BSBAPTY003') {
            $("#selectMohUserDiv").show();
            $("#reasonForRejectionDiv").show();
            $("#rfiSubContent").hide();
            $("#facValidityEndDateDiv").hide();
        } else if (selectValue === 'MOHPRO007') {
            $("#selectMohUserDiv").show();
            $("#reasonForRejectionDiv").hide();
            $("#rfiSubContent").hide();
            $("#facValidityEndDateDiv").show();
        } else if (selectValue === 'MOHPRO002') {
            $("#selectMohUserDiv").hide();
            $("#reasonForRejectionDiv").hide();
            $("#rfiSubContent").show();
            $("#facValidityEndDateDiv").hide();
            viewRfiApplication();
        } else {
            $("#selectMohUserDiv").hide();
            $("#reasonForRejectionDiv").hide();
            $("#rfiSubContent").hide();
            $("#facValidityEndDateDiv").hide();
        }
    })
})
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-web/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}

function cancelJump() {
    $('#afterCanNotSubmit').modal('hide');
}

function jumpToTaskList() {
    window.location.href = "/bsb-web/eservice/INTRANET/MohBsbTaskList";
}
