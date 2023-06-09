$(function () {
    var attachmentDiv = $("#attachmentUploadDiv");

    $("#doUpload").change(function () {
        attachmentDiv.show();
    });
    $("#noComment").change(function () {
        attachmentDiv.hide();
    });



    $("a[data-upload-file]").click(function () {
        // create input file
        var name = $(this).attr("data-upload-file");
        var id = name + +new Date();
        var newFileInput = document.createElement("input");
        newFileInput.setAttribute("type", "file");
        newFileInput.setAttribute("id", id);
        newFileInput.setAttribute("name", name);
        newFileInput.addEventListener("change", addAttachment);
        $("#fileUploadInputDiv").append(newFileInput);

        // click to select file
        newFileInput.click();
    });


    $("#submitBtn").click(function () {
        showWaiting();
        $("input[name='action_type']").val("submit");
        $("#mainForm").submit();
    });
})


function addAttachment() {
    var id = this.getAttribute("id");

    // add filename, size, delete and reload button
    var span = document.createElement("span");
    span.setAttribute("id", id + "Span");
    span.innerText = genFileInfo(this);

    var delBtn = document.createElement("button");
    delBtn.setAttribute("type", "button");
    delBtn.setAttribute("class", "btn btn-secondary btn-sm delFileBtn");
    delBtn.setAttribute("onclick", "deleteFile('" + id + "')");
    delBtn.innerText = "Delete";

    var fileDiv = document.createElement("div");
    fileDiv.setAttribute("id", id + "FileDiv");
    fileDiv.appendChild(span);
    fileDiv.appendChild(delBtn);

    var name = this.getAttribute("name");
    var gpa = $("a[data-upload-file=" + name + "]");
    var gp = gpa.closest('.file-upload-gp')[0];
    gp.insertBefore(fileDiv, gpa[0]);
}


function deleteFile(id) {
    // delete info and button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the delete list
    var deleteSavedInput = document.getElementById("deleteNewFiles");
    appendCSInputVal(deleteSavedInput, id);
}


function genFileInfo(fileInputEl) {
    var f = fileInputEl.files;
    return f[0].name + '(' + (f[0].size/1024).toFixed(1) + 'KB)';
}