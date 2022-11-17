<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach items="${currentPreviewSvcInfo.appSvcSpecialServiceInfoList}" var="appSvcSpecialServiceInfo" varStatus="status">
            <iais:row>
                <div class="col-xs-12 app-title">
                    <p><c:out value="${appSvcSpecialServiceInfo.premName}"/></p>
                    <p><c:if test="${not empty appSvcSpecialServiceInfo.premAddress}"> Address: </c:if><c:out value="${appSvcSpecialServiceInfo.premAddress}"/></p>
                </div>
            </iais:row>

            <div class="panel-group" id="specialService" role="tablist" aria-multiselectable="true">
                <c:forEach var="specialServiceSectionDto" items="${appSvcSpecialServiceInfo.specialServiceSectionDtoList}" varStatus="subSvcRelStatus">
                    <div class="panel panel-default">
                        <div class="panel-heading " role="tab">
                            <h4 class="panel-title">
                                <a role="button" class="collapsed" data-toggle="collapse" href="#${status.index}${subSvcRelStatus.index}SSI" aria-expanded="true" aria-controls="${status.index}${subSvcRelStatus.index}SSI">
                                    <c:out value="${specialServiceSectionDto.newSvcName}"/>
                                </a>
                            </h4>
                        </div>
                        <div id="${status.index}${subSvcRelStatus.index}SSI" class="panel-collapse collapse">
                            <input type="hidden" class ="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                            <div class="panel-body">
                                <c:choose>
                                    <c:when test="${specialServiceSectionDto.emptyDto}">
                                        <div class="panel-main-content">
                                            <p><h4><iais:message key="NEW_ACK039"/></h4></p>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcCgoDtoList}" varStatus="cgoStatus">
                                            <c:set var="index" value="${cgoStatus.index}"/>
                                            <c:set var="cgoDtoListLength" value="${specialServiceSectionDto.appSvcCgoDtoList.size()}"/>
                                            <c:set var="title" value="Clinical Governance Officer ${cgoDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewClinicalDirectorDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcSectionLeaderList}" varStatus="slStatus">
                                            <c:set var="index" value="${slStatus.index}"/>
                                            <c:set var="slDtoListLength" value="${specialServiceSectionDto.appSvcSectionLeaderList.size()}"/>
                                            <c:set var="isShowMore" value="1"/>
                                            <c:set var="title" value="Section Leader ${slDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcNurseDtoList}" varStatus="nicStatus">
                                            <c:set var="index" value="${nicStatus.index}"/>
                                            <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcNurseDtoList.size()}"/>
                                            <c:set var="title" value="Registered Nurse ${NurseDtoListLength > 1?index+1:''}"/>
                                            <c:set value="nic" var="type"/>
                                            <%@include file="viewSsiPersonnelDetail.jsp" %>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcRadiationSafetyOfficerDtoList}" varStatus="rsoStatus">
                                            <c:set var="index" value="${rsoStatus.index}"/>
                                            <c:set var="rsoDtoListLength" value="${specialServiceSectionDto.appSvcRadiationSafetyOfficerDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="isShowSaluation" value="1"/>
                                            <c:set var="title" value="Radiation Safety Officer ${rsoDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcPersonnelDtoList}" varStatus="svStatus">
                                            <c:set var="index" value="${svStatus.index}"/>
                                            <c:set var="svDtoListLength" value="${specialServiceSectionDto.appSvcPersonnelDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="isShowSaluation" value="1"/>
                                            <c:set var="title" value="Service Personnel  ${svDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <%--<c:forEach var="person" items="${specialServiceSectionDto.appSvcDiagnosticRadiographerDtoList}" varStatus="drStatus">
                                            <c:set var="index" value="${drStatus.index}"/>
                                            <c:set var="drDtoListLength" value="${specialServiceSectionDto.appSvcDiagnosticRadiographerDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="title" value="Diagnostic Radiographer ${drDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>--%>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcMedicalPhysicistDtoList}" varStatus="mpStatus">
                                            <c:set var="index" value="${mpStatus.index}"/>
                                            <c:set var="mpDtoListLength" value="${specialServiceSectionDto.appSvcMedicalPhysicistDtoList.size()}"/>
                                            <c:set var="isShowMore" value="1"/>
                                            <c:set var="isShowSaluation" value="1"/>
                                            <c:set var="title" value="Medical Physicist ${mpDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="person" items="${specialServiceSectionDto.appSvcRadiationPhysicistDtoList}" varStatus="rpStatus">
                                            <c:set var="index" value="${rpStatus.index}"/>
                                            <c:set var="rpDtoListLength" value="${specialServiceSectionDto.appSvcRadiationPhysicistDtoList.size()}"/>
                                            <c:set var="isShowMore" value="1"/>
                                            <c:set var="isShowSaluation" value="0"/>
                                            <c:set var="title" value="Radiation Physicist ${rpDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>

                                        <%--<c:forEach var="person" items="${specialServiceSectionDto.appSvcNMTechnologistDtoList}" varStatus="nmStatus">
                                            <c:set var="index" value="${rpStatus.index}"/>
                                            <c:set var="nmDtoListLength" value="${specialServiceSectionDto.appSvcNMTechnologistDtoList.size()}"/>
                                            <c:set var="isShowMore" value="1"/>
                                            <c:set var="title" value="Nuclear Medicine Technologist ${nmDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSectionLeaderDetail.jsp"%>
                                        </c:forEach>--%>

                                        <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcRadiationOncologist}" varStatus="roStatus">
                                            <c:set var="index" value="${roStatus.index}"/>
                                            <c:set var="roDtoListLength" value="${specialServiceSectionDto.appSvcRadiationOncologist.size()}"/>
                                            <c:set var="title" value="Radiation Oncologist ${roDtoListLength > 1?index+1:''}"/>
                                            <c:set value="ro" var="type"/>
                                            <%@include file="viewSsiPersonnelDetail.jsp" %>
                                        </c:forEach>

                                        <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcRadiationCqmp}" varStatus="cqmpStatus">
                                            <c:set var="index" value="${cqmpStatus.index}"/>
                                            <c:set var="cqmpDtoListLength" value="${specialServiceSectionDto.appSvcRadiationCqmp.size()}"/>
                                            <c:set var="title" value="Clinically Qualified Medical Physicist ${cqmpDtoListLength > 1?index+1:''}"/>
                                            <c:set value="cqmp" var="type"/>
                                            <%@include file="viewSsiPersonnelDetail.jsp" %>
                                        </c:forEach>

                                        <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcMedicalDosimetrist}" varStatus="mdStatus">
                                            <c:set var="index" value="${mdStatus.index}"/>
                                            <c:set var="mdDtoListLength" value="${specialServiceSectionDto.appSvcMedicalDosimetrist.size()}"/>
                                            <c:set var="title" value="Medical Dosimetrist ${mdDtoListLength > 1?index+1:''}"/>
                                            <c:set value="md" var="type"/>
                                            <%@include file="viewSsiPersonnelDetail.jsp" %>
                                        </c:forEach>

                                        <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcRadiationTherapist}" varStatus="rtStatus">
                                            <c:set var="index" value="${rtStatus.index}"/>
                                            <c:set var="rtDtoListLength" value="${specialServiceSectionDto.appSvcRadiationTherapist.size()}"/>
                                            <c:set var="title" value="Radiation Therapist ${rtDtoListLength > 1?index+1:''}"/>
                                            <c:set value="rt" var="type"/>
                                            <%@include file="viewSsiPersonnelDetail.jsp" %>
                                        </c:forEach>

                                        <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcDirectorDtoList}" varStatus="direStatus">
                                            <c:set var="index" value="${direStatus.index}"/>
                                            <c:set var="DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="title" value="Emergency Department Director ${DirectorDtoListLength > 1?index+1:''}"/>
                                            <%@include file="viewSpecialServicesFromDetail.jsp"%>
                                        </c:forEach>

                                        <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcNurseDirectorDtoList}" varStatus="nurStatus">
                                            <c:set var="index" value="${nurStatus.index}"/>
                                            <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcNurseDirectorDtoList.size()}"/>
                                            <c:set var="isShowMore" value="0"/>
                                            <c:set var="title" value="Emergency Department Nursing-in-charge ${NurseDtoListLength > 1?index+1:''}"/>
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
