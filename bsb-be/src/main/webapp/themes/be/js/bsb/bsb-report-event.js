
function downloadFile(id) {
    url = "/bsb-web/ajax/doc/download/incidentView/repo/" + id;
    window.open(url);
}

function downloadSupportDocument(maskedAppId,maskedRepoId,docName){
    url = "/bsb-web/ajax/doc/download/incident/repo/" + maskedRepoId;
}


$(function (){
    $("#submitButton").click(function (){
        showWaiting();
        $("[name='action_type']").val("submit");
        $("#mainForm").submit();
    });

    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    $("#addNote").click(function (){
        showWaiting();
        $("[name='action_type']").val("add");
        $("#mainForm").submit();
    })

    $("#close").click(function (){
        $("#submitCloseModal").modal('show');
    })

})


function viewApp(module){
    var url = "";
    if(module === 'notification'){
        url = "/bsb-web/eservice/INTRANET/ViewIncidentNotification";
    }else if(module === 'investigation'){
        url = "/bsb-web/eservice/INTRANET/ViewInvestReport";
    } else if(module === 'followup1A'){
        url = "/bsb-web/eservice/INTRANET/ViewFollowup1A";
    } else if(module === 'followup1B'){
        url = "/bsb-web/eservice/INTRANET/ViewFollowup1B";
    }
    window.open(url);
}

function closeNote(){
    showWaiting();
    $("[name='action_type']").val("close");
    $("#mainForm").submit();
}