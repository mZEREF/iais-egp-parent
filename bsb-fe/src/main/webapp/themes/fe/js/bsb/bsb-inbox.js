function bsbInboxViewMsg(msgId) {
    showWaiting();
    $("[name='action_type']").val("viewMsg");
    $("[name='action_value']").val(msgId);
    $("#mainForm").submit();
}


function delDraftCancelBtn() {
    $('#deleteDraftModal').modal('hide');
}
function delDraftYesBtn() {
    $('#deleteDraftModal').modal('hide');
    showWaiting();
    $("[name='action_type']").val("deleteDraft");
    $("#mainForm").submit();
}
function delDraftMsgYesBtn() {
    $('#deleteDraftMessage').modal('hide');
}


$(function () {
    $("#clearBtn").click(function () {
        resetNiceSelect("#searchPanel");
    });

    $("#searchMsgType").change(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("msgType");
        $("#mainForm").submit();
    });


    $("#searchAppType").change(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("appType");
        $("#mainForm").submit();
    });


    $("#searchSubjectBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("subject");
        $("#mainForm").submit();
    });


    $("#searchBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("#mainForm").submit();
    });

    $("#clearDataSubBtn").click(function () {
        $("#searchDataSubNo").val("");
        $("#searchFacilityName option:first").prop("selected",'selected');
        $("#searchType option:first").prop("selected",'selected');
        $("#searchStatus option:first").prop("selected",'selected');
        $("#inboxFilter .current").text("All");
    });

    $("select[data-action-select]").change(function () {
        var val = this.value;
        if (val.startsWith('deleteDraft')) {
            $("[name='action_value']").val(val);
            $('#deleteDraftModal').modal('show');
        } else {
            window.location = this.value;
        }
    });
    if ($("#afterDeleteDraftApp").val() === 'true') {
        $("#deleteDraftMessage").modal('show');
    }

    $("#viewSubmission").click(function (){
        showWaiting();
        $("[name='action_type']").val("search");
        $("#mainForm").submit();
    });
});