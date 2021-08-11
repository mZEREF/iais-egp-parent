$(function() {
    $("#others").hide();
    $("[name='ModeOfProcurement_LocalTransfer']").hide();
    $("[name='ModeOfProcurement_Import']").hide();
    var selectValueNatureOfTheSample = $("#natureOfTheSample").val();
    for(var i=0;i<selectValueNatureOfTheSample.length ;i++){
        if(selectValueNatureOfTheSample[i] == "BNOTS006" ) {
            $("#others").show();
        }
    }
    var selectValueModeOfProcurement = $("#modeOfProcurement").val();
    if(selectValueModeOfProcurement == "BMOP001") {
        $("[name='ModeOfProcurement_LocalTransfer']").show();
        $("[name='ModeOfProcurement_Import']").hide();
    }else if(selectValueModeOfProcurement == "BMOP002"){
        $("[name='ModeOfProcurement_LocalTransfer']").hide();
        $("[name='ModeOfProcurement_Import']").show();
    }else{
        $("[name='ModeOfProcurement_LocalTransfer']").hide();
        $("[name='ModeOfProcurement_Import']").hide();
    }
    $('#SaveDraft').click(function () {
        showWaiting();
        submit('documents', 'saveDraft', $('#selectDraftNo').val());
    });
    $("#facilityId").change(function() {
        var selectText = $('#facilityId option:selected').text();
        $("#facilityName").val(selectText);
    })
    $("#biologicalId").change(function() {
        var selectText = $('#biologicalId option:selected').text();
        $("#listOfAgentsOrToxins").val(selectText);
    })
    $("#natureOfTheSample").change(function() {
        var selectValue = $(this).val();
        var flag = false;
        for(var i=0;i<selectValue.length ;i++){
            if(selectValue[i] == "BNOTS006" ) {
                flag = true;
            }
        }
        if(flag == true){
            $("#others").show();
        }else{
            $("#others").hide();
        }
    })
    $("#modeOfProcurement").change(function() {
        var selectValue = $(this).val();
        if(selectValue == "BMOP001") {
            $("[name='ModeOfProcurement_LocalTransfer']").show();
            $("[name='ModeOfProcurement_Import']").hide();
        }else if(selectValue == "BMOP002"){
            $("[name='ModeOfProcurement_LocalTransfer']").hide();
            $("[name='ModeOfProcurement_Import']").show();
        }else{
            $("[name='ModeOfProcurement_LocalTransfer']").hide();
            $("[name='ModeOfProcurement_Import']").hide();
        }
    })

})