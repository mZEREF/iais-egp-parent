function followUpItem(itemVal, action_value){
    showWaiting();
    $("[name='itemValue']").val(itemVal);
    $("[name='action_type']").val("followUpItems");
    $("[name='action_value']").val(action_value);
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

    $("#requestExtension").change(function (){
        var inputEl = document.getElementById("requestExtension");
        if (inputEl.checked) {
            $("#reasonMandatory").show();
        }else {
            $("#reasonMandatory").hide();
        }
    });
})