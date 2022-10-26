$(function () {
    $("#processingDecision").change(function () {
        var selectValue = $(this).val();
        if (selectValue === 'MOHPRO002') {
            $('#rfiSubContent').show();
        } else {
            $('#rfiSubContent').hide();
        }
    })
})
