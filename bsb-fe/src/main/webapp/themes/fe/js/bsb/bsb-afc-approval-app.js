$(function () {
    readonlyPartPage();

    $("#edit").click(function (){
        unreadonlyPartPage();
    });

    $("#next").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        unreadonlyPartPage();
        $("#mainForm").submit();
    });

    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("back");
        unreadonlyPartPage();
        $("#mainForm").submit();
    });

    $("a[data-step-key]").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val($(this).attr("data-step-key"));
        unreadonlyPartPage();
        $("#mainForm").submit();
    });

    $("#facilityName").val($("#facilityId").find("option:checked").text());
    $("#activityType").val($("#activityId").find("option:checked").text());

    $("#facilityId").change(function (){
        $("#facilityName").val($("#facilityId").find("option:checked").text());
    })

    $("#activityId").change(function (){
        $("#activityType").val($("#activityId").find("option:checked").text());
    })

    // doc upload
    $("a[data-upload-file]").click(function () {
        // create input file
        var name = $(this).attr("data-upload-file");
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

    $("#addNewSection").click(function () {
        var meta = readSectionRepeatMetaData();
        addSection(meta.amtInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    });

    $(".removeBtn").click(removeBtnEventHandler);

    $("#addNewBatSection").click(function () {
        var meta = readSectionRepeatMetaData();
        var amtHiddenInput = $("input[name=" + meta.amtInputName +"]");
        var currentAmt = parseInt(amtHiddenInput.val());
        var nextAmt = currentAmt + 1;

        var section0 = $("#" + meta.sectionIdPrefix + meta.separator + "0");
        var newSectionDiv = section0.clone(true)[0];
        modifyClonedNode(newSectionDiv, nextAmt - 1, meta.separator);

        var newCloseDiv = newCloseButton(nextAmt);
        newSectionDiv.children[0].appendChild(newCloseDiv);

        var sectionGroupDiv = document.getElementById(meta.sectionGroupId);
        sectionGroupDiv.appendChild(newSectionDiv);
        amtHiddenInput.val(nextAmt);
    });
});

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

function readonlyPartPage() {
    $("input[type='text']").css("color", "#999");
    $("input[type='text']").css("border-color", "#ededed");
    $("input[type='text']").attr("disabled",true);
    $("input[type='number']").css("color", "#999");
    $("input[type='number']").css("border-color", "#ededed");
    $("input[type='number']").attr("disabled",true);
    $("input[type='radio']").attr("disabled",true);
    $("input[type='checkbox']").attr("disabled",true);
    $("div.nice-select").addClass("disabled");
    $(".btn.btn-secondary").attr("disabled",true);
    $(".btn.file-upload.btn-secondary").css("pointer-events","none");
    $("#addNewSection").attr("disabled",true).css("pointer-events","none");
    $("#addNewSection").css({opacity:0.5});
    $("#addNewBatSection").attr("disabled",true).css("pointer-events","none");
    $("#addNewBatSection").css({opacity:0.5});

}

function unreadonlyPartPage() {
    $("input[type='text']").css("color", "");
    $("input[type='text']").css("border-color", "");
    $("input[type='text']").removeAttr("disabled");
    $("input[type='number']").css("color", "");
    $("input[type='number']").css("border-color", "");
    $("input[type='number']").removeAttr("disabled");
    $("input[type='radio']").removeAttr("disabled");
    $("input[type='checkbox']").removeAttr("disabled");
    $("div.nice-select").removeClass("disabled");
    $(".btn.btn-secondary").removeAttr("disabled");
    $(".btn.file-upload.btn-secondary").css("pointer-events","auto");
    $("#addNewSection").attr("disabled",false).css("pointer-events","auto");
    $("#addNewSection").css({opacity:1});
    $("#addNewBatSection").attr("disabled",false).css("pointer-events","auto");
    $("#addNewBatSection").css({opacity:1});
}

function deleteFile(id) {
    // delete input
    var inputEl = document.getElementById(id);
    inputEl.parentNode.removeChild(inputEl);

    // delete delete button, reload button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);
}

function reloadFile(id) {
    // trigger click on the input file
    var inputEl = document.getElementById(id);
    inputEl.click();
}

function deleteSavedFile(id) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the delete list
    var deleteSavedInput = document.getElementById("deleteExistFiles");
    appendInputValue(deleteSavedInput, id);
}

function deleteNewFile(id) {
    // delete delete button, reload button and download button
    var fileDiv = document.getElementById(id + "FileDiv");
    fileDiv.parentNode.removeChild(fileDiv);

    // add id into the delete list
    var deleteSavedInput = document.getElementById("deleteNewFiles");
    appendInputValue(deleteSavedInput, id);
}

function reloadSavedFile(id, type) {
    deleteSavedFile(id);
    $("a[data-upload-file=" + type + "]")[0].click();
}

function reloadNewFile(id, type) {
    deleteNewFile(id);
    $("a[data-upload-file=" + type + "]")[0].click();
}

function downloadFile(cond, id, type, filename) {
    var url;
    if (cond === 'saved') {
        url = "/bsb-fe/ajax/doc/download/facReg/repo/" + id;
    } else if (cond === 'new') {
        url = "/bsb-fe/ajax/doc/download/facReg/new/" + id;
    }

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



function genFileInfo(fileInputEl) {
    var f = fileInputEl.files;
    return f[0].name + '(' + (f[0].size/1024).toFixed(1) + 'KB)';
}

function appendInputValue(input, value) {
    if (input.value) {
        input.value = input.value + "," + value;
    } else {
        input.value = value;
    }
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
        if (sectionIdPrefix === 'authSection' || sectionIdPrefix === 'committeeSection') {
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
        if (sectionIdPrefix === 'authSection' || sectionIdPrefix === 'committeeSection') {
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

//---------- END ----------