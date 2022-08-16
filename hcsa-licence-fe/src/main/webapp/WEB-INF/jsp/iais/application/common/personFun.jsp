<c:if test="${empty psnContent}">
    <c:set var="psnContent" value="person-content"/>
</c:if>
<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                    <span style="font-size: 2rem;" id="prsErrorMsg">
                      <iais:message key="GENERAL_ERR0048" escape="false"/>
                    </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal"
                        onclick="$('#PRS_SERVICE_DOWN').modal('hide');">OK
                </button>
            </div>
        </div>
    </div>
</div>
<input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT">
<script type="text/javascript">
    $(function () {
        let psnContent = '.${psnContent}';
        removePersonEvent(psnContent);
        assignSelectEvent(psnContent);
        profRegNoEvent(psnContent);
        psnEditEvent(psnContent);
        // init page
        initPerson(psnContent);

        if ($('#PRS_SERVICE_DOWN_INPUT').val() == 'PRS_SERVICE_DOWN') {
            $('#PRS_SERVICE_DOWN').modal('show');
        }
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
        fillForm($content, data, prefix, $('div.' + cntClass).index($currContent));

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
            var needControlName = isNeedControlName(assignSelectVal, licPerson, appType);
            console.log("isNeedControlName: " + needControlName);
            checkProfRegNo($currContent, prgNo, needControlName);
        });
    };

    function checkProfRegNo($currContent, prgNo, needControlName, callback) {
        showWaiting();
        if (isEmpty(prgNo)) {
            fillPrsInfo($currContent, null, needControlName);
            disablePrsInfo($currContent, false);
            dismissWaiting();
            if (typeof callback === 'function') {
                callback($currContent, null);
            }
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
                fillPrsInfo($currContent, canFill ? data : null, needControlName);
                disablePrsInfo($currContent, canFill);
                if (typeof callback === 'function') {
                    callback($currContent, canFill ? data : null);
                }
                dismissWaiting();
            },
            'error': function () {
                fillPrsInfo($currContent, null, needControlName);
                disablePrsInfo($currContent, false);
                dismissWaiting();
            }
        });
    }

    function fillPrsInfo($currContent, data, needControlName) {
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
            if (needControlName && !isEmpty(data.name)) {
                $currContent.find('.name').val(name);
            }
        }

        $currContent.find('.speciality p').html(specialty);
        $currContent.find('.subSpeciality p').html(subspecialty);
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

    function toggleOnVal(sel, val, elem) {
        toggleOnSelect(sel, val, $(sel).closest('.form-group').siblings(elem));
    }

    function isNeedControlName(assignSelectVal, licPerson, appType) {
        return /*'newOfficer' == assignSelectVal &&*/ '1' != licPerson && 'APTY002' == appType;
    }

    function fillForm(ele, data, prefix, suffix) {
        var $selector = getJqueryNode(ele);
        if (isEmptyNode($selector)) {
            return;
        }
        if (isEmpty(data)) {
            clearFields($selector);
            return;
        }
        if (isEmpty(prefix)) {
            prefix = "";
        }
        if (isEmpty(suffix)) {
            suffix = "";
        }
        for (var i in data) {
            var val = data[i];
            if (Object.prototype.toString.call(val) === "[object Object]") {
                fillValue(ele, val, prefix, suffix);
            }
            var name = prefix + i + suffix;
            var $input = $selector.find('[name="' + name + '"]');
            if ($input.length == 0) {
                name = prefix + capitalize(i) + suffix;
                $input = $selector.find('[name="' + name + '"]');
            }
            if ($input.length == 0) {
                continue;
            }
            if ($input.hasClass('field-date')) {
                val = data[i + 'Str'];
            }
            fillValue($input, val, true);
        }
    }

</script>