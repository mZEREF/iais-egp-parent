$(document).ready(function () {
    let $sameAmount = $('input[name="sameAmount"]');
    $sameAmount.each(function () {
        let row = $(this).closest(".form-group");
        let input = row.find("input[type='text']");
        input.attr('readonly', $(this).prop("checked"))
    })

    $sameAmount.change(function () {
        let row = $(this).closest(".form-group");
        let input = row.find("input[type='text']");
        input.attr('readonly', $(this).prop("checked"))
        if ($(this).prop("checked")){
            input.val(row.find("input[type='hidden']").val())
        }
    })

});