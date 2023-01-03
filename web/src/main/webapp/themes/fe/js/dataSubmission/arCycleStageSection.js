$(document).ready(function (){
    showFieldMandatory();
    toggleOnSelect("#mainIndication",'AR_MI_013', 'mainIndicationOtherRow');
    toggleOnSelectNoSelect("#totalPreviouslyPreviously",'21', 'totalNumberARCOtherRow');
    if($("#usedDonorOocyteRadioYes").is(':checked')){
        showUsedDonorOocyteControlClass(0);
    }else {
        hideUsedDonorOocyteControlClass(0);
    }

    $('input[name="startDate"]').change( function (){
        calculateAge($("#startDate").val());
    });
    $("#cyclesUndergoneOverseas").change(function(){
        showFieldMandatory();
    });

    $("#enhancedCounsellingRadioNo").change(function (){
        const overseaNum = parseInt($('#cyclesUndergoneOverseas').val());
        const localNum = $('#cyclesUndergoneLocal').val();
        const startYear = $('#startYear').val();
        const startMonth = $('#startMonth').val();
        if (overseaNum + localNum > 10 || startYear > 45 || startYear==45&&startMonth>0){
            showEnhancedCounsellingTipNo();
        }
    })
    showDataStartTooltip();
    showPopCommon('#DSERR019TipShow','#DSERR019Tip',1);
    showPopCommon('#donorMessageTipShow','#donorMessageTip',1);
    mutualExclusionCheckBox('#currentArTreatmentCheckAR_CAT_001','#currentArTreatmentCheckAR_CAT_002');
    mutualExclusionCheckBox('#currentArTreatmentCheckAR_CAT_002','#currentArTreatmentCheckAR_CAT_001');
});

function showDataStartTooltip(){
    if($('#currentArTreatmentCheckAR_CAT_003').is(':checked')|| $('#currentArTreatmentCheckAR_CAT_004').is(':checked')){
        getDataStartTooltipDesc("AR_CAT_0034")
        return;
    }
    if($('#currentArTreatmentCheckAR_CAT_001').is(':checked')){
        getDataStartTooltipDesc("AR_CAT_001");
        return;
    }
    if($('#currentArTreatmentCheckAR_CAT_002').is(':checked')){
        getDataStartTooltipDesc("AR_CAT_002");
        return;
    }
    getDataStartTooltipDesc('');
}

function getDataStartTooltipDesc(key){
    $('#dateStartTooltip1').hide();
    $('#dateStartTooltip2').hide();
    $('#dateStartTooltip3').hide();
    switch (key){
        case "AR_CAT_002":
            $('#dateStartTooltip2').show();
            break;
        case "AR_CAT_001":
            $('#dateStartTooltip1').show();
            break;
        case "AR_CAT_0034":
            $('#dateStartTooltip3').show();
    }


}

function doInactiveCurrentArTreatment(key){
    if(key == 'AR_CAT_001'){
        mutualExclusionCheckBox('#currentArTreatmentCheckAR_CAT_001','#currentArTreatmentCheckAR_CAT_002');
    }else if(key == 'AR_CAT_002'){
        mutualExclusionCheckBox('#currentArTreatmentCheckAR_CAT_002','#currentArTreatmentCheckAR_CAT_001');
    }
}

function mutualExclusionCheckBox(key1,key2){
    if($(key1).is(':checked')){
        $(key2).attr("disabled",true);
    }else {
        $(key2).attr("disabled",false);
    }
}


function enhancedCounsellingTipClose(){
    $('#enhancedCounsellingTip').modal('hide');
}


function showEnhancedCounsellingTipNo(){
    if($("#enhancedCounsellingRadioNo").is(':checked')){
        $('#enhancedCounsellingTip').modal('show');
    }
}

function toggleOnSelectNoSelect(sel, value, area){
    var valueK = $(sel).val();
    if(valueK != null && valueK != '' && valueK != value){
        toggleOnSelect(sel,valueK,area);
    }else {
        toggleOnSelect(sel,'-1',area);
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
            $('#cycleAgeYear').html(data.freezingYear);
            $('#cycleAgeMonth').html(data.freezingMonth);
            showFieldMandatory();
        }
    });
}

function showFieldMandatory(){
    const overseaNum = parseInt($('#cyclesUndergoneOverseas').val());
    const localNum = $('#cyclesUndergoneLocal').val();
    const startYear = $('#startYear').val();
    const startMonth = $('#startMonth').val();
    const numberOfCyclesUndergoneLocally = $('#cycleUnderLocal').val();

    if (numberOfCyclesUndergoneLocally  > 10 || overseaNum + localNum > 10 || startYear > 45 || startYear==45&&startMonth>0) {
        $('#enhancedCounsellingFieldMandatory').show();
    } else {
        $('#enhancedCounsellingFieldMandatory').hide();
    }
}





