$(function () {
    $("#cancelBtn").click(function (){
        // showWaiting();
        // $("#mainForm").submit();
    });

    $("#nextBtn").click(function (){
        showWaiting();
        $("#mainForm").submit();
    });

    //confirmPage
    $("#submitBtn").click(function (){
        showWaiting();
        $("[name='action_type']").val("doSubmit");
        $("#mainForm").submit();
    });

    //back
    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });
})

function isRemarksMandatory (){
    var reasonOption = $("#reason").val();
    if (reasonOption ==='WDREASN007'){
        $("#remarksSpan").show();
    }else{
        $("#remarksSpan").hide();
    }
}