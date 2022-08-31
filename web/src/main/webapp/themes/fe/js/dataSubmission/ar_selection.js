$(function (){
    bindButton();

    $("input[name='submissionMethod']").change(function (){
        let checkedSelector = $("input[name='submissionMethod']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;
        let $donorSampleSection = $("#donorSampleSection");
        let $formEntrySection = $("#formEntrySection");

        if(val === 'DS_MTD001' && isChecked){
            $formEntrySection.show();
        }else if(val === 'DS_MTD002' && isChecked){
            $formEntrySection.hide();
            $donorSampleSection.show();
            reloadSection($formEntrySection);
        }else{
            $formEntrySection.hide();
            $donorSampleSection.hide();
        }

    });

    // checkbox donor sample
    $("input[name='submissionType']").change(function (){
        let checkedSelector = $("input[name='submissionType']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;

        let sectionSelector = $("#donorSampleSection");
        let patientSection = $("#patientSection");

        if(val === 'AR_TP003' && isChecked){
            sectionSelector.show();
            patientSection.hide();
            reloadSection(patientSection);
        }else if(val === 'AR_TP001' && isChecked){
            patientSection.show();
            sectionSelector.hide();
            reloadSection(sectionSelector);
            reloadPATValidation();
        }else{
            sectionSelector.hide();
            patientSection.hide();
        }

    });

    //indicate patient section 1.id 2.passport
    $("input[name='hasIdNumber']").change(function (){
      let checkedSelector = $("input[name='hasIdNumber']:checked");
      let checkedNum = checkedSelector.length;
      let $passportIdentify = $("#passportIdentify");
      let $idIdentify = $("#idIdentify");
      let $indicateIdentitySection = $("#indicateIdentitySection");
      let $idTypeSection = $("#idTypeSection");
      if(checkedNum){
          let currentVal = checkedSelector.val();
          if(currentVal !== "" || currentVal !== null){
              $indicateIdentitySection.show();
              if(currentVal === 'Y'){
                  $passportIdentify.hide();
                  $idIdentify.show();
                  $idTypeSection.show();
                  reloadPATValidation();
              }else if(currentVal === 'N'){
                  $passportIdentify.show();
                  $idIdentify.hide();
                  $idTypeSection.hide();
                  reloadPATValidation();
              }else{
                  $passportIdentify.hide();
                  $idIdentify.hide();
                  $idTypeSection.hide();
              }
          }
      }else{
          $indicateIdentitySection.hide();
          reloadSection($indicateIdentitySection);
      }
    });

    $("#validatePAT").click(function (){
        let isPatHasId = $("input[name='hasIdNumber']:checked").val();
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

function reloadSection(selector){
    console.warn("reloadSection %o", selector)
    $(selector).find("input[type = 'radio']").prop("checked", false).change();
    $(selector).find("input[type = 'text']").val("").change();
    $(selector).find("input[type = 'checkbox']").prop("checked",false).change();
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
    $("#cycleRadioDiv").empty();
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
            let $registerPatientSection = $("#registerPatientSection");
            let $amendPatientSection = $("#amendPatientSection");
            if (data.needShowError) {
                $registerPatientSection.hide();
                $amendPatientSection.hide();
            } else {
                $registerPatientSection.hide();
                $amendPatientSection.hide();
                $("#registeredPatient").val(data.registeredPT ? 'Y' : 'N');
                if (data.registeredPT) {
                    $registerPatientSection.hide();
                    $amendPatientSection.show();


                    let $registeredPTDetail = $("#registeredPTDetail");
                    let $registeredTRTDetail = $("#registeredTRTDetail");
                    let $registeredHBDetail = $("#registeredHBDetail");
                    $registeredTRTDetail.html(data.preArPatient);
                    $registeredHBDetail.html(data.arHusband);
                    $registeredPTDetail.html(data.arPatient);

                    $registeredPTDetail.show();
                    if (isEmpty(data.preArPatient)) {
                        $registeredTRTDetail.css("display", "none");
                    } else {
                        $registeredTRTDetail.css("display", "block");
                    }
                    if (isEmpty(data.arHusband)) {
                        $registeredHBDetail.css("display", "none");
                    } else {
                        $registeredHBDetail.css("display", "block");
                    }
                    $("#cycleRadioDiv").prepend(data.cycleRadio)
                    const $cycleRadio = $("input[name='cycleRadio']");
                    $cycleRadio.unbind("change")
                    $cycleRadio.change(function () {
                        const nextStageMap = JSON.parse(data.cycleNextStageMap)
                        const nextStages = nextStageMap[this.id]
                        const $nextStage = $("#nextStage");
                        $nextStage.empty();
                        $nextStage.append(nextStages)
                        $nextStage.niceSelect("update")
                    })
                } else {
                    $registerPatientSection.show();
                    $amendPatientSection.hide();
                }
            }

            dismissWaiting();
        },
        error: function (data) {
            console.log("err");
            dismissWaiting();
        }
    });
}

function triggerAllEventOnce() {
    $("input[name='submissionMethod']").trigger('change');
    $("input[name = 'submissionType']").trigger('change');
    $("input[name='hasIdNumber']").trigger('change');
    $("#validatePAT").trigger('click');
    $("input[name ='previousIdentification']").trigger('change');
    $("input[name ='hasIdNumberHbd']").trigger('change');
}

function reloadPATValidation(){
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
