$(function () {
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
        if (value.checked === true && !$(this).attr("disabled")){
            var radioName = $(this).attr("name");
            removeRadioDisable(radioName);
        }
    })
    facilityActivityYesRadioObj.click(function (){
        var radioName = $(this).attr("name");
        removeRadioDisable(radioName)
    })
    facilityActivityNoRadioObj.click(function (){
        var radioName = $(this).attr("name");
        $("input[data-bat-activityType='"+radioName+"']").click();
        addRadioDisable(radioName);
    })
})

function removeRadioDisable(radioName){
    $("input[data-bat-activityType='"+radioName+"']").removeAttr("disabled");
}

function addRadioDisable(radioName){
    $("input[data-bat-activityType='"+radioName+"']").attr("disabled",true);
}