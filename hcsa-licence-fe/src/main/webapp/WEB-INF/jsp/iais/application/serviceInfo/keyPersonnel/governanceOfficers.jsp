<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>

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

    <c:set var="personList" value="${currSvcInfoDto.appSvcCgoDtoList}"/>
    <c:set var="personConfig" value="${currStepConfig}"/>

    <c:choose>
        <c:when test="${empty personList && personConfig.mandatoryCount > 1}">
            <c:set var="personCount" value="${personConfig.mandatoryCount}"/>
        </c:when>
        <c:when test="${empty personList}">
            <c:set var="personCount" value="1"/>
        </c:when>
        <c:when test="${personConfig.mandatoryCount > personList.size() }">
            <c:set var="personCount" value="${personConfig.mandatoryCount}"/>
        </c:when>
        <c:otherwise>
            <c:set var="personCount" value="${personList.size()}"/>
        </c:otherwise>
    </c:choose>

    <c:set var="isCGO" value="true"/>
    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="status">
        <c:set var="index" value="${status.index}" />
        <c:set var="person" value="${personList[index]}"/>

        <%@include file="personnelDetail.jsp" %>
    </c:forEach>


    <c:if test="${!isRfi}">
        <c:set var="needAddPsn" value="true"/>
        <c:choose>
            <c:when test="${personConfig.status =='CMSTAT003'}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${personCount >= personConfig.maximumCount}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
        </c:choose>
        <div class="col-md-12 col-xs-12 addPersonnelDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addPersonnelBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add <c:out value="${singleName}"/></span>
            </span>
        </div>
    </c:if>
</div>
<script type="text/javascript">
    $(function() {
        $('.addPersonnelBtn').on('click', function () {
            addPersonnel();
        });
    });
</script>
