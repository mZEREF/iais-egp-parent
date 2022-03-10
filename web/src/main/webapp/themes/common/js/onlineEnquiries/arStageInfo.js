function doBack(arViewFull,arAdv,arBase){
    showWaiting();
    if(arViewFull==1){
        $("[name='crud_action_type']").val('backViewInv');
    }else if(arViewFull==2){
        $("[name='crud_action_type']").val('backViewCyc');
    }else if(arAdv==1){
        $("[name='crud_action_type']").val('backAdv');
    }else if(arBase==1){
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

$(document).ready(function () {
    $('#oldDsSelect').change(function () {

        var reason= $('#oldDsSelect option:selected').val();

        showWaiting();
        $("[name='crud_action_additional']").val('${arSuperDataSubmissionDto.dataSubmissionDto.submissionNo}');
        $("[name='crud_type']").val(reason);
        $("[name='crud_action_type']").val('step');
        $('#mainForm').submit();

    });
})