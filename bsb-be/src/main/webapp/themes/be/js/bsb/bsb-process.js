$(function () {
    validate();
    $('#viewAppDiv').hide();
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
    $("#viewAppBtn").click(function () {
        $('#viewAppDiv').show();
    })
})
function validate(){
    var ifProcess = $("#ifProcess").val();
    if("Y"== ifProcess){
        $('#info').removeClass("active");
        $('#doProcess').click();
        $('#process').addClass("active");
    }
    // else if("Y" == '${doDocument}'){
    //     console.log("dod");
    //     $('#info').removeClass("active");
    //     $('#document').addClass("active");
    //     $('#doDocument').click();
    // }
    // if("Y"=='${uploadFileValidate}'){
    //     $('#uploadButton').click();
    // }
}