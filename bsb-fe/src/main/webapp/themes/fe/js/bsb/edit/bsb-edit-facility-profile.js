function specialEditDisableAll() {
    editDisableBtn4MultiSection("retrieveAddressBtn")
    editDisableBtn("addNewProfileSection")
}

var fieldMap = new Map([
    // ["facName", editEnableTextInput4MultiSection],
    // ["facType", editEnableSelect4MultiSection],
    // ["facTypeDetails", editEnableTextInput4MultiSection],
    // ["isSameAddress", editEnableRadioButton4MultiSection],
    // ["block", editEnableTextInput4MultiSection],
    // ["addressType", editEnableSelect4MultiSection],
    // ["streetName", editEnableTextInput4MultiSection],
    // ["floor", editEnableTextInput4MultiSection],
    // ["unitNo", editEnableTextInput4MultiSection],
    // ["postalCode", editEnableTextInput4MultiSection],
    // ["buildingName", editEnableTextInput4MultiSection],
    // ["protectedPlace", editEnableRadioButton4MultiSection],
    //
    // ["inChargePersonName", editEnableTextInput4MultiSection],
    // ["inChargePersonDesignation", editEnableTextInput4MultiSection],
    // ["inChargePersonEmail", editEnableTextInput4MultiSection],
    // ["inChargePersonContactNo", editEnableTextInput4MultiSection],
    //
    // ["opvSabin1IM", editEnableRadioButton4MultiSection],
    // ["opvSabin2IM", editEnableRadioButton4MultiSection],
    // ["opvSabin3IM", editEnableRadioButton4MultiSection],
    // ["opvSabin1IMExpectedDestructDt", editEnableTextInput4MultiSection],
    // ["opvSabin2IMExpectedDestructDt", editEnableTextInput4MultiSection],
    // ["opvSabin3IMExpectedDestructDt", editEnableTextInput4MultiSection],
    // ["opvSabin1IMRetentionReason", editEnableTextarea4MultiSection],
    // ["opvSabin2IMRetentionReason", editEnableTextarea4MultiSection],
    // ["opvSabin3IMRetentionReason", editEnableTextarea4MultiSection],
    //
    // ["opvSabin1PIM", editEnableRadioButton4MultiSection],
    // ["opvSabin2PIM", editEnableRadioButton4MultiSection],
    // ["opvSabin3PIM", editEnableRadioButton4MultiSection],
    // ["opvSabin1PIMRiskLevel", editEnableSelect4MultiSection],
    // ["opvSabin2PIMRiskLevel", editEnableSelect4MultiSection],
    // ["opvSabin3PIMRiskLevel", editEnableSelect4MultiSection],
    // ["opvSabin1PIMRetentionReason", editEnableTextarea4MultiSection],
    // ["opvSabin2PIMRetentionReason", editEnableTextarea4MultiSection],
    // ["opvSabin3PIMRetentionReason", editEnableTextarea4MultiSection],
    ["allField", editEnableAll],
    ["retrieveAddressBtn", editEnableBtn4MultiSection],
    ["addNewProfileSection", editEnableBtn],
])