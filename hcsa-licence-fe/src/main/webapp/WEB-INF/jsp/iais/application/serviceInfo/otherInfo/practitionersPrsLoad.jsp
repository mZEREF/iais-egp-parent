<div class="modal" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
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

    function practitionersProfRegNoEvent(target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.profRegNo').unbind('blur');
        $target.find('.profRegNo').on('blur', function () {
            showWaiting();
            var prgNo = $(this).val();
            var $currContent = $(this).closest(target);
            let prefix = $currContent.data('prefix');
            let index = $currContent.data('seq');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            if (isEmpty(index)) {
                index = "";
            }
            console.log("prefix - " + prefix + " : " + index);
            checkProfRegNo($currContent, prgNo, prefix,index);
        });
    };

    function checkProfRegNo($currContent, prgNo, prefix,index) {
        showWaiting();
        let callback = getPrsCallback();
        if (isEmpty(prgNo)) {
            if (typeof callback === 'function') {
                callback($currContent, null);
            } else {
                fillPrsInfo($currContent, null, prefix,index);
                disablePrsInfo($currContent, false, prefix,index);
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
                    callback($currContent, canFill ? data : null, prefix, index);
                } else {
                    fillPrsInfo($currContent, canFill ? data : null, prefix, index);
                    disablePrsInfo($currContent, canFill, prefix, index);
                }
                dismissWaiting();
            },
            'error': function () {
                if (typeof callback === 'function') {
                    callback($currContent, null);
                } else {
                    fillPrsInfo($currContent, null, prefix,index);
                    disablePrsInfo($currContent, false, prefix,index);
                }
                dismissWaiting();
            }
        });
    }

    function fillPrsInfo($currContent, data, prefix, index) {
        var name = '';
        var specialty = '';
        var qualification = '';
        if (!isEmpty(data)) {
            if (!isEmpty(data.name)) {
                name = data.name;
                console.log("set name = " + name)
            }
            if (!isEmpty(data.specialty)) {
                specialty = data.specialty;
            }
            if (!isEmpty(data.qualification)) {
                qualification = data.qualification;
            }
        }
        $('input.namePro[data-prefix="' + prefix + '"]').val(name);
        $currContent.find('input.name').prop('name',prefix+'name'+index).val(name);
        $currContent.find('.speciality input').html(specialty);
        $currContent.find('.qualification input').html(qualification);
    }

    function disablePrsInfo($currContent, flag, prefix, index) {
        if (flag) {
            var name = $('input.namePro[data-prefix="' + prefix + '"]').val();
            if (!isEmpty(name)) {
                disableContent($currContent.find('.name').prop('name',prefix+'name'+index));
            }
        } else {
            unDisableContent($currContent.find('.name').prop('name',prefix+'name'+index));
        }
    }

    function getPrsCallback() {
        return null;
    }
</script>