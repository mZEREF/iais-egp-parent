/* Create map in each page's jsp or js, such as:
var testMap = {
    "name" : editEnableTextInput,
    "addNewSection" : editEnableRadioButton
};
 * Then register the click event for the 'edit' button to use the map.
 * */


function editEnableAllFields(fieldMap) {
    for (const key of fieldMap.keys()) {
        fieldMap.get(key)(key);
    }
}

function editEnableFields(fieldMap, enabledFields) {
    for (const f of enabledFields) {
        const enableFun = fieldMap.get(f);
        if (typeof(enableFun) != "undefined") {
            enableFun(f);
        }
    }
}






var indexInputName = undefined;
var separator = undefined;
$(function () {
    try {
        if (readSectionRepeatMetaData) {
            const meta = readSectionRepeatMetaData();
            indexInputName = meta.idxInputName;
            separator = meta.separator;
        }
    } catch (err) {
    }
})

function editEnable4MultiSection(name, func) {
    var idxInput = $("input[name=" + indexInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);
    for (var idx  of idxArr) {
        func(name + separator + idx);
    }
}




function editEnableTextInput(name) {
    var element = $("input[type='text'][name='" + name + "' ]");
    element.css("color", "");
    element.css("border-color", "");
    element.removeAttr("disabled");
}

function editEnableTextInput4MultiSection(name) {
    editEnable4MultiSection(name, editEnableTextInput);
}

function editEnableNumberInput(name) {
    var element = $("input[type='number'][name='" + name + "' ]");
    element.css("color", "");
    element.css("border-color", "");
    element.removeAttr("disabled");
}

function editEnableNumberInput4MultiSection(name) {
    editEnable4MultiSection(name, editEnableNumberInput)
}

function editEnableRadioButton(name) {
    $("input[type='radio'][name='" + name + "' ]").removeAttr("disabled");
}

function editEnableRadioButton4MultiSection(name) {
    editEnable4MultiSection(name, editEnableRadioButton);
}

function editEnableCheckbox(name) {
    $("input[type='checkbox'][name='" + name + "' ]").removeAttr("disabled");
}

function editEnableCheckbox4MultiSection(name) {
    editEnable4MultiSection(name, editEnableCheckbox);
}

function editEnableTextarea(name) {
    var element = $("textarea[name='" + name + "' ]");
    element.removeAttr("disabled");
    element.css("color", "");
    element.css("border-color", "");
}

function editEnableTextarea4MultiSection(name) {
    editEnable4MultiSection(name, editEnableTextarea);
}

function editEnableSelect(name) {
    var element = $("select[name='" + name + "' ]");
    element.removeAttr("disabled");
    element.next('div.nice-select').removeClass("disabled");
}

function editEnableSelect4MultiSection(name) {
    editEnable4MultiSection(name, editEnableSelect);
}

function editEnableFileUploadBtn(id) {
    const uploadDiv = $("#" + id).parent();
    uploadDiv.find(".btn.btn-secondary").attr("disabled", false);
    uploadDiv.find(".btn.file-upload.btn-secondary").css("pointer-events","auto");
}

function editDisableBtn(id) {
    const btn =$("#" + id);
    btn.attr("disabled",true).css("pointer-events","none");
    btn.css({opacity:0.5});
}

function editDisableBtn4MultiSection(id) {
    editEnable4MultiSection(id, editDisableBtn);
}

function editEnableBtn(id) {
    const btn =$("#" + id);
    btn.attr("disabled",false).css("pointer-events","auto");
    btn.css({opacity:1});
}

function editEnableBtn4MultiSection(id) {
    editEnable4MultiSection(id, editEnableBtn);
}

function editDisableAll() {
    const textInput = $("input[type='text']");
    textInput.css("color", "#999");
    textInput.css("border-color", "#ededed");
    textInput.attr("disabled",true);

    const numberInput = $("input[type='number']");
    numberInput.css("color", "#999");
    numberInput.css("border-color", "#ededed");
    numberInput.attr("disabled",true);

    $("input[type='radio']").attr("disabled",true);
    $("input[type='checkbox']").attr("disabled",true);

    const textareaInput = $("textarea");
    textareaInput.attr("disabled",true);
    textareaInput.css("color", "#999");
    textareaInput.css("border-color", "#ededed");

    $("div.nice-select").addClass("disabled");
    $(".file-upload-gp .btn.btn-secondary").attr("disabled",true);
    $(".btn.file-upload.btn-secondary").css("pointer-events","none");
}

function editEnableAll(){
    const textInput = $("input[type='text']");
    textInput.css("color", "");
    textInput.css("border-color", "");
    textInput.removeAttr("disabled");

    const numberInput = $("input[type='number']");
    numberInput.css("color", "");
    numberInput.css("border-color", "");
    numberInput.removeAttr("disabled");

    $("input[type='radio']").removeAttr("disabled");
    $("input[type='checkbox']").removeAttr("disabled");

    const textareaInput = $("textarea");
    textareaInput.removeAttr("disabled");
    textareaInput.css("color", "");
    textareaInput.css("border-color", "");

    $("div.nice-select").removeClass("disabled");
    $(".file-upload-gp .btn.btn-secondary").removeAttr("disabled");
    $(".btn.file-upload.btn-secondary").css("pointer-events","auto");
}