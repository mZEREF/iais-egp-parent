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
                var personnelSel = $(this).find('.personnelSel').val();
                var $personnelContentEle = $(this);
                personnelSelFun(personnelSel, $personnelContentEle);
            });
        }
        if (${AppSubmissionDto.needEditController && !isClickEdit}) {
            disabledPage();
        }
        doEdit();
        spRemove();
        designationChange();

        initPage($('div.panel-main-content'))

        $('input[name="prsLoading"]').each(function () {
            if ($(this).val() == 'true') {
                var $currContent = $(this).closest('.personnel-content');
                inputReadonly($currContent.find('input[name="name"]'));
            }
        });

        if ("${errormapIs}" == 'error') {
            $('.svcPsnEdit').trigger('click');
        }
        init = 1;
        // refreshServerPersonIndex()
        fileUploadEvent()
        //no回填
        profRegNoEvent($('.personnel-content'));
        removePersonEvent();

        initChangePsnItem();
    });

    function initPage(target){

        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.each(function (k, v) {
            if ($(v).find('div.personnel-content').length == 1) {
                $(v).find('.assign-psn-item').html('');
            }
            $(v).find('div.personnel-content').each(function (i, x) {
                var flag=isEmpty($(x).find('input.profRegNo').val())?false:true;
                disablePersonnel($(x),flag,true);
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


    var absencePsnSel = function (val, $Ele) {
        $('.svcPsnSel').addClass('hidden');
        var prsFlag = $('input[name="prsFlag"]').val();
        let $eles = $('.personnel-content');
        if ($Ele != '') {
            $eles = $($Ele)
        }
        $eles.each(function (k, v) {
            if ('Blood Banking' == val) {
                $(this).find('.personnel-sel').addClass('hidden');
                $(this).find('.new-svc-personnel-form').removeClass('hidden');
                $(this).find('.personnel-designation').removeClass('hidden');
                $(this).find('.personnel-name').removeClass('hidden');
                $(this).find('.personnel-qualification').addClass('hidden');
                $(this).find('.personnel-regnNo ').removeClass('hidden');
                $(this).find('.personnel-wrkExpYear').removeClass('hidden');
                if ("Y" == prsFlag) {
                    console.log("blood banking")
                    // inputReadonly($(this).find('input[name="name"]'));
                }
            } else if ('Tissue Banking p1' == val) {
                $(this).find('.personnel-sel').addClass('hidden');
                $(this).find('.new-svc-personnel-form').removeClass('hidden');
                $(this).find('.personnel-designation').addClass('hidden');
                $(this).find('.personnel-name').removeClass('hidden');
                $(this).find('.personnel-qualification').removeClass('hidden');
                $(this).find('.personnel-regnNo ').addClass('hidden');
                $(this).find('.personnel-wrkExpYear').removeClass('hidden');
                if ("Y" == prsFlag) {
                    inputCancelReadonly($(this).find('input[name="name"]'));
                }
            } else {
                $(this).find('.personnel-sel').addClass('hidden');
                $(this).find('.new-svc-personnel-form').removeClass('hidden');
                $(this).find('.personnel-designation').addClass('hidden');
                $(this).find('.personnel-name').removeClass('hidden');
                $(this).find('.personnel-qualification').removeClass('hidden');
                $(this).find('.personnel-regnNo ').addClass('hidden');
                $(this).find('.personnel-wrkExpYear').removeClass('hidden');
                if ("Y" == prsFlag) {
                    inputCancelReadonly($(this).find('input[name="name"]'));
                }
            }
        });
    };

    var personnelSel = function () {
        $('.personnelSel').change(function () {
            var personnelSel = $(this).val();
            var $personnelContentEle = $(this).closest('.personnel-content');
            var prsFlag = $('input[name="prsFlag"]').val();
            if (init != 0) {
                //clear data;
                $personnelContentEle.find('.personnel-designation select[name="designation"]').val('');
                var designation = $personnelContentEle.find('.personnel-designation  option[value=""]').html();
                $personnelContentEle.find('select[name="designation"]').next().find('.current').html(designation);

                $personnelContentEle.find('.personnel-name input[name="name"]').val('');
                $personnelContentEle.find('.personnel-regnNo input[name="regnNo"]').val('');
                $personnelContentEle.find('.personnel-wrkExpYear input[name="wrkExpYear"]').val('');
                $personnelContentEle.find('.personnel-qualification input[name="qualification"]').val('');
                if ('Y' == prsFlag) {
                    inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
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
                inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
            }
        } else if ('SPPT002' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').addClass('hidden');
            $personnelContentEle.find('.personnel-name').removeClass('hidden');
            $personnelContentEle.find('.personnel-qualification').removeClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').removeClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
            if ('Y' == prsFlag) {
                inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
            }
        } else if ('SPPT003' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').addClass('hidden');
            $personnelContentEle.find('.personnel-name').removeClass('hidden');
            $personnelContentEle.find('.personnel-qualification').addClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
            inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
        } else if ('SPPT004' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').addClass('hidden');
            $personnelContentEle.find('.personnel-name').removeClass('hidden');
            $personnelContentEle.find('.personnel-qualification').addClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').removeClass('hidden');
            //regnNo.
            if ('Y' == prsFlag) {
                inputReadonly($personnelContentEle.find('input[name="name"]'));
            }
        } else if ('' == personnelSel) {
            $personnelContentEle.find('.personnel-designation').addClass('hidden');
            $personnelContentEle.find('.personnel-name').addClass('hidden');
            $personnelContentEle.find('.personnel-qualification').addClass('hidden');
            $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
            if ('Y' == prsFlag) {
                inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
            }
        }
    }


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

        console.log('add',111)

        fillValue($tgt.find('input.locateWtihNonHcsa'), locateWtihNonHcsa);

        var $currContent = $(target).find('div.personnel-content').last();

        $currContent.find('.date_picker').datepicker({
            format:"dd/mm/yyyy",
            autoclose:true,
            todayHighlight:true,
            orientation:'bottom'
        });
        clearFields($currContent);
        // $currContent.find('.speciality').html('');
        // $currContent.find('.subSpeciality').html('');
        // $currContent.find('.othersubSpeciality').html('');
        // $currContent.find('.qualification').html('');
        // $currContent.find('.typeOfCurrRegi').html('');
        // $currContent.find('.typeOfRegister').html('');
        $currContent.find('.speciality').html('');
        $currContent.find('.subSpeciality').html('');
        $currContent.find('.othersubSpeciality').html('');
        $currContent.find('.qualification').html('');
        refreshIndex($currContent, $(target).find('div.personnel-content').length - 1);

        disablePersonnel($currContent, false,true);

        $(target).find('div.personnel-content').first().find('.assign-psn-item').html('1');

        removePersonEvent();

        profRegNoEvent($currContent);

        var length =  $target.find('div.personnel-content').length;

        $target.find('input.length').val(length);

        console.log(length)

        dismissWaiting();
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
            let $currContent = $Content.find('div.personnel-content');
            $currContent.each(function (k, v) {
                refreshIndex($(v), k);
            });

            if ($currContent.length == 1) {
                $currContent.find('.assign-psn-item').html('');
            }
            var len =  $Content.find('div.personnel-content').length;
            if (len==0){
                $Content.find('input.length').val(1);
            }else {
                $Content.find('input.length').val(len);
            }
        });
    }

    $('.addListBtn').click(function () {
        addPersonnels($(this).closest('div.panel-main-content'));

    });




    $('.addSpecialListBtn').click(function () {
        showWaiting();
        let target =  $('div.personnel-content:last')
        let src = target.clone();
        clearFields(src);
       target.after(src);
        pageController($('.personnel-content:last'));
        spRemove();
        var psnLength = $('.personnel-content').length;
        let $target =  $('div.personnel-content:last')
        toggleTag($target.find('.removeBtn'), psnLength-1 != 0);
        changePsnItem();
        designationChange();
        dismissWaiting();
    });



    function refreshServerPersonIndex() {
        var slLength = $('.personnel-content').length;
        $('input[name="slLength"]').val(slLength);
        $('input[name="commonIndex"]').val(slLength)
        console.info("length: " + slLength)
        console.info("index: " + slLength)
        var $content = $('.personnel-content');

        //刷新index  后续的
        let content = $('input[name="personnelType"]').val();
        if (content != "PersonnelDetail") {
            refreshIndex($content);
            $content.each(function (k, v) {
                if (slLength <= 1 && k == 0) {
                    $(this).find('.assign-psn-item').html('');
                } else {
                    $(this).find('.assign-psn-item').html(k + 1);
                }
            });
        }

    }

    function removeSp() {
        console.log("SP --->");
        $('.spErrorMsg').html("");
    }

    var pageController = function ($Ele) {
        let flag = $("#curr").val();
        if (flag == 'NMI' || flag == 'NMA') {
            personnelSel();
            if ($Ele == '') {
                //triggering event
                $('.personnelSel').trigger('change');
            } else {
                $Ele.find('.personnelSel').trigger('change');
            }
        } else if ('BLB' == flag) {
            absencePsnSel('Blood Banking', $Ele);
        } else if ('TSB' == flag) {
            absencePsnSel('Tissue Banking p1', $Ele);
        } else {
            absencePsnSel('other service', $Ele);
        }

    }


    var doEdit = function () {
        $('.svcPsnEdit').click(function () {
            let $context = $(this).closest("td")
            $(this).addClass('hidden');
            unDisabledPartPage($context);
            $('#isEditHiddenVal').val('1');
            $context.find('input[name="isPartEdit"]').val('1');
            $(this).find('input[name="prsLoading"]').each(function () {
                console.log('11111111111111')
                var prsLoading = $(this).val();
                if (prsLoading == 'true') {
                    var $currContent = $(this).closest('.personnel-content');
                    inputReadonly($currContent.find('input[name="name"]'));
                }
            });
        });
    }
    var spRemove = function () {
        $('.removeBtn').click(function () {
            var $psnContentEle = $(this).closest('.personnel-content');
            $psnContentEle.remove();

            //reset number
            $('.personnel-content').each(function (k, v) {
                $(this).find('.assign-psn-item').html(k + 1);
            });
            var psnLength = $('.personnel-content').length;
            <%--if (psnLength < '${spHcsaSvcPersonnelDto.maximumCount}') {--%>
            <%--    $('#addPsnDiv').removeClass('hidden');--%>
            <%--}--%>
            if (psnLength <= 1) {
                $('.assign-psn-item:eq(0)').html('');
            }

            $('input.length').val(psnLength)

            $('#isEditHiddenVal').val('1');
        });
    }

    function aaa(obj) {
        console.log('loading prs info ...');
        showWaiting();
        var $loadingContent = $(obj).closest('.personnel-content');
        var prgNo = $(obj).val();
        if (prgNo == "" || prgNo == null || prgNo == undefined) {
            clearPrsInfo($loadingContent);
            dismissWaiting();
            return;
        }
        var no = $(obj).val();
        var jsonData = {
            'prgNo': no
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                if (isEmpty(data)) {
                    console.log("The return data is null for PRS");
                } else if ('-1' == data.statusCode || '-2' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo($loadingContent);
                } else if (data.hasException) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0048" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo($loadingContent);
                } else if ('401' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0054" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo($loadingContent);
                } else {
                    loadingSp(data, obj);
                }
                dismissWaiting();
            },
            'error': function () {
                clearPrsInfo($loadingContent);
                dismissWaiting();
            }
        });

    }

    var clearPrsInfo = function ($loadingContent) {
        $loadingContent.find('input[name="name"]').val('');
    };

    function loadingSp(data, obj) {
        var $CurrentPsnEle = $(obj).closest('.personnel-content');
        const name = data.name;
        $CurrentPsnEle.find("input[name='name']").val(name);
        var prsFlag = $('input[name="prsFlag"]').val();
        if ('Y' == prsFlag) {
            inputReadonly($CurrentPsnEle.find('input[name="name"]'));
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

    function notLoadingSp() {
        $("input[name='name']").prop('readonly', false);
        $("input[name='name']").css('border-color', '');
        $("input[name='name']").css('color', '');
    }

    function notLoadingSpl() {
        var val = $("input[name='regnNo']").val();
        if (val != "") {
            $("input[name='name']").prop('readonly', true);
            $("input[name='name']").css('border-color', '#ededed');
            $("input[name='name']").css('color', '#999');
        }
    }

    function cancel() {
        $('#PRS_SERVICE_DOWN').modal('hide');
    }

    var changePsnItem = function () {
        $('.assign-psn-item').each(function (k, v) {
            $(this).html(k + 1);
        });
    };

    var initChangePsnItem = function () {
        let flag = '${logo}';
        if (flag === 'SP000'){
            var psnLength = $('.personnel-content').length;
            if (psnLength >1){
                $('.assign-psn-item').each(function (k, v) {
                    $(this).html(k + 1);
                });
            }
        }
    };


    var designationChange = function () {
        $('.designation').unbind('change');
        $('.designation').change(function () {
            var thisVal = $(this).val();
            if ("Others" == thisVal) {
                $(this).closest('.personnel-content').find('.otherDesignationDiv').removeClass('hidden');
            } else {
                $(this).closest('.personnel-content').find('.otherDesignationDiv').addClass('hidden');
            }
        });
    };

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
        //清除除了第一个的

        $($premContent).find('.personnel-content:not(:first)').remove();

        // $('div.personnel-content:not(:first)').remove();
        if (isEmpty(data) || !$.isArray(data)) {
            clearFields($('.personnel-content'));
            return;
        }
        clearFields($('.personnel-content'))
        var len = data.length;
        for (var i = 0; i < len; i++) {
            //如果没多余的，那么重新拷贝一份
            if (isEmptyNode($('.personnel-content').eq(i))) {
                //重新拷贝一份    TODO
                addPersonnels($premContent);
            }
            console.log(data[i])
            fillForm($('.personnel-content').eq(i), data[i], '', i)
        }
    }
</script>
