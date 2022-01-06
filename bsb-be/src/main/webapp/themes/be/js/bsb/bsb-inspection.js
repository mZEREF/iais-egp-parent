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

    $("#viewSelfAssessmt").click(function () {
        showWaiting();
        $("input[name='action_type']").val('viewSelfAssessment');
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
})


function viewApplication() {
    /* incomplete
     * This method is not implemented in other modules.
     * This function should be a common one.
     */
    window.open ("/bsb-be/eservice/INTRANET/MohBeAppViewDetails");
}



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
                        '<option value="NC" selected="selected">Non-compliance</option>' +
                        '<option value="followUp">Follow-up item</option>' +
                    '</select>' +
                    '<div class="nice-select" tabindex="0">' +
                        '<span class="current">Non-compliance</span>' +
                        '<ul class="list">' +
                            '<li class="option selected" data-value="NC">Non-compliance</li>' +
                            '<li class="option" data-value="followUp">Follow-up item</li>' +
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