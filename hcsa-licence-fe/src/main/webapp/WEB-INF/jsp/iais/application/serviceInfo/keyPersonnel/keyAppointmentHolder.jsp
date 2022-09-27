<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>

<c:set var="personList" value="${currSvcInfoDto.appSvcKeyAppointmentHolderDtoList}"/>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<div class="row form-horizontal">
    <c:if test="${AppSubmissionDto.needEditController }">
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <iais:row>
                <div class="text-right app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </iais:row>
        </c:if>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    </c:if>
    <iais:row>
        <div class="col-xs-12">
            <p class="app-title"><c:out value="${currStepName}"/></p>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
        </div>
    </iais:row>

    <c:choose>
        <c:when test="${empty personList && currStepConfig.mandatoryCount > 1}">
            <c:set var="personCount" value="${currStepConfig.mandatoryCount}"/>
        </c:when>
        <c:when test="${empty personList}">
            <c:set var="personCount" value="1"/>
        </c:when>
        <c:when test="${currStepConfig.mandatoryCount > personList.size() }">
            <c:set var="personCount" value="${currStepConfig.mandatoryCount}"/>
        </c:when>
        <c:otherwise>
            <c:set var="personCount" value="${personList.size()}"/>
        </c:otherwise>
    </c:choose>


    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="status">
        <c:set var="index" value="${status.index}"/>
        <c:set var="person" value="${personList[index]}"/>

        <%@include file="personnelDetail.jsp" %>
    </c:forEach>

    <c:if test="${!isRfi}">
        <div class="col-md-12 col-xs-12 addKeyAppointmentHolderDiv">
            <span class="addKeyAppointmentHolderBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Another <c:out value="${singleName}"/></span>
            </span>
        </div>
    </c:if>
</div>
<script type="text/javascript">
    $(function() {
        let psnContent = '.person-content';
        removePersonEvent(psnContent);
        assignSelectEvent(psnContent);
        psnEditEvent(psnContent);
        profRegNoEvent(psnContent);
        $('.addKeyAppointmentHolderBtn').on('click', function () {
            addPersonnel(psnContent);
        });
        initPerson(psnContent);
        <c:if test="${AppSubmissionDto.needEditController}">
        $(psnContent).each(function () {
            disablePsnContent($(this), psnContent);
        });
        </c:if>
    });

    function refreshPersonOthers($target, action) {
        if (action == 1) {
            removeTag('.addKeyAppointmentHolderDiv');
        } else {
            const maxCount = eval('${currStepConfig.maximumCount}');
            toggleTag('.addKeyAppointmentHolderDiv', $('div.person-content').length < maxCount);
        }
    }
</script>
<%--
<script>
    var initEnd = false;
    $(function () {
        refreshBtn();
        assignSel();
        addKeyAppointmentHolder();
        doEdit();
        addDisabled();
        removeKeyAppointmentHolder();
        $('.assignSel').closest('div.row').each(function (idx, ele) {
            if ($(ele).is(':visible')) {
                $(ele).find('select.assignSel').trigger('change');
            }
        });

        initNationality('div.keyAppointmentHolderContent', 'select[name^="idType"]', '.nationalityDiv');

        if ("${errormapIs}" == 'error') {
            $('.svcPsnEdit').trigger('click');
        }
        initEnd = true;
    });

    var assignSel = function () {
        $('.assignSel').change(function () {
            var assignSelVal = $(this).val();
            var $keyAppointmentHolder = $(this).closest('div.keyAppointmentHolderContent').find('div.keyAppointmentHolder');
            if ('-1' == assignSelVal) {
                $keyAppointmentHolder.addClass('hidden');
                if (initEnd) {
                    clearFields($keyAppointmentHolder);
                }
                toggleOnSelect($keyAppointmentHolder.find('select[name^="idType"]'), 'IDTYPE003',
                    $keyAppointmentHolder.find('.nationalityDiv'));
            } else if ('newOfficer' == assignSelVal) {
                $keyAppointmentHolder.removeClass('hidden');
                unDisabledPartPage($keyAppointmentHolder);
                if (initEnd) {
                    clearFields($keyAppointmentHolder);
                } else {
                    addDisabled();
                }
                toggleOnSelect($keyAppointmentHolder.find('select[name^="idType"]'), 'IDTYPE003',
                    $keyAppointmentHolder.find('.nationalityDiv'));
            } else {
                $keyAppointmentHolder.removeClass('hidden');
                var arr = $(this).val().split(',');
                var nationality = arr[0];
                var idType = arr[1];
                var idNo = arr[2];
                loadSelectKah($keyAppointmentHolder, nationality, idType, idNo);
            }
        });
    };

    var addKeyAppointmentHolder = function () {
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
                        $('.addKeyAppointmentHolderDiv').before(data.resultJson + '');
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

    var doEdit = function () {
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
        $content.each(function (k, v) {
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
        $content.each(function (index, v) {
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
            'nationality': nationality,
            'idType': idType,
            'idNo': idNo,
            'psnType': 'MAP'
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/person-info/svc-code',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                if (data == null) {
                    console.log("loadSelectKah data == null");
                    return;
                }
                fillKahForm($CurrentPsnEle, data);
                dismissWaiting();
            },
            'error': function () {
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
        toggleOnSelect($CurrentPsnEle.find('select[name^="idType"]'), 'IDTYPE003', $CurrentPsnEle.find('.nationalityDiv'));

        var isLicPerson = data.licPerson;
        if ('1' == isLicPerson) {
            //add disabled not add input disabled style
            personDisable($CurrentPsnEle, '', 'Y');
            var psnEditDto = data.psnEditDto;
            setPsnDisabled($CurrentPsnEle, psnEditDto);
        } else {
            unDisabledPartPage($CurrentPsnEle);
        }
        if (!initEnd || '1' != $CurrentPsnEle.closest('div.keyAppointmentHolderContent').find('input.isPartEdit').val()) {
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
        $(targetSelector).each(function (k, v) {
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
                var $errorSpan = $ele.find('span[name="iaisErrorMsg"][id="error_' + orgName + '"]');
                if ($errorSpan.length > 0) {
                    $errorSpan.prop('id', 'error_' + name + k);
                }
            });
        });
    }
</script>--%>
