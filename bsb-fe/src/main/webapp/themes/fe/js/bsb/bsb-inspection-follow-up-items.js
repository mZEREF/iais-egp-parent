$(function (){
    $("#saveBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("save");
        $("#mainForm").submit();
    });

    $("#draftBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("saveDraft");
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