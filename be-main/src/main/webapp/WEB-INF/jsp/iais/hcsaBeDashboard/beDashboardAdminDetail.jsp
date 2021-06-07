<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/5/24
  Time: 10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>

<style>
    @media only screen and (min-width: 1201px) and (max-width: 1920px) {
        .dashboard-multiple .dashboard-tile-item .dashboard-tile {
            border-radius: 18px;
            height: 265px;
            margin-bottom: 55px;
        }
    }
    .dashboard-chart .dashboard-tile-item .dashboard-tile a.empty-chart {
        cursor: default;
        background-image: url('<%=webroot1%>img/chart-0.png');
        background-repeat: no-repeat;
        background-position: center 25%;
    }
</style>
<div class="main-content" style="min-height: 73vh;">

    <input type="hidden" name="overAllVal" value='${dashAllSvcCircleKpi}'/>
    <input type="hidden" name="BLBVal" value='${dashBLBCircleKpi}'/>
    <input type="hidden" name="CLBVal" value='${dashCLBCircleKpi}'/>
    <input type="hidden" name="EASVal" value='${dashEASCircleKpi}'/>
    <input type="hidden" name="MTSVal" value='${dashMTSCircleKpi}'/>
    <input type="hidden" name="NMAVal" value='${dashNMACircleKpi}'/>
    <input type="hidden" name="NMIVal" value='${dashNMICircleKpi}'/>
    <input type="hidden" name="RDSVal" value='${dashRDSCircleKpi}'/>
    <input type="hidden" name="TSBVal" value='${dashTSBCircleKpi}'/>

    <form method="post" id="beDashboardForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="hcsaBeDashboardSwitchType" value="">
        <%--<input type="hidden" id="dashSysStageVal" name="dashSysStageVal" value=''/>--%>
        <input type="hidden" name="dashSysStageVal" value="${dashSysStageValReq}"/>
        <input type="hidden" id="switchAction" name="switchAction" value="${dashSwitchActionValue}">

        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>
                                All Active
                                <c:choose>
                                    <c:when test="${'ASO' == dashSysStageValReq}">
                                        Admin Screening
                                    </c:when>
                                    <c:when test="${'PSO' == dashSysStageValReq}">
                                        Professional Screening
                                    </c:when>
                                    <c:when test="${'PRE' == dashSysStageValReq}">
                                        Pre-Inspection
                                    </c:when>
                                    <c:when test="${'INS' == dashSysStageValReq}">
                                        Inspection
                                    </c:when>
                                    <c:when test="${'POT' == dashSysStageValReq}">
                                        Post-Inspection
                                    </c:when>
                                    <c:when test="${'AO1' == dashSysStageValReq}">
                                        Approval Officer 1
                                    </c:when>
                                    <c:when test="${'AO2' == dashSysStageValReq}">
                                        Approval Officer 2
                                    </c:when>
                                    <c:when test="${'AO3' == dashSysStageValReq}">
                                        Approval Officer 3
                                    </c:when>
                                </c:choose>
                                 Tasks
                            </h2>
                        </div>
                        <div class="row">
                            <div class="col-md-4 col-xs-12">
                                <a data-tab="#" href="javascript:;" style="cursor: default;">
                                    <div id="canvas-holder">
                                        <canvas id="overAllCanvas"></canvas>
                                    </div>
                                    <p class="dashboard-txt main-chart-text font-color-black"> Overall</p>
                                </a>
                            </div>
                            <div class="col-md-8 col-xs-12">
                                <div class="form-horizontal filter-box">
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">Application type</label>
                                        <div class="col-xs-12 col-sm-6 col-md-5" style="padding-left: unset;padding-top: 1%;">
                                            <iais:select cssClass="" name="appType" firstOption="" needSort="true" options="appTypeOption" multiValues="${dashAppTypeCheckList}" multiSelect="true" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">Service Licence</label>
                                        <div class="col-xs-12 col-sm-6 col-md-5" style="padding-left: unset;padding-top: 1%;">
                                            <iais:select cssClass="" name="svcLic" firstOption="" needSort="true" options="dashServiceOption" multiValues="${dashSvcCheckList}" multiSelect="true" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-12 col-xs-12">
                                <div style="text-align:right;">
                                    <button class="btn btn-secondary" type="button" id="sysClearBtn" name="sysClearBtn">Clear</button>
                                    <button class="btn btn-primary" type="button" id="sysSearchBtn" name="sysSearchBtn">Search</button>
                                </div>
                                <br/>
                            </div>
                            <hr>
                            <div class="col-xs-12">
                                <div class="dashboard-chart dashboard-multiple" style="cursor: default;">
                                    <c:forEach var="svcOp" items="${dashServiceOption}" varStatus="status">
                                        <div class="dashboard-tile-item">
                                            <div class="dashboard-tile">
                                                <a data-tab="#" href="javascript:;" style="cursor: default;">
                                                    <div>
                                                        <canvas id="${svcOp.value}Canvas"></canvas>
                                                    </div>
                                                    <p align="center" class="font-color-black cursor-default">${svcOp.text}</p>
                                                </a>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <iais:pagination param="dashSearchParam" result="dashSearchResult"/>
                                <div class="table-gp">
                                    <table class="table application-group" style="border-collapse:collapse;">
                                        <thead>
                                        <tr>
                                            <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="false" field="GROUP_NO" value="Application No."></iais:sortableHeader>
                                            <iais:sortableHeader needSort="false" field="APP_TYPE" value="Application Type"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="false" field="COU" value="Submission Type"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="false" field="SUBMIT_DT" value="Application Date"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="false" field="" value="Last Modified Date"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="false" field="PMT_STATUS" value="Payment Status"></iais:sortableHeader>
                                        </tr>
                                        </thead>
                                        <c:choose>
                                            <c:when test="${empty dashSearchResult.rows}">
                                                <tr>
                                                    <td colspan="7">
                                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="pool" items="${dashSearchResult.rows}"
                                                           varStatus="status">
                                                    <tr style="display: table-row;" id="advfilter${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}">
                                                        <td><c:out value="${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}"/></td>
                                                        <td>
                                                            <p style="width: 165px;"><c:out value="${pool.groupNo}"/>
                                                                <a class="accordion-toggle  collapsed" style="float: right" data-toggle="collapse" aria-expanded="false" data-target="#advfilter${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}" onclick="getAppByGroupId('${pool.id}','${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}')">
                                                                </a>
                                                            </p>
                                                        </td>
                                                        <td><iais:code code="${pool.appType}"/></td>
                                                        <td><c:out value="${pool.submissionType}"/></td>
                                                        <td><fmt:formatDate value='${pool.submitDt}' pattern='dd/MM/yyyy' /></td>
                                                        <td><fmt:formatDate value='${pool.groupUpDt}' pattern='dd/MM/yyyy' /></td>
                                                        <td><iais:code code="${pool.pmtStatus}"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <a class="back" id="Back" href="javascript:;"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="beDashboardFun.jsp"%>
<script>
    var dividajaxlist = [];
    $(document).ready(function () {
        $('#Back').click(function () {
            showWaiting();
            $('input[name="hcsaBeDashboardSwitchType"]').val('system');
            var mainPoolForm = document.getElementById('beDashboardForm');
            mainPoolForm.submit();
        });

        chartRegister();
        initChart('overAll');
        <c:forEach var="svcOp" items="${dashServiceOption}" varStatus="status">
            initChart('${svcOp.value}', null);
        </c:forEach>

        $('#sysClearBtn').click(function () {
            doClear();
        });

        $('#sysSearchBtn').click(function () {
            showWaiting();
            $("[name='hcsaBeDashboardSwitchType']").val('sysdet');
            var mainPoolForm = document.getElementById('beDashboardForm');
            mainPoolForm.submit();
        });
    });

    var getAppByGroupId = function (applicationGroupId, divid) {
        if (!isInArray(dividajaxlist,divid)) {
            groupAjax(applicationGroupId, divid);
        } else {
            var display = $('#advfilterson' + divid).css('display');
            if (display == 'none') {
                $('#advfilterson' + divid).show();
            } else {
                $('#advfilterson' + divid).hide();
            }
        }
    };

    var groupAjax = function (applicationGroupId, divid) {
        dividajaxlist.push(divid);
        $.post(
            '/main-web/hcsa/intranet/dashboard/dashSysDetail.do',
            {groupId: applicationGroupId},
            function (data) {
                let dashSupportFlag = data.dashSupportFlag;
                let result = data.result;
                if('Success' == result) {
                    let res = data.ajaxResult;
                    let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                        '<td colspan="7" style="padding: 0px 8px !important;">' +
                        '<div class="accordian-body p-3 collapse in" id="row1" aria-expanded="true" style="">' +
                        '<table class="table application-item" style="background-color: #F3F3F3;margin-bottom:0px;" >' +
                        '<thead>' +
                        '<tr>';
                    if ("true" == dashSupportFlag) {
                        html += '<th><input type="checkbox" id="checkbox' + divid + '" onclick="chooseAllcheckBox(' + divid + ')" </th>';
                    }

                    html += '<th width="15%">Application No.</th>' +
                        '<th width="15%">Application Type</th>' +
                        '<th width="15%">Service Licence</th>' +
                        '<th width="15%">Officer Assigned</th>' +
                        '<th width="15%">Days at Current Stage</th>' +
                        '<th width="10%">Target TAT at Current Stage</th>' +
                        '<th width="10%">Total Days Taken for Application</th>' +
                        '<th width="10%">Total TAT for Application</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody>';
                    for (let i = 0; i < res.rowCount; i++) {
                        var color = "black";
                        if ("black" == res.rows[i].kpiColor) {
                            color = "black";
                        } else if ("red" == res.rows[i].kpiColor) {
                            color = "red";
                        } else if ("amber" == res.rows[i].kpiColor) {
                            color = "#DD9C00";
                        }
                        html += '<tr style = "color : ' + color + ';">';
                        if ("true" == dashSupportFlag) {
                            html += '<td><input type="checkbox" name="taskId" id= "taskId" data-appNo="'+ res.rows[i].applicationNo+'" data-taskstatus = "' + res.rows[i].status + '" value="' + res.rows[i].taskMaskId + '" onclick="chooseFirstcheckBox(' + divid + ')"></td>'
                        }
                        html += '<td><p class="visible-xs visible-sm table-row-title">Application No.</p><p><a onclick="javascript:dashboardAppViewShow(' + "'" + res.rows[i].id + "'" + ');">' + res.rows[i].applicationNo + '</a></p></td>';
                        html += '<td><p class="visible-xs visible-sm table-row-title">Application Type</p><p>' + res.rows[i].appTypeStrShow + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Service Licence</p><p>' + res.rows[i].serviceName + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Officer Assigned</p><p>';
                        for (let j = 0; j < res.rows[i].appOwnerList.length; j++) {
                            html += res.rows[i].appOwnerList[j] + '<br>';
                        }
                        html += '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Days at Current Stage</p><p>' + res.rows[i].slaDays + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Target TAT at Current Stage</p><p>' + res.rows[i].kpi + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Total Days Taken for Application</p><p>' + res.rows[i].tolalSlaDays + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Total TAT for Application</p><p>' + res.rows[i].allStageSumKpi + '</p></td>' +
                            '</tr>';
                    }
                    html += '</tbody></table></div></td></tr>';
                    $('#advfilter' + divid).after(html);
                }
            }
        )
    };

    var concealAppDetail = function () {
        console.log('concealAppDetail ...');
        var $currentTr = $(this).closest('tr');
        var recordCount = $currentTr.find('input[name="recordCount"]').val();
        $currentTr.find('input[name="loadFlag"]').val('1');
    };

    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
    };
    function dashboardAppViewShow(appPremCorrId) {
        showWaiting();
        $.post(
            '/main-web/hcsa/intranet/dashboard/applicationView.show',
            {appPremCorrId: appPremCorrId},
            function (data) {
                let dashAppShowFlag = data.dashAppShowFlag;
                if ('SUCCESS' == dashAppShowFlag) {
                    window.open ("/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService");
                    dismissWaiting();
                } else {
                    dismissWaiting();
                }
            }
        );
        dismissWaiting();
    }

    function jumpToPagechangePage() {
        showWaiting();
        $("[name='hcsaBeDashboardSwitchType']").val('syspage');
        var mainPoolForm = document.getElementById('beDashboardForm');
        mainPoolForm.submit();
    }
</script>

