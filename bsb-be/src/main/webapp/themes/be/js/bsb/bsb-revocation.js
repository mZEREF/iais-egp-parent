function doRevoke(id,fac){
    showWaiting();
    $("#approvalId").val(id);
    $("#from").val(fac);
    $("[name='action_type']").val("doRevoke");
    $("#mainForm").submit();
}

function checkEndTime(startTime,endTime){
    if(endTime<startTime){
        return false;
    }
    return true;
}

$(function () {
    $("select[data-action-select]").change(function () {
        window.location = this.value;
    });
    //Facility list
    $("#searchBtn").click(function (){
        var optionValue = $("#auditType option:selected").val();
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_auditType").html("This is Mandatory");
        }else {
            showWaiting();
            $("#error_auditType").html("");
            $("[name='action_type']").val("doSearch");
            $("#mainForm").submit();
        }
    });

    $("#clearBtn").click(function () {
        $('input[type="text"]').val("");
        $("#beInboxFilter input[type='checkbox']").prop('checked', false);
        $("#beInboxFilter .multi-select-button").html("-- Select --");
        $("#facilityClassification option").prop('selected',false);
        $("select[name = 'facilityName'] option:first").prop("selected",'selected');
        $("select[name = 'processType'] option:first").prop("selected",'selected');
        $("select[name = 'approvalType'] option:first").prop("selected",'selected');
        $("select[name = 'approvalStatus'] option:first").prop("selected",'selected');
        $("input[name = 'approvedDateFrom']").val("");
        $("input[name = 'approvedDateTo']").val("");
        $("#beInboxFilter .current").text("Please Select");
    });
    // DO submit revocation
    $("#clearButton1").click(function () {
        $('#reason').val("");
        $('#remark').val("");
    });

    $("#nextBtn").click(function () {
        showWaiting();
        $("#mainForm").submit();
    });

    // revocationList
    $("#searchBtn2").click(function () {
        var fromDt = $("#searchAppDateFrom").val();
        var endDt = $("#searchAppDateTo").val();
        var flag = checkEndTime(fromDt, endDt);
        if (flag) {
            showWaiting();
            $("#error_searchAppDateTo").html("");
            $("[name='action_type']").val("doSearch");
            $("#mainForm").submit();
        }else{
            $("#error_searchAppDateTo").html("EndDate can not be earlier than startDate.");
        }
    });

    $("#clearBtn2").click(function () {
        $('#facilityName').val("");
        $('#facilityAddress').val("");
        $('#applicationNo').val("");
        $('#applicationStatus').val("");
        $("#searchAppDateFrom").val("");
        $("#searchAppDateTo").val("");
        $("#facilityClassification option:first").prop("selected",'selected');
        $("#facilityType option:first").prop("selected",'selected');
        $("#processType option:first").prop("selected",'selected');
        $("#applicationType option:first").prop("selected",'selected');
        $("#beInboxFilter .current").text("Please Select");
    });

    //update inventory
    $("#clearButton3").click(function () {
        $('#number').val("");
        $("#toxinsSelect option:first").prop("selected",'selected');
        $("#stateSelect option:first").prop("selected",'selected');
        $("#clearSelect .current").text("Please Select");
    });

    $("#submitRevoke").click(function (){
        showWaiting();
        $("[name='action_type']").val("doSubmit");
        $("#mainForm").submit();
    });

    //AO process revocation application
    $("#submitButton").click(function () {
        showWaiting();
        $("#mainForm").submit();
    });

    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    })
});