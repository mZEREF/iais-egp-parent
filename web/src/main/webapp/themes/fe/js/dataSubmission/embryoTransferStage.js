$(document).ready(function () {
    $('.transferNumSelect').change(function () {
        let v = parseInt($(this).val());
        let $section2nd = $('#section2nd');
        let $section3rd = $('#section3rd');
        if (v == 1) {
            $section2nd.hide();
            $section3rd.hide();
            hide2nd();
            hide3rd();
        } else if (v == 2) {
            $section2nd.show();
            $section3rd.hide();
            hide3rd();
        } else if (v == 3) {
            $section2nd.show();
            $section3rd.show();
        }
        triggerFlag(3);
    });

    $('.ageSelect').change(function () {
        let thisVal = $(this).find('option:selected').val();
        if (greaterFourDay(thisVal)) {
            triggerFlag(2);
        }
    });

    function hide2nd() {
        let $secondSelect = $('select[name="secondEmbryoAge"]');

        $secondSelect.val(null);
        $secondSelect.niceSelect("update");

        let $radio = $('input[name="secondEmbryoType"]');
        $radio.attr("checked", false);
        $radio.prop("checked", false);
    }

    function hide3rd() {
        let $secondSelect = $('select[name="thirdEmbryoAge"]');

        $secondSelect.val(null);
        $secondSelect.niceSelect("update");

        let $radio = $('input[name="thirdEmbryoType"]');
        $radio.attr("checked", false);
        $radio.prop("checked", false);
    }

    function triggerFlag(flagNum){
        let popMessage = "";
        let flagTwo = flagOutEmbryoTransferAgeAndCount();
        let flagThree = flagOutEmbryoTransferCountAndPatAge();
        if (flagTwo){
            popMessage += $('#flagTwoMessage').html() + "<br>";
        }else if(flagNum == 2){
            return;
        }
        if (flagThree){
            popMessage += $('#flagThreeMessage').html() + "<br>";
        }else if(flagNum == 3){
            return;
        }
        if (popMessage.length > 0){
            $('#flagOutDiv span').html(popMessage);
            $('#flagOutDiv').modal('show');
        }
    }

    function flagOutEmbryoTransferCountAndPatAge() {
        let currentTransferNum = parseInt($(".transferNumSelect").val());
        currentTransferNum += parseInt($("#embryoTransferCount").val());
        let age = parseInt($("#age").val());
        let haveStimulationCycles = $("#haveStimulationCycles").val();
        if (currentTransferNum >= 3) {
            if (age < 37 || (!haveStimulationCycles)) {
                return true;
            }
        }
        return false;
    }

    function currentHaveGreateFiveDay() {
        let firstEmbryoAge = $('select[name="firstEmbryoAge"] option:selected').val();
        let secondEmbryoAge = $('select[name="secondEmbryoAge"] option:selected').val();
        let thirdEmbryoAge = $('select[name="thirdEmbryoAge"] option:selected').val();
        return greaterFourDay(firstEmbryoAge) || greaterFourDay(secondEmbryoAge) || greaterFourDay(thirdEmbryoAge)
    }

    function greaterFourDay(code) {
        return code === "AOFET005" || code === "AOFET006"
    }

    function flagOutEmbryoTransferAgeAndCount() {
        let totalEmbryos = parseInt($("#totalEmbryos").val());
        let haveEmbryoTransferGreaterFiveDay = $("#haveEmbryoTransferGreaterFiveDay").val() == 'true';
        let currentHaveGreateFiveDay1 = currentHaveGreateFiveDay();
        console.log("trigger flagOutEmbryoTransferAgeAndCount total embryo num is :" + totalEmbryos + " haveEmbryoTransferGreaterFiveDay is " + haveEmbryoTransferGreaterFiveDay + " currentHaveGreateFiveDay: " + currentHaveGreateFiveDay1)
        if (totalEmbryos >= 3) {
            if (haveEmbryoTransferGreaterFiveDay || currentHaveGreateFiveDay1) {
                return true
            }
        }
        return false;
    }
});