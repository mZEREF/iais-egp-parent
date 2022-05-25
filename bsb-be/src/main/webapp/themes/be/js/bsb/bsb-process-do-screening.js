$(function () {
    validate();
    var processingDecisionObj = $("#processingDecision");
    var processingDecisionVal =  processingDecisionObj.val();
    if (processingDecisionVal === "MOHPRO001") {
        $("#selectMohUserDiv").show();
    } else {
        $("#selectMohUserDiv").hide();
    }
    processingDecisionObj.change(function () {
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO001") {
            $("#selectMohUserDiv").show();
        } else {
            $("#selectMohUserDiv").hide();
        }
    })
    var facilityActivityNoRadioObj = $("input[data-radio-type='facilityActivityNo']");
    var activityRadioNoList = facilityActivityNoRadioObj.valueOf();
    $.each(activityRadioNoList, function (n, value){
        if (value.checked === true){
            var radioName = $(this).attr("name");
            addRadioDisable(radioName);
        }
    })
    var facilityActivityYesRadioObj = $("input[data-radio-type='facilityActivityYes']");
    var activityRadioYesList = facilityActivityYesRadioObj.valueOf();
    $.each(activityRadioYesList, function (n, value){
        if (value.checked === true){
            var radioName = $(this).attr("name");
            removeRadioDisable(radioName);
        }
    })
    facilityActivityNoRadioObj.removeAttr("disabled");
    facilityActivityYesRadioObj.removeAttr("disabled");
    facilityActivityYesRadioObj.click(function (){
        var radioName = $(this).attr("name");
        removeRadioDisable(radioName)
    })
    facilityActivityNoRadioObj.click(function (){
        var radioName = $(this).attr("name");
        $("input[data-bat-activityId='"+radioName+"']").click();
        addRadioDisable(radioName);
    })
    $("#submitBtn").click(function () {
        showWaiting();
        $("input[type='radio']").removeAttr("disabled");

        var decisionVal = $("#processingDecision").val();
        if (decisionVal === "MOHPRO001") {
            var countApproved = 0;
            $.each(activityRadioYesList, function (n, value){
                if (value.checked === true){
                    countApproved ++;
                }
            })
            if (countApproved <= 0) {
                $("#errorApprovalNone").show();
                dismissWaiting();
            } else {
                $('#mainForm').submit();
            }
        } else {
            $('#mainForm').submit();
        }
    })
})
function validate(){
    var ifProcess = $("#ifProcess").val();
    if("Y" === ifProcess){
        $('#info').removeClass("active");
        $('#doProcess').click();
        $('#process').addClass("active");
    }
}
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-web/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}

function removeRadioDisable(radioName){
    $("input[data-bat-activityId='"+radioName+"']").removeAttr("disabled");
}

function addRadioDisable(radioName){
    $("input[data-bat-activityId='"+radioName+"']").attr("disabled",true);
}
