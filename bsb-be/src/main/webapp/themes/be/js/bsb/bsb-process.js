$(function () {
    validate();
    var riskLevelObj = $("#riskLevel");
    var riskLevel = riskLevelObj.val();
    if (riskLevel === "RLOTBA001" || riskLevel === "RLOTBA003"){
        $("#commentFalse").hide();
        $("#commentTrue").show();
    }else{
        $("#commentFalse").show();
        $("#commentTrue").hide();
    }
    riskLevelObj.change(function () {
        var selectValue = $(this).val();
        if(selectValue === "RLOTBA001" || selectValue === "RLOTBA003") {
            $("#commentFalse").hide();
            $("#commentTrue").show();
        }else{
            $("#commentFalse").show();
            $("#commentTrue").hide();
        }
    })
    $("#submitButton").click(function () {
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
    $("input[data-radio-type='facilityActivityYes']").click(function (){
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
