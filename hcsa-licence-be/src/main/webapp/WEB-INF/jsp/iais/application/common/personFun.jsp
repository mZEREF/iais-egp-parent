<script type="text/javascript">
    function initPerson(target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.each(function (k, v) {
            if ($("#errorMapIs").val() == 'error') {
                $(v).find('.error-msg').on('DOMNodeInserted', function () {
                    if ($(this).not(':empty')) {
                        doEditPsn($(v), target);
                    }
                });
            }
            checkPersonContent($(v), true);
        });
        // init add more button
        if (typeof refreshPersonOthers === 'function') {
            refreshPersonOthers($target);
        }
        if ($target.length == 1) {
            $target.find('.psnHeader').html('');
        }
        initNationality(target, 'select.idType', '.nationalityDiv');
        checkSpecialtyGetDateMandatory(target);
        dealMandatoryCount();
    }

    var psnEditEvent = function (target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.psnEdit').unbind('click');
        $target.find('.psnEdit').on('click', function () {
            doEditPsn($(this).closest(target), target);
        });
    }

    function doEditPsn($currContent, target) {
        if (isEmptyNode($currContent) || isEmpty(target)) {
            return;
        }
        if (hideEditBtn($currContent)) {
            return;
        }
        $currContent.find('.isPartEdit').val('1');
        console.log(target+'-edit','==========>')
        $(target + '-edit').val('1');
        hideTag($currContent.find('.edit-content'));
        unDisableContent($currContent);
        checkPersonDisabled($currContent);
        if (typeof refreshPersonOthers === 'function') {
            refreshPersonOthers($currContent);
        }
    }

    function hideEditBtn($currContent) {
        let $target = $currContent.find('.psnEdit');
        if (isEmptyNode($target)) {
            return true;
        }
        return $target.is(':hidden');
    }

    function disablePsnContent($currContent, target) {
        if (isEmptyNode($currContent) || isEmpty(target)) {
            return;
        }
        // edit btn
        let $target = $currContent.find('.psnEdit');
        let canEdit = !isEmptyNode($target);
        if (canEdit) {
            showTag($currContent.find('.removeEditDiv:not(:first)'));
        } else {
            hideTag($currContent.find('.removeEditDiv'));
        }
        disableContent($currContent);
        if (typeof refreshPersonOthers === 'function') {
            refreshPersonOthers($currContent, canEdit ? 0 : 1);
        }
    }

    function refreshPerson($target, k) {
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.psnHeader').html(k + 1);
        resetIndex($target, k);
        if (typeof refreshPersonOthers === 'function') {
            refreshPersonOthers($target);
        }
    }

    function addPersonnel(target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        showWaiting();
        var $tgt = $(target + ':last');
        var src = $tgt.clone();
        $tgt.after(src);
        var $currContent = $(target).last();
        initFormNodes($currContent);
        clearFields($currContent);
        hideTag($currContent.find('.rfc-psn-detail'));
        hideTag($currContent.find('.edit-content'));
        unDisableContent($currContent.find('.assignSelDiv'));
        showTag($currContent.find('.assignSelDiv'));
        refreshPerson($currContent, $(target).length - 1);
        $(target + ':first').find('.psnHeader').html('1');
        $currContent.find('input.assignSelVal').val('-1');
        removePersonEvent(target);
        assignSelectEvent(target);
        profRegNoEvent(target);
        checkPersonContent($currContent, true);
        $currContent.find('.isPartEdit').val('1');
        $(target + '-edit').val('1');
        dealMandatoryCount();
    }

    var removePersonEvent = function (target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.removeBtn').unbind('click');
        $target.not(':first').find('.removeBtn').on('click', function () {
            showWaiting();
            $(this).closest(target).remove();
            let $currContent = $(target);
            $currContent.each(function (k, v) {
                refreshPerson($(v), k);
                //doEditPsn($currContent, target);
            });
            if ($currContent.length == 1) {
                $currContent.find('.psnHeader').html('');
            }
            $(target + '-edit').val('1');
            dealMandatoryCount();
            dismissWaiting();
        });
    }

    var assignSelectEvent = function (target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.assignSel').unbind('change');
        $target.find('.assignSel').on('change', function () {
            showWaiting();
            var assignVal = $(this).val();
            var $currContent = $(this).closest(target);
            $currContent.find('input.assignSelVal').val(assignVal);
            checkPersonContent($currContent, false, false);
            removePersonEvent(target);
        });
    }

    function checkPersonContent($currContent, onlyInit, fromUser) {
        var assignVal = $currContent.find('input.assignSelVal').val();
        var $content = $currContent.find('.person-detail');
        console.info("Assign Val: " + assignVal);
        if ('-1' == assignVal || isEmpty(assignVal)) {
            hideTag($content);
            $currContent.find('.speciality p').html('');
            $currContent.find('.subSpeciality p').html('');
            $currContent.find('.qualification p').html('');
            $content.find('.designation').trigger('change');
            $content.find('.idType').trigger('change');
            $currContent.find('input.licPerson').val('0');
            dismissWaiting();
        } else if ('newOfficer' == assignVal) {
            showTag($content);
            if (!onlyInit) {
                clearFields($content);
                $currContent.find('.speciality p').html('');
                $currContent.find('.subSpeciality p').html('');
                $currContent.find('.qualification p').html('');
                $currContent.find('.specialtyGetDateLabel .mandatory').remove();
                unDisableContent($content);
            }
            checkPersonDisabled($content,!onlyInit,true);
            $content.find('.designation').trigger('change');
            $content.find('.idType').trigger('change');
            $currContent.find('input.licPerson').val('0');
            dismissWaiting();
        } else {
            showTag($content);
            if (onlyInit) {
                $content.find('.designation').trigger('change');
                $content.find('.idType').trigger('change');
                checkPersonDisabled($currContent, true);
                dismissWaiting();
                return;
            }
            unDisableContent($content);
            var url = "/person-info";
            if (fromUser) {
                url = "/user-account-info";
            }
            var indexNo = $currContent.find('input.indexNo').val();
            var arr = assignVal.split(',');
            var nationality = arr[0];
            var idType = arr[1];
            var idNo = arr[2];
            var data = {
                'nationality': nationality,
                'idType': idType,
                'idNo': idNo,
                'indexNo': indexNo
            };
            var opt = {
                url: '${pageContext.request.contextPath}' + url,
                type: 'GET',
                data: data
            };
            callCommonAjax(opt, "personSelCallback", $currContent);
        }
    }

    function personSelCallback(data, $currContent) {
        var $content = $currContent.find('.person-detail');
        if (data == null) {
            clearFields($content);
            return;
        }
        var cntClass = $currContent.attr('class');
        var prefix = $currContent.find('.prepsn').val();
        fillFormData($content, data, prefix, $('div.' + cntClass).index($currContent), ['psnEditDto']);

        $currContent.find('.speciality p').html(data.speciality);
        $currContent.find('.subSpeciality p').html(data.subSpeciality);
        $currContent.find('.qualification p').html(data.qualification);
        $currContent.find('input.licPerson').val(data.licPerson ? 1 : 0);
        $currContent.find('input.isPartEdit').val(1);
        $currContent.find('input.indexNo').val(data.indexNo);
        $currContent.find('input.psnEditField').val(data.psnEditFieldStr);
        checkPersonDisabled($currContent);
        $currContent.find('.designation').trigger('change');
        $currContent.find('.idType').trigger('change');
        dismissWaiting();
    }

    function checkPersonDisabled($currContent, onlyInit,flag) {
        let psnEditFieldData = $currContent.find('.psnEditField').val();
        console.log(psnEditFieldData,'edit====>')
        if (isEmpty(psnEditFieldData)) {
            $currContent.find('.licPerson').val(0);
        } else {
            let data;
            try {
                data = $.parseJSON(psnEditFieldData);
            } catch (e) {
                data = {};
            }

            if ('1' == $currContent.find('.licPerson').val()) {
                $.each(data, function (i, val) {
                    let $input = $currContent.find('.' + i + ':input');
                    if ($input.length > 0 && !val) {
                        disableContent($input);
                    }
                });
            } else if (!onlyInit) {
                $.each(data, function (i, val) {
                    let $input = $currContent.find('.' + i + ':input');
                    if ($input.length > 0) {
                        unDisableContent($input);
                    }
                });
            }
        }

        if (!isEmpty($currContent.find('input.profRegNo').val())) {
            disablePrsInfo($currContent, true,flag);
        } else if (!onlyInit) {
            disablePrsInfo($currContent, false,flag);
        }
    }

    function toggleOnVal(sel, val, elem) {
        toggleOnSelect(sel, val, $(sel).closest('.form-group').siblings(elem));
    }

    function initNationality(parent, idTypeTag, nationalityDiv) {
        $(parent).find(idTypeTag).on('change', function () {
            var $content = $(this).closest(parent.replace(':last', ''));
            toggleOnSelect(this, 'IDTYPE003', $content.find(nationalityDiv));
        });
        $(parent).each(function (index, ele) {
            toggleOnSelect($(ele).find(idTypeTag), 'IDTYPE003', $(ele).find(nationalityDiv));
        });
    }

    let dealMandatoryCount = function (){
        const minCount = eval('${currStepConfig.mandatoryCount}');
        const minDpoCount = eval('${dpoHcsaSvcPersonnelDto.mandatoryCount}');
        $('.person-content').each(function (K,V) {
            if (K+1 <= minCount){
                hideTag($(V).find('.removeEditDiv'))
            }
        })
        $('.dpo-person-content').each(function (K,V) {
            if (K+1 <= minDpoCount){
                hideTag($(V).find('.removeEditDiv'))
            }
        })
    }

</script>