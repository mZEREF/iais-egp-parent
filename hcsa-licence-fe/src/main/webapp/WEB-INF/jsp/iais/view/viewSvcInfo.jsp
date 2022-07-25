<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<style type="text/css">
    table {
        table-layout: fixed;
        word-break: break-all;
    }
</style>
<c:forEach items="${currentviewSvcInfo.hcsaServiceStepSchemeDtos}" var="hcsaServiceStepSchemeDto">
    <c:set var="currStepName" value="${hcsaServiceStepSchemeDto.stepName}" scope="request"/>
    <%--<c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST001'}">
        <jsp:include page="/WEB-INF/jsp/iais/common/viewSvcDisciplines.jsp"/>
    </c:if>--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST002'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewSvcGovernanceOfficer.jsp"/>
    </c:if><%--STEP_CLINICAL_GOVERNANCE_OFFICERS--%>
    <%--<c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST003'}">
        <jsp:include page="/WEB-INF/jsp/iais/common/viewSvcAllocation.jsp"/>
    </c:if>--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST004'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewSvcPrincipalOfficers.jsp"/>
    </c:if><%--STEP_PRINCIPAL_OFFICERS--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST005'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/document/viewSvcDocument.jsp"/>
    </c:if><%--STEP_DOCUMENTS--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST006'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/svcPersonnel/viewSvcPerson.jsp"/>
    </c:if><%--STEP_SERVICE_PERSONNEL--%>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST007'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewMedAlert.jsp"/>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST008'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/others/viewSvcVehicle.jsp"/>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST009'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewSvcClinicalDirector.jsp"/>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST010'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/others/viewSvcCharges.jsp"/>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST012'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/businessInfo/viewSvcBusiness.jsp"/>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST013'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/svcPersonnel/viewSectionLeader.jsp"/>
    </c:if>
    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST014'}">
        <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewKeyAppointmentHolder.jsp"/>
    </c:if>
</c:forEach>
