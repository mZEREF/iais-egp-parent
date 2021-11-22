$(document).ready(function () {
    $('#hasOocyte').click(function () {
        if ($(this).prop('checked')) {
            $('.oocytesParts').show();
        } else {
            $('.oocytesParts').hide();
            $('.oocytesParts').find("input").val("");
        }
    });

    $('#hasEmbryo').click(function () {
        if ($(this).prop('checked')) {
            $('.embryosParts').show();
        } else {
            $('.embryosParts').hide();
            $('.embryosParts').find("input").val("");
        }
    });
});