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
    $("#submitBtn").click(function () {
        showWaiting();
        $("input[type='radio']").removeAttr("disabled");
        $('#mainForm').submit();
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
    var url = "/bsb-be/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}

function removeRadioDisable(radioName){
    $("input[data-bat-activityId='"+radioName+"']").removeAttr("disabled");
}

function addRadioDisable(radioName){
    $("input[data-bat-activityId='"+radioName+"']").attr("disabled",true);
}
