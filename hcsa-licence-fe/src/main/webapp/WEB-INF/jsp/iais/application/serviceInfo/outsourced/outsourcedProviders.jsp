<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<div class="row normal-label">
    <c:choose>
        <c:when test="${(currSvcInfoDto.serviceName == AppServicesConsts.SERVICE_NAME_ACUTE_HOSPITAL) || (currSvcInfoDto.serviceName == AppServicesConsts.SERVICE_NAME_BLOOD_BANKING)}">
            <%@include file="outsourceService.jsp"%>
            <c:if test="${!empty outSourceParam}">
                <%@include file="clinicalLaboratory.jsp"%>
                <%@include file="radiologicalService.jsp"%>
            </c:if>
        </c:when>
        <c:otherwise>
        </c:otherwise>
    </c:choose>
</div>