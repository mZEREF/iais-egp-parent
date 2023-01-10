<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--<c:set var="headingSign" value="completed"/>--%>
<c:set var="cycleType" value="${arSuperDataSubmissionDto.cycleDto.cycleType}"/>
<c:set var="cycleStage" value="${arSuperDataSubmissionDto.dataSubmissionDto.cycleStage}"/>
<c:set var="declaration" value="${arSuperDataSubmissionDto.dataSubmissionDto.declaration}" />

<c:choose>
    <c:when test="${cycleType == 'DSCL_001'}">
        <%-- AR Patient--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewPatientDetail.jsp"/>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewHusbandDetail.jsp"/>
    </c:when>
    <c:when test="${cycleType == 'DSCL_003'}">
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/new/section/previewSampleSection.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_CL001'}">
        <%-- AR_CYCLE_AR--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewArCycleStageSection.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_CL002'}">
        <%-- AR_CYCLE_IUI--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewIuiCycleSection.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_CL003'}">
        <%-- AR_CYCLE_EFO--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewEfoDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_CL005'}">
        <%-- AR_CYCLE_SFO--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewSfoDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG001'}">
        <%-- AR_STAGE_OOCYTE_RETRIEVAL--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewOocyteRetrieval.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG002'}">
        <%-- AR_STAGE_FERTILISATION--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewFertilisationDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG003'}">
        <%-- AR_STAGE_EMBRYO_CREATED --%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewEmbryoCreatedDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG004'}">
        <%-- AR_STAGE_THAWING--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewThawingDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG005'}">
        <%-- AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewPgtDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG006'}">
        <%-- AR_STAGE_EMBRYO_TRANSFER--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewEmbryoTranseferDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG007'}">
        <%-- AR_STAGE_AR_TREATMENT_SUBSIDIES --%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewArTreatmentSubsidiesStageDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG008'}">
        <%-- AR_STAGE_IUI_TREATMENT_SUBSIDIES --%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewIuiTreatmentSubsidiesDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG009'}">
        <%-- AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewEmbryoTransferredOutcomeStageDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG010'}">
        <%-- AR_STAGE_OUTCOME--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewOutcomeStageDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG011'}">
        <%-- AR_STAGE_OUTCOME_OF_PREGNANCY--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewPregnancyOutcomeStageDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG012'}">
        <%-- AR_STAGE_FREEZING--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewFreezingSection.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG013'}">
        <%-- AR_STAGE_DONATION--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewDonationDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG014'}">
        <%-- AR_STAGE_DISPOSAL--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewDisposalDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG015'}">
        <%-- AR_STAGE_END_CYCLE--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewEndCycleDetail.jsp"/>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG016'}">
        <%-- AR_STAGE_TRANSFER_IN_AND_OUT--%>
        <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewTransferInOutStageDetail.jsp"/>
    </c:when>
</c:choose>
<c:if test="${cycleStage != 'AR_STG014'}">
    <%@include file="../assistedReproduction/section/previewDisposalDetail.jsp" %>
</c:if>
<%@include file="../assistedReproduction/common/previewDsAmendment.jsp" %>
<%@include file="../assistedReproduction/common/arDeclaration.jsp" %>

