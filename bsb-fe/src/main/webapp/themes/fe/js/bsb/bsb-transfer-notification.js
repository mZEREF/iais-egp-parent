$(function (){
    //notification page
    $("#doConfirm").click(function (){
        showWaiting();
        $("[name='action_type']").val("doConfirm");
        $("#mainForm").submit();
    });

    //InnerFooter.jsp & notification page
    $("#saveButton").click(function (){
        showWaiting();
        $("[name='action_type']").val("doSave");
        $("#mainForm").submit();
    });

    // $("#saveDraft").click(function (){
    //     showWaiting();
    //     $("[name='action_type']").val("saveDraft");
    //     $("#mainForm").submit();
    // });

    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    $("#next").click(function (){
        $("#mainForm").submit();
    })

})
function stChange(obj){
    var meta = readSectionRepeatMetaData();
    var num = $(obj).attr("name").split(meta.separator)[1];
    var scheduleType = $("#scheduleType"+meta.separator+num).val();
    if(scheduleType !== 'SCHTYPE006'){
        $("#agentFifth"+meta.separator+num).hide();
        $("#agentEpFifth"+meta.separator+num).show();
    } else if(scheduleType === "SCHTYPE006"){
        $("#agentEpFifth"+meta.separator+num).hide();
        $("#agentFifth"+meta.separator+num).show();
    }
}



