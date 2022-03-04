$(function () {
    readonlyPartPage();

    $("#edit").click(function (){
        unreadonlyPartPage();
    });

    $("#next").click(function () {
        showWaiting();
        sumbitRemoveDisable();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        $("#mainForm").submit();
    });

    $("#saveDraft").click(function () {
        showWaiting();
        sumbitRemoveDisable();
        $("input[name='action_type']").val("draft");
        $("#mainForm").submit();
    });

    $("#back").click(function () {
        showWaiting();
        sumbitRemoveDisable();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("back");
        $("#mainForm").submit();
    });

    $("a[data-step-key]").click(function () {
        showWaiting();
        sumbitRemoveDisable();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val($(this).attr("data-step-key"));
        $("#mainForm").submit();
    });

    $("a[review-edit-data-step-key]").click(function (){
        showWaiting();
        sumbitRemoveDisable();
        $("input[name='action_type']").val("reviewEdit");
        $("input[name='action_value']").val($(this).attr("review-edit-data-step-key"));
        $("#mainForm").submit();
    });

    $("#previewButton").click(function (){
        showWaiting();
        sumbitRemoveDisable();
        $("input[name='action_type']").val("review");
        $("input[name='action_value']").val("next");
        $("#mainForm").submit();
    })

    $("#submit").click(function () {
        if ($("#require").is(':checked') && $("#accuracy").is(':checked')) {
            showWaiting();
            sumbitRemoveDisable();
            $("input[name='action_type']").val("jump");
            $("input[name='action_value']").val("next");
            $("#mainForm").submit();
        } else {
            $("#submitDeclareModal").modal('show');
            $(".btn.btn-secondary").removeAttr("disabled");
        }
    });

    // facility classification radio button changes
    var overseasVal = $("#overseasCon");
    $("#typeLocal").change(function () {
        overseasVal.hide();
    });
    $("#typeOverseas").change(function () {
        overseasVal.show();
    });


    $("#addNewSection").click(function () {
        var meta = readSectionRepeatMetaData();
        addSection(meta.idxInputName, meta.sectionIdPrefix, meta.headerTitlePrefix, meta.sectionGroupId, meta.separator);
    });


    $(".removeBtn").click(removeBtnEventHandler);

});