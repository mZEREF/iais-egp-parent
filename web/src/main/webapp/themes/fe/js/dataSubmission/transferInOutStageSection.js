$(document).ready(function () {
    $('#transferTypeIn').click(function () {
        if($(this).prop('checked')) {
            $('.outFromParts').hide();
            $('.inFromParts').show();

        }
    });
    $('#transferTypeOut').click(function () {
        if($(this).prop('checked')) {
            $('.inFromParts').hide();
            $('.outFromParts').show();
        }
    });
    toggleOnSelect("#transInFromHciCode",'AR_TIF_003', 'othersInFrom');
    toggleOnSelect("#transOutToHciCode",'AR_TIF_003', 'othersOutFrom');

    toggleOnCheck('#transferredCheckAR_WWT_001','transferred0')
    toggleOnCheck('#transferredCheckAR_WWT_002','transferred1')
    toggleOnCheck('#transferredCheckAR_WWT_003','transferred2')

});