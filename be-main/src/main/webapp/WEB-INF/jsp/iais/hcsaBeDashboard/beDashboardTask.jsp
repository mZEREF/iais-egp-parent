<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/4/2
  Time: 15:21
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
          <button aria-label="Close" data-dismiss="alert" class="close" type="button" onclick="javascript:closeMaintainace();"><span aria-hidden="true">x</span></button>
          <h3 style="margin-top:0;"><i class="fa fa-wrench"></i> Upcoming Scheduled Maintainace</h3> <%--NOSONAR--%>
          <c:out value="${schEdule_AlERt_Msg__atTR}" escapeXml="false"/></div>
      </c:if>
      <c:if test="${not empty bAnner_AlERt_Msg__atTR}">
        <div class="dashalert alert-info dash-announce alertBanner">
          <button aria-label="Close" data-dismiss="alert" class="close" type="button" onclick="javascript:closeBanner();"><span aria-hidden="true">x</span></button>
          <h3 style="margin-top:0;"><i class="fa fa-bell"></i> Announcement</h3><%--NOSONAR--%>
          <c:out value="${bAnner_AlERt_Msg__atTR}" escapeXml="false"/>
        </div>
      </c:if>
    </div>
  </c:if>
  <form method="post" id="beDashboardForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="hcsaBeDashboardSwitchType" value="">
    <input type="hidden" id="action" name="action" value="">
    <input type="hidden" id="switchAction" name="switchAction" value="${dashSwitchActionValue}">
    <input type="hidden" id="chkIdList" name="chkIdList" value="">
    <input type="hidden" id="taskId" name="taskId" value="">
    <div class="col-xs-12">
      <div class="center-content">
        <div class="intranet-content">
          <iais:body>
            <iais:section title="" id = "demoList">
              <div class="form-horizontal">
                <div class="form-group">
                  <label class="col-xs-12 col-md-4 control-label">Role</label>
                  <div class="col-xs-10 col-sm-7 col-md-6">
                    <iais:select name="beDashRoleId" onchange="chooseCurRole()" options="beDashRoleIds"
                                 cssClass="roleIds" value="${dashRoleCheckDto.checkCurRole}" needSort="true"></iais:select>
                  </div>
                </div>
              </div>
              <%@ include file="/WEB-INF/jsp/iais/hcsaBeDashboard/beDashboardMenu.jsp" %>
              <br>
              <div class="row">
                <div class="col-xs-10 col-md-12">
                  <div class="components">
                    <a class="btn btn-secondary" data-toggle="collapse" name="filterBtn"
                       data-target="#beInboxFilter">Filter</a>
                  </div>
                </div>
              </div>
              <p></p>
              <div id = "beInboxFilter" class="collapse">
                <iais:row>
                  <iais:field value="Application No."/>
                  <iais:value width="18">
                    <input type="text" name="application_no" value="${dashSearchParam.filters['application_no']}"/>
                  </iais:value>
                </iais:row>
                <iais:row>
                  <iais:field value="Application Type"/>
                  <iais:value width="18">
                    <iais:select name="application_type" options="appTypeOption" cssClass="application_type"
                                 firstOption="Please Select" needSort="true"
                                 value="${dashSearchParam.filters['application_type']}"></iais:select>
                  </iais:value>
                </iais:row>
                <c:if test="${'common' ne dashSwitchActionValue && 'reply' ne dashSwitchActionValue}">
                  <iais:row>
                    <iais:field value="Application Status"/>
                    <iais:value width="18">
                      <c:if test="${empty dashCommonPoolStatus}">
                        <iais:select name="application_status" options="appStatusOption" needSort="true"
                                     cssClass="application_status" firstOption="Please Select"
                                     value="${dashAppStatus}"></iais:select>
                      </c:if>
                      <c:if test="${not empty dashCommonPoolStatus}">
                        <iais:select name="application_status" options="appStatusOption" needSort="true"
                                     cssClass="application_status" firstOption="Please Select"
                                     value="APST029"></iais:select>
                      </c:if>
                    </iais:value>
                  </iais:row>
                </c:if>
                <iais:row>
                  <iais:field value="HCI Code"/>
                  <iais:value width="18">
                    <input type="text" name="hci_code" value="${dashSearchParam.filters['hci_code']}"/>
                  </iais:value>
                </iais:row>
                <iais:row>
                  <iais:field value="HCI Name"/>
                  <iais:value width="18">
                    <input type="text" name="hci_name" value="${dashSearchParam.filters['hci_name']}"/>
                  </iais:value>
                </iais:row>
                <iais:row>
                  <iais:field value="HCI Address"/>
                  <iais:value width="18">
                    <input type="text" name="hci_address" value="${dashSearchParam.filters['hci_address']}"/>
                  </iais:value>
                </iais:row>
                <iais:action style="text-align:right;">
                  <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                  <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                </iais:action>
              </div>
            </iais:section>
            <br>
            <br>
            <h3>
              <span>Search Results</span>
            </h3>
            <iais:pagination param="dashSearchParam" result="dashSearchResult"/>
            <div class="table-gp">
              <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                <thead>
                <tr>
                  <th scope="col" style="display: none"></th>
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
                        <td><p style="width: 165px;"><c:out value="${pool.appGroupNo}"/><a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right"
                                                                                                   data-toggle="collapse" aria-expanded="false"
                                                                                                   data-target="#dropdown${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}"
                                                                                                   onclick="getAppByGroupId('${pool.appGroupNo}','${(status.index + 1) + (dashSearchParam.pageNo - 1) * dashSearchParam.pageSize}')"></a></p></td>
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
            <c:if test="${'assignme' eq dashSwitchActionValue}">
              <c:choose>
                <c:when test="${'AO1' eq iais_Login_User_Info_Attr.curRoleId}">
                  <div class="application-tab-footer">
                    <div class="row">
                      <div class="col-xs-11 col-md-11">
                        <div class="text-right">
                          <a class="btn btn-primary btn-support"
                             onclick="javascript:approve()">Support</a>
                          <a class="btn btn-primary btn-approve"
                             onclick="javascript:aoApprove('ao1approve')">Approve</a>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:when>
                <c:when test="${'AO2' eq iais_Login_User_Info_Attr.curRoleId}">
                  <div class="application-tab-footer">
                    <div class="row">
                      <div class="col-xs-11 col-md-11">
                        <div class="text-right">
                          <a class="btn btn-primary btn-support"
                             onclick="javascript:approve()">Support</a>
                          <a class="btn btn-primary btn-approve"
                             onclick="javascript:aoApprove('ao2approve')">Approve</a>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:when>
                <c:when test="${'AO3' eq iais_Login_User_Info_Attr.curRoleId}">
                  <div class="application-tab-footer">
                    <div class="row">
                      <div class="col-xs-11 col-md-11">
                        <div class="text-right">
                          <a class="btn btn-primary btn-approve"
                             onclick="javascript:approve()">Approve</a>
                          <a class="btn btn-primary btn-trigger"
                             onclick="javascript:trigger()">Trigger to DMS</a>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:when>
              </c:choose>
            </c:if>
          </iais:body>
        </div>
      </div>
    </div>
    <iais:confirm msg="GENERAL_ERR0023"  needCancel="false" callBack="cancel()" popupOrder="support" ></iais:confirm>
    <iais:confirm msg=""  needCancel="false" callBack="aocancel()" popupOrder="approveAo" ></iais:confirm>
  </form>
</div>
<script type="text/javascript">
    var dividajaxlist = [];

    $("#clearBtn").click(function () {
        $('input[name="application_no"]').val("");
        $('input[name="hci_code"]').val("");
        $('input[name="hci_address"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="application_status"]').val("");
        $("#beInboxFilter .current").text("Please Select");
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#inspector_name option:first").prop("selected", 'selected');
        $("#searchCondition .current").text("Please Select");
    })

    function cancel() {
        $('#support').modal('hide');
    }
    function aocancel() {
        $('#approveAo').modal('hide');
    }
    function tagConfirmCallbacksupport(){
        $('#support').modal('hide');
    }
    function tagConfirmCallbackapproveAo(){
        $('#approveAo').modal('hide');
    }
    function intraDashboardSubmit(action) {
        showWaiting();
        $("[name='hcsaBeDashboardSwitchType']").val(action);
        var mainPoolForm = document.getElementById('beDashboardForm');
        mainPoolForm.submit();
    }

    $("#searchBtn").click(function () {
        showWaiting();
        let dashSwitchActionValue = $('#switchAction').val();
        intraDashboardSubmit(dashSwitchActionValue);
    })

    function groupAjax(applicationGroupNo, divid) {
        dividajaxlist.push(divid);
        $.post(
            '/main-web/hcsa/intranet/dashboard/appGroup.do',
            {groupNo: applicationGroupNo},
            function (data) {
                let dashSupportFlag = data.dashSupportFlag;
                let result = data.result;
                if('Success' == result) {
                    let res = data.ajaxResult;
                    let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                        '<td colspan="7" style="padding: 0px 8px !important;">' +
                        '<div class="accordian-body p-3 collapse in" id="dropdown' + divid + '" aria-expanded="true" style="">' +
                        '<table class="table application-item" style="background-color: #F3F3F3;margin-bottom:0px;" >' +
                        '<thead>' +
                        '<tr>';
                    if ("true" == dashSupportFlag) {
                        html += '<th scope="col" ><input type="checkbox" id="checkbox' + divid + '" onclick="chooseAllcheckBox(' + divid + ')" </th>';
                    }

                    html += '<th width="15%">Application No.</th>' +
                        '<th width="15%">Service</th>' +
                        '<th width="15%">Licence Expiry Date</th>' +
                        '<th width="15%">Application Status</th>' +
                        '<th width="15%">HCI Code</th>' +
                        '<th width="25%">HCI Name / Address</th>' +
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
                        let canDoTask = res.rows[i].canDoTask;
                        if('1' == canDoTask) {
                            html += '<td><p class="visible-xs visible-sm table-row-title">Application No.</p><p><a href="#" id="' + res.rows[i].taskMaskId + '" onclick="javascript:doDashboardTaskOrShow(' + "'" + res.rows[i].taskMaskId + "'" + ');">' + res.rows[i].applicationNo + '</a></p></td>';
                        } else if ('2' == canDoTask) {
                            html += '<td><p class="visible-xs visible-sm table-row-title">Application No.</p><p><a href="#" id="' + res.rows[i].taskMaskId + '" class="applicationNoAHref" data-href=' + res.rows[i].dashTaskUrl +' data-task=' + res.rows[i].taskMaskId +  '>' + res.rows[i].applicationNo + '</a></p></td>';
                        } else {
                            html += '<td><p class="visible-xs visible-sm table-row-title">Application No.</p><p><a href="#" id="' + res.rows[i].taskMaskId + '" onclick="javascript:dashboardAppViewShow(' + "'" + res.rows[i].id + "'" + ');">' + res.rows[i].applicationNo + '</a></p></td>';
                        }
                        html += '<td><p class="visible-xs visible-sm table-row-title">Service</p><p>' + res.rows[i].serviceName + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Licence Expiry Date</p><p>' + res.rows[i].licenceExpiryDateStr + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Application Status</p><p>' + res.rows[i].appStatusStrShow + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">HCI Code</p><p>' + res.rows[i].hciCode + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">HCI Name / Address</p><p>' + res.rows[i].hciAddress + '</p></td>' +
                            '</tr>';
                    }
                    html += '</tbody></table></div></td></tr>';
                    $('#advfilter' + divid).after(html);
                }
            }
        )
    }

    function doDashboardTaskOrShow(taskId) {
        showWaiting();
        $("#taskId").val(taskId);
        intraDashboardSubmit('comassign');
    }

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
        )
        dismissWaiting();
    }

    function getAppByGroupId(applicationGroupNo, divid) {
        if (!isInArray(dividajaxlist,divid)) {
            groupAjax(applicationGroupNo, divid);
        } else {
            var display = $('#advfilterson' + divid).css('display');
            if (display == 'none') {
                $('#advfilterson' + divid).show();
            } else {
                $('#advfilterson' + divid).hide();
            }
        }
    }

    function jumpToPagechangePage() {
        showWaiting();
        intraDashboardSubmit('page');
    }

    function aoApprove(action) {
        if ($("input:checkbox:checked").length > 0) {
            showWaiting();
            var arr = new Array();
            var num = 0;
            $("input:checkbox:checked").each(function(i){
                console.log($("#"+$(this).val()).html());
                if($(this).val() != "on" && $("#"+$(this).val()).html() != ""){
                    arr[num] = $("#"+$(this).val()).html();
                    num ++;
                }
            });
            $.ajax({
                url:'${pageContext.request.contextPath}/hcsa/intranet/dashboard/aoApprove.do',
                data:{
                    applications:  arr.toString()
                },
                contentType:"application/x-www-form-urlencoded",
                type:'POST',
                'success':function (data) {
                    if(data.res == 1){
                        $('#action').val(action);
                        $('#switchAction').val('approve');
                        intraDashboardSubmit('approve');
                    }else{
                        dismissWaiting();
                        $('#approveAo .modal-body span').html(data.noApprove);
                        $('#approveAo').modal('show');
                    }
                }
            });
            dismissWaiting();
        } else {
            $('#support').modal('show');
        }
    }

    function approve() {
        if ($("input:checkbox:checked").length > 0) {
            showWaiting();
            var approveStatus = true;
            var data ;
            $("input:checkbox:checked").each(function () {
                console.log($(this).attr('data-taskstatus'));
                if ($(this).attr('data-taskstatus') == 'Pending Internal Clarification') {
                    approveStatus = false;
                    data = $(this).attr('data-appNo');
                }
            });

            console.log(approveStatus);
            if(!approveStatus){
                dismissWaiting();
                $('#approveAo .modal-body span').html(data + " You have no access to support.");
                $('#approveAo').modal('show');
            }else{
                $('#switchAction').val('approve');
                $('#action').val('approve');
                intraDashboardSubmit('approve');
            }
        } else {
            $('#support').modal('show');
        }
    }

    function trigger() {
        if ($("input:checkbox:checked").length > 0) {
            $('#action').val('trigger');
            $('#switchAction').val('approve');
            showWaiting();
            intraDashboardSubmit('approve');
        } else {
            $('#support').modal('show');
        }

    }

    function chooseAllcheckBox(id) {
        if ($('#checkbox' + id).prop('checked')) {
            $('#advfilterson' + id + ' input[type="checkbox"]').prop("checked", true)
        } else {
            $('#advfilterson' + id + ' input[type="checkbox"]').prop("checked", false)
        }
    }

    function chooseFirstcheckBox(id) {
        var divid = 'checkbox' + id;
        var flag = true;

        $('#advfilterson' + id + ' input[type="checkbox"]').each(function () {
            if ($(this).attr('id') != divid) {
                if (!$(this).is(':checked')) {
                    flag = false;
                }
            }
        });
        if (flag) {
            $('#checkbox' + id).prop("checked", true)
        } else {
            $('#checkbox' + id).prop("checked", false)
        }
    }

    function chooseCurRole() {
        showWaiting();
        let roleSelect = document.getElementById("beDashRoleId");
        let index = roleSelect.selectedIndex ;
        let roleSelectVal = roleSelect.options[index].value;
        $.post(
            '/main-web/hcsa/intranet/dashboard/dashRole.switch',
            {roleSelectVal: roleSelectVal},
            function (data) {
                let dashRoleSwitchFlag = data.dashRoleSwitchFlag;
                if ('SUCCESS' == dashRoleSwitchFlag) {
                    $('#switchAction').val('system');
                    intraDashboardSubmit("system");
                } else if ('FAIL' == dashRoleSwitchFlag) {
                    url = "${pageContext.request.contextPath}/eservice/INTRANET/MohHcsaBeDashboard";
                    window.location.href = url;
                }else {
                    let dashSwitchActionValue = $('#switchAction').val();
                    intraDashboardSubmit(dashSwitchActionValue);
                }
            }
        )
    }

    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
    }

    function closeBanner() {
        $('.alertBanner').hide();
        $.ajax({
            data:{},
            type:"POST",
            dataType: 'json',
            url: '/main-web/backend/closeBanner.do',
            error:function(data){},
            success:function(data){}
        });
    }

    function closeMaintainace() {
        $('.alertMaintainace').hide();
        $.ajax({
            data:{},
            type:"POST",
            dataType: 'json',
            url: '/main-web/backend/closeMaintenance.do',
            error:function(data){},
            success:function(data){}
        });
    }

    $(document).on("click",".applicationNoAHref",function(){
        showWaiting();
        var href = this.getAttribute("data-href");
        var task = this.getAttribute("data-task");
        $.ajax({
            data:{
                taskId: task,
            },
            type:"POST",
            dataType: 'json',
            url: '/main-web/hcsa/intranet/dashboard/changeTaskStatus.do',
            error:function(data){

            },
            success:function(data){
                window.location.href = href;
            }
        });
    })
</script>