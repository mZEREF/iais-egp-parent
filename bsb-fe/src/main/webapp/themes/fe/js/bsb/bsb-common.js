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

function resetInput(parentEl){
    parentEl.find("input").attr("value","");
}

function resetRadio(parentEl) {
    parentEl.find(":radio:checked").prop("checked", false);
}

function resetNotDisabledRadio(parentEl) {
    parentEl.find(":radio:checked:not(:disabled)").prop("checked", false);
}

function resetCheckbox(parentEl) {
    parentEl.find(":checkbox:checked").prop("checked", false);
}

function resetNotDisabledCheckbox(parentEl) {
    parentEl.find(":checkbox:checked:not(:disabled)").prop("checked", false);
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

//contactNoIdPrefix is text contactNo id
//suffix is special contactNo suffix
//this method is used to handle contact input maxlength is change on some special condition
function modifyContactNoLength(relevant, contactNoIdPrefix, suffix) {
    let nat = $(relevant).val();

    var contactNoSelector = $("#"+contactNoIdPrefix+suffix);
    if('NAT0001' === nat){
        contactNoSelector.attr('maxLength','8');
    } else {
        contactNoSelector.attr('maxLength','20')
    }
}


//contactNoIdPrefix is input id number's id
//suffix is special idNumber suffix
//this method is used to handle idNumber input maxlength is change on some special condition
function modifyIdNumberLength(relevant, idNoIdPrefix, suffix) {
    let idType = $(relevant).val();

    var idNoSelector = $("#"+idNoIdPrefix+suffix);
    if('IDTYPE003' === idType){
        idNoSelector.attr('maxLength','13');
    } else {
        idNoSelector.attr('maxLength','9')
    }
}