function rectifyItem(itemVal){
    showWaiting();
    $("[name='itemValue']").val(itemVal);
    $("[name='action_type']").val("rectify");
    $("#mainForm").submit();
}

$(function (){
    $("#submitBtn").click(function () {
        var isAllRectify = $("[name ='isAllRectify']").val();
        if(isAllRectify === 'true'){
            showWaiting();
            $("[name='action_type']").val("submit");
            $("#mainForm").submit();
        }else{
            $("#submitModal").modal('show');
        }
    });

    $("#saveBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("save");
        $("#mainForm").submit();
    });

    $("#cancelBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("back");
        $("#mainForm").submit();
    });
})