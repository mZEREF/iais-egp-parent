$(function (){
    $("#next").click(function (){
        var reportType = $("input[name = 'reportType']:checked").val();
        if(reportType === "EVTYPE001"){
            window.location.href = "/bsb-fe/eservice/INTERNET/IncidentNotification"
        }
    })
})