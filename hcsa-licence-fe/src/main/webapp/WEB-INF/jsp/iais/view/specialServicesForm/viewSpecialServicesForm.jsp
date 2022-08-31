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
                <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcDirectorDtoList}" varStatus="direStatus">
                    <c:set var="index" value="${direStatus.index}"/>
                    <c:set var="DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                    <c:set var="title" value="Emergency Department Director ${DirectorDtoListLength > 1?index+1:''}"/>
                    <%@include file="viewSpecialServicesFromDetail.jsp"%>
                </c:forEach>
                <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcChargedNurseDtoList}" varStatus="nurStatus">
                    <c:set var="index" value="${nurStatus.index}"/>
                    <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcChargedNurseDtoList.size()}"/>
                    <c:set var="title" value="Emergency Department Nurse-in-charge ${NurseDtoListLength > 1?index+1:''}"/>
                    <%@include file="viewSpecialServicesFromDetail.jsp"%>
                </c:forEach>
                <c:set var="appSvcSuplmFormDto" value="${specialServiceSectionDto.appSvcSuplmFormDto}"/>
                <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
                    <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>
                    <c:if test="${batchSize > 0}">
                        <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
                        <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
                            <c:if test="${item.display}">
                                <%@ include file="../supplementaryForm/viewItem.jsp" %>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </c:forEach>
            </c:forEach>
        </c:forEach>
    </div>
</div>
