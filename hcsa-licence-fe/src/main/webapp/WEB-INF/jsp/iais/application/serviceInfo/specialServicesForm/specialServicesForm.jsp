<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<div class="row form-horizontal">

    <c:set var="appPremSpecialisedDtoList" value="${appPremSpecialisedDtoList}"/>
    <c:set var="appSvcSpecialServiceInfoMap" value="${appSvcSpecialServiceInfoMap}"/>

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

    <c:forEach var="appSvcSpecialServiceInfo" items="${appSvcSpecialServiceInfoList}" varStatus="status">

        <iais:row>
            <div class="col-xs-12 app-title">
                <p><c:out value="${appSvcSpecialServiceInfo.premName}"/></p>
                <p>Address: <c:out value="${appSvcSpecialServiceInfo.premAddress}"/></p>
            </div>
        </iais:row>

        <c:forEach var="specialServiceSectionDto" items="${appSvcSpecialServiceInfo.specialServiceSectionDtoList}" varStatus="subSvcRelStatus">

            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-heading " id="business-heading"  role="tab">
                        <h4 class="panel-title">
                            <strong >
                                <c:out value="${specialServiceSectionDto.svcName}"/>
                            </strong>
                        </h4>
                    </div>

                    <c:forEach var="minCount" items="${specialServiceSectionDto.minCount}">
                        <c:if test="${minCount.key == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR}">
                            <c:set var="DirMinCount" value="${minCount.value}"/>
                        </c:if>
                        <c:if test="${minCount.key == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR}">
                            <c:set var="NurMinCount" value="${minCount.value}"/>
                        </c:if>
                    </c:forEach>
                    <c:forEach var="maxCount" items="${specialServiceSectionDto.maxCount}">
                        <c:if test="${maxCount.key == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR}">
                            <c:set var="DirMaxCount" value="${maxCount.value}"/>
                        </c:if>
                        <c:if test="${maxCount.key == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR}">
                            <c:set var="NurMaxCount" value="${maxCount.value}"/>
                        </c:if>
                    </c:forEach>

                    <div class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="business-heading">
                        <input type="hidden" class ="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                        <div class="row panel-body" style="padding-left: 6%">
                            <c:choose>
                                <c:when test="${DirMaxCount==0&&NurMaxCount==0}">
                                    <div class="panel-main-content">
                                        <p><h4><iais:message key="NEW_ACK039"/></h4></p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="panel-main-content">
                                        <c:choose>
                                            <c:when test="${specialServiceSectionDto.appSvcDirectorDtoList != null && specialServiceSectionDto.appSvcDirectorDtoList.size()>1}">
                                                <input class="length" type="hidden" name="${status.index}${subSvcRelStatus.index}DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                                                <c:set var="DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input class="length" type="hidden" name="${status.index}${subSvcRelStatus.index}DirectorDtoListLength" value="1"/>
                                                <c:set var="DirectorDtoListLength" value="1"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:forEach begin="0" end="${DirectorDtoListLength - 1}" step="1" varStatus="direStatus">
                                            <c:set var="index" value="${direStatus.index}"/>
                                            <c:set var="appSvcPersonnelDto" value="${specialServiceSectionDto.appSvcDirectorDtoList[index]}"/>
                                            <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}dir"/>
                                            <c:set var="title" value="Emergency Department Director"/>
                                            <%@include file="specialServiceDetail.jsp" %>
                                        </c:forEach>
                                        <iais:row>
                                            <div class="col-md-12 col-xs-12 addDiv">
                                                <input type="hidden" class ="disDiOrNu" name="disDiOrNu" value="Di"/>
                                                <span class="addBtn" style="color:deepskyblue;cursor:pointer;">
                                            <span style="">Add more</span>
                                        </span>
                                            </div>
                                        </iais:row>
                                    </div>

                                    <div class="panel-main-content">
                                        <c:choose>
                                            <c:when test="${specialServiceSectionDto.appSvcChargedNurseDtoList != null && specialServiceSectionDto.appSvcChargedNurseDtoList.size()>1}">
                                                <input class="length" type="hidden" name="${status.index}${subSvcRelStatus.index}NurseDtoListLength" value="${specialServiceSectionDto.appSvcChargedNurseDtoList.size()}"/>
                                                <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcChargedNurseDtoList.size()}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input class="length" type="hidden" name="${status.index}${subSvcRelStatus.index}NurseDtoListLength" value="1"/>
                                                <c:set var="NurseDtoListLength" value="1"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:forEach begin="0" end="${NurseDtoListLength - 1}" step="1" varStatus="nurStatus">
                                            <c:set var="index" value="${nurStatus.index}"/>
                                            <c:set var="appSvcPersonnelDto" value="${specialServiceSectionDto.appSvcChargedNurseDtoList[index]}"/>
                                            <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}nur"/>
                                            <c:set var="title" value="Emergency Department Nurse-in-charge"/>
                                            <%@include file="specialServiceDetail.jsp"%>
                                        </c:forEach>
                                        <iais:row>
                                            <div class="col-md-12 col-xs-12 addDiv">
                                                <input type="hidden" class ="disDiOrNu" name="disDiOrNu" value="Nu"/>
                                                <span class="addBtn" style="color:deepskyblue;cursor:pointer;">
                                            <span style="">Add more</span>
                                        </span>
                                            </div>
                                        </iais:row>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:forEach>
</div>

<%@include file="specialServicesFormFun.jsp" %>

<script>
    $(function() {
        $('.addBtn').on('click', function () {
            addPersonnel($(this).closest('div.panel-main-content'));
        });
    });
</script>
