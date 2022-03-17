<c:set var="isRfi" value="${requestInformationConfig != null}"/>

<input id="isEditHiddenVal" type="hidden" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<div class="row">
    <div class="col-xs-12 col-md-12 text-right">
        <c:if test="${AppSubmissionDto.needEditController }">
            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                <div class="app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
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
        <p><iais:message key="NEW_ACK029"/></p>
        <p><span class="error-msg" name="iaisErrorMsg" id="error_psnMandatory"></span></p>
        <c:choose>
            <c:when test="${empty AppSvcKeyAppointmentHolderDtoList && keyAppointmentHolderConfigDto.mandatoryCount > 1}">
                <c:set var="pageLength" value="${keyAppointmentHolderConfigDto.mandatoryCount}"/>
            </c:when>
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
                <input type="hidden" class="isPartEdit" name="isPartEdit${index}" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
                <input type="hidden" class="indexNo" name="indexNo${index}" value="${AppSvcKeyAppointmentHolderDto.indexNo}"/>

                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-6 control-label formtext col-md-8">
                                <div class="cgo-header">
                                    <strong>Key Appointment Holder <label class="assign-psn-item"><c:if test="${pageLength > 1}">${index+1}</c:if></label></strong>
                                </div>
                            </div>

                            <div class="col-md-4 col-xs-7 text-right">
                                <c:if test="${index - keyAppointmentHolderConfigDto.mandatoryCount >=0}">
                                    <div class="removeKeyAppointmentHolderBtn">
                                        <h4 class="text-danger">
                                            <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                                        </h4>
                                    </div>
                                </c:if>
                            </div>

                            <c:if test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType || requestInformationConfig != null}">
                                <div class="col-sm-10">
                                    <label class="control-font-label">
                                        <c:if test="${!empty AppSvcKeyAppointmentHolderDto.name && !empty AppSvcKeyAppointmentHolderDto.idNo && !empty AppSvcKeyAppointmentHolderDto.idType}">
                                            ${AppSvcKeyAppointmentHolderDto.name}, ${AppSvcKeyAppointmentHolderDto.idNo} (<iais:code code="${AppSvcKeyAppointmentHolderDto.idType}"/>)
                                        </c:if>
                                    </label>
                                </div>
                                <div class="col-sm-2 text-right">
                                    <div class="edit-content">
                                        <c:if test="${'true' == canEdit}">
                                            <div class="text-right app-font-size-16">
                                                <a id="edit" class="svcPsnEdit" href="javascript:void(0);">
                                                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                                </a>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>

                        </div>
                    </div>
                </div>

                <div class="row <c:if test="${AppSubmissionDto.needEditController && '-1' != AppSvcKeyAppointmentHolderDto.assignSelect && not empty AppSvcKeyAppointmentHolderDto.assignSelect}">hidden</c:if>">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-6 control-label formtext col-md-5">
                                <label  class="control-label control-set-font control-font-label">Add/Assign a Key Appointment Holder</label>
                                <span class="mandatory">*</span>
                            </div>

                            <div class="col-sm-5 col-md-7">
                                <div class="">
                                    <iais:select cssClass="assignSel"  name="assignSel${index}" options="KeyAppointmentHolderAssignSelect" needSort="false" value="${AppSvcKeyAppointmentHolderDto.assignSelect}"></iais:select>
                                    <span id="error_assignSelect${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="keyAppointmentHolder">
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
                                    <iais:input cssClass="name" maxLength="110" type="text" name="name${index}" value="${AppSvcKeyAppointmentHolderDto.name}"></iais:input>
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
                                    <iais:input cssClass="idNo" maxLength="20" type="text" name="idNo${index}"
                                                value="${AppSvcKeyAppointmentHolderDto.idNo}"></iais:input>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row nationalityDiv">
                        <div class="control control-caption-horizontal">
                            <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-6 control-label formtext col-md-5">
                                    <label  class="control-label control-set-font control-font-label">Country of issuance</label>
                                    <span class="mandatory">*</span>
                                </div>
                                <div class="col-sm-5 col-md-7">
                                    <div class="">
                                        <iais:select firstOption="Please Select" name="nationality${index}" codeCategory="CATE_ID_NATIONALITY"
                                                     cssClass="nationality" value="${AppSvcKeyAppointmentHolderDto.nationality}" />
                                    </div>
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
            <span class="addKeyAppointmentHolderBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Key Appointment Holder</span>
            </span>
        </div>

    </div>
</div>
<script>
    var initEnd = false;
    $(function () {
        refreshBtn();
        assignSel();
        addKeyAppointmentHolder();
        doEdit();
        addDisabled();
        removeKeyAppointmentHolder();
        $('.assignSel').closest('div.row').each(function (idx, ele){
            if ($(ele).is(':visible')) {
                $(ele).find('select.assignSel').trigger('change');
            }
        });

        initNationality('div.keyAppointmentHolderContent', 'select[name^="idType"]', '.nationalityDiv');

        if("${errormapIs}"=='error'){
            $('.svcPsnEdit').trigger('click');
        }
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
                toggleIdType($keyAppointmentHolder.find('select[name^="idType"]'), $keyAppointmentHolder.find('.nationalityDiv'));
            }else if('newOfficer' == assignSelVal){
                $keyAppointmentHolder.removeClass('hidden');
                unDisabledPartPage($keyAppointmentHolder);
                if (initEnd){
                    clearFields($keyAppointmentHolder);
                }else {
                    addDisabled();
                }
                toggleIdType($keyAppointmentHolder.find('select[name^="idType"]'), $keyAppointmentHolder.find('.nationalityDiv'));
            }else{
                $keyAppointmentHolder.removeClass('hidden');
                var arr = $(this).val().split(',');
                var nationality = arr[0];
                var idType = arr[1];
                var idNo = arr[2];
                loadSelectKah($keyAppointmentHolder, nationality, idType, idNo);
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
                        $('#isEditHiddenVal').val('1');
                        removeKeyAppointmentHolder();
                        refreshKeyAppointmentHolder();
                        assignSel();

                        initNationality('div.keyAppointmentHolderContent:last', 'select[name^="idType"]', '.nationalityDiv');
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
            console.log(".svcPsnEdit:click")
            var $currContent = $(this).closest('div.keyAppointmentHolderContent');
            $currContent.find('input.isPartEdit').val('1');
            $currContent.find('select.assignSel').val('newOfficer');
            $currContent.find('.edit-content').addClass('hidden');
            $currContent.find('input[type="text"]').prop('disabled', false);
            $currContent.find('div.nice-select').removeClass('disabled');
            $currContent.find('input[type="text"]').css('border-color', '');
            $currContent.find('input[type="text"]').css('color', '');
            $('#isEditHiddenVal').val('1');
            refreshBtn();
        });
    };

    function addDisabled() {
        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        console.log("addDisabled start: appType=" + appType + "rfiObj=" + rfiObj);
        if (('APTY005' == appType || 'APTY004' == appType) || '1' == rfiObj) {
            console.log("disabledPage start");
            disabledPage();
        }
    }

    var removeKeyAppointmentHolder = function () {
        $('.removeBtn').unbind('click');
        $('.removeBtn').click(function () {
            console.log(".removeBtn:click")
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
        console.log("refreshKeyAppointmentHolder start")
        var $content = $('div.keyAppointmentHolderContent');
        myRefreshIndex($content);
        var keyAppointmentHolderLength = $content.length;
        $('input[name="keyAppointmentHolderLength"]').val(keyAppointmentHolderLength);
        $content.each(function (k,v) {
            if (keyAppointmentHolderLength <= 1 && k == 0) {
                $(this).find('.assign-psn-item').html('');
            } else {
                $(this).find('.assign-psn-item').html(k + 1);
            }
        });
        refreshBtn();
    }

    function refreshBtn() {
        var $content = $('div.keyAppointmentHolderContent');
        var kahLength = $content.length;
        $('input[name="keyAppointmentHolderLength"]').val(kahLength);
        console.info("length: " + kahLength);
        $content.each(function (index,v) {
            let isPartEdit = $(v).find(".isPartEdit").val();
            if (index < '${keyAppointmentHolderConfigDto.mandatoryCount}') {
                $(v).find('.removeKeyAppointmentHolderBtn').remove();
            } else {
                $(v).find('.removeKeyAppointmentHolderBtn').show();
            }
        });

        <c:if test="${!isRfi && (AppSubmissionDto.appType == 'APTY002' || canEdit)}" var="canShowAddBtn">
        // display add more
        if (kahLength < '${keyAppointmentHolderConfigDto.maximumCount}') {
            $('.addKeyAppointmentHolderDiv').show();
        } else {//hidden add more
            $('.addKeyAppointmentHolderDiv').hide();
        }
        </c:if>
        <c:if test="${!canShowAddBtn}">
        $('.addKeyAppointmentHolderDiv').remove();
        </c:if>
    }

    var loadSelectKah = function ($CurrentPsnEle, nationality, idType, idNo) {
        showWaiting();
        var jsonData = {
            'nationality':nationality,
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
                    console.log("loadSelectKah data == null");
                    return;
                }
                fillKahForm($CurrentPsnEle, data);
                dismissWaiting();
            },
            'error':function () {
                console.log("loadSelectKah error");
                dismissWaiting();
            }
        });
    };

    var fillKahForm = function ($CurrentPsnEle, data) {
        console.log("fillKahForm start");
        console.log("fillKahForm data:" + data);
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
        fillValue($CurrentPsnEle.find('select[name^="idType"]'), data.idType);
        <!-- idNo-->
        $CurrentPsnEle.find('.idNo').val(data.idNo);
        <!-- Nationality -->
        fillValue($CurrentPsnEle.find('select[name^="nationality"]'), data.nationality);
        toggleIdType($CurrentPsnEle.find('select[name^="idType"]'), $CurrentPsnEle.find('.nationalityDiv'));

        var isLicPerson = data.licPerson;
        if('1' == isLicPerson){
            //add disabled not add input disabled style
            personDisable($CurrentPsnEle, '', 'Y');
            var psnEditDto = data.psnEditDto;
            setPsnDisabled($CurrentPsnEle, psnEditDto);
        }else{
            unDisabledPartPage($CurrentPsnEle);
        }
        if(!initEnd || '1' != $CurrentPsnEle.closest('div.keyAppointmentHolderContent').find('input.isPartEdit').val()){
            addDisabled();
        }
        console.log("fillKahForm end")
    };

    function myRefreshIndex(targetSelector) {
        if (isEmpty(targetSelector)) {
            return;
        }
        if ($(targetSelector).length == 0) {
            return;
        }
        $(targetSelector).each(function (k,v) {
            var $ele = $(v);
            var $selector = $ele.find(':input');
            if ($selector.length == 0) {
                $ele.text(k + 1);
                return;
            }
            $selector.each(function () {
                var type = this.type, tag = this.tagName.toLowerCase(), $input = $(this);
                var orgName = $input.attr('name');
                var orgId = $input.attr('id');
                if (isEmpty(orgName)) {
                    orgName = orgId;
                }
                if (isEmpty(orgName)) {
                    return;
                }
                var result = /([a-zA-Z_]*)/g.exec(orgName);
                var name = !isEmpty(result) && result.length > 0 ? result[0] : orgName;
                $input.prop('name', name + k);
                if (orgName == orgId) {
                    $input.prop('id', name + k);
                }
                var $errorSpan = $ele.find('span[name="iaisErrorMsg"][id="error_'+ orgName +'"]');
                if ($errorSpan.length > 0) {
                    $errorSpan.prop('id', 'error_' + name + k);
                }
            });
        });
    }
</script>