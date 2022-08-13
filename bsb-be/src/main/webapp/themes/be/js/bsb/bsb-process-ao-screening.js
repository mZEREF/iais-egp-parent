$(function () {
    validate();
    var processingDecisionObj = $("#processingDecision");
    var processingDecisionVal =  processingDecisionObj.val();
    if (processingDecisionVal === "MOHPRO005") {
        $("#selectMohUserDiv").show();
    } else {
        $("#selectMohUserDiv").hide();
    }
    processingDecisionObj.change(function () {
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO005") {
            $("#selectMohUserDiv").show();
        } else {
            $("#selectMohUserDiv").hide();
        }
    })
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
    var url = "/bsb-web/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}