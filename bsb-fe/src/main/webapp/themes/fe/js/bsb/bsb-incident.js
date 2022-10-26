$(function (){
    $("#safeRadio").change(function (){
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='INCTYPE001']").attr("checked", true);
        $("#safeTypes").show();
        $("#securTypes").hide();
        $("#securTypes :checked").prop("checked",false);
        $("#generTypes").hide();
        $("#generTypes :checked").prop("checked",false);
        $("#otherDel").hide();
    });

    $("#securRadio").change(function (){
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='INCTYPE002']").attr("checked", true);
        $("#securTypes").show();
        $("#safeTypes").hide();
        $("#safeTypes :checked").prop("checked",false);
        $("#generTypes").hide();
        $("#generTypes :checked").prop("checked",false);
        $("#otherDel").hide();
    });

    $("#generRadio").change(function (){
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='INCTYPE003']").attr("checked", true);
        $("#safeTypes").hide();
        $("#safeTypes :checked").prop("checked",false);
        $("#securTypes").hide();
        $("#securTypes :checked").prop("checked",false);
        $("#generTypes").show();
        $("#otherDel").hide();
    });

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

    $("#possibleY").change(function (){
        $("#releasePossibleY").show();
    });
    $("#possibleN").change(function (){
        $("#releasePossibleY").hide();
    });
    $("#involvedY").change(function (){
        $("#involved").show();
    });
    $("#involvedN").change(function (){
        $("#involved").hide();
    });

    $("#others").change(function (){
        if($("#others").prop('checked')){
            $("#otherDel").show();
        }else{
            $("#otherDel").hide();
        }
    });

    $("#others1").change(function (){
        if($("#others1").prop('checked')){
            $("#otherDel").show();
        }else{
            $("#otherDel").hide();
        }
    });

    $("#others2").change(function (){
        if($("#others2").prop('checked')){
            $("#otherDel").show();
        }else{
            $("#otherDel").hide();
        }
    });

    $("#policyCause").change(function (){
        if($("#policyCause").prop('checked')){
            $("#policyExp").show();
        } else{
            $("#policyExp").hide();
        }
    });

    $("#trainCause").change(function (){
        if($("#trainCause").prop('checked')){
            $("#trainExp").show();
        } else{
            $("#trainExp").hide();
        }
    });

    $("#equipCause").change(function (){
        if($("#equipCause").prop('checked')){
            $("#equipExp").show();
        } else{
            $("#equipExp").hide();
        }
    });

    $("#facilityCause").change(function (){
        if($("#facilityCause").prop('checked')){
            $("#facExp").show();
        } else{
            $("#facExp").hide();
        }
    });

    $("#personalCause").change(function (){
        if($("#personalCause").prop('checked')){
            $("#perExp").show();
        } else{
            $("#perExp").hide();
        }
    });

    $("#inadequateCause").change(function (){
        if($("#inadequateCause").prop('checked')){
            $("#inadequateExp").show();
        } else{
            $("#inadequateExp").hide();
        }
    });

    $("#environmentCause").change(function (){
        if($("#environmentCause").prop('checked')){
            $("#environmentExp").show();
        } else{
            $("#environmentExp").hide();
        }
    });

    $("#humanBeCause").change(function (){
        if($("#humanBeCause").prop('checked')){
            $("#humanBeExp").show();
        } else{
            $("#humanBeExp").hide();
        }
    });

    $("#performCause").change(function (){
        if($("#performCause").prop('checked')){
            $("#performExp").show();
        } else{
            $("#performExp").hide();
        }
    });

    $("#othersCause").change(function (){
        if($("#othersCause").prop('checked')){
            $("#otherSection").show();
            $("#otherExp").show();
        }else{
            $("#otherSection").hide();
            $("#otherExp").hide();
        }
    });

    $("#facName").change(function (){
        var facName = $("#facName").val();
        if(facName === ""){
            $("#activityType").html("<option value=\"\">Please select<\/option>");
        }else{
            $.post('/bsb-web/incident/activity.do',
                {facName: facName},
                function (data) {
                    var result = data.result;
                    if (result === 'success') {
                        var actResult = data.actResult;
                        var bioResult = data.bioResult;
                        var optionString = "";
                        var optionLi = "";
                        var optionString1 = "";
                        var optionLi1 = "";
                        var bat = $("#batName");
                        $.map(actResult,function(key,value){
                            optionString += "<option value=\"" + value + "\">" + key + "</option>";
                            optionLi += "<li data-value=\"" + value + "\" class=\"option\">" + key + "</li>";
                        })
                        for (var i = 0; i < bioResult.length; i++) {
                            optionString1 += "<option value=\"" + bioResult[i] + "\">" + bioResult[i] + "</option>";
                            optionLi1 += "<label class=\"multi-select-menuitem\" for=\"batName_" +(i+1) + " \" role=\"menuitem\" " +"> "+
                                "<input id=\"batName_"+ (i+1) +  "\" type = \"checkbox\" value=\""+bioResult[i]+"\""+"/>"
                                + bioResult[i]
                                + "</label>"
                        }
                        var activityType = $("#activityType");
                        activityType.html("<option value=\"\">Please select<\/option>" + optionString);
                        activityType.next().children("ul.list").html("<li data-value class=\"option selected focus\">Please Select<\/li>" + optionLi);
                        // bat.html("<option value=\"\">Please select<\/option>" + optionString1);
                        // bat.next().children(".multi-select-menu").children(".multi-select-menuitems").html("<label class=\"multi-select-menuitem\" for=\"batName_0\" role =\"menuitem\"> <input id=\"batName_0\" type=\"checkbox\" value=\"Please select\"> Please Select </label>"+optionLi1);
                    } else {

                    }
                }
            )
        }
    })

    $("a[data-step-key]").click(jumpToStep);

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

})

function readSectionRepeatMetaData() {
    return {
        idxInputName: $("#section_repeat_section_idx_name").val(),
        sectionIdPrefix: $("#section_repeat_section_id_prefix").val(),
        headerTitlePrefix: $("#section_repeat_header_title_prefix").val(),
        sectionGroupId: $("#section_repeat_section_group_id").val(),
        separator: $("#section_repeat_separator").val()
    };
}

function addSection(idxInputName, sectionIdPrefix, headerTitlePrefix, sectionGroupId, separator) {
    var idxInput = $("input[name=" + idxInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);

    var currentAmt = idxArr.length;
    var nextIdx = parseInt(idxArr[currentAmt - 1]) + 1;

    var section0 = $("#" + sectionIdPrefix + separator + "0");
    if (currentAmt === 1) {
        if (sectionIdPrefix === 'invPersonSection') {
            var headerDiv = newSectionHeader(1, 0, headerTitlePrefix);
            section0[0].insertBefore(headerDiv, section0[0].firstChild);
        } else {
            changeFirstSectionHeader(sectionIdPrefix, 0, headerTitlePrefix, true);
        }
    }
    var newSectionDivJqObj = section0.clone(true);
    var newSectionDiv = newSectionDivJqObj[0];
    var newHeaderDiv = newSectionHeader(currentAmt + 1, nextIdx, headerTitlePrefix);
    newSectionDiv.replaceChild(newHeaderDiv, newSectionDiv.children[0]);
    modifyClonedNode(newSectionDiv, nextIdx, separator);

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

    if (sectionIdPrefix === 'committeeSection') {
        $("#committeeExternalCompNameDiv" + separator + nextIdx).hide();
    }
}

function removeBtnEventHandler() {
    var idx = $(this).attr("data-current-idx");
    var meta = readSectionRepeatMetaData();
    if (meta) {
        removeSection(idx, meta.idxInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    }
}

function removeSection(idx, idxInputName, sectionIdPrefix, titlePrefix, sectionGroupId, separator) {
    var idxInput = $("input[name=" + idxInputName +"]");
    var curIdxes = idxInput.val();
    var idxArr = curIdxes.trim().split(/ +/);

    var nextAmt = idxArr.length - 1;
    idxArr = removeIdx(idxArr, idx);
    deleteSection(sectionIdPrefix, separator, idx);
    idxInput.val(idxArr.join(" "));

    if (nextAmt === 1) {
        if (sectionIdPrefix === 'invPersonSection') {
            var authSection0 = document.getElementById(sectionIdPrefix + separator + "0");
            authSection0.removeChild(authSection0.children[0]);
        } else {
            changeFirstSectionHeader(sectionIdPrefix, 0, titlePrefix, separator, false);
        }
    } else {
        refreshH3(sectionGroupId, titlePrefix);
    }
}

/* add or remove num in title for the first section */
function changeFirstSectionHeader(sectionIdPrefix, idx, titlePrefix, separator, add) {
    var title = add ? titlePrefix + 1 : titlePrefix;
    $("#" + sectionIdPrefix + separator + idx + " > div > h3").text(title);
}

function refreshH3(sectionGroupId, titlePrefix) {
    var num = 0;
    $("#" + sectionGroupId + " > section > div:first-child > h3").each(function () {
        $(this).text(titlePrefix + " " + ++num);
    });
}

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

/* oldEl is a JQuery Object */
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

function deleteSection(sectionIdPrefix, separator, idx) {
    var section = document.getElementById(sectionIdPrefix + separator + idx);
    section.parentNode.removeChild(section);
}

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
    }
}

function openIncident(maskedEditId){
    window.open("/bsb-web/eservice/INTERNET/IncidentNotification?editRefId="+maskedEditId);
}

//---------- END ----------


