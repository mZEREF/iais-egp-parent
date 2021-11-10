var liveBirth = "OUTOPRE001";
var noLiveBirth = "OUTOPRE002";
var unknown = "OUTOPRE003";
var others = "OUTOPRE004";

var singleton = "OSIOU001";
var twin = "OSIOU002";
var triplet = "OSIOU003";
var quadruplet = "OSIOU004";
var unknown = "OSIOU005";
$(document).ready(function () {
    bindAllEvent();
    triggerAllEventOnce()
});

function bindAllEvent() {
    $('#firstUltrasoundOrderShow').change(firstUltrasoundOrderShowChangeFunction);
    $('#pregnancyOutcome').change(pregnancyOutcomeChangeFunction);
    $('input[name="birthPlace"]').change(birthPlaceChangeFunction);
    $('input[name="babyDetailsUnknown"]').change(babyDetailsUnknownChangeFunction);
    $('#NICUCareBabyNum').change(NICUCareBabyNumChangeFunction);
    $('#l2CareBabyNum').change(l2CareBabyNumChangeFunction);
    $('#l3CareBabyNum').change(l3CareBabyNumChangeFunction);
    $("#deliveryDateUnknown").change(deliveryDateCheckboxChangeFunction);
    let $maleLiveBirthNum = $('#maleLiveBirthNum');
    $maleLiveBirthNum.change(changeTotalLiveBirthNum);
    $maleLiveBirthNum.change(changeBabySection);
    let $femaleLiveBirthNum = $('#femaleLiveBirthNum');
    $femaleLiveBirthNum.change(changeTotalLiveBirthNum);
    $femaleLiveBirthNum.change(changeBabySection);
    bindBirthDefect();
    bindDefectType();
}

function triggerAllEventOnce() {
    $('#firstUltrasoundOrderShow').trigger('change');
    $('#pregnancyOutcome').trigger('change');
    $('input[name="birthPlace"]').trigger('change');
    $('input[name="babyDetailsUnknown"]').trigger('change');
    $('#NICUCareBabyNum').trigger('change');
    $('#l2CareBabyNum').trigger('change');
    $('#l3CareBabyNum').trigger('change');
    $("#deliveryDateUnknown").trigger('change');
    $('#maleLiveBirthNum').trigger('change');
    $('#femaleLiveBirthNum').trigger('change');
    $('.birthDefect').trigger('change');
    $('.defectType').trigger('change');
}

function firstUltrasoundOrderShowChangeFunction() {
    let pregnancyOutcomeVal = $('#pregnancyOutcome option:selected').val();
    let firstUltrasoundOrderShowVal = $('#firstUltrasoundOrderShow option:selected').val();

    let $wasSelFoeReduCarryOutDiv = $('#wasSelFoeReduCarryOutDiv');
    let $stillBirthNumSection = $('#stillBirthNumSection');

    if (pregnancyOutcomeVal == noLiveBirth || (pregnancyOutcomeVal == liveBirth && firstUltrasoundOrderShowVal != singleton)) {
        $stillBirthNumSection.show();
    } else {
        $stillBirthNumSection.hide();
    }

    if (pregnancyOutcomeVal == liveBirth && firstUltrasoundOrderShowVal != singleton) {
        $wasSelFoeReduCarryOutDiv.show();
    } else {
        $wasSelFoeReduCarryOutDiv.hide();
    }
}

function pregnancyOutcomeChangeFunction() {
    let pregnancyOutcomeVal = $('#pregnancyOutcome option:selected').val();
    let firstUltrasoundOrderShowVal = $('#firstUltrasoundOrderShow option:selected').val();

    let $liveBirthNumSection = $('#liveBirthNumSection');
    let $stillBirthNumSection = $('#stillBirthNumSection');
    let $wasSelFoeReduCarryOutDiv = $('#wasSelFoeReduCarryOutDiv');
    let $otherPregnancyOutcomeDiv = $('#otherPregnancyOutcomeDiv');
    let $deliverySection = $('#deliverySection');
    let $deliveryDateFieldMandatory = $('#deliveryDateFieldMandatory');
    let $babyDetailsUnknownFieldMandatory = $('#babyDetailsUnknownFieldMandatory');


    if (pregnancyOutcomeVal == liveBirth) {
        $liveBirthNumSection.show();
        $deliveryDateFieldMandatory.show();
        $babyDetailsUnknownFieldMandatory.show();
    } else {
        $liveBirthNumSection.hide();
        $deliveryDateFieldMandatory.hide();
        $babyDetailsUnknownFieldMandatory.hide();
    }

    if (pregnancyOutcomeVal == noLiveBirth || (pregnancyOutcomeVal == liveBirth && firstUltrasoundOrderShowVal != singleton)) {
        $stillBirthNumSection.show();
    } else {
        $stillBirthNumSection.hide();
    }

    if (pregnancyOutcomeVal == liveBirth && firstUltrasoundOrderShowVal != singleton) {
        $wasSelFoeReduCarryOutDiv.show();
    } else {
        $wasSelFoeReduCarryOutDiv.hide();
    }

    if (pregnancyOutcomeVal == unknown) {
        $deliverySection.hide();
    } else {
        $deliverySection.show();
    }

    if (pregnancyOutcomeVal == others) {
        $otherPregnancyOutcomeDiv.show();
    } else {
        $otherPregnancyOutcomeDiv.hide();
    }
}

function birthPlaceChangeFunction() {
    let $localBirthPlaceDiv = $('#localBirthPlaceDiv');
    if ($('input[name="birthPlace"]:checked').val() == "Local Birth") {
        $localBirthPlaceDiv.show();
    } else {
        $localBirthPlaceDiv.hide();
    }
}

function babyDetailsUnknownChangeFunction() {
    let $pregnancyOutcomeStageBabySection = $('.pregnancyOutcomeStageBabySection');
    if ($('input[name="babyDetailsUnknown"]:checked').val() == 'true') {
        $pregnancyOutcomeStageBabySection.hide();
    } else {
        $pregnancyOutcomeStageBabySection.show();
    }
}

function NICUCareBabyNumChangeFunction() {
    var reason = $('#NICUCareBabyNum option:selected').html();
    let $careBabyNumSection = $('#careBabyNumSection');
    if (parseInt(reason) > 0) {
        $careBabyNumSection.show();
    } else {
        $careBabyNumSection.hide();
    }
}

function l2CareBabyNumChangeFunction() {
    var reason = $('#l2CareBabyNum option:selected').html();
    let $l2CareBabyDaysDiv = $('#l2CareBabyDaysDiv');
    if (parseInt(reason) > 0) {
        $l2CareBabyDaysDiv.show();
    } else {
        $l2CareBabyDaysDiv.hide();
    }
}

function l3CareBabyNumChangeFunction() {
    var reason = $('#l3CareBabyNum option:selected').html();
    let $l3CareBabyDaysDiv = $('#l3CareBabyDaysDiv');
    if (parseInt(reason) > 0) {
        $l3CareBabyDaysDiv.show();
    } else {
        $l3CareBabyDaysDiv.hide();
    }
}

function deliveryDateCheckboxChangeFunction() {
    $("#deliveryDate").attr("disabled", $("#deliveryDateUnknown").prop("checked"));
}

function bindBirthDefect() {
    let $birthDefect = $('.birthDefect');
    $birthDefect.unbind();
    $birthDefect.change(function () {
        if ($(this).closest(".form-group").find('.birthDefect:checked').val() == "Yes") {
            $(this).closest(".form-group").next('div[name="defectTypeSectionName"]').show();
        } else {
            $(this).closest(".form-group").next('div[name="defectTypeSectionName"]').hide();
        }
    });
}

function bindDefectType() {
    let $defectType = $('.defectType');
    $defectType.unbind();
    $defectType.change(function () {
        if ($(this).val() == "other") {
            if ($(this).prop("checked")) {
                $(this).closest(".form-group").next('div[name="otherDefectTypeDivName"]').show();
            } else {
                $(this).closest(".form-group").next('div[name="otherDefectTypeDivName"]').hide();
            }
        }
    });
}

function changeTotalLiveBirthNum() {
    let maleLiveBirthNum = parseInt($('#maleLiveBirthNum').val());
    let femaleLiveBirthNum = parseInt($('#femaleLiveBirthNum').val());
    let total = 0;
    if (maleLiveBirthNum) {
        total += maleLiveBirthNum;
    }
    if (femaleLiveBirthNum) {
        total += femaleLiveBirthNum;
    }
    $('#totalLiveBirthNum').html(total);
}

function changeBabySection() {
    let currentBabySize = $('div[name="defectTypeSectionName"]').length;
    let babySize = parseInt($('#totalLiveBirthNum').html());
    if (babySize > currentBabySize) {
        addBabaSection(currentBabySize,babySize - currentBabySize);
    }else if (babySize < currentBabySize){
        if (babySize == 0){
            $('.pregnancyOutcomeStageBabySection').remove();
        }else {
            $('#pregnancyOutcomeStageBabySection'+ (babySize - 1)).nextAll('.pregnancyOutcomeStageBabySection').remove()
        }
    }
}

function addBabaSection(babyIndex, babySize) {
    showWaiting();
    $.ajax({
        url: $('#_contextPath').val() + '/ar/pregnancy-outcome-baby-html',
        dataType: 'json',
        data: {
            "babyIndex": babyIndex,
            "babySize": babySize
        },
        type: 'POST',
        success: function (data) {
            if ('200' == data.resCode) {
                $('.NICUCareBabyNumRow').before(data.resultJson + '');
                if (babyIndex == 0){
                    $('.birthWeight').niceSelect();
                }else {
                    $('#pregnancyOutcomeStageBabySection'+ (babyIndex - 1)).nextAll('.pregnancyOutcomeStageBabySection').find('.birthWeight').niceSelect();
                }
                bindBirthDefect();
                bindDefectType();
            }
            dismissWaiting();
        },
        error: function (data) {
            console.log("err");
            dismissWaiting();
        }
    });
}