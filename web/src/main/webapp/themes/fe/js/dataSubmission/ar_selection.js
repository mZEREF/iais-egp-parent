$(function () {
    bindButton();

    $("input[name='submissionMethod']").change(function () {
        showFormEntryDiv();
        showBatchUploadDiv();
    }).trigger('change');

    // checkbox donor sample
    $("input[name='submissionType']").change(function () {
        showDonorSampleDiv();
        showPatientDiv();
    }).trigger('change');

    //indicate patient section 1.id 2.passport
    $("input[name='ptHasIdNumber']").change(showPatientIdentify).trigger('change');

    $('input[name="registeredPatient"]').change(showAmendOrNewPatientSection).trigger('change');

    $('input[name="hasCycle"]').change(function (){
        showCycleRadioRow();
        showNextStageRow();
    }).trigger('change');

    $("#validatePAT").click(function () {
        let isPatHasId = $("input[name='ptHasIdNumber']:checked").val();
        let identityNo = $("#identityNo").val();

        let identityType = (isPatHasId === 'Y' ? 'NRICORFIN' : 'PASSPORT');
        if (isEmpty(isPatHasId)) {
            identityType = "";
        }

        validatePatient(identityType, identityNo);
    });

    $("input[name ='previousIdentification']").change(function () {
        let checkedSelector = $("input[name='previousIdentification']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;
        let previousPatientSection = $("#previousPatientSection");

        if ("1" === val && isChecked) {
            previousPatientSection.show();
        } else if ("0" === val && isChecked) {
            previousPatientSection.hide();
        } else {
            previousPatientSection.hide();
        }
    });

    $("input[name ='hasIdNumberHbd']").change(function () {
        let checkedSelector = $("input[name='hasIdNumberHbd']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;
        let hbIdNumberSection = $("#hbIdNumberSection");
        let hbNoNumberSection = $("#hbNoNumberSection");
        let hbIdTypeSection = $("#hbIdTypeSection");

        if (val === 'Y' && isChecked) {
            hbIdNumberSection.show();
            hbNoNumberSection.hide();
            hbIdTypeSection.show();
        } else if (val === 'N' && isChecked) {
            hbIdNumberSection.hide();
            hbNoNumberSection.show();
            hbIdTypeSection.hide();
        } else {
            hbIdNumberSection.hide();
            hbNoNumberSection.hide();
            hbIdTypeSection.hide();
        }
    });

    triggerAllEventOnce();

})

function reloadSection(selector) {
    $(selector).find("input[type = 'radio']").prop("checked", false).change();
    $(selector).find("input[type = 'text']").val("").change();
    $(selector).find("input[type = 'checkbox']").prop("checked", false).change();
    $(selector).find('select').val(null).change();
    $(selector).find('select').niceSelect("update");
}

function validatePatient(isPatHasId, identityNo) {
    let centreSel = $('#centreSel option:selected').val();
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
            if (data.needShowError) {
                $('input[name="registeredPatient"]').val('N').trigger('change');
            } else {
                $('input[name="registeredPatient"]').val(data.registeredPT ? 'Y' : 'N').trigger('change');
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

                    const $nextStage = $("#nextStage");
                    $nextStage.empty();
                    $nextStage.append(data.nextStageOptions);
                    $nextStage.niceSelect("update");

                    if (data.cycles && data.cycles.length > 0){
                        $('#ptNameTitle').html(data.ptName);
                        $('#patIdNoTitle').html(identityNo);
                        for (let i = 0; i < data.cycles.length; i++) {
                            let cycle = data.cycles[i];
                            const radioHtml = `
                                <div class="form-check col-xs-12" style="padding: 0;">
                                    <input class="form-check-input" id="cycleRadio${i}" type="radio" name="cycleRadio" value="${cycle.id}">
                                    <label class="form-check-label" for="cycleRadio${i}">
                                        <span class="check-circle"></span>[${cycle.type}] Submission ID ${cycle.no}
                                    </label>
                                </div>`;
                            $("#newCycleRadio").before(radioHtml)
                        }
                        $('input[name="hasCycle"]').val('Y').trigger('change');
                    } else {
                        $('#newCycleRadio').val('N').trigger('change');
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

function reloadPATValidation() {
    let $registeredPTDetail = $("#registeredPTDetail");
    let $registeredTRTDetail = $("#registeredTRTDetail");
    let $registeredHBDetail = $("#registeredHBDetail");

    $registeredPTDetail.find('*').remove();
    $registeredTRTDetail.find('*').remove();
    $registeredHBDetail.find('*').remove();

    $registeredPTDetail.hide();
    $registeredTRTDetail.hide();
    $registeredHBDetail.hide();
    $("#amendPatientSection").hide();
    $("#registerPatientSection").hide();
}

function submit(action, value, additional) {
    $("[name='crud_type']").val(action);
    $("[name='crud_action_value']").val(value);
    $("[name='crud_action_additional']").val(additional);
    var mainForm = document.getElementById('mainForm');
    showWaiting();
    mainForm.submit();
}

function bindButton() {
    let currPage = $('input[name="currentStage"]').val();
    console.log('----- ' + currPage + ' -----');
    if (isEmpty(currPage)) {
        currPage = "";
    }
    if ($('#backBtn').length > 0) {
        $('#backBtn').click(function () {
            showWaiting();
            if ('confirm' === currPage) {
                submit('page');
            } else if ('ack' === currPage) {
                submit('back');
            } else {
                submit('return');
            }
        });
    }

    if ($('#saveDraftBtn').length > 0) {
        if ('ack' === currPage) {
            $('#saveDraftBtn').remove();
        } else {
            $('#saveDraftBtn').click(function () {
                showWaiting();
                submit('draft');
            });
        }
    }

    if ($('#nextBtn').length > 0) {
        $('#nextBtn').click(function () {
            showWaiting();
            if ('confirm' === currPage) {
                submit('submission');
            } else {
                submit('confirm');
            }
        });
    }
}

function jumpToInbox() {
    showWaiting();
    var token = $('input[name="OWASP_CSRFTOKEN"]').val();
    var url = "/main-web/eservice/INTERNET/MohInternetInbox";
    if (!isEmpty(token)) {
        url += '?OWASP_CSRFTOKEN=' + token;
    }
    document.location = url;
}

function showDraftModal() {
    const saveDraft = $('#saveDraft');
    if ($('#saveDraftSuccess').val() === 'success' && saveDraft.length > 0) {
        saveDraft.modal('show');
        setTimeout(function () {
            $('#saveDraftSuccess').val('');
        }, 2000);
    }
    // draft modal
    var $draft = $("#_draftModal");
    if ($draft.length > 0) {
        $draft.modal('show');
    }
}

function showFormEntryDiv() {
    const formEntryDiv = $('#formEntryDiv')
    const submissionMethodVal = $('input[name="submissionMethod"]:checked').val();
    if (submissionMethodVal === 'DS_MTD001') {
        formEntryDiv.show();
    } else {
        formEntryDiv.hide();
        clearFields(formEntryDiv);
        showDonorSampleDiv();
        showPatientDiv();
    }
}

function showBatchUploadDiv() {
    const batchUploadDiv = $('#batchUploadDiv')
    const submissionMethodVal = $('input[name="submissionMethod"]:checked').val();
    if (submissionMethodVal === 'DS_MTD002') {
        batchUploadDiv.show();
    } else {
        batchUploadDiv.hide();
        clearFields(batchUploadDiv);
    }
}

function showDonorSampleDiv() {
    const donorSampleDiv = $('#donorSampleDiv')
    const submissionTypeVal = $('input[name="submissionType"]:checked').val();
    if (submissionTypeVal === 'AR_TP003') {
        donorSampleDiv.show();
    } else {
        donorSampleDiv.hide();
        reloadSection(donorSampleDiv);
    }
}

function showPatientDiv() {
    const patientDiv = $('#patientDiv')
    const submissionTypeVal = $('input[name="submissionType"]:checked').val();
    if (submissionTypeVal === 'AR_TP001') {
        patientDiv.show();
    } else {
        patientDiv.hide();
        reloadSection(patientDiv);
    }
}

function showPatientIdentify() {
    const hasIdNumberVal = $("input[name='ptHasIdNumber']:checked").val();
    const indicateIdentitySection = $("#indicateIdentitySection");
    const idIdentify = $("#idIdentify");
    const passportIdentify = $("#passportIdentify");

    indicateIdentitySection.show();
    passportIdentify.hide();
    idIdentify.hide();
    if (hasIdNumberVal === 'Y') {
        idIdentify.show();
    } else if (hasIdNumberVal === 'N') {
        passportIdentify.show();
    } else {
        indicateIdentitySection.hide();
    }
}

function showAmendOrNewPatientSection() {
    const registeredPatientVal = $('input[name="registeredPatient"]').val();
    const amendPatientSection = $("#amendPatientSection");
    const registerPatientSection = $("#registerPatientSection");

    amendPatientSection.hide();
    registerPatientSection.hide();
    if (registeredPatientVal === 'Y'){
        amendPatientSection.show();
    } else if (registeredPatientVal === 'N'){
        registerPatientSection.show();
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

function showNextStageRow(){
    const cycleRadioVal = $("input[name='cycleRadio']:checked").val();
    const hasCycleVal = $('input[name="hasCycle"]').val();
    const nextStageRow = $('#nextStageRow');

    if (hasCycleVal === 'N' || cycleRadioVal === 'newCycle'){
        nextStageRow.show();
    } else {
        nextStageRow.hide();
        clearFields(nextStageRow);
    }
}
