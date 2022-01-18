function schTypeChange(obj) {
    var meta = readSectionRepeatMetaData();
    var num = $(obj).attr("name").split(meta.separator)[1];
    var scheduleType = $("#scheduleType" + meta.separator + num).val();
    if (scheduleType !== 'SCHTYPE006' && scheduleType !== '') {
        $("#agentFifth" + meta.separator + num).hide();
        $("#agentEpFifth" + meta.separator + num).show();
        $("#batDocument" + meta.separator + num).show();
    } else if (scheduleType === "SCHTYPE006") {
        $("#agentEpFifth" + meta.separator + num).hide();
        $("#agentFifth" + meta.separator + num).show();
        $("#batDocument" + meta.separator + num).show();
    } else {
        $("#agentEpFifth" + meta.separator + num).hide();
        $("#agentFifth" + meta.separator + num).hide();
    }
    if (scheduleType === 'Please Select' || scheduleType === '') {
        $("#bat" + meta.separator + num).html("<option value=\"\">Please select<\/option>");
        $("#bat" + meta.separator + num).next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>");
    } else {
        callAjaxGetBat(scheduleType,meta,num);
    }

    var docList = $("#documentList").val();
    var list = $("."+docList+"--v--"+num);

    //empty all new saved  file
    list.find(".file-upload-gp").find("div").empty();
    var deleteList = $("#existFiles"+"--v--"+num).val();
    // add id into the delete list
    var deleteNewFiles = document.getElementById("deleteNewFiles");
    appendInputValue(deleteNewFiles,deleteList);

    //empty all saved files
    var deleteSavedList = $("#existSavedFiles"+"--v--"+num).val();
    //add saved id into the delete list
    var deleteSavedFilesEl = document.getElementById("deleteExistFiles");
    appendInputValue(deleteSavedFilesEl,deleteSavedList);

    var docH3 = list.find("h3");
    addHtml(docH3,scheduleType);
}

function callAjaxGetBat(scheduleType,meta,num){
    $.post('/bsb-fe/bio-info/bio.do',
        {schedule: scheduleType},
        function (data) {
            var result = data.result;
            if (result == 'success') {
                var queryResult = data.queryResult;
                var optionString = "";
                var optionString1 = "";
                for (var i = 0; i < queryResult.length; i++) {
                    optionString += "<option value=\"" + queryResult[i] + "\">" + queryResult[i] + "</option>";
                    optionString1 += "<li data-value=\"" + queryResult[i] + "\" class=\"option\">" + queryResult[i] + "</li>"
                }
                $("#bat" + meta.separator + num).html("<option value=\"\">Please select<\/option>" + optionString);
                $("#bat" + meta.separator + num).next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>" + optionString1);
            } else {

            }
        }
    )
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

function appendInputValue(input, value) {
    if (input.value) {
        input.value = input.value + "," + value;
    } else {
        input.value = value;
    }
}

$(function () {
    //facilitySelectPage.jsp
    $("#facNextBtn").click(function () {
        var optionVal = $("#facSelect option:selected").val();
        if (optionVal == "") {
            $("#facSelectError").html("This is Mandatory");
        } else {
            showWaiting();
            $("[name='action_type']").val("doNext");
            $("#facSelectError").html("");
            $("#facId").val(optionVal);
            $("#mainForm").submit();
        }
    });

    $("#next").click(function () {
        showWaiting();
        $("[name='action_type']").val("doNext");
        // var optionVal = $("#facSelect option:selected").val();
        // $("#facId").val(optionVal);
        $("#mainForm").submit();
    });

    //notification page
    $("#doConfirm").click(function () {
        showWaiting();
        $("[name='action_type']").val("doConfirm");
        $("#mainForm").submit();
    });

    //InnerFooter.jsp & notification page
    $("#saveButton").click(function () {
        if($("#ensureT").is(':checked')){
            showWaiting();
            $("[name='action_type']").val("doSave");
            $("#mainForm").submit();
        }else {
            $("#submitDeclareModal").modal('show');
        }
    });

    $("#back").click(function () {
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    $("#edit").click(function () {
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    //notificationTypePage.jsp
    $("#nextBtn").click(function () {
        showWaiting();
        var optionValue = $("#selectType option:selected").val();
        if (optionValue == "1") {
            $("[name='action_type']").val("doConsume");
        } else if (optionValue == "2") {
            $("[name='action_type']").val("doDisposal");
        } else if (optionValue == "3") {
            $("[name='action_type']").val("doExport");
        } else if (optionValue == "4") {
            $("[name='action_type']").val("doReceipt");
        } else if (optionValue == "5") {
            $("[name='action_type']").val("doRequestTransfer");
        } else if (optionValue == "6") {
            $("[name='action_type']").val("doTransferNotification");
        } else if (optionValue == "7") {
            $("[name='action_type']").val("doAckOfReceiptOfTransfer");
        } else if (optionValue == "8") {
            $("[name='action_type']").val("doReportInventory");
        }
        $("#mainForm").submit();
    })
})

