<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <c:forEach var="otherInfo" items="${currentPreviewSvcInfo.appSvcOtherInfoList}" varStatus="status">
            <c:set var="oldOtherInfo"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcOtherInfoList[status.index]}" />
            <iais:row>
                <div class="app-title">${otherInfo.premName}</div>
                <p class="font-18 bold">Address: ${otherInfo.premAddress}</p>
            </iais:row>
            <div class="row">
                <div class="">
                    <c:choose>
                        <c:when test="${(currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE) ||
                        (currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)}">
                            <%@include file="viewDentalService.jsp"%>
                            <c:if test="${currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE}">
                                <%@include file="viewTop.jsp"%>
                                <c:if test="${otherInfo.provideTop eq '1'}">
                                    <c:forEach var="practitioners" items="${otherInfo.otherInfoTopPersonPractitionersList}" varStatus="status">
                                        <c:set var="oldPractitioners" value="${oldOtherInfo.otherInfoTopPersonPractitionersList[status.index]}"/>
                                        <p class="col-xs-12" style="font-weight: bold;!important;">Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion&nbsp;
                                            <c:if test="${fn:length(otherInfo.otherInfoTopPersonPractitionersList)>1}">${status.index+1}</c:if></p>
                                        <%@include file="viewTopPersonPractitioners.jsp"%>
                                    </c:forEach>

                                    <c:forEach var="anaesthetists" items="${otherInfo.otherInfoTopPersonAnaesthetistsList}" varStatus="status">
                                        <c:set var="oldAnaesthetists" value="${oldOtherInfo.otherInfoTopPersonAnaesthetistsList[status.index]}"/>
                                        <p class="col-xs-12" style="font-weight: bold;!important;">Name, Professional Regn. No. and Qualification of anaesthetists&nbsp;
                                            <c:if test="${fn:length(otherInfo.otherInfoTopPersonAnaesthetistsList)>1}">${status.index+1}</c:if></p>
                                        <%@include file="viewTopPresonAnaesthetists.jsp"%>
                                    </c:forEach>

                                    <c:forEach var="nurses" items="${otherInfo.otherInfoTopPersonNursesList}" varStatus="status">
                                        <c:set var="oldNurses" value="${oldOtherInfo.otherInfoTopPersonNursesList[status.index]}"/>
                                        <p class="col-xs-12" style="font-weight: bold;!important;">Name and Qualifications of trained nurses&nbsp;
                                            <c:if test="${fn:length(otherInfo.otherInfoTopPersonNursesList)>1}">${status.index+1}</c:if></p>
                                        <%@include file="viewTopPersonNurses.jsp"%>
                                    </c:forEach>

                                    <c:forEach var="counsellors" items="${otherInfo.otherInfoTopPersonCounsellorsList}" varStatus="status">
                                        <c:set var="oldCounsellors" value="${oldOtherInfo.otherInfoTopPersonCounsellorsList[status.index]}"/>
                                        <p class="col-xs-12" style="font-weight: bold;!important;">Name and Qualifications of certified TOP counsellors&nbsp;
                                            <c:if test="${fn:length(otherInfo.otherInfoTopPersonCounsellorsList)>1}">${status.index+1}</c:if></p>
                                        <%@include file="viewTopPersonCounsellors.jsp"%>
                                    </c:forEach>

                                    <%@include file="viewOtherSupplementaryForm.jsp"%>
                                    <p class="col-xs-12">
                                        <strong>
                                            Documentation
                                        </strong>
                                    </p>
                                    <%@include file="viewDoucmentation.jsp"%>

                                    <%@include file="viewTopAbort.jsp"%>
                                </c:if>
                                <p class="col-xs-12">
                                    <strong>
                                        Yellow Fever Vaccination
                                    </strong>
                                </p>
                                <%@include file="viewYfVs.jsp"%>
                            </c:if>
                        </c:when>
                        <c:when test="${currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_RENAL_DIALYSIS_CENTRE}">
                            <%@include file="viewRenalDialysisCentreService.jsp"%>
                        </c:when>
                        <c:when test="${currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE}">
                            <%@include file="viewAmbulatorySurgicalCentreService.jsp"%>
                            <%@include file="viewTop.jsp"%>
                            <c:if test="${otherInfo.provideTop eq '1'}">
                                <c:forEach var="practitioners" items="${otherInfo.otherInfoTopPersonPractitionersList}" varStatus="status">
                                    <c:set var="oldPractitioners" value="${oldOtherInfo.otherInfoTopPersonPractitionersList[status.index]}"/>
                                    <p class="col-xs-12">Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion&nbsp;
                                        <c:if test="${fn:length(otherInfo.otherInfoTopPersonPractitionersList)>1}">${status.index+1}</c:if></p>
                                    <%@include file="viewTopPersonPractitioners.jsp"%>
                                </c:forEach>

                                <c:forEach var="anaesthetists" items="${otherInfo.otherInfoTopPersonAnaesthetistsList}" varStatus="status">
                                    <c:set var="oldAnaesthetists" value="${oldOtherInfo.otherInfoTopPersonAnaesthetistsList[status.index]}"/>
                                    <p class="col-xs-12">Name, Professional Regn. No. and Qualification of anaesthetists&nbsp;
                                        <c:if test="${fn:length(otherInfo.otherInfoTopPersonAnaesthetistsList)>1}">${status.index+1}</c:if></p>
                                    <%@include file="viewTopPresonAnaesthetists.jsp"%>
                                </c:forEach>

                                <c:forEach var="nurses" items="${otherInfo.otherInfoTopPersonNursesList}" varStatus="status">
                                    <c:set var="oldNurses" value="${oldOtherInfo.otherInfoTopPersonNursesList[status.index]}"/>
                                    <p class="col-xs-12">Name and Qualification of trained nurses&nbsp;
                                        <c:if test="${fn:length(otherInfo.otherInfoTopPersonNursesList)>1}">${status.index+1}</c:if></p>
                                    <%@include file="viewTopPersonNurses.jsp"%>
                                </c:forEach>

                                <c:forEach var="counsellors" items="${otherInfo.otherInfoTopPersonCounsellorsList}" varStatus="status">
                                    <c:set var="oldCounsellors" value="${oldOtherInfo.otherInfoTopPersonCounsellorsList[status.index]}"/>
                                    <p class="col-xs-12">Name, Professional Regn. No. and Qualification of certified TOP counsellors&nbsp;
                                        <c:if test="${fn:length(otherInfo.otherInfoTopPersonCounsellorsList)>1}">${status.index+1}</c:if></p>
                                    <%@include file="viewTopPersonCounsellors.jsp"%>
                                </c:forEach>

                                <%@include file="viewOtherSupplementaryForm.jsp"%>
                                <p class="col-xs-12">
                                    <strong>
                                        Documentation
                                    </strong>
                                </p>
                                <%@include file="viewDoucmentation.jsp"%>

                                <%@include file="viewTopAbort.jsp"%>
                            </c:if>
                            <p class="col-xs-12">
                                <strong>
                                    Yellow Fever Vaccination
                                </strong>
                            </p>
                            <%@include file="viewYfVs.jsp"%>
                        </c:when>
                        <c:when test="${currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL}">
                            <%@include file="viewTop.jsp"%>
                            <c:if test="${otherInfo.provideTop eq '1'}">
                                <c:forEach var="practitioners" items="${otherInfo.otherInfoTopPersonPractitionersList}" varStatus="status">
                                    <c:set var="oldPractitioners" value="${oldOtherInfo.otherInfoTopPersonPractitionersList[status.index]}"/>
                                    <p class="col-xs-12">Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion&nbsp;
                                        <c:if test="${fn:length(otherInfo.otherInfoTopPersonPractitionersList)>1}">${status.index+1}</c:if></p>
                                    <%@include file="viewTopPersonPractitioners.jsp"%>
                                </c:forEach>

                                <c:forEach var="anaesthetists" items="${otherInfo.otherInfoTopPersonAnaesthetistsList}" varStatus="status">
                                    <c:set var="oldAnaesthetists" value="${oldOtherInfo.otherInfoTopPersonAnaesthetistsList[status.index]}"/>
                                    <p class="col-xs-12">Name, Professional Regn. No. and Qualification of anaesthetists&nbsp;
                                        <c:if test="${fn:length(otherInfo.otherInfoTopPersonAnaesthetistsList)>1}">${status.index+1}</c:if></p>
                                    <%@include file="viewTopPresonAnaesthetists.jsp"%>
                                </c:forEach>

                                <c:forEach var="nurses" items="${otherInfo.otherInfoTopPersonNursesList}" varStatus="status">
                                    <c:set var="oldNurses" value="${oldOtherInfo.otherInfoTopPersonNursesList[status.index]}"/>
                                    <p class="col-xs-12">Name and Qualification of trained nurses&nbsp;
                                        <c:if test="${fn:length(otherInfo.otherInfoTopPersonNursesList)>1}">${status.index+1}</c:if></p>
                                    <%@include file="viewTopPersonNurses.jsp"%>
                                </c:forEach>

                                <c:forEach var="counsellors" items="${otherInfo.otherInfoTopPersonCounsellorsList}" varStatus="status">
                                    <c:set var="oldCounsellors" value="${oldOtherInfo.otherInfoTopPersonCounsellorsList[status.index]}"/>
                                    <p class="col-xs-12">Name, Professional Regn. No. and Qualification of certified TOP counsellors&nbsp;
                                        <c:if test="${fn:length(otherInfo.otherInfoTopPersonCounsellorsList)>1}">${status.index+1}</c:if></p>
                                    <%@include file="viewTopPersonCounsellors.jsp"%>
                                </c:forEach>

                                <%@include file="viewOtherSupplementaryForm.jsp"%>
                                <p class="col-xs-12">
                                    <strong>
                                        Documentation
                                    </strong>
                                </p>
                                <%@include file="viewDoucmentation.jsp"%>

                                <%@include file="viewTopAbort.jsp"%>
                            </c:if>
                            <p class="col-xs-12">
                                <strong>
                                    Yellow Fever Vaccination
                                </strong>
                            </p>
                            <%@include file="viewYfVs.jsp"%>
                        </c:when>
                        <c:when test="${currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}">
                            <%@include file="viewYfVs.jsp"%>
                        </c:when>
                    </c:choose>
                    <%@include file="viewOtherService.jsp"%>
                </div>
            </div>
            </c:forEach>
        </div>
    </div>
</div>