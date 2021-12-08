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

function deleteFile(id) {
    // delete input
    var inputEl = document.getElementById(id);
    inputEl.parentNode.removeChild(inputEl);

    // delete delete button, reload button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);
}

function deleteSavedFile(id) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the delete list
    var deleteSavedInput = document.getElementById("deleteExistFiles");
    appendCSInputVal(deleteSavedInput, id);
}

function deleteNewFile(id) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the delete list
    var deleteSavedInput = document.getElementById("deleteNewFiles");
    appendCSInputVal(deleteSavedInput, id);
}

function reloadSavedFile(id, type) {
    deleteSavedFile(id);
    $("a[data-upload-file=" + type + "]")[0].click();
}

function reloadNewFile(id, type) {
    deleteNewFile(id);
    $("a[data-upload-file=" + type + "]")[0].click();
}

function downloadFile(cond, id) {
    var url;
    if (cond === 'saved') {
        url = "/bsb-fe/ajax/doc/download/approvalApp/repo/" + id;
    } else if (cond === 'new') {
        url = "/bsb-fe/ajax/doc/download/approvalApp/new/" + id;
    }

    window.open(url);
}

function genFileInfo(fileInputEl) {
    var f = fileInputEl.files;
    return f[0].name + '(' + (f[0].size/1024).toFixed(1) + 'KB)';
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
        if (sectionIdPrefix === 'authSection' || sectionIdPrefix === 'committeeSection') {
            var authSection0 = document.getElementById(sectionIdPrefix + separator + "0");
            authSection0.removeChild(authSection0.children[0]);
        } else {
            changeFirstSectionHeader(sectionIdPrefix, 0, titlePrefix, separator, false);
        }
    } else {
        refreshH3(sectionGroupId, titlePrefix);
    }
}

/* add or remove num in title for the first section */
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

function newToolTip(oldEl) {
    var attributes = oldEl.prop("attributes");
    var newEl = document.createElement("a");
    var newElJq = $(newEl);
    $.each(attributes, function() {
        newElJq.attr(this.name, this.value);
    });
    newElJq.html(oldEl.html());
    return newElJq;
}

function changeFirstSectionHeader(sectionIdPrefix, idx, titlePrefix, separator, add) {
    var title = add ? titlePrefix + 1 : titlePrefix;
    $("#" + sectionIdPrefix + separator + idx + " > div > h3").text(title);
}

function refreshH3(sectionGroupId, titlePrefix) {
    var num = 0;
    $("#" + sectionGroupId + " > section > div:first-child > h3").each(function () {
        $(this).text(titlePrefix + " " + ++num);
    });
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

//---------- START kinda common func ----------
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

function deleteSection(sectionIdPrefix, separator, idx) {
    var section = document.getElementById(sectionIdPrefix + separator + idx);
    section.parentNode.removeChild(section);
}

function replaceNodeAttributeSuffixNum(node, attrName, idx, separator) {
    if (node.hasAttribute(attrName)) {
        var rawValue = node.getAttribute(attrName);
        var parts = rawValue.split(separator);
        var newValue;
        if (parts.length > 1) {
            newValue = parts[0] + separator + idx;
        } else {
            newValue = rawValue;
        }
        node.setAttribute(attrName, newValue);
    }
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
        if(node.type !== 'radio' && node.type !== 'checkbox'){
            node.value = "";
        }
    } else if (node.nodeName === 'DIV') {
        replaceNodeAttributeSuffixNum(node, 'id', idx, separator);
    } else if (node.nodeName === 'SELECT') {
        replaceNodeAttributeSuffixNum(node, 'id', idx, separator);
        replaceNodeAttributeSuffixNum(node, 'name', idx, separator);
    }
}