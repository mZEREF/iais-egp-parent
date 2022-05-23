$(document).ready(function() {
    $('#reasonSelect').change(function () {

        othersReasonDisplay();

    });
    $('input:radio[name="indicatedRadio"]').change(function () {

        var reason = $('input[name="indicatedRadio"]:checked').val();

        if("1"==reason){
            $('#reasonDisplay0').attr("style","display: none");
            $('#reasonDisplay1').attr("style","display: block");
            othersReasonDisplay();
        }
        if("0"==reason) {
            var oldReason = $('input[name="oldReason"]').val();

            if(oldReason.includes('EFOR0')){
                $('input[name="textReason"]').val('');
                $('input[name="oldReason"]').val('');
            }
            $('#reasonDisplay0').attr("style","display: block");
            $('#reasonDisplay1').attr("style","display: none");
            $('#othersReason').attr("style","display: none");
        }

    });


});

function othersReasonDisplay() {
    var reason= $('#reasonSelect option:selected').val();

    if("EFOR004"==reason){
        $('#othersReason').attr("style","display: block");
    }else {
        $('#othersReason').attr("style","display: none");
    }
}