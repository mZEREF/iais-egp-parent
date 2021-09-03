$(function() {
    $("#others").hide();
    $("[name='ModeOfProcurement_LocalTransfer']").hide();
    $("[name='ModeOfProcurement_Import']").hide();
    var selectValueNatureOfTheSample = $("#natureOfTheSample").val();
    if(selectValueNatureOfTheSample != null){
        for(var i=0;i<selectValueNatureOfTheSample.length ;i++){
            if(selectValueNatureOfTheSample[i] == "BNOTS006" ) {
                $("#others").show();
            }
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
    var facilitySelected = $("#facilityName").find("option:checked").val();
    $("#facilityId").val(facilitySelected);
    $("#facilityName").change(function() {
        $("#facilityId").val($(this).val());
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
    var selectValueSchedule = $("#schedule").val();
    if (selectValueSchedule == "SCHTYPE001"){
        $("#select1").show();
    }else if (selectValueSchedule == "SCHTYPE002"){
        $("#select2").show();
    }else if (selectValueSchedule == "SCHTYPE003"){
        $("#select3").show();
    }else if (selectValueSchedule == "SCHTYPE004"){
        $("#select4").show();
    }else if (selectValueSchedule == "SCHTYPE005"){
        $("#select5").show();
    }else if (selectValueSchedule == "SCHTYPE006") {
        $("#select6").show();
    }

    $("#schedule").change(function() {
        var schedule = $(this).val();
        if (schedule == "SCHTYPE001"){
            $("[name='selectHidden']").hide();
            $("#listOfAgentsOrToxins").val("");
            $("#select1").show();
        }else if (schedule == "SCHTYPE002"){
            $("[name='selectHidden']").hide();
            $("#listOfAgentsOrToxins").val("");
            $("#select2").show();
        }else if (schedule == "SCHTYPE003"){
            $("[name='selectHidden']").hide();
            $("#listOfAgentsOrToxins").val("");
            $("#select3").show();
        }else if (schedule == "SCHTYPE004"){
            $("[name='selectHidden']").hide();
            $("#listOfAgentsOrToxins").val("");
            $("#select4").show();
        }else if (schedule == "SCHTYPE005"){
            $("[name='selectHidden']").hide();
            $("#listOfAgentsOrToxins").val("");
            $("#select5").show();
        }else if (schedule == "SCHTYPE006"){
            $("[name='selectHidden']").hide();
            $("#listOfAgentsOrToxins").val("");
            $("#select6").show();
        }
    })
    $("#Next").click(function (){
        var pageValue = $("#page_id").val();
        if(pageValue == "form_page"){
            $("[name='crud_action_type_form_page']").val("PrepareDocuments");
            SOP.Crud.cfxSubmit("mainForm","loading");
        }else if(pageValue == "document_page"){
            $("[name='crud_action_type_form_page']").val("PreparePreview");
            SOP.Crud.cfxSubmit("mainForm","loading");
        }
    })
    $("#Back").click(function (){
        var pageValue = $("#page_id").val();
        if(pageValue == "form_page"){
            $("[name='crud_action_type_form_page']").val("PrepareJump");
            SOP.Crud.cfxSubmit("mainForm","loading");
        }else if(pageValue == "document_page"){
            $("[name='crud_action_type_form_page']").val("PrepareForms");
            SOP.Crud.cfxSubmit("mainForm","loading");
        }else if(pageValue == "preview_page"){
            $("[name='crud_action_type_form_page']").val("PrepareDocuments");
            SOP.Crud.cfxSubmit("mainForm","loading");
        }
    })
    $("#SaveDraft").click(function (){
        SOP.Crud.cfxSubmit("mainForm","doSaveDraft");
    })
    $("#Submit").click(function (){
        SOP.Crud.cfxSubmit("mainForm","doSubmit");
    })
    $("#subApprovalEdit").click(function (){
        $("[name='crud_action_type_form_page']").val("PrepareForms");
        SOP.Crud.cfxSubmit("mainForm","loading");
    })
    var controlLi = $('#controlLi').val();
    var $tarSel = $('#'+controlLi+'li');
    if ($tarSel.length > 0) {
        $tarSel.addClass('active');
        if ($tarSel.attr("class").match("active")){
            $tarSel.removeClass("incomplete");
            $tarSel.removeClass("complete");
        }
    }
    $('#nav-tabs-ul a').click(function() {
        var currId = $(this).attr('id');
        console.log(currId);
        if (controlLi == currId) {
            return;
        }else if(currId == "PrepareForms"){
            $("[name='crud_action_type_form_page']").val("PrepareForms");
            $("[name='crud_action_type']").val("loading");
            $('#mainForm').submit();
        }else if(currId == "PrepareDocuments"){
            $("[name='crud_action_type_form_page']").val("PrepareDocuments");
            $("[name='crud_action_type']").val("loading");
            $('#mainForm').submit();
        }else if(currId == "PreparePreview"){
            $("[name='crud_action_type_form_page']").val("PreparePreview");
            $("[name='crud_action_type']").val("loading");
            $('#mainForm').submit();
        }
    });

})