<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%--<style type="text/css">
    table {
        table-layout: fixed;
        word-break: break-all;
    }
</style>--%>
<c:forEach items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}" var="hcsaServiceStepSchemeDto">
    <c:set var="currStepName" value="${hcsaServiceStepSchemeDto.stepName}" scope="request"/>
    <c:set var="currentStep" value="${hcsaServiceStepSchemeDto.stepCode}"/>
    <%--STEP_CLINICAL_GOVERNANCE_OFFICERS--%>
    <c:choose>
        <c:when test="${currentStep == 'SVST002'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewSvcGovernanceOfficer.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST004'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewSvcPrincipalOfficers.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST006'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/svcPersonnel/viewSvcPerson.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST007'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewMedAlert.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST008'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/others/viewSvcVehicle.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST009'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewSvcClinicalDirector.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST010'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/others/viewSvcCharges.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST012'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/businessInfo/viewSvcBusiness.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST013'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/svcPersonnel/viewSectionLeader.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST014'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewKeyAppointmentHolder.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST015'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/otherInfo/viewOtherInformation.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST016'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/supplementaryForm/viewSupplementaryForm.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST017'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/specialServicesForm/viewSpecialServicesForm.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST005'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/document/viewSvcDocument.jsp"/>
        </c:when>
        <c:when test="${currentStep == 'SVST018'}">
            <jsp:include page="/WEB-INF/jsp/iais/view/outsourced/viewOutsourcedProviders.jsp"/>
        </c:when>
    </c:choose>
</c:forEach>
