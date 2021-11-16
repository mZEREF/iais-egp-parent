function schTypeChange(obj) {
    if (obj!=null) {
        var meta = readSectionRepeatMetaData();
        var num = $(obj).attr("name").split(meta.separator)[1];
        var schedule = $("#scheduleType" + meta.separator + num).val();
        if (schedule !== 'SCHTYPE006' && schedule !== '') {
            $("#agentFifth" + meta.separator + num).hide();
            $("#agentEpFifth" + meta.separator + num).show();
            $("#toxinDoc" + meta.separator + num).hide();
        } else if (schedule === "SCHTYPE006") {
            $("#agentEpFifth" + meta.separator + num).hide();
            $("#agentFifth" + meta.separator + num).show();
            $("#toxinDoc" + meta.separator + num).show();
        } else {
            $("#agentEpFifth" + meta.separator + num).hide();
            $("#agentFifth" + meta.separator + num).hide();
            $("#toxinDoc" + meta.separator + num).hide();
        }
        if (schedule === 'Please Select' || schedule === '') {
            $("#bat" + meta.separator + num).html("<option value=\"\">Please select<\/option>");
            $("#bat" + meta.separator + num).next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>");
        } else {
            $.post('/bsb-fe/bio-info/bio.do',
                {schedule: schedule},
                function (data) {
                    var result = data.result;
                    if (result == 'success') {
                        var queryResult = data.queryResult;
                        console.log(queryResult);
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
    }
}
$(function (){
    //facilitySelectPage.jsp
    $("#facNextBtn").click(function (){
        var optionVal = $("#facSelect option:selected").val();
        if (optionVal == "") {
            $("#facSelectError").html("This is Mandatory");
        }else{
            showWaiting();
            $("[name='action_type']").val("doNext");
            $("#facSelectError").html("");
            $("#facId").val(optionVal);
            $("#mainForm").submit();
        }
    });

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

    //notificationTypePage.jsp
    $("#nextBtn").click(function () {
        showWaiting();
        var optionValue = $("#selectType option:selected").val();
        if (optionValue == "1") {
            $("[name='action_type']").val("doConsume");
        }else if (optionValue == "2") {
            $("[name='action_type']").val("doDisposal");
        }else if (optionValue == "3") {
            $("[name='action_type']").val("doExport");
        }else if (optionValue == "4") {
            $("[name='action_type']").val("doReceipt");
        }else if (optionValue == "5") {
            $("[name='action_type']").val("doRequestTransfer");
        }else if (optionValue == "6") {
            $("[name='action_type']").val("doTransferNotification");
        }else if (optionValue == "7") {
            $("[name='action_type']").val("doAckOfReceiptOfTransfer");
        }else if (optionValue == "8") {
            $("[name='action_type']").val("doReportInventory");
        }
        $("#mainForm").submit();
    })
})

