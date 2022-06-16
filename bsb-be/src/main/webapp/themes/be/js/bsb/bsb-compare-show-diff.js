$(function () {
    $("[data-compare-new-data]").each(function () {
        var attrVal = $(this).attr("data-compare-new-data");
        var newVal = $(this).valueOf().html();
        if (attrVal !== "" || (attrVal === "" && newVal !== "no different")) {
            // has different, display new value
            $(this).show();
        }
    })
});