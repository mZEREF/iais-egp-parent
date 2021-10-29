$(document).ready(function () {
    $('.transferNumSelect').change(function () {
        let v = parseInt($(this).val());
        if (v == 1){
            $('#section2nd').hide();
            $('#section3rd').hide();
        }else if (v == 2){
            $('#section2nd').show();
            $('#section3rd').hide();
        }else if (v == 3){
            $('#section2nd').show();
            $('#section3rd').show();
        }
    });
});