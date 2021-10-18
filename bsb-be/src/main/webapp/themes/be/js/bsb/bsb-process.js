$(function () {
    validate();
    $("#submitButton").click(function () {
        $('#mainForm').submit();
    })
    $("#back").click(function (){
        showWaiting();
        $("[name='crud_action_type']").val("back");
        $('#mainForm').submit();
    })
    $("#backFromDoc").click(function (){
        $('#doInfo').click();
    })
    $("#inspectionOrCertificationBack").click(function (){
        $('#doDocument').click();
    })
    $("#contentBack").click(function (){
        $('#doInspectionOrCertification').click();
    })
    $("#processBack").click(function (){
        $('#doContent').click();
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