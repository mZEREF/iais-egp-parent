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

    $('div.personnel-content').each(function (k, v) {
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

</script>