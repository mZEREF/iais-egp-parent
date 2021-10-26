$(document).ready(function () {

    $('input[type="text"]').blur(function () {

        var transEmbrFreshOccNum = $('#transEmbrFreshOccNum').val();
        var poorDevFreshOccNum = $('#poorDevFreshOccNum').val();
        var transEmbrThawOccNum = $('#transEmbrThawOccNum').val();
        var poorDevThawOccNum = $('#poorDevThawOccNum').val();

        var totalNum = Number(transEmbrFreshOccNum)+Number(poorDevFreshOccNum)+Number(transEmbrThawOccNum)+Number(poorDevThawOccNum);

        $('#totalNum').html(totalNum);
    });
});
function keyNumericPress() {
    var keyCode = event.keyCode;
    event.returnValue = keyCode >= 48 && keyCode <= 57;
}