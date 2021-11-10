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
    toggleOnSelect("#transInFromHciCode",'${DataSubmissionConsts.TRANSFERRED_IN_FROM_OTHERS}', 'othersInFrom');
    toggleOnSelect("#transOutToHciCode",'${DataSubmissionConsts.TRANSFERRED_IN_FROM_OTHERS}', 'othersOutFrom');

});
