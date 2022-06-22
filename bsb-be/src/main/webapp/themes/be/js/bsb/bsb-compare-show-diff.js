$(function () {
    $("[data-compare-new-data]").each(function () {
        var attrVal = $(this).attr("data-compare-new-data");
        var newVal = $(this).valueOf().html();
        if (attrVal !== "" || (attrVal === "" && newVal !== "no different")) {
            // has different, display new value
            $(this).show();
        }
    })

    $("[data-compare-new]").each(function () {
        var replaceWith = "-";
        var newAttrVal = $(this).attr("data-compare-new");
        var newVal = $(this).valueOf().html();
        var oldValueObject = $("[data-compare-old='"+newAttrVal+"']").valueOf();
        var oldVal = oldValueObject.html();
        if (oldVal === "" && newVal !== "") {
            oldValueObject.html(replaceWith);
            $(this).show();
        } else if (oldVal !== "" && newVal === "") {
            $(this).html(replaceWith);
            $(this).show();
        } else if (oldVal !== "" && newVal !== "" && oldVal !== newVal) {
            $(this).show();
        }
    })

    $("[data-list-compare-new]").each(function () {
        var a = "";
    })

    $("[data-compare-new-doc]").each(function () {
        var replaceWith = "-";
        var newAttrVal = $(this).attr("data-compare-new-doc");
        var newRepoId = $(this).attr("data-repo-id");
        var oldValueObject = $("[data-compare-old-doc='"+newAttrVal+"']").valueOf();
        var oldRepoId = oldValueObject.attr("data-repo-id");

        if (oldRepoId === "" && newRepoId !== "") {
            oldValueObject.html(replaceWith);
            $(this).show();
        } else if (oldRepoId !== "" && newRepoId === "") {
            $(this).html(replaceWith);
            $(this).show();
        } else if (oldRepoId !== "" && newRepoId !== "" && oldRepoId !== newRepoId) {
            $(this).show();
        }
    })
});