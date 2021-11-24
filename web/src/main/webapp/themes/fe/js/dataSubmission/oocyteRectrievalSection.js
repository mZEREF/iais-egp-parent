$(document).ready(function () {
    $('input[type="checkbox"]').change(function () {
        if ($("#isFromPatient").prop('checked') || $("#isFromPatientTissue").prop('checked')) {
            $("#isFromPatient").attr("disabled", false);
            $("#isFromPatientTissue").attr("disabled", false);
            $("#isFromDonor").attr("disabled", true);
            $("#isFromDonorTissue").attr("disabled", true);
        } else if ($("#isFromDonor").prop('checked') || $("#isFromDonorTissue").prop('checked')) {
            $("#isFromPatient").attr("disabled", true);
            $("#isFromPatientTissue").attr("disabled", true);
            $("#isFromDonor").attr("disabled", false);
            $("#isFromDonorTissue").attr("disabled", false);
        } else {
            $("#isFromPatient").attr("disabled", false);
            $("#isFromPatientTissue").attr("disabled", false);
            $("#isFromDonor").attr("disabled", false);
            $("#isFromDonorTissue").attr("disabled", false);
        }
    });

    $('input[type="number"]').change(function () {
        var totalNum = 0;
        $('input[type="number"]').each(function () {
            let val = $(this).val();
            if (val) {
                var intNum = parseInt(val);
                if (intNum) {
                    totalNum += intNum;
                }
            }
        })
        $('#totalRetrievedNum').html(totalNum);
    });

    $('input[type="checkbox"]').trigger("change");
});