$(function () {
    validate();
    $("input[type='radio']").attr("disabled",true);
    $("#submitBtn").click(function () {
        showWaiting();
        $("input[type='radio']").removeAttr("disabled");
        $('#mainForm').submit();
    })
})
function validate(){
    var ifProcess = $("#ifProcess").val();
    if("Y" === ifProcess){
        $('#info').removeClass("active");
        $('#doProcess').click();
        $('#process').addClass("active");
    }
}
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-be/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}