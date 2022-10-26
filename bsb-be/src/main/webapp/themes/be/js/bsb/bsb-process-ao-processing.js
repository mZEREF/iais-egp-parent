$(function () {
    $("#submitBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("submit");
        $('#mainForm').submit();
    });

    if ($("#isSubmitHM").val() === 'N') {
        $('#info').removeClass("active");
        $('#doProcess').click();
        $('#process').addClass("active");
        $('#afterCanNotSubmit').modal('show');
    }

    $("#processingDecision").change(function () {
        var selectValue = $(this).val();
        if (selectValue === 'MOHPRO005') {
            // hm
            $("#selectMohUserDiv").show();
            $("#aoRecommendationDiv").show();
            $("#reasonForRejectionDiv").hide();
        } else if (selectValue === 'MOHPRO027') {
            //approve
            $("#selectMohUserDiv").hide();
            $("#aoRecommendationDiv").hide();
            $("#reasonForRejectionDiv").hide();
        } else if (selectValue === 'MOHPRO024') {
            //reject
            $("#selectMohUserDiv").hide();
            $("#aoRecommendationDiv").hide();
            $("#reasonForRejectionDiv").show();
        } else {
            $("#selectMohUserDiv").hide();
            $("#aoRecommendationDiv").hide();
            $("#reasonForRejectionDiv").hide();
        }
    })
})
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-web/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}


function cancelJump() {
    $('#afterCanNotSubmit').modal('hide');
}