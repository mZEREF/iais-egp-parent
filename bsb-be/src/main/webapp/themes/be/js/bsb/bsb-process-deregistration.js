$(function () {
    validate();
    $("#submitButton").click(function () {
        showWaiting();
        $('#mainForm').submit();
    })

    // TODO: check these decision
    var processingDecisionVal = $("#processingDecision").val();
    if (processingDecisionVal == "MOHPRO003" || processingDecisionVal == "MOHPRO005"){
        $("#reasonForRejection").show();
    }else{
        $("#reasonForRejection").hide();
    }
    $("#processingDecision").change(function () {
        var processingDecisionSelectVal = $(this).val();
        if(processingDecisionSelectVal == "MOHPRO003" || processingDecisionSelectVal == "MOHPRO005") {
            $("#reasonForRejection").show();
        }else{
            $("#reasonForRejection").hide();
        }
    })
})
function validate(){
    var ifProcess = $("#ifProcess").val();
    if("Y"== ifProcess){
        $('#info').removeClass("active");
        $('#doProcess').click();
        $('#process').addClass("active");
    }
}
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-web/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}