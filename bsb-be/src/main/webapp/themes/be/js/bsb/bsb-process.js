$(function () {
    validate();
    var riskLevel = $("#riskLevel").val();
    if (riskLevel == "RLOTBA001" || riskLevel == "RLOTBA003"){
        $("#commentFalse").hide();
        $("#commentTrue").show();
    }else{
        $("#commentFalse").show();
        $("#commentTrue").hide();
    }
    $("#riskLevel").change(function () {
        var selectValue = $(this).val();
        if(selectValue == "RLOTBA001" || selectValue == "RLOTBA003") {
            $("#commentFalse").hide();
            $("#commentTrue").show();
        }else{
            $("#commentFalse").show();
            $("#commentTrue").hide();
        }
    })
    $("#submitButton").click(function () {
        $('#mainForm').submit();
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
function doOpenApp() {
    window.open ("/bsb-be/eservice/INTRANET/MohBeAppViewDetails");
}
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-be/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}