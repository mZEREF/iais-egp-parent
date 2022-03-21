function changeTotalNum() {
    var totalNum = 0 ;
    if($('#donatedForResearch').is(':checked')){
        var donResForTreatNum = $('#donResForTreatNum').val();
        var donResForCurCenNotTreatNum = $('#donResForCurCenNotTreatNum').val();
        totalNum=totalNum+Number(donResForTreatNum)+Number(donResForCurCenNotTreatNum);
    }
    if($('#donatedForTraining').is(':checked')){
        var trainingNum = $('#trainingNum').val();
        totalNum=totalNum+Number(trainingNum);
    }
    if($('#donatedForTreatment').is(':checked')){
        var treatNum = $('#treatNum').val();
        totalNum=totalNum+Number(treatNum);
    }

    $('#totalNum').html(totalNum);
}

$(document).ready(function () {

    $('input[type="text"]').blur(function () {
        changeTotalNum();

    });



    $('#donatedForResearch').change(function () {
        if($(this).is(':checked')){
            $('#researchDisplay').attr("style","display: block");
        }else {
            $('#researchDisplay').attr("style","display: none");
        }
        changeTotalNum();
    });

    $('#donatedForTraining').change(function () {
        if($(this).is(':checked')){
            $('#trainingDisplay').attr("style","display: block");
        }else {
            $('#trainingDisplay').attr("style","display: none");
        }
        changeTotalNum();
    });

    $('#donatedForTreatment').change(function () {
        if($(this).is(':checked')){
            $('#treatmentDisplay').attr("style","display: block");
        }else {
            $('#treatmentDisplay').attr("style","display: none");
        }
        changeTotalNum();
    });

    $('#donatedForResearchOther').change(function () {
        if($(this).is(':checked')){
            $('#donatedForResearchOtherDisplay').attr("style","display: block");
        }else {
            $('#donatedForResearchOtherDisplay').attr("style","display: none");
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