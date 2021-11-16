$(document).ready(function () {
    let freshCount = $('input[name="freshCount"]').val();
    let frozenCount = $('input[name="frozenCount"]').val();
    $('input[name="coFunding"]').change(function () {
        let $isThereAppealRow = $('#isThereAppealRow');
        let val = $('input[name="coFunding"]:checked').val();
        console.log(freshCount + "    " + frozenCount);
        if (val == "Fresh" && freshCount >= 3) {
            $isThereAppealRow.show();
        } else if (val == "Frozen" && frozenCount >= 3) {
            $isThereAppealRow.show();
        } else {
            $isThereAppealRow.hide();
        }
    });
    $('input[name="coFunding"]').trigger("change");
});