$(function (){
    $("#processingDecision").change(function (){
        var decision = $(this).val();
        if('MOHPRO017' === decision){
            $("#rfiSection").show();
        } else {
            $("#rfiSection").hide();
        }
    });
});
