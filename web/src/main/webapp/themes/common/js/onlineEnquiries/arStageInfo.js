function doBack(arViewFull){
    showWaiting();
    if(arViewFull==1){
        $("[name='crud_action_type']").val('backViewInv');
    }else if(arViewFull==2){
        $("[name='crud_action_type']").val('backViewCyc');
    }else {
        $("[name='crud_action_type']").val('backBase');
    }
    $('#mainForm').submit();
}

function nextTab(subNo){
    showWaiting();
    $("[name='crud_action_additional']").val(subNo);
    $("[name='crud_action_type']").val('step');
    $('#mainForm').submit();
}