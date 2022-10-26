$(function () {
    $("#submitBtn").click(function (){
        showWaiting();
        $("[name='action_type']").val("submit");
        $("#mainForm").submit();
    });

    $("#doDecision").change(function () {
        var selectValue = $(this).val();
        if (selectValue === 'MOHPRO002') {
            $("#commentForApplicantDiv").show();
        } else {
            $("#commentForApplicantDiv").hide();
        }
    });
})