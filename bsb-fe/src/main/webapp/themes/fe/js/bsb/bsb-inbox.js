function bsbInboxViewMsg(msgId) {
    showWaiting();
    $("[name='action_type']").val("viewMsg");
    $("[name='action_value']").val(msgId);
    $("#mainForm").submit();
}


$(function () {
    $("#searchMsgType").change(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("msgType");
        $("#mainForm").submit();
    })


    $("#searchAppType").change(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("appType");
        $("#mainForm").submit();
    })


    $("#searchSubjectBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("subject");
        $("#mainForm").submit();
    })


    $("#searchBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("#mainForm").submit();
    });

    $("select[data-action-select]").change(function () {
        window.location = this.value;
    });
});