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

    $("a[data-step-key]").click(jumpToStep);
    $("li.tracker-item[data-step-key]").click(jumpToStep);

    $("#submit").click(function () {
        if ($("#declare").is(':checked')) {
            showWaiting();
            $("input[name='action_type']").val("jump");
            $("input[name='action_value']").val("next");
            $("#mainForm").submit();
        } else {
            $("#submitDeclareModal").modal('show');
        }
    });

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
        $("input[value='ACTVITY001']").attr("checked", true);
        bsl3ActivityTypesDiv.show();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.hide();
    });
    $("#bsl4Radio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='ACTVITY001']").attr("checked", true);
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.show();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.hide();
    });
    $("#ufRadio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='ACTVITY002']").attr("checked", true);
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.show();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.hide();
    });
    $("#lspfRadio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='ACTVITY005']").attr("checked", true);
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.show();
        rfActivityTypesDiv.hide();
    });
    $("#rfRadio").change(function () {
        activityTypeP.show();
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='ACTVITY008']").attr("checked", true);
        bsl3ActivityTypesDiv.hide();
        bsl4ActivityTypesDiv.hide();
        ufActivityTypesDiv.hide();
        lspfActivityTypesDiv.hide();
        rfActivityTypesDiv.show();
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

    $("input[data-custom-ind=batOthersSampleType]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('sampleOthers'.length, id.length);
        if ($(this).is(":checked")) {
            $("#batOtherSampleTypeDiv" + idx).show();
        } else {
            $("#batOtherSampleTypeDiv" + idx).hide();
        }
    });



    $("#addNewSection").click(function () {
        var meta = readSectionRepeatMetaData();
        addSection(meta.idxInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    });


    $(".removeBtn").click(removeBtnEventHandler);


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

        /* Reset all checkbox to unchecked */
        newSectionDivJqObj.find(":checkbox:checked").prop("checked", false);
        $("#batOtherSampleTypeDiv" + meta.separator +  nextIdx).hide();
    });
});