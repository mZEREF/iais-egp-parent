$(document).ready(function () {
    var $inactionModal = $("#inactionModal");
    if ($inactionModal.length > 0) {
        $inactionModal.modal('show');
        $inactionModal.click(function () {
            $inactionModal.modal('hide');
            submit('return');
        })
    }
    var $hasConfirmationModal = $("#hasConfirmationModal");
    if ($hasConfirmationModal.length > 0) {
        $hasConfirmationModal.modal('show');
        $hasConfirmationModal.click(function () {
            $inactionModal.modal('hide');
            submit('return');
        })
    }
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

function mySubmit(action){
    console.log("submit " + action);
    $("[name='crud_type']").val(action);
    var mainForm = document.getElementById('mainForm');
    showWaiting();
    mainForm.submit();
}