<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<c:set var="pcdType" value="${ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR}"/>
<c:set var="personList" value="${currSvcInfoDto.appSvcClinicalDirectorDtoList}"/>
<style>
    label{font-weight: normal;!important;}
</style>
<iais:row cssClass="row col-xs-12 col-md-12 text-right">
    <c:if test="${AppSubmissionDto.needEditController }">
        <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
        <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
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
        <c:set var="needAddPsn" value="true"/>
        <c:choose>
            <c:when test="${currStepConfig.status =='CMSTAT003'}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${personCount >= currStepConfig.maximumCount}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
        </c:choose>
        <div class="col-md-12 col-xs-12 addClinicalDirectorDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addClinicalDirectorBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Another <c:out value="${singleName}"/></span>
            </span>
        </div>
    </c:if>
</div>
<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        //refresh();
    });


    $(function() {
        $('.addClinicalDirectorBtn').on('click', function () {
            addPersonnel('div.person-content');
        });
    });

    function refreshPersonOthers($target, k) {
        var maxCount = eval('${currStepConfig.maximumCount}');
        toggleTag('.addKeyAppointmentHolderDiv', $('div.person-content').length < maxCount);
    }

</script>