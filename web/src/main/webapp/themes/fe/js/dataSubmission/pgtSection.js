$(document).ready(function () {
    $('#isOtherPgt').change(function () {
        if($(this).is(':checked')){
            $('#pgtOthersDisplay').attr("style","display: block");
        }else {
            $('#pgtOthersDisplay').attr("style","display: none");
        }
    });

    $('#isPgtMCom').change(function () {
        if($('#isPgtMEbt').is(':checked')||$('#isPgtMRare').is(':checked')||$('#isPgtMCom').is(':checked')){
            $('#pgtMDisplay').attr("style","display: block");
        }else {
            $('#pgtMDisplay').attr("style","display: none");
        }
        isThereAppealDisplay();
    });
    $('#isPgtMRare').change(function () {
        if($('#isPgtMEbt').is(':checked')||$('#isPgtMRare').is(':checked')||$('#isPgtMCom').is(':checked')){
            $('#pgtMDisplay').attr("style","display: block");
        }else {
            $('#pgtMDisplay').attr("style","display: none");
        }
        isThereAppealDisplay();
    });
    $('#isPgtMEbt').change(function () {
        if($('#isPgtMEbt').is(':checked')||$('#isPgtMRare').is(':checked')||$('#isPgtMCom').is(':checked')){
            $('#pgtMDisplay').attr("style","display: block");
        }else {
            $('#pgtMDisplay').attr("style","display: none");
        }
        isThereAppealDisplay();
    });

    $('#isPgtMDsld').change(function () {
        if($(this).is(':checked')){
            $('#pgtMRefNoDisplay').attr("style","display: block");
        }else {
            $('#pgtMRefNoDisplay').attr("style","display: none");
        }
    });

    $('#isPgtSr').change(function () {
        if($(this).is(':checked')){
            $('#pgtSrDisplay').attr("style","display: block");
        }else {
            $('#pgtSrDisplay').attr("style","display: none");
        }
        isThereAppealDisplay();
    });

    $('#radioYes').change(function () {
        isThereAppealDisplay();
    });
    $('#radioNo').change(function () {
        isThereAppealDisplay();
    });

    $('#isPgtA').change(function () {
        if($(this).is(':checked')){
            $('#pgtADisplay').attr("style","display: block");
        }else {
            $('#pgtADisplay').attr("style","display: none");
        }
    });

    $('#isPtt').change(function () {
        if($(this).is(':checked')){
            $('#pttDisplay').attr("style","display: block");
        }else {
            $('#pttDisplay').attr("style","display: none");
        }
    });

    $("[name='pgtAResult']").change(function () {

        if($(this).val()=='Abnormal'){
            $('#AbnormalDisplay').attr("style","display: block");
        }else {
            $('#AbnormalDisplay').attr("style","display: none");
        }
    });


    $('#isEmbryosBiopsiedLocal').change(function () {

        var reason= $('#isEmbryosBiopsiedLocal option:selected').val();

        if("Others"==reason){
            $('#othersEmbryosBiopsyDisplay').attr("style","display: block");
        }else {
            $('#othersEmbryosBiopsyDisplay').attr("style","display: none");
        }

    });

    $('#isBiopsyLocal').change(function () {

        var reason= $('#isBiopsyLocal option:selected').val();

        if("Others"==reason){
            $('#othersBiopsyDisplay').attr("style","display: block");
        }else {
            $('#othersBiopsyDisplay').attr("style","display: none");
        }

    });
});


var isThereAppealDisplay = function () {
    var pgtMCom=$("#isPgtMCom").is(':checked');
    var pgtMRare=$("#isPgtMRare").is(':checked');
    var pgtMEbt=$("#isPgtMEbt").is(':checked');
    var pgtSr=$("#isPgtSr").is(':checked');
    var radioYes=$("#radioYes").is(':checked');
    var count= '<%= request.getAttribute("count")%>';
    if((pgtMCom || pgtMRare|| pgtMEbt|| pgtSr)&& radioYes && count>=6){
        $('#appealDisplay').attr("style","display: block");
    }else {
        $('#appealDisplay').attr("style","display: none");
    }
}
