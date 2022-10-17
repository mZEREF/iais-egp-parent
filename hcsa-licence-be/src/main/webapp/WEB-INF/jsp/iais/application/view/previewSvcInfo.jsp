<style type="text/css">
    table {
        table-layout: fixed;
        word-break: break-all;
    }
</style>
<c:forEach items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}" var="hcsaServiceStepSchemeDto">
    <c:set var="currStepName" value="${hcsaServiceStepSchemeDto.stepName}" scope="request"/>
    <c:choose>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST002'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/keyPersonnel/viewSvcGovernanceOfficer.jsp"/>
        </c:when><%--STEP_CLINICAL_GOVERNANCE_OFFICERS--%>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST004'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/keyPersonnel/viewSvcPrincipalOfficers.jsp"/>
        </c:when><%--STEP_PRINCIPAL_OFFICERS--%>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST006'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/svcPersonnel/viewSvcPerson.jsp"/>
        </c:when><%--STEP_SERVICE_PERSONNEL--%>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST007'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/keyPersonnel/viewMedAlert.jsp"/>
        </c:when>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST008'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/previewSvcVehicle.jsp"/>
        </c:when>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST009'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/keyPersonnel/viewSvcClinicalDirector.jsp"/>
        </c:when>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST010'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/previewSvcCharges.jsp"/>
        </c:when>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST012'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/previewSvcBusiness.jsp"/>
        </c:when>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST013'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/svcPersonnel/viewSectionLeader.jsp"/>
        </c:when>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST014'}">
            <jsp:include page="/WEB-INF/jsp/iais/application/view/keyPersonnel/viewKeyAppointmentHolder.jsp"/>
        </c:when>
        <c:when test="${hcsaServiceStepSchemeDto.stepCode=='SVST005'}">
            <%--STEP_DOCUMENTS--%>
            <jsp:include page="/WEB-INF/jsp/iais/application/view/document/viewSvcDocument.jsp"/>
        </c:when>
    </c:choose>
</c:forEach>
