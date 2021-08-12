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
    $("#facilityName").change(function() {
        var selectValue = $(this).val();
        $("#facilityId").val(selectValue);
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
    $("#schedule").change(function() {
        var schedule = $(this).val();
        $.post('/bsb-fe/bio-info/bio.do',
            {schedule: schedule},
            function (data){
                var result = data.result;
                if(result == 'success'){
                    var queryResult = data.queryResult;
                    var optionString = "";
                    var optionString1 = "";
                    for (var i = 0; i < queryResult.length; i++) {
                        optionString += "<option value=\""  + queryResult[i].id + "\">" + queryResult[i].name + "</option>";
                        optionString1+= "<label class=\"multi-select-menuitem\" name=\"checkAll\" for=\"biological_"+i+"\" role=\"menuitem\">"+"<input id=\"biological_"+i+"\" type=\"checkbox\" value=\""+queryResult[i].id+"\">"+queryResult[i].name+"</label>"
                    }
                    $("#listOfAgentsOrToxins").html(optionString);
                    $("#listOfAgentsOrToxins").next().children(".multi-select-menu").html(optionString1);
                }else{
                    $("#listOfAgentsOrToxins").next().children(".multi-select-menu").html("");
                }
            }
        )
    })
    $("#listOfAgentsOrToxins").change(function() {
        console.log("hehe");
    })
    $("#listOfAgentsOrToxins").change(function() {
        var selectValue = $('#listOfAgentsOrToxins option:selected').val();
        $("#biologicalId").val(selectValue);
    })
})