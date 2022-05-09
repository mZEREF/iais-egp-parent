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
    $("#bsl3Radio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='ACTVITY004']").attr("checked", true);
        bsl3ActivityTypesDiv.show();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.hide();
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
    });
    $("#ufRadio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.show();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.hide();
    });
    $("#lspfRadio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.show();
        rfActivityTypesDiv.hide();
    });
    $("#rfRadio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.show();
    });


    $("input[name=protectedPlace]").change(function () {
        var id = $(this).attr("id");
        if (id === 'isAProtectedPlace') {
            $("#docUploadDiv").show();
        } else if (id === 'notAProtectedPlace') {
            $("#docUploadDiv").hide();
            $("#notGazetteModal").modal('show');
        }
    })

    $("input[name=isSameAddress ]").change(function (){
        var id = $(this).attr("id");
        if(id === 'isSameAddress'){
            $("#isSameAddrSection").show();
            $("#isSameAddrSectionY").show();
            $("#isSameAddrSectionN").hide();
        } else if(id === 'notSameAddress'){
            $("#isSameAddrSection").show();
            $("#isSameAddrSectionY").hide();
            $("#isSameAddrSectionN").show();
        }
    })

    $("#addressType").change(function () {
        var addressType = $(this).val();
        if(addressType === 'ADDTY001') {
            $("#aptMandatoryBlk").show();
            $("#aptMandatoryFloor").show();
            $("#aptMandatoryUnit").show();
        } else {
            $("#aptMandatoryBlk").hide();
            $("#aptMandatoryFloor").hide();
            $("#aptMandatoryUnit").hide();
        }
    });

    $("input[name=appointed]").change(function (){
        var id = $(this).attr("id");
        if(id === 'hasAppointedCertifier'){
            $("#appointedCertifierSection").show();
        } else if(id === 'notAppointedCertifier'){
            $("#appointedCertifierSection").hide();
        }
    })

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



    $("#addNewSection").click(function () {
        var meta = readSectionRepeatMetaData();
        addSection(meta.idxInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    });


    $(".removeBtn").click(function () {
        var idx = $(this).attr("data-current-idx");
        var meta = readSectionRepeatMetaData();
        if (meta) {
            var idxInput = $("input[name=" + meta.idxInputName +"]");
            var curIdxes = idxInput.val();
            var idxArr = curIdxes.trim().split(/ +/);

            idxArr = removeIdx(idxArr, idx);
            deleteSection(meta.sectionIdPrefix, meta.separator, idx);
            // set the input after the deletion of DOM to make sure the consistent between view and value.
            idxInput.val(idxArr.join(" "));
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