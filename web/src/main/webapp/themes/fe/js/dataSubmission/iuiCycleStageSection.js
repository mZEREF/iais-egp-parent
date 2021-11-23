$(document).ready(function (){
    showDonorArea("sourceOfSemenOpAR_SOS_003",0);
});


function showDonorArea(id,load){
    if($('#'+id).is(':checked')){
        showUsedDonorOocyteControlClass(load);
    }else {
        hideUsedDonorOocyteControlClass(load);
    }
}

