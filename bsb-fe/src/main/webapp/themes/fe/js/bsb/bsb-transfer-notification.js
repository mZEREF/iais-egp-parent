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
    var num = $(obj).attr("name").split("--v--")[1];
    var scheduleType = $("#scheduleType"+"--v--"+num).val();
    if(scheduleType !== 'SCHTYPE006'){
        $("#agentFifth"+"--v--"+num).hide();
        $("#agentEpFifth"+"--v--"+num).show();
    } else if(scheduleType === "SCHTYPE006"){
        $("#agentEpFifth"+"--v--"+num).hide();
        $("#agentFifth"+"--v--"+num).show();
    }

    var docList = $("#documentList").val();
    var list = $("."+docList+"--v--"+num);

    //empty all new saved  file
    list.find(".file-upload-gp").find("div").empty();
    var deleteList = $("#existFiles"+"--v--"+num).val();
    var deleteSplit = deleteList.split(",");
    // add id into the delete list
    var deleteNewFiles = document.getElementById("deleteNewFiles");
    deleteNewFiles.value = deleteList;

    var docH3 = list.find("h3");
    addHtml(docH3,scheduleType);
}

//joint the title of file
function addHtml(docH3,sType){
    if(docH3.find("span").length === 0){
        if(sType !== 'SCHTYPE006'){
            docH3.html("Inventory: Biological Agents");
        } else{
            docH3.html("Inventory: Toxins");
        }
    }else {
        if(sType !== 'SCHTYPE006'){
            docH3.html("Inventory: Biological Agents <span class=\"mandatory otherQualificationSpan\">*</span>");
        } else{
            docH3.html("Inventory: Toxins <span class=\"mandatory otherQualificationSpan\">*</span>");
        }
    }

}



