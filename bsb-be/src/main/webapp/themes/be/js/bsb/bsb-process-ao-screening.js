$(function () {
    $("#processingDecision").change(function () {
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO005") {
            $("#selectMohUserDiv").show();
            $("#aoRecommendationDiv").show();
            $("#reasonForRejectionDiv").hide();
        } else if (selectValue === "MOHPRO024") {
            $("#selectMohUserDiv").hide();
            $("#aoRecommendationDiv").hide();
            $("#reasonForRejectionDiv").show();
        } else {
            $("#selectMohUserDiv").hide();
            $("#aoRecommendationDiv").hide();
            $("#reasonForRejectionDiv").hide();
        }
    })
    $("#submitBtn").click(function () {
        showWaiting();
        $("input[type='radio']").removeAttr("disabled");
        $("[name='action_type']").val("submit");
        $('#mainForm').submit();
    })
})
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-web/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}