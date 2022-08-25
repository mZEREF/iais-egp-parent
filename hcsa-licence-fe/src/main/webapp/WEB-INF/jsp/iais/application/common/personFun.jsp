<c:if test="${empty psnContent}">
    <c:set var="psnContent" value="person-content"/>
</c:if>
<%@include file="prsLoad.jsp" %>
<script type="text/javascript">
    $(function () {
        let psnContent = '.${psnContent}';
        removePersonEvent(psnContent);
        assignSelectEvent(psnContent);
        psnEditEvent(psnContent);
        // init page
        initPerson(psnContent);
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
            checkPersonContent($(v), true);
        });
        if ($target.length == 1) {
            $target.find('.psnHeader').html('');
        }
    }

    var psnEditEvent = function (target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.psnEdit').unbind('click');
        $target.find('.psnEdit').on('click', function () {
            showWaiting();
            var $currContent = $(this).closest(target);
            $currContent.find('input.isPartEdit').val('1');
            $(target + '-edit').val('1');
            hideTag($(this).closest('.edit-content'));
            unDisableContent($currContent);
            dismissWaiting();
        });
    }

    function refreshPerson($target, k) {
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.psnHeader').html(k + 1);
        resetIndex($target, k);
        if (typeof refreshPersonOthers === 'function') {
            refreshPersonOthers($target, k);
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
        clearFields($currContent);
        refreshPerson($currContent, $(target).length - 1);
        $(target + ':first').find('.psnHeader').html('1');
        $currContent.find('input.assignSelVal').val('-1');
        removePersonEvent(target);
        assignSelectEvent(target);
        profRegNoEvent(target);
        checkPersonContent($currContent, true);
        $(target + '-edit').val('1');
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
            });
            if ($currContent.length == 1) {
                $currContent.find('.psnHeader').html('');
            }
            $(target + '-edit').val('1');
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
                unDisableContent($content);
            }
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

    function personSelCallback(data, $currContent) {
        var $content = $currContent.find('.person-detail');
        if (data == null) {
            clearFields($content);
            return;
        }
        var cntClass = $currContent.attr('class');
        var prefix = $currContent.find('.prepsn').val();
        fillForm($content, data, prefix, $('div.' + cntClass).index($currContent), ['psnEditDto']);

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
            disablePrsInfo($currContent, true);
        } else if (!onlyInit) {
            disablePrsInfo($currContent, false);
        }
    }

    function toggleOnVal(sel, val, elem) {
        toggleOnSelect(sel, val, $(sel).closest('.form-group').siblings(elem));
    }
</script>