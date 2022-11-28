<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach items="${currentPreviewSvcInfo.appSvcSpecialServiceInfoList}" var="appSvcSpecialServiceInfo" varStatus="status">
            <c:set var="oldappSvcSpecialServiceInfo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSpecialServiceInfoList[status.index]}"/>
            <iais:row>
                <div class="col-xs-12">
                    <span class="newVal" attr="${appSvcSpecialServiceInfo.premName}:${appSvcSpecialServiceInfo.premAddress}">
                        <div class="app-title"><c:out value="${appSvcSpecialServiceInfo.premName}"/></div>
                        <p class="font-18 bold"><c:if test="${not empty appSvcSpecialServiceInfo.premAddress}"> Address: </c:if>
                            <c:out value="${appSvcSpecialServiceInfo.premAddress}"/></p>
                    </span>
                </div>
                <div class="col-xs-12">
                    <span class="oldVal" style="display: none" attr="${oldappSvcSpecialServiceInfo.premName}:${oldappSvcSpecialServiceInfo.premAddress}">
                        <p><c:out value="${oldappSvcSpecialServiceInfo.premName}"/></p>
                        <p><c:if test="${not empty oldappSvcSpecialServiceInfo.premAddress}"> Address: </c:if>
                            <c:out value="${oldappSvcSpecialServiceInfo.premAddress}"/></p>
                    </span>
                </div>
            </iais:row>
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <c:forEach var="specialServiceSectionDto" items="${appSvcSpecialServiceInfo.specialServiceSectionDtoList}" varStatus="subSvcRelStatus">
                    <c:set var="oldspecialServiceSectionDto" value="${oldappSvcSpecialServiceInfo.specialServiceSectionDtoList[subSvcRelStatus.index]}"/>
                    <div class="panel panel-default svc-content">
                        <div class="panel-heading " role="tab">
                            <h4 class="panel-title">
                                <a role="button" class="collapsed" data-toggle="collapse" href="#${status.index}${subSvcRelStatus.index}SSI" aria-expanded="true" aria-controls="${status.index}${subSvcRelStatus.index}SSI">
                                    <c:out value="${specialServiceSectionDto.newSvcName}"/>
                                </a>
                            </h4>
                        </div>
                        <div id="${status.index}${subSvcRelStatus.index}SSI" class="panel-collapse collapse">
                            <div class="panel-body" style="margin-left: -50px !important;">
                                <c:choose>
                                    <c:when test="${specialServiceSectionDto.emptyDto}">
                                        <div class="panel-main-content">
                                            <p><h4><iais:message key="NEW_ACK039"/></h4></p>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcCgoDtoList}" varStatus="cgoStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcCgoDtoList[cgoStatus.index]}"/>
                                            <c:set var="index" value="${cgoStatus.index}"/>
                                            <c:set var="cgoDtoListLength" value="${specialServiceSectionDto.appSvcCgoDtoList.size()}"/>
                                            <c:set var="title" value="${HcsaConsts.CLINICAL_GOVERNANCE_OFFICER} ${cgoDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewClinicalDirectorDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcSectionLeaderList}" varStatus="slStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcSectionLeaderList[slStatus.index]}"/>
                                            <c:set var="index" value="${slStatus.index}"/>
                                            <c:set var="slDtoListLength" value="${specialServiceSectionDto.appSvcSectionLeaderList.size()}"/>
                                            <c:set var="isShowMore" value="1"/>
                                            <c:set var="title" value="${HcsaConsts.SECTION_LEADER} ${slDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcNurseDtoList}" varStatus="nicStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcNurseDtoList[nicStatus.index]}"/>
                                            <c:set var="index" value="${nicStatus.index}"/>
                                            <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcNurseDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="type" value="nic"/>
                                            <c:set var="title" value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_REGISTERED_NURSE} ${NurseDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcRadiationSafetyOfficerDtoList}" varStatus="rsoStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcRadiationSafetyOfficerDtoList[rsoStatus.index]}"/>
                                            <c:set var="index" value="${rsoStatus.index}"/>
                                            <c:set var="rsoDtoListLength" value="${specialServiceSectionDto.appSvcRadiationSafetyOfficerDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="title" value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_RADIATION_SAFETY_OFFICER} ${rsoDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcPersonnelDtoList}" varStatus="svStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcPersonnelDtoList[svStatus.index]}"/>
                                            <c:set var="index" value="${svStatus.index}"/>
                                            <c:set var="svDtoListLength" value="${specialServiceSectionDto.appSvcPersonnelDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="title" value="Service Personnel  ${svDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <%--<c:forEach var="person" items="${specialServiceSectionDto.appSvcDiagnosticRadiographerDtoList}" varStatus="drStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcDiagnosticRadiographerDtoList[drStatus.index]}"/>
                                            <c:set var="index" value="${drStatus.index}"/>
                                            <c:set var="drDtoListLength" value="${specialServiceSectionDto.appSvcDiagnosticRadiographerDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="title" value="Diagnostic Radiographer ${drDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>--%>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcMedicalPhysicistDtoList}" varStatus="mpStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcMedicalPhysicistDtoList[mpStatus.index]}"/>
                                            <c:set var="index" value="${mpStatus.index}"/>
                                            <c:set var="mpDtoListLength" value="${specialServiceSectionDto.appSvcMedicalPhysicistDtoList.size()}"/>
                                            <c:set var="isShowMore" value="1"/>
                                            <c:set var="title" value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_MEDICAL_PHYSICIST} ${mpDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcRadiationPhysicistDtoList}" varStatus="rpStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcRadiationPhysicistDtoList[rpStatus.index]}"/>
                                            <c:set var="index" value="${rpStatus.index}"/>
                                            <c:set var="rpDtoListLength" value="${specialServiceSectionDto.appSvcRadiationPhysicistDtoList.size()}"/>
                                            <c:set var="isShowMore" value="1"/>
                                            <c:set var="title" value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_RADIOLOGY_PROFESSIONAL} ${rpDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcRadiationOncologist}" varStatus="roStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcRadiationOncologist[roStatus.index]}"/>
                                            <c:set var="index" value="${roStatus.index}"/>
                                            <c:set var="roDtoListLength" value="${specialServiceSectionDto.appSvcRadiationOncologist.size()}"/>
                                            <c:set var="type" value="ro"/>
                                            <c:set var="title" value="Radiation Oncologist ${roDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSpecialServicesSsiPersonnel.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcRadiationCqmp}" varStatus="cqpmStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcRadiationCqmp[cqpmStatus.index]}"/>
                                            <c:set var="index" value="${cqpmStatus.index}"/>
                                            <c:set var="cqpmDtoListLength" value="${specialServiceSectionDto.appSvcRadiationCqmp.size()}"/>
                                            <c:set var="type" value="cqpm"/>
                                            <c:set var="title" value="Clinically Qualified Medical Physicist ${cqpmDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSpecialServicesSsiPersonnel.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcMedicalDosimetrist}" varStatus="mdStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcMedicalDosimetrist[mdStatus.index]}"/>
                                            <c:set var="index" value="${mdStatus.index}"/>
                                            <c:set var="mdDtoListLength" value="${specialServiceSectionDto.appSvcMedicalDosimetrist.size()}"/>
                                            <c:set var="type" value="md"/>
                                            <c:set var="title" value="Medical Dosimetrist ${mdDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSpecialServicesSsiPersonnel.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcRadiationTherapist}" varStatus="rtStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcRadiationTherapist[rtStatus.index]}"/>
                                            <c:set var="index" value="${rtStatus.index}"/>
                                            <c:set var="rtDtoListLength" value="${specialServiceSectionDto.appSvcRadiationTherapist.size()}"/>
                                            <c:set var="type" value="rt"/>
                                            <c:set var="title" value="Radiation Therapist ${rtDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSpecialServicesSsiPersonnel.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcDirectorDtoList}" varStatus="direStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcDirectorDtoList[direStatus.index]}"/>
                                            <c:set var="index" value="${direStatus.index}"/>
                                            <c:set var="DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="title" value="Emergency Department Director ${DirectorDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSpecialServicesFromDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcNurseDirectorDtoList}" varStatus="nurStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcNurseDirectorDtoList[nurStatus.index]}"/>
                                            <c:set var="index" value="${nurStatus.index}"/>
                                            <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcNurseDirectorDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="title" value="Emergency Department Nursing-in-charge ${NurseDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSpecialServicesFromDetail.jsp"%>
                                        </c:forEach>

                                        <%--<c:forEach var="person" items="${specialServiceSectionDto.appSvcNMTechnologistDtoList}" varStatus="nmStatus">
                                            <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcNMTechnologistDtoList[nmStatus.index]}"/>
                                            <c:set var="nmDtoListLength" value="${specialServiceSectionDto.appSvcNMTechnologistDtoList.size()}"/>
                                            <c:set var="index" value="${nmStatus.index}"/>
                                            <c:set var="isShowMore" value="1"/>
                                            <c:set var="title" value="Nuclear Medicine Technologist ${nmDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>--%>

                                        <div style="margin-left: -18px !important;">
                                            <c:set var="appSvcSuplmFormDto" value="${specialServiceSectionDto.appSvcSuplmFormDto}"/>
                                            <c:set var="oldAppSvcSuplmFormDto" value="${oldspecialServiceSectionDto.appSvcSuplmFormDto}"/>
                                            <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
                                                <table class="col-xs-12">
                                                    <c:set var="oldAppSvcSuplmGroupDto" value="${oldAppSvcSuplmFormDto.appSvcSuplmGroupDtoList[status.index]}"/>
                                                    <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>
                                                    <c:if test="${batchSize > 0}">
                                                        <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
                                                        <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="statuss">
                                                            <c:set var="oldItem" value="${oldAppSvcSuplmGroupDto.appSvcSuplmItemDtoList[statuss.index]}"/>
                                                            <c:if test="${item.display || oldItem.display}">
                                                                <%@ include file="../supplementaryForm/viewItem.jsp" %>
                                                            </c:if>
                                                        </c:forEach>
                                                    </c:if>
                                                </table>
                                            </c:forEach>
                                        </div>

                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
    </div>
</div>
