$(function () {
    var fileUploadMeta = readFileUploadMetaData();

    $("#" + fileUploadMeta.multiUploadInputId).change(function () {
        // get file type
        var uploadFileName = this.getAttribute(fileUploadMeta.uploadFileTypeInputNameAttr);
        if (!uploadFileName) {
            return false;
        }

        var fileAmt = this.files.length;
        for (var i = 0; i < fileAmt; ++i) {
            newUploadSingleFile(uploadFileName, this.files[i], fileUploadMeta.fileInputDivId, i);
        }
        // clear selected files
        this.value = "";

        // clear the name field
        this.setAttribute(fileUploadMeta.uploadFileTypeInputNameAttr, "");
    });


    $("a[data-upload-file]").click(function () {
        var name = $(this).attr("data-upload-file");
        var multiFileInput = $("#" + fileUploadMeta.multiUploadInputId);
        multiFileInput.attr(fileUploadMeta.uploadFileTypeInputNameAttr, name);
        multiFileInput.click();
    });


    $("#" + fileUploadMeta.echoReloadInputId).change(function () {
        var fileAmt = this.files.length;
        if (fileAmt === 0) {
            return true;
        }

        // get old id, 'delete id list', and file type
        var oldId = this.getAttribute(fileUploadMeta.echoReloadOldIdAttr);
        var deleteIdList = this.getAttribute(fileUploadMeta.echoReloadDeleteHiddenInputIdAttr);
        var uploadFileName = this.getAttribute(fileUploadMeta.uploadFileTypeInputNameAttr);
        if (!oldId || !deleteIdList || !uploadFileName) {
            return false;
        }

        // add old id in 'delete id list'
        deleteNewFile(oldId);

        // create new
        newUploadSingleFile(uploadFileName, this.files[0], fileUploadMeta.fileInputDivId);

        // clear value
        this.value = "";
        this.setAttribute(fileUploadMeta.echoReloadOldIdAttr, "");
        this.setAttribute(fileUploadMeta.echoReloadDeleteHiddenInputIdAttr, "");
        this.setAttribute(fileUploadMeta.uploadFileTypeInputNameAttr, "");
    });
})


function readFileUploadMetaData() {
    return {
        multiUploadInputId: 'multiUploadTrigger',
        uploadFileTypeInputNameAttr: 'data-file-ind',
        fileInputDivId: 'fileUploadInputDiv',
        echoReloadInputId: 'echoReloadTrigger',
        echoReloadOldIdAttr: 'data-old-id',
        echoReloadDeleteHiddenInputIdAttr: 'data-delete-list-input-id',
    };
}

function newUploadSingleFile(name, file, fileInputDivId, idSuffix) {
    // The suffix is necessary when user select multi files at a time
    var id = idSuffix ? name + +new Date() + idSuffix : name + +new Date();
    createSingleFileInput(id, name, file, fileInputDivId);

    // insert span and buttons
    var span = document.createElement("span");
    span.setAttribute("id", id + "Span");
    span.innerText = file.name + '(' + (file.size/1024).toFixed(1) + 'KB)';

    var delBtn = document.createElement("button");
    delBtn.setAttribute("type", "button");
    delBtn.setAttribute("class", "btn btn-secondary btn-sm delFileBtn");
    delBtn.setAttribute("onclick", "deleteFile('" + id + "')");
    delBtn.innerText = "Delete";

    var reloadBtn = document.createElement("button");
    reloadBtn.setAttribute("type", "button");
    reloadBtn.setAttribute("class", "btn btn-secondary btn-sm reUploadFileBtn");
    reloadBtn.setAttribute("onclick", "reloadFile('" + id + "')");
    reloadBtn.innerText = "Reload";

    var fileDiv = document.createElement("div");
    fileDiv.setAttribute("id", id + "FileDiv");
    fileDiv.appendChild(span);
    fileDiv.appendChild(delBtn);
    fileDiv.appendChild(reloadBtn);

    var gpa = $("a[data-upload-file=" + name + "]");
    var gp = gpa.closest('.file-upload-gp')[0];
    gp.insertBefore(fileDiv, gpa[0]);
}

function createSingleFileInput(id, name, file, fileInputDivId) {
    var newFileInput = document.createElement("input");
    newFileInput.setAttribute("type", "file");
    newFileInput.setAttribute("id", id);
    newFileInput.setAttribute("name", name);
    newFileInput.addEventListener("change", reloadFileUpdateTxt);
    var dataTransfer = new DataTransfer();
    dataTransfer.items.add(file);
    newFileInput.files = dataTransfer.files;
    $("#" + fileInputDivId).append(newFileInput);
}

// this function is intended to be used by single file input
function genFileInfo(fileInputEl) {
    var f = fileInputEl.files;
    return f[0].name + '(' + (f[0].size/1024).toFixed(1) + 'KB)';
}


function reloadFileUpdateTxt() {
    var id = this.getAttribute("id");
    var fileDiv = document.getElementById(id + "FileDiv");
    if (fileDiv) {
        var spanEl = document.getElementById(id + 'Span');
        spanEl.innerText = genFileInfo(this);
    }
}



//**************** START ************** before submit function ******************************
function deleteFile(id) {
    // delete input
    var inputEl = document.getElementById(id);
    inputEl.parentNode.removeChild(inputEl);

    // delete delete button, reload button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);
}

// before submit function
function reloadFile(id) {
    // trigger click on the input file
    var inputEl = document.getElementById(id);
    inputEl.click();
}
//**************** END ************** before submit function ******************************



//++++++++++++++++ START ++++++++++++++ new uploaded function ++++++++++++++++++++++++++++++
function deleteNewFile(id) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the 'delete list'
    var deleteSavedInput = document.getElementById("deleteNewFiles");
    appendCSInputVal(deleteSavedInput, id);
}

function reloadNewFile(id, type) {
    var fileUploadMeta = readFileUploadMetaData();
    var reloadFileInput = $("#" + fileUploadMeta.echoReloadInputId);
    reloadFileInput.attr(fileUploadMeta.echoReloadOldIdAttr, id);
    reloadFileInput.attr(fileUploadMeta.echoReloadDeleteHiddenInputIdAttr, "deleteNewFiles");
    reloadFileInput.attr(fileUploadMeta.uploadFileTypeInputNameAttr, type);
    reloadFileInput.click();
}
//++++++++++++++++ END ++++++++++++++ new uploaded function ++++++++++++++++++++++++++++++



//---------------- START ---------------- saved function ------------------------------
function deleteSavedFile(id) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the 'delete list'
    var deleteSavedInput = document.getElementById("deleteExistFiles");
    appendCSInputVal(deleteSavedInput, id);
}

function reloadSavedFile(id, type) {
    var fileUploadMeta = readFileUploadMetaData();
    var reloadFileInput = $("#" + fileUploadMeta.echoReloadInputId);
    reloadFileInput.attr(fileUploadMeta.echoReloadOldIdAttr, id);
    reloadFileInput.attr(fileUploadMeta.echoReloadDeleteHiddenInputIdAttr, "deleteExistFiles");
    reloadFileInput.attr(fileUploadMeta.uploadFileTypeInputNameAttr, type);
    reloadFileInput.click();
}
//---------------- END ---------------- saved function ------------------------------