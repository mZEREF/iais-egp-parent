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
        removePersonEvent();
        assignSelectEvent();
        profRegNoEvent();
        psnEditEvent();

        $('div.${psnContent}').each(function (k, v) {
            if ($("#errorMapIs").val() == 'error') {
                if ($(v).find('.error-msg:not(:empty)').length > 0) {
                    $(v).find('.psnEdit').trigger("click");
                }
            }
            checkPersonContent($(v), true);
        });
        if ($('div.${psnContent}').length == 1) {
            $('div.${psnContent}').find('.psnHeader').html('');
        }

        if ($('#PRS_SERVICE_DOWN_INPUT').val() == 'PRS_SERVICE_DOWN') {
            $('#PRS_SERVICE_DOWN').modal('show');
        }
    });

    var psnEditEvent = function () {
        $('.psnEdit').unbind('click');
        $('.psnEdit').on('click', function () {
            showWaiting();
            var $currContent = $(this).closest('.${psnContent}');
            $currContent.find('input.isPartEdit').val('1');
            $('#isEditHiddenVal').val('1');
            hideTag($(this).closest('.edit-content'));
            unDisableContent($currContent);
            dismissWaiting();
        });
    }

    function refreshPerson($target, k) {
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.psnHeader').html(k + 1);
        resetIndex($target, k);
    }

    function addPersonnel() {
        showWaiting();
        var $target = $('div.${psnContent}:last');
        var src = $target.clone();
        $target.after(src);
        var $currContent = $('div.${psnContent}').last();
        clearFields($currContent);
        refreshPerson($currContent, $('div.${psnContent}').length - 1);
        $currContent.find('input.assignSelVal').val('-1');
        $('div.${psnContent}:first').find('.psnHeader').html('1');
        removePersonEvent();
        assignSelectEvent();
        profRegNoEvent();
        checkPersonContent($currContent, true);
        $('#isEditHiddenVal').val('1');
    }

    var removePersonEvent = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn:not(:first)').on('click', function () {
            showWaiting();
            $(this).closest('.${psnContent}').remove();
            $('div.${psnContent}').each(function (k, v) {
                refreshPerson($(v), k);
            });
            if ($('div.${psnContent}').length == 1) {
                $('div.${psnContent}').find('.psnHeader').html('');
            }
            $('#isEditHiddenVal').val('1');
            dismissWaiting();
        });
    }

    var assignSelectEvent = function () {
        $('.assignSel').unbind('change');
        $('.assignSel').on('change', function () {
            showWaiting();
            var assignVal = $(this).val();
            var $currContent = $(this).closest('.${psnContent}');
            $currContent.find('input.assignSelVal').val(assignVal);
            checkPersonContent($currContent, false, false);
            removePersonEvent();
        });
    }

    function checkPersonContent($currContent, onlyInit, fromUser, callback) {
        var assignVal = $currContent.find('input.assignSelVal').val();
        var $content = $currContent.find('.person-detail');
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
            var jsonData = {
                'nationality': nationality,
                'idType': idType,
                'idNo': idNo,
                'indexNo': indexNo
            };
            $.ajax({
                'url': '${pageContext.request.contextPath}' + url,
                'dataType': 'json',
                'data': jsonData,
                'type': 'GET',
                'success': function (data) {
                    if (data == null) {
                        clearFields($content);
                        return;
                    }
                    if (typeof callback === 'function') {
                        callback($currContent, data);
                    } else {
                        fillForm($content, data, "", $('div.${psnContent}').index($currContent));

                        $currContent.find('.speciality p').html(data.specialty);
                        $currContent.find('.subSpeciality p').html(data.subspecialty);
                        $currContent.find('.qualification p').html(data.qualification);
                        $currContent.find('input.licPerson').val(data.licPerson ? 1 : 0);
                        $currContent.find('input.isPartEdit').val(1);
                        $currContent.find('input.indexNo').val(data.indexNo);
                        $currContent.find('input.psnEditField').val(data.psnEditFieldStr);
                        checkPersonDisabled($currContent);
                        $currContent.find('.designation').trigger('change');
                        $currContent.find('.idType').trigger('change');
                    }
                    dismissWaiting();
                },
                'error': function () {
                    dismissWaiting();
                }
            });
        }
    }

    function checkPersonDisabled($currContent, onlyInit) {
        var data;
        try {
            data = $.parseJSON($currContent.find('.psnEditField:input').val());
        } catch (e) {
            data = {};
        }
        ;
        if ('1' == $currContent.find('.licPerson').val()) {
            $.each(data, function (i, val) {
                //console.info(i + " : " + val);
                var $input = $currContent.find('.' + i + ':input');
                if ($input.length > 0 && !val) {
                    disableContent($input);
                }
            });
        }

        if (!isEmpty($currContent.find('input.profRegNo').val())) {
            disablePrsInfo($currContent, true);
        } else if (!onlyInit) {
            disablePrsInfo($currContent, false);
        }
    }

    var profRegNoEvent = function () {
        $('.profRegNo').unbind('blur');
        $('.profRegNo').on('blur', function () {
            showWaiting();
            var prgNo = $(this).val();
            var $currContent = $(this).closest('.${psnContent}');
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
        }
        if (needControlName && !isEmpty(data.name)) {
            $currContent.find('.name').val(name);
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

</script>