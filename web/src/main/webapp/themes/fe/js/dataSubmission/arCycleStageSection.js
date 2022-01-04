$(document).ready(function (){
    toggleOnSelect("#mainIndication",'AR_MI_013', 'mainIndicationOtherRow');
    toggleOnSelectNoSelect("#totalPreviouslyPreviously",'21', 'totalNumberARCOtherRow');
    if($("#usedDonorOocyteRadioYes").is(':checked')){
        showUsedDonorOocyteControlClass(0);
    }else {
        hideUsedDonorOocyteControlClass(0);
    }
    showPopCommon('#enhancedCounsellingTipShow','#enhancedCounsellingTip',1);
    showPopCommon('#DSERR019TipShow','#DSERR019Tip',1);
    showPopCommon('#DSERR021MessageShow','#DSERR021Message',1);
    mutualExclusionCheckBox('#currentArTreatmentCheckAR_CAT_001','#currentArTreatmentCheckAR_CAT_002');
    mutualExclusionCheckBox('#currentArTreatmentCheckAR_CAT_002','#currentArTreatmentCheckAR_CAT_001');
});


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

function doEnhancedCounsellingMandatory(key){
    if(key == 'false'){
        if($("#cyclesUndergoneOverseas").val() != '') {
            let value= parseInt($("#cyclesUndergoneOverseas").val());
            if(value > 10){
                $("#enhancedCounsellingTitle").find('.mandatory').remove();
                $("#enhancedCounsellingTitle").append('<span class="mandatory">*</span>');
            }else{
                $("#enhancedCounsellingTitle").find('.mandatory').remove();
            }
        }
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

function DSERR021MessageClose(){
    $('#DSERR021Message').modal('hide');
}




