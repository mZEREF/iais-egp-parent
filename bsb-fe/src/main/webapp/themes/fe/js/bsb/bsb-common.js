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

function resetNiceSelect(selector) {
    $(selector).find("div.nice-select").each(function () {
        var firstOp = $(this).find("ul.list > li:first");
        // we need to click twice to set the value
        firstOp.trigger('click'); firstOp.trigger('click');
    });
}


/* append comma separated input value */
function appendCSInputVal(input, value) {
    appendInputVal(input, value, ",");
}

/* append space separated input value */
function appendSSInputVal(input, value) {
    appendInputVal(input, value, " ");
}

/* append input value with specific separator */
function appendInputVal(input, value, separator) {
    if (input.value) {
        input.value = input.value + separator + value;
    } else {
        input.value = value;
    }
}

