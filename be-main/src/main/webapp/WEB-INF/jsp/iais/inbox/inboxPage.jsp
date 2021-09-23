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
      <h3 style="margin-top:0;"><i class="fa fa-wrench"></i> Upcoming Scheduled Maintenance</h3> <%--NOSONAR--%>
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
    <form method="post" id="mainSupForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="SearchSwitchType" value="">
        <input type="hidden" id="action" name="action" value="">
        <input type="hidden" id="chkIdList" name="chkIdList" value="">
        <input type="hidden" id="inspector_name" name="inspector_name" value="">
        <div class="col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <iais:body>
                        <iais:section title="" id = "demoList">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">Role</label>
                                    <div class="col-xs-10 col-sm-7 col-md-6">
                                        <iais:select name="roleIds" onchange="chooseCurRole()" options="roleIds"
                                                     cssClass="roleIds" value="${curRole}"></iais:select>
                                    </div>
                                </div>
                            </div>
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
                                        <input type="text" name="application_no" value="${application_no}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Application Type"/>
                                    <iais:value width="18">
                                        <iais:select name="application_type" options="appTypeOption" cssClass="application_type"
                                                     firstOption="Please Select" needSort="true"
                                                     value="${backendinboxSearchParam.filters['application_type']}"></iais:select>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Application Status"/>
                                    <iais:value width="18">
                                        <iais:select name="application_status" options="appStatusOption"
                                                     cssClass="application_status"
                                                     firstOption="Please Select"
                                                     value="${application_status}"></iais:select>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="HCI Code"/>
                                    <iais:value width="18">
                                        <input type="text" name="hci_code" value="${backendinboxSearchParam.filters['hci_code']}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="HCI Name"/>
                                    <iais:value width="18">
                                        <input type="text" name="hci_name" value="${hci_name}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="HCI Address"/>
                                    <iais:value width="18">
                                        <input type="text" name="hci_address" value="${hci_address}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:action style="text-align:right;">
                                    <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                                    <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                                </iais:action>
                            </div>
                        </iais:section>
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <iais:pagination param="backendinboxSearchParam" result="supTaskSearchResult"/>
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
                                    <iais:sortableHeader needSort="false" field="SUBMIT_DT" value="Last Modified Date"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="PMT_STATUS" value="Payment Status"></iais:sortableHeader>
                                </tr>
                                </thead>
                                <c:choose>
                                    <c:when test="${empty supTaskSearchResult.rows}">
                                        <tr>
                                            <td colspan="7">
                                                <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="pool" items="${supTaskSearchResult.rows}"
                                                   varStatus="status">
                                            <tr style="display: table-row;" id="advfilter${(status.index + 1) + (backendinboxSearchParam.pageNo - 1) * backendinboxSearchParam.pageSize}">
                                                <td><c:out value="${(status.index + 1) + (backendinboxSearchParam.pageNo - 1) * backendinboxSearchParam.pageSize}"/></td>
                                                <td><p style="width: 165px;"><c:out value="${pool.applicationGroupNo}"/><a class="accordion-toggle  collapsed" style="float: right"
                                                                                                                           data-toggle="collapse" aria-expanded="false"
                                                                                                                           data-target="#advfilter${(status.index + 1) + (backendinboxSearchParam.pageNo - 1) * backendinboxSearchParam.pageSize}"
                                                                                                                           onclick="getAppByGroupId('${pool.applicationGroupNo}','${(status.index + 1) + (backendinboxSearchParam.pageNo - 1) * backendinboxSearchParam.pageSize}')"></a></p></td>
                                                <td><c:out value="${pool.applicationType}"/></td>
                                                <td><c:if test="${pool.count > 1}"><c:out value="Multiple"/></c:if><c:if test="${pool.count == 1}"><c:out value="Single"/></c:if></td>
                                                <td><c:out value="${pool.submitDate}"/></td>
                                                <td><fmt:formatDate value='${pool.groupUpDt}' pattern='dd/MM/yyyy' /></td>
                                                <td><c:out value="${pool.paymentstatus}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                        </div>
                        <c:choose>
                            <c:when test="${\"AO1\".equals(curRole)}">
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
                            <c:when test="${\"AO2\".equals(curRole)}">
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
                            <c:when test="${\"AO3\".equals(curRole)}">
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

    function inspectorSearchTask_optionNameAuto(value) {
        if (value != null && value != null) {
            $("#inspectorSearchTask_inspectorName").val(value);
        }
        doInspectorSearchTaskSelect();
    }

    function doInspectorSearchTaskSelect() {
        var options = $("#inspectorSearchTask_inspectorName option:selected");
        $("#inspector_name").val(options);
    }

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


        $.ajax({
            data: {remove: 1},
            type: "POST",
            dataType: 'json',
            url: '/main-web/backend/removeSearchParam.do',
            error: function (data) {

            },
            success: function (data) {

            }
        })
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
    function submit(action) {
        showWaiting();
        $("[name='SearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainSupForm');
        mainPoolForm.submit();
    }

    $("#searchBtn").click(function () {
        showWaiting();
        submit('search');
    })

    function groupAjax(applicationGroupNo, divid) {
        console.log("groupajax")
        dividajaxlist.push(divid);
        $.post(
            '/main-web/backend/appGroup.do',
            {groupno: applicationGroupNo},
            function (data, status) {
                console.log("ajax start")
                var serviceName = data.serviceName;
                var res = data.ajaxResult;
                var url = data.appNoUrl;
                var taskList = data.taskList;
                var hastaskList = data.hastaskList;
                var html = '';
                html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                    '<td colspan="7" style="padding: 0px 8px !important;">' +
                    '<div class="accordian-body p-3 collapse in" id="row1" aria-expanded="true" style="">' +
                    '<table class="table application-item" style="background-color: #F3F3F3;margin-bottom:0px;" >' +
                    '<thead>' +
                    '<tr>';
                if (hastaskList == "true") {
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
                for (var i = 0; i < res.length; i++) {
                    var color = "black";
                    if (res[i].timeLimitWarning == "black") {
                        color = "black";
                    } else if (res[i].timeLimitWarning == "red") {
                        color = "red";
                    } else if (res[i].timeLimitWarning == "amber") {
                        color = "#DD9C00";
                    }
                    if(res[i].hciName == null || res[i].hciName == ''){
                        var address = res[i].address;
                    }else{
                        var address = res[i].hciName + ' / ' +res[i].address;
                    }

                    html += '<tr style = "color : ' + color + ';">';
                    if (hastaskList == "true") {
                        html += '<td><input type="checkbox" name="taskId" id= "taskId" data-appNo="'+ res[i].applicationNo+'" data-taskstatus = "'+res[i].status+'" value="' + taskList[res[i].refNo] + '" onclick="chooseFirstcheckBox(' + divid + ')"></td>'
                    }
                    html += '<td><p class="visible-xs visible-sm table-row-title">Application No.</p><p><a id="' + taskList[res[i].refNo] + '" class="applicationNoAHref" data-href=' + url[res[i].refNo] +' data-task=' + taskList[res[i].refNo] +  '>' + res[i].applicationNo + '</a></p></td>' +
                        '<td><p class="visible-xs visible-sm table-row-title">Service</p><p>' + serviceName[res[i].serviceId] + '<p></td>' +
                        '<td><p class="visible-xs visible-sm table-row-title">License Expiry Date</p><p>' + res[i].expiryDate + '</p></td>' +
                        '<td><p class="visible-xs visible-sm table-row-title">Application Status</p><p>' + res[i].status + '</p></td>' +
                        '<td><p class="visible-xs visible-sm table-row-title">HCi Code</p><p>' + res[i].hciCode + '</p></td>' +
                        '<td><p class="visible-xs visible-sm table-row-title">HCi Address</p><p>' + address + '</p></td>' +
                        '</tr>';
                }
                html += '</tbody></table></div></td></tr>';
                console.log(dividajaxlist)
                console.log(divid)
                console.log("ajax end")
                $('#advfilter' + divid).after(html);
            }
        )


    }

    function getAppByGroupId(applicationGroupNo, divid) {
        console.log("getAppByGroupId")
        console.log(dividajaxlist)
        console.log(divid)
        if (!isInArray(dividajaxlist,divid)) {
            groupAjax(applicationGroupNo, divid);
        } else {
            console.log("show or hide")
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
        submit('page');
    }

    function aoApprove(action) {
        if ($("input:checkbox:checked").length > 0) {
            var arr = new Array();
            var num = 0;
            $("input:checkbox:checked").each(function(i){

                if($(this).val() != "on" && $("#"+$(this).val()).html() != ""){
                    arr[num] = $("#"+$(this).val()).html();
                    num ++;
                }
            });
            $.ajax({
                url:'${pageContext.request.contextPath}/backend/aoApprove.do',
                data:{
                    applications:  arr.toString()
                },
                contentType:"application/x-www-form-urlencoded",
                type:'POST',
                'success':function (data) {
                    if(data.res == 1){
                        $('#action').val(action);
                        submit('approve');
                    }else{
                        $('#approveAo .modal-body span').html(data.noApprove+ " You have no access to approve.");
                        $('#approveAo').modal('show');

                    }
                }
            });
            /*$.post(
                '/main-web/backend/aoApprove.do',
                {'applications':  arr},
                function (data, status) {
                    // $('#action').val(action);
                    // showWaiting();
                    // submit('approve');
                }
            );*/

        } else {
            $('#support').modal('show');
        }
    }

    function approve() {
        if ($("input:checkbox:checked").length > 0) {
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
                $('#approveAo .modal-body span').html(data+ " You have no access to support.");
                $('#approveAo').modal('show');
            }else{
                $('#action').val('approve');
                showWaiting();
                submit('approve');
            }

        } else {
            $('#support').modal('show');
        }

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

    function trigger() {
        if ($("input:checkbox:checked").length > 0) {
            $('#action').val('trigger');
            showWaiting();
            submit('approve');
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
        submit('changeRole');
    }

    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
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
            url: '/main-web/backend/changeTaskStatus.do',
            error:function(data){

            },
            success:function(data){
                window.location.href = href;
            }
        });
    })

</script>
