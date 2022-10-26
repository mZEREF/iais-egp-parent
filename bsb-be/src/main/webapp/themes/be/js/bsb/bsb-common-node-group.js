$(function () {
    $("#next").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        $("#mainForm").submit();
    });

    $("#previous").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("previous");
        $("#mainForm").submit();
    });

    $("a[data-step-key]").click(jumpToStep);
    $("li[data-step-key]").click(jumpToStep);
});


/* Jump for any element contains 'data-step-key' */
function jumpToStep() {
    showWaiting();
    $("input[name='action_type']").val("jump");
    $("input[name='action_value']").val($(this).attr("data-step-key"));
    $("#mainForm").submit();
}