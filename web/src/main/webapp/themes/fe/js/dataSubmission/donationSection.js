$(document).ready(function () {

    $('input[type="number"]').blur(function () {

        var donResForTreatNum = $('#donResForTreatNum').val();
        var donResForCurCenNotTreatNum = $('#donResForCurCenNotTreatNum').val();
        var trainingNum = $('#trainingNum').val();
        var treatNum = $('#treatNum').val();

        var totalNum = Number(donResForTreatNum)+Number(donResForCurCenNotTreatNum)+Number(trainingNum)+Number(treatNum);


        $('#totalNum').html(totalNum);

    });



    $('#donatedForResearch').change(function () {
        if($(this).is(':checked')){
            $('#researchDisplay').attr("style","display: block");
        }else {
            $('#researchDisplay').attr("style","display: none");
        }
    });

    $('#donatedForTraining').change(function () {
        if($(this).is(':checked')){
            $('#trainingDisplay').attr("style","display: block");
        }else {
            $('#trainingDisplay').attr("style","display: none");
        }
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