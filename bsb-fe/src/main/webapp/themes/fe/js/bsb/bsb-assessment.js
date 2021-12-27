$(function () {
    $("button[data-custom-ind=processSelfAssessment]").click(function () {
        showWaiting();
        var appId = $(this).attr("data-custom-app");
        var action = this.value;
        $("input[name='action_type']").val("load");
        $("input[name='action_value']").val(action);
        $("input[name='action_additional']").val(appId);
        $("#mainForm").submit();
    });

    $("button[data-custom-ind=printSelfAssessment]").click(function () {
        // to be implemented in future
    });


    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val("back");
        $("#mainForm").submit();
    });

    $("#clear").click(function () {
        showWaiting();
        $("input[name='action_type']").val("clear");
        $("#mainForm").submit();
    });

    $("#save").click(function () {
        showWaiting();
        $("input[name='action_type']").val("save");
        $("#mainForm").submit();
    });
})