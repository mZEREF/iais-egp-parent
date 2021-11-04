function schTypeChange(obj) {
    var meta = readSectionRepeatMetaData();
    var num = $(obj).attr("name").split(meta.separator)[1];
    var schedule = $("#scheduleType" + meta.separator + num).val();
    if (schedule !== 'SCHTYPE006' && schedule!=='') {
        $("#agentFifth" + meta.separator + num).hide();
        $("#agentEpFifth" + meta.separator + num).show();
        $("#toxinDoc" + meta.separator + num).hide();
    } else if (schedule === "SCHTYPE006") {
        $("#agentEpFifth" + meta.separator + num).hide();
        $("#agentFifth" + meta.separator + num).show();
        $("#toxinDoc" + meta.separator + num).show();
    }else{
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
                console.log("sssssss==" + bat);
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

$(function (){
    $("#savebtn").click(function (){
        $("#mainForm").submit();
    });

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

        }
        $("#mainForm").submit();
    })
})

