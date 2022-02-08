$(function (){
    $("#next").click(function (){
        var reportType = $("input[name = 'reportType']:checked").val();
        if(reportType === "EVTYPE001"){
            window.location.href = "/bsb-fe/eservice/INTERNET/IncidentNotification";
        }else if(reportType === "EVTYPE002"){
            window.location.href = "/bsb-fe/eservice/INTERNET/InvestigationReport";
        }else if(reportType === "EVTYPE003"){
            window.location.href = "/bsb-fe/eservice/INTERNET/IncidentFollowUPReport1A";
        }else if(reportType === "EVTYPE004"){
            window.location.href = "/bsb-fe/eservice/INTERNET/IncidentFollowUPReport1B";
        }
    })
})