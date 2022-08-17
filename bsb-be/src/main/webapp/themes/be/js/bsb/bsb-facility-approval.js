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
        // TODO: check these app status and decision
        if (decision === 'MOHPRO007' && currentStatus === 'BSBAPST030' || decision === 'BSBAPST031' && currentStatus === 'BSBAPST032') {
            $("#selectMohUserDiv").show();
            $("#rfiSubContent").hide();
        } else if (decision === 'MOHPRO002') {
            $("#rfiSubContent").show();
            $("#selectMohUserDiv").hide();
            var url = "/bsb-web/eservice/INTRANET/MohFacilityApprovalDORecommendation/1/PrepareRfi";
            window.open(url,'_blank');
        } else {
            $("#selectMohUserDiv").hide();
            $("#rfiSubContent").hide();
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
