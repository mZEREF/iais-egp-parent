$(document).ready(function (){
    showDonorArea("sourceOfSemenOpAR_SOS_003");
});


function showDonorArea(id){
    if($('#'+id).is(':checked')){
        showUsedDonorOocyteControlClass(0);
    }else {
        hideUsedDonorOocyteControlClass(0);
    }
}

