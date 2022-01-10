$(document).ready(function () {
    $('input[name=arTechniquesUsed]').change(function () {
        if($('#arTechniquesUsedCheckAR_ATU_001').prop('checked')) {
            $('#ivfMandatory1').text('*');

            $('#ivfMandatory2').text('*');
        }else {
            $('#ivfMandatory1').text('');

            $('#ivfMandatory2').text('');
        }
        if($('#arTechniquesUsedCheckAR_ATU_002').prop('checked')) {
            $('#icsiMandatory1').text('*');

            $('#icsiMandatory2').text('*');
        }else {
            $('#icsiMandatory1').text('');

            $('#icsiMandatory2').text('');
        }
        if($('#arTechniquesUsedCheckAR_ATU_003').prop('checked')) {
            $('#giftMandatory1').text('*');

            $('#giftMandatory2').text('*');
        }else {
            $('#giftMandatory1').text('');

            $('#giftMandatory2').text('');
        }
        if($('#arTechniquesUsedCheckAR_ATU_004').prop('checked')) {
            $('#ziftMandatory1').text('*');

            $('#ziftMandatory2').text('*');
        }else {
            $('#ziftMandatory1').text('');

            $('#ziftMandatory2').text('');
        }
    });
});