
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

    $("#viewAppBtn").click(function (){
        showWaiting();
        $("[name='action_type']").val("view");
        $("#mainForm").submit();
    })

})