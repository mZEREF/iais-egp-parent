$(document).ready(function() {
    $('input[type="number"]').blur(function () {

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
                $('#otherDiscardedNumField').html('Discarded for Other Reasons');
                $('#oocyteDisplay').attr("style","display: block");
                $('#embryoDisplay').attr("style","display: none");
                break;
            case "DISPTY004":
            case "DISPTY005":
            case "DISPTY006":
                $('#otherDiscardedNumField').html('Discarded for Other Reasons');
                $('#embryoDisplay').attr("style","display: block");
                $('#oocyteDisplay').attr("style","display: none");
                break;
            case "DISPTY007":
                $('#oocyteDisplay').attr("style","display: none");
                $('#embryoDisplay').attr("style","display: none");
                $('#otherDiscardedNumField').html('Discarded for Other Reasons <span class="mandatory">*</span>');break;
            default:
                $('#oocyteDisplay').attr("style","display: none");
                $('#embryoDisplay').attr("style","display: none");
                $('#otherDiscardedNumField').html('Discarded for Other Reasons');

        }
    });
});
