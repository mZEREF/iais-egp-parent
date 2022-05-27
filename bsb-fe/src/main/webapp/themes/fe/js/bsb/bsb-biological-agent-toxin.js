$(function () {
    var detailRelatedIdsPrefix = ["sampleOthers", "workBiomanufacturing", "workAnimal", "workOthers"];
    var detailDivIdPrefix = "sampleWorkDetailDiv";
    $("input[data-custom-ind=batOthersSampleType]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('sampleOthers'.length, id.length);
        showOrHideDetailsDiv(detailDivIdPrefix, detailRelatedIdsPrefix, idx);
    });

    $("input[data-custom-ind=batOthersWorkType]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('workOthers'.length, id.length);
        showOrHideDetailsDiv(detailDivIdPrefix, detailRelatedIdsPrefix, idx);
    });

    $("input[data-custom-ind=batAnimalWorkType]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('workAnimal'.length, id.length);
        showOrHideDetailsDiv(detailDivIdPrefix, detailRelatedIdsPrefix, idx);
    });

    $("input[data-custom-ind=batBmfWorkType]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('workBiomanufacturing'.length, id.length);
        showOrHideDetailsDiv(detailDivIdPrefix, detailRelatedIdsPrefix, idx);
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
            url:"/bsb-web/address-info/" + postalCode,
            dataType: 'json',
            error:function(){
                $("#invalidPostalCodeModal").modal('show');
            },
            success:function(data) {
                $("#blockNoT" + separator + idx).val(data.block_no);
                $("#floorNoT" + separator + idx).val(data.floor);
                $("#unitNoT" + separator + idx).val(data.unit_no);
                $("#streetNameT" + separator + idx).val(data.street);
                $("#buildingNameT" + separator + idx).val(data.building);
            }
        });
    });

    $("#exportingRetrieveAddressBtn").click(function retrieveAddress() {
        var idx = $(this).attr("data-current-idx");
        var separator = $(this).attr("data-section-separator");
        var postalCode = $("#postalCodeE" + separator + idx).val();
        if (!postalCode) {
            return false;
        }
        $.ajax({
            type:"GET",
            url:"/bsb-web/address-info/" + postalCode,
            dataType: 'json',
            error:function(){
                $("#invalidPostalCodeModal").modal('show');
            },
            success:function(data) {
                $("#blockNoE" + separator + idx).val(data.block_no);
                $("#floorNoE" + separator + idx).val(data.floor);
                $("#unitNoE" + separator + idx).val(data.unit_no);
                $("#streetNameE" + separator + idx).val(data.street);
                $("#buildingNameE" + separator + idx).val(data.building);
            }
        });
    });


    $("select[data-custom-ind=addressTypeT]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('addressTypeT'.length, id.length);
        var addressType = $(this).val();
        if(addressType === 'ADDTY001') {
            $("#aptMandatoryBlkT" + idx).show();
            $("#aptMandatoryFloorT" + idx).show();
            $("#aptMandatoryUnitT" + idx).show();
            $("#aptMandatoryStreetT" + idx).hide();
        } else {
            $("#aptMandatoryBlkT" + idx).hide();
            $("#aptMandatoryFloorT" + idx).hide();
            $("#aptMandatoryUnitT" + idx).hide();
            $("#aptMandatoryStreetT" + idx).show();
        }
    });

    $("select[data-custom-ind=addressTypeE]").change(function () {
        var id = $(this).attr("id");
        var idx = id.substring('addressTypeE'.length, id.length);
        var addressType = $(this).val();
        if(addressType === 'ADDTY001') {
            $("#aptMandatoryBlkE" + idx).show();
            $("#aptMandatoryFloorE" + idx).show();
            $("#aptMandatoryUnitE" + idx).show();
            $("#aptMandatoryStreetE" + idx).hide();
        } else {
            $("#aptMandatoryBlkE" + idx).hide();
            $("#aptMandatoryFloorE" + idx).hide();
            $("#aptMandatoryUnitE" + idx).hide();
            $("#aptMandatoryStreetE" + idx).show();
        }
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


function showOrHideDetailsDiv(detailIdPrefix, checkboxIdsPrefix, idx) {
    var show = false;
    var idPrefix;
    for (idPrefix of checkboxIdsPrefix) {
        if ($("#" + idPrefix + idx).is(":checked")) {
            show = true;
            break;
        }
    }
    if (show) {
        $("#" + detailIdPrefix + idx).show();
    } else {
        $("#" + detailIdPrefix + idx).hide();
    }
}