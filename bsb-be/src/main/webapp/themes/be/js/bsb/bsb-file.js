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

function addReloadFile() {
    var id = this.getAttribute("id");
    var f = this.files;
    var tr = "<tr id="+id+'FileTr'+">"+"<td>" + f[0].name + "</td>" + "<td>"+"Internal"+"</td>" + "<td>"
        + "<a href=\"javascript:void(0)\" onClick=\"downloadNewSuspensionFile('+"+id+"+')\">"+f[0].name+"</a>"+ "</td>"
        + "<td>" + (f[0].size / 1024).toFixed(1) + "KB" + "</td>" + "<td>" + "" + "</td>" + "<td>" + new Date().toLocaleDateString() + "</td>"
        + "<td>" + "  <button type=\"button\" class=\"btn btn-secondary-del btn-sm\" onclick=\"javascript:deleteNewFile(this,'"+id+"');\">Delete</button>" +"</td>"+"</tr>";
    doAddTr(tr);
}

function doAddTr(tr) {
    $("#tbodyFileListId").append(tr);
}

function deleteNewFile(row,id) {
    $(row).parent('td').parent('tr').remove();
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

function deleteSavedFile(row,id) {
    $(row).parent('td').parent('tr').remove();
    //remove this id from newFileMap
    // add id into the delete list
    var deleteSavedInput = document.getElementById("deleteExistFiles");
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

function downloadFile(cond, id) {
    var url;
    if (cond === 'saved') {
        url = "/bsb-be/ajax/doc/download/facility/repo/" + id;
    }
    window.open(url);
}

function downloadWithdrawnFile(id) {
    var url = "/bsb-be/ajax/doc/download/withdrawn/repo/" + id;
    window.open(url);
}

function downloadNewSuspensionFile(id) {
    var url = "/bsb-be/ajax/doc/download/suspension/new/" + id;
    window.open(url);
}

function downloadSavedSuspensionFile(id) {
    var url = "/bsb-be/ajax/doc/download/suspension/repo/" + id;
    window.open(url);
}