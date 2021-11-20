//DO submit revocation process entrance
// function doProcess(fac){
//     showWaiting();
    // $("#appId").val(id);
    // $("#from").val(fac);
    // $("[name='action_type']").val("doProcess");
    // $("#mainForm").submit();
// }

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
        $("#approvalStatus option:first").prop("selected",'selected');
        $("#approvalNo").val("");
        $("#beInboxFilter .current").text("Please Select");
    });
    // DO submit revocation
    $("#clearButton1").click(function () {
        $('#reason').val("");
        $('#remark').val("");
    });

    $("#submitButton1").click(function () {
        var reasonValue = $("#reason").val();
        var isUpload = $("#have").val();
        if (reasonValue == "" || reasonValue == null) {
            $("#error_reason").html("Please enter the reason");
        }else{
            // if (isUpload!=null) {
                $("#error_reason").html("");
                showWaiting();
                $("[name='action_type']").val("doSubmit");
                $("#mainForm").submit();
            // }else {
            //     alert("Please upload file");
            // }
        }
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

    $("#submitButton3").click(function () {
        showWaiting();
        $("[name='action_type']").val("doUpdate");
        $("#mainForm").submit();
    });

    //AO process revocation application
    $("#submitButton").click(function () {
        var optionValue = $("#decision option:selected").val();
        if (optionValue == "BSBAOPD001") {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "approve");
        }
        if (optionValue == "BSBAOPD002") {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "reject");
        }
        if (optionValue == "BSBAOPD003") {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "routeBack");
        }
        if (optionValue == "BSBAOPD004") {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "submit");
        }
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_decision").html("Please select valid options!");
        }
    });

    //back
    $("#backToTask").click(function (){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    });

    $("#backToSubmit2").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    //back from ackPage
    $("#backFromAckPage").click(function (){
        showWaiting();
        $("#mainForm").submit();
    });

    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    })

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
});