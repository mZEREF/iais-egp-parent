
function downloadFile(cond,id) {
    var url;
    if(cond === 'tab'){
        url = "/bsb-be/ajax/doc/download/incident/repo/" + id;
    }else if(cond === 'view'){
        url = "/bsb-be/ajax/doc/download/incidentView/repo/" + id;
    }
    window.open(url);
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
    })


})


function viewApp(module){
    var url = "";
    if(module === 'notification'){
        url = "/bsb-be/eservice/INTRANET/ViewIncidentNotification";
    }else if(module === 'investigation'){
        url = "/bsb-be/eservice/INTRANET/ViewInvestReport";
    } else if(module === 'followup1A'){
        url = "/bsb-be/eservice/INTRANET/ViewFollowup1A";
    } else if(module === 'followup1B'){
        url = "/bsb-be/eservice/INTRANET/ViewFollowup1Bc ";
    }
    window.open(url);
}