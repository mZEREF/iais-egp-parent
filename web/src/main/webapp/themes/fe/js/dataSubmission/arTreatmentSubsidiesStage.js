$(document).ready(function () {
    let freshCount = $('input[name="freshCount"]').val();
    let frozenCount = $('input[name="frozenCount"]').val();
    let $radio = $('input[name="coFunding"]');
    $radio.change(function () {
        let $isThereAppealRow = $('#isThereAppealRow');
        let val = $('input[name="coFunding"]:checked').val();
        console.log(freshCount + "    " + frozenCount);
        if (val == "ATSACF002" && freshCount >= 3) {
            $isThereAppealRow.show();
        } else if (val == "ATSACF003" && frozenCount >= 3) {
            $isThereAppealRow.show();
        } else {
            $isThereAppealRow.hide();
        }
    });
    $radio.trigger("change");
});