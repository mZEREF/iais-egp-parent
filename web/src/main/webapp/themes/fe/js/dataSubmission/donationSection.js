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

function bindDonorEvent() {
    $('#donatedType').change(maleOrFemaleDonorShow).trigger('change');
    $('input[type=radio][name=isOocyteDonorPatient]').change(femaleKnownIdentityShow).trigger('change');
    $('input[type=radio][name=isFemaleIdentityKnown]').change(femaleIdTypeshow).trigger('change');
    $('input[type=radio][name=femaleIdType]').change(femaleNumbershow).trigger('change');
    $('input[type=radio][name=isSpermDonorPatient]').change(maleKnownIdentityShow).trigger('change');
    $('input[type=radio][name=isMaleIdentityKnown]').change(maleIdTypeshow).trigger('change');
    $('input[type=radio][name=maleIdType]').change(maleNumbershow).trigger('change');
}

function maleOrFemaleDonorShow () {
    var donatedType = $("#donatedType").val();
    const displayOocyteDonorPatient = $('#displayOocyteDonorPatient');
    const displaySpermDonorPatient = $('#displaySpermDonorPatient');
    if(donatedType == 'DONTY001' || donatedType == 'DONTY002' ){
        displayOocyteDonorPatient.show();
        displaySpermDonorPatient.hide();
        clearFields(displaySpermDonorPatient);
        maleKnownIdentityShow ();
    } else if(donatedType == 'DONTY004'){
        displaySpermDonorPatient.show();
        displayOocyteDonorPatient.hide();
        clearFields(displayOocyteDonorPatient);
        femaleKnownIdentityShow ();
    } else if(donatedType == 'DONTY003'){
        displayOocyteDonorPatient.show();
        displaySpermDonorPatient.show();
    }else {
        displayOocyteDonorPatient.hide();
        clearFields(displayOocyteDonorPatient);
        femaleKnownIdentityShow ();
        displaySpermDonorPatient.hide();
        clearFields(displaySpermDonorPatient);
        maleKnownIdentityShow ();
    }
}

function femaleKnownIdentityShow () {
    const displayIsFemaleIdentityKnown = $('#displayIsFemaleIdentityKnown');
    if($(isOocyteDonorPatientYes).is(':checked')){
        displayIsFemaleIdentityKnown.show();
    } else {
        displayIsFemaleIdentityKnown.hide();
        clearFields(displayIsFemaleIdentityKnown);
        femaleIdTypeshow ()
    }
}

function femaleIdTypeshow () {
    const displayFemaleHaveNricFin = $('#displayFemaleHaveNricFin');
    if($(isFemaleIdentityKnownYes).is(':checked')){
        displayFemaleHaveNricFin.show();
    } else {
        displayFemaleHaveNricFin.hide();
        clearFields(displayFemaleHaveNricFin);
        femaleNumbershow ();
    }
}

function femaleNumbershow () {
    const displayFemaleNricFinNumber = $('#displayFemaleNricFinNumber');
    const displayPassportNumber = $('#displayFemalePassportNumber');
    if($(femaleIdTypeYes).is(':checked')){
        displayFemaleNricFinNumber.show();
        displayPassportNumber.hide();
        clearFields(displayPassportNumber);
    } else if($(femaleIdTypeNo).is(':checked')){
        displayPassportNumber.show();
        displayFemaleNricFinNumber.hide();
        clearFields(displayFemaleNricFinNumber);
    } else{
        displayPassportNumber.hide();
        clearFields(displayPassportNumber);
        displayFemaleNricFinNumber.hide();
        clearFields(displayFemaleNricFinNumber);
    }
}

function maleKnownIdentityShow () {
    const displayIsMaleIdentityKnown = $('#displayIsMaleIdentityKnown');
    if($(isSpermDonorPatientYes).is(':checked')){
        displayIsMaleIdentityKnown.show();
    } else {
        displayIsMaleIdentityKnown.hide();
        clearFields(displayIsMaleIdentityKnown);
        maleIdTypeshow ()
    }
}

function maleIdTypeshow () {
    const displayMaleHaveNricFin = $('#displayMaleHaveNricFin');
    if($(isMaleIdentityKnownYes).is(':checked')){
        displayMaleHaveNricFin.show();
    } else {
        displayMaleHaveNricFin.hide();
        clearFields(displayMaleHaveNricFin);
        maleNumbershow ();
    }
}

function maleNumbershow () {
    const displayMaleNricFinNumber = $('#displayMaleNricFinNumber');
    const displayMalePassportNumber = $('#displayMalePassportNumber');
    if($(maleIdTypeYes).is(':checked')){
        displayMaleNricFinNumber.show();
        displayMalePassportNumber.hide();
        clearFields(displayMalePassportNumber);
    } else if($(maleIdTypeNo).is(':checked')){
        displayMalePassportNumber.show();
        displayMaleNricFinNumber.hide();
        clearFields(displayMaleNricFinNumber);
    } else{
        displayMalePassportNumber.hide();
        clearFields(displayMalePassportNumber);
        displayMaleNricFinNumber.hide();
        clearFields(displayMaleNricFinNumber);
    }
}


$(document).ready(function () {

    $('input[type="text"]').blur(function () {
        changeTotalNum();

    });

    $('input[type=radio][name=directedDonation]').change(function () {
        if($(directedYes).is(':checked')){
            $('#recipientNoDisplay').attr("style","display: block");
        }else {
            $('#recipientNoDisplay').attr("style","display: none");
        }
    })

    bindDonorEvent();

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