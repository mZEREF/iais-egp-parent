$(document).ready(function () {
    $('#isPgtM').change(function () {
        if($(this).is(':checked')){
            $('#pgtMDisplay').attr("style","display: block");
        }else {
            $('#pgtMDisplay').attr("style","display: none");
        }
    });

    $('#isPgtSr').change(function () {
        if($(this).is(':checked')){
            $('#pgtSrDisplay').attr("style","display: block");
        }else {
            $('#pgtSrDisplay').attr("style","display: none");
        }
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
});