<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach items="${currentPreviewSvcInfo.appSvcSpecialServiceInfoList}" var="appSvcSpecialServiceInfo" varStatus="status">
            <iais:row>
                <div class="col-xs-12 app-title">
                    <p><c:out value="${appSvcSpecialServiceInfo.premName}"/></p>
                    <p>Address: <c:out value="${appSvcSpecialServiceInfo.premAddress}"/></p>
                </div>
            </iais:row>

            <c:forEach var="specialServiceSectionDto" items="${appSvcSpecialServiceInfo.specialServiceSectionDtoList}" varStatus="subSvcRelStatus">

                <iais:row>
                    <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                        <p><strong><c:out value="${specialServiceSectionDto.svcName}"/></strong></p>
                    </div>
                </iais:row>

                <c:set var="DirMaxCount" value="0"/>
                <c:set var="NurMaxCount" value="0"/>
                <c:forEach var="maxCount" items="${specialServiceSectionDto.maxCount}">
                    <c:if test="${maxCount.key == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR}">
                        <c:set var="DirMaxCount" value="${maxCount.value}"/>
                    </c:if>
                    <c:if test="${maxCount.key == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR}">
                        <c:set var="NurMaxCount" value="${maxCount.value}"/>
                    </c:if>
                </c:forEach>

                <c:choose>
                    <c:when test="${DirMaxCount==0&&NurMaxCount==0}">
                        <div>
                            <p><h4><iais:message key="NEW_ACK039"/></h4></p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${specialServiceSectionDto.appSvcDirectorDtoList != null && specialServiceSectionDto.appSvcDirectorDtoList.size()>1}">
                                <c:set var="DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="DirectorDtoListLength" value="1"/>
                            </c:otherwise>
                        </c:choose>
                        <c:forEach begin="0" end="${DirectorDtoListLength - 1}" step="1" varStatus="direStatus">
                            <c:set var="index" value="${direStatus.index}"/>
                            <c:set var="appSvcPersonnelDto" value="${specialServiceSectionDto.appSvcDirectorDtoList[index]}"/>
                            <c:set var="title" value="Emergency Department Director ${DirectorDtoListLength > 1?index+1:''}"/>
                            <%@include file="viewSpecialServicesFromDetail.jsp"%>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${specialServiceSectionDto.appSvcChargedNurseDtoList != null && specialServiceSectionDto.appSvcChargedNurseDtoList.size()>1}">
                                <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcChargedNurseDtoList.size()}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="NurseDtoListLength" value="1"/>
                            </c:otherwise>
                        </c:choose>
                        <c:forEach begin="0" end="${NurseDtoListLength - 1}" step="1" varStatus="nurStatus">
                            <c:set var="index" value="${nurStatus.index}"/>
                            <c:set var="appSvcPersonnelDto" value="${specialServiceSectionDto.appSvcChargedNurseDtoList[index]}"/>
                            <c:set var="title" value="Emergency Department Nurse-in-charge ${NurseDtoListLength > 1?index+1:''}"/>
                            <%@include file="viewSpecialServicesFromDetail.jsp"%>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:forEach>
    </div>
</div>
