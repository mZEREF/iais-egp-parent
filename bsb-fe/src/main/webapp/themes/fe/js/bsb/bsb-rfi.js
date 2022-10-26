$(function (){
    $("#submitBtn").click(function () {
        var isCompletedAllRfi = $("[name ='completedAllRfi']").val();
        if(isCompletedAllRfi === "true"){
            showWaiting();
            $("#mainForm").submit();
        }else{
            $("#submitModal").modal('show');
        }
    });
})

function processRfi(url, maskRfiAppId, maskRfiDataId) {
    window.location.href = url + "?rfiAppId=" + maskRfiAppId + "&maskedRfiDataId=" + maskRfiDataId;
}