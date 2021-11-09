$(document).ready(function() {
    $('#abandonReasonSelect').change(function () {

        var abandonReason= $('#abandonReasonSelect option:selected').val();

        if(abandonReason == "ENDRA005"){
            $('#otherAbandonReason').attr("style","display: block");
        }else {
            $('#otherAbandonReason').attr("style","display: none");
        }
    });
});

$(document).ready(function () {
    $('#radioYes').click(function () {
        if($(this).prop('checked')){
            $('.endFromParts').show();
        }
    });

    $('#radioNo').click(function () {
        if($(this).prop('checked')){
            $('.endFromParts').hide();
        }
    });
});