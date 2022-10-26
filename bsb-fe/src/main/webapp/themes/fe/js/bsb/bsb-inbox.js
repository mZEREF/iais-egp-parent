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

    $("#searchMsgDateFrom").change(function (){
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("msgDtFrom");
        $("#mainForm").submit();
    });

    $("#searchMsgDateTo").change(function (){
        showWaiting();
        $("[name='action_type']").val("search");
        $("[name='action_value']").val("msgDtTo");
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
        } else if (val.startsWith('downloadCertification')) {
            var downloadType = $("[name='download_type']").val();
            var downloadId = $("[name='download_id']").val();
            downloadInsCerFile(downloadType,downloadId);
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
        var checkedChkLength = $("input[name=chkChild]:checked").length;
        if(checkedChkLength >= 1){
            $("#doArchiveModal").modal('show');
        }else{
            $("#archiveModal").modal('show');
        }
    });

    $("#confirmArchive").click(function (){
        showWaiting();
        $("[name='action_type']").val("doArchive");
        $("[name='action_value']").val("inbox");
        $("#mainForm").submit();
    })

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

    if($("input[name='afterDoArchive']").val() === 'true'){
        $("#isArchivedModal").modal('show');
    }

    let existDraftModal = $("#existDraftModal");
    if (existDraftModal) {
        existDraftModal.modal('show');
    }
});

function downloadInsCerFile(type, id) {
    var url;
    if (type === 'INS') {
        url = "/bsb-web/ajax/doc/download/final/inspection/" + id;
    } else if (type === 'CER') {
        url = "/bsb-web/ajax/doc/download/final/certification/" + id;
    } else if (type === 'ALL') {
        url = "/bsb-web/ajax/doc/download/final/inspectionAndCertification/" + id;
    }

    window.open(url);
}

function draftAction(action){
    $('#existDraftModal').modal('hide');
    showWaiting();
    document.location = $('#lastUrl').val() +'&draftAction='+action;
}

function facilityActionControl(facilityId, selectId) {
    var actionValue = $("#"+ selectId).val();
    $.ajax({
        type:"GET",
        url:"/bsb-web/action-control/facility",
        data: {'actionValue':actionValue, 'facilityId': facilityId},
        dataType: "text",
        success:function(data) {
            console.log("success");
            if (data != null && data !== "") {
                if (data === "hasOnGoingApp") {
                    $("#invalidActionModal").modal('show');
                } else {
                    window.location = data;
                }
            }
            console.log("data content null");
        },
        error:function(){
            console.log("error");
        }
    });
}

function approvalActionControl(approvalId, selectId) {
    var actionValue = $("#"+ selectId).val();
    $.ajax({
        type:"GET",
        url:"/bsb-web/action-control/approval",
        data: {'actionValue':actionValue, 'approvalId': approvalId},
        dataType: "text",
        success:function(data) {
            console.log("success");
            if (data != null && data !== "") {
                if (data === "hasOnGoingApp") {
                    $("#invalidActionModal").modal('show');
                } else {
                    window.location = data;
                }
            }
            console.log("data content null");
        },
        error:function(){
            console.log("error");
        }
    });
}

function closeInvalidActionModal() {
    $('#invalidActionModal').modal('hide');
}