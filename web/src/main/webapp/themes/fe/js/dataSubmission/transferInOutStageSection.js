$(document).ready(function () {
    $('#transferTypeOut').click(function () {

            $('.inFromParts').attr("style","display: block");
            $('.outFromParts').attr("style","display: none");

    });
       $('#transferTypeOut').click(function () {

               $('.outFromParts').attr("style","display: block");
               $('.inFromParts').attr("style","display: none");


       });

/*   $('#transferTypeIn').click(function () {
            $('.outFromParts').hide();
            $('.inFromParts').show();

    });
    $('#transferTypeOut').click(function () {
            $('.inFromParts').hide();
            $('.outFromParts').show();

    });*/
    $('#inFromSelect').change(function () {

        var inFromreason= $('#inFromSelect option:selected').val();

        if("AR_TIT_003"==inFromreason){
            $('#inFromOthersParts').attr("style","display: block");
        }else {
            $('#inFromOthersParts').attr("style","display: none");
        }

    });
    $('#outFromSelect').change(function () {

        var outFromreason= $('#outFromSelect option:selected').val();

        if("AR_TIT_003"==outFromreason){
            $('#outFromOthersParts').attr("style","display: block");
        }else {
            $('#outFromOthersParts').attr("style","display: none");
        }

    });

});