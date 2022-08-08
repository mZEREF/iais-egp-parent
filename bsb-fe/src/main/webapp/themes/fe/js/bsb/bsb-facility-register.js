$(function () {
    $("#submit").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        $("#mainForm").submit();

        /* Follow code will use JS check and popup a modal */
        // if ($("#regulationDeclare").is(':checked') && $("#accuracyDeclare").is(':checked')) {
        //     showWaiting();
        //     $("input[name='action_type']").val("jump");
        //     $("input[name='action_value']").val("next");
        //     $("#mainForm").submit();
        // } else {
        //     $("#submitDeclareModal").modal('show');
        // }
    });


    // facility classification radio button changes
    var activityTypeP = $("#activityTypeP");
    var bsl3ActivityTypesDiv = $("#bsl3Types");
    var bsl4ActivityTypesDiv = $("#bsl4Types");
    var ufActivityTypesDiv = $("#ufTypes");
    var lspfActivityTypesDiv = $("#lspfTypes");
    var rfActivityTypesDiv = $("#rfTypes");
    var facActivityMandatory = $("#facActivityMandatory");
    $("#bsl3Radio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='ACTVITY004']").attr("checked", true);
        bsl3ActivityTypesDiv.show();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.hide();
        facActivityMandatory.hide();
    });
    $("#bsl4Radio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='ACTVITY004']").attr("checked", true);
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.show();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.hide();
        facActivityMandatory.hide();
    });
    $("#ufRadio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.show();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.hide();
        facActivityMandatory.show();
    });
    $("#lspfRadio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.show();
        rfActivityTypesDiv.hide();
        facActivityMandatory.show();
    });
    $("#rfRadio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.show();
        facActivityMandatory.show();
    });

    $("select[data-custom-ind=facType]").change(function () {
        var id = $(this).attr("id");
        var index = id.substring('facType'.length);
        if (this.value === 'FACTYPE005') {
            $("#facTypeDetailsFormGroup" + index).show();
        } else {
            $("#facTypeDetailsFormGroup" + index).hide();
        }
    });

    $("input[data-custom-ind=isSameAddress]").change(function () {
        var id = $(this).attr("id");
        var name = $(this).attr("name");
        var index = name.substring('isSameAddress'.length);
        if (id.startsWith('isSameAddress')) {
            $("#isSameAddrSection" + index).show();
            $("#isSameAddrSectionY" + index).show();
            $("#isSameAddrSectionN" + index).hide();
        } else if (id.startsWith('notSameAddress')) {
            $("#isSameAddrSection" + index).show();
            $("#isSameAddrSectionY" + index).hide();
            $("#isSameAddrSectionN" + index).show();
        }
    });

    $("input[data-custom-ind=gazetted]").change(function () {
        var id = $(this).attr("id");
        var name = $(this).attr("name");
        var index = name.substring('protectedPlace'.length);
        if (id.startsWith('isAProtectedPlace')) {
            $("#docUploadDiv" + index).show();
        } else if (id.startsWith('notAProtectedPlace')) {
            $("#docUploadDiv" + index).hide();
            $("#notGazetteModal").modal('show');
        }
    });

    $("select[data-custom-ind=addressType]").change(function () {
        var id = $(this).attr("id");
        var index = id.substring('addressType'.length);
        var addressType = $(this).val();
        if(addressType === 'ADDTY001') {
            $("#aptMandatoryBlk" + index).show();
            $("#aptMandatoryFloor" + index).show();
            $("#aptMandatoryUnit" + index).show();
            $("#aptMandatoryStreet" + index).hide();
        } else {
            $("#aptMandatoryBlk" + index).hide();
            $("#aptMandatoryFloor" + index).hide();
            $("#aptMandatoryUnit" + index).hide();
            $("#aptMandatoryStreet" + index).show();
        }
    });

    $("#addressType").change(function () {
        var addressType = $(this).val();
        if(addressType === 'ADDTY001') {
            $("#aptMandatoryBlk").show();
            $("#aptMandatoryFloor").show();
            $("#aptMandatoryUnit").show();
            $("#aptMandatoryStreet").hide();
        } else {
            $("#aptMandatoryBlk").hide();
            $("#aptMandatoryFloor").hide();
            $("#aptMandatoryUnit").hide();
            $("#aptMandatoryStreet").show();
        }
    });

    $("input[data-custom-ind=opvSabin]").change(function () {
        var id = $(this).attr("id");
        var name = $(this).attr("name");
        var parts = name.split(/--v--/);
        var prefix = parts[0];
        var index = parts[1];
        if (id.startsWith(prefix + 'Y')) {
            $("#" + prefix + 'SubSection--v--' + index).show();
        } else if (id.startsWith(prefix + 'N')) {
            $("#" + prefix + 'SubSection--v--' + index).hide();
        }
    });

    $("input[name=appointed]").change(function (){
        var id = $(this).attr("id");
        if(id === 'hasAppointedCertifier'){
            $("#appointedCertifierSection").show();
        } else if(id === 'notAppointedCertifier'){
            $("#appointedCertifierSection").hide();
        }
    });

    $("input[data-custom-ind=committeePersonnelIsEmployee]").change(function () {
        var id = $(this).attr("id");
        var idx;
        if (id.startsWith('isAnEmployee')) {
            idx = id.substring('isAnEmployee'.length, id.length);
            $("#committeeExternalCompNameDiv" + idx).hide();
        } else if (id.startsWith('notAnEmployee')) {
            idx = id.substring('notAnEmployee'.length, id.length);
            $("#committeeExternalCompNameDiv" + idx).show();
        }
    });


    $("#addNewProfileSection").click(function () {
        var meta = readSectionRepeatMetaData();
        var idxInput = $("input[name=" + meta.idxInputName +"]");
        var curIdxes = idxInput.val();
        var idxArr = curIdxes.trim().split(/ +/);

        var currentAmt = idxArr.length;
        var nextIdx = parseInt(idxArr[currentAmt - 1]) + 1;

        var section0 = $("#" + meta.sectionIdPrefix + meta.separator + "0");
        var newSectionDivJqObj = section0.clone(true);
        var newSectionDiv = newSectionDivJqObj[0];
        if (currentAmt === 1) {
            changeFirstSectionHeader(meta.sectionIdPrefix, 0, meta.headerTitlePrefix, meta.separator, true);
        }
        modifyClonedNode(newSectionDiv, nextIdx, meta.separator);

        var newHeaderDiv = newSectionHeader(currentAmt + 1, nextIdx, meta.headerTitlePrefix);
        newSectionDiv.replaceChild(newHeaderDiv, newSectionDiv.children[0]);

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
        resetNotDisabledRadio(newSectionDivJqObj);
        resetNotDisabledCheckbox(newSectionDivJqObj);
        /* Reset date picker */
        setupAllDatePickers(newSectionDivJqObj);
        /* Reset tool tip */
        setupAllToolTip(newSectionDivJqObj);

        /* hide unfolded sections */
        $("#facTypeDetailsFormGroup" + meta.separator + nextIdx).hide();
        $("#isSameAddrSection" + meta.separator + nextIdx).hide();
        $("#docUploadDiv" + meta.separator + nextIdx).hide();
        // hide mandatory asterisk
        $("#aptMandatoryBlk" + meta.separator + nextIdx).hide();
        $("#aptMandatoryFloor" + meta.separator + nextIdx).hide();
        $("#aptMandatoryUnit" + meta.separator + nextIdx).hide();
        $("#aptMandatoryStreet" + meta.separator + nextIdx).hide();
        // hide inventory info subsection
        newSectionDivJqObj.find("div[data-custom-ind=opvSabinSubSection]").hide();
    });


    $("#addNewOfficerSection").click(function () {
        var meta = readSectionRepeatMetaData();

        var idxInput = $("input[name=" + meta.idxInputName +"]");
        var curIdxes = idxInput.val().trim();
        var firstOfficer = !curIdxes;

        var nextIdx;
        if (firstOfficer) {
            nextIdx = 0;
        } else {
            var idxArr = curIdxes.split(/ +/);
            var currentAmt = idxArr.length;
            nextIdx = parseInt(idxArr[currentAmt - 1]) + 1;
        }

        var section0 = $("#" + meta.sectionIdPrefix + meta.separator + "0");
        if (firstOfficer) {
            $("#fakeOfficerSection").hide();
            modifyClonedNode(section0[0], 0, meta.separator);
            resetNiceSelect(section0);
            section0.show();
            appendSSInputVal(idxInput[0], nextIdx);
        } else {
            var newSectionDivJqObj = section0.clone(true);
            var newSectionDiv = newSectionDivJqObj[0];

            modifyClonedNode(newSectionDiv, nextIdx, meta.separator);

            var sectionGroupDiv = document.getElementById(meta.sectionGroupId);
            sectionGroupDiv.appendChild(newSectionDiv);
            appendSSInputVal(idxInput[0], nextIdx);


            /* Reset select to first option */
            newSectionDivJqObj.find("div.nice-select").each(function (index) {
                // This unique class is intended for auto test framework.
                // ('unq' is just a random string used to distinguish it from already exists name)
                $(this).attr("class", "nice-select " + meta.sectionIdPrefix + meta.separator + nextIdx + "unq" + index);
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

            /* Set current idx */
            setupCurrentIndexInd(newSectionDivJqObj, nextIdx, "data-current-idx")

            newSectionDivJqObj.show();
        }
    });


    $(".removeBtn").click(function () {
        var idx = $(this).attr("data-current-idx");
        var meta = readSectionRepeatMetaData();
        if (meta) {
            var idxInput = $("input[name=" + meta.idxInputName +"]");
            var curIdxes = idxInput.val();
            var idxArr = curIdxes.trim().split(/ +/);
            if (idx === '0') {
                $("#" + meta.sectionIdPrefix + meta.separator + idx).hide();
            } else {
                deleteSection(meta.sectionIdPrefix, meta.separator, idx);
            }
            idxArr = removeIdx(idxArr, idx);
            // set the input after the deletion of DOM to make sure the consistent between view and value.
            idxInput.val(idxArr.join(" "));

            if (meta.deletedIdxInputName) {
                var deletedIdxInput = document.getElementById(meta.deletedIdxInputName);
                appendSSInputVal(deletedIdxInput, idx);
            }

            if (meta.sectionIdPrefix === 'profileInfoSection') {
                if (idxArr.length === 1) {
                    // remove the sequence number at all
                    changeFirstSectionHeader(meta.sectionIdPrefix, 0, meta.headerTitlePrefix, meta.separator, false);
                } else {
                    // re-calculate sequence number
                    refreshH3(meta.sectionGroupId, meta.headerTitlePrefix);
                }
            } else if (meta.sectionIdPrefix === 'officerSection') {
                if (idxArr.length === 0) {
                    $("#fakeOfficerSection").show();
                }
            }
        }
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


    $("a[data-custom-ind=retrieveAddressBtn]").click(function () {
        var id = $(this).attr("id");
        var index = id.substring('retrieveAddressBtn'.length);
        var postalCode = $("#postalCodeN" + index).val();
        if (!postalCode) {
            return false;
        }
        $.ajax({
            type:"GET",
            url:"/bsb-web/address-info/" + postalCode,
            dataType: 'json',
            error:function(){
                $("#invalidPostalCodeModal").modal('show');
            },
            success:function(data) {
                $("#addressType" + index).val(data.address_type);
                $("#blockN" + index).val(data.block_no);
                $("#floorN" + index).val(data.floor);
                $("#unitNoN" + index).val(data.unit_no);
                $("#streetNameN" + index).val(data.street);
                $("#buildingNameN" + index).val(data.building);
            }
        });
    });
});

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

/* num is the amount */
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


function printFacilityRegistration(id) {
    window.open("/bsb-web/eservice/INTERNET/MohBsbFacilityRegistration/1/Print?printId=" + id);
}