<div class="row">
    <div class="col-xs-12 col-md-12 text-right">
        <c:if test="${AppSubmissionDto.needEditController }">
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                <div class="app-font-size-16">
                    <a class="back" id="RfcSkip">Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em></a>
                </div>
            </c:if>
            <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
        </c:if>
    </div>
</div>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

<div class="row">
    <div class="col-xs-12">
        <p style="font-weight: 600;font-size: 2.2rem">Key Appointment Holder</p>
        <hr>
        <p>Key appointment holders are the governing body and generally the controlling mind and will of the licensee.They have the authority to provide high-level management and clinical direction but do not directly influence day-to-day operations on the ground</p>

        <c:choose>
            <c:when test="${empty AppSvcKeyAppointmentHolderDtoList}">
                <c:set var="pageLength" value="1"/>
            </c:when>
            <c:when test="${keyAppointmentHolderConfigDto.mandatoryCount > AppSvcKeyAppointmentHolderDtoList.size() }">
                <c:set var="pageLength" value="${keyAppointmentHolderConfigDto.mandatoryCount}"/>
            </c:when>
            <c:otherwise>
                <c:set var="pageLength" value="${AppSvcKeyAppointmentHolderDtoList.size()}"/>
            </c:otherwise>
        </c:choose>

        <input type="hidden" name="keyAppointmentHolderLength" value="${pageLength}" />
        <c:forEach begin="0" end="${pageLength-1}" step="1" varStatus="keyAppointmentHolderStatus">
            <c:set var="index" value="${keyAppointmentHolderStatus.index}" />
            <c:set var="AppSvcKeyAppointmentHolderDto" value="${AppSvcKeyAppointmentHolderDtoList[index]}"/>
            <div class="keyAppointmentHolderContent">
                <input type="hidden" class="isPartEdit" name="isPartEdit${index}" value="0"/>
                <input type="hidden" class="cgoIndexNo" name="cgoIndexNo${index}" value="${AppSvcKeyAppointmentHolderDto.cgoIndexNo}"/>

                <div class="col-md-12 col-xs-12">
                    <div class="edit-content">
                        <c:if test="${'true' == canEdit}">
                            <div class="text-right app-font-size-16"><a id="edit" class="svcPsnEdit"><em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit</a></div>
                        </c:if>
                    </div>
                </div>

                <div class="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <label  class="control-label control-set-font control-font-label">
                                <div class="cgo-header">
                                    <strong>Key Appointment Holder <label class="assign-psn-item"><c:if test="${AppSvcKeyAppointmentHolderDtoList.size() > 1}">${index+1}</c:if></label></strong>
                                </div>
                            </label>
                        </div>

                        <div class="col-md-7 col-xs-7 text-right">
                            <c:if test="${index - keyAppointmentHolderConfigDto.mandatoryCount >=0}">
                                <div class="removeKeyAppointmentHolderBtn">
                                    <h4 class="text-danger">
                                        <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                                    </h4>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-6 control-label formtext col-md-5">
                                <label  class="control-label control-set-font control-font-label">Add/Assign a Key Appointment Holder</label>
                                <span class="mandatory">*</span>
                            </div>

                            <div class="col-sm-5 col-md-7" id="assignSelect">
                                <div class="">
                                    <iais:select cssClass="assignSel"  name="assignSel${index}" options="KeyAppointmentHolderAssignSelect" needSort="false" value="${AppSvcKeyAppointmentHolderDto.assignSelect}"></iais:select>
                                    <span id="error_assignSelect${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="keyAppointmentHolder hidden">
                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Name</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-3 col-xs-12">
                                    <iais:select cssClass="salutation"  name="salutation${index}" codeCategory="CATE_ID_SALUTATION" value="${AppSvcKeyAppointmentHolderDto.salutation}" firstOption="Please Select"></iais:select>
                                </div>

                                <div class="col-sm-4 col-xs-12">
                                    <iais:input cssClass="name" maxLength="66" type="text" name="name${index}" value="${AppSvcKeyAppointmentHolderDto.name}"></iais:input>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                    <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">ID No.
                                        <span class="mandatory">*</span>
                                    </label>
                                </div>
                                <div class="col-sm-3 col-xs-12">
                                    <div class="">
                                        <iais:select cssClass="idType"  name="idType${index}" needSort="false" value="${AppSvcKeyAppointmentHolderDto.idType}" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE"></iais:select>
                                    </div>
                                </div>
                                <div class="col-sm-4 col-xs-12">
                                    <iais:input cssClass="idNo" maxLength="9" type="text" name="idNo${index}" value="${AppSvcKeyAppointmentHolderDto.idNo}"></iais:input>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-3 control-label formtext col-md-5">
                                </div>
                                <div class="col-sm-7">
                                    <span class="error-msg" id="error_idTypeNo${index}" name="iaisErrorMsg"></span>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

        </c:forEach>

        <div class="addKeyAppointmentHolderDiv">
            <span class="addKeyAppointmentHolderBtn <c:if test="${canEdit}">hidden</c:if>" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Key Appointment Holder</span>
            </span>
        </div>

    </div>
</div>
<script>
    var initEnd = false;
    $(function () {
        assignSel();
        addKeyAppointmentHolder();
        doEdit();
        addDisabled();
        removeKeyAppointmentHolder();
        $('select.assignSel').trigger('change');
        initEnd = true;
    });

    var assignSel= function () {
        $('.assignSel').change(function () {
            var assignSelVal = $(this).val();
            var $keyAppointmentHolder = $(this).closest('div.keyAppointmentHolderContent').find('div.keyAppointmentHolder');
            if('-1' == assignSelVal){
                $keyAppointmentHolder.addClass('hidden');
                if (initEnd){
                    clearFields($keyAppointmentHolder);
                }
            }else if('newOfficer' == assignSelVal){
                $keyAppointmentHolder.removeClass('hidden');
                unDisabledPartPage($keyAppointmentHolder);
                if (initEnd){
                    clearFields($keyAppointmentHolder);
                }
            }else{
                $keyAppointmentHolder.removeClass('hidden');
                var arr = $(this).val().split(',');
                var idType = arr[0];
                var idNo = arr[1];
                loadSelectKah($keyAppointmentHolder, idType, idNo);
            }
        });
    };

    var addKeyAppointmentHolder = function(){
        $('.addKeyAppointmentHolderBtn').unbind('click');
        $('.addKeyAppointmentHolderBtn').click(function () {
            showWaiting();
            var keyAppointmentHolderLength = $('.keyAppointmentHolderContent').length;
            $.ajax({
                url: '${pageContext.request.contextPath}/keyAppointmentHolder-html',
                dataType: 'json',
                data: {
                    "keyAppointmentHolderLength": keyAppointmentHolderLength
                },
                type: 'POST',
                success: function (data) {
                    if ('200' == data.resCode) {
                        $('.addKeyAppointmentHolderDiv').before(data.resultJson+'');
                        removeKeyAppointmentHolder();
                        refreshKeyAppointmentHolder();
                        assignSel();
                        $('#isEditHiddenVal').val('1');
                    }
                    dismissWaiting();
                },
                error: function () {
                    dismissWaiting();
                }
            });
        });
    };

    var doEdit = function (){
        $('.svcPsnEdit').click(function () {
            var $currContent = $(this).closest('div.keyAppointmentHolderContent');
            $currContent.find('input.isPartEdit').val('1');
            $currContent.find('.edit-content').addClass('hidden');
            $currContent.find('input[type="text"]').prop('disabled', false);
            $currContent.find('div.nice-select').removeClass('disabled');
            $currContent.find('input[type="text"]').css('border-color', '');
            $currContent.find('input[type="text"]').css('color', '');
            $('#isEditHiddenVal').val('1');
        });
    };

    function addDisabled() {
        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        if (('APTY005' == appType || 'APTY004' == appType) || '1' == rfiObj) {
            disabledPage();
        }
    }

    var removeKeyAppointmentHolder = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            showWaiting();
            var keyAppointmentHolderLength = $('.keyAppointmentHolderContent').length;
            if (keyAppointmentHolderLength <= '${keyAppointmentHolderConfigDto.mandatoryCount}') {
                dismissWaiting();
                return;
            }
            var $currkeyAppointmentHolderContent = $(this).closest('div.keyAppointmentHolderContent');
            $currkeyAppointmentHolderContent.remove();
            $('#isEditHiddenVal').val('1');
            refreshKeyAppointmentHolder();
            dismissWaiting();
        });
    }

    function refreshKeyAppointmentHolder() {
        var keyAppointmentHolderLength = $('.keyAppointmentHolderContent').length;
        $('input[name="keyAppointmentHolderLength"]').val(keyAppointmentHolderLength);
        if (keyAppointmentHolderLength <= '${keyAppointmentHolderConfigDto.mandatoryCount}') {
            $('.removeKeyAppointmentHolderBtn').hide();
        } else {
            $('.removeKeyAppointmentHolderBtn').show();
        }
        if ("${AppSubmissionDto.appType == 'APTY002' || 'true' == canEdit}"){
            // display add more
            if (keyAppointmentHolderLength < '${keyAppointmentHolderConfigDto.maximumCount}') {
                $('.addKeyAppointmentHolderDiv').removeClass('hidden');
            }else{//hidden add more
                $('.addKeyAppointmentHolderDiv').addClass('hidden');
            }
        }
        if (keyAppointmentHolderLength <= 1) {
            $('.keyAppointmentHolderContent:eq(0) .assign-psn-item').html('');
        }
    }

    var loadSelectKah = function ($CurrentPsnEle, idType, idNo) {
        showWaiting();
        var jsonData = {
            'idType':idType,
            'idNo':idNo,
            'psnType':'MAP'
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/person-info/svc-code',
            'dataType':'json',
            'data':jsonData,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    return;
                }
                fillKahForm($CurrentPsnEle, data);
                dismissWaiting();
            },
            'error':function () {
                dismissWaiting();
            }
        });
    };

    var fillKahForm = function ($CurrentPsnEle, data) {
        <!--salutation-->
        var salutation = data.salutation;
        if (salutation == null || salutation == 'undefined' || salutation == '') {
            salutation = '';
        }
        $CurrentPsnEle.find('.salutation').val(salutation);
        var salutationVal = $CurrentPsnEle.find('option[value="' + salutation + '"]').html();
        $CurrentPsnEle.find('.salutation').next().find('.current').html(salutationVal);
        <!--name-->
        $CurrentPsnEle.find('.name').val(data.name);
        <!-- idType-->
        var idType = data.idType;
        if (idType == null || idType == 'undefined' || idType == '') {
            idType = '';
        }
        $CurrentPsnEle.find('.idType').val(idType);
        var idTypeVal = $CurrentPsnEle.find('option[value="' + idType + '"]').html();
        $CurrentPsnEle.find('.idType').next().find('.current').html(idTypeVal);
        <!-- idNo-->
        $CurrentPsnEle.find('.idNo').val(data.idNo);

        var isLicPerson = data.licPerson;
        if('1' == isLicPerson){
            //add disabled not add input disabled style
            personDisable($CurrentPsnEle, '', 'Y');
            var psnEditDto = data.psnEditDto;
            setPsnDisabled($CurrentPsnEle, psnEditDto);
        }else{
            unDisabledPartPage($CurrentPsnEle);
        }
    };
</script>