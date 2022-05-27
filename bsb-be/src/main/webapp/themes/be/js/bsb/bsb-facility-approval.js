$(function (){
    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val('back');
        $("#mainForm").submit();
    });

    $("#clear").click(function () {
        showWaiting();
        $("input[name='action_type']").val("clear");
        $("#mainForm").submit();
    });

    $("#processingDecision").change(function () {
        var decision = $("#processingDecision").val();
        var currentStatus = $("#currentStatus").val();
        if(decision === 'MOHPRO007' && currentStatus === 'BSBAPST001' || decision === 'MOHPRO009' && currentStatus === 'BSBAPST002'){
            $("#selectMohUserDiv").show();
        }else{
            $("#selectMohUserDiv").hide();
        }
    })

    $("#submitBtn").click(function () {
        showWaiting();
        $("input[data-radio-type='facilityAgentRadio']").each(function (){
            $(this).removeAttr("disabled");
        })
        $("input[name='action_type']").val("submit");
        $('#mainForm').submit();
    })
})
