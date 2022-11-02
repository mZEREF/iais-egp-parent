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
            <c:if test="${currentPreviewSvcInfo.serviceCode eq AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE
        || currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE}">
                <%@ include file="viewDentalService.jsp"%>
            </c:if>

            <c:if test="${currentPreviewSvcInfo.serviceCode eq AppServicesConsts.SERVICE_CODE_RENAL_DIALYSIS_CENTRE}">
                <%@include file="viewRenalDialysisCentreService.jsp" %>
            </c:if>

            <c:if test="${currentPreviewSvcInfo.serviceCode eq AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE}">
                <%@include file="viewAmbulatorySurgicalCentreService.jsp"%>
            </c:if>
            <c:if test="${currentPreviewSvcInfo.serviceCode eq AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE
        || currentPreviewSvcInfo.serviceCode eq AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE
        || currentPreviewSvcInfo.serviceCode eq AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL
        || currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}">
                <c:if test="${currentPreviewSvcInfo.serviceCode != AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}">
                    <%@include file="viewOtherInformationTopPerson.jsp"%>
                    <%@include file="viewOtherForm.jsp"%>
                    <%@include file="viewDoucmentation.jsp"%>
                    <%@include file="viewAbort.jsp"%>
                </c:if>
                <c:if test="${currentPreviewSvcInfo.serviceCode != AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE}">
                    <%@include file="viewYfVs.jsp"%>
                </c:if>
            </c:if>
            <%@include file="viewOtherService.jsp"%>
        </div>
    </c:forEach>
</div>

