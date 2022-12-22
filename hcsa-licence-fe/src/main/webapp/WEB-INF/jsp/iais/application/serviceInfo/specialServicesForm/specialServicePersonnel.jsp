<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>

<c:set var="max" value="${specialServiceSectionDto.maxCount}" />
<c:set var="min" value="${specialServiceSectionDto.minCount}" />
<c:forEach var="pMax" items="${max}">
    <c:set var="psnType" value="${pMax.key}" />
    <c:if test="${min[psnType] >= 0 && pMax.value>0}" >
        <c:choose>
            <c:when test="${psnType == ApplicationConsts.PERSONNEL_PSN_TYPE_CGO}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcCgoDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcSectionLeaderList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcNurseDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcRadiationSafetyOfficerDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_COMBINE}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcPersonnelDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcMedicalPhysicistDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcRadiationPhysicistDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_ONCOLOGIST}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcRadiationOncologist}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_DOSIMETRIST}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcMedicalDosimetrist}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_THERAPIST}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcRadiationTherapist}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcDirectorDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcNurseDirectorDtoList}" />
            </c:when>
            <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_CQMP}">
                <c:set var="personList" value="${specialServiceSectionDto.appSvcRadiationCqmp}" />
            </c:when>
        </c:choose>

        <c:choose>
            <c:when test="${empty personList && min[psnType]!=0}">
                <c:set var="personCount" value="${min[psnType]}"/>
            </c:when>
            <c:when test="${empty personList && min[psnType]==0}">
                <c:set var="personCount" value="1"/>
            </c:when>
            <c:when test="${min[psnType] > personList.size() }">
                <c:set var="personCount" value="${min[psnType]}"/>
            </c:when>
            <c:otherwise>
                <c:set var="personCount" value="${personList.size()}"/>
            </c:otherwise>
        </c:choose>
        <div class="panel-main-content normal-label ${psnType}">
            <c:choose>
                <c:when test="${psnType == ApplicationConsts.PERSONNEL_PSN_TYPE_CGO}">
                    <c:set var="title" value="${HcsaConsts.CLINICAL_GOVERNANCE_OFFICER}"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <p><h4 style="font-size: 16px"><iais:message key="NEW_ACK035"/></h4></p>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                        <c:set var="index" value="${vs.index}" />
                        <c:set var="person" value="${personList[index]}"/>
                        <c:set var="prepsn" value="${status.index}${subSvcRelStatus.index}cgo"/>
                        <%@include file="personnelDetail.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER}">
                    <c:set var="title" value="${HcsaConsts.SECTION_LEADER}"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <p><h4 style="font-size: 16px"><iais:message key="NEW_ACK030"/></h4></p>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                        <c:set var="index" value="${vs.index}" />
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}sl"/>
                        <c:set var="personTypeToShow" value="1"/>
                        <%@include file="sectionLeaderDetail.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE}">
                    <c:set var="title" value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_REGISTERED_NURSE}"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                        <c:set var="index" value="${vs.index}"/>
                        <c:set var="type" value="nic"/>
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}nic"/>
                        <%@include file="specialServicesPersonnels.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER}">
                    <c:set var="title" value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_RADIATION_SAFETY_OFFICER}"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                        <c:set var="index" value="${vs.index}" />
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}rso"/>
                        <c:set var="personTypeToShow" value="0"/>
                        <%@include file="servicePersonnelDetail.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_COMBINE}">
                    <c:set var="title" value="Service Personnel"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                        <c:set var="index" value="${vs.index}" />
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}sv"/>
                        <c:set var="personTypeToShow" value="1"/>
                        <%@include file="servicePersonnelDetail.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST}">
                    <c:set var="title" value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_MEDICAL_PHYSICIST}"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                        <c:set var="index" value="${vs.index}" />
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}mp"/>
                        <c:set var="personTypeToShow" value="1"/>
                        <%@include file="sectionLeaderDetail.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL}">
                    <c:set var="title" value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_STR_RADIOLOGY_PROFESSIONAL}"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                        <c:set var="index" value="${vs.index}" />
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}rp"/>
                        <c:set var="personTypeToShow" value="0"/>
                        <%@include file="sectionLeaderDetail.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_ONCOLOGIST}">
                    <c:set var="title" value="Radiation Oncologist"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <p><h4 style="font-size: 16px">Please indicate at least ${min[psnType]!=-1?min[psnType]:0} Radiation Oncologist</h4></p>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="ro">
                        <c:set var="index" value="${ro.index}"/>
                        <c:set value="ro" var="type"/>
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}ro"/>
                        <%@include file="specialServicesPersonnels.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_DOSIMETRIST}">
                    <c:set var="title" value="Medical Dosimetrist"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="md">
                        <c:set var="index" value="${md.index}"/>
                        <c:set value="md" var="type"/>
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}md"/>
                        <%@include file="specialServicesPersonnels.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_THERAPIST}">
                    <c:set var="title" value="Radiation Therapist"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <p><h4 style="font-size: 16px">Please indicate at least ${min[psnType]!=-1?min[psnType]:0} Radiation Therapist</h4></p>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="rt">
                        <c:set var="index" value="${rt.index}"/>
                        <c:set value="rt" var="type"/>
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}rt"/>
                        <%@include file="specialServicesPersonnels.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR}">
                    <c:set var="title" value="Emergency Department Director"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                        <c:set var="index" value="${vs.index}"/>
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}dir"/>
                        <c:set var="title" value="Emergency Department Director"/>
                        <%@include file="specialServiceDetail.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR}">
                    <c:set var="title" value="Emergency Department Nursing-in-charge"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="vs">
                        <c:set var="index" value="${vs.index}"/>
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}nur"/>
                        <%@include file="specialServiceDetail.jsp" %>
                    </c:forEach>
                </c:when>
                <c:when test="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_CQMP}">
                    <c:set var="title" value="Clinically Qualified Medical Physicist"/>
                    <label class="control-label control-set-font control-font-label">
                        <div class="app-title">
                            <c:out value="${title}"/>
                        </div>
                    </label>
                    <p><h4 style="font-size: 16px">Please indicate at least ${min[psnType]!=-1?min[psnType]:0} Clinically Qualified Medical Physicist</h4></p>
                    <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="cqmp">
                        <c:set var="index" value="${cqmp.index}"/>
                        <c:set value="cqmp" var="type"/>
                        <c:set var="appSvcPersonnelDto" value="${personList[index]}"/>
                        <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}cqmp"/>
                        <%@include file="specialServicesPersonnels.jsp" %>
                    </c:forEach>
                </c:when>
            </c:choose>
            <iais:row>
                <div class="col-md-12 col-xs-12 addDiv <c:if test="${personCount >= pMax.value}">hidden</c:if>">
                    <input type="hidden" class ="psnType" value="${psnType}"/>
                    <input type="hidden" class ="MaxCount" value="${pMax.value}"/>
                    <input type="hidden" class ="Length" name="${status.index}${subSvcRelStatus.index}${psnType}Length" value="${personCount}"/>
                    <c:if test="${!isRfi}">
                        <span class="addBtn" style="color:deepskyblue;cursor:pointer;">
                            <span style="">+ Add Another ${title}</span>
                        </span>
                    </c:if>
                </div>
            </iais:row>
        </div>
    </c:if>
</c:forEach>
<script type="text/javascript">
    $(function() {
        $('.addBtn').unbind('click');
        $('.addBtn').on('click', function () {
            var type=$(this).closest('div.addDiv').find('input.psnType').val();
            var maxCount=$(this).closest('div.addDiv').find('input.MaxCount').val();
            addPerson($(this).closest('div.panel-main-content'),type,maxCount);
        });
    });
</script>