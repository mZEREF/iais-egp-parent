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
        profRegNoEvent(psnContent);
        psnEditEvent(psnContent);
        // init page
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
                $(v).find('.psnHeader').html('');
            }
            $(v).find('div.personnel-content').each(function (i, x) {
                var flag=isEmpty($(x).find('input.profRegNo').val())?false:true;
                disablePrsInfo($(x),flag,true);
            })

        });

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
    }

    function addPersonnel(target) {
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
        removePersonEvent();
        profRegNoEvent($currContent);

        var length =  $target.find('div.personnel-content').length;
        $target.find('input.length').val(length);
        dismissWaiting();
    }

    var removePersonEvent = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').on('click', function () {
            var $Content = $(this).closest('div.panel-main-content');
            $(this).closest('div.personnel-content').remove();
            let $currContent = $Content.find('div.personnel-content');
            $currContent.each(function (k, v) {
                refreshPerson($(v), k);
            });
            if ($currContent.length == 1) {
                $currContent.find('.psnHeader').html('');
            }
            var len =  $Content.find('div.personnel-content').length;
            if (len==0){
                $Content.find('input.length').val(1);
            }else {
                $Content.find('input.length').val(len);
            }
        });
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
            console.log('profRegNoEvent：'+needControlName);
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
                    $currContent.find('.profRegNo').val('');
                } else if ('-2' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                    $currContent.find('.profRegNo').val('');
                } else if (data.hasException) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0048" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                    $currContent.find('.profRegNo').val('');
                } else if ('401' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0054" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                    $currContent.find('.profRegNo').val('');
                } else {
                    canFill = true;
                }
                fillPrsInfo($currContent, canFill ? data : null, needControlName);
                disablePrsInfo($currContent, canFill,needControlName);
                if (typeof callback === 'function') {
                    callback($currContent, canFill ? data : null);
                }
                dismissWaiting();
            },
            'error': function () {
                fillPrsInfo($currContent, null, needControlName);
                disablePrsInfo($currContent, false,needControlName);
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

    function isNeedControlName(assignSelectVal, licPerson, appType) {
        return /*'newOfficer' == assignSelectVal &&*/ '1' != licPerson && 'APTY002' == appType;
    }
</script>