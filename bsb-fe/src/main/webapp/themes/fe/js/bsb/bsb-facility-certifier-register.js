$(function () {

    // var element = $("textarea");
    // var temp =  element.text().replace(/\r?\n/g,"<br/>").replace(/\s/g,"&nbsp;");
    // element.html(temp);

    $("#next").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        $("#mainForm").submit();
    });

    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("back");
        $("#mainForm").submit();
    });

    $("a[data-step-key]").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val($(this).attr("data-step-key"));
        $("#mainForm").submit();
    });


    // // facility classification radio button changes
    var overseasVal = $("#overseasCon");
    $("#typeLocal").change(function () {
        overseasVal.hide();
    });
    $("#typeOverseas").change(function () {
        overseasVal.show();
    });


    $("#addNewSection").click(function () {
        var meta = readSectionRepeatMetaData();
        addSection(meta.amtInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    });


    $(".removeBtn").click(removeBtnEventHandler);

});

function showLeader(obj){
   $(obj).parent().parent().parent().next().show();
}

function hideLeader(obj){
    $(obj).parent().parent().parent().next().hide();
}

function readSectionRepeatMetaData() {
    return {
        amtInputName: $("#section_repeat_amt_input_name").val(),
        sectionIdPrefix: $("#section_repeat_section_id_prefix").val(),
        headerTitlePrefix: $("#section_repeat_header_title_prefix").val(),
        sectionGroupId: $("#section_repeat_section_group_id").val(),
        separator: $("#section_repeat_separator").val()
    };
}

function addSection(amtInputName, sectionIdPrefix, headerTitlePrefix, sectionGroupId, separator) {
    var amtHiddenInput = $("input[name=" + amtInputName +"]");
    var currentAmt = parseInt(amtHiddenInput.val());
    var nextAmt = currentAmt + 1;

    var section0 = $("#" + sectionIdPrefix + separator + "0");
    if (currentAmt === 1) {
        if (sectionIdPrefix === 'cerTeamSection') {
            var headerDiv = newSectionHeader(1, headerTitlePrefix);
            section0[0].insertBefore(headerDiv, section0[0].firstChild);
        } else {
            changeH3(sectionIdPrefix, 1, headerTitlePrefix);
        }
    }
    var newSectionDiv = section0.clone(true)[0];
    var newHeaderDiv = newSectionHeader(nextAmt, headerTitlePrefix);
    newSectionDiv.replaceChild(newHeaderDiv, newSectionDiv.firstChild);
    modifyClonedNode(newSectionDiv, nextAmt - 1, separator);

    var sectionGroupDiv = document.getElementById(sectionGroupId);
    sectionGroupDiv.appendChild(newSectionDiv);
    amtHiddenInput.val(nextAmt);
}

function removeBtnEventHandler() {
    var num = $(this).attr("data-current-idx");
    var meta = readSectionRepeatMetaData();
    if (meta) {
        removeSection(num, meta.sectionIdPrefix, meta.amtInputName, meta.headerTitlePrefix, meta.separator);
    }
}

function removeSection(num, sectionIdPrefix, hiddenInputName, titlePrefix, separator) {
    deleteSection(sectionIdPrefix, separator, num);
    var nextAmt = updateInputAmt(hiddenInputName, -1);
    if (nextAmt === 1) {
        if (sectionIdPrefix === 'cerTeamSection') {
            var authSection0 = document.getElementById(sectionIdPrefix + separator + "0");
            authSection0.removeChild(authSection0.children[0]);
        } else {
            changeH3(sectionIdPrefix, 0, titlePrefix, separator);
        }
    }
}

function changeH3(sectionIdPrefix, num, titlePrefix, separator) {
    var title = num > 0 ? titlePrefix + num : titlePrefix;
    $("#" + sectionIdPrefix + separator + num + " > div > h3").text(title);
}

function newCloseButton(num) {
    var emEl = document.createElement("em");
    emEl.setAttribute('data-current-idx', (num - 1).toString());
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

function newSectionHeader(num, titlePrefix) {
    var h3El = document.createElement("h3");
    h3El.className = 'col-xs-9 col-sm-10 col-md-11';
    h3El.style.cssText = 'border-bottom: 1px solid black';
    h3El.innerText = titlePrefix + num;
    var divEl = document.createElement("div");
    divEl.className = 'form-group';
    divEl.append(h3El);
    if (num !== 1) {
        var closeButtonDiv = newCloseButton(num);
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

function deleteSection(sectionIdPrefix, separator, num) {
    var section = document.getElementById(sectionIdPrefix + separator + num);
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

function modifyClonedNode(node, num, separator) {
    if (node.children) {
        for (var i = 0, length = node.children.length; i < length; i++) {
            modifyClonedNode(node.children[i], num, separator);
        }
    }

    if (node.nodeName === 'SECTION') {
        replaceNodeAttributeSuffixNum(node, 'id', num, separator);
    } else if (node.nodeName === 'LABEL') {
        replaceNodeAttributeSuffixNum(node, 'for', num, separator);
    } else if (node.nodeName === 'SPAN') {
        replaceNodeAttributeSuffixNum(node, 'data-err-ind', num, separator);
    } else if (node.nodeName === 'INPUT') {
        replaceNodeAttributeSuffixNum(node, 'id', num, separator);
        replaceNodeAttributeSuffixNum(node, 'name', num, separator);
        node.value = "";
    } else if (node.nodeName === 'DIV') {
        replaceNodeAttributeSuffixNum(node, 'id', num, separator);
    } else if (node.nodeName === 'SELECT') {
        replaceNodeAttributeSuffixNum(node, 'id', num, separator);
        replaceNodeAttributeSuffixNum(node, 'name', num, separator);
    }
}