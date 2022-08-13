$(function (){
    $("input[name='submissionMethod']").change(function (){
        let checkedSelector = $("input[name='submissionMethod']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;
        let $donorSampleSection = $("#donorSampleSection");
        let $formEntrySection = $("#formEntrySection");

        if(val === 'DS_MTD001' && isChecked){
            $formEntrySection.show();
            $donorSampleSection.hide();
            reloadSection($donorSampleSection);
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
              reloadSection($indicateIdentitySection);
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
      }
    });


    $("#validatePAT").click(function (){
        let isPatHasId = $("input[name='hasIdNumber']:checked").val();
        let identityNo = $("#identityNo").val();

        let identityType = (isPatHasId === 'Y'? 'NRICORFIN':'PASSPORT');
        if(isEmpty(isPatHasId)){
            identityType = "";
        }

        validatePatient(identityType,identityNo);
    });

    function validatePatient(isPatHasId, identityNo) {
        showWaiting();
        $.ajax({
            url: $('#_contextPath').val() + '/ar/validate-patient-info',
            dataType: 'json',
            data: {
                "isPatHasId":isPatHasId,
                "identityNo":identityNo
            },
            type: 'POST',
            success: function (data) {
                let $registerPatientSection = $("#registerPatientSection");
                let $amendPatientSection = $("#amendPatientSection");
                if(data.needShowError){
                    $registerPatientSection.hide();
                    $amendPatientSection.hide();
                }else{
                    $registerPatientSection.hide();
                    $amendPatientSection.hide();
                    if(data.registeredPT){
                        $registerPatientSection.hide();
                        $amendPatientSection.show();


                        let $registeredPTDetail = $("#registeredPTDetail");
                        let $registeredTRTDetail = $("#registeredTRTDetail");
                        let $registeredHBDetail = $("#registeredHBDetail");
                        $registeredTRTDetail.html(data.preArPatient);
                        $registeredHBDetail.html(data.arHusband);
                        $registeredPTDetail.html(data.arPatient);

                        $registeredPTDetail.show();
                        if(isEmpty(data.preArPatient)){
                            $registeredTRTDetail.css("display","none");
                        }else{
                            $registeredTRTDetail.css("display","block");
                        }
                        if(isEmpty(data.arHusband)){
                            $registeredHBDetail.css("display","none");
                        }else{
                            $registeredHBDetail.css("display","block");
                        }
                    }else{
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

    $("input[name ='previousIdentificationWIFE']").change(function (){
        let checkedSelector = $("input[name='previousIdentificationWIFE']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;
        let previousPatientSection = $("#previousPatientSection");

        if("1" === val && isChecked){
            previousPatientSection.show();
        } else if("0" === val && isChecked){
            previousPatientSection.hide();
        } else{
            previousPatientSection.hide();
        }
    });

    $("input[name ='hbHasIdNumber']").change(function (){
        let checkedSelector = $("input[name='hbHasIdNumber']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;
        let hbIdNumberSection = $("#hbIdNumberSection");
        let hbNoNumberSection = $("#hbNoNumberSection");
        let hbIdTypeSection = $("#hbIdTypeSection");

        if(val === 'Y' && isChecked){
            hbIdNumberSection.show();
            hbNoNumberSection.hide();
            hbIdTypeSection.show();
        } else if(val === 'N' && isChecked){
            hbIdNumberSection.hide();
            hbNoNumberSection.show();
            hbIdTypeSection.hide();
        } else{
            hbIdNumberSection.hide();
            hbNoNumberSection.hide();
            hbIdTypeSection.hide();
        }
    });

    // donated sample local or overseas
    $("input[name = 'sampleRoot']").change(function (){
        let checkedSelector = $("input[name='sampleRoot']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;
        let localSelector = $("#localDonateSection");
        let sampleRootSelector = $("#sampleRootSection");
        let overseasSelector = $("#overseasDonateSection");

        if(val === 'local' && isChecked){
            sampleRootSelector.show();
            localSelector.show();
            overseasSelector.hide();
            reloadSection(overseasSelector);
        }else if(val === 'overseas' && isChecked){
            sampleRootSelector.show();
            localSelector.hide();
            overseasSelector.show();
            reloadSection(localSelector);
        }else{
            sampleRootSelector.hide();
            localSelector.hide();
            overseasSelector.hide();
        }
    });


    // sample type
    //DONTY001,DONTY002 ==> female
    //DONTY004 ==> male
    //DONTY003 ==> both
    $("#sampleType").change(function (){
        let val = $("#sampleType").val();
        let femaleSelector = $("#femaleSampleType");
        let maleSelector = $("#maleSampleType");

        switch (val){
            case "DONTY001":
            case "DONTY002":
                femaleSelector.show();
                maleSelector.hide();
                reloadSection(maleSelector);
                break;
            case "DONTY003":
                femaleSelector.show();
                maleSelector.show();
                break;
            case "DONTY004":
                femaleSelector.hide();
                maleSelector.show();
                reloadSection(femaleSelector);
                break;
            default:
                femaleSelector.hide();
                maleSelector.hide();
                reloadSection(maleSelector);
                reloadSection(femaleSelector);
                break;
        }
    });


    $("input[name = 'isKnowIdentityF']").change(function (){
        let checkedSelector = $("input[name='isKnowIdentityF']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;
        let isKnowIdentityFSection = $("#isKnowIdentityFSection");


        if(val === 'N' && isChecked){
            isKnowIdentityFSection.hide();
            reloadSection(isKnowIdentityFSection);
        }else if(val === 'Y' && isChecked){
            isKnowIdentityFSection.show();
        }else{
            isKnowIdentityFSection.hide();
        }
    });

    $("input[name = 'hasIdNumberF']").change(function (){
       let noIdSectionF = $("#noIdSectionF");
       let hasIdSectionF = $("#hasIdSectionF");
       let checkedSelector = $("input[name='hasIdNumberF']:checked");
       let val = checkedSelector.val();
       let isChecked = checkedSelector.length;

       if(val === 'N' && isChecked){
           noIdSectionF.show();
           hasIdSectionF.hide();
           reloadSection(hasIdSectionF);
       }else if(val === 'Y' && isChecked){
           noIdSectionF.hide();
           hasIdSectionF.show();
           reloadSection(noIdSectionF);
       }else{
           noIdSectionF.hide();
           hasIdSectionF.hide();
       }
    });

    $("input[name = 'isKnowIdentityM']").change(function (){
        let checkedSelector = $("input[name='isKnowIdentityM']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;
        let isKnowIdentityMSection = $("#isKnowIdentityMSection");

        if(val === 'N' && isChecked){
            isKnowIdentityMSection.hide();
            reloadSection(isKnowIdentityMSection);
        }else if(val === 'Y' && isChecked){
            isKnowIdentityMSection.show();
        }else{
            isKnowIdentityMSection.hide();
        }
    });

    $("input[name = 'hasIdNumberM']").change(function (){
        let noIdSectionM = $("#noIdSectionM");
        let hasIdSectionM = $("#hasIdSectionM");
        let checkedSelector = $("input[name='hasIdNumberM']:checked");
        let val = checkedSelector.val();
        let isChecked = checkedSelector.length;

        if(val === 'N' && isChecked){
            noIdSectionM.show();
            hasIdSectionM.hide();
            reloadSection(hasIdSectionM);
        }else if(val === 'Y' && isChecked){
            noIdSectionM.hide();
            hasIdSectionM.show();
            reloadSection(noIdSectionM);
        }else{
            noIdSectionM.hide();
            hasIdSectionM.hide();
        }
    });

    $("#donPurposeResearch").change(function (){
        let isChecked = $("#donPurposeResearch").prop("checked");
        let dpResearchSection = $("#dpResearchSection");
       if(isChecked){
           dpResearchSection.show();
       }else{
           dpResearchSection.hide();
           reloadSection(dpResearchSection);
       }
    });

    $("#donPurposeTraining").change(function (){
        let isChecked = $("#donPurposeTraining").prop("checked");
        let dpTrainingSection = $("#dpTrainingSection");
        if(isChecked){
            dpTrainingSection.show();
        }else{
            dpTrainingSection.hide();
            reloadSection(dpTrainingSection);
        }
    });

    $("#donPurposeTreatment").change(function (){
        let isChecked = $("#donPurposeTreatment").prop("checked");
        let dpTreatmentSection = $("#dpTreatmentSection");
        if(isChecked){
            dpTreatmentSection.show();
        }else{
            dpTreatmentSection.hide();
            reloadSection(dpTreatmentSection);
        }
    });

    triggerAllEventOnce();
})


function reloadSection(selector){
    $(selector).find("input[type = 'radio']").prop("checked",false).change();
    $(selector).find("input[type = 'text']").val("");
    $(selector).find("input[type = 'checkbox']").prop("checked",false).change();
    $(selector).find('select').val(null).change();
    $(selector).find('select').niceSelect("update");
}


function triggerAllEventOnce(){
    $("input[name='submissionMethod']").trigger('change');
    $("input[name = 'submissionType']").trigger('change');
    $("input[name='hasIdNumber']").trigger('change');
    $("input[name = 'sampleRoot']").trigger('change');
    $("#sampleType").trigger('change');
    $("input[name = 'isKnowIdentityF']").trigger('change');
    $("input[name = 'hasIdNumberF']").trigger('change');
    $("input[name = 'isKnowIdentityM']").trigger('change');
    $("input[name = 'hasIdNumberM']").trigger('change');
    $("#donPurposeResearch").trigger('change');
    $("#donPurposeTraining").trigger('change');
    $("#donPurposeTreatment").trigger('change');
    $("#validatePAT").trigger('click');
    $("input[name ='previousIdentificationWIFE']").trigger('change');
    $("input[name ='hbHasIdNumber']").trigger('change');
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

