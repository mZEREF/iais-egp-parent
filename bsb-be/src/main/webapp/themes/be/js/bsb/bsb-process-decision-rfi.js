$(function (){
    var isClosePage = $("input[name='closePage']").val();
    if (isClosePage === "true") {
        window.close();
    }
    $("#submitAppRfiBtn").click(function () {
        showWaiting();
        $("#mainForm").submit();
    });
})