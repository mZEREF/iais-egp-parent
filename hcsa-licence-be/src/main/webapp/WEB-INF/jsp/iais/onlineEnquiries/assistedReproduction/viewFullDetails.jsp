<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
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
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="<c:if test="${empty preInspReport }">active</c:if><c:if test="${not empty preInspReport }">complete</c:if>" role="presentation">
                                                <a href="#tabPatientInfo" aria-controls="tabPatientInfo" role="tab" data-toggle="tab">Patient Information</a></li>
                                            <li class="complete" role="presentation">
                                                <a href="#tabHusbandInfo" aria-controls="tabHusbandInfo" role="tab" data-toggle="tab">Husband Information</a></li>
                                            <li class="<c:if test="${preInspReport == '1'}">active</c:if><c:if test="${ preInspReport != '1' }">complete</c:if>" role="presentation">
                                                <a href="#tabInventory" aria-controls="tabInventory" role="tab" data-toggle="tab">Inventory</a></li>
                                            <li class="complete" role="presentation">
                                                <a href="#tabCoFundingHistory" aria-controls="tabCoFundingHistory" role="tab" data-toggle="tab">Co-funding History</a></li>
                                            <li class="<c:if test="${preInspReport == '2'}">active</c:if><c:if test="${ preInspReport != '2' }">complete</c:if>" role="presentation">
                                                <a href="#tabCycleStage" aria-controls="tabCycleStage" role="tab" data-toggle="tab">Cycle Stages</a></li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabPatientInfo" aria-controls="tabPatientInfo" role="tab" data-toggle="tab">Licensee Details</a></div>
                                                <div class="swiper-slide"><a href="#tabHusbandInfo" aria-controls="tabHusbandInfo" role="tab" data-toggle="tab">Personnel Details</a></div>
                                                <div class="swiper-slide"><a href="#tabInventory" aria-controls="tabInventory" role="tab" data-toggle="tab">Personnel Details</a></div>
                                                <div class="swiper-slide"><a href="#tabCoFundingHistory" aria-controls="tabCoFundingHistory" role="tab" data-toggle="tab">Personnel Details</a></div>
                                                <div class="swiper-slide"><a href="#tabCycleStage" aria-controls="tabComplianceHistory" role="tab" data-toggle="tab">Compliance History</a></div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>

                                        <div class="tab-content ">
                                            <div class="tab-pane <c:if test="${empty preInspReport }">active</c:if> " id="tabPatientInfo" role="tabpanel">
                                                <%@include file="patientInfo.jsp" %>
                                            </div>

                                            <div class="tab-pane" id="tabHusbandInfo" role="tabpanel">
                                                <%@include file="husbandInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${preInspReport == '1'}">active</c:if>"  id="tabInventory" role="tabpanel">
                                                <%@include file="inventory.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabCoFundingHistory" role="tabpanel">
                                                <%@include file="coFundingHistory.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${preInspReport == '2'}">active</c:if>" id="tabCycleStage" role="tabpanel">
                                                <%@include file="cycleStage.jsp" %>
                                            </div>

                                        </div>
                                    </div>
                                    <a href="#" onclick="javascript:doBack();" ><em class="fa fa-angle-left"> </em> Back</a>
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
    function doReportSearch(appPremCorrId){
        showWaiting();
        $("[name='crud_action_value']").val(appPremCorrId);
        $("[name='crud_action_type']").val('preStage');
        $('#mainForm').submit();
    }
    function doBack(){
        showWaiting();
        $("[name='crud_action_type']").val('back');
        $('#mainForm').submit();
    }

</script>