$(document).ready(function (){
    var $content = $('.med');
    refreshIndex($content);
    $('select[class = "frequency"]').each(function(index, ele){
        $(ele).unbind('change');
        $(ele).on('change', function() {
            toggleOnSelect(ele, 'FRE009', 'othersFrequency' + index);
        })
    });
    var drugMedicationLength = $content.length;
    $('input[name="drugMedicationLength"]').val(drugMedicationLength);
    $('.panel-main-content').children(":first");
    var num=$('#medicationDiv').length;
    if(num==0){
        $('.removeMedications').hide();
    }
});

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
    var $meContent = $('.medicationContent > div:nth-of-type(2)');
    console.log($meContent)
    refreshIndex($content);
    refreshId($meContent);
    $meContent.each(function (k,v) {
        var $input = $(v);
        if(k>=1){
            $input.attr("style","display: none")
        }
    });
    var $frequencyContent = $('select[class = "frequency"]');
    console.log($frequencyContent)
    $('select[class = "frequency"]').each(function(index, ele){
        $(ele).unbind('change');
        $(ele).on('change', function() {
            toggleOnSelect(ele, 'FRE009', 'othersFrequency' + index);
        })
    });
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
function refreshId(targetSelector) {
    $(targetSelector).each(function (k,v) {
        var $input = $(v);
        if ($input.hasClass('not-refresh')) {
            return;
        }
        var orgId = $input.attr('id');
        if (isEmpty(orgId)) {
            return;
        }
        var result = /(.*\D+)/g.exec(orgId);
        var id = !isEmpty(result) && result.length > 0 ? result[0] : orgId;
        $input.prop('id', id + k);
    });
}