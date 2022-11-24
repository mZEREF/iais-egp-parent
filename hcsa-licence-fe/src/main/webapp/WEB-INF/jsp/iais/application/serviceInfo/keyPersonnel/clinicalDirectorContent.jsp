<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>

<c:set var="pcdType" value="${ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR}"/>
<c:set var="personList" value="${currSvcInfoDto.appSvcClinicalDirectorDtoList}"/>
<style>
    label{font-weight: normal;!important;}
</style>
<iais:row cssClass="row col-xs-12 col-md-12 text-right">
    <c:if test="${AppSubmissionDto.needEditController }">
        <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <div class="app-font-size-16">
                <a class="back" id="RfcSkip" href="javascript:void(0);">
                    Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                </a>
            </div>
        </c:if>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    </c:if>
</iais:row>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>

<div class="row form-horizontal">
    <iais:row>
        <div class="col-xs-12">
            <p class="app-title"><c:out value="${currStepName}"/></p>
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

    <input type="hidden" name="cdLength" value="${pageLength}" />
    <c:forEach begin="0" end="${personCount-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}"/>
        <c:set var="person" value="${personList[index]}"/>
        <%@include file="clinicalDirectorDetail.jsp" %>
    </c:forEach>

    <c:if test="${!isRfi}">
        <div class="col-md-12 col-xs-12 addClinicalDirectorDiv">
            <span class="addClinicalDirectorBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add <c:out value="${singleName}"/></span>
            </span>
        </div>
    </c:if>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        let psnContent = '.person-content';
        removePersonEvent(psnContent);
        assignSelectEvent(psnContent);
        psnEditEvent(psnContent);
        profRegNoEvent(psnContent);
        $('.addClinicalDirectorBtn').on('click', function () {
            addPersonnel(psnContent);
        });
        <c:if test="${AppSubmissionDto.needEditController}">
        $(psnContent).each(function () {
            disablePsnContent($(this), psnContent);
        });
        </c:if>
        initPerson(psnContent);
    });

    function refreshPersonOthers($target, action) {
        if (action == 1) {
            removeTag('.addClinicalDirectorDiv');
        } else {
            const maxCount = eval('${currStepConfig.maximumCount}');
            toggleTag('.addClinicalDirectorDiv', $('div.person-content').length < maxCount);
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
        initFormNodes($currContent);
        clearFields($currContent);
        hideTag($currContent.find('.rfc-psn-detail'));
        hideTag($currContent.find('.edit-content'));
        unDisableContent($currContent.find('.assignSelDiv'));
        showTag($currContent.find('.assignSelDiv'));
        refreshPerson($currContent, $(target).length - 1);
        $(target + ':first').find('.psnHeader').html('1');
        $currContent.find('input.assignSelVal').val('-1');
        removePersonEvent(target);
        assignSelectEvent(target);
        profRegNoEvent(target);
        checkPersonContent($currContent, true);
        $currContent.find('.isPartEdit').val('1');
        $(target + '-edit').val('1');
        clearFields($currContent);
        otherSpecialEvent(target);
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
            var specialPerson = $currContent.find('input.specialPerson').val();
            var needControlName = isNeedControlName(assignSelectVal, licPerson, appType,specialPerson);
            checkProfRegNo($currContent, prgNo, needControlName);
        });
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
        if (!isEmpty(data)) {
            let length = specialty.length;
            let condation = false;
            for (let i = 0; i < length; i++) {
                if (isEmpty(specialty[i])) {
                    continue;
                }
                condation = true;
                if (condation) {
                    break;
                }
            }
            if (!condation) {
                $currContent.find('.relevantExperienceLabels .mandatory').remove();
                $currContent.find('.relevantExperienceLabels').append('<span class="mandatory">*</span>');
            }else {
                $currContent.find('.relevantExperienceLabels .mandatory').remove();
            }
        } else {
            $currContent.find('.relevantExperienceLabels .mandatory').remove();
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
            $currContent.find('.relevantExperienceLabels .mandatory').remove();
            dismissWaiting();
        } else if ('newOfficer' == assignVal) {
            showTag($content);
            if (!onlyInit) {
                clearFields($content);
                $currContent.find('.speciality p').html('');
                $currContent.find('.subSpeciality p').html('');
                $currContent.find('.qualification p').html('');
                $currContent.find('.relevantExperienceLabels .mandatory').remove();
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
            unDisableContent($content);
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
        fillFormData($content, data, prefix, $('div.' + cntClass).index($currContent), ['psnEditDto']);
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
        let profRegNo = data.profRegNo;
        if (isEmpty(profRegNo)){
            $currContent.find('.relevantExperienceLabels .mandatory').remove();
        }
        let condation = false;

        if (isEmpty(data.specialty)){
        }else {
            let length = data.specialty.length;
            for (let i = 0; i < length; i++) {
                if (isEmpty(data.specialty[i])) {
                    continue;
                }
                condation = true;
                if (condation) {
                    break;
                }
            }
        }
        if (!isEmpty(profRegNo) && !condation){
            $currContent.find('.relevantExperienceLabels .mandatory').remove();
            $currContent.find('.relevantExperienceLabels').append(' <span class="mandatory">*</span>');
        }
        dismissWaiting();
    }





</script>