$(document).ready(function () {
    $('#_needReUpload').val(0);
    $('#_fileType').val("XLSX, CSV");
});

function cloneUploadFile() {
    var fileId= '#uploadFile';
    $(fileId).after( $( fileId).clone().val(""));
    $(fileId).remove();
    var $btns = $('#uploadFileShowId').find('button');
    if ($btns.length >= 2) {
        $btns.not(':last').trigger('click');
    }
    $('#hasItems').val('0');
}

function deleteFileFeAjax(id,fileIndex) {
    $('#hasItems').val('0');
    callAjaxDeleteFile(id,fileIndex);
}