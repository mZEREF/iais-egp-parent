/* This js file assume the cascade dropdown data is saved in a data structure as follows:
 * Map<String, List< text-value(obj/map) >>.
 * key is the value of the trigger dropdown, value is a list of dropdown to be cascaded.
 * text-value(obj/map): {"text": "<option text>", "value": "<option value>"}.
 *
 * <br/>
 *
 * This js also support rich dropdown option, these function will expect a data structure as follows:
 * Map<String, List< text-value-disable(obj/map) >>.
 * The value contains an additional disable property.
 * text-value-disable(obj/map): {"text": "<option text>", "value": "<option value>", "disable": <true/false>}.
 * */


/* preFunc is called at the start of the change event, postFunc is called at the end of the change event */
function registerCascadeEvent(triggerDropDownId, targetDropdownId, dataMap, preFunc, postFunc) {
    $("#"+triggerDropDownId).change(function () {
        if (preFunc) {preFunc();}
        changeOptionItemsFromDataMap(targetDropdownId, dataMap, this.value);
        if (postFunc) {postFunc();}
    });
}

/* see #registerCascadeEvent, but the target dropdown ID can be calculated by a function with trigger dropdown ID as argument */
function registerCascadeEvent4RelatedId(triggerDropDownId, targetDropdownIdComputeFunc, dataMap, preFunc, postFunc) {
    $("#"+triggerDropDownId).change(function () {
        if (preFunc) {preFunc();}
        var targetDropdownId = targetDropdownIdComputeFunc(triggerDropDownId);
        changeOptionItemsFromDataMap(targetDropdownId, dataMap, this.value);
        if (postFunc) {postFunc();}
    });
}


/* The value comes from the trigger dropdown.
 * If the value can not be found in the data map, nothing will happen */
function changeOptionItemsFromDataMap(dropDownId, dataMap, value) {
    if (dataMap[value]) {
        changeOptionItems(dropDownId, dataMap[value]);
    }
}


function changeOptionItems(dropDownId, optionItemList) {
    var dropdown = document.getElementById(dropDownId);
    // delete all options
    dropdown.length = 0;

    // create and add options
    var option;
    for (const optionItem of optionItemList) {
        option = new Option(optionItem.text, optionItem.value, false, false);
        dropdown.options[dropdown.options.length] = option;
    }
}



/* ********************************************************************************** */

/* preFunc is called at the start of the change event, postFunc is called at the end of the change event.
 * dataMap contains rich cascaded options */
function registerRichCascadeEvent(triggerDropDownId, targetDropdownId, dataMap, preFunc, postFunc) {
    $("#"+triggerDropDownId).change(function () {
        if (preFunc) {preFunc();}
        changeRichOptionItemsFromDataMap(targetDropdownId, dataMap, this.value);
        if (postFunc) {postFunc();}
    });
}

/* The value comes from the trigger dropdown.
 * If the value can not be found in the data map, nothing will happen */
function changeRichOptionItemsFromDataMap(dropDownId, dataMap, value) {
    if (dataMap[value]) {
        changeRichOptionItems(dropDownId, dataMap[value]);
    }
}

function changeRichOptionItems(dropDownId, optionItemList) {
    var dropdown = document.getElementById(dropDownId);
    // delete all options
    dropdown.length = 0;

    // create and add options
    var option;
    for (const optionItem of optionItemList) {
        option = new Option(optionItem.text, optionItem.value, false, false);
        if (optionItem.disable) {
            option.disabled = true;
        }
        dropdown.options[dropdown.options.length] = option;
    }
}