function specialEditDisableAll() {
    editDisableBtn("localTransferRetrieveAddressBtn")
    editDisableBtn("addNewBatSection")
    editDisableBtn4MultiSection("removeBtn")
    editDisableBtn("exportingRetrieveAddressBtn")
}

var fieldMap = new Map([
    ["allField", editEnableAll],

    ["schedule", editEnableSelect4MultiSection],
    ["batName", editEnableTextInput4MultiSection],
    ["sampleType", editEnableCheckbox4MultiSection],
    ["workType", editEnableCheckbox4MultiSection],
    ["sampleWorkDetail", editEnableTextarea4MultiSection],
    ["estimatedMaximumVolume", editEnableTextInput4MultiSection],
    ["methodOrSystem", editEnableTextInput4MultiSection],
    ["procurementMode", editEnableRadioButton4MultiSection],

    ["facNameT", editEnableTextInput4MultiSection],
    ["postalCodeT", editEnableTextInput4MultiSection],
    ["addressTypeT", editEnableSelect4MultiSection],
    ["blockNoT", editEnableTextInput4MultiSection],
    ["floorNoT", editEnableTextInput4MultiSection],
    ["unitNoT", editEnableTextInput4MultiSection],
    ["streetNameT", editEnableTextInput4MultiSection],
    ["buildingNameT", editEnableTextInput4MultiSection],
    ["contactPersonNameT", editEnableTextInput4MultiSection],
    ["emailAddressT", editEnableTextInput4MultiSection],
    ["contactNoT", editEnableTextInput4MultiSection],
    ["expectedDateT", editEnableTextInput4MultiSection],
    ["courierServiceProviderNameT", editEnableTextInput4MultiSection],
    ["remarksT", editEnableTextarea4MultiSection],

    ["facNameE", editEnableTextInput4MultiSection],
    ["postalCodeE", editEnableTextInput4MultiSection],
    ["addressTypeE", editEnableSelect4MultiSection],
    ["blockNoE", editEnableTextInput4MultiSection],
    ["floorNoE", editEnableTextInput4MultiSection],
    ["unitNoE", editEnableTextInput4MultiSection],
    ["streetNameE", editEnableTextInput4MultiSection],
    ["buildingNameE", editEnableTextInput4MultiSection],
    ["countryE", editEnableSelect4MultiSection],
    ["cityE", editEnableTextInput4MultiSection],
    ["stateE", editEnableTextInput4MultiSection],
    ["contactPersonNameE", editEnableTextInput4MultiSection],
    ["emailAddressE", editEnableTextInput4MultiSection],
    ["contactNoE", editEnableTextInput4MultiSection],
    ["expectedDateE", editEnableTextInput4MultiSection],
    ["courierServiceProviderNameE", editEnableTextInput4MultiSection],
    ["remarksE", editEnableTextarea4MultiSection],

    ["localTransferRetrieveAddressBtn", editEnableBtn],
    ["addNewBatSection", editEnableBtn],
    ["exportingRetrieveAddressBtn", editEnableBtn],
])