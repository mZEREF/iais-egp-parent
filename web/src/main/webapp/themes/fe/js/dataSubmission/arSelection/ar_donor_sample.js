const SAMPLE_TYPE_FRESH_OOCYTE = 'DONTY001';
const SAMPLE_TYPE_OOCYTE = 'DONTY002';
const SAMPLE_TYPE_EMBRYO = 'DONTY003';
const SAMPLE_TYPE_SPERM = 'DONTY004';
const SAMPLE_TYPE_FRESH_SPERM = 'DONTY005';

$(document).ready(function () {
    bindAllEvent();
});

function bindAllEvent() {
    const sampleType = $('#sampleType');
    sampleType.change(femaleDonorDivShow).trigger('change');
    $('input[name="donorIdentityKnown"]').change(knowFIdDivShow).trigger('change');
    $('input[name="hasIdNumberF"]').change(femaleNumberShow).trigger('change');

    sampleType.change(maleDonorDivShow).trigger('change');
    $('input[name="maleDonorIdentityKnow"]').change(knowMIdDivShow).trigger('change');
    $('input[name="hasIdNumberM"]').change(maleNumberShow).trigger('change');

    $('input[name="localOrOversea"]').change(sampleFromRowShow).trigger('change');
    $('#donationReason').change(reasonOtherDivShow).trigger('change')
    $('#donatedForResearch').change(researchDivShow).trigger('change');
    $('#donatedForResearchOther').change(otherResearchStarShow).trigger('change');

    $('#donatedForTraining').change(trainingNumRowShow).trigger('change');
    $('#donatedForTreatment').change(treatmentNumRowShow).trigger('change');

    $('input[name="donResForTreatNum"]').change(donatedNumChange);
    $('input[name="donResForCurCenNotTreatNum"]').change(donatedNumChange);
    $('input[name="trainingNum"]').change(donatedNumChange);
    $('input[name="treatNum"]').change(donatedNumChange);

    $('input[name="donorSampleAge"]').change(ageConfirmShow);
    $('input[name="maleDonorSampleAge"]').change(maleAgeConfirmShow);
}

function femaleDonorDivShow() {
    const femaleDonorDiv = $('#femaleDonorDiv');
    if (showFemale()) {
        femaleDonorDiv.show();
    } else {
        femaleDonorDiv.hide();
        clearFields(femaleDonorDiv);
        knowFIdDivShow();
    }
}

function knowFIdDivShow() {
    const isKnowIdentityFVal = $('input[name="donorIdentityKnown"]:checked').val();
    const knowFIdDiv = $('#knowFIdDiv');
    if (isKnowIdentityFVal === 'DIK001') {
        knowFIdDiv.show();
    } else {
        knowFIdDiv.hide();
        clearFields(knowFIdDiv);
        femaleNumberShow();
    }
}

function femaleNumberShow() {
    const isNricFemaleVal = $('input[name="hasIdNumberF"]:checked').val();
    const fPassportNumberField = $('#fPassportNumberField');
    const fNricNumberField = $('#fNricNumberField');
    const idNumberRow = $('#idNumberRow');
    const idNumber = $('input[name="idNumber"]')
    fPassportNumberField.hide();
    fNricNumberField.hide();
    idNumberRow.show();
    if (isNricFemaleVal === '1') {
        fNricNumberField.show();
        idNumber.attr('maxlength', 9);
    } else if (isNricFemaleVal === '0') {
        fPassportNumberField.show();
        idNumber.attr('maxlength', 20);
    } else {
        idNumberRow.hide();
    }
}

function maleDonorDivShow() {
    const maleDonorDiv = $('#maleDonorDiv');
    if (showMale()) {
        maleDonorDiv.show();
    } else {
        maleDonorDiv.hide();
        clearFields(maleDonorDiv);
        knowMIdDivShow();
    }
}

function knowMIdDivShow() {
    const isKnowIdentityMVal = $('input[name="maleDonorIdentityKnow"]:checked').val();
    const knowMIdDiv = $('#knowMIdDiv');
    if (isKnowIdentityMVal === '1') {
        knowMIdDiv.show();
    } else {
        knowMIdDiv.hide();
        clearFields(knowMIdDiv);
        maleNumberShow();
    }
}

function maleNumberShow() {
    const isNricMaleVal = $('input[name="hasIdNumberM"]:checked').val();
    const mPassportNumberField = $('#mPassportNumberField');
    const mNricNumberField = $('#mNricNumberField');
    const mIdNumberRow = $('#mIdNumberRow');
    const idNumberMale = $('input[name="idNumberMale"]')
    mPassportNumberField.hide();
    mNricNumberField.hide();
    mIdNumberRow.show();
    if (isNricMaleVal === '1') {
        mNricNumberField.show();
        idNumberMale.attr('maxlength', 9)
    } else if (isNricMaleVal === '0') {
        mPassportNumberField.show();
        idNumberMale.attr('maxlength', 20)
    } else {
        mIdNumberRow.hide();
    }
}

function sampleFromRowShow() {
    const localOrOverseaVal = $('input[name="localOrOversea"]:checked').val();
    const sampleFromLocal = $('#sampleFromLocal');
    const sampleFromOversea = $('#sampleFromOversea');
    disableContent()
    if (localOrOverseaVal === '1') {
        sampleFromLocal.show();
        sampleFromOversea.hide();
        sampleFromOversea.find('input').attr("disabled", true);
        clearFields(sampleFromOversea);
    } else if (localOrOverseaVal === '0') {
        sampleFromOversea.show();
        sampleFromOversea.find('input').attr("disabled", false);
        sampleFromLocal.hide();
        clearFields(sampleFromLocal);
    } else {
        sampleFromLocal.hide();
        sampleFromOversea.hide();
        clearFields(sampleFromLocal);
        clearFields(sampleFromOversea);
    }
}


function reasonOtherDivShow() {
    const donationReasonVal = $('#donationReason option:selected').val();
    const reasonOtherRow = $('#reasonOtherRow')
    if (donationReasonVal === 'DONRES004') {
        reasonOtherRow.show()
    } else {
        reasonOtherRow.hide()
        clearFields(reasonOtherRow)
    }
}

function researchDivShow() {
    const isResearch = $('#donatedForResearch').prop('checked');
    const researchDiv = $('#researchDiv');
    if (isResearch) {
        researchDiv.show();
    } else {
        researchDiv.hide();
        clearFields(researchDiv);
        otherResearchStarShow();
    }
}

function otherResearchStarShow() {
    const isOtherResearch = $('#donatedForResearchOther').prop('checked');
    const otherResearchStar = $('#otherResearchStar');
    if (isOtherResearch) {
        otherResearchStar.show();
    } else {
        otherResearchStar.hide();
    }
}

function trainingNumRowShow() {
    const isTraining = $('#donatedForTraining').prop('checked');
    const trainingNumRow = $('#trainingNumRow');
    if (isTraining) {
        trainingNumRow.show();
    } else {
        trainingNumRow.hide();
        clearFields(trainingNumRow);
    }
}

function treatmentNumRowShow() {
    const isTreatment = $('#donatedForTreatment').prop('checked');
    const treatmentNumRow = $('#treatmentNumRow');
    if (isTreatment) {
        treatmentNumRow.show();
    } else {
        treatmentNumRow.hide();
        clearFields(treatmentNumRow);
    }
}

function donatedNumChange() {
    let num = 0;
    const donResForTreatNum = parseInt($('input[name="donResForTreatNum"]').val());
    const donResForCurCenNotTreatNum = parseInt($('input[name="donResForCurCenNotTreatNum"]').val());
    const trainingNum = parseInt($('input[name="trainingNum"]').val());
    const treatNum = parseInt($('input[name="treatNum"]').val());
    if (donResForTreatNum) num += donResForTreatNum;
    if (donResForCurCenNotTreatNum) num += donResForCurCenNotTreatNum;
    if (trainingNum) num += trainingNum;
    if (treatNum) num += treatNum;
    $('#donatedNum').html(num);
}

function ageConfirmShow() {
    const sampleType = $('#sampleType option:selected').val();
    const femaleAgeVal = parseInt($('input[name="donorSampleAge"]').val());

    if (sampleType) {
        if (femaleAgeVal>35 || femaleAgeVal<21) {
            $('#oocyteAgeConfirm').show();
        }
    }
}

function maleAgeConfirmShow() {
    const sampleType = $('#sampleType option:selected').val();
    const maleAgeVal = parseInt($('input[name="maleDonorSampleAge"]').val());

    if (sampleType) {
        if (maleAgeVal>40 || maleAgeVal<21) {
            $('#spermAgeConfirm').show();
        }
    }
}

function overAgeRange(fAge, mAge, maxAge) {
    if (fAge) {
        if (fAge < 21 || fAge > maxAge) {
            return true;
        }
    }
    if (mAge) {
        if (mAge < 21 || mAge > maxAge) {
            return true;
        }
    }
    return false;
}

function showFemale() {
    const sampleType = $('#sampleType option:selected').val();
    return sampleType === SAMPLE_TYPE_FRESH_OOCYTE || sampleType === SAMPLE_TYPE_OOCYTE || sampleType === SAMPLE_TYPE_EMBRYO;
}

function showMale() {
    const sampleType = $('#sampleType option:selected').val();
    return sampleType === SAMPLE_TYPE_EMBRYO || sampleType === SAMPLE_TYPE_SPERM || sampleType === SAMPLE_TYPE_FRESH_SPERM;
}