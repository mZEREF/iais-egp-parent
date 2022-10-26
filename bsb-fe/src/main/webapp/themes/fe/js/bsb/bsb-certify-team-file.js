$(function (){
    var fileUploadMeta = readFileUploadMetaData();

    var isUpload = $("#isUploadCertFile").val();
    if(isUpload === 'Y'){
        window.location.href = "#certFile";
    }

    $("#" + fileUploadMeta.singleUploadInputId).change(function () {
        // get file type
        var uploadFileName = this.getAttribute(fileUploadMeta.uploadFileTypeInputNameAttr);
        if (!uploadFileName) {
            return false;
        }

        var fileAmt = this.files.length;
        for (var i = 0; i < fileAmt; ++i) {
            var id = uploadFileName + +new Date();
            createSingleFileInput(id, uploadFileName, this.files[i], fileUploadMeta.fileInputDivId);
        }

        showWaiting();
        $("[name='action_type']").val("loadCertifyTeamFile");
        $("#mainForm").submit();
    });

    $("a[certify-data-upload-file]").click(function (){
        var name = $(this).attr("certify-data-upload-file");
        var multiFileInput = $("#" + fileUploadMeta.singleUploadInputId);
        multiFileInput.attr(fileUploadMeta.uploadFileTypeInputNameAttr, name);
        multiFileInput.click();
    });
})

function deleteNewCertTeamFile(id,key) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileDiv");
    createUpload(key,fileDiv.parentNode);
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the 'delete list'
    var deleteSavedInput = document.getElementById("deleteNewCertTeamFiles");
    appendCSInputVal(deleteSavedInput, id);
}

function deleteSavedCertTeamFile(id,key) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileDiv");
    createUpload(key,fileDiv.parentNode);
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the 'delete list'
    var deleteSavedInput = document.getElementById("deleteExistCertTeamFiles");
    appendCSInputVal(deleteSavedInput, id);
}

function createUpload (key,node){
    var uploadBtn = document.createElement("a");
    uploadBtn.setAttribute("class", "btn file-upload btn-sm");
    uploadBtn.setAttribute("certify-data-upload-file",key);
    uploadBtn.setAttribute("href","javascript:void(0);");
    uploadBtn.addEventListener("click",function (){
        var fileUploadMeta = readFileUploadMetaData();
        var name = $(this).attr("certify-data-upload-file");
        var multiFileInput = $("#" + fileUploadMeta.singleUploadInputId);
        multiFileInput.attr(fileUploadMeta.uploadFileTypeInputNameAttr, name);
        multiFileInput.click();
    })
    uploadBtn.innerText = "Upload";

    node.appendChild(uploadBtn);
}
