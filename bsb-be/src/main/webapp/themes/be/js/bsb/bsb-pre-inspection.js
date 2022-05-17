$(function () {
    $('#processingDecision').change(function () {
        const processingDecisionVal = $('#processingDecision option:selected').val();
        const rfiCheckBox = $('#rfiCheckBox');

        if (processingDecisionVal === 'MOHPRO002') {
            rfiCheckBox.show();
        } else {
            rfiCheckBox.hide();
        }
    })

    $("#AppPreInspRfiCheck").click(function () {
        if($(this).is(':checked')) {
            var url = "/bsb-be/eservice/INTRANET/MohBsbPreInspection/1/PrepareApplicationRfi";
            window.open(url,'_blank');
        }
    })

    var isClosePage = $("input[name='closePage']").val();
    if (isClosePage === "Y") {
        window.close();
    }
    $("#submitAppRfiBtn").click(function () {
        showWaiting();
        $("#mainForm").submit();
    });
})
