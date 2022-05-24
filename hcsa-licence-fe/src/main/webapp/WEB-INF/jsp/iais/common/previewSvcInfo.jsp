<style type="text/css">
    table {
        table-layout: fixed;
        word-break: break-all;
    }
</style>
<c:forEach items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}" var="hcsaServiceStepSchemeDto">
    <c:set var="currStepName" value="${hcsaServiceStepSchemeDto.stepName}" scope="request"/>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST001'}">
        <%@include file="previewSvcDisciplines.jsp"%>
    </c:if><%--STEP_LABORATORY_DISCIPLINES--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST002'}">
        <%@include file="previewSvcGovernanceOfficer.jsp"%>
    </c:if><%--STEP_CLINICAL_GOVERNANCE_OFFICERS--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST003'}">
        <%@include file="previewSvcAllocation.jsp"%>
    </c:if><%--STEP_DISCIPLINE_ALLOCATION--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST004'}">
        <%@include file="previewSvcPrincipalOfficers.jsp"%>
    </c:if><%--STEP_PRINCIPAL_OFFICERS--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST005'}">
        <%@include file="previewSvcDocument.jsp"%>
    </c:if><%--STEP_DOCUMENTS--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST006'}">
        <%@include file="previewSvcPerson.jsp"%>
    </c:if><%--STEP_SERVICE_PERSONNEL--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST007'}">
        <%@include file="previewMedAlert.jsp"%>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST008'}">
        <%@include file="previewSvcVehicle.jsp"%>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST009'}">
        <%@include file="previewSvcClinicalDirector.jsp"%>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST010'}">
        <%@include file="previewSvcCharges.jsp"%>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST012'}">
        <%@include file="previewSvcBusiness.jsp"%>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST013'}">
        <%@include file="previewSectionLeader.jsp"%>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST014'}">
        <%@include file="previewKeyAppointmentHolder.jsp"%>
    </c:if>
</c:forEach>
