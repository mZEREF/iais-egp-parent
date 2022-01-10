$(function () {
    removeMedications();
});
function addMedications() {
    var targetClass = '.med';
    var $content = $(targetClass).eq(0).clone();
    console.log("Target: " + $content.length);
    $('#medicationDiv').append($content);
    clearFields($content);
    var num=$('#medicationDiv div').length;
    if(num!=0){
        $('#medicationDiv .med .removeMedications').show();
    }
    refreshKeyAppointmentHolder();
    removeMedications();
}
function removeMedications(){
    $('.removeMedications').on("click", function() {
        console.log("click is triger")
        $(this).closest('#medicationDiv .med').remove();
        refreshKeyAppointmentHolder();
    })
}
function refreshKeyAppointmentHolder() {
    console.log("refreshKeyAppointmentHolder start")
    var $content = $('.med');
    refreshIndex($content);
    var drugMedicationLength = $content.length;
    $('input[name="drugMedicationLength"]').val(drugMedicationLength);
    $content.each(function (k,v) {
        if (k == 0) {
            $(this).find('.assign-psn-item').text('1');
        } else {
            $(this).find('.assign-psn-item').text(k+1);
        }
    });
}
$(document).ready(function (){
    var $content = $('.med');
    refreshIndex($content);
    var drugMedicationLength = $content.length;
    $('input[name="drugMedicationLength"]').val(drugMedicationLength);
    $('.panel-main-content').children(":first");
    var num=$('#medicationDiv').length;
    if(num==0){
        $('.removeMedications').hide();
    }
});