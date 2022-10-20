$(function () {
    $("input[name='ptHasIdNumber']").change(function() {
        showPatientIdentify();
    }).trigger('change');

    $("input[name='identityNo']").change(function () {
    }).trigger('change');

    $('input[name="existedPatient"]').change(function () {
        showAmendPatientSection();
        showNewPatientSection();
        showNextBtn();
    }).trigger('change');

    $('input[name="hasCycle"]').change(function () {
        showCycleRadioRow();
        showNextStageRow();
    }).trigger('change');

    $('input[name="previousIdentification"]').change(showPreviousPatientSection).trigger('change');

    $("input[name='previousIdentification']").change(showHusDiv).trigger('change');

    $("input[name='hubHasIdNumber']").change(function () {
        showHubContentDiv();
        showHubNumberField();
    }).trigger('change');

    $(function(){
        $("#identityNo").bind('input porpertychange',function(){
            $("#registerPatientSection").hide();
            $("#amendPatientSection").hide();
        });
    });

    $(document).ready(function () {
        $('input[type=radio][name=ptHasIdNumber]').change(function() {
            $("#registerPatientSection").hide();
            $("#amendPatientSection").hide();
        });
    });

    $("#validatePAT").click(function () {
        let isPatHasId = $("input[name='ptHasIdNumber']:checked").val();
        let identityNo = $("#identityNo").val();
        clearErrorMsg();
        if (identityNo == "") {
            $("#error_identityNo").html("This is a mandatory field.")
            return
        }
        $('input[name="existedPatient"]').val(null).trigger('change');
        validatePatient(isPatHasId, identityNo);
    });

    $("#pt-amend").click(function () {
        submit("amend");
    });

    if ($('input[name="existedPatient"]').val() === 'Y'){
        validatePatient($("input[name='ptHasIdNumber']:checked").val(), $("#identityNo").val())
    }
})

function validatePatient(isPatHasId, identityNo) {
    let centreSel = $('#centreSel option:selected').val();
    if (!centreSel){
        centreSel = $('#centreSel').val();
    }
    clearErrorMsg();
    if (!centreSel) {
        $("#error_noArLicences").html("This is a mandatory field.")
        return
    }
    $('#newCycleRadio').prevUntil("#cycleRadioStart").remove();
    showWaiting();
    $.ajax({
        url: $('#_contextPath').val() + '/ar/validate-patient-info',
        dataType: 'json',
        data: {
            "isPatHasId": isPatHasId,
            "identityNo": identityNo,
            "centreSel": centreSel
        },
        type: 'POST',
        success: function (data) {
            $("#lastStatus").val(data.lastStatus)
            if (data.needShowError) {
                $('input[name="existedPatient"]').val('N').trigger('change');
                // TODO
            } else {
                $('input[name="existedPatient"]').val(data.registeredPT ? 'Y' : 'N').trigger('change');
                if (data.registeredPT) {
                    const $registeredTRTDetail = $("#registeredTRTDetail");
                    $('#ptName').html(data.ptName);
                    $('#ptBirth').html(data.ptBirth);
                    $('#ptNat').html(data.ptNat);
                    $('#ptEth').html(data.ptEth);
                    if (data.ptPreId) {
                        $registeredTRTDetail.show();
                        $('#ptPreId').html(data.ptPreId);
                        $('#ptPreName').html(data.ptPreName);
                        $('#ptPreNat').html(data.ptPreNat);
                    } else {
                        $registeredTRTDetail.hide();
                    }
                    $('#husName').html(data.husName);
                    $('#husBirth').html(data.husBirth);
                    $('#husNat').html(data.husNat);
                    $('#husEth').html(data.husEth);

                    if (data.cycles && data.cycles.length > 0) {
                        $('#ptNameTitle').html(data.ptName);
                        $('#patIdNoTitle').html(identityNo);
                        for (let i = 0; i < data.cycles.length; i++) {
                            let cycle = data.cycles[i];
                            const radioHtml = `
                                <div class="form-check col-xs-12" style="padding: 0;">
                                    <input class="form-check-input" id="cycleRadio${i}" type="radio" name="cycleRadio" value="${cycle.cycleId}">
                                    <label class="form-check-label" for="cycleRadio${i}">
                                        <span class="check-circle"></span>[${cycle.displayType}] Submission ID ${cycle.displaySubmissionNo}
                                    </label>
                                </div>`;
                            $("#newCycleRadio").before(radioHtml)
                        }
                        $('input[name="hasCycle"]').val('Y').trigger('change');
                    } else {
                        $('input[name="hasCycle"]').val('N').trigger('change');
                    }
                }
            }
            dismissWaiting();
        },
        error: function () {
            console.log("err");
            dismissWaiting();
        }
    });
}

function showPatientIdentify() {
    const hasIdNumberVal = $("input[name='ptHasIdNumber']:checked").val();
    const indicateIdentitySection = $("#indicateIdentitySection");
    const idIdentify = $("#idIdentify");
    const passportIdentify = $("#passportIdentify");

    indicateIdentitySection.show();
    passportIdentify.hide();
    idIdentify.hide();
    if (hasIdNumberVal === '1') {
        idIdentify.show();
    } else if (hasIdNumberVal === '0') {
        passportIdentify.show();
    } else {
        indicateIdentitySection.hide();
    }
}

function showAmendPatientSection() {
    const existedPatientVal = $('input[name="existedPatient"]').val();
    const amendPatientSection = $("#amendPatientSection");

    if (existedPatientVal === 'Y') {
        amendPatientSection.show();
    } else {
        amendPatientSection.hide();
        clearFields(amendPatientSection);
    }
}

function showNewPatientSection() {
    const existedPatientVal = $('input[name="existedPatient"]').val();
    const registerPatientSection = $("#registerPatientSection");

    if (existedPatientVal === 'N') {
        registerPatientSection.show();
    } else {
        registerPatientSection.hide();
        clearFields(registerPatientSection);
        showPreviousPatientSection();
        showHusDiv();
    }
}

function showCycleRadioRow() {
    const hasCycleVal = $('input[name="hasCycle"]').val();
    const cycleRadioRow = $('#cycleRadioRow');
    const cycleRadio = $("input[name='cycleRadio']");

    if (hasCycleVal === 'Y') {
        cycleRadioRow.show();
        cycleRadio.change(showNextStageRow)
    } else {
        cycleRadioRow.hide();
        clearFields(cycleRadioRow);
        cycleRadio.unbind("change")
    }
}

function showNextStageRow() {
    const cycleRadioVal = $("input[name='cycleRadio']:checked").val();
    const nextStageRow = $('#nextStageRow');
    const nextOffStageRow = $('#nextOffStageRow');
    const lastStatus = $('input[name="lastStatus"]').val();
    const hasCycleVal = $('input[name="hasCycle"]').val();
    if ((cycleRadioVal === 'newCycle' && (lastStatus === 'DS003' || lastStatus==='DS005' ||lastStatus==='DS006'|| lastStatus==='DS007' || lastStatus==='DS016')) || hasCycleVal === 'N') {
        nextStageRow.show();
        nextOffStageRow.hide();
    } else if (cycleRadioVal === 'newCycle') {
        nextOffStageRow.show();
        nextStageRow.hide();
    } else {
        nextOffStageRow.hide();
        nextStageRow.hide();
        clearFields(nextStageRow);
        clearFields(nextOffStageRow);
    }

}

function showPreviousPatientSection() {
    const ptPreviousIdentificationVal = $("input[name='previousIdentification']:checked").val();
    const previousPatientSection = $('#previousPatientSection');

    if (ptPreviousIdentificationVal === '1') {
        previousPatientSection.show();
    } else {
        previousPatientSection.hide();
        clearFields(previousPatientSection);
    }
}

function showHusDiv() {
    const ptPreviousIdentificationVal = $("input[name='previousIdentification']:checked").val();
    const husDiv = $('#husDiv');

    if (!isEmpty(ptPreviousIdentificationVal)) {
        husDiv.show();
    } else {
        husDiv.hide();
        clearFields(husDiv);
        showHubContentDiv();
    }
}

function showHubContentDiv() {
    const hubHasIdNumberVal = $("input[name='hubHasIdNumber']:checked").val();
    const hubContentDiv = $('#hubContentDiv');

    if (!isEmpty(hubHasIdNumberVal)) {
        hubContentDiv.show();
    } else {
        hubContentDiv.hide();
        clearFields(hubContentDiv);
    }
}

function showHubNumberField() {
    const hubHasIdNumberVal = $("input[name='hubHasIdNumber']:checked").val();
    const hubNricField = $('#hubNricField');
    const hubPassportField = $('#hubPassportField');

    hubNricField.hide();
    hubPassportField.hide();
    if (hubHasIdNumberVal === '1'){
        hubNricField.show();
    } else if (hubHasIdNumberVal === '0'){
        hubPassportField.show();
    }
}