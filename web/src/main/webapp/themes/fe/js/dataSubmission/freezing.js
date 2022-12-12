$(document).ready(function (){
    $("#isFreshOocyte").change(function () {
        var freshOocyte = $("#isFreshOocyte").prop('checked');
        if (freshOocyte) {
            $('#freshOocyte').show();
        } else {
            $('#freshOocyte').hide();
            clearFields('#freshOocyte');
        }
    });
    $("#isThawedOocyte").change(function () {
        var thawedOocyte = $("#isThawedOocyte").prop('checked');
        if (thawedOocyte){
            $('#thawedOocyte').show();
        } else {
            $('#thawedOocyte').hide();
            clearFields('#thawedOocyte');
        }
    });
    $("#isFreshEmbryo").change(function () {
        var freshEmbryo = $("#isFreshEmbryo").prop('checked');
        if (freshEmbryo) {
            $('#freshEmbryo').show();
        } else {
            $('#freshEmbryo').hide();
            clearFields('#freshEmbryo');
        }
    });
    $("#isThawedEmbryo").change(function () {
        var thawedEmbryo = $("#isThawedEmbryo").prop('checked');
        if (thawedEmbryo) {
            $('#thawedEmbryo').show();
        } else {
            $('#thawedEmbryo').hide();
            clearFields('#thawedEmbryo');
        }
    });
});
