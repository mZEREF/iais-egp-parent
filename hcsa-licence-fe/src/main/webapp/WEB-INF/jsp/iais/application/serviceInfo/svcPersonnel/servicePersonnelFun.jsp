<script type="text/javascript">
    var init = 0;
    $(document).ready(function () {
        //Binding method
        $('#governanceOfficersBack').click(function () {
            submitForms('laboratoryDisciplines', null, null, 'clinical');
        });
        $('#governanceOfficersSaveDraft').click(function () {
            submitForms('governanceOfficers', 'saveDraft', null, 'clinical');
        });
        $('#governanceOfficersNext').click(function () {
            var controlFormLi = $('#controlFormLi').val();
            submitForms('nuclearMedicineImaging', null, null, controlFormLi);
        });
        if ($('#PRS_SERVICE_DOWN_INPUT').val() == 'PRS_SERVICE_DOWN') {
            $('#PRS_SERVICE_DOWN').modal('show');
        }
        pageController('');
        let flag = $("#curr").val();
        if (flag == 'NMI' || 'NMA' == flag) {
            $('.personnel-content').each(function (k, v) {
                var personnelSel = $(this).find('.personnelType').val();
                console.log(personnelSel,'personnlellll====>>>')
                personnelSelFun(personnelSel, $(v));
            });
        }
        initPage($('div.contents'))
        $('input[name="prsLoading"]').each(function () {
            if ($(this).val() == 'true') {
                var $currContent = $(this).closest('.personnel-content');
                inputReadonly($currContent.find('.name'));
            }
        });
        init = 1;
        fileUploadEvent()
        designationChange()
        profRegNoEvent($('.personnel-content'));
        otherSpecialEvents($('.personnel-content'));
        removePersonEvent();

        //  RFC
        let appType = $('input[name="applicationType"]').val();
        console.log(appType,'${isRfi}')
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            disableContent($('.personnel-content'));
        </c:if>
        <c:if test="${isRfi}">
            disableContent($('.personnel-content'));
        </c:if>

        let svcContent = '.personnel-content';
        psnEditEvent(svcContent);
        <c:if test="${AppSubmissionDto.needEditController}">
        $(svcContent).each(function (k,v) {
            if ($("#errorMapIs").val() == 'error') {
                $(v).find('.error-msg').on('DOMNodeInserted', function () {
                    if ($(v).not(':empty')) {
                        $(v).find('.isPartEdit').val(1);
                        $('#isEditHiddenVal').val('1');
                        unDisableContent($(v))
                    }
                });
            }
        });
        </c:if>
    });

    function initPage(target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.each(function (k, v) {
            if ($(v).find('div.personnel-content').length == 1) {
                $(v).find('.assign-psn-item').html('');
            }
            controlCountEvent($(v), true)
            $(v).find('div.personnel-content').each(function (i, x) {
                var flag = isEmpty($(x).find('input.profRegNo').val()) ? false : true;
                disablePersonnel($(x), flag, true);
            })
        });
    }

    function disablePersonnel($currContent, flag, needControlName) {
        if (flag) {
            disableContent($currContent.find('.specialtyGetDate'));
            disableContent($currContent.find('.typeOfCurrRegi'));
            disableContent($currContent.find('.currRegiDate'));
            disableContent($currContent.find('.praCerEndDate'));
            disableContent($currContent.find('.typeOfRegister'));
            if (needControlName) {
                var name = $currContent.find('.name').val();
                if (!isEmpty(name)) {
                    disableContent($currContent.find('.name'));
                }
            }
        } else {
            unDisableContent($currContent.find('.specialtyGetDate'));
            unDisableContent($currContent.find('.typeOfCurrRegi'));
            unDisableContent($currContent.find('.currRegiDate'));
            unDisableContent($currContent.find('.praCerEndDate'));
            unDisableContent($currContent.find('.typeOfRegister'));
            if (needControlName) {
                unDisableContent($currContent.find('.name'));
            }
        }
    }

    var designationChange = function () {
        $('.designation').unbind('change');
        $('.designation').change(function () {
            var thisVal = $(this).val();
            console.log('===========>',thisVal)
            if("DES999" == thisVal || "Others" == thisVal){
                $(this).closest('.personnel-content').find('.otherDesignationDiv').removeClass('hidden');
            }else{
                $(this).closest('.personnel-content').find('.otherDesignationDiv').addClass('hidden');
                $(this).closest('.personnel-content').find('.otherDesignation').val('')
            }
        });
    };

    var personnelSel = function () {
        $('.personnelType').change(function () {
            var personnelSel = $(this).val();
            var $personnelContentEle = $(this).closest('.personnel-content');
            var prsFlag = $('input[name="prsFlag"]').val();
            if (init != 0) {
                //clear data;
                console.log('clear data--->',init)
                $personnelContentEle.find('.personnel-designation .designation').val('');
                $personnelContentEle.find('.personnel-name .name').val('');
                $personnelContentEle.find('.personnel-regnNo .profRegNo').val('');
                $personnelContentEle.find('.personnel-wrkExpYear .wrkExpYear').val('');
                $personnelContentEle.find('.personnel-qualification .qualification').val('');
                $personnelContentEle.find('.otherDesignationDiv .otherDesignation').val('');
                if ('Y' == prsFlag) {
                    inputCancelReadonly($personnelContentEle.find('.name'));
                }
            }
            personnelSelFun(personnelSel, $personnelContentEle);
        });
    };

    var personnelSelFun = function (personnelSel, $personnelContentEle) {
        var prsFlag = $('input[name="prsFlag"]').val();
        console.log(prsFlag,'=======================>>>no')
        if ('SPPT001' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').removeClass('hidden');
            $personnelContentEle.find('.personnel-name').removeClass('hidden');
            $personnelContentEle.find('.personnel-qualification').removeClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').removeClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
            if ('Y' == prsFlag) {
                inputCancelReadonly($personnelContentEle.find('.name'));
            }
        } else if ('SPPT002' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').addClass('hidden');
            $personnelContentEle.find('.personnel-name').removeClass('hidden');
            $personnelContentEle.find('.personnel-qualification').removeClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').removeClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
            if ('Y' == prsFlag) {
                inputCancelReadonly($personnelContentEle.find('.name'));
            }
        } else if ('SPPT003' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').addClass('hidden');
            $personnelContentEle.find('.personnel-name').removeClass('hidden');
            $personnelContentEle.find('.personnel-qualification').addClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
            if ('Y' == prsFlag) {
                inputCancelReadonly($personnelContentEle.find('.name'));
            }
        } else if ('SPPT004' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').addClass('hidden');
            $personnelContentEle.find('.personnel-name').removeClass('hidden');
            $personnelContentEle.find('.personnel-qualification').addClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').removeClass('hidden');
            if ('Y' == prsFlag) {
                inputReadonly($personnelContentEle.find('.name'));
            }
        } else if ('' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').addClass('hidden');
            $personnelContentEle.find('.personnel-name').addClass('hidden');
            $personnelContentEle.find('.personnel-qualification').addClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
            if ('Y' == prsFlag) {
                inputCancelReadonly($personnelContentEle.find('.name'));
            }
        }
    //    hidden otherDesignationDiv  clear designation
        clearErrorMsg($personnelContentEle)
        if (init != 0) {
            clearFields($personnelContentEle.find('.designation'))
            $personnelContentEle.find('.otherDesignationDiv').addClass('hidden');
        }
    }
    //common
    function addPersonnels(target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        showWaiting();
        var $tgt = $(target).find('div.personnel-content').last();
        var locateWtihNonHcsa = $tgt.find('input.locateWtihNonHcsa:checked').val();
        var src = $tgt.clone();
        clearFields(src)
        $tgt.after(src);
        fillValue($tgt.find('input.locateWtihNonHcsa'), locateWtihNonHcsa);
        var $currContent = $(target).find('div.personnel-content').last();
        pageController($currContent)
        $currContent.find('.date_picker').datepicker({
            format: "dd/mm/yyyy",
            autoclose: true,
            todayHighlight: true,
            orientation: 'bottom'
        });
        // clearFields($currContent);
        $currContent.find('.speciality').html('');
        $currContent.find('.subSpeciality').html('');
        $currContent.find('.othersubSpeciality').html('');
        $currContent.find('.qualification').html('');
        $currContent.find('.otherDesignationDiv').addClass('hidden')
        $currContent.find('.SpecialtyGetDate .mandatory').remove();

        $currContent.find('.isPartEdit').val(1)
        $('.personnel-content-edit').val(1)

        unDisableContent($currContent)
        let length = $(target).find('div.personnel-content').length;
        $(target).find('.AR p').html(length)
        //
        refreshIndex($currContent, length - 1);
        $(target).find('div.personnel-content').first().find('.assign-psn-item').html('1');
        disablePersonnel($currContent, false, true);
        controlCountEvent($target);
        removePersonEvent();
        profRegNoEvent($currContent);
        otherSpecialEvents($currContent);
        designationChange()
        dismissWaiting();
    }

    //  TODO
    function controlCountEvent($target) {
        var psnLength = $target.find('div.personnel-content').length;
        let count = $target.find('.maxCount').val();
            if (psnLength >= count) {
                $target.find('.addDpoDiv').addClass('hidden');
            } else
                $target.find('.addDpoDiv').removeClass('hidden');
    }

    function refreshIndex($target, k) {
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.assign-psn-item').html(k + 1);
        resetIndex($target, k);
    }

    var removePersonEvent = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').on('click', function () {
            var $Content = $(this).closest('div.contents');
            $(this).closest('div.personnel-content').remove();
            controlCountEvent($Content)
            let length = $Content.find('div.personnel-content').length;
            $Content.find('.AR p').html(length)

            let $currContent = $Content.find('div.personnel-content');
            $currContent.each(function (k, v) {
                refreshIndex($(v), k);
            });
            if ($currContent.length == 1) {
                $currContent.find('.assign-psn-item').html('');
            }
            $('.personnel-content-edit').val(1)
        });
    }

    $('.addListBtn').click(function () {
        addPersonnels($(this).closest('div.contents'));
    });



    var pageController = function ($Ele) {
        let flag = $("#curr").val();
        // NMI
        if (flag == 'NMI' || flag == 'NMA') {
            console.log("begin---->",init)
            personnelSel();
            if ($Ele == '') {
                //triggering event
                $('.personnelType').trigger('change');
            } else {
                $Ele.find('.personnelType').trigger('change');
            }
        }
    }
    function inputReadonly($content) {
        $content.prop('readonly', true);
        $content.css('border-color', '#ededed');
        $content.css('color', '#999');
    }

    function inputCancelReadonly($content) {
        $content.prop('readonly', false);
        $content.css('border-color', '');
        $content.css('color', '');
    }

    function cancel() {
        $('#PRS_SERVICE_DOWN').modal('hide');
    }

    var fileUploadEvent = function () {
        $('.file-upload').unbind('click');
        $('.file-upload').click(function () {
            var uploadKey = "uploadFile";
            clearFlagValueFEFile();
            $('#selectFileDiv').html('<input id="' + uploadKey + '" class="selectedFile" name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="ajaxCallUpload(\'mainForm\',\'' + uploadKey + '\');" aria-label="selectedFile">');
            $('input[type="file"]').click();
        });
    }

    function initUploadFileData() {
        $('#_needReUpload').val(0);
        $('#_fileType').val("XLSX");
        $('#_singleUpload').val("1");
    }

    function doActionAfterUploading(data, fileAppendId) {
        fillNurse($("#" + fileAppendId + "ShowId").closest('div.contents'), data.appSvcPersonnelDto);
        if (data.msgType == 'Y') {
            $('#' + fileAppendId + 'ShowId').empty();
        }
    }

    function fillNurse($premContent, data) {
        $($premContent).find('.personnel-content:not(:first)').remove();
        if (isEmpty(data) || !$.isArray(data)) {
            clearFields($('.personnel-content'));
            return;
        }
        clearFields($premContent)
        var len = data.length;
        console.log(data,'data========>>>>')
        for (var i = 0; i < len; i++) {
            let $target = $premContent.find('.personnel-content').eq(i);
            if (isEmptyNode($target)) {
                //   TODO
                addPersonnels($premContent)
                $target = $premContent.find('.personnel-content').eq(i);
            }
            fillFormData($target, data[i], 'SP003', i)
            let profRegNo = $target.find('.profRegNo').val()
            let designation = $target.find('.designation').val()
            unDisableContent($target)
            if (!isEmpty(profRegNo)){
                $target.find('.profRegNo').trigger('blur')
            }
            if (!isEmpty(designation)){
                $target.find('.designation').trigger('change')
            }
            let maxCount = '${nuPersonnelMax}';
            controlCountEvent($($premContent))
            if (i >= maxCount-1){
                break;
            }
        }
    }

    var profRegNoEvent = function (target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.profRegNo').unbind('blur');
        $target.find('.profRegNo').on('blur', function () {
            showWaiting();
            var prgNo = $(this).val();
            var $currContent = $(this).closest(target);
            var assignSelectVal = $currContent.find('.assignSelVal').val();
            var appType = $('input[name="applicationType"]').val();
            var licPerson = $currContent.find('input.licPerson').val();
            var specialPerson = $currContent.find('input.specialPerson').val();
            var needControlName = isNeedControlName(assignSelectVal, licPerson, appType,specialPerson);
            checkProfRegNo($currContent, prgNo, needControlName);
        });
    };

    function checkProfRegNo($currContent, prgNo, needControlName) {
        showWaiting();
        let callback = getPrsCallback();
        if (isEmpty(prgNo)) {
            if (typeof callback === 'function') {
                callback($currContent, null);
            } else {
                fillPrsInfo($currContent, null, needControlName);
                disablePrsInfo($currContent, false, needControlName);
            }
            dismissWaiting();
            return;
        }
        var jsonData = {
            'prgNo': prgNo
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                var canFill = false;
                if (isEmpty(data)) {
                    console.log("The return data is null for PRS");
                } else if ('-1' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else if ('-2' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else if (data.hasException) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0048" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else if ('401' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0054" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                } else {
                    canFill = true;
                }
                if (typeof callback === 'function') {
                    callback($currContent, canFill ? data : null);
                } else {
                    fillPrsInfo($currContent, canFill ? data : null, needControlName);
                    disablePrsInfo($currContent, canFill, needControlName);
                }
                dismissWaiting();
            },
            'error': function () {
                if (typeof callback === 'function') {
                    callback($currContent, null);
                } else {
                    fillPrsInfo($currContent, null, needControlName);
                    disablePrsInfo($currContent, false, needControlName);
                }
                dismissWaiting();
            }
        });
    }

    function fillPrsInfo($currContent, data, needControlName) {
        console.log(data,'data===============>>>>>')
        var name = '';
        var specialty = '';
        var subspecialty = '';
        var qualification = '';
        var specialtyGetDate = '';
        var typeOfCurrRegi = '';
        var currRegiDate = '';
        var praCerEndDate = '';
        var typeOfRegister = '';
        if (!isEmpty(data)) {
            if (!isEmpty(data.name)) {
                name = data.name;
            }
            if (!isEmpty(data.specialty)) {
                specialty = data.specialty;
            }
            if (!isEmpty(data.subspecialty)) {
                subspecialty = data.subspecialty;
            }
            if (!isEmpty(data.qualification)) {
                qualification = data.qualification;
            }
            if (!isEmpty(data.entryDateSpecialist)) {
                specialtyGetDate = data.entryDateSpecialist[0];
            }
            if (!isEmpty(data.registration)) {
                var registration = data.registration[0];
                typeOfCurrRegi = registration['Registration Type'];
                currRegiDate = registration['Registration Start Date'];
                praCerEndDate = registration['PC End Date'];
                typeOfRegister = registration['Register Type'];
            }
        }
        if (needControlName) {
            $currContent.find('.name').val(name);
        }
        $currContent.find('.speciality p').html(specialty);
        $currContent.find('.subSpeciality p').html(subspecialty);
        if (!isEmpty(data)) {
            let length = data.specialty.length;
            let condation = false;
            for (let i = 0; i < length; i++) {
                if (isEmpty(data.specialty[i])) {
                    continue;
                }
                condation = true;
                if (condation) {
                    break;
                }
            }
            let lengths = data.specialty.length;
            let condations = false;
            for (let i = 0; i < lengths; i++) {
                if (isEmpty(data.subspecialty[i])) {
                    continue;
                }
                condations = true;
                if (condations) {
                    break;
                }
            }
            let ar = data && condation;
            let content = $currContent.find('.nurse').val();
            let nur = data && content && (condation || condations)
            console.log(ar, '============ar=====>', nur, '-------------------<---------------')

            if (ar || nur) {
                $currContent.find('.SpecialtyGetDate .mandatory').remove();
                $currContent.find('.SpecialtyGetDate').append('<span class="mandatory">*</span>');
            }else {
                $currContent.find('.SpecialtyGetDate .mandatory').remove();
            }
        }else {
            $currContent.find('.SpecialtyGetDate .mandatory').remove();
        }

        $currContent.find('.qualification p').html(qualification);
        $currContent.find('.specialtyGetDate').val(specialtyGetDate);
        $currContent.find('.typeOfCurrRegi').val(typeOfCurrRegi);
        $currContent.find('.currRegiDate').val(currRegiDate);
        $currContent.find('.praCerEndDate').val(praCerEndDate);
        $currContent.find('.typeOfRegister').val(typeOfRegister);
    }

    function disablePrsInfo($currContent, flag, needControlName) {
        if (flag) {
            disableContent($currContent.find('.specialtyGetDate'));
            disableContent($currContent.find('.typeOfCurrRegi'));
            disableContent($currContent.find('.currRegiDate'));
            disableContent($currContent.find('.praCerEndDate'));
            disableContent($currContent.find('.typeOfRegister'));
            if (needControlName) {
                var name = $currContent.find('.name').val();
                if (!isEmpty(name)) {
                    disableContent($currContent.find('.name'));
                }
            }
        } else {
            unDisableContent($currContent.find('.specialtyGetDate'));
            unDisableContent($currContent.find('.typeOfCurrRegi'));
            unDisableContent($currContent.find('.currRegiDate'));
            unDisableContent($currContent.find('.praCerEndDate'));
            unDisableContent($currContent.find('.typeOfRegister'));
            if (needControlName) {
                unDisableContent($currContent.find('.name'));
            }
        }
    }


    let otherSpecialEvents = function (target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.nurseSpecial').unbind('blur');
        $target.find('.nurseSpecial').on('blur', function () {
            var content = $(this).val();
            var $currContent = $(this).closest(target);
            let speciality = $currContent.find('.speciality p').html();
            let subSpeciality = $currContent.find('.subSpeciality p').html();
            if (!isEmpty(content)){
                $currContent.find('.SpecialtyGetDate .mandatory').remove();
                $currContent.find('.SpecialtyGetDate').append('<span class="mandatory">*</span>');
            }else if (isEmpty(speciality) && isEmpty(subSpeciality)) {
                $currContent.find('.SpecialtyGetDate .mandatory').remove();
            }

        });
    };

</script>
