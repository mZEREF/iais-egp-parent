$(document).ready(function () {
    var $form = $('#mainForm');
    if ($form.length == 0) {
        $form = $(form);
    }
    $form.append('<input type="hidden" name="uploadFormId" id="uploadFormId" value="">');
    $form.append('<input type="hidden" name="fileAppendId" id="fileAppendId" value="">');
    $form.append('<input type="hidden" name="reloadIndex" id="reloadIndex" value="-1">');
    $form.append('<input type="hidden" name="_needReUpload" id="_needReUpload" value="1">');
    $form.append('<input type="hidden" name="_singleUpload" id="_singleUpload" value="0">');
    $form.append('<input type="hidden" name="_fileType" id="_fileType" value="">');
    $form.append('<input type="hidden" name="needGlobalMaxIndex" id="needGlobalMaxIndex" value="0">');
    $form.append('<input type="hidden" name="fileMaxSize" id="fileMaxSize" value="">');
    $form.append('<input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="">');

    var options = {
        url: BASE_CONTEXT_PATH + '/file/init',
        async: false
    };
    options.data = {};
    callCommonAjax(options, '_initFileData');
});

function _initFileData(data) {
    if (isEmpty(data)) {
        return;
    }
    console.log("init file data");
    $('#fileMaxSize').val(data.fileMaxSize);
    $('#fileMaxMBMessage').val(data.fileMaxMBMessage);
    $('#_fileType').val(data.fileType);
    if (typeof initUploadFileData === 'function') {
        initUploadFileData(data);
    }
}

function deleteFileFeAjax(id, fileIndex) {
    callAjaxDeleteFile(id, fileIndex);
}

function _getFileTag(fileAppendId) {
    var $file = $("#" + fileAppendId);
    if ($file.length == 0) {
        $file = $("input[type='file'][name='selectedFile']:first");
    }
    if ($file.length == 0) {
        $file = $("input[type='file']:first");
    }
    return $file;
}

function reUploadFileFeAjax(fileAppendId, index, idForm) {
    $("#reloadIndex").val(index);
    $("#fileAppendId").val(fileAppendId);
    $("#uploadFormId").val(idForm);
    $("#selectedFile").click();
}

function deleteFileFeDiv(id) {
    $("#" + id).remove();
    //$("#selectedFile").click();
    _getFileTag(fileAppendId).click();
}

function deleteFileFeAjax(id, fileIndex) {
    callAjaxDeleteFile(id, fileIndex);
}

function callAjaxDeleteFile(repoId, fileIndex) {
    showWaiting();
    var data = {"fileAppendId": repoId, "fileIndex": fileIndex};
    $.post(
        BASE_CONTEXT_PATH + "/file/deleteFeCallFile",
        data,
        function (data) {
            if (data != null && data == 1) {
                deleteFileFeDiv(repoId + "Div" + fileIndex);
            }
            dismissWaiting();
        }
    )
}

function deleteFileFeDiv(id) {
    $("#" + id).remove();
}

function ajaxCallUpload(idForm, fileAppendId) {
    ajaxCallUploadForMax(idForm, fileAppendId, false);
}

function ajaxCallUploadForMax(idForm, fileAppendId, needMaxGlobalIndex) {
    showWaiting();
    var reloadIndex = $("#reloadIndex").val();
    if (reloadIndex == -1) {
        $("#fileAppendId").val(fileAppendId);
    }
    $("#needGlobalMaxIndex").val(needMaxGlobalIndex);
    fileAppendId = $("#fileAppendId").val();
    $("#error_" + fileAppendId + "Error").html("");
    $("#uploadFormId").val(idForm);
    var form = new FormData($("#" + idForm)[0]);
    var maxFileSize = $("#fileMaxSize").val();
    var rslt = validateFileSizeMaxOrEmpty(maxFileSize);
    //alert('rslt:'+rslt);
    if (rslt == 'N') {
        $("#error_" + fileAppendId + "Error").html($("#fileMaxMBMessage").val());
        clearFlagValueFEFile();
    } else if (rslt == 'E') {
        clearFlagValueFEFile();
    } else {
        $.ajax({
            type: "post",
            url: BASE_CONTEXT_PATH + "/file/ajax-upload-file?stamp=" + new Date().getTime(),
            data: form,
            async: true,
            dataType: "json",
            processData: false,
            contentType: false,
            success: function (data) {
                if (data != null && data.description != null) {
                    if (data.msgType == "Y") {
                        if (reloadIndex != -1) {
                            $("#" + fileAppendId + "Div" + reloadIndex).after("<Div id = '" + fileAppendId + "Div" + reloadIndex + "Copy' ></Div>");
                            deleteFileFeDiv(fileAppendId + "Div" + reloadIndex);
                            $("#reloadIndex").val(-1);
                            $("#" + fileAppendId + "Div" + reloadIndex + "Copy").after(data.description);
                            deleteFileFeDiv(fileAppendId + "Div" + reloadIndex + "Copy");
                        } else {
                            $("#" + fileAppendId + "ShowId").append(data.description);
                        }
                        $("#error_" + fileAppendId + "Error").html("");
                        cloneUploadFile();
                    } else {
                        $("#error_" + fileAppendId + "Error").html(data.description);
                    }
                }
                if (typeof doActionAfterUploading === 'function') {
                    doActionAfterUploading(data, fileAppendId);
                }
                dismissWaiting();
            },
            error: function (msg) {
                //alert("error");
                dismissWaiting();
            }
        });
    }


}

function clearFlagValueFEFile() {
    $("#reloadIndex").val(-1);
    //$("#fileAppendId").val("");
    $("#uploadFormId").val("");
    if (typeof handleOnClickingUploadBtn === 'function') {
        handleOnClickingUploadBtn($("#fileAppendId").val());
    }
    dismissWaiting();
}

function validateFileSizeMaxOrEmpty(maxSize) {
    var $file = _getFileTag($("#fileAppendId").val());
    var fileV = $file.val();
    var file = $file.get(0).files[0];
    if (fileV == null || fileV == "" || file == null || file == undefined) {
        return "E";
    }
    var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
    //alert('fileSize:'+fileSize);
    //alert('maxSize:'+maxSize);
    fileSize = parseInt(fileSize);
    if (fileSize >= maxSize) {
        $file.after($file.clone().val(""));
        $file.eq('0').remove();
        return "N";
    }
    return "Y";
}

function cloneUploadFile() {
    var fileId = $("#fileAppendId").val();
    var $file = _getFileTag(fileId);
    $file.after($file.clone().val(""));
    $file.remove();
    if ('1' == $('#_singleUpload').val() && $('#' + fileId + 'ShowId').length > 0) {
        var $btns = $('#' + fileId + 'ShowId').find('button');
        if ($btns.length >= 2) {
            $btns.not(':last').trigger('click');
        }
    }
}
