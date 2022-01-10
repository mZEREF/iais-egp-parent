var liveBirth = "OUTOPRE001";
var noLiveBirth = "OUTOPRE002";
var outcome_unknown = "OUTOPRE003";
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
    $maleLiveBirthNum.change(changeBabySectionFunction);
    let $femaleLiveBirthNum = $('#femaleLiveBirthNum');
    $femaleLiveBirthNum.change(changeTotalLiveBirthNum);
    $femaleLiveBirthNum.change(changeBabySectionFunction);
    $('.birthDefect').change(birthDefectChangeFunction);
    $('.defectType').change(defectTypeChangeFunction);
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
        $stillBirthNumSection.find("input").val("");
    }

    if (firstUltrasoundOrderShowVal != singleton && firstUltrasoundOrderShowVal) {
        $wasSelFoeReduCarryOutDiv.show();
    } else {
        $wasSelFoeReduCarryOutDiv.hide();
        let $wasSelFoeReduCarryOut = $('#wasSelFoeReduCarryOut');
        $wasSelFoeReduCarryOut.val("1");
        $wasSelFoeReduCarryOut.niceSelect("update");
    }
}

function pregnancyOutcomeChangeFunction() {
    let pregnancyOutcomeVal = $('#pregnancyOutcome option:selected').val();

    let $liveBirthNumSection = $('#liveBirthNumSection');
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

        $liveBirthNumSection.find('input').val("");

        $('#maleLiveBirthNum').trigger('change');
        $('#femaleLiveBirthNum').trigger('change');
    }

    if (pregnancyOutcomeVal == outcome_unknown ||  (!pregnancyOutcomeVal)) {
        $deliverySection.hide();

        $deliverySection.find('select').val(null)
        $deliverySection.find('select').niceSelect('update');

        let $deliveryDateUnknown = $('#deliveryDateUnknown');
        $deliveryDateUnknown.attr('checked', false);
        $deliveryDateUnknown.prop('checked', false);
        $('#deliveryDate').val(null);

        let $birthPlaceLocal = $('#birthPlaceLocal');
        $birthPlaceLocal.attr('checked', true);
        $birthPlaceLocal.prop('checked', true);

        let $babyDetailsUnknownNo = $('#babyDetailsUnknownNo');
        $babyDetailsUnknownNo.attr('checked', true);
        $babyDetailsUnknownNo.prop('checked', true);

        $("#deliveryDateUnknown").trigger('change');
        $('input[name="birthPlace"]').trigger('change');
        $('input[name="babyDetailsUnknown"]').trigger('change');
    } else {
        $deliverySection.show();
    }

    if (pregnancyOutcomeVal == others) {
        $otherPregnancyOutcomeDiv.show();
    } else {
        $otherPregnancyOutcomeDiv.hide();
        $otherPregnancyOutcomeDiv.find('input').val("");
    }

    firstUltrasoundOrderShowChangeFunction();
}

function birthPlaceChangeFunction() {
    let $localBirthPlaceDiv = $('#localBirthPlaceDiv');
    if ($('input[name="birthPlace"]:checked').val() == "POSBP001") {
        $localBirthPlaceDiv.show();
    } else {
        $localBirthPlaceDiv.hide();
        $localBirthPlaceDiv.find('input').val("");
    }
}

function babyDetailsUnknownChangeFunction() {
    let $pregnancyOutcomeStageBabySection = $('.pregnancyOutcomeStageBabySection');
    if ($('input[name="babyDetailsUnknown"]:checked').val() != 'true') {
        $pregnancyOutcomeStageBabySection.show();
        changeBabySection();
    } else {
        $pregnancyOutcomeStageBabySection.hide();

        $pregnancyOutcomeStageBabySection.find('select').val(null);
        $pregnancyOutcomeStageBabySection.find('select').niceSelect('update');
        $pregnancyOutcomeStageBabySection.find('input[value="No"]').prop('checked', true);
        $pregnancyOutcomeStageBabySection.find('input[value="No"]').attr('checked', true);

        $('.birthDefect').trigger('change');
    }
}

function NICUCareBabyNumChangeFunction() {
    var reason = $('#NICUCareBabyNum option:selected').html();
    let $careBabyNumSection = $('#careBabyNumSection');
    if (parseInt(reason) > 0) {
        $careBabyNumSection.show();
    } else {
        $careBabyNumSection.hide();
        $careBabyNumSection.find('select').val(0);
        $careBabyNumSection.find('select').niceSelect('update');
        $('#l2CareBabyNum').trigger('change');
        $('#l3CareBabyNum').trigger('change');
    }
}

function l2CareBabyNumChangeFunction() {
    var reason = $('#l2CareBabyNum option:selected').html();
    let $l2CareBabyDaysDiv = $('#l2CareBabyDaysDiv');
    if (parseInt(reason) > 0) {
        $l2CareBabyDaysDiv.show();
    } else {
        $l2CareBabyDaysDiv.hide();
        $l2CareBabyDaysDiv.find('input').val("");
    }
}

function l3CareBabyNumChangeFunction() {
    var reason = $('#l3CareBabyNum option:selected').html();
    let $l3CareBabyDaysDiv = $('#l3CareBabyDaysDiv');
    if (parseInt(reason) > 0) {
        $l3CareBabyDaysDiv.show();
    } else {
        $l3CareBabyDaysDiv.hide();
        $l3CareBabyDaysDiv.find('input').val("");
    }
}

function deliveryDateCheckboxChangeFunction() {
    let $deliveryDate = $("#deliveryDate");
    let $deliveryDateUnknown = $("#deliveryDateUnknown");
    $deliveryDate.attr("disabled", $deliveryDateUnknown.prop("checked"));
    if ($deliveryDateUnknown.prop("checked")) {
        $deliveryDate.css("border-color", "rgb(237, 237, 237)");
        $deliveryDate.css("color", "rgb(153, 153, 153)");
        $deliveryDate.prop("value",null);
    } else {
        $deliveryDate.css("border-color", "");
        $deliveryDate.css("color", "");
    }
}

function birthDefectChangeFunction() {
    if ($(this).closest(".form-group").find('.birthDefect:checked').val() == "Yes") {
        $(this).closest(".form-group").next('div[name="defectTypeSectionName"]').show();
    } else {
        $(this).closest(".form-group").next('div[name="defectTypeSectionName"]').hide();

        $(this).closest(".form-group").next('div[name="defectTypeSectionName"]').find('input[type="checkbox"]').attr('checked', false);
        $(this).closest(".form-group").next('div[name="defectTypeSectionName"]').find('input[type="checkbox"]').prop('checked', false);

        $('.defectType').trigger('change');
    }
}

function defectTypeChangeFunction() {
    if ($(this).val() == "POSBDT008") {
        if ($(this).prop("checked")) {
            $(this).closest(".form-group").next('div[name="otherDefectTypeDivName"]').show();
        } else {
            $(this).closest(".form-group").next('div[name="otherDefectTypeDivName"]').hide();
            $(this).closest(".form-group").next('div[name="otherDefectTypeDivName"]').find('input').val('');
        }
    }
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

function changeBabySectionFunction() {
    if ($('input[name="babyDetailsUnknown"]:checked').val() != 'true') {
        changeBabySection()
    }
    //75742
    let babySize = parseInt($('#totalLiveBirthNum').html());
    let $babyDetailsPageDiv = $('.babyDetailsPageDiv');
    if (babySize > 0) {
        $babyDetailsPageDiv.show();
    } else {
        $babyDetailsPageDiv.hide();
        let $NICUCareBabyNum = $('#NICUCareBabyNum');
        $NICUCareBabyNum.val(0)
        $NICUCareBabyNum.trigger('change');
    }
}

function changeBabySection() {
    let currentBabySize = $('div[name="defectTypeSectionName"]').length;
    let babySize = parseInt($('#totalLiveBirthNum').html());
    if (babySize > currentBabySize) {
        addBabaSection(currentBabySize, babySize - currentBabySize);
    } else if (babySize < currentBabySize) {
        if (babySize == 0) {
            $('.pregnancyOutcomeStageBabySection').remove();
        } else {
            $('#pregnancyOutcomeStageBabySection' + (babySize - 1)).nextAll('.pregnancyOutcomeStageBabySection').remove()
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
                if (babyIndex == 0) {
                    $('.birthWeight').niceSelect();
                } else {
                    $('#pregnancyOutcomeStageBabySection' + (babyIndex - 1)).nextAll('.pregnancyOutcomeStageBabySection').find('.birthWeight').niceSelect();
                }
                let $birthDefect = $('.birthDefect');
                $birthDefect.unbind('change');
                let $defectType = $('.defectType');
                $defectType.unbind('change');
                $birthDefect.change(birthDefectChangeFunction);
                $defectType.change(defectTypeChangeFunction);
            }
            dismissWaiting();
        },
        error: function (data) {
            console.log("err");
            dismissWaiting();
        }
    });
}