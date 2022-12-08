$(document).ready(function() {
    $('#reasonSelect').change(function () {

        othersReasonDisplay();

    });

    $('input[name="efoDateStarted"]').change( function (){
        calculateAge($("#efoDateStarted").val());
    });

    $('input:radio[name="indicatedRadio"]').change(function () {

        var reason = $('input[name="indicatedRadio"]:checked').val();

        if("1"==reason){
            $('#reasonDisplay0').attr("style","display: none");
            $('#reasonDisplay1').attr("style","display: block");
            othersReasonDisplay();
        }
        if("0"==reason) {
            var oldReason = $('input[name="oldReason"]').val();

            if(oldReason.includes('EFOR0')){
                $('input[name="textReason"]').val('');
                $('input[name="oldReason"]').val('');
            }
            $('#reasonDisplay0').attr("style","display: block");
            $('#reasonDisplay1').attr("style","display: none");
            $('#othersReason').attr("style","display: none");
        }

    });

    $('input[type=text][name="cryopresNum"]').change(function() {
        var cryopresVal = $('input[name="cryopresNum"]').val();
        if (cryopresVal == '0') {
            $("#Others").show();
        } else {
            $("#Others").hide();
        }
    });
    $('#medicallyYes').show();
    $('#medicallyNo').hide();
    $('input[type=radio][name=indicatedRadio]').change(function () {
        sfoIndicatedTooltip();
    });
});

function sfoIndicatedTooltip() {
    var val=$('input:radio[name="indicatedRadio"]:checked').val();
    if (val == 1) {
        $('#medicallyYes').show();
        $('#medicallyNo').hide();
    } else if (val == 0) {
        $('#medicallyNo').show();
        $('#medicallyYes').hide();
    }
}

function othersReasonDisplay() {
    var reason= $('#reasonSelect option:selected').val();

    if("EFOR004"==reason){
        $('#othersReason').attr("style","display: block");
    }else {
        $('#othersReason').attr("style","display: none");
    }
}

function calculateAge(freezingDate) {
    $.ajax({
        url: $('#_contextPath').val() + '/ar/calculate-age',
        dataType: 'json',
        data: {
            "efoDateStarted": freezingDate,
        },
        type: 'POST',
        success: function (data) {
            $("#startYear").val(data.freezingYear);
            $("#startMonth").val(data.freezingMonth);
            $('#freezingYear').html(data.freezingYear);
            $('#freezingMonth').html(data.freezingMonth);
        }
    });
}