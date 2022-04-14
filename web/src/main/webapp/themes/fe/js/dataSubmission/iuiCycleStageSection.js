$(document).ready(function (){
    showDonorArea("sourceOfSemenOpAR_SOS_003","sourceOfSemenOpAR_SOS_004",0);
   if($('#ownPremisesRadioNo').is(':checked')) {
       showOtherPremises(0);
   }else {
       showOtherPremises(1);
   }
    showPopCommon('#DSERR019TipShow','#DSERR019Tip',1);
    showPopCommon('#donorMessageTipShow','#donorMessageTip',1);
});


function showDonorArea(id,id2,load){
     let  selcetNum = $("#sourceOfSemenShowDonorNum").val();
     let checkNum = 0;
     if($('#'+id).is(':checked')){
         checkNum++;
     }
     if($('#'+id2).is(':checked')){
         checkNum++;
     }
    if(selcetNum==0 && checkNum==1 || checkNum==2 && load ==0){
        showUsedDonorOocyteControlClass(load);
    }else if (checkNum==0) {
        hideUsedDonorOocyteControlClass(load);
    }
    $("#sourceOfSemenShowDonorNum").val(checkNum);
}

function showOtherPremises(value) {
    if(value == 1){
        $("#otherPremisesRow").hide();
    }else {
        $("#otherPremisesRow").show();
    }
}

