$(function () {
    $("#next").click(function () {
        if ($("#declaration1").is(':checked') && $("#declaration2").is(':checked')) {
            showWaiting();
            $("input[name='action_type']").val("jump");
            $("input[name='action_value']").val("next");
            $("#mainForm").submit();
        } else {
            $("#submitDeclareModal").modal('show');
        }
    });
});