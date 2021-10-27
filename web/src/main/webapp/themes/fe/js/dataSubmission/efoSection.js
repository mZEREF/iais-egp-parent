$(document).ready(function() {
    $('#reasonSelect').change(function () {

        var reason= $('#reasonSelect option:selected').val();

        if("EFOR004"==reason){
            $('#othersReason').attr("style","display: block");
        }else {
            $('#othersReason').attr("style","display: none");
        }

    });
});