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
            $('#pregnancyOutcomeStageBabySection').show();
        }else {
            $('#pregnancyOutcomeStageBabySection').hide();
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

    $('input[name="birthDefect"]').change(function () {
        if (!$(this).val() == 'yes'){
            $(this).next('div[name="defectTypeSectionName"]').show();
        }else {
            $(this).next('div[name="defectTypeSectionName"]').hide();
        }
    });

    $('input[name="defectType"]').change(function () {
        if (!$(this).val() == 'other'){
            $(this).next('div[name="otherDefectTypeDivName"]').show();
        }else {
            $(this).next('div[name="otherDefectTypeDivName"]').hide();
        }
    });
});