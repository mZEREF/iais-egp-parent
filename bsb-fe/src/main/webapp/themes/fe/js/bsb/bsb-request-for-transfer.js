function stChange(obj){
    var num = $(obj).attr("name").split("--v--")[1];
    var scheduleType = $("#scheduleType"+"--v--"+num).val();
    if(scheduleType !== 'SCHTYPE006'){
        $("#agentFifth"+"--v--"+num).hide();
        $("#agentEpFifth"+"--v--"+num).show();
    } else if(scheduleType === "SCHTYPE006"){
        $("#agentEpFifth"+"--v--"+num).hide();
        $("#agentFifth"+"--v--"+num).show();
    } else{
        $("#agentEpFifth" + meta.separator + num).hide();
        $("#agentFifth" + meta.separator + num).hide();
    }
    if (scheduleType === 'Please Select' || scheduleType === '') {
        var bat = $("#batCode"+"--v--"+num);
        bat.html("<option value=\"\">Please select<\/option>");
        bat.next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>");
    } else {
        $.post('/bsb-web/bio-info/bio.do',
            {schedule: scheduleType},
            function (data) {
                var result = data.result;
                if (result === 'success') {
                    var queryResult = data.queryResult;
                    console.log(queryResult);
                    var optionString = "";
                    var optionString1 = "";
                    for (var i = 0; i < queryResult.length; i++) {
                        optionString += "<option value=\"" + queryResult[i] + "\">" + queryResult[i] + "</option>";
                        optionString1 += "<li data-value=\"" + queryResult[i] + "\" class=\"option\">" + queryResult[i] + "</li>"
                    }
                    var bat = $("#batCode"+"--v--"+num);
                    bat.html("<option value=\"\">Please select<\/option>" + optionString);
                    bat.next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>" + optionString1);
                } else {

                }
            }
        )
    }

    var docList = $("#documentList").val();
    var list = $("."+docList+"--v--"+num);

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

$(function (){

    $("#next").click(function (){
        showWaiting();
        $("[name='action_type']").val("doNext");
        // var optionVal = $("#facSelect option:selected").val();
        // $("#facId").val(optionVal);
        $("#mainForm").submit();
    });

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
    $("#edit").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });
})

