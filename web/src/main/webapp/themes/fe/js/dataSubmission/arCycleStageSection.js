$(document).ready(function (){
    toggleOnSelect("#mainIndication",'AR_MI_013', 'mainIndicationOtherRow');
    toggleOnSelect("#totalPreviouslyPreviously",'21', 'totalNumberARCOtherRow');
    if($("#usedDonorOocyteRadioYes").is(':checked')){
        showUsedDonorOocyteControlClass(0);
    }else {
        hideUsedDonorOocyteControlClass(0);
    }
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