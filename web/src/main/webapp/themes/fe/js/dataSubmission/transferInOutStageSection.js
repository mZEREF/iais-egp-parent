$(document).ready(function () {
    $('#transferTypeIn').click(function () {
        if($(this).prop('checked')){
            $('.inFromParts').show();
        }else {
            $('.inFromParts').hide();
        }
    });

    $('#transferTypeOut').click(function () {
        if($(this).prop('checked')){
            $('.outFromParts').show();
        }else {
            $('.outFromParts').hide();
        }
    });
});