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
        var selector = $("#searchPanel");
        resetInput(selector);
        resetNiceSelect(selector);
    });

    $("#searchMsgType").change(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("msgType");
        $("#mainForm").submit();
    });


    $("#searchSubType").change(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("subType");
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

    $("input[name=chkParent]").click(function (){
        var chkLength = $("input[name=chkParent]:checked").length;
        if(chkLength === 1){
            $("input[name=chkChild]").each(function (){
                if(!$(this).prop("disabled")){
                    $(this).prop("checked",true);
                }
            })
        }else if(chkLength < 1){
            $("input[name=chkChild]").prop("checked",false);
        }
    });

    $("input[name=chkChild]").change(function (){
        var allChkLength = $("input[name=chkChild]").length;
        var checkedChkLength = $("input[name=chkChild]:checked").length;
        var disabledChkLength = $("input[name=chkChild]:disabled").length;
        if(allChkLength-disabledChkLength === checkedChkLength){
            $("input[name=chkParent]").prop("checked",true);
        }else{
            $("input[name=chkParent]").prop("checked",false);
        }
    });

    $("#doArchive").click(function (){
        showWaiting();
        $("[name='action_type']").val("doArchive");
        $("[name='action_value']").val("inbox");
        $("#mainForm").submit();
    });

    $("#moveArchive").click(function (){
        showWaiting();
        $("[name='action_type']").val("moveArchive");
        $("[name='action_value']").val("archive");
        $("#mainForm").submit();
    });

    $("#archive").click(function (){
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("archive");
        $("#mainForm").submit();
    });

    $("#inbox").click(function (){
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("inbox");
        $("#mainForm").submit();
    });
});