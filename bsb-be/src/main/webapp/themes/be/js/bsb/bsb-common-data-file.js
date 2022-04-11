$(function () {
    $("a[data-upload-data-file]").click(function () {
        var name = $(this).attr("data-upload-data-file");
        var fileInputEl = $("#" + name);
        fileInputEl.click();
    });


    $("input[data-data-file-input]").change(function () {
        var fileAmt = this.files.length;
        if (fileAmt === 0) {
            return;
        }
        showWaiting();
        $("[name='action_type']").val("loadDataFile");
        $("#mainForm").submit();
    });
});


function delete1DataFile(id) {
    showWaiting();
    $("[name='action_type']").val("deleteDataFile");
    $("[name='action_value']").val(id);
    $("#mainForm").submit();
}


function expandFile(srcPath, type) {
    showWaiting();
    $("[name='action_type']").val("expandFile");
    $("[name='action_value']").val(type);
    $("[name='action_additional']").val(srcPath);
    $("#mainForm").submit();
}