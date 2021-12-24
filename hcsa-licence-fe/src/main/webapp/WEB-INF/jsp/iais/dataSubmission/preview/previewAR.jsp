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
        <%@include file="../assistedReproduction/section/previewPatientDetail.jsp" %>
        <%@include file="../assistedReproduction/section/previewHusbandDetail.jsp" %>
        <c:if test="${arSuperDataSubmissionDto.appType eq 'DSTY_005'}">
            <%@include file="../assistedReproduction/common/previewDsAmendment.jsp" %>
        </c:if>
    </c:when>
    <c:when test="${cycleType == 'DSCL_003'}">
        <%@include file="../assistedReproduction/section/previewDonorSection.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_CL001'}">
        <%-- AR_CYCLE_AR--%>
        <%@include file="../assistedReproduction/section/previewArCycleStageSection.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_CL002'}">
        <%-- AR_CYCLE_IUI--%>
        <%@include file="../assistedReproduction/section/previewIuiCycleSection.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_CL003'}">
        <%-- AR_CYCLE_EFO--%>
        <%@include file="../assistedReproduction/section/previewEfoDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG001'}">
        <%-- AR_STAGE_OOCYTE_RETRIEVAL--%>
        <%@include file="../assistedReproduction/section/previewOocyteRetrieval.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG002'}">
        <%-- AR_STAGE_FERTILISATION--%>
        <%@include file="../assistedReproduction/section/previewFertilisationDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG003'}">
        <%-- AR_STAGE_EMBRYO_CREATED --%>
        <%@include file="../assistedReproduction/section/previewEmbryoCreatedDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG004'}">
        <%-- AR_STAGE_THAWING--%>
        <%@include file="../assistedReproduction/section/previewThawingDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG005'}">
        <%-- AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING--%>
        <%@include file="../assistedReproduction/section/previewPgtDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG006'}">
        <%-- AR_STAGE_EMBRYO_TRANSFER--%>
        <%@include file="../assistedReproduction/section/previewEmbryoTranseferDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG007'}">
        <%-- AR_STAGE_AR_TREATMENT_SUBSIDIES --%>
        <%@include file="../assistedReproduction/section/previewArTreatmentSubsidiesStageDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG008'}">
        <%-- AR_STAGE_IUI_TREATMENT_SUBSIDIES --%>
        <%@include file="../assistedReproduction/section/previewIuiTreatmentSubsidiesDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG009'}">
        <%-- AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED--%>
        <%@include file="../assistedReproduction/section/previewEmbryoTransferredOutcomeStageDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG010'}">
        <%-- AR_STAGE_OUTCOME--%>
        <%@include file="../assistedReproduction/section/previewOutcomeStageDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG011'}">
        <%-- AR_STAGE_OUTCOME_OF_PREGNANCY--%>
        <%@include file="../assistedReproduction/section/previewPregnancyOutcomeStageDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG012'}">
        <%-- AR_STAGE_FREEZING--%>
        <%@include file="../assistedReproduction/section/previewFreezingSection.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG013'}">
        <%-- AR_STAGE_DONATION--%>
        <%@include file="../assistedReproduction/section/previewDonationDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG014'}">
        <%-- AR_STAGE_DISPOSAL--%>
        <%@include file="../assistedReproduction/section/previewDisposalDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG015'}">
        <%-- AR_STAGE_END_CYCLE--%>
        <%@include file="../assistedReproduction/section/previewEndCycleDetail.jsp" %>
    </c:when>
    <c:when test="${cycleStage == 'AR_STG016'}">
        <%-- AR_STAGE_TRANSFER_IN_AND_OUT--%>
        <%@include file="../assistedReproduction/section/previewTransferInOutStageDetail.jsp" %>
    </c:when>
</c:choose>
<c:if test="${arSuperDataSubmissionDto.appType ne 'DSTY_005'}">
<%@include file="../assistedReproduction/common/arDeclaration.jsp" %>
</c:if>