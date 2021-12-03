$(function (){
    $("#safeRadio").change(function (){
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='INCTYPE001']").attr("checked", true);
        $("#safeTypes").show();
        $("#securTypes").hide();
        $("#generTypes").hide();
    });

    $("#securRadio").change(function (){
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='INCTYPE002']").attr("checked", true);
        $("#safeTypes").hide();
        $("#securTypes").show();
        $("#generTypes").hide();
    });

    $("#generRadio").change(function (){
        $("input[type='checkbox']").removeAttr("checked");
        $("input[value='INCTYPE003']").attr("checked", true);
        $("#safeTypes").hide();
        $("#securTypes").hide();
        $("#generTypes").show();
    });

    $("#next").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        $("#mainForm").submit();
    });

    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("back");
        $("#mainForm").submit();
    });

    $("#possibleY").change(function (){
        $("#releasePossibleY").show();
    });
    $("#possibleN").change(function (){
        $("#releasePossibleY").hide();
    });
    $("#involvedY").change(function (){
        $("#involved").show();
    });
    $("#involvedN").change(function (){
        $("#involved").hide();
    })

})
