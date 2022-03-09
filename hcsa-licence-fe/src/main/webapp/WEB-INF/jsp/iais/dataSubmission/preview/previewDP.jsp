<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="submissionType" value="${dpSuperDataSubmissionDto.submissionType}"/>

<c:choose>
    <c:when test="${submissionType == 'DP_TP001'}">
        <%-- Patient Information --%>
        <%@include file="../drugPractices/section/previewPatientInformationDetail.jsp" %>
    </c:when>
    <c:when test="${submissionType == 'DP_TP002'}">
        <%-- Drug Prescribed --%>
        <%@include file="../drugPractices/section/previewDrugSubmissionSection.jsp" %>
        <%@include file="../drugPractices/section/previewDrugMedicationSection.jsp" %>
    </c:when>
    <c:when test="${submissionType == 'DP_TP003'}">
        <%-- Sovenor Inventory --%>
    </c:when>
</c:choose>
<%@include file="../drugPractices/common/dpDeclaration.jsp" %>