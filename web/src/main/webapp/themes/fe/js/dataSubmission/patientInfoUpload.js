$(document).ready(function () {
    $('#_needReUpload').val(0);
    $('#_fileType').val("DS_PATIENT");

    $('#saveDraftBtn').remove();
    $('#backBtn').unbind('click');
    $('#backBtn').click(function () {
        showWaiting();
        submit('back');
    });
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
    clearErrorMsg();
    $('.itemErrorTableDiv').remove();
}

function deleteFileFeAjax(id,fileIndex) {
    $('#hasItems').val('0');
    $('#itemSize').html('0');
    $('#nextBtn').html('Preview');
    callAjaxDeleteFile(id,fileIndex);
}

function doActionWhenError(data) {
    $('#uploadFileShowId div').hide();
    var $btns = $('#uploadFileShowId').find('button');
    if ($btns.length >= 0) {
        $btns.trigger('click');
    }
}