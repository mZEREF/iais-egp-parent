$(document).ready(function (){
    showDonorArea("sourceOfSemenOpAR_SOS_003","sourceOfSemenOpAR_SOS_004",0);
   if($('#ownPremisesRadioNo').is(':checked')) {
       showOtherPremises(0);
   }else {
       showOtherPremises(1);
   }
    showPopCommon('#DSERR019TipShow','#DSERR019Tip',1);
    showPopCommon('#donorMessageTipShow','#donorMessageTip',1);

    $('input[name="iuiCycleStartDate"]').change( function (){
        calculateAge($("#iuiCycleStartDate").val());
    });
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
        $("#chooseYesRow").show();
    }else {
        $("#otherPremisesRow").show();
        $("#chooseYesRow").hide();
    }
}

function calculateAge(iuiCycleStartDate) {
    $.ajax({
        url: $('#_contextPath').val() + '/ar/calculate-age',
        dataType: 'json',
        data: {
            "efoDateStarted": iuiCycleStartDate,
        },
        type: 'POST',
        success: function (data) {
            $("#startYear").val(data.freezingYear);
            $("#startMonth").val(data.freezingMonth);
            $('#iuiYear').html(data.freezingYear);
            $('#iuiMonth').html(data.freezingMonth);
        }
    });
}

