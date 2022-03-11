$(document).ready(function () {
    $('input[name=arTechniquesUsed]').change(function () {
        if($('#arTechniquesUsedCheckAR_ATU_001').prop('checked')) {
            $('#ivfMandatory1').text('*');
            $('#ivfMandatory2').text('*');
            $('input[name=freshOocytesInseminatedNum]').attr('disabled',false)
            $('input[name=thawedOocytesInseminatedNum]').attr('disabled',false)
        }else {
            $('#ivfMandatory1').text('');
            $('#ivfMandatory2').text('');
            $('input[name=freshOocytesInseminatedNum]').attr('disabled',true)
            $('input[name=thawedOocytesInseminatedNum]').attr('disabled',true)
        }
        if($('#arTechniquesUsedCheckAR_ATU_002').prop('checked')) {
            $('#icsiMandatory1').text('*');
            $('#icsiMandatory2').text('*');
            $('input[name=freshOocytesMicroInjectedNum]').attr('disabled',false)
            $('input[name=thawedOocytesMicroinjectedNum]').attr('disabled',false)
        }else {
            $('#icsiMandatory1').text('');
            $('#icsiMandatory2').text('');
            $('input[name=freshOocytesMicroInjectedNum]').attr('disabled',true)
            $('input[name=thawedOocytesMicroinjectedNum]').attr('disabled',true)
        }
        if($('#arTechniquesUsedCheckAR_ATU_003').prop('checked')) {
            $('#giftMandatory1').text('*');
            $('#giftMandatory2').text('*');
            $('input[name=freshOocytesGiftNum]').attr('disabled',false)
            $('input[name=thawedOocytesGiftNum]').attr('disabled',false)
        }else {
            $('#giftMandatory1').text('');
            $('#giftMandatory2').text('');
            $('input[name=freshOocytesGiftNum]').attr('disabled',true)
            $('input[name=thawedOocytesGiftNum]').attr('disabled',true)
        }
        if($('#arTechniquesUsedCheckAR_ATU_004').prop('checked')) {
            $('#ziftMandatory1').text('*');
            $('#ziftMandatory2').text('*');
            $('input[name=freshOocytesZiftNum]').attr('disabled',false)
            $('input[name=thawedOocytesZiftNum]').attr('disabled',false)
        }else {
            $('#ziftMandatory1').text('');
            $('#ziftMandatory2').text('');
            $('input[name=freshOocytesZiftNum]').attr('disabled',true)
            $('input[name=thawedOocytesZiftNum]').attr('disabled',true)
        }
    });
});