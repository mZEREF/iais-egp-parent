function showLeader(obj){
    $(obj).parent().parent().parent().next().show();
}

function hideLeader(obj){
    $(obj).parent().parent().parent().next().hide();
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

function addSection(idxInputName, sectionIdPrefix, headerTitlePrefix, sectionGroupId, separator) {
    var idxInput = $("input[name=" + idxInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);

    var currentAmt = idxArr.length;
    var nextIdx = parseInt(idxArr[currentAmt - 1]) + 1;

    var section0 = $("#" + sectionIdPrefix + separator + "0");
    if (currentAmt === 1) {
        if (sectionIdPrefix === 'cerTeamSection') {
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

    var comm = $("#common"+separator+nextIdx);
    if(comm.val() === 'common'){
        $(comm).parent().parent().parent().next().show();
    }else{
        $(comm).parent().parent().parent().next().hide();
    }

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
        if (sectionIdPrefix === 'cerTeamSection') {
            var authSection0 = document.getElementById(sectionIdPrefix + separator + "0");
            authSection0.removeChild(authSection0.children[0]);
        } else {
            changeFirstSectionHeader(sectionIdPrefix, 0, titlePrefix, separator, false);
        }
    } else {
        refreshH3(sectionGroupId, titlePrefix);
    }
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


function genFileInfo(fileInputEl) {
    var f = fileInputEl.files;
    return f[0].name + '(' + (f[0].size/1024).toFixed(1) + 'KB)';
}

/* add or remove num in title for the first section */
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

//---------- START kinda common func ----------
function updateInputAmt(hiddenInputName, changeAmt) {
    var amtHiddenInput = $("input[name=" + hiddenInputName + "]");
    var currentAmt = parseInt(amtHiddenInput.val());
    var nextAmt = currentAmt + changeAmt;
    amtHiddenInput.val(nextAmt);
    return nextAmt;
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
    } else if (node.nodeName === 'TEXTAREA') {
        replaceNodeAttributeSuffixNum(node, 'id', idx, separator);
        replaceNodeAttributeSuffixNum(node, 'name', idx, separator);
        node.value = "";
    }

}