function doOpenApp() {
    window.open ("/bsb-be/eservice/INTRANET/MohBeAppViewDetails");
}

$(function () {
    $("#submitButton").click(function () {
        showWaiting();
        $("#mainForm").submit();
    });
});