function paginationOperation(actionValue) {
    showWaiting();
    $("[name='action_type']").val("page");
    $("[name='action_value']").val(actionValue);
    $("#mainForm").submit();
}

function changePage(action) {
    $('#pageJumpNoTextchangePage').val(action);
    paginationOperation('changePage');
}

function sortRecords(field, sortType) {
    showWaiting();
    $("[name='action_type']").val("sort");
    $("[name='action_value']").val(field);
    $("[name='action_additional']").val(sortType);
    $("#mainForm").submit();
}


$(function () {
    $('#pageJumpNoPageSize').change(function () {
        paginationOperation('changeSize');
    })
});


function parseAndShowErrorMsg(data) {
    if (data && data !== '' && data !== '{}') {
        $("#iaisErrorFlag").val("BLOCK");
        var obj = JSON.parse(data);
        for (var field in obj) {
            if (obj.hasOwnProperty(field)) {
                showOneErrorMsg(field, obj[field]);
            }
        }
    }
}

function showOneErrorMsg(field, errorMsg) {
    $("span[data-err-ind=" + field + "]").text(errorMsg);
}

function clearAllErrMsg() {
    $("#iaisErrorFlag").val("");
    $("span[data-err-ind]").each(function(){
        $(this).html("");
    });
}



