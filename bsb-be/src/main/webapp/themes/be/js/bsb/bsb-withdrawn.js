$(function () {
    $("#submitBtn").click(function (){
        showWaiting();
        $("#mainForm").submit();
    });
})