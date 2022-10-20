<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
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
                                <c:out value="${specialServiceSectionDto.svcName}"/>
                            </h4>
                        </div>
                        <div class="panel-collapse collapse in " role="tabpanel">
                            <div class="panel-body" style="margin-left: -50px !important;">
                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcCgoDtoList}" varStatus="cgoStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcCgoDtoList[cgoStatus.index]}"/>
                                    <c:set var="cgoDtoListLength" value="${specialServiceSectionDto.appSvcCgoDtoList.size()}"/>
                                    <c:set var="title" value="Clinical Governance Officer ${cgoDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewClinicalDirectorDetail.jsp"%>
                                </c:forEach>

                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcSectionLeaderList}" varStatus="slStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcSectionLeaderList[slStatus.index]}"/>
                                    <c:set var="slDtoListLength" value="${specialServiceSectionDto.appSvcSectionLeaderList.size()}"/>
                                    <c:set var="isShowMore" value="1"/>
                                    <c:set var="title" value="Section Leader ${slDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewSectionLeaderDetail.jsp"%>
                                </c:forEach>

                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcNurseDtoList}" varStatus="nicStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcNurseDtoList[nicStatus.index]}"/>
                                    <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcNurseDtoList.size()}"/>
                                    <c:set var="isShowMore" value="1"/>
                                    <c:set var="title" value="Nurse in Charge ${NurseDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewSpecialServicesFromDetail.jsp"%>
                                </c:forEach>

                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcRadiationSafetyOfficerDtoList}" varStatus="rsoStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcRadiationSafetyOfficerDtoList[rsoStatus.index]}"/>
                                    <c:set var="rsoDtoListLength" value="${specialServiceSectionDto.appSvcRadiationSafetyOfficerDtoList.size()}"/>
                                    <c:set var="isShowMore" value="0"/>
                                    <c:set var="title" value="Radiation Safety Officer ${rsoDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewSectionLeaderDetail.jsp"%>
                                </c:forEach>

                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcDiagnosticRadiographerDtoList}" varStatus="drStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcDiagnosticRadiographerDtoList[drStatus.index]}"/>
                                    <c:set var="drDtoListLength" value="${specialServiceSectionDto.appSvcDiagnosticRadiographerDtoList.size()}"/>
                                    <c:set var="isShowMore" value="0"/>
                                    <c:set var="title" value="Diagnostic Radiographer ${drDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewSectionLeaderDetail.jsp"%>
                                </c:forEach>

                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcMedicalPhysicistDtoList}" varStatus="mpStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcMedicalPhysicistDtoList[mpStatus.index]}"/>
                                    <c:set var="mpDtoListLength" value="${specialServiceSectionDto.appSvcMedicalPhysicistDtoList.size()}"/>
                                    <c:set var="isShowMore" value="1"/>
                                    <c:set var="title" value="Medical Physicist ${mpDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewSectionLeaderDetail.jsp"%>
                                </c:forEach>

                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcRadiationPhysicistDtoList}" varStatus="rpStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcRadiationPhysicistDtoList[rpStatus.index]}"/>
                                    <c:set var="rpDtoListLength" value="${specialServiceSectionDto.appSvcRadiationPhysicistDtoList.size()}"/>
                                    <c:set var="isShowMore" value="1"/>
                                    <c:set var="title" value="Radiation Physicist ${rpDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewSectionLeaderDetail.jsp"%>
                                </c:forEach>

                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcNMTechnologistDtoList}" varStatus="nmStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcNMTechnologistDtoList[nmStatus.index]}"/>
                                    <c:set var="nmDtoListLength" value="${specialServiceSectionDto.appSvcNMTechnologistDtoList.size()}"/>
                                    <c:set var="isShowMore" value="1"/>
                                    <c:set var="title" value="Nuclear Medicine Technologist ${nmDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewSectionLeaderDetail.jsp"%>
                                </c:forEach>

                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcDirectorDtoList}" varStatus="direStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcDirectorDtoList[direStatus.index]}"/>
                                    <c:set var="DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                                    <c:set var="isShowMore" value="0"/>
                                    <c:set var="title" value="Emergency Department Director ${DirectorDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewSpecialServicesFromDetail.jsp"%>
                                </c:forEach>

                                <c:forEach var="person" items="${specialServiceSectionDto.appSvcNurseDirectorDtoList}" varStatus="nurStatus">
                                    <c:set var="oldPerson" value="${oldspecialServiceSectionDto.appSvcNurseDirectorDtoList[nurStatus.index]}"/>
                                    <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcNurseDirectorDtoList.size()}"/>
                                    <c:set var="isShowMore" value="0"/>
                                    <c:set var="title" value="Emergency Department Nurse Director ${NurseDtoListLength > 1?index+1:''}"/>
                                    <%@include file="viewSpecialServicesFromDetail.jsp"%>
                                </c:forEach>
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
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
    </div>
</div>
