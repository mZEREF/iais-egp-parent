$(function () {
    $("#searchBtn").click(function () {
        showWaiting();
        $("[name='action_type']").val("search");
        $("#mainForm").submit();
    })


    $("#clearBtn").click(function() {
        $('input[name="searchFacName"]').val("");
        $('input[name="searchFacAddr"]').val("");
        $('input[name="searchAppNo"]').val("");
        $('input[name="searchAppDateFrom"]').val("");
        $('input[name="searchAppDateTo"]').val("");
        $("#searchFacType option:first").prop("selected", 'selected');
        $("#searchProcessType option:first").prop("selected", 'selected');
        $("#searchAppType option:first").prop("selected", 'selected');
        $("#searchAppStatus option:first").prop("selected", 'selected');

    })
});