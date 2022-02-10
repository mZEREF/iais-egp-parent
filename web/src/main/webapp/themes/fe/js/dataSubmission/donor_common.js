function validateDonor(index){
    $("#crud_action_value_valiate_donor").val(index);
    submit("page");
}

function deleteDonor(index){
    if($("#crud_action_value_donor_size").val()>1){
        $("#crud_action_value_ar_stage").val(index);
        submit("page");
    }
}
function addDonor(){
    //index ==-1 : add
    sumbitPage(-1);
}
function rollbackDonor(flag){
    //index ==-3 : rollbackDonor to 0
    //index ==-4 : rollbackDonor to 1
    if(flag == 1){
        sumbitPage(-3);
    }else {
        sumbitPage(-4);
    }
}

function sumbitPage(donorAction){
    $("#crud_action_value_ar_stage").val(donorAction);
    submit("page");
}


function showDonor(index){
    $("#deleteDonor"+index).show();
    $("#source"+index+"Row").hide();
    $("#otherSource"+index+"Row").hide();
    $("#donorSampleCode"+index+"Row").hide();
    //$("#source"+index).val("");
    $("#otherSource"+index).val("");
    $("#donorSampleCode"+index).val("");
    $("#idNo"+index+"Row").show();
    $("#relation"+index+"Row").hide();
    $("#age"+index).val("");
    $("#age"+index+"Row").hide();
}
function hideDonor(index){
    $("#source"+index+"Row").show();
    $("#donorSampleCode"+index+"Row").show();
    $("#idNo"+index+"Row").hide();
    //$("#idType"+index).val("");
    $("#idNumber"+index).val("");
    $("#age"+index).val("");
    $("#age"+index+"Row").hide();
    $("#relation"+index+"Row").hide();
}

function showUsedDonorOocyteControlClass(flag){
    if(flag == 1){
        rollbackDonor(0);
    }else {
        $(".usedDonorOocyteControl").show();
    }
}

function hideUsedDonorOocyteControlClass(flag){
    if(flag == 1){
        rollbackDonor(1);
    }else {
        $(".usedDonorOocyteControlClass").hide();
    }
}

function removeAges(index){
    $("#age"+index+"Row").hide();
    $("#age"+index).val("");
    $("#resetDonor"+index).val("1");
    $("#relation"+index+"Row").hide();
}

function sourceChange(own,value,row,index){
    toggleOnSelect(own,value,row);
    removeAges(index);
}

function DSERR019MessageTipClose(){
    $('#DSERR019Tip').modal('hide');
}
