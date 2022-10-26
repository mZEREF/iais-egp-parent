$(function () {
    $("#submitBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("submit");
        $('#mainForm').submit();
    })
})
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-web/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}