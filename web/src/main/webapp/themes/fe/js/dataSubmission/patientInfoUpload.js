$(document).ready(function () {
    $('#_needReUpload').val(0);
    $('#_fileType').val("XLSX, CSV");

    $('#saveDraftBtn').remove();
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
    $('#itemSize').html('0');
    $('#nextBtn').html('Preview');
}

function deleteFileFeAjax(id,fileIndex) {
    $('#hasItems').val('0');
    callAjaxDeleteFile(id,fileIndex);
}