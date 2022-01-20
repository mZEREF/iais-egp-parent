$(function () {
    $("#selectedFileShowText").click(function () {
        $("#internalFileSelectedFile").click();
    });
    $("#selectedFileShowTextName").click(function () {
        $("#internalFileSelectedFile").click();
    });

    $("#internalFileSelectedFile").change(fileChange);
})


function closeUploadDoc(){
    $('#internalFileSelectErrorSpan').html('')
    $('#internalFileTypeErrorSpan').html('')
    $('#internalFileType').val('');
    var modal = $('#uploadDoc');
    modal.dialog().dialog('close');
    modal.dialog('open');
    deleteSelectedInternalDoc();
}

function uploadInternalDoc(){
    $('#uploadInternalFileBtn').attr("disabled",true);
    showWaiting();
    if(validateUploadInternalDoc())
        callAjaxUploadFile();
    dismissWaiting();
}


function validateUploadInternalDoc() {
    var internalFileSelectedFile = $("#internalFileSelectedFile");
    var filename = getFileName(internalFileSelectedFile.val());
    var file = internalFileSelectedFile.get(0).files[0];
    if(filename === undefined || filename === "" || file === undefined || file == null) {
        $('#internalFileSelectErrorSpan').html('This is mandatory.');
        $('#uploadInternalFileBtn').attr("disabled", false);
        return false;
    }

    var maxSize = $("#internalFileMaxSize").val();
    maxSize = (maxSize == null || maxSize === "") ? 4 : parseInt(maxSize);
    var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
    fileSize = parseInt(fileSize);
    if(fileSize > maxSize) {
        $('#internalFileSelectErrorSpan').html($("#fileMaxMBMessage").val());
        if(fileSize >= 100) {
            deleteSelectedInternalDoc();
        }
        $('#uploadInternalFileBtn').attr("disabled", false);
        return false;
    }

    if(filename.length > 100){
        $('#internalFileSelectErrorSpan').html($("#fileMaxLengthMessage").val());
        $('#uploadInternalFileBtn').attr("disabled", false);
        return false;
    }

    var fileType = $("#internalFileAllowTypes").val();
    if(fileType == null || fileType === ""){
        fileType = "PDF,JPG,PNG,DOCX,DOC";
    }
    var fileParts = filename.split(".");
    var fileSuffix = fileParts[fileParts.length-1];
    if(fileType.indexOf(fileSuffix.toUpperCase()) === -1) {
        $('#internalFileSelectErrorSpan').html('Only files with the following extensions are allowed:'+ fileType +'. Please re-upload the file.');
        $('#uploadInternalFileBtn').attr("disabled", false);
        return false;
    }


    var docTypeLength = $('#internalFileType').val().trim().length;
    if (docTypeLength === 0) {
        $('#internalFileTypeErrorSpan').html('This is mandatory.');
        $('#uploadInternalFileBtn').attr("disabled", false);
        return false;
    }

    var docTypeMaxLength = 50;
    if(docTypeLength > docTypeMaxLength){
        $('#internalFileTypeErrorSpan').html('Exceeding the maximum length by ' + docTypeMaxLength );
        $('#uploadInternalFileBtn').attr("disabled", false);
        return false;
    }

    return true;
}



function deleteSelectedInternalDoc() {
    var file = $("#internalFileSelectedFile")
    var fileClone = file.clone();
    fileClone.change(fileChange);
    file.after(fileClone.val(""));
    file.remove();
    $('#selectedFileShowTextName').val("");
}


function fileChange(){
    var file = $(this).val();
    if(file != null && file !== ""){
        $('#selectedFileShowTextName').val(getFileName(file));
    }else {
        $('#selectedFileShowTextName').val("");
    }
}

// need to change to support unix-like system
function getFileName(o) {
    var pos = o.lastIndexOf("\\");
    return o.substring(pos + 1);
}



function deleteInternalDocument(row, appId, repoId) {
    showWaiting();
    callAjaxDeleteFile(appId, repoId);
    $(row).parent('td').parent('tr').remove();
    // if no record left, we show 'No record found.'
    var tbody = $("#internalDocBody");
    if (tbody.children("tr").length === 0) {
        tbody.append('<tr id="noRecordTr"><td colspan="6" style="text-align: left">No record found.</td></tr>');
    }
    dismissWaiting();
}


function callAjaxUploadFile() {
    var form = new FormData($("#fileUploadForm")[0]);
    $.ajax({
        type: "post",
        url: "/bsb-be/ajax/doc/internal-doc",
        data: form,
        async: true,
        processData: false,
        contentType: false,
        dataType: "json",
        success: function (data) {
            if (data.success) {
                // check if there is no line exists, we need to remove the 'No record found.'.
                var tbody = $("#internalDocBody");
                tbody.children("#noRecordTr").remove();

                // insert new line
                var newTr = '<tr>' +
                                '<td>' + data.docType +'</td>' +
                                '<td><a href="javascript:void(0)" onclick="downloadInternalDocument(\'' + data.fileRepoId + '\',\'' + data.docName + '\')">' + data.docName +'</a></td>' +
                                '<td>' + (data.docSize/1024).toFixed(0) +'KB</td>' +
                                '<td>' + data.submitByName +'</td>' +
                                '<td>' + data.submitDtStr + '</td>' +
                                '<td><button type="button" class="btn btn-secondary-del btn-sm" onclick="deleteInternalDocument(this,\'' + data.applicationId + '\',\'' + data.fileRepoId + '\');">DELETE</button></td>' +
                            '</tr>';
                tbody.append(newTr);

                // close modal
                $("#cancelDoc").click();
            } else {
                $('#internalFileSelectErrorSpan').html(data.error_msg);
            }
            $('#uploadInternalFileBtn').attr("disabled", false);
        },
        error: function (data) {
            $('#internalFileSelectErrorSpan').html("ERROR");
        }
    });
}


function callAjaxDeleteFile(appId, repoId) {
    var reqData = {"appId":appId,"repoId":repoId};
    $.ajax({
        type: "delete",
        url: "/bsb-be/ajax/doc/internal-doc",
        data: reqData,
        async: true,
        dataType: "json",
        success: function (data) {
            // do nothing now
        }
    });
}


function downloadInternalDocument(repoId, filename) {
    var url = "/bsb-be/ajax/doc/internal-doc?repoId=" + repoId + "&filename=" + filename;
    url = encodeURI(url);
    window.open(url);
}