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
            let length = $('.personnel-content').length;
            if (length == 1) {
                $('.personnel-content').find('.assign-psn-item').html('')
            }
            $('.personnel-content').each(function (k, v) {
                var personnelSel = $(this).find('.personnelType').val();
                var $personnelContentEle = $(this);
                personnelSelFun(personnelSel, $personnelContentEle);
            });
        }
        spRemove();
        initPage($('div.panel-main-content'))
        $('input[name="prsLoading"]').each(function () {
            if ($(this).val() == 'true') {
                var $currContent = $(this).closest('.personnel-content');
                inputReadonly($currContent.find('.name'));
            }
        });
        if ("${errormapIs}" == 'error') {
            $('.svcPsnEdit').trigger('click');
        }
        init = 1;
        fileUploadEvent()
        designationChange()
        //no
        profRegNoEvent($('.personnel-content'));
        removePersonEvent();
        let target = $('.special-person')
        controlCountEvent($(target))
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
        $personnelContentEle.find('.new-svc-personnel-form ').removeClass('hidden');
        var prsFlag = $('input[name="prsFlag"]').val();
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
            inputCancelReadonly($personnelContentEle.find('.name'));
        } else if ('SPPT004' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').addClass('hidden');
            $personnelContentEle.find('.personnel-name').removeClass('hidden');
            $personnelContentEle.find('.personnel-qualification').addClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').removeClass('hidden');
            //regnNo.
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
        $tgt.after(src);
        fillValue($tgt.find('input.locateWtihNonHcsa'), locateWtihNonHcsa);
        var $currContent = $(target).find('div.personnel-content').last();
        $currContent.find('.date_picker').datepicker({
            format: "dd/mm/yyyy",
            autoclose: true,
            todayHighlight: true,
            orientation: 'bottom'
        });
        clearFields($currContent);
        $currContent.find('.speciality').html('');
        $currContent.find('.subSpeciality').html('');
        $currContent.find('.othersubSpeciality').html('');
        $currContent.find('.qualification').html('');
        $currContent.find('.otherDesignationDiv').addClass('hidden')
        refreshIndex($currContent, $(target).find('div.personnel-content').length - 1);
        $(target).find('div.personnel-content').first().find('.assign-psn-item').html('1');
        disablePersonnel($currContent, false, true);
        controlCountEvent($target, true);
        removePersonEvent();
        profRegNoEvent($currContent);
        designationChange()
        dismissWaiting();
    }

    function controlCountEvent($target, flag) {
        var psnLength = $target.find('div.personnel-content').length;
        let count = $target.find('.maxCount').val();
        if (flag) {
            if (psnLength >= count) {
                $target.find('.addDpoDiv').addClass('hidden');
            } else
                $target.find('.addDpoDiv').removeClass('hidden');
        } else {
            if (psnLength >= count) {
                $target.find('.addSpecialListBtn').addClass('hidden');
            } else
                $target.find('.addSpecialListBtn').removeClass('hidden');
        }

    }

    function refreshIndex($target, k) {
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.assign-psn-item').html(k + 1);
        resetIndex($target, k);
    }

    var removePersonEvent = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').on('click', function () {
            var $Content = $(this).closest('div.panel-main-content');
            $(this).closest('div.personnel-content').remove();
            controlCountEvent($Content, true)
            let $currContent = $Content.find('div.personnel-content');
            $currContent.each(function (k, v) {
                refreshIndex($(v), k);
            });
            if ($currContent.length == 1) {
                $currContent.find('.assign-psn-item').html('');
            }
        });
    }

    $('.addListBtn').click(function () {
        addPersonnels($(this).closest('div.panel-main-content'));
    });

    //special
    $('.addSpecialListBtn').click(function () {
        showWaiting();
        let target = $('div.personnel-content:last')
        let src = target.clone();
        clearFields(src);
        target.after(src);
        spRemove();
        pageController($('.personnel-content:last'));
        $('.personnel-content').first().find('.assign-psn-item').html('1');
        $('.personnel-content').last().find('.otherDesignationDiv').addClass('hidden')
        var psnLength = $('.personnel-content').length;
        let $target = $('div.personnel-content:last')
        let targets = $('.special-person');
        controlCountEvent($(targets))
        refreshIndex($target, psnLength - 1);
        profRegNoEvent($('.personnel-content:last'));
        designationChange()
        dismissWaiting();
    });

    function removeSp() {
        console.log("SP --->");
        $('.spErrorMsg').html("");
    }

    var pageController = function ($Ele) {
        let flag = $("#curr").val();
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

    var spRemove = function () {
        $('.removeBtns').click(function () {
            var $psnContentEle = $(this).closest('.personnel-content');
            $psnContentEle.remove();
            $('.personnel-content').each(function (k, v) {
                refreshIndex($(v), k)
            });
            let targets = $('.special-person')
            controlCountEvent($(targets))
            var psnLength = $('.personnel-content').length;
            if (psnLength <= 1) {
                $('.assign-psn-item:eq(0)').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
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
        fillNurse($("#" + fileAppendId + "ShowId").closest('div.panel-main-content'), data.appSvcPersonnelDto);
    }

    function fillNurse($premContent, data) {
        $($premContent).find('.personnel-content:not(:first)').remove();
        // $('div.personnel-content:not(:first)').remove();
        if (isEmpty(data) || !$.isArray(data)) {
            clearFields($('.personnel-content'));
            return;
        }
        clearFields($('.personnel-content'))
        var len = data.length;
        for (var i = 0; i < len; i++) {
            if (isEmptyNode($('.personnel-content').eq(i))) {
                //   TODO
                addPersonnels($premContent);
            }
            console.log(data[i])
            fillFormData($('.personnel-content').eq(i), data[i], '', i)
        }
    }

</script>
