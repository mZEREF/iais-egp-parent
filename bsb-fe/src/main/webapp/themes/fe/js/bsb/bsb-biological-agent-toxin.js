$(function () {
    $("input[data-custom-ind=batOthersSampleType]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('sampleOthers'.length, id.length);
        if ($(this).is(":checked")) {
            $("#batOtherSampleTypeDiv" + idx).show();
        } else {
            if(!$("#workOthers" + idx).is(":checked")) {
                $("#batOtherSampleTypeDiv" + idx).hide();
            }
        }
    });

    $("input[data-custom-ind=batOthersWorkType]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('workOthers'.length, id.length);
        if ($(this).is(":checked")) {
            $("#batOtherWorkTypeDiv" + idx).show();
        } else {
            if(!$("#sampleOthers" + idx).is(":checked")) {
                $("#batOtherWorkTypeDiv" + idx).hide();
            }
        }
    });

    $("input[data-custom-ind=batProcurementModeLocal]").click(function () {
        var id = $(this).attr("id");
        var idx = id.substring('procurementModeLocal'.length, id.length);
        $("#transferringFacilityDiv" + idx).show();
        $("#exportingFacilityDiv" + idx).hide();
        $("#sourceFacilityDiv" + idx).hide();
    });
    $("input[data-custom-ind=batProcurementModeImport]").click(function () {
        var id = $(this).attr("id");
        var idx = id.substring('procurementModeImport'.length, id.length);
        $("#transferringFacilityDiv" + idx).hide();
        $("#exportingFacilityDiv" + idx).show();
        $("#sourceFacilityDiv" + idx).hide();
    });
    $("input[data-custom-ind=batProcurementModePossession]").click(function () {
        var id = $(this).attr("id");
        var idx = id.substring('procurementModePossession'.length, id.length);
        $("#transferringFacilityDiv" + idx).hide();
        $("#exportingFacilityDiv" + idx).hide();
        $("#sourceFacilityDiv" + idx).show();
    });
});