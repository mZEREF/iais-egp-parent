$(function () {
    $("button[data-custom-ind=processSelfAssessment]").click(function () {
        showWaiting();
        var action = this.value;
        $("input[name='action_type']").val("load");
        $("input[name='action_value']").val(action);
        $("#mainForm").submit();
    });

    $("button[data-custom-ind=printSelfAssessment]").click(function () {
        var appId = $(this).attr('value');
        showPopupWindow('/bsb-web/eservice/INTERNET/MohBsbSubmitSelfAssessment/1/BindAction?loadPopupPrint=Y&action_type=print&action_value=Print&action_additional=' + appId);
    });

    $("button[data-custom-ind=uploadSelfAssessment]").click(function () {
        showWaiting();
        var action = this.value;
        $("input[name='action_type']").val("upload");
        $("input[name='action_value']").val(action);
        $("#mainForm").submit();
    });

    $("button[data-custom-ind=downloadSelfAssessment]").click(function () {
        var appId = $(this).attr("data-custom-app");
        showPopupWindow(BASE_CONTEXT_PATH + '/self-assessment/exporting-data?appId=' + appId + '&stamp=" + new Date().getTime()');
    });

    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val("back");
        $("#mainForm").submit();
    });

    $("#save").click(function () {
        showWaiting();
        $("input[name='action_type']").val("save");
        $("#mainForm").submit();
    });

    $("#submit").click(function () {
        showWaiting();
        $("input[name='action_type']").val("save");
        $("input[name='action_value']").val("submit");
        $("#mainForm").submit();
    });
});

/* file upload start */
function initUploadFileData() {
    $('#_needReUpload').val(0);
    $('#_fileType').val("XLSX");
    $('#_singleUpload').val("1");
}

function handleOnClickingUploadBtn() {
    $('.itemErrorTableDiv').hide();
}
/* file upload end */

function switchNextStep(index) {
    $('.config').hide();
    $('#' + index).show();
}