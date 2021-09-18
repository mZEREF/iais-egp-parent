$(function() {
    $("#others").hide();
    $("[name='ModeOfProcurement_LocalTransfer']").hide();
    $("[name='ModeOfProcurement_Import']").hide();
    var selectValueNatureOfTheSample = $("#natureOfTheSample").val();
    if(selectValueNatureOfTheSample != null){
        for(var i=0;i<selectValueNatureOfTheSample.length ;i++){
            if(selectValueNatureOfTheSample[i] == "BNOTS007" ) {
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
    }else if(selectValueModeOfProcurement == ""){
        $("[name='ModeOfProcurement_LocalTransfer']").hide();
        $("[name='ModeOfProcurement_Import']").hide();
    }
    $("#facilityName").val($("#facilityId").find("option:checked").text());
    $("#facilityId").change(function() {
        $("#facilityName").val($("#facilityId").find("option:checked").text());
    })
    $("#natureOfTheSample").change(function() {
        var selectValue = $(this).val();
        var flag = false;
        for(var i=0;i<selectValue.length ;i++){
            if(selectValue[i] == "BNOTS007" ) {
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
    }else if (selectValueSchedule == ""){
        $("#select0").show();
    }
    $("#schedule").change(function() {
        var schedule = $(this).val();
        if (schedule == "SCHTYPE001"){
            $("[name='selectHidden']").hide();
            clearBiologicalList();
            $("#select1").show();
        }else if (schedule == "SCHTYPE002"){
            $("[name='selectHidden']").hide();
            clearBiologicalList();
            $("#select2").show();
        }else if (schedule == "SCHTYPE003"){
            $("[name='selectHidden']").hide();
            clearBiologicalList();
            $("#select3").show();
        }else if (schedule == "SCHTYPE004"){
            $("[name='selectHidden']").hide();
            clearBiologicalList();
            $("#select4").show();
        }else if (schedule == "SCHTYPE005"){
            $("[name='selectHidden']").hide();
            clearBiologicalList();
            $("#select5").show();
        }else if (schedule == "SCHTYPE006"){
            $("[name='selectHidden']").hide();
            clearBiologicalList();
            $("#select6").show();
        }else if(schedule == ""){
            $("[name='selectHidden']").hide();
            clearBiologicalList();
            $("#select0").show();
        }
    })
    $("#Clear").click(function (){
        $("div,textarea").val("");
        $(".date_picker").val("");
        $("input[type='text']").val("");
        $("input[type='number']").val("");
        $("#facilityId option:first").prop("selected",'selected');
        $("#schedule option:first").prop("selected",'selected');
        $("#modeOfProcurement option:first").prop("selected",'selected');
        $("#country option:first").prop("selected",'selected');
        $("input[type='checkbox']").prop('checked', false);
        $(".multi-select-button").html("-- Select --");
        $("#natureOfTheSample option").prop('selected',false);
        clearBiologicalList();
        $("#beInboxFilter .current").text("Please Select");
    })
    $("#nextBtn").click(function (){
        showWaiting();
        $('#mainForm').submit();
    })
    $("#Next").click(function (){
        showWaiting();
        $("#actionType").val("next");
        $('#mainForm').submit();
    })
    $("#Back").click(function (){
        showWaiting();
        $("#actionType").val("back");
        $('#mainForm').submit();
    })
    $("#SaveDraft").click(function (){
        showWaiting();
        $("#actionType").val("saveDraft");
        $('#mainForm').submit();
    })
    $("#Submit").click(function (){
        showWaiting();
        $("#actionType").val("submit");
        $('#mainForm').submit();
    })
    $("#subApprovalEdit").click(function (){
        showWaiting();
        $("#actionType").val("next");
        $('#mainForm').submit();
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
        if (controlLi == currId) {
            return;
        }else if(currId == "PrepareForms"){
            showWaiting();
            $("#actionType").val("form");
            $('#mainForm').submit();
        }else if(currId == "PrepareDocuments"){
            showWaiting();
            $("#actionType").val("document");
            $('#mainForm').submit();
        }else if(currId == "PreparePreview"){
            showWaiting();
            $("#actionType").val("preview");
            $('#mainForm').submit();
        }
    });
})
function clearBiologicalList(){
    $("#select0 option").prop('selected',false);
    $("#select1 option").prop('selected',false);
    $("#select2 option").prop('selected',false);
    $("#select3 option").prop('selected',false);
    $("#select4 option").prop('selected',false);
    $("#select5 option").prop('selected',false);
    $("#select6 option").prop('selected',false);
}