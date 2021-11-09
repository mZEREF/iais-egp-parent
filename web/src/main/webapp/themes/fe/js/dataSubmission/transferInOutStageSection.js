$(document).ready(function () {
 /*   $('#transferTypeOut').click(function () {
        if($(this).val()=='in'){
            $('.inFromParts').attr("style","display: block");
            $('.outFromParts').attr("style","display: none");
        }
    });
       $('#transferTypeOut').click(function () {
           if($(this).val()=='out'){
               $('.outFromParts').attr("style","display: block");
               $('.inFromParts').attr("style","display: none");
           }

       });*/

   $('#transferTypeIn').click(function () {
        if($(this).val()=='in'){
            $('.outFromParts').hide();
            $('.inFromParts').show();
        }

    });
    $('#transferTypeOut').click(function () {
        if($(this).val() == 'out'){
            $('.inFromParts').hide();
            $('.outFromParts').show();

        }
    });


});