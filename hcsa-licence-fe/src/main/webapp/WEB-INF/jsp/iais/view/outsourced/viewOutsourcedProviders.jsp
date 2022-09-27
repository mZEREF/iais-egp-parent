<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<div class="amended-service-info-gp form-horizontal min-row">
    <iais:row>
        <div class="col-xs-12">
            <p class="app-title"><c:out value="${currStepName}"/></p>
        </div>

        <div class="amend-preview-info form-horizontal min-row">
            <c:set />
                <c:choose>
                    <c:when test="${(currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_DENTAL_SERVICE) || (currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_MEDICAL_SERVICE)}">
                    <c:when test="${currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_ACUTE_HOSPITAL}">
                        <%@include file="viewClinicalBoratoryContent.jsp"%>
                        <%@include file="viewRadiologicalServiceContent.jsp"%>
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>
        </div>

    </iais:row>
</div>
