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
        if ($('#PRS_SERVICE_DOWN_INPUT').val() == 'PRS_SERVICE_DOWN') {
            $('#PRS_SERVICE_DOWN').modal('show');
        }
    });

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
        checkSpecialtyGetDateMandatoryEvent(target);
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
            if ($.isArray(data.registration) && !isEmpty(data.registration[0])) {
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
        $currContent.find('.qualification p').html(qualification);

        $currContent.find('.specialtyGetDate').val(specialtyGetDate);
        $currContent.find('.typeOfCurrRegi').val(typeOfCurrRegi);
        $currContent.find('.currRegiDate').val(currRegiDate);
        $currContent.find('.praCerEndDate').val(praCerEndDate);
        $currContent.find('.typeOfRegister').val(typeOfRegister);
    }

    function disablePrsInfo($currContent, flag, needControlName) {
        if (flag) {
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
            unDisableContent($currContent.find('.typeOfCurrRegi'));
            unDisableContent($currContent.find('.currRegiDate'));
            unDisableContent($currContent.find('.praCerEndDate'));
            unDisableContent($currContent.find('.typeOfRegister'));
            if (needControlName) {
                unDisableContent($currContent.find('.name'));
            }
        }
        checkSpecialtyGetDateMandatory($currContent);
    }

    function isNeedControlName(assignSelectVal, licPerson, appType,specialPerson) {
        return /*'newOfficer' == assignSelectVal &&*/'1'==specialPerson||('1' != licPerson && 'APTY002' == appType);
    }

    function getPrsCallback() {
        return null;
    }

    function checkSpecialtyGetDateMandatoryEvent(currContent, specialityOther = '.specialityOther',
                                                 specialtyGetDateLabel = '.specialtyGetDateLabel') {
        let $currContent = getJqueryNode(currContent);
        if (isEmptyNode($currContent)) {
            return;
        }
        let $specialityOther = $currContent.find(specialityOther);
        if (isEmptyNode($specialityOther)) {
            return;
        }
        $specialityOther.unbind('blur');
        $specialityOther.on('blur', function () {
            checkSpecialtyGetDateMandatory(currContent, specialityOther, specialtyGetDateLabel);
        });
    }

    function checkSpecialtyGetDateMandatory(currContent, specialityOther = '.specialityOther',
                                            specialtyGetDateLabel = '.specialtyGetDateLabel') {
        let $currContent = getJqueryNode(currContent);
        if (isEmptyNode($currContent)) {
            return;
        }
        let $specialtyGetDateLabel = $currContent.find(specialtyGetDateLabel);
        if (isEmptyNode($specialtyGetDateLabel)) {
            return;
        }
        $specialtyGetDateLabel.find('.mandatory').remove();
        let $specialityOther = $currContent.find(specialityOther);
        if (!isEmpty(getValue($specialityOther))) {
            $specialtyGetDateLabel.append(' <span class="mandatory">*</span>');
        }
    }
</script>