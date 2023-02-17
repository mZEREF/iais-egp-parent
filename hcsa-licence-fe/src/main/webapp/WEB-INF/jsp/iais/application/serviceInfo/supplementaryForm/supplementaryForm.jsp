<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>

<div class="row form-horizontal normal-label person-content">
    <input type="hidden" class="not-refresh isPartEdit" name="isPartEdit" value="0"/>
    <input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
    <input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

    <c:if test="${AppSubmissionDto.needEditController }">
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <iais:row>
                <div class="text-right app-font-size-16 col-xs-12">
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
            <h2 class="app-title"><c:out value="${currStepName}"/></h2>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
        </div>
    </iais:row>
    <c:set var="appSvcSuplmFormList" value="${currSvcInfoDto.appSvcSuplmFormList}"/>
    <c:forEach var="appSvcSuplmFormDto" items="${appSvcSuplmFormList}">

        <iais:row>
            <div class="col-xs-12">
                <div class="app-title">${appSvcSuplmFormDto.premName}</div>
                <p class="font-18 bold">Address: ${appSvcSuplmFormDto.premAddress}</p>
            </div>
        </iais:row>

        <c:set var="itemPrefix" value="${appSvcSuplmFormDto.premisesVal}"/>

        <iais:row cssClass="edit-content">
            <c:if test="${canEdit}">
                <div class="text-right app-font-size-16 col-xs-12">
                    <a class="edit psnEdit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
            </c:if>
        </iais:row>

        <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}">
            <c:set var="count" value="${appSvcSuplmGroupDto.count}"/>
            <c:if test="${count > 0}">
                <c:set var="baseSize" value="${appSvcSuplmGroupDto.baseSize}"/>
                <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
                <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
                    <%@ include file="item.jsp" %>
                </c:forEach>
                <c:if test="${not empty groupId}">
                    <iais:value cssClass="col-xs-12 error_${groupId}">
                        <span class="error-msg " name="iaisErrorMsg" id="error_${groupId}"></span>
                    </iais:value>
                    <div class="form-group col-md-12 col-xs-12 addMoreDiv" data-group="${groupId}" data-prefix="${itemPrefix}">
                        <input class="not-clear" type="hidden" value="${count}" name="${itemPrefix}${groupId}"/>
                        <input class="not-clear" type="hidden" value="${appSvcSuplmGroupDto.maxCount}" name="${itemPrefix}${groupId}-max"/>
                        <span class="addMoreBtn" style="color:deepskyblue;cursor:pointer;">
                            <c:choose>
                                <c:when test="${AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE == currentSvcCode}">
                                    <span style="">Add more</span>
                                </c:when>
                                <c:otherwise>
                                    <span style="">+ Add more</span>
                                </c:otherwise>
                            </c:choose>
                            </span>
                    </div>
                </c:if>
            </c:if>
        </c:forEach>
    </c:forEach>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="itemFun.jsp" %>
<script type="text/javascript">
    $(function () {
        let svcContent = '.person-content';
        psnEditEvents(svcContent);
        <c:if test="${AppSubmissionDto.needEditController}">
        $(svcContent).each(function (k,v) {
            if ($("#errorMapIs").val() == 'error') {
                $(v).find('.error-msg').on('DOMNodeInserted', function () {
                    if ($(this).not(':empty')) {
                        $(v).find('.isPartEdit').val(1);
                        $('#isEditHiddenVal').val('1');
                        $(v).find('a.edit').trigger('click');
                    }
                });
            }
        });
        </c:if>
    });

    var psnEditEvents = function (target) {
        var $target = $(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.psnEdit').unbind('click');
        $target.find('.psnEdit').on('click', function () {
            doEditPsn($(this).closest(target), target);
        });
    }

    function doEditPsn($currContent, target) {
        if (isEmptyNode($currContent) || isEmpty(target)) {
            return;
        }
        if (hideEditBtn($currContent)) {
            return;
        }
        $currContent.find('.isPartEdit').val('1');
        console.log(target+'-edit','==========>')
        $(target + '-edit').val('1');
        hideTag($currContent.find('.edit-content'));
        unDisableContent($currContent);
    }

    function hideEditBtn($currContent) {
        let $target = $currContent.find('.psnEdit');
        if (isEmptyNode($target)) {
            return true;
        }
        return $target.is(':hidden');
    }
</script>
