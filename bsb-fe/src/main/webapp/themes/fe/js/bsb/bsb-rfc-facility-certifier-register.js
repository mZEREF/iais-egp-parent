$(function () {
    readonlyPartPage();

    $("#edit").click(function (){
        unreadonlyPartPage();
    });

    $("#next").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("next");
        sumbitRemoveDisable();
        $("#mainForm").submit();
    });

    $("#saveDraft").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("draft");
        sumbitRemoveDisable();
        $("#mainForm").submit();
    });

    $("#back").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val("back");
        sumbitRemoveDisable();
        $("#mainForm").submit();
    });

    $("a[data-step-key]").click(function () {
        showWaiting();
        $("input[name='action_type']").val("jump");
        $("input[name='action_value']").val($(this).attr("data-step-key"));
        sumbitRemoveDisable();
        $("#mainForm").submit();
    });

    // doc upload
    $("a[data-upload-file]").click(function () {
        // create input file
        var name = $(this).attr("data-upload-file");
        var id = name + +new Date();
        var newFileInput = document.createElement("input");
        newFileInput.setAttribute("type", "file");
        newFileInput.setAttribute("id", id);
        newFileInput.setAttribute("name", name);
        newFileInput.addEventListener("change", addReloadFile);
        $("#fileUploadInputDiv").append(newFileInput);

        // click to select file
        newFileInput.click();
    });


    // // facility classification radio button changes
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