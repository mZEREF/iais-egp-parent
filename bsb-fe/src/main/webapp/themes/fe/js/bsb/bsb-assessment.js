$(function () {
    $("button[data-custom-ind=processSelfAssessment]").click(function () {
        showWaiting();
        var appId = $(this).attr("data-custom-app");
        var action = this.value;
        $("input[name='action_type']").val("load");
        $("input[name='action_value']").val(action);
        $("input[name='action_additional']").val(appId);
        $("#mainForm").submit();
    });

    $("button[data-custom-ind=printSelfAssessment]").click(function () {
        var appId = $(this).attr('value');
        showPopupWindow('/bsb-fe/eservice/INTERNET/MohBsbSubmitSelfAssessment/1/BindAction?loadPopupPrint=Y&action_type=print&action_value=Print&action_additional=' + appId);
    });

    $("button[data-custom-ind=uploadSelfAssessment]").click(function () {
        showWaiting();
        var appId = $(this).attr("data-custom-app");
        var action = this.value;
        $("input[name='action_type']").val("upload");
        $("input[name='action_value']").val(action);
        $("input[name='action_additional']").val(appId);
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

    $("#clear").click(function () {
        showWaiting();
        $("input[name='action_type']").val("clear");
        $("#mainForm").submit();
    });

    $("#save").click(function () {
        showWaiting();
        $("input[name='action_type']").val("save");
        $("#mainForm").submit();
    });

});

function initUploadFileData() {
    $('#_needReUpload').val(0);
    $('#_fileType').val("XLSX");
    $('#_singleUpload').val("1");
}

function clearFlagValueFEFile() {
    $("#reloadIndex").val(-1);
    $("#fileAppendId").val("");
    $("#uploadFormId").val("");
    $('.itemErrorTableDiv').hide();
    dismissWaiting();
}

function switchNextStep(index) {
    $('.config').hide();
    $('#' + index).show();
}