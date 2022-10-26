$(function () {
    $("[data-compare-new]").each(function () {
        var replaceWith = "-";
        var newAttrVal = $(this).attr("data-compare-new");
        var newVal = $(this).attr("data-val");
        var oldValueObject = $("[data-compare-old='"+newAttrVal+"']").valueOf();
        var oldVal = oldValueObject.attr("data-val");
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
});