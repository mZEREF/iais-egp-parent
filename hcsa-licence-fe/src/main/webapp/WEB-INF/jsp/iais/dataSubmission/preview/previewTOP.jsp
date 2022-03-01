<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${submissionType == 'TOP_TP002'}">
        <%@include file="../terminationOfPregnancy/section/patientDetails.jsp" %>
    </c:when>
    <c:when test="${submissionType == 'TOP_TP001'}">
        <%@include file="../terminationOfPregnancy/section/topPatientSelectionSection.jsp" %>
    </c:when>
</c:choose>
