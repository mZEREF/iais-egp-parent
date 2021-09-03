function doClear() {
    $("#selectSearchChkMsg").hide();
    $('input[name="searchNo"]').val("");
    $('input[type="radio"]').prop("checked", false);
}

function doAdvancedSearch() {
    showWaiting();
    var chk = $("[name='searchChk']:checked");
    var dropIds = new Array();
    chk.each(function () {
        dropIds.push($(this).val());
    });
    if (dropIds.length === 0) {
        $("#selectSearchChkMsg").show();
        dismissWaiting();
    } else {
        $("[name='action_type']").val("advSearch");
        $("#mainForm").submit();
    }
}


function doSearch() {
    showWaiting();
    var chk = $("[name='searchChk']:checked");
    var dropIds = new Array();
    chk.each(function () {
        dropIds.push($(this).val());
    });
    if (dropIds.length === 0) {
        $("#selectSearchChkMsg").show();
        dismissWaiting();
    } else {
        $("[name='action_type']").val("doSearch");
        $("#mainForm").submit();
    }
}

function doAppInfo(appId) {
    showWaiting();
    $("#appId").val(appId);
    $("[name='action_type']").val("appDetail");
    $("#mainForm").submit();
}

$(function (){
    $('select[name="scheduleType"]').change(function (){
        var schedule = $('select[name="scheduleType"] option:selected').val();
        $.post('/bsb-be/bio-info/bio.do',
            {schedule: schedule},
            function (data){
                var result = data.result;
                if(result == 'success'){
                    var queryResult = data.queryResult;
                    console.log(queryResult);
                    var optionString = "";
                    var optionString1 = "";
                    for (var i = 0; i < queryResult.length; i++) {
                        optionString += "<option value=\""  + queryResult[i] + "\">" + queryResult[i] + "</option>";
                        optionString1+= "<li data-value=\""+queryResult[i]+"\" class=\"option\">"+queryResult[i]+"</li>"
                    }
                    $("select[name = 'biologicalAgent']").html("<option value=\"\">Please select<\/option>"+optionString);
                    $("select[name = 'biologicalAgent']").next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>"+optionString1);
                }else{

                }
            }
        )
    })
})

function doAdvSearch() {
    $("[name='action_type']").val("doAdvSearch");
    $("#mainForm").submit();
}

function doAdvAfterSearch(){
    showWaiting();
    $("[name='action_type']").val("afterSearch");
    $("#mainForm").submit();
}

function doBack() {
    showWaiting();
    $("[name='action_type']").val("back");
    $("#mainForm").submit();
}

function doAdvClear() {
    $('input[type="text"]').val("");
    $("#error_to_date").hide();
    $("#error_start_to_date").hide();
    $("#error_expiry_date").hide();
    $("#clearFilterForSearch input[type='checkbox']").prop('checked', false);
    $("#clearFilterForSearch .multi-select-button").html("-- Select --");
    $("#facilityType option").prop('selected',false);
    $("#natureOfTheSample option").prop('selected',false);
    $("select[name = 'applicationType'] option:first").prop("selected",'selected');
    $("select[name = 'applicationStatus'] option:first").prop("selected",'selected');
    $("select[name = 'facilityClassification'] option:first").prop("selected",'selected');
    $("select[name = 'facilityName'] option:first").prop("selected",'selected');
    $("select[name = 'scheduleType'] option:first").prop("selected",'selected');
    $("select[name = 'biologicalAgent'] option:first").prop("selected",'selected');
    $("select[name = 'riskLevelOfTheBiologicalAgent'] option:first").prop("selected",'selected');
    $("select[name = 'processType'] option:first").prop("selected",'selected');
    $("select[name = 'gazettedArea'] option:first").prop("selected",'selected');
    $("select[name = 'facilityStatus'] option:first").prop("selected",'selected');
    $("select[name = 'approvedFacilityCertifier'] option:first").prop("selected",'selected');
    $("select[name = 'approvalType'] option:first").prop("selected",'selected');
    $("select[name = 'approvalStatus'] option:first").prop("selected",'selected');
    $("select[name = 'organisationName'] option:first").prop("selected",'selected');
    $("select[name = 'afcStatus'] option:first").prop("selected",'selected');
    $("input[name = 'applicationSubmissionDateFrom']").val("");
    $("input[name = 'applicationSubmissionDateTo']").val("");
    $("input[name = 'approvalDateFrom']").val("");
    $("input[name = 'approvalDateTo']").val("");
    $("input[name = 'approvalSubmissionDateFrom']").val("");
    $("input[name = 'approvalSubmissionDateTo']").val("");
    $("input[name = 'approvedDateFrom']").val("");
    $("input[name = 'approvedDateTo']").val("");
    $("input[name = 'facilityExpiryDateFrom']").val("");
    $("input[name = 'facilityExpiryDateTo']").val("");
    $("#clearFilterForSearch .current").text("Please Select");

}

function doAdvAfterSearch() {
    showWaiting();
    $("[name='action_type']").val("afterSearch");
    $("#mainForm").submit();
}

