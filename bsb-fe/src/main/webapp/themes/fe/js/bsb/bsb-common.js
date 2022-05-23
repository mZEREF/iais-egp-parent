//region pagination & sorting --------------------------------------------------------
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
//endregion --------------------------------------------------------



//region Save Draft --------------------------------------------------------
function cancelJumpAfterDraft() {
    $('#afterSaveDraft').modal('hide');
}
function jumpAfterDraft() {
    window.location.href = "/bsb-web/eservice/INTERNET/MohBSBInboxApp";
}


$(function () {
    $("#saveDraft").click(function () {
        showWaiting();
        $("input[name='action_type']").val("draft");
        $("#mainForm").submit();
    });

    if ($("#afterSaveAsDraft").val() === 'true') {
        $('#afterSaveDraft').modal('show');
    }
});
//endregion --------------------------------------------------------



//region Validation & Error Message --------------------------------------------------------
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
//endregion --------------------------------------------------------



//region input reset --------------------------------------------------------
/* Below parentEl are all jQuery objects */
function resetNiceSelect(parentEl) {
    parentEl.find("div.nice-select").each(function () {
        var firstOp = $(this).find("ul.list > li:first");
        // we need to click twice to set the value
        firstOp.trigger('click'); firstOp.trigger('click');
    });
}

function resetRadio(parentEl) {
    parentEl.find(":radio:checked").prop("checked", false);
}

function resetCheckbox(parentEl) {
    parentEl.find(":checkbox:checked").prop("checked", false);
}
//endregion --------------------------------------------------------



//region input value append --------------------------------------------------------
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
//endregion --------------------------------------------------------




function printPage(param, addt) {
    showWaiting();
    $("[name='action_type']").val("print");
    $("[name='action_value']").val(param);
    $("[name='action_additional']").val(addt);
    $("#mainForm").submit();
}