$(function () {
    var processingDecisionObj = $("#processingDecision");
    if (processingDecisionObj.val() === "MOHPRO002") {
        $("#rfiSubContent").show();
    } else {
        $("#rfiSubContent").hide();
    }
    processingDecisionObj.change(function (){
        var selectValue = $(this).val();
        if (selectValue === "MOHPRO002") {
            $("#rfiSubContent").show();
            var url = "/bsb-web/eservice/INTRANET/MohBsbDOVerification/1/PrepareRfi";
            window.open(url,'_blank');
        } else {
            $("#rfiSubContent").hide();
        }
    })
    $("#submitBtn").click(function () {
        showWaiting();
        $('#mainForm').submit();
    })
})