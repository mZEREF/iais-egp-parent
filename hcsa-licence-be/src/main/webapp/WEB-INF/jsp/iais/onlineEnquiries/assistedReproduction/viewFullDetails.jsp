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
        <input type="hidden" name="preActiveHidden" id="preActiveHidden" value="${preActive}"/>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body >
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="<c:if test="${empty preActive }">active</c:if><c:if test="${not empty preActive }">complete</c:if>" role="presentation">
                                                <a href="#tabPatientInfo" aria-controls="tabPatientInfo" role="tab" data-toggle="tab">Patient Information</a></li>
                                            <li class="complete" role="presentation">
                                                <a href="#tabHusbandInfo" aria-controls="tabHusbandInfo" role="tab" data-toggle="tab">Husband Information</a></li>
                                            <li id="inventoryTab" class="<c:if test="${preActive == '1'}">active</c:if><c:if test="${ preActive != '1' }">complete</c:if>" role="presentation">
                                                <a href="#tabInventory" aria-controls="tabInventory" role="tab" data-toggle="tab">Inventory</a></li>
                                            <li class="complete" role="presentation">
                                                <a href="#tabCoFundingHistory" aria-controls="tabCoFundingHistory" role="tab" data-toggle="tab">Co-funding History</a></li>
                                            <li id="cycleStageTab" class="<c:if test="${preActive == '2'}">active</c:if><c:if test="${ preActive != '2' }">complete</c:if>" role="presentation">
                                                <a href="#tabCycleStage" aria-controls="tabCycleStage" role="tab" data-toggle="tab">Cycle Stages</a></li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabPatientInfo" aria-controls="tabPatientInfo" role="tab" data-toggle="tab">Patient Information</a></div>
                                                <div class="swiper-slide"><a href="#tabHusbandInfo" aria-controls="tabHusbandInfo" role="tab" data-toggle="tab">Husband Information</a></div>
                                                <div class="swiper-slide"><a href="#tabInventory" aria-controls="tabInventory" role="tab" data-toggle="tab">Inventory</a></div>
                                                <div class="swiper-slide"><a href="#tabCoFundingHistory" aria-controls="tabCoFundingHistory" role="tab" data-toggle="tab">Co-funding History</a></div>
                                                <div class="swiper-slide"><a href="#tabCycleStage" aria-controls="tabComplianceHistory" role="tab" data-toggle="tab">Cycle Stages</a></div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>

                                        <div class="tab-content ">
                                            <div class="tab-pane <c:if test="${empty preActive }">active</c:if> " id="tabPatientInfo" role="tabpanel">
                                                <%@include file="patientInfo.jsp" %>
                                            </div>

                                            <div class="tab-pane" id="tabHusbandInfo" role="tabpanel">
                                                <%@include file="husbandInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${preActive == '1'}">active</c:if>"  id="tabInventory" role="tabpanel">
                                                <%@include file="inventory.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabCoFundingHistory" role="tabpanel">
                                                <%@include file="coFundingHistory.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${preActive == '2'}">active</c:if>" id="tabCycleStage" role="tabpanel">
                                                <%@include file="cycleStage.jsp" %>
                                            </div>

                                        </div>
                                    </div>
                                    <div class="row">
                                        <a href="#" onclick="javascript:doBack('${arAdv}','${arBase}');" ><em class="fa fa-angle-left"> </em> Back</a>
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
    var preActiveHidden=$("[name='preActiveHidden']").val();

    var dividajaxlist = [];

    function doReportSearch(appPremCorrId){
        showWaiting();
        $("[name='crud_action_value']").val(appPremCorrId);
        $("[name='crud_action_type']").val('preStage');
        $('#mainForm').submit();
    }
    function doBack(arAdv,arBase){
        showWaiting();
        if(arAdv==1){
            $("[name='crud_action_type']").val('backAdv');
        }
        if(arBase==1){
            $("[name='crud_action_type']").val('backBase');
        }
        $('#mainForm').submit();
    }
    $(document).ready(function() {
        $('#inventoryTab').click(function(){
            showWaiting();
            $("[name='crud_action_type']").val('searchInv');
            $('#mainForm').submit();
        });
        $('#cycleStageTab').click(function(){
            showWaiting();
            $("[name='crud_action_type']").val('searchCyc');
            $('#mainForm').submit();
        });

    });

    function searchInventory() {
        showWaiting();
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        $("[name='crud_action_type']").val('searchInv');
        $('#mainForm').submit();
    }

    function jumpToPagechangePage() {
        if(preActiveHidden==='1'){
            showWaiting();
            $("[name='crud_action_type']").val('searchInv');
            $('#mainForm').submit();
        }
        if(preActiveHidden==='2'){
            showWaiting();
            $("[name='crud_action_type']").val('searchCyc');
            $('#mainForm').submit();
        }

    }


    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        if(preActiveHidden==='1'){
            $("[name='crud_action_type']").val('searchInv');
        }
        if(preActiveHidden==='2'){
            $("[name='crud_action_type']").val('searchCyc');
        }
        $('#mainForm').submit();
    }

    var getStageByCycleId = function (cycleId, divid) {
        if (!isInArray(dividajaxlist,divid)) {
            stageAjax(cycleId, divid);
        } else {
            var display = $('#advfilterson' + divid).css('display');
            if (display == 'none') {
                $('#advfilterson' + divid).show();
            } else {
                $('#advfilterson' + divid).hide();
            }
        }
    };
    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
    };
    var stageAjax = function (cycleIder, divid) {
        dividajaxlist.push(divid);
        $.post(
            '/hcsa-licence-web/hcsa/intranet/ar/cycleStageDetail.do',
            {cycleIder: cycleIder},
            function (data) {
                let result = data.result;
                if('Success' == result) {
                    let res = data.ajaxResult;
                    let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                        '<td colspan="7" class="hiddenRow">' +
                        '<div class="accordian-body p-3 collapse in" id="dropdown' + divid + '" >' +
                        '<table class="table application-item" style="background-color: #F3F3F3;" >' +
                        '<thead>' +
                        '<tr>';


                    html += '<th width="25%">Submission ID</th>' +
                        '<th width="25%">Date</th>' +
                        '<th width="25%">Stage</th>' +
                        '<th width="25%">View Full Details</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody>';
                    for (let i = 0; i < res.length; i++) {
                        var color = "black";

                        html += '<tr style = "color : ' + color + ';">';

                        html += '<td><p class="visible-xs visible-sm table-row-title">Submission ID</p><p>' + res[i].submissionNo + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Date</p><p>' + res[i].submitDt + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Stage</p><p>' + res[i].cycleStage + '<p></td>';

                        html += '<td><p class="visible-xs visible-sm table-row-title">View Full Details</p><p>' +
                            '<button type="button" onclick="fullDetailsView(' + "'" + res[i].cycleId + "'" + ')" class="btn btn-default btn-sm">'+
                        'View Full Details</button></p></td>'+
                            '</tr>';
                    }
                    html += '</tbody></table></div></td></tr>';
                    $('#advfilter' + divid).after(html);
                }
            }
        )
    };
</script>
