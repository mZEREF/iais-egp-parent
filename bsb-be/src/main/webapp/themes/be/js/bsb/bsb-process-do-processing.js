$(function () {
    validate();
    $("#submitBtn").click(function () {
        showWaiting();
        $('#mainForm').submit();
    });

    if ($("#canSubmit").val() === 'N') {
        $('#afterCanNotSubmit').modal('show');
    }
})
function validate(){
    var ifProcess = $("#ifProcess").val();
    if("Y" === ifProcess){
        $('#info').removeClass("active");
        $('#doProcess').click();
        $('#process').addClass("active");
    }
}
function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-web/ajax/doc/download/applicationDoc/" + repoId;
    window.open(url);
}

function cancelJump() {
    $('#afterCanNotSubmit').modal('hide');
}

function jumpToTaskList() {
    window.location.href = "/bsb-web/eservice/INTRANET/MohBsbTaskList";
}
