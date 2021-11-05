$(document).ready(function() {
    $('input[type="text"]').blur(function () {

        var immature = $('#immature').val();
        var abnormallyFertilised = $('#abnormallyFertilised').val();
        var unfertilised = $('#unfertilised').val();
        var atretic = $('#atretic').val();
        var damaged = $('#damaged').val();
        var lysedOrDegenerated = $('#lysedOrDegenerated').val();

        var unhealthyNum = $('#unhealthyNum').val();
        var otherDiscardedNum = $('#otherDiscardedNum').val();
        var totalNum ;

        var reason= $('#disposedType option:selected').val();
        switch (reason) {
            case "DISPTY001":
            case "DISPTY002":
            case "DISPTY003":
                totalNum = Number(immature)+
                    Number(abnormallyFertilised)+
                    Number(unfertilised)+
                    Number(atretic)+
                    Number(damaged)+
                    Number(lysedOrDegenerated)+
                    Number(otherDiscardedNum);
                break;
            case "DISPTY004":
            case "DISPTY005":
            case "DISPTY006":
                totalNum =
                    Number(unhealthyNum)+
                    Number(otherDiscardedNum);
                break;
            case "DISPTY007":
                totalNum=Number(otherDiscardedNum);break;
            default:
                totalNum=Number(otherDiscardedNum);

        }
        $('#totalNum').html(totalNum);

        if(Number(otherDiscardedNum)>0){
            $('#otherDiscardedReasonField').html('Other Reasons for Discarding <span class="mandatory">*</span>')
        }else {
            $('#otherDiscardedReasonField').html('Other Reasons for Discarding')
        }

    });

    $('#disposedType').change(function () {

        var reason= $('#disposedType option:selected').val();
        switch (reason) {
            case "DISPTY001":
            case "DISPTY002":
            case "DISPTY003":
                $('#otherDiscardedNumField').html('Type of Research for Which Donated');
                $('#oocyteDisplay').attr("style","display: block");
                $('#embryoDisplay').attr("style","display: none");
                break;
            case "DISPTY004":
            case "DISPTY005":
            case "DISPTY006":
                $('#otherDiscardedNumField').html('Type of Research for Which Donated');
                $('#embryoDisplay').attr("style","display: block");
                $('#oocyteDisplay').attr("style","display: none");
                break;
            case "DISPTY007":
                $('#oocyteDisplay').attr("style","display: none");
                $('#embryoDisplay').attr("style","display: none");
                $('#otherDiscardedNumField').html('Type of Research for Which Donated <span class="mandatory">*</span>');break;
            default:
                $('#oocyteDisplay').attr("style","display: none");
                $('#embryoDisplay').attr("style","display: none");
                $('#otherDiscardedNumField').html('Type of Research for Which Donated');

        }
    });
});

function keyNumericPress() {
    var keyCode = event.keyCode;
    event.returnValue = keyCode >= 48 && keyCode <= 57;
}