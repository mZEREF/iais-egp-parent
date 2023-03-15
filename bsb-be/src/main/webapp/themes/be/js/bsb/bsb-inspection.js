$(function () {
    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val('back');
        $("#mainForm").submit();
    });

    $("#clear").click(function () {
        showWaiting();
        $("input[name='action_type']").val("clear");
        $("#mainForm").submit();
    });

    $("#save").click(function () {
        showWaiting();
        $("input[name='action_type']").val("save");
        $("#mainForm").submit();
    });

    $("#adhocBth").click(function () {
        showWaiting();
        $("input[name='action_type']").val('adhoc');
        $("#mainForm").submit();
    });

    $("#viewChecklist").click(function () {
        showWaiting();
        $("input[name='action_type']").val('viewChecklist');
        $("#mainForm").submit();
    });

    $("#addFinding").click(function () {
        var meta = readSectionRepeatMetaData();
        addInsFinding(meta.idxInputName, meta.sectionIdPrefix, meta.sectionGroupId, meta.separator, meta.itemSelection);
    });

    $(".removeFindingBtn").click(removeBtnEventHandler);

    $("#submitFindingBtn").click(function () {
        showWaiting();
        $("input[name='action_type']").val('insFinding');
        $("#mainForm").submit();
    });

    $("#submitOutcomeBtn").click(function () {
        showWaiting();
        $("input[name='action_type']").val('insOutcome');
        $("#mainForm").submit();
    });

    $("#submitBtn").click(function () {
        showWaiting();
        $("input[name='action_type']").val('submit');
        $("#mainForm").submit();
    });

    $("#submitButton").click(function () {
        showWaiting();
        $("input[name='action_type']").val('submit');
        $("#mainForm").submit();
    });

    $("#submitRfiBtn").click(function () {
        var processingDecision = $("#processingDecision").val();
        if (processingDecision === "MOHPRO002") {
            validationRfiAppSelect();
        } else {
            showWaiting();
            $("input[name='action_type']").val('submit');
            $("#mainForm").submit();
        }
    })

    $("#processingDecision").change(function () {
        var selectValue = this.value;
        var appStatus = this.getAttribute("data-app-status");
        var show = false;
        if (selectValue === 'MOHPRO014') {  // decision is rout to AO for review
            if (appStatus === 'BSBAPST300' ||  // DO submit inspection report
                appStatus === 'BSBAPST302' ||  // DO submit inspection report revision
                appStatus === 'BSBAPST312') {  // DO NC Email draft
                show = true;
            }
        } else if((selectValue === 'MOHPRO007' || selectValue === 'MOHPRO003') &&    // decision is approve or reject
            (appStatus === 'BSBAPST314' ||  // DO NC rectification
             appStatus === 'BSBAPST316' ||  // DO NC clarification
             appStatus === 'BSBAPST322'  // DO Follow-up verification
            )) {
            show = true;
        } else if (appStatus === 'BSBAPST303' && selectValue === 'MOHPRO018') {  // DO approve inspection report
            show = true;
        } else if (appStatus === 'BSBAPST304' && selectValue === 'MOHPRO019') {  // AO approve inspection report
            show = true;
        } else if (appStatus === 'BSBAPST033' && selectValue === 'MOHPRO009') {  // draft approval letter
            show = true;
        }
        if (show) {
            $("#selectMohUserDiv").show();
        } else {
            $("#selectMohUserDiv").hide();
        }
    });

    $("#processingExtensionDecision").change(function () {
        var selectValue = this.value;
        if (selectValue === 'MOHPRO007') {
            $("#newDueDateDiv").show();
        } else {
            $("#newDueDateDiv").hide();
        }
    });

    $("#previewBtn").click(function (){
        showWaiting();
        $("input[name='action_type']").val('preview');
        $("#mainForm").submit();
    });

    $("#uploadChecklist").click(function (){
        showWaiting();
        $("input[name='action_type']").val('upload');
        $("#mainForm").submit();
    });

    $("#downloadChecklist").click(function () {
        showPopupWindow(BASE_CONTEXT_PATH + '/inspection/checklist/exporting-data?stamp=" + new Date().getTime()');
    });

    $("#listAdhoc").click(function () {
        showWaiting();
        $("input[name='action_type']").val('listAdhoc');
        $("#mainForm").submit();
    });

    $("#editAdhocBtn").click(function () {
        showWaiting();
        $("input[name='crud_action_type']").val('addAdhoc');
        $("#mainForm").submit();
    });

    $("#backAdhocBtn").click(function () {
        showWaiting();
        $("input[name='crud_action_type']").val('saveAdhoc');
        $("#mainForm").submit();
    });

    var afterDraft = $("input[name='afterSaveDraft']").val();
    if (afterDraft === 'true') {
        $("#afterSaveDraftMd").modal('show');
    }

    $("#applicationSelect").click(function () {
        if($(this).is(':checked')) {
            viewRfiApplication();
        }
    })
})

// inspection findings' sections

function readSectionRepeatMetaData() {
    return {
        idxInputName: $("#section_repeat_section_idx_name").val(),
        sectionIdPrefix: $("#section_repeat_section_id_prefix").val(),
        sectionGroupId: $("#section_repeat_section_group_id").val(),
        separator: $("#section_repeat_separator").val(),
        itemSelection: $("#chkl_item_selection").val()
    };
}


/* idx & amt are number */
function genInsFinding(amt, idx, sectionIdPrefix, separator, itemSelectionString) {
    var fullItemSectionString = itemSelectionString.replace('{REPLACE}', separator + idx);
    return '<tr id="' + sectionIdPrefix + separator + idx + '">' +
                '<td style="border: 0; padding-top: 5px; padding-bottom: 0">' + amt + '</td>' +
                '<td style="border: 0; padding-top: 5px; padding-bottom: 0">' + fullItemSectionString + '</td>' +
                '<td style="border: 0; padding-top: 5px; padding-bottom: 0">' +
                    '<select name="findingType' + separator + idx + '" style="display: none" aria-label="finding type">' +
                        '<option value="Non-compliance" selected="selected">Non-compliance</option>' +
                        '<option value="Follow-up">Follow-up item</option>' +
                    '</select>' +
                    '<div class="nice-select" tabindex="0">' +
                        '<span class="current">Non-compliance</span>' +
                        '<ul class="list">' +
                            '<li class="option selected" data-value="Non-compliance">Non-compliance</li>' +
                            '<li class="option" data-value="Follow-up">Follow-up item</li>' +
                        '</ul>' +
                    '</div>' +
                '</td>' +
                '<td style="border: 0; padding-top: 5px; padding-bottom: 0"><textarea name="findingRemark' + separator + idx + '" cols="40" rows="1" maxlength="300" aria-label="MoH remarks"></textarea></td>' +
                '<td style="border: 0; padding-top: 5px; padding-bottom: 0"><input type="text" style="margin-bottom: 0" autocomplete="off" name="deadline' + separator + idx + '" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" aria-label="deadline"/></td>' +
                '<td style="border: 0; padding-top: 5px; padding-bottom: 0"><em data-current-idx="' + idx + '" class="fa fa-times-circle del-size-36 cursorPointer removeFindingBtn"></em></td>' +
            '</tr>';
}


function addInsFinding(idxInputName, sectionIdPrefix, sectionGroupId, separator, itemSelectionString) {
    var idxInput = $("input[name=" + idxInputName +"]");
    var curIdxes = idxInput.val().trim();

    var nextAmt;
    var nextIdx;

    if (curIdxes === '') {
        nextAmt = 1;
        nextIdx = 0;
    } else {
        var idxArr = curIdxes.split(/ +/);
        var currentAmt = idxArr.length;
        nextAmt = currentAmt + 1;
        nextIdx = parseInt(idxArr[currentAmt - 1]) + 1;
    }

    var sectionGroup = $("#" + sectionGroupId);
    sectionGroup.append(genInsFinding(nextAmt, nextIdx, sectionIdPrefix, separator, itemSelectionString));
    appendSSInputVal(idxInput[0], nextIdx);

    var newSectionObj = sectionGroup.children("tr:last");
    /* register remove button event */
    newSectionObj.find(".removeFindingBtn").click(removeBtnEventHandler);
    /* Set date picker */
    newSectionObj.find(".date_picker").each(function () {
        $(this).datepicker({
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
        removeInsFinding(idx, meta.idxInputName, meta.sectionIdPrefix, meta.sectionGroupId, meta.separator);
    }
}

function removeInsFinding(idx, idxInputName, sectionIdPrefix, sectionGroupId, separator) {
    var idxInput = $("input[name=" + idxInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);

    deleteSection(sectionIdPrefix, separator, idx);
    idxArr = removeIdx(idxArr, idx);
    idxInput.val(idxArr.join(" "));

    refreshNumber(sectionGroupId);
}

function refreshNumber(sectionGroupId) {
    var num = 0;
    $("#" + sectionGroupId + " > tr > td:first-child").each(function () {
        $(this).text(++num);
    });
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

function deleteSection(sectionIdPrefix, separator, idx) {
    var section = document.getElementById(sectionIdPrefix + separator + idx);
    section.parentNode.removeChild(section);
}

//checkBox - inspection NCs may delete in future
function doInspectorProRecCheckAll(){
    if ($('#allNcItemCheck').is(':checked')) {
        $("input[name = 'ncItemCheck']").attr("checked","true");
    } else {
        $("input[name = 'ncItemCheck']").removeAttr("checked");
    }
}

function doInspectorProRecCheck(){
    var flag = true;
    var allNcItemCheck = document.getElementById("allNcItemCheck");
    var ncItemCheckList = document.getElementsByName("ncItemCheck");
    for (var x = 0; x < ncItemCheckList.length; x++) {
        if(ncItemCheckList[x].checked==false){
            flag = false;
            break;
        }
    }
    allNcItemCheck.checked = flag;
}

/* file upload start */
function initUploadFileData() {
    $('#_needReUpload').val(0);
    $('#_fileType').val("BSB");
    $('#_singleUpload').val("1");
}

function handleOnClickingUploadBtn() {
    $('.itemErrorTableDiv').hide();
}
/* file upload end */

function cancelSaveDraftModule() {
    $('#afterSaveDraftMd').modal('hide');
}

function validationRfiAppSelect() {
    var rfiApp = $("#applicationSelect").is(":checked");
    var rfiPre = $("#preInspectionChecklistSelect").is(":checked");
    if (!rfiApp && !rfiPre) {
        $("#rfiErrorMsg").show();
    } else {
        showWaiting();
        $("input[name='action_type']").val('submit');
        $("#mainForm").submit();
    }
}