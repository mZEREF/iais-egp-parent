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
<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.service.client.MsgTemplateMainClient" %>
<%@ page import="java.util.List" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;

    String alertFlag = (String) ParamUtil.getSessionAttr(request, "AlERt__Msg_FLAg_attr");
    if (alertFlag == null) {
        MsgTemplateMainClient emc = SpringContextHelper.getContext().getBean(MsgTemplateMainClient.class);
        List<MsgTemplateDto> msgTemplateDtoList = emc.getAlertMsgTemplate(AppConsts.DOMAIN_INTRANET).getEntity();
        if (IaisCommonUtils.isEmpty(msgTemplateDtoList)) {
            ParamUtil.setSessionAttr(request, "AlERt__Msg_FLAg_attr", "noneed");
        } else {
            for (MsgTemplateDto mt : msgTemplateDtoList) {
                String msgContent = mt.getMessageContent().replaceAll("\r", "");
                msgContent = msgContent.replaceAll("\n", "");
                msgContent = msgContent.replaceAll("'", "&apos;");
                if (MsgTemplateConstants.MSG_TEMPLATE_BANNER_ALERT_BE.equals(mt.getId())) {
                    ParamUtil.setSessionAttr(request, "bAnner_AlERt_Msg__atTR", msgContent);
                } else if (MsgTemplateConstants.MSG_TEMPLATE_SCHEDULE_MAINTENANCE_BE.equals(mt.getId())) {
                    ParamUtil.setSessionAttr(request, "schEdule_AlERt_Msg__atTR", msgContent);
                }
            }
            ParamUtil.setSessionAttr(request, "AlERt__Msg_FLAg_attr", "fetched");
        }
    }
%>
<div class="main-content" style="min-height: 73vh;">
    <c:if test="${not empty bAnner_AlERt_Msg__atTR || not empty schEdule_AlERt_Msg__atTR}">
    <div class="col-md-12">
        <c:if test="${not empty schEdule_AlERt_Msg__atTR}">
            <div class="dashalert alert-info dash-announce alertMaintainace">
                <button aria-label="Close" data-dismiss="alert" class="close" type="button" onclick="javascript:$('.alertMaintainace').hide();"><span aria-hidden="true">x</span></button>
                <h3 style="margin-top:0;"><i class="fa fa-wrench"></i> Upcoming Scheduled Maintainace</h3> <%--NOSONAR--%>
                <c:out value="${schEdule_AlERt_Msg__atTR}" escapeXml="false"/></div>
        </c:if>
        <c:if test="${not empty bAnner_AlERt_Msg__atTR}">
            <div class="dashalert alert-info dash-announce alertBanner">
                <button aria-label="Close" data-dismiss="alert" class="close" type="button" onclick="javascript:$('.alertBanner').hide();"><span aria-hidden="true">x</span></button>
                <h3 style="margin-top:0;"><i class="fa fa-bell"></i> Announcement</h3><%--NOSONAR--%>
                <c:out value="${bAnner_AlERt_Msg__atTR}" escapeXml="false"/>
            </div>
        </c:if>
    </div>
    </c:if>

    <input type="hidden" name="overAllVal" value='${dashOverAllCircleKpi}'/>
    <input type="hidden" name="BLBVal" value='${dashOverAllCircleKpi}'/>
    <input type="hidden" name="CLBVal" value='${dashOverAllCircleKpi}'/>
    <input type="hidden" name="EASVal" value='${dashOverAllCircleKpi}'/>
    <input type="hidden" name="MTSVal" value='${dashOverAllCircleKpi}'/>
    <input type="hidden" name="NMAVal" value='${dashOverAllCircleKpi}'/>
    <input type="hidden" name="NMIVal" value='${dashOverAllCircleKpi}'/>
    <input type="hidden" name="RDSVal" value='${dashOverAllCircleKpi}'/>
    <input type="hidden" name="TSBVal" value='${dashOverAllCircleKpi}'/>

    <form method="post" id="beDashboardForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="hcsaBeDashboardSwitchType" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="row">
                            <div class="col-md-4 col-xs-12">
                                <a data-tab="#" href="javascript:;">
                                    <div id="canvas-holder">
                                        <canvas id="overAllCanvas"></canvas>
                                    </div>
                                    <p class="dashboard-txt main-chart-text"> Overall</p>
                                </a>
                            </div>
                            <div class="col-md-8 col-xs-12">
                                <div class="form-horizontal filter-box">
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">Application type</label>
                                        <div class="col-xs-12 col-sm-6 col-md-5" style="padding-left: unset;padding-top: 1%;">
                                            <iais:select cssClass="" name="appType" firstOption="" options="appTypeOption" multiValues="" multiSelect="true" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">Service Licence</label>
                                        <div class="col-xs-12 col-sm-6 col-md-5" style="padding-left: unset;padding-top: 1%;">
                                            <iais:select cssClass="" name="svcLic" firstOption="" needSort="true" options="dashServiceOption" multiValues="" multiSelect="true" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <hr>
                            <div class="col-xs-12">
                                <div class="dashboard-chart">
                                    <c:forEach var="svcOp" items="${dashServiceOption}" varStatus="status">
                                        <div class="dashboard-tile-item">
                                            <div class="dashboard-tile">
                                                <a data-tab="#" href="javascript:;">
                                                    <div>
                                                        <canvas id="${svcOp.value}Canvas"></canvas>
                                                    </div>
                                                    <p align="center" class="font-color-black">${svcOp.text}</p>
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

                                                <tr>
                                                    <td>1</td>
                                                    <td>
                                                        <input type="hidden" name="recordCount" value="1">
                                                        <input type="hidden" name="appNo" value="AQ200731002759L" />
                                                        <input type="hidden" name="loadFlag" value="0" />
                                                        <p style="width: 165px;">AQ200731002759L
                                                            <a class="app-group-div collapse in collapsed" style="float: right" data-toggle="collapse" aria-expanded="false" data-target="#advfilter1"></a>
                                                        </p>
                                                    </td>
                                                    <td>Request For Change</td>
                                                    <td>Multiple</td>
                                                    <td>05/01/2021</td>
                                                    <td>06/01/2021</td>
                                                    <td>Payment Successful</td>
                                                </tr>

                                                <tr>
                                                    <td>2</td>
                                                    <td>
                                                        <input type="hidden" name="recordCount" value="2">
                                                        <input type="hidden" name="appNo" value="AN2101050083408" />
                                                        <input type="hidden" name="loadFlag" value="0" />
                                                        <p style="width: 165px;">AN2101050083408
                                                            <a class="app-group-div  collapse in collapsed" style="float: right" data-toggle="collapse" aria-expanded="false" data-target="#advfilter2"></a>
                                                        </p>
                                                    </td>
                                                    <td>New Licence Application</td>
                                                    <td>Multiple</td>
                                                    <td>05/01/2021</td>
                                                    <td>06/01/2021</td>
                                                    <td>N/A</td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="pool" items="${dashSearchResult.rows}"
                                                           varStatus="status">
                                                    <tr style="display: table-row;" id="advfilter${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}">
                                                        <td><c:out value="${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}"/></td>
                                                        <td>
                                                            <p style="width: 165px;"><c:out value="${pool.appGroupNo}"/>
                                                                <a class="accordion-toggle  collapsed" style="float: right" data-toggle="collapse" aria-expanded="false" data-target="#advfilter${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}" onclick="getAppByGroupId('${pool.appGroupNo}','${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}')">
                                                                </a>
                                                            </p>
                                                        </td>
                                                        <td><iais:code code="${pool.applicationType}"/></td>
                                                        <td><c:out value="${pool.submissionType}"/></td>
                                                        <td><fmt:formatDate value='${pool.submitDt}' pattern='dd/MM/yyyy' /></td>
                                                        <td><fmt:formatDate value='${pool.groupUpDt}' pattern='dd/MM/yyyy' /></td>
                                                        <td><iais:code code="${pool.paymentStatus}"/></td>
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
    $(document).ready(function () {
        $('.app-group-div').click(function () {
            displayAppDetail($(this));
        });

        $('#Back').click(function () {
            showWaiting();
            $('input[name="hcsaBeDashboardSwitchType"]').val('system');
            var mainPoolForm = document.getElementById('beDashboardForm');
            mainPoolForm.submit();
        });

        chartRegister();
        initChart('overAll');
        <c:forEach var="svcOp" items="${dashServiceOption}" varStatus="status">
            initChart('${svcOp.value}');
        </c:forEach>
    });

    var displayAppDetail = function(obj){
        var display = obj.css('display');
        var $currentTr = obj.closest('tr');
        var appGrpNo = $currentTr.find('input[name="appNo"]').val();
        var loadFlag = $currentTr.find('input[name="loadFlag"]').val();
        var recordCount = $currentTr.find('input[name="recordCount"]').val();

        if('0' != loadFlag){
            if (display == 'none') {
                obj.collapse('hide');
            } else {
                obj.collapse('show');
            }
            return;
        }
        $.ajax({
            'url':'',
            'type':'',
            'data':'',
            'dataType':'',
            'success':function (data) {

            },
            'error':function () {

            }
        });

        var html = '<td colspan="7"><div id="advfilter' + recordCount +'" class="panel-collapse collapse">' +
            '<table class="table application-item" style="background-color: #F3F3F3;margin-bottom:0px;" >\n' +
            '            <thead>\n' +
            '            <tr>\n' +
            '                <th>Application No</th>\n' +
            '                <th>Application Type</th>\n' +
            '                <th>Service Licence</th>\n' +
            '                <th>Officer Assigned</th>\n' +
            '                <th>Target TAT at Current Stage</th>\n' +
            '                <th>Total Days Taken for Application</th>\n' +
            '                <th>Total TAT for Application</th>\n' +
            '            </tr>\n' +
            '            </thead>\n' +
            '            <tbody>\n' +
            '            <tr>\n' +
            '                <td>AQ200731002759L-01</td>\n' +
            '                <td>New</td>\n' +
            '                <td>Clinical Laboratory</td>\n' +
            '                <td>N/A</td>\n' +
            '                <td>N/A</td>\n' +
            '                <td>N/A</td>\n' +
            '                <td>N/A</td>\n' +
            '            </tr>\n' +
            '            <tr>\n' +
            '                <td>AQ200731002759L-02</td>\n' +
            '                <td>New</td>\n' +
            '                <td>Nuclear Medicine Assay</td>\n' +
            '                <td>N/A</td>\n' +
            '                <td>N/A</td>\n' +
            '                <td>N/A</td>\n' +
            '                <td>N/A</td>\n' +
            '            </tr>\n' +
            '            </tbody>\n' +
            '        </table>\n' +
            '    </div>\n' +
            '</td>';
        $currentTr.after(html);
        obj.collapse('show');
        $currentTr.find('input[name="loadFlag"]').val('1');

    };

    var concealAppDetail = function () {
        console.log('concealAppDetail ...');
        var $currentTr = $(this).closest('tr');
        var recordCount = $currentTr.find('input[name="recordCount"]').val();
        $currentTr.find('input[name="loadFlag"]').val('1');
    };

</script>

