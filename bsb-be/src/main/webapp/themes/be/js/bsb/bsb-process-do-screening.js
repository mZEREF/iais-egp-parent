$(function () {
    $("#processingDecision").change(function () {
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO001") {
            $("#selectMohUserDiv").show();
            $("#rfiSubContent").hide();
            $("#reasonForRejectionDiv").hide();
            showRfcInsAndCerDiv(false);
        } else if (selectValue === "MOHPRO002") {
            $("#selectMohUserDiv").hide();
            $("#rfiSubContent").show();
            $("#reasonForRejectionDiv").hide();
            viewRfiApplication();
            showRfcInsAndCerDiv(false);
        } else if (selectValue === "MOHPRO003") {
            $("#inspectionRequiredN").attr("checked", "checked");
            $("#certificationRequiredN").attr("checked", "checked");
            $("#selectMohUserDiv").hide();
            $("#rfiSubContent").hide();
            $("#reasonForRejectionDiv").show();
            showRfcInsAndCerDiv(false);
        } else if (selectValue === "MOHPRO028") {
            $("#selectMohUserDiv").show();
            $("#rfiSubContent").hide();
            $("#reasonForRejectionDiv").hide();
            showRfcInsAndCerDiv(true);
        } else {
            $("#selectMohUserDiv").hide();
            $("#rfiSubContent").hide();
            $("#reasonForRejectionDiv").hide();
            showRfcInsAndCerDiv(false);
        }
    })
    $("#submitBtn").click(function () {
        showWaiting();
        $("input[type='radio']").removeAttr("disabled");
        $("[name='action_type']").val("submit");
        $('#mainForm').submit();
    })
})
function showRfcInsAndCerDiv(show){
    let insAndCerDiv = $("#insAndCerDiv");
    if (insAndCerDiv) {
        if (show){
            insAndCerDiv.show();
        } else {
            insAndCerDiv.hide();
        }
    }
}
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-web/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}
