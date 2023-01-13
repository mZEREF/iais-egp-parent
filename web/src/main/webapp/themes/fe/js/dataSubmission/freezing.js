$(document).ready(function (){
    $("#isFreshOocyte").change(function () {
        var freshOocyte = $("#isFreshOocyte").prop('checked');
        if (freshOocyte) {
            $('#freshOocyteCryopNum').val('');
            $('#freshOocyte').show();
        } else {
            $('#freshOocyte').hide();
            clearFields('#freshOocyte');
        }
    });
    $("#isThawedOocyte").change(function () {
        var thawedOocyte = $("#isThawedOocyte").prop('checked');
        if (thawedOocyte){
            $('#thawedOocyteCryopNum').val('');
            $('#thawedOocyte').show();
        } else {
            $('#thawedOocyte').hide();
            clearFields('#thawedOocyte');
        }
    });
    $("#isFreshEmbryo").change(function () {
        var freshEmbryo = $("#isFreshEmbryo").prop('checked');
        if (freshEmbryo) {
            $('#freshEmbryoCryopNum').val('');
            $('#freshEmbryo').show();
        } else {
            $('#freshEmbryo').hide();
            clearFields('#freshEmbryo');
        }
    });
    $("#isThawedEmbryo").change(function () {
        var thawedEmbryo = $("#isThawedEmbryo").prop('checked');
        if (thawedEmbryo) {
            $('#thawedEmbryoCryopNum').val('');
            $('#thawedEmbryo').show();
        } else {
            $('#thawedEmbryo').hide();
            clearFields('#thawedEmbryo');
        }
    });
});
