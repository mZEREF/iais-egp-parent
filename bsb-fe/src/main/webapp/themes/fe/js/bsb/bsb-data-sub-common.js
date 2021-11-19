$(function () {
    isHidden();
    $("#addNewSection").click(function () {
        var meta = readSectionRepeatMetaData();
        addSection(meta.idxInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    });

    $(".removeBtn").click(removeBtnEventHandler);

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

function isHidden() {
    var meta = readSectionRepeatMetaData();
    var idxInput = $("input[name=" + meta.idxInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);
    for (var i of idxArr) {
        var schedule = $("#scheduleType--v--"+i).val();
        if (schedule !== 'SCHTYPE006' && schedule !== '') {
            $("#agentFifth--v--" + i).hide();
            $("#agentEpFifth--v--" + i).show();
        } else if (schedule === "SCHTYPE006") {
            $("#agentEpFifth--v--" + i).hide();
            $("#agentFifth--v--" + i).show();
        }
    }
}

function isHidden2(i) {
    var schedule = $("#scheduleType--v--"+i).val();
    if (schedule !== 'SCHTYPE006' && schedule !== '') {
        $("#agentFifth--v--" + i).hide();
        $("#agentEpFifth--v--" + i).show();
    } else if (schedule === "SCHTYPE006") {
        $("#agentEpFifth--v--" + i).hide();
        $("#agentFifth--v--" + i).show();
    } else {
        $("#agentFifth--v--" + i).hide();
        $("#agentEpFifth--v--" + i).hide();
    }
}

function addSection(idxInputName, sectionIdPrefix, headerTitlePrefix, sectionGroupId, separator) {
    var idxInput = $("input[name=" + idxInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);

    var currentAmt = idxArr.length;
    var nextIdx = parseInt(idxArr[currentAmt - 1]) + 1;

    var section0 = $("#" + sectionIdPrefix + separator + "0");
    if (currentAmt === 1) {
        if (sectionIdPrefix === 'notTSection' || sectionIdPrefix === 'conSection' || sectionIdPrefix === 'disSection' || sectionIdPrefix === 'expSection' || sectionIdPrefix === 'recSection') {
            var headerDiv = newSectionHeader(1, 0, headerTitlePrefix);
            section0[0].insertBefore(headerDiv, section0[0].firstChild);
        } else {
            changeFirstSectionHeader(sectionIdPrefix, 0, headerTitlePrefix, true);
        }
    }
    var newSectionDivJqObj = section0.clone(true);
    var newSectionDiv = newSectionDivJqObj[0];
    var newHeaderDiv = newSectionHeader(currentAmt + 1, nextIdx, headerTitlePrefix);
    newSectionDiv.replaceChild(newHeaderDiv, newSectionDiv.children[0]);
    modifyClonedNode(newSectionDiv, nextIdx, separator);

    var sectionGroupDiv = document.getElementById(sectionGroupId);
    sectionGroupDiv.appendChild(newSectionDiv);
    appendSSInputVal(idxInput[0], nextIdx);

    var text = "upload--v--"+(currentAmt);
    $($("a[data-upload-file = "+ text +"]")[0]).prevAll().remove();

    /* Reset select to first option */
    newSectionDivJqObj.find("div.nice-select").each(function (index) {
        // This unique class is intended for auto test framework.
        // ('unq' is just a random string used to distinguish it from already exists name)
        $(this).attr("class", "nice-select " + sectionIdPrefix + separator + nextIdx + "unq" + index);
        var firstOp = $(this).find("ul.list > li:first-child");
        // we need to click twice to set the value
        firstOp.trigger('click'); firstOp.trigger('click');
    });

    /* Reset all radio button and checkbox to unchecked */
    newSectionDivJqObj.find(":radio:checked").prop("checked", false);
    newSectionDivJqObj.find(":checkbox:checked").prop("checked", false);

    /* Set date picker */
    newSectionDivJqObj.find(".date_picker").each(function () {
        var oldEL = $(this);
        var newEL = newDatePicker(oldEL);
        oldEL.replaceWith(newEL);
        newEL.datepicker({
            format:"dd/mm/yyyy",
            autoclose:true,
            todayHighlight:true,
            orientation:'bottom'
        });
    });

    isHidden2(currentAmt);
}

/* add or remove num in title for the first section */
function changeFirstSectionHeader(sectionIdPrefix, idx, titlePrefix, separator, add) {
    var title = add ? titlePrefix + 1 : titlePrefix;
    $("#" + sectionIdPrefix + separator + idx + " > div > h3").text(title);
}

function deleteNewFile(id) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the delete list
    var deleteSavedInput = document.getElementById("deleteNewFiles");
    appendInputValue(deleteSavedInput, id);
}

function reloadNewFile(id) {
    deleteNewFile(id);
    var ids = id.split("--v--");
    var text = ids[0]+"--v--"+ids[1].charAt(0);
    $("a[data-upload-file=" + text + "]")[0].click();
}

function reloadFile(id) {
    // trigger click on the input file
    var inputEl = document.getElementById(id);
    inputEl.click();
}

function deleteFile(id) {
    // delete input
    var inputEl = document.getElementById(id);
    inputEl.parentNode.removeChild(inputEl);

    // delete delete button, reload button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);
}

function appendInputValue(input, value) {
    if (input.value) {
        input.value = input.value + "," + value;
    } else {
        input.value = value;
    }
}

function changeH3(sectionIdPrefix, num, titlePrefix, separator) {
    var title = num > 0 ? titlePrefix + num : titlePrefix;
    $("#" + sectionIdPrefix + separator + num + " > div > h3").text(title);
}

function removeBtnEventHandler() {
    var idx = $(this).attr("data-current-idx");
    var meta = readSectionRepeatMetaData();
    if (meta) {
        removeSection(idx, meta.idxInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    }
}

function removeSection(idx, idxInputName, sectionIdPrefix, titlePrefix, sectionGroupId, separator) {
    var idxInput = $("input[name=" + idxInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);

    var nextAmt = idxArr.length - 1;
    idxArr = removeIdx(idxArr, idx);
    deleteSection(sectionIdPrefix, separator, idx);
    idxInput.val(idxArr.join(" "));

    if (nextAmt === 1) {
        if (sectionIdPrefix === 'notTSection' || sectionIdPrefix === 'conSection' || sectionIdPrefix === 'disSection' || sectionIdPrefix === 'expSection' || sectionIdPrefix === 'recSection') {
            var notSection0 = document.getElementById(sectionIdPrefix + separator + "0");
            notSection0.removeChild(notSection0.children[0]);
        } else {
            changeFirstSectionHeader(sectionIdPrefix, 0, titlePrefix, separator, false);
        }
    } else {
        refreshH3(sectionGroupId, titlePrefix);
    }
}

/* remove item in array */
function removeIdx(idxArr, idx) {
    var newArr = [];
    var j = -1;
    for (var i of idxArr) {
        if (i !== idx) {
            newArr[++j] = i;
        }
    }
    return newArr;
}

function refreshH3(sectionGroupId, titlePrefix) {
    var num = 0;
    $("#" + sectionGroupId + " > section > div:first-child > h3").each(function () {
        $(this).text(titlePrefix + " " + ++num);
    });
}

/* oldEl is a JQuery Object */
function newDatePicker(oldEl) {
    var attributes = oldEl.prop("attributes");

    var newEl = document.createElement("input");
    var newElJq = $(newEl);
    // loop through attributes and apply them on new element
    $.each(attributes, function() {
        newElJq.attr(this.name, this.value);
    });
    newElJq.val("");
    return newElJq;
}

function addReloadFile() {
    var id = this.getAttribute("id");
    var fileDiv = document.getElementById(id + "FileDiv");
    if (fileDiv) {
        // change filename and size
        var spanEl = document.getElementById(id + 'Span');
        spanEl.innerText = genFileInfo(this);
    } else {
        // add filename, size, delete and reload button
        var span = document.createElement("span");
        span.setAttribute("id", id + "Span");
        span.innerText = genFileInfo(this);

        var delBtn = document.createElement("button");
        delBtn.setAttribute("type", "button");
        delBtn.setAttribute("class", "btn btn-secondary btn-sm delFileBtn");
        delBtn.setAttribute("onclick", "deleteFile('" + id + "')");
        delBtn.innerText = "Delete";

        var reloadBtn = document.createElement("button");
        reloadBtn.setAttribute("type", "button");
        reloadBtn.setAttribute("class", "btn btn-secondary btn-sm reUploadFileBtn");
        reloadBtn.setAttribute("onclick", "reloadFile('" + id + "')");
        reloadBtn.innerText = "Reload";

        fileDiv = document.createElement("div");
        fileDiv.setAttribute("id", id + "FileDiv");
        fileDiv.appendChild(span);
        fileDiv.appendChild(delBtn);
        fileDiv.appendChild(reloadBtn);

        var name = this.getAttribute("name");
        var gpa = $("a[data-upload-file=" + name + "]");
        var gp = gpa.closest('.file-upload-gp')[0];
        gp.insertBefore(fileDiv, gpa[0]);
    }
}

function genFileInfo(fileInputEl) {
    var f = fileInputEl.files;
    return f[0].name + '(' + (f[0].size/1024).toFixed(1) + 'KB)';
}

function downloadFile(cond, id, filename) {
    var url = "/bsb-fe/ajax/doc/download/dataSub/new/" + id;

    if (url) {
        $.ajax({
            type: "GET",
            url: url,
            async: true,
            responseType: "blob",
            success: function(content) {
                expDownload(content, filename);
            },
            error: function () {
                $("span[data-err-ind='" + type + "']").innerText = "Fail to download the file";
            }
        });
    }
}

function expDownload(content, filename) {
    var a = document.createElement("a");
    var blob = new Blob([content]);
    a.href = window.URL.createObjectURL(blob);
    a.target = "_parent";
    a.download = filename;
    a.click();
    a.remove();
}

//---------- START kinda common func ----------
function updateInputAmt(hiddenInputName, changeAmt) {
    var amtHiddenInput = $("input[name=" + hiddenInputName + "]");
    var currentAmt = parseInt(amtHiddenInput.val());
    var nextAmt = currentAmt + changeAmt;
    amtHiddenInput.val(nextAmt);
    return nextAmt;
}

function readSectionRepeatMetaData() {
    return {
        idxInputName: $("#section_repeat_section_idx_name").val(),
        sectionIdPrefix: $("#section_repeat_section_id_prefix").val(),
        headerTitlePrefix: $("#section_repeat_header_title_prefix").val(),
        sectionGroupId: $("#section_repeat_section_group_id").val(),
        separator: $("#section_repeat_separator").val()
    };
}

/* num is amount */
function newSectionHeader(num, idx, titlePrefix) {
    var h3El = document.createElement("h3");
    h3El.className = 'col-xs-9 col-sm-10 col-md-11';
    h3El.style.cssText = 'border-bottom: 1px solid black';
    h3El.innerText = titlePrefix + num;
    var divEl = document.createElement("div");
    divEl.className = 'form-group';
    divEl.append(h3El);
    if (num !== 1) {
        var closeButtonDiv = newCloseButton(idx);
        divEl.appendChild(closeButtonDiv);
    }
    return divEl;
}

function newCloseButton(idx) {
    var emEl = document.createElement("em");
    emEl.setAttribute('data-current-idx', idx.toString());
    emEl.className = 'fa fa-times-circle del-size-36 cursorPointer removeBtn';
    emEl.addEventListener("click", removeBtnEventHandler);
    var h4El = document.createElement("h4");
    h4El.className = 'text-danger';
    var divEl = document.createElement("div");
    divEl.className = 'col-sm-1';
    h4El.append(emEl);
    divEl.append(h4El);
    return divEl;
}

function modifyClonedNode(node, idx, separator) {
    if (node.children) {
        for (var i = 0, length = node.children.length; i < length; i++) {
            modifyClonedNode(node.children[i], idx, separator);
        }
    }

    if (node.nodeName === 'SECTION') {
        replaceNodeAttributeSuffixNum(node, 'id', idx, separator);
    } else if (node.nodeName === 'LABEL') {
        replaceNodeAttributeSuffixNum(node, 'for', idx, separator);
    } else if (node.nodeName === 'SPAN') {
        replaceNodeAttributeSuffixNum(node, 'data-err-ind', idx, separator);
    } else if (node.nodeName === 'INPUT') {
        replaceNodeAttributeSuffixNum(node, 'id', idx, separator);
        replaceNodeAttributeSuffixNum(node, 'name', idx, separator);
        node.value = "";
    } else if (node.nodeName === 'DIV') {
        replaceNodeAttributeSuffixNum(node, 'id', idx, separator);
        replaceNodeAttributeSuffixNum(node, 'class', idx, separator);
    } else if (node.nodeName === 'SELECT') {
        replaceNodeAttributeSuffixNum(node, 'id', idx, separator);
        replaceNodeAttributeSuffixNum(node, 'name', idx, separator);
    }else if(node.nodeName === 'A'){
        replaceNodeAttributeSuffixNum(node,'data-upload-file',idx,separator);
    }else if(node.nodeName === 'TEXTAREA'){
        replaceNodeAttributeSuffixNum(node,'id',idx,separator);
        replaceNodeAttributeSuffixNum(node,'name',idx,separator);
    }
}

function deleteSection(sectionIdPrefix, separator, idx) {
    var section = document.getElementById(sectionIdPrefix + separator + idx);
    section.parentNode.removeChild(section);
}

function replaceNodeAttributeSuffixNum(node, attrName, num, separator) {
    if (node.hasAttribute(attrName)) {
        var rawValue = node.getAttribute(attrName);
        var parts = rawValue.split(separator);
        var newValue;
        if (parts.length > 1) {
            newValue = parts[0] + separator + num;
            if(parts[1].substr(1,parts[1].length).length>1){
                newValue = parts[0] + separator +num + parts[1].substr(1,parts[1].length);
            }
        } else {
            newValue = rawValue;
        }
        node.setAttribute(attrName, newValue);
    }
}

function replaceButtonAttributeSuffixNum(node, attrName, num, separator) {
    if (node.hasAttribute(attrName)) {
        var rawValue = node.getAttribute(attrName);
        var value = rawValue.substr(rawValue.indexOf("(")+1,rawValue.indexOf(")"));
        var parts = value.split(separator);
        var newValue;
        if (parts.length > 1) {
            newValue = rawValue.substr(0,rawValue.indexOf("(")+1)+parts[0] + separator + num + +new Date()+"\')";
        } else {
            newValue = rawValue;
        }
        node.setAttribute(attrName, newValue);
    }
}

/* append space separated input value */
function appendSSInputVal(input, value) {
    appendInputVal(input, value, " ");
}