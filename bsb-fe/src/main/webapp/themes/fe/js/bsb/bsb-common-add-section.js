/* The function to add a new section need to be implemented by each module.
 * You may not use functions here, you can create your own method and register it.
 */



/* Read meta info used by 'add section' function. */
function readSectionRepeatMetaData() {
    return {
        idxInputName: $("#section_repeat_section_idx_name").val(),
        sectionIdPrefix: $("#section_repeat_section_id_prefix").val(),
        headerTitlePrefix: $("#section_repeat_header_title_prefix").val(),
        sectionGroupId: $("#section_repeat_section_group_id").val(),
        separator: $("#section_repeat_separator").val()
    };
}


/* Create a close button div, it contains an index which can be used to delete the created section */
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


/* A deletion action is performed on an element with the 'data-current-idx' attribute, which indicate the
 * index, so we can delete the correct section.
 * The method call #removeSection to do the actual work. */
function removeBtnEventHandler() {
    var idx = $(this).attr("data-current-idx");
    var meta = readSectionRepeatMetaData();
    if (meta) {
        removeSection(idx, meta.idxInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    }
}

/* To clearly remove a section, we need:
 * 1, remove existing index in the input.
 * 2, delete section div in DOM.
 * 3, rectify the header sequence number */
function removeSection(idx, idxInputName, sectionIdPrefix, titlePrefix, sectionGroupId, separator) {
    var idxInput = $("input[name=" + idxInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);

    var nextAmt = idxArr.length - 1;
    idxArr = removeIdx(idxArr, idx);
    deleteSection(sectionIdPrefix, separator, idx);
    // set the input after the deletion of DOM to make sure the consistent between view and value.
    idxInput.val(idxArr.join(" "));

    if (nextAmt === 1) {
        // remove the sequence number at all
        changeFirstSectionHeader(sectionIdPrefix, 0, titlePrefix, separator, false);
    } else {
        // re-calculate sequence number
        refreshH3(sectionGroupId, titlePrefix);
    }
}


/* Add or remove num in title for the first section.
 * If the parameter 'add' is true, the header will be 'prefix+1', else will be 'prefix' itself */
function changeFirstSectionHeader(sectionIdPrefix, idx, titlePrefix, separator, add) {
    var title = add ? titlePrefix + 1 : titlePrefix;
    $("#" + sectionIdPrefix + separator + idx + " > div > h3").text(title);
}

/* Re-calculate header number. This method is used when user want to remove a section. */
function refreshH3(sectionGroupId, titlePrefix) {
    var num = 0;
    $("#" + sectionGroupId + " > section > div:first-child > h3").each(function () {
        $(this).text(titlePrefix + " " + ++num);
    });
}


//------------------------------------------------------------------------------------------------------

/* Set up all date picker to new created elements to make them associated to new cloned input.
 * This method relies on date picker js.
 * 'parentEl' is a jQuery object */
function setupAllDatePickers(parentEl) {
    parentEl.find(".date_picker").each(function () {
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

/* Create a new date picker DOM-object from an existing one.
 * When user add a new section, the cloned date picker is associated to the old input element,
 * we use this method to create a same but new element to replace the cloned one.
 * 'oldEl' is a JQuery Object,
 * return type is a DOM-object. */
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


/* Set up all tool tip to new created elements to make them associated to new cloned <a> tag.
 * This method relies on tool tip js.
 * 'parentEl' is a jQuery object */
function setupAllToolTip(parentEl) {
    parentEl.find("a[data-toggle='tooltip']").each(function () {
        var oldEL = $(this);
        var newEl = newToolTip(oldEL);
        oldEL.replaceWith(newEl);
        newEl.tooltip();
    });
}

/* Create a new tool tip DOM-object from an existing one.
 * When user add a new section, the cloned tool tip is associated to the old icon,
 * we use this method to create a same but new element to replace the cloned one.
 * 'oldEl' is a JQuery Object,
 * return type is a DOM-object. */
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


/* remove a section in DOM according to sectionIdPrefix, separator and index */
function deleteSection(sectionIdPrefix, separator, idx) {
    var section = document.getElementById(sectionIdPrefix + separator + idx);
    section.parentNode.removeChild(section);
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


/* The attribute is usually the name of the input. When the page can add section, the name will be
 * 'name+separator+index'. If we clone a section, then we need to replace index to new value.
 * called by #modifyClonedNode */
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

/* When we clone a section, we need to clear existing values.
 * Replace old index to new. */
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