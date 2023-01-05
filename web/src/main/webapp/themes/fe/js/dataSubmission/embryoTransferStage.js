$(document).ready(function () {
    $('.transferNumSelect').change(function () {
        let v = parseInt($(this).val());
        let $section2 = $('#2Embryo');
        let $section3 = $('#3Embryo');
        let $section4 = $('#4Embryo');
        let $section5 = $('#5Embryo');
        let $section6 = $('#6Embryo');
        let $section7 = $('#7Embryo');
        let $section8 = $('#8Embryo');
        let $section9 = $('#9Embryo');
        let $section10 = $('#10Embryo');
        if (v == 1) {
            $section2.hide();
            $section3.hide();
            $section4.hide();
            $section5.hide();
            $section6.hide();
            $section7.hide();
            $section8.hide();
            $section9.hide();
            $section10.hide();
            hide2nd();
            hide3rd();
        } else if (v == 2) {
            $section2.show();
            $section3.hide();
            $section4.hide();
            $section5.hide();
            $section6.hide();
            $section7.hide();
            $section8.hide();
            $section9.hide();
            $section10.hide();
            hide3rd();
        } else if (v == 3) {
            $section2.show();
            $section3.show();
            $section4.hide();
            $section5.hide();
            $section6.hide();
            $section7.hide();
            $section8.hide();
            $section9.hide();
            $section10.hide();
        } else if (v == 4) {
            $section2.show();
            $section3.show();
            $section4.show();
            $section5.hide();
            $section6.hide();
            $section7.hide();
            $section8.hide();
            $section9.hide();
            $section10.hide();
        }else if (v == 5) {
            $section2.show();
            $section3.show();
            $section4.show();
            $section5.show();
            $section6.hide();
            $section7.hide();
            $section8.hide();
            $section9.hide();
            $section10.hide();
        }else if (v == 6) {
            $section2.show();
            $section3.show();
            $section4.show();
            $section5.show();
            $section6.show();
            $section7.hide();
            $section8.hide();
            $section9.hide();
            $section10.hide();
        }else if (v == 7) {
            $section2.show();
            $section3.show();
            $section4.show();
            $section5.show();
            $section6.show();
            $section7.show();
            $section8.hide();
            $section9.hide();
            $section10.hide();
        }else if (v == 8) {
            $section2.show();
            $section3.show();
            $section4.show();
            $section5.show();
            $section6.show();
            $section7.show();
            $section8.show();
            $section9.hide();
            $section10.hide();
        }else if (v == 9) {
            $section2.show();
            $section3.show();
            $section4.show();
            $section5.show();
            $section6.show();
            $section7.show();
            $section8.show();
            $section9.show();
            $section10.hide();
        }else if (v == 10) {
            $section2.show();
            $section3.show();
            $section4.show();
            $section5.show();
            $section6.show();
            $section7.show();
            $section8.show();
            $section9.show();
            $section10.show();
        }
        triggerFlag(3);
    });

    $('.ageSelect').change(function () {
        let thisVal = $(this).find('option:selected').val();
        if (greaterFourDay(thisVal)) {
            triggerFlag(2);
        }
    });
    triggerFlag(0);
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
        if(flagNum == 0){
            popMessage += $('#amendMessage').html() + "<br>";
            if(popMessage != "undefined<br>"){
                $('#flagOutDiv span').html(popMessage);
                $('#flagOutDiv').modal('show');
            }
            return;
        }
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
        let haveStimulationCycles = $("#haveStimulationCycles").val() == 'true';
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