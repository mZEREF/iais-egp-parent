$(function () {
    $("#next").unbind("click");
    $("#previous").unbind("click");
    $("a[data-step-key]").unbind("click");
    $("li[data-step-key]").unbind("click");

    $("#next").click(function () {
        showWaiting();
        removeDisable();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        $("#mainForm").submit();
    });

    $("#previous").click(function () {
        showWaiting();
        removeDisable();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("previous");
        $("#mainForm").submit();
    });

    $("a[data-step-key]").click(jumpStep);
    $("li[data-step-key]").click(jumpStep);
});

function jumpStep() {
    showWaiting();
    removeDisable();
    $("input[name='action_type']").val("jump");
    $("input[name='action_value']").val($(this).attr("data-step-key"));
    $("#mainForm").submit();
}

function readonlyPartPage() {
    $("input[type='text']").css("color", "#999");
    $("input[type='text']").css("border-color", "#ededed");
    $("input[type='text']").attr("disabled",true);
    $("input[type='number']").css("color", "#999");
    $("input[type='number']").css("border-color", "#ededed");
    $("input[type='number']").attr("disabled",true);
    $("input[type='radio']").attr("disabled",true);
    $("input[type='checkbox']").attr("disabled",true);
    $("textarea").attr("disabled",true);
    $("textarea").css("color", "#999");
    $("textarea").css("border-color", "#ededed");
    $("div.nice-select").addClass("disabled");
    $(".btn.btn-secondary").attr("disabled",true);
    $(".btn.file-upload.btn-secondary").css("pointer-events","none");
    $("#addNewSection").attr("disabled",true).css("pointer-events","none");
    $("#addNewSection").css({opacity:0.5});
    $("#addNewBatSection").attr("disabled",true).css("pointer-events","none");
    $("#addNewBatSection").css({opacity:0.5});
}


function removeDisable(){
    $("input[type='text']").css("color", "");
    $("input[type='text']").css("border-color", "");
    $("input[type='text']").removeAttr("disabled");
    $("input[type='number']").css("color", "");
    $("input[type='number']").css("border-color", "");
    $("input[type='number']").removeAttr("disabled");
    $("input[type='radio']").removeAttr("disabled");
    $("input[type='checkbox']").removeAttr("disabled");
    $("textarea").removeAttr("disabled");
    $("textarea").css("color", "");
    $("textarea").css("border-color", "");
    $("div.nice-select").removeClass("disabled");
    $(".btn.btn-secondary").removeAttr("disabled");
    $(".btn.file-upload.btn-secondary").css("pointer-events","auto");
    $("#addNewSection").attr("disabled",false).css("pointer-events","auto");
    $("#addNewSection").css({opacity:1});
    $("#addNewBatSection").attr("disabled",false).css("pointer-events","auto");
    $("#addNewBatSection").css({opacity:1});
}
