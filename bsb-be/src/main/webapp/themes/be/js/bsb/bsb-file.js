$(function () {
    // doc upload
    $("a[data-upload-file]").click(function () {
        // create input file
        var name = $(this).attr("data-upload-file")
        var id = name + +new Date();
        var newFileInput = document.createElement("input");
        newFileInput.setAttribute("type", "file");
        newFileInput.setAttribute("id", id);
        newFileInput.setAttribute("name", name);
        newFileInput.addEventListener("change", addReloadFile);
        $("#fileUploadInputDiv").append(newFileInput);
        // click to select file
        newFileInput.click();
    });
});

function downloadFile(cond, id) {
    var url;
    if (cond === 'saved') {
        url = "/bsb-be/ajax/doc/download/facility/repo/" + id;
    }
    window.open(url);
}

function deleteNewFile(id) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileTr");
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the delete list
    var deleteSavedInput = document.getElementById("deleteNewFiles");
    appendInputValue(deleteSavedInput, id);
}

function reloadNewFile(id) {
    deleteNewFile(id);
    $("a[data-upload-file=" + id + "]")[0].click();
}

function deleteFile(id) {
    // delete input
    var inputEl = document.getElementById(id);
    inputEl.parentNode.removeChild(inputEl);

    // delete delete button, reload button
    var fileDiv = document.getElementById(id + "FileTr");
    fileDiv.parentNode.removeChild(fileDiv);
}

function appendInputValue(input, value) {
    if (input.value) {
        input.value = input.value + "," + value;
    } else {
        input.value = value;
    }
}

function addReloadFile() {
    var id = this.getAttribute("id");
    var fileDiv = document.getElementById(id + "FileDiv");
    if (fileDiv) {
        // change filename and size
        var spanEl = document.getElementById(id + 'FileTr');
        spanEl.innerText = genFileInfo(this);
    } else {
        // add filename, size, delete and reload button
        var tr = document.createElement("tr");
        tr.setAttribute("id", id + "FileTr");
        tr.innerText = genFileInfo(this);

        // var delBtn = document.createElement("button");
        // delBtn.setAttribute("type", "button");
        // delBtn.setAttribute("class", "btn btn-secondary btn-sm delFileBtn");
        // delBtn.setAttribute("onclick", "deleteFile('" + id + "')");
        // delBtn.innerText = "Delete";
        //
        // var reloadBtn = document.createElement("button");
        // reloadBtn.setAttribute("type", "button");
        // reloadBtn.setAttribute("class", "btn btn-secondary btn-sm reUploadFileBtn");
        // reloadBtn.setAttribute("onclick", "reloadFile('" + id + "')");
        // reloadBtn.innerText = "Reload";
        //
        // fileDiv = document.createElement("div");
        // fileDiv.setAttribute("id", id + "FileTr");
        // fileDiv.appendChild(tr);
        // fileDiv.appendChild(delBtn);
        // fileDiv.appendChild(reloadBtn);

        var name = this.getAttribute("name");
        var gpa = $("a[data-upload-file=" + name + "]");
        var gp = gpa.closest('.file-upload-gp')[0];
        gp.insertBefore(fileDiv, gpa[0]);
    }
}

function genFileInfo(fileInputEl) {
    var f = fileInputEl.files;
    // return f[0].name + '(' + (f[0].size/1024).toFixed(1) + 'KB)';
    return "<td width=\"20%\"><p>" + f[0].name + "</p></td>" + "<td></td>" + "<td  width=\"20%\"><p>" + f[0].name + "</p></td>" +
        "<td width=\"15%\"><p>" + (f[0].size / 1024).toFixed(1) + "KB" + "</p></td>" + "<td width=\"20%\"><p>" + xxx + "</p></td>" + "<td width=\"25%\"><p>" + new Date() + "</p></td>";
}

function downloadRevokeFile(id) {
    var url = "/bsb-be/ajax/doc/download/revocation/repo/" + id;
    window.open(url);
}
