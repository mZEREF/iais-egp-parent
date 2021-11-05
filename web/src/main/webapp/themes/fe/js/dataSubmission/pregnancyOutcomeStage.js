$(document).ready(function () {
    $('#pregnancyOutcome').change(function () {
        var reason= $('#pregnancyOutcome option:selected').html();
        var firstUltrasoundOrderShowReason= $('#firstUltrasoundOrderShow option:selected').html();
        console.log(reason);
        if (reason == "Others"){
            $('#otherPregnancyOutcomeDiv').show();
        }else {
            $('#otherPregnancyOutcomeDiv').hide();
        }
        if (reason == "Live Birth"){
            $('#liveBirthNumSection').show();
        }else {
            $('#liveBirthNumSection').hide();
        }
        if (reason == "No Live Birth"){
            $('#stillBirthNumSection').show();
        }else {
            $('#stillBirthNumSection').hide();
        }

        if (reason == "Live Birth" && firstUltrasoundOrderShowReason == "Singleton"){
            $('#wasSelFoeReduCarryOutDiv').show();
        }else {
            $('#wasSelFoeReduCarryOutDiv').hide();
        }
    });

    $('#firstUltrasoundOrderShow').change(function () {
        var reason= $('#firstUltrasoundOrderShow option:selected').html();
        var pregnancyOutcomeReason= $('#pregnancyOutcome option:selected').html();
        if (pregnancyOutcomeReason == "Live Birth" && reason == "Singleton"){
            $('#wasSelFoeReduCarryOutDiv').show();
        }else {
            $('#wasSelFoeReduCarryOutDiv').hide();
        }

        if (reason != "Unknown"){
            $('#deliverySection').show();
        }else {
            $('#deliverySection').hide();
        }
    });

    $('input[name="birthPlace"]').change(function () {
        if ($(this).val() == "Local"){
            $('#localBirthPlaceDiv').show();
        }else {
            $('#localBirthPlaceDiv').hide();
        }
    });

    $('input[name="babyDetailsUnknown"]').change(function () {
        if (!$(this).val()){
            $('.pregnancyOutcomeStageBabySection').show();
        }else {
            $('.pregnancyOutcomeStageBabySection').hide();
        }
    });

    $('#NICUCareBabyNum').change(function () {
        var reason= $('#NICUCareBabyNum option:selected').html();
        if (parseInt(reason) > 0){
            $('#careBabyNumSection').show();
        }else {
            $('#careBabyNumSection').hide();
        }
    });
    $('#l2CareBabyNum').change(function () {
        var reason= $('#l2CareBabyNum option:selected').html();
        if (parseInt(reason) > 0){
            $('#l2CareBabyDaysDiv').show();
        }else {
            $('#l2CareBabyDaysDiv').hide();
        }
    });
    $('#l3CareBabyNum').change(function () {
        var reason= $('#l3CareBabyNum option:selected').html();
        if (parseInt(reason) > 0){
            $('#l3CareBabyDaysDiv').show();
        }else {
            $('#l3CareBabyDaysDiv').hide();
        }
    });
    bindBirthDefect();
    bindDefectType();

    $('#maleLiveBirthNum').change(changeTotalLiveBirthNum);

    $('#femaleLiveBirthNum').change(changeTotalLiveBirthNum);

    $('#maleLiveBirthNum').change(changeBabySection);

    $('#femaleLiveBirthNum').change(changeBabySection);
});

function bindBirthDefect() {
    $('.birthDefect').unbind();
    $('.birthDefect').change(function () {
        console.log("birthDefect is :"+$(this).val());
        if ($(this).val() == 'Yes') {
            $(this).closest(".form-group").next('div[name="defectTypeSectionName"]').show();
        } else {
            $(this).closest(".form-group").next('div[name="defectTypeSectionName"]').hide();
        }
    });
}

function bindDefectType() {
    $('.defectType').unbind();
    $('.defectType').change(function () {
        if ($(this).val()=="other") {
            if ($(this).prop("checked")){
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
        console.log("maleLiveBirthNum:" + maleLiveBirthNum);
        total += maleLiveBirthNum;
    }
    if (femaleLiveBirthNum) {
        console.log("femaleLiveBirthNum:" + femaleLiveBirthNum);
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

var addBabaSection = function (babyIndex,babySize) {
    showWaiting();
    console.log($('#_contextPath').val());
    $.ajax({
        url: $('#_contextPath').val() + '/ar/pregnancy-outcome-baby-html',
        dataType: 'json',
        data: {
            "babyIndex": babyIndex,
            "babySize":babySize
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