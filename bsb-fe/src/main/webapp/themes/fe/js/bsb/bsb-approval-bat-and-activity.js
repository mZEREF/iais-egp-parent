$(function () {
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

    $("#saveDraft").click(function () {
        showWaiting();
        $("input[name='action_type']").val("draft");
        $("#mainForm").submit();
    });

    $("a[data-step-key]").click(jumpToStep);
    $("li.tracker-item[data-step-key]").click(jumpToStep);

    $("#submit").click(function () {
        if ($("#declare1").is(':checked') && $("#declare2").is(':checked')) {
            showWaiting();
            $("input[name='action_type']").val("jump");
            $("input[name='action_value']").val("next");
            $("#mainForm").submit();
        } else {
            $("#submitDeclareModal").modal('show');
        }
    });

    $(".removeBtn").click(removeBtnEventHandler);


    $("#addNewSection").click(function () {
        var meta = readSectionRepeatMetaData();
        addSection(meta.idxInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    });

    $("#addNewBatSection").click(function () {
        var meta = readSectionRepeatMetaData();
        var idxInput = $("input[name=" + meta.idxInputName +"]");
        var curIdxes = idxInput.val();
        var idxArr = curIdxes.trim().split(/ +/);

        var currentAmt = idxArr.length;
        var nextIdx = parseInt(idxArr[currentAmt - 1]) + 1;

        var section0 = $("#" + meta.sectionIdPrefix + meta.separator + "0");
        var newSectionDivJqObj = section0.clone(true);
        var newSectionDiv = newSectionDivJqObj[0];
        modifyClonedNode(newSectionDiv, nextIdx, meta.separator);

        var newCloseDiv = newCloseButton(nextIdx);
        newSectionDiv.children[0].appendChild(newCloseDiv);

        var sectionGroupDiv = document.getElementById(meta.sectionGroupId);
        sectionGroupDiv.appendChild(newSectionDiv);
        appendSSInputVal(idxInput[0], nextIdx);

        /* Reset select to first option */
        newSectionDivJqObj.find("div.nice-select").each(function (index) {
            // This unique class is intended for auto test framework.
            // ('unq' is just a random string used to distinguish it from already exists name)
            $(this).attr("class", "nice-select " + meta.sectionIdPrefix + meta.separator + nextIdx + "unq" + index);
            var firstOp = $(this).find("ul.list > li:first");
            // we need to click twice to set the value
            firstOp.trigger('click'); firstOp.trigger('click');
        });

        /* Reset all radio button and checkbox to unchecked */
        resetRadio(newSectionDivJqObj);
        resetCheckbox(newSectionDivJqObj);

        /* Set date picker */
        setupAllDatePickers(newSectionDivJqObj);

        /* Set current idx */
        setupCurrentIndexInd(newSectionDivJqObj, nextIdx, "data-current-idx")

        $("#sampleWorkDetailDiv" + meta.separator +  nextIdx).hide();

        // set schedule-bat on change event
        newSectionDivJqObj.find("select[data-cascade-dropdown=schedule-bat]").each(function () {
            var id = $(this).attr("id");
            var batDropdownId = computeBatDropdownIdByScheduleDropdownId(id);
            $("#"+id).unbind("change");
            registerCascadeEvent(id, batDropdownId, scheduleBatDataJson, null, function () {
                $("#"+batDropdownId).niceSelect("update");
            });
        });
    });

    $("#retrieveAddressBtn").click(function retrieveAddress() {
        var postalCode = $("#postalCodeN").val();
        if (!postalCode) {
            return false;
        }
        $.ajax({
            type:"GET",
            url:"/bsb-fe/address-info/" + postalCode,
            dataType: 'json',
            error:function(){
                $("#invalidPostalCodeModal").modal('show');
            },
            success:function(data) {
                $("#addressType").val(data.address_type);
                $("#blockN").val(data.block_no);
                $("#floorN").val(data.floor);
                $("#unitNoN").val(data.unit_no);
                $("#streetNameN").val(data.street);
                $("#buildingNameN").val(data.building);
            }
        });
    });
});

function addSection(idxInputName, sectionIdPrefix, headerTitlePrefix, sectionGroupId, separator) {
    var idxInput = $("input[name=" + idxInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);

    var currentAmt = idxArr.length;
    var nextIdx = parseInt(idxArr[currentAmt - 1]) + 1;

    var section0 = $("#" + sectionIdPrefix + separator + "0");
    if (currentAmt > 1) {
        changeFirstSectionHeader(sectionIdPrefix, 0, headerTitlePrefix, true);
    }
    var newSectionDivJqObj = section0.clone(true);
    var newSectionDiv = newSectionDivJqObj[0];
    modifyClonedNode(newSectionDiv, nextIdx, separator);
    if(currentAmt === 2){
        $("#" + sectionIdPrefix + separator + nextIdx).find("div");
    }
    var newHeaderDiv = newSectionHeader(currentAmt + 1, nextIdx, headerTitlePrefix);
    newSectionDiv.replaceChild(newHeaderDiv, newSectionDiv.children[0]);


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
    resetRadio(newSectionDivJqObj);
    resetCheckbox(newSectionDivJqObj);

    /* Set date picker */
    setupAllDatePickers(newSectionDivJqObj);

    /* Reset tool tip */
    setupAllToolTip(newSectionDivJqObj);
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
function jumpToStep() {
    showWaiting();
    $("input[name='action_type']").val("jump");
    $("input[name='action_value']").val($(this).attr("data-step-key"));
    $("#mainForm").submit();
}

function cancelLoadDraftData() {
    $('#haveData').modal('hide');
    showWaiting();
    $("input[name='action_type']").val("jump");
    $("input[name='action_value']").val("next");
    $("input[name='action_load_draft']").val("N");
    $("#mainForm").submit();
}
function loadDraftData() {
    $('#haveData').modal('hide');
    showWaiting();
    $("input[name='action_type']").val("jump");
    $("input[name='action_value']").val("next");
    $("input[name='action_load_draft']").val("Y");
    $("#mainForm").submit();
}

$(function (){
    $("#continueB").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        $("#mainForm").submit();
    });

    if ($("#haveSuitableDraftData").val() === 'true') {
        $('#haveData').modal('show');
    }
});