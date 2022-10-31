<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<div class="amended-service-info-gp form-horizontal min-row">
    <iais:row>
        <div class="col-xs-12">
            <p class="app-title"><c:out value="${currStepName}"/></p>
        </div>
    </iais:row>
    <c:set var="appSvcOtherInfoList" value="${currentPreviewSvcInfo.appSvcOtherInfoList}"/>
    <c:forEach var="appSvcOtherInfoDto" items="${appSvcOtherInfoList}">
        <iais:row>
            <div class="col-xs-12">
                <div class="app-title">${appSvcOtherInfoDto.premName}</div>
                <p class="font-18 bold">Address: ${appSvcOtherInfoDto.premAddress}</p>
            </div>
        </iais:row>
        <div class="amend-preview-info form-horizontal min-row">
            <c:choose>
                <c:when test="${(currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_DENTAL_SERVICE) || (currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_MEDICAL_SERVICE)}">
                    <%@include file="viewDentalService.jsp"%>
                    <c:if test="${currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_MEDICAL_SERVICE}">
                        <%@include file="viewOtherInformationTopPerson.jsp"%>
                        <%@include file="viewOtherForm.jsp"%>
                        <%@include file="viewDoucmentation.jsp"%>
                        <%@include file="viewAbort.jsp"%>
                        <%@include file="viewYfVs.jsp"%>
                    </c:if>
                </c:when>
                <c:when test="${currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_RENAL_DIALYSIS_CENTRE}">
                    <%@include file="viewRenalDialysisCentreService.jsp"%>
                </c:when>
                <c:when test="${currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_AMBULATORY_SURGICAL_CENTRE}">
                    <%@include file="viewAmbulatorySurgicalCentreService.jsp"%>
                    <%@include file="viewOtherInformationTopPerson.jsp"%>
                    <%@include file="viewOtherForm.jsp"%>
                    <%@include file="viewDoucmentation.jsp"%>
                    <%@include file="viewAbort.jsp"%>
                </c:when>
                <c:when test="${currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_ACUTE_HOSPITAL}">
                    <%@include file="viewOtherInformationTopPerson.jsp"%>
                    <%@include file="viewOtherForm.jsp"%>
                    <%@include file="viewDoucmentation.jsp"%>
                    <%@include file="viewAbort.jsp"%>
                    <%@include file="viewYfVs.jsp"%>
                </c:when>
                <c:when test="${currentPreviewSvcInfo.serviceName == AppServicesConsts.SERVICE_NAME_COMMUNITY_HOSPITAL}">
                    <%@include file="viewYfVs.jsp"%>
                </c:when>
                <c:otherwise>

                </c:otherwise>
            </c:choose>
            <%@include file="viewOtherService.jsp"%>
        </div>
    </c:forEach>
</div>

