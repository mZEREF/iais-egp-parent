<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<%--<c:set var="submissionType" value="${topSuperDataSubmissionDto.submissionType}"/>
<c:choose>
    <c:when test="${submissionType == 'TOP_TP002'}">

        <%@include file="../terminationOfPregnancy/common/previewTopDsAmendment.jsp" %>
        <%@include file="../terminationOfPregnancy/common/topDeclaration.jsp" %>
    </c:when>
    <c:when test="${submissionType == 'TOP_TP001'}">

    </c:when>
</c:choose>--%>
<%@include file="../terminationOfPregnancy/section/previewPatientDetails.jsp" %>
<%@include file="../terminationOfPregnancy/section/previewFamilyPlanning.jsp" %>
<%@include file="../terminationOfPregnancy/section/previewPreTermination.jsp" %>
<%@include file="../terminationOfPregnancy/section/previewPresentTermination.jsp" %>
<%@include file="../terminationOfPregnancy/section/previewPostTermination.jsp" %>
<%@include file="../terminationOfPregnancy/common/previewTopDsAmendment.jsp" %>
<%@include file="../terminationOfPregnancy/common/topDeclaration.jsp" %>