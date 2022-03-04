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

    $("a[data-step-key]").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val($(this).attr("data-step-key"));
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

    $("#saveDraft").click(function () {
        showWaiting();
        $("input[name='action_type']").val("draft");
        $("#mainForm").submit();
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
            var firstOp = $(this).find("ul.list > li:first-child");
            // we need to click twice to set the value
            firstOp.trigger('click'); firstOp.trigger('click');
        });

        /* Reset all radio button and checkbox to unchecked */
        newSectionDivJqObj.find(":radio:checked").prop("checked", false);
        newSectionDivJqObj.find(":checkbox:checked").prop("checked", false);

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

        /* Reset tool tip */
        newSectionDivJqObj.find("a[data-toggle='tooltip']").each(function () {
            var oldEL = $(this);
            var newEl = newToolTip(oldEL);
            oldEL.replaceWith(newEl);
            newEl.tooltip();
        });
    });
});