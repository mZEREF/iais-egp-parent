$(function () {
    $("#back").click(function () {
        showWaiting();
        $("[name='action_type']").val('back');
        $("#mainForm").submit();
    });

    $("#viewSelfAssessmt").click(function () {
        showWaiting();
        $("[name='action_type']").val('viewSelfAssessment');
        $("#mainForm").submit();
    });

    $("#submit").click(function () {
        showWaiting();
        $("[name='action_type']").val('submit');
        $("#mainForm").submit();
    });
})


function viewApplication() {
    /* incomplete
     * This method is not implemented in other modules.
     * This function should be a common one.
     */
    window.open ("/bsb-be/eservice/INTRANET/MohBeAppViewDetails");
}