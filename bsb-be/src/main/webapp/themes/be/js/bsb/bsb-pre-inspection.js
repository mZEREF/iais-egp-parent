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
            var url = "/bsb-web/eservice/INTRANET/MohBsbPreInspection/1/PrepareApplicationRfi";
            window.open(url,'_blank');
        }
    })
})
