$(function (){
    $("#submitBtn").click(function (){
        showWaiting();
        $("#mainForm").submit();
    });
});

function pickUpRescheduleAppt() {
    if ($("input:checkbox:checked").length > 0) {
        showWaiting();
        $("[name='action_type']").val("doReschedule");
        $("#mainForm").submit();
    } else {
        $('#rescheduleApptAlert').modal('show');
    }
}

function rescheduleApptCancel() {
    $('#rescheduleApptAlert').modal('hide');
}