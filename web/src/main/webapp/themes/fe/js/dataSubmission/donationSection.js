$(document).ready(function () {

    $('input[type="text"]').blur(function () {

        var curCenDonatedNum = $('#curCenDonatedNum').val();
        var otherCenDonatedNum = $('#otherCenDonatedNum').val();
        var resDonarNum = $('#resDonarNum').val();
        var curCenResDonatedNum = $('#curCenResDonatedNum').val();
        var otherCenResDonarNum = $('#otherCenResDonarNum').val();
        var trainingNum = $('#trainingNum').val();
        var totalNum = Number(curCenDonatedNum)+Number(otherCenDonatedNum)+Number(resDonarNum)+Number(curCenResDonatedNum)+Number(otherCenResDonarNum)+Number(trainingNum);


        $('#totalNum').html(totalNum);

        if(Number(curCenDonatedNum)>0){
            $('#isCurCenDonatedField').html('Which AR Centre was Gamete(s) / Embryo(s) Donated to? <span class="mandatory">*</span>')
        }else {
            $('#isCurCenDonatedField').html('Which AR Centre was Gamete(s) / Embryo(s) Donated to?')
        }

        if(Number(resDonarNum)>0||Number(curCenResDonatedNum)>0){
            $('#isCurCenResTypeField').html('Type of Research for Which Donated <span class="mandatory">*</span>')
        }else {
            $('#isCurCenResTypeField').html('Type of Research for Which Donated')
        }

        if(Number(otherCenResDonarNum)>0){
            $('#isInsSentToCurField').html('Other AR Centre / Institution Sent to <span class="mandatory">*</span>')
        }else {
            $('#isInsSentToCurField').html('Other AR Centre / Institution Sent to')
        }
    });



    $('#isCurCenDonated').change(function () {

        var reason= $('#isCurCenDonated option:selected').val();

        if("Others"==reason){
            $('#otherDonatedCenDisplay').attr("style","display: block");
            $('#directedDonorIdDisplay').attr("style","display: none");

        }else {
            $('#directedDonorIdDisplay').attr("style","display: block");
            $('#otherDonatedCenDisplay').attr("style","display: none");
        }

    });
    $('#isCurCenResTypeOther').change(function () {
        if($(this).is(':checked')){
            $('#curCenResTypeOtherDisplay').attr("style","display: block");
        }else {
            $('#curCenResTypeOtherDisplay').attr("style","display: none");
        }
    });

    $('#isInsSentToCur').change(function () {

        var reason= $('#isInsSentToCur option:selected').val();

        if("Others"==reason){
            $('#insSentToOtherCenDisplay').attr("style","display: block");
        }else {
            $('#insSentToOtherCenDisplay').attr("style","display: none");
        }

    });

    $('#donationReason').change(function () {

        var reason= $('#donationReason option:selected').val();

        if("DONRES004"==reason){
            $('#otherDonationReasonDisplay').attr("style","display: block");
        }else {
            $('#otherDonationReasonDisplay').attr("style","display: none");
        }

    });
});
