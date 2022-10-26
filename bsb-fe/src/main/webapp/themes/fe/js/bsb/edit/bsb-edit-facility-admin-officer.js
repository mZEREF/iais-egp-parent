function specialEditDisableAll() {
    editDisableBtn("addNewOfficerSection")
    editDisableBtn4MultiSection("removeBtn")
}

var fieldMap = new Map([
    ["allField", editEnableAll],
    ["addNewOfficerSection", editEnableBtn],
    ["removeBtn", editEnableBtn4MultiSection],
])