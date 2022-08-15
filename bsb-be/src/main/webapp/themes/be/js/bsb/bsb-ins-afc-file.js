$(function () {
    $("#selectedFileShowText").click(function () {
        $("#selectedFileInput").click();
    });
    $("#selectedFileShowTextName").click(function () {
        $("#selectedFileInput").click();
    });

    $("#selectedFileInput").change(fileChange);
});
function closeUploadDoc() {
    $('#selectedFileInputErrorSpan').html('')
    $('#fileTypeErrorSpan').html('')
    $("#uploadDoc .current").text("Please Select");
    $("#fileType option:first").prop("selected", 'selected');
    var modal = $('#uploadDoc');
    modal.dialog().dialog('close');
    modal.dialog('open');
    deleteSelectedDoc();
}

function uploadDoc() {
    $('#uploadFileBtn').attr("disabled", true);
    showWaiting();
    // var input1 = document.getElementById("selectedFileInput");
    // addFileTable(input1);
    // addFileDisplayInfo(input1);
    if (validateUploadInternalDoc()) {
        var fileType = $("#fileType option:selected").val();
        $("input[name='docType']").val(fileType);
        $("input[name='action_value']").val("uploadDoc");
        // $("#cancelDoc").click();
        $("#mainForm").submit();
    }
    dismissWaiting();
}
function validateUploadInternalDoc() {
    var internalFileSelectedFile = $("#selectedFileInput");
    var filename = getFileName(internalFileSelectedFile.val());
    var file = internalFileSelectedFile.get(0).files[0];
    if(filename === undefined || filename === "" || file === undefined || file == null) {
        $('#selectedFileInputErrorSpan').html('This is mandatory.');
        $('#uploadFileBtn').attr("disabled", false);
        return false;
    }

    var maxSize = $("#internalFileMaxSize").val();
    maxSize = (maxSize == null || maxSize === "") ? 4 : parseInt(maxSize);
    var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
    fileSize = parseInt(fileSize);
    if(fileSize > maxSize) {
        $('#selectedFileInputErrorSpan').html($("#fileMaxMBMessage").val());
        if(fileSize >= 100) {
            deleteSelectedInternalDoc();
        }
        $('#uploadFileBtn').attr("disabled", false);
        return false;
    }

    if(filename.length > 100){
        $('#selectedFileInputErrorSpan').html($("#fileMaxLengthMessage").val());
        $('#uploadFileBtn').attr("disabled", false);
        return false;
    }

    var fileType = $("#internalFileAllowTypes").val();
    if(fileType == null || fileType === ""){
        fileType = "PDF,JPG,PNG,DOCX,DOC";
    }
    var fileParts = filename.split(".");
    var fileSuffix = fileParts[fileParts.length-1];
    if(fileType.indexOf(fileSuffix.toUpperCase()) === -1) {
        $('#selectedFileInputErrorSpan').html('Only files with the following extensions are allowed:'+ fileType +'. Please re-upload the file.');
        $('#uploadFileBtn').attr("disabled", false);
        return false;
    }


    var docTypeLength = $('#internalFileType').val().trim().length;
    if (docTypeLength === 0) {
        $('#fileTypeErrorSpan').html('This is mandatory.');
        $('#uploadFileBtn').attr("disabled", false);
        return false;
    }

    var docTypeMaxLength = 50;
    if(docTypeLength > docTypeMaxLength){
        $('#fileTypeErrorSpan').html('Exceeding the maximum length by ' + docTypeMaxLength );
        $('#uploadFileBtn').attr("disabled", false);
        return false;
    }

    return true;
}

function deleteSelectedDoc() {
    var file = $("#selectedFileInput")
    var fileClone = file.clone();
    fileClone.change(fileChange);
    file.after(fileClone.val(""));
    file.remove();
    $('#selectedFileShowTextName').val("");
}


function fileChange() {
    var file = $(this).val();
    if (file != null && file !== "") {
        $('#selectedFileShowTextName').val(getFileName(file));
    } else {
        $('#selectedFileShowTextName').val("");
    }
}

// need to change to support unix-like system
function getFileName(o) {
    var pos = o.lastIndexOf("\\");
    return o.substring(pos + 1);
}

function addFileTable(input1) {
    var i = 1;
    var id = input1.id + i;
    var user = $("input[name='loginUser']").val();
    var f = input1.files;

    var docType = $("#fileType").val();

    var tr = "<tr id=" + id + 'FileTr' + ">" + "<td style=\"width: 5%;text-align: center\">" + 1 + "</td>" + "<td style=\"width: 15%;text-align: center\">" + f[0].name + "</td>" + "<td style=\"width: 15%;text-align: center\">" + docType + "</td>" + "<td style=\"width: 12%;text-align: center\">" + user + "</td>"
        + "<td style=\"width: 12%;text-align: center\">" + new Date().toLocaleDateString() + "</td>" + "<td style=\"width: 15%;text-align: center\">" + 1 + "</td>"
        + "<td>" + "<a class=\"btn btn-secondary btn-sm\" href=\"javascript:void(0)\" onClick=\"downloadNewSuspensionFile('+" + id + "+')\">" + "Download" + "</a>" + "</td>"
        + "<td style=\"text-align: center\">" + "<p>" + "<input type='checkbox' readonly/>" + "</p>" + "</td>" + "<td style=\"text-align: center\">" + "<p>" + "<input type='checkbox' readonly/>" + "</p>" + "</td>" + "<td style=\"text-align: center\">" + "<p>" + "<input type='checkbox' readonly/>" + "</p>" + "</td>"
        + "</tr>";
    doAddTr(tr);
}

function addFileDisplayInfo(input1) {
    var i = 1;
    var id = input1.id+i;
    var fileDiv = document.getElementById(id + "FileDiv");
    if (!fileDiv) {
        fileDiv = document.createElement("div");
        fileDiv.setAttribute("id", id + "FileDiv");
    }
    // add filename, size, delete and reload button
    var span = genSpan(id,input1);
    var delBtn = genDelBtn(id);

    fileDiv.appendChild(span);
    fileDiv.appendChild(delBtn);
    var tr = document.createElement("tr");
    fileDiv.appendChild(tr);

    var name = input1.name;
    var gpa = $("#uploadDocModalBtn");
    var gp = gpa.closest('.file-upload-gp')[0];
    gp.insertBefore(fileDiv, gpa[0]);
    i++;
}

function genSpan(id,input1) {
    var span = document.createElement("span");
    span.setAttribute("id", id + "Span");
    span.innerText = genFileInfo(input1);
    return span;
}

function genDelBtn(id) {
    var delBtn = document.createElement("button");
    delBtn.setAttribute("type", "button");
    delBtn.setAttribute("class", "btn btn-secondary btn-sm delFileBtn");
    delBtn.setAttribute("onclick", "deleteNewFile('" + id + "')");
    delBtn.innerText = "Delete";
    return delBtn;
}

function genFileInfo(fileInputEl) {
    var f = fileInputEl.files;
    return f[0].name + '(' + (f[0].size/1024).toFixed(1) + 'KB)';
}

function doAddTr(tr) {
    $("#tbodyFileListId").append(tr);
}

function deleteNewFile(id) {
    var docTable = document.getElementById("docTable");
    var trNodes = docTable.getElementsByTagName("tr");
    var delTrId = id+"FileTr";
    //remove table tr
    if (trNodes.length > 1){
        for (let i = 1; i < trNodes.length; i++) {
            var trId = trNodes[i].id;
            if (delTrId === trId){
                trNodes[i].remove();
            }
        }
    }
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);

    //remove this id from newFileMap
    // add id into the delete list
    var deleteNewInput = document.getElementById("deleteNewFiles");
    appendInputValue(deleteNewInput, id);
}

function appendInputValue(input, value) {
    if (input.value) {
        input.value = input.value + "," + value;
    } else {
        input.value = value;
    }
}

function downloadFile(cond,id) {
    var url;
    if (cond === 'saved') {
        url = "/bsb-web/ajax/doc/download/insAFC/repo/" + id;
    } else if (cond === 'new') {
        url = "/bsb-web/ajax/doc/download/insAFC/new/" + id;
    }
    window.open(url);
}