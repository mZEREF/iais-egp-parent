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
})