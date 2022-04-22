$(function () {
    $("input[data-custom-ind=batOthersSampleType]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('sampleOthers'.length, id.length);
        if ($(this).is(":checked")) {
            $("#sampleWorkDetailDiv" + idx).show();
        } else {
            if(!$("#workOthers" + idx).is(":checked")) {
                $("#sampleWorkDetailDiv" + idx).hide();
            }
        }
    });

    $("input[data-custom-ind=batOthersWorkType]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('workOthers'.length, id.length);
        if ($(this).is(":checked")) {
            $("#sampleWorkDetailDiv" + idx).show();
        } else {
            if(!$("#sampleOthers" + idx).is(":checked")) {
                $("#sampleWorkDetailDiv" + idx).hide();
            }
        }
    });



    $("input[data-custom-ind=batProcurementModeLocal]").click(function () {
        var id = $(this).attr("id");
        var idx = id.substring('procurementModeLocalTransfer'.length, id.length);
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


    $("#localTransferRetrieveAddressBtn").click(function retrieveAddress() {
        var idx = $(this).attr("data-current-idx");
        var separator = $(this).attr("data-section-separator");
        var postalCode = $("#postalCodeT" + separator + idx).val();
        if (!postalCode) {
            return false;
        }
        $.ajax({
            type:"GET",
            url:"/bsb-fe/address-info/" + postalCode,
            dataType: 'json',
            error:function(){
                $("#invalidPostalCodeModal").modal('show');
            },
            success:function(data) {
                $("#addressTypeT" + separator + idx).val(data.address_type);
                $("#blockNoT" + separator + idx).val(data.block_no);
                $("#floorNoT" + separator + idx).val(data.floor);
                $("#unitNoT" + separator + idx).val(data.unit_no);
                $("#streetNameT" + separator + idx).val(data.street);
                $("#buildingNameT" + separator + idx).val(data.building);
            }
        });
    });


    $("select[data-cascade-dropdown=schedule-bat]").each(function () {
        var id = $(this).attr("id");
        var batDropdownId = computeBatDropdownIdByScheduleDropdownId(id);
        registerCascadeEvent(id, batDropdownId, scheduleBatDataJson, null, function () {
            $("#"+batDropdownId).niceSelect("update");
        });
    });
});



function computeBatDropdownIdByScheduleDropdownId(scheduleDropdownId) {
    var idx = scheduleDropdownId.substring("schedule".length, scheduleDropdownId.length);
    return "batName" + idx;
}