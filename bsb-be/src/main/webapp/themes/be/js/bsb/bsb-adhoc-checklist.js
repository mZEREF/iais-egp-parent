$(function () {
    $("#cancelBtn").click(function () {
        showWaiting();
        $("input[name='crud_action_type']").val('cancel');
        $("#mainForm").submit();
    });


    $("#saveAdhocItemBtn").click(function () {
        showWaiting();
        $("input[name='crud_action_type']").val('save');
        $("#mainForm").submit();
    });

    $("#adhocItemPoolBtn").click(function () {
        showWaiting();
        $("input[name='crud_action_type']").val('pool');
        $("#mainForm").submit();
    });


    $("#customAdhocBtn").click(function () {
        showWaiting();
        $("input[name='crud_action_type']").val('custom');
        $("#mainForm").submit();
    });

    $("#addAdhocBtn").click(function () {
        showWaiting();
        $("input[name='crud_action_type']").val('add');
        $("#mainForm").submit();
    });

    $("#customBtn").click(function () {
        showWaiting();
        $("input[name='crud_action_type']").val('customItem');
        $("#mainForm").submit();
    });
})

function removeAdhocItem(id) {
    showWaiting();
    SOP.Crud.cfxSubmit("mainForm", "remove", id);
}

function sortRecords(sortFieldName, sortType) {
    showWaiting();
    SOP.Crud.cfxSubmit("mainForm", "sort", sortFieldName, sortType);
}

function jumpToPagechangePage() {
    showWaiting();
    SOP.Crud.cfxSubmit("mainForm", "change");
}