﻿<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<c:set var="cycleStage" value="${arSuperDataSubmissionDto.dataSubmissionDto.cycleStage}"/>
<c:set var="submitDt" value="${arSuperDataSubmissionDto.dataSubmissionDto.submitDt}" />
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" >
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body >
                            <div class="col-xs-12">
                                <ul class="progress-tracker nav nav-tabs">
                                    <c:forEach items="${cycleStageList}" var="steplist" varStatus="status">
                                        <c:choose>
                                            <c:when test ="${steplist.submitDt <= submitDt}">
                                                <li id="${steplist.cycleStage}Tab" class="tracker-item active" data-service-step="${steplist.cycleStage}">
                                                    <a href="#tab${steplist.cycleStage}" aria-controls="tab${steplist.cycleStage}" role="tab" data-toggle="tab"><iais:code code="${steplist.cycleStage}"/></a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li id="${steplist.cycleStage}Tab" class="tracker-item disabled" data-service-step="${steplist.cycleStage}">
                                                    <a href="#tab${steplist.cycleStage}" aria-controls="tab${steplist.cycleStage}" role="tab" data-toggle="tab"><iais:code code="${steplist.cycleStage}"/></a>
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

                                <div class="tab-content ">
                                    <div class="tab-pane active " id="tab${cycleStage}" role="tabpanel">


                                        <c:choose>

                                            <c:when test="${cycleStage == 'AR_CL001'}">
                                                <%-- AR_CYCLE_AR--%>
                                                <%@include file="cycleStage/previewArCycleStageSection.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_CL002'}">
                                                <%-- AR_CYCLE_IUI--%>
                                                <%@include file="cycleStage/previewIuiCycleSection.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_CL003'}">
                                                <%-- AR_CYCLE_EFO--%>
                                                <%@include file="cycleStage/previewEfoDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG001'}">
                                                <%-- AR_STAGE_OOCYTE_RETRIEVAL--%>
                                                <%@include file="cycleStage/previewOocyteRetrieval.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG002'}">
                                                <%-- AR_STAGE_FERTILISATION--%>
                                                <%@include file="cycleStage/previewFertilisationDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG003'}">
                                                <%-- AR_STAGE_EMBRYO_CREATED --%>
                                                <%@include file="cycleStage/previewEmbryoCreatedDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG004'}">
                                                <%-- AR_STAGE_THAWING--%>
                                                <%@include file="cycleStage/previewThawingDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG005'}">
                                                <%-- AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING--%>
                                                <%@include file="cycleStage/previewPgtDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG006'}">
                                                <%-- AR_STAGE_EMBRYO_TRANSFER--%>
                                                <%@include file="cycleStage/previewEmbryoTranseferDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG007'}">
                                                <%-- AR_STAGE_AR_TREATMENT_SUBSIDIES --%>
                                                <%@include file="cycleStage/previewArTreatmentSubsidiesStageDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG008'}">
                                                <%-- AR_STAGE_IUI_TREATMENT_SUBSIDIES --%>
                                                <%@include file="cycleStage/previewIuiTreatmentSubsidiesDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG009'}">
                                                <%-- AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED--%>
                                                <%@include file="cycleStage/previewEmbryoTransferredOutcomeStageDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG010'}">
                                                <%-- AR_STAGE_OUTCOME--%>
                                                <%@include file="cycleStage/previewOutcomeStageDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG011'}">
                                                <%-- AR_STAGE_OUTCOME_OF_PREGNANCY--%>
                                                <%@include file="cycleStage/previewPregnancyOutcomeStageDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG012'}">
                                                <%-- AR_STAGE_FREEZING--%>
                                                <%@include file="cycleStage/previewFreezingSection.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG013'}">
                                                <%-- AR_STAGE_DONATION--%>
                                                <%@include file="cycleStage/previewDonationDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG014'}">
                                                <%-- AR_STAGE_DISPOSAL--%>
                                                <%@include file="cycleStage/previewDisposalDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG015'}">
                                                <%-- AR_STAGE_END_CYCLE--%>
                                                <%@include file="cycleStage/previewEndCycleDetail.jsp" %>
                                            </c:when>
                                            <c:when test="${cycleStage == 'AR_STG016'}">
                                                <%-- AR_STAGE_TRANSFER_IN_AND_OUT--%>
                                                <%@include file="cycleStage/previewTransferInOutStageDetail.jsp" %>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </div>

                            </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $('#AR_CL001Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_CL001');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_CL002Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_CL002');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_CL003Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_CL003');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG001Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG001');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG002Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG002');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG003Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG003');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG004Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG004');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG005Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG005');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG006Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG006');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG007Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG007');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG008Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG008');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG009Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG009');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG010Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG010');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG011Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG011');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG012Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG012');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG013Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG013');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG014Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG014');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG015Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG015');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });
        $('#AR_STG016Tab').click(function(){
            showWaiting();
            $("[name='crud_action_additional']").val('AR_STG016');
            $("[name='crud_action_type']").val('step');
            $('#mainForm').submit();
        });

    });
</script>
