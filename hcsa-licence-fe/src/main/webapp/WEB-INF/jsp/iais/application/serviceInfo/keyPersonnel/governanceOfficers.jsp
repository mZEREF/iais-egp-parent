<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<c:set var="psnType" value="${ApplicationConsts.PERSONNEL_PSN_TYPE_CGO}"/>
<c:set var="personList" value="${currSvcInfoDto.appSvcCgoDtoList}"/>

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
            <h2 class="app-title"><c:out value="${currStepName}"/></h2>
            <p><h4><iais:message key="NEW_ACK035"/></h4></p>
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
        <c:set var="index" value="${status.index}" />
        <c:set var="person" value="${personList[index]}"/>
        <%@include file="personnelDetail.jsp" %>
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
        <div class="col-md-12 col-xs-12 addPersonnelDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addPersonnelBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Another <c:out value="${singleName}"/></span>
            </span>
        </div>
    </c:if>
</div>
<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<script type="text/javascript">
    $(function() {
        $('.addPersonnelBtn').on('click', function () {
            addPersonnel('div.person-content');
        });
    });

    function refreshPersonOthers($target, k) {
        var maxCount = eval('${currStepConfig.maximumCount}');
        toggleTag('.addPersonnelDiv', $('div.person-content').length < maxCount);
    }
</script>
