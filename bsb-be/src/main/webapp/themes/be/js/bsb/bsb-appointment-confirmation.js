$(function () {
    $("#processingDecision").change(function () {
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO002") {
            // rfi
            $("#rfiSubContent").show();
            $("#selectAnotherDate").hide();
        } else if (selectValue === "MOHPRO010") {
            // select another
            $("#selectAnotherDate").show();
            $("#rfiSubContent").hide();
        } else {
            // confirm
            $("#selectAnotherDate").hide();
            $("#rfiSubContent").hide();
        }
    })
});

function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-web/ajax/doc/download/appointment/fac/repo/" + repoId;
    window.open(url);
}