$(document).ready(function (){
    showDonorArea("sourceOfSemenOpAR_SOS_003",0);
   if($('#ownPremisesRadioNo').is(':checked')) showOtherPremises(0);
});


function showDonorArea(id,load){
    if($('#'+id).is(':checked')){
        showUsedDonorOocyteControlClass(load);
    }else {
        hideUsedDonorOocyteControlClass(load);
    }
}

function showOtherPremises(value) {
    if(value == 1){
        $("#otherPremisesRow").hide();
    }else {
        $("#otherPremisesRow").show();
    }
}

