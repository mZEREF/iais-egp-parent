<c:if test="${empty psnContent}">
    <c:set var="psnContent" value="personnel-content"/>
</c:if>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>
<script type="text/javascript">
    $(function () {
        assignSelectEvent();
        removePersonnelEvent();
        personnelSel();
        initPerson($('div.panel-main-content'));
    });

    function initPerson(target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.each(function (k, v) {
            if ($("#errorMapIs").val() == 'error') {
                if ($(v).find('.error-msg:not(:empty)').length > 0) {
                    $(v).find('.psnEdit').trigger("click");
                }
            }
            if ($(v).find('div.personnel-content').length == 1) {
                $(v).find('.assign-psn-item').html('');
            }
            $(v).find('div.personnel-content').each(function (i, x) {
                /*var flag=isEmpty($(x).find('input.profRegNo').val())?false:true;
                disablePrsInfo($(x),flag,true);
                checkPersonContent($(x), true);*/
                checkPersonDisabled($(x), true);
                var personnelSel = $(x).find('.personnelType').val();
                var flag = $(x).find('input.personTypeToShow').val();
                personnelSelFun(personnelSel, $(x),flag);
            })
        });
    }

    function refreshPerson($target, k) {
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.assign-psn-item').html(k + 1);
        resetIndex($target, k);
    }

    function addPerson(target,type,maxCount) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        showWaiting();
        var $tgt = $(target).find('div.personnel-content').last();
        var src = $tgt.clone();
        $tgt.after(src);
        var $currContent = $(target).find('div.personnel-content').last();
        $currContent.find('.date_picker').datepicker({
            format:"dd/mm/yyyy",
            autoclose:true,
            todayHighlight:true,
            orientation:'bottom'
        });
        clearFields($currContent);
        $currContent.find('.speciality').html('');
        $currContent.find('.subSpeciality').html('');
        $currContent.find('.othersubSpeciality').html('');
        $currContent.find('.qualification').html('');
        refreshPerson($currContent, $(target).find('div.personnel-content').length - 1);
        disablePrsInfo($currContent, false,true);
        $(target).find('div.personnel-content').first().find('.psnHeader').html('1');
        removePersonnelEvent();
        profRegNoEvent($currContent);
        assignSelectEvent($currContent);
        checkPersonContent($currContent, true);
        personnelSel();
        personnelSelFun('',$currContent,'');
        var length =  $target.find('div.personnel-content').length;
        if(length >= maxCount){
            $target.find('.addDiv').addClass('hidden');
        }
        $target.find('input.Length').val(length);
        dismissWaiting();
    }

    var removePersonnelEvent = function () {
        $('.removeBtns').unbind('click');
        $('.removeBtns').on('click', function () {
            var $Content = $(this).closest('div.panel-main-content');
            var maxCount=$Content.find('input.MaxCount').val();
            $(this).closest('div.personnel-content').remove();
            let $currContent = $Content.find('div.personnel-content');
            $currContent.each(function (k, v) {
                refreshPerson($(v), k);
            });
            if ($currContent.length == 1) {
                $currContent.find('.assign-psn-item').html('');
            }
            var len =  $Content.find('div.personnel-content').length;
            $Content.find('input.Length').val(len);
            if(len < maxCount){
                $Content.find('.addDiv').removeClass('hidden');
            }
        });
    }

    function toggleOther(sel, val, elem) {
        toggleOnSelect(sel, val, $(sel).closest('.form-group').siblings(elem));
    }

    var assignSelectEvent = function () {
        $('.assignSel').unbind('change');
        $('.assignSel').on('change', function () {
            showWaiting();
            var assignVal = $(this).val();
            var $currContent = $(this).closest('div.personnel-content');
            $currContent.find('input.assignSelVal').val(assignVal);
            checkPersonContent($currContent, false, false);
        });
    }

    function checkPersonContent($currContent, onlyInit, fromUser) {
        var assignVal = $currContent.find('input.assignSelVal').val();
        var $content = $currContent.find('.person-detail');
        if ('-1' == assignVal || isEmpty(assignVal)) {
            clearFields($content);
            hideTag($content);
            $currContent.find('.speciality p').html('');
            $currContent.find('.subSpeciality p').html('');
            $currContent.find('.qualification p').html('');
            $content.find('.designation').trigger('change');
            $content.find('.idType').trigger('change');
            $currContent.find('input.licPerson').val('0');
            unDisableContent($content);
            dismissWaiting();
        } else if ('newOfficer' == assignVal) {
            showTag($content);
            if (!onlyInit) {
                clearFields($content);
                $currContent.find('.speciality p').html('');
                $currContent.find('.subSpeciality p').html('');
                $currContent.find('.qualification p').html('');
            }
            unDisableContent($content);
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

    function checkPersonDisabled($currContent, onlyInit) {
        let data;
        try {
            data = $.parseJSON($currContent.find('.psnEditField').val());
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
        if (!isEmpty($currContent.find('input.profRegNo').val())) {
            disablePrsInfo($currContent, true,true);
        } else if (!onlyInit) {
            disablePrsInfo($currContent, false,true);
        }
    }

    function personSelCallback(data, $currContent) {
        var $content = $currContent.find('.person-detail');
        if (data == null) {
            clearFields($content);
            return;
        }
        var prefix = $currContent.find('.prepsn').val();
        var subfix=$currContent.index();
        fillFormData($content, data, prefix, subfix, ['psnEditDto']);
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
        $currContent.find('.profRegNo').trigger('blur');
        dismissWaiting();
    }

    function toggleOnVal(sel, val, elem) {
        toggleOnSelect(sel, val, $(sel).closest('.form-group').siblings(elem));
    }

    var personnelSel = function () {
        $('.personnelType').change(function () {
            var personnelSel = $(this).val();
            var $personnelContentEle = $(this).closest('.personnel-content');
            var flag = $personnelContentEle.find('input.personTypeToShow').val();
            personnelSelFun(personnelSel, $personnelContentEle,flag);
        });
    };

    var personnelSelFun = function (personnelSel, $personnelContentEle,flag) {
        if ('' == personnelSel) {
            clearFields($personnelContentEle);
            $personnelContentEle.find('.personnel-name').addClass('hidden');
            $personnelContentEle.find('.personnel-qualification').addClass('hidden');
            $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
        }else {
            if (0==flag){
                $personnelContentEle.find('.personnel-name').removeClass('hidden');
                $personnelContentEle.find('.personnel-qualification').addClass('hidden');
                $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
            }else if (1==flag){
                $personnelContentEle.find('.personnel-name').removeClass('hidden');
                $personnelContentEle.find('.personnel-qualification').removeClass('hidden');
                $personnelContentEle.find('.personnel-wrkExpYear').removeClass('hidden');
            }
        }
    }
</script>