<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<%
    String webrootCom=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<c:set var="cycleStage" value="${arSuperDataSubmissionDto.dataSubmissionDto.cycleStage}"/>
<c:set var="submissionNo" value="${arSuperDataSubmissionDto.dataSubmissionDto.submissionNo}" />
<webui:setLayout name="iais-internet"/>
<%@include file="../../common/dashboard.jsp"%>
<style>
    .progress-tracker li:before {
        position: relative;
        width: 28px;
        height: 28px;
        border: 1px solid #BFBFBF;
        border-radius: 50%;
        background-color: white;
        display: block;
        left: 50%;
        margin-left: -14px;
        top: 0;
        margin-bottom: -26px;
        z-index: -1;
    }
    .progress-tracker li:not(:first-child):after {
        position: absolute;
        height: 1px;
        width: 100%;
        right: 50%;
        background-color: #BFBFBF;
        top: 12px;
        z-index: -2;
    }
    .progress-tracker li.active:after {
        height: 2px;
        top: 12px;
        background-color: #1989BF;
    }
    .nav > li > a {
        position: relative;
        display: block;
        padding: 10px 15px;
        margin-top: 20px;
    }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="arSuperVisSubmissionNo" id="arSuperVisSubmissionNo" value="${arSuperDataSubmissionDto.dataSubmissionDto.submissionNo}"/>

    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <iais:body >
                    <div class="col-xs-12">
                        <div class="tab-gp dashboard-tab">
                            <div class="components">
                                <div class="table-responsive">
                                    <ul class="progress-tracker nav ">
                                        <c:forEach items="${cycleStageList}" var="steplist" varStatus="status">
                                            <c:choose>
                                                <c:when test ="${steplist.submissionNo <= submissionNo}">
                                                    <li onclick="nextTab('${steplist.submissionNo}')" class="tracker-item active" style="color: white;" data-service-step="${steplist.cycleStage}">
                                                            ${status.index+1}<a href="#tab${steplist.cycleStage}" style="color: #000;" aria-controls="tab${steplist.cycleStage}" role="tab" data-toggle="tab"><iais:code code="${steplist.cycleStage}"/></a>
                                                    </li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li onclick="nextTab('${steplist.submissionNo}')" class="tracker-item " data-service-step="${steplist.cycleStage}">
                                                            ${status.index+1}<a href="#tab${steplist.cycleStage}" style="color: #000;" aria-controls="tab${steplist.cycleStage}" role="tab" data-toggle="tab"><iais:code code="${steplist.cycleStage}"/></a>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>

                                        </c:forEach>
                                    </ul>
                                    <div class="tab-nav-mobile visible-xs visible-sm">
                                        <div class="swiper-wrapper" role="tablist">
                                            <div class="swiper-slide"><a href="#tab${cycleStage}" aria-controls="tab${cycleStage}" role="tab" data-toggle="tab"><iais:code code="${cycleStage}"/></a></div>

                                        </div>
                                        <div class="swiper-button-prev"></div>
                                        <div class="swiper-button-next"></div>
                                    </div>
                                </div>
                            </div>


                            <div class="tab-content row">
                                <c:if test="${empty arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}">
                                    <div class="tab-pane active col-lg-12 col-xs-12 panel-group"  role="tabpanel">
                                        <c:if test="${cycleStage == 'AR_CL001'}">
                                            <%-- AR_CYCLE_AR--%>
                                            <%@include file="cycleStage/previewArCycleStageSection.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_CL002'}">
                                            <%-- AR_CYCLE_IUI--%>
                                            <%@include file="cycleStage/previewIuiCycleSection.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_CL003'}">
                                            <%-- AR_CYCLE_EFO--%>
                                            <%@include file="cycleStage/previewEfoDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG001'}">
                                            <%-- AR_STAGE_OOCYTE_RETRIEVAL--%>
                                            <%@include file="cycleStage/previewOocyteRetrieval.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG002'}">
                                            <%-- AR_STAGE_FERTILISATION--%>
                                            <%@include file="cycleStage/previewFertilisationDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG003'}">
                                            <%-- AR_STAGE_EMBRYO_CREATED --%>
                                            <%@include file="cycleStage/previewEmbryoCreatedDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG004'}">
                                            <%-- AR_STAGE_THAWING--%>
                                            <%@include file="cycleStage/previewThawingDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG005'}">
                                            <%-- AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING--%>
                                            <%@include file="cycleStage/previewPgtDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG006'}">
                                            <%-- AR_STAGE_EMBRYO_TRANSFER--%>
                                            <%@include file="cycleStage/previewEmbryoTranseferDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG007'}">
                                            <%-- AR_STAGE_AR_TREATMENT_SUBSIDIES --%>
                                            <%@include file="cycleStage/previewArTreatmentSubsidiesStageDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG008'}">
                                            <%-- AR_STAGE_IUI_TREATMENT_SUBSIDIES --%>
                                            <%@include file="cycleStage/previewIuiTreatmentSubsidiesDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG009'}">
                                            <%-- AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED--%>
                                            <%@include file="cycleStage/previewEmbryoTransferredOutcomeStageDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG010'}">
                                            <%-- AR_STAGE_OUTCOME--%>
                                            <%@include file="cycleStage/previewOutcomeStageDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG011'}">
                                            <%-- AR_STAGE_OUTCOME_OF_PREGNANCY--%>
                                            <%@include file="cycleStage/previewPregnancyOutcomeStageDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG012'}">
                                            <%-- AR_STAGE_FREEZING--%>
                                            <%@include file="cycleStage/previewFreezingSection.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG013'}">
                                            <%-- AR_STAGE_DONATION--%>
                                            <%@include file="cycleStage/previewDonationDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG014'}">
                                            <%-- AR_STAGE_DISPOSAL--%>
                                            <%@include file="cycleStage/previewDisposalDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG015'}">
                                            <%-- AR_STAGE_END_CYCLE--%>
                                            <%@include file="cycleStage/previewEndCycleDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG016'}">
                                            <%-- AR_STAGE_TRANSFER_IN_AND_OUT--%>
                                            <%@include file="cycleStage/previewTransferInOutStageDetail.jsp" %>
                                        </c:if>
                                    </div>
                                </c:if>
                                <c:if test="${not empty arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}">
                                    <div class="tab-pane active col-lg-12 col-xs-12 panel-group"  role="tabpanel">
                                        <c:if test="${cycleStage == 'AR_CL001'}">
                                            <%-- AR_CYCLE_AR--%>
                                            <%@include file="cycleStageAment/previewArCycleStageSection.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_CL002'}">
                                            <%-- AR_CYCLE_IUI--%>
                                            <%@include file="cycleStageAment/previewIuiCycleSection.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_CL003'}">
                                            <%-- AR_CYCLE_EFO--%>
                                            <%@include file="cycleStageAment/previewEfoDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG001'}">
                                            <%-- AR_STAGE_OOCYTE_RETRIEVAL--%>
                                            <%@include file="cycleStageAment/previewOocyteRetrieval.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG002'}">
                                            <%-- AR_STAGE_FERTILISATION--%>
                                            <%@include file="cycleStageAment/previewFertilisationDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG003'}">
                                            <%-- AR_STAGE_EMBRYO_CREATED --%>
                                            <%@include file="cycleStageAment/previewEmbryoCreatedDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG004'}">
                                            <%-- AR_STAGE_THAWING--%>
                                            <%@include file="cycleStageAment/previewThawingDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG005'}">
                                            <%-- AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING--%>
                                            <%@include file="cycleStageAment/previewPgtDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG006'}">
                                            <%-- AR_STAGE_EMBRYO_TRANSFER--%>
                                            <%@include file="cycleStageAment/previewEmbryoTranseferDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG007'}">
                                            <%-- AR_STAGE_AR_TREATMENT_SUBSIDIES --%>
                                            <%@include file="cycleStageAment/previewArTreatmentSubsidiesStageDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG008'}">
                                            <%-- AR_STAGE_IUI_TREATMENT_SUBSIDIES --%>
                                            <%@include file="cycleStageAment/previewIuiTreatmentSubsidiesDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG009'}">
                                            <%-- AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED--%>
                                            <%@include file="cycleStageAment/previewEmbryoTransferredOutcomeStageDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG010'}">
                                            <%-- AR_STAGE_OUTCOME--%>
                                            <%@include file="cycleStageAment/previewOutcomeStageDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG011'}">
                                            <%-- AR_STAGE_OUTCOME_OF_PREGNANCY--%>
                                            <%@include file="cycleStageAment/previewPregnancyOutcomeStageDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG012'}">
                                            <%-- AR_STAGE_FREEZING--%>
                                            <%@include file="cycleStageAment/previewFreezingSection.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG013'}">
                                            <%-- AR_STAGE_DONATION--%>
                                            <%@include file="cycleStageAment/previewDonationDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG014'}">
                                            <%-- AR_STAGE_DISPOSAL--%>
                                            <%@include file="cycleStageAment/previewDisposalDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG015'}">
                                            <%-- AR_STAGE_END_CYCLE--%>
                                            <%@include file="cycleStageAment/previewEndCycleDetail.jsp" %>
                                        </c:if>
                                        <c:if test="${cycleStage == 'AR_STG016'}">
                                            <%-- AR_STAGE_TRANSFER_IN_AND_OUT--%>
                                            <%@include file="cycleStageAment/previewTransferInOutStageDetail.jsp" %>
                                        </c:if>
                                    </div>
                                </c:if>
                            </div>
                            <div class="tab-content row">
                                <a href="#" onclick="javascript:doBack('${arViewFull}','${arAdv}','${arBase}');" ><em class="fa fa-angle-left"> </em> Back</a>
                            </div>
                        </div>
                    </div>
                </iais:body>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript" src="<%=webrootCom%>js/onlineEnquiries/arStageInfo.js"></script>
