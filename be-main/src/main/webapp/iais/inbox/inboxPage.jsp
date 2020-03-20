<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>

<div class="main-content">
    <form method="post" id="mainSupForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="SearchSwitchType" value="">
        <input type="hidden" id="taskId" name="taskId" value="">
        <input type="hidden" id="action" name="action" value="">
        <input type="hidden" id="chkIdList" name="chkIdList" value="">
        <input type="hidden" id="inspector_name" name="inspector_name" value="">
        <div class="col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Role</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:select name="roleIds" onchange="chooseCurRole()" options="roleIds"
                                             cssClass="roleIds"
                                             firstOption="${curRole}" value="${curRole}"></iais:select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-10 col-md-12">
                        <div class="components">
                            <a class="btn btn-secondary" data-toggle="collapse"
                               data-target="#searchCondition">Filter</a>
                        </div>
                    </div>
                </div>

                <div id="searchCondition" class="collapse">
                    <div class="form-horizontal filter-box">
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">HCI Code</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input type="text" name="hci_code" value="${hci_code}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">HCI Name / HCI Address</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input type="text" name="hci_address" value="${hci_address}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Application No.</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input type="text" name="application_no" value="${application_no}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Application Type</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:select name="application_type" options="appTypeOption" cssClass="application_type"
                                             firstOption="Please Select"
                                             value="${supTaskSearchParam.filters['application_type']}"></iais:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Application Status</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:select name="application_status" options="appStatusOption"
                                             cssClass="application_status"
                                             firstOption="Please Select"
                                             value="${supTaskSearchParam.filters['application_status']}"></iais:select>
                            </div>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-11 col-md-11">
                                <div class="text-right">
                                    <a class="btn btn-secondary"
                                       onclick="javascript:doInspectorSearchTaskClear()">Clear</a>
                                    <a class="btn btn-primary"
                                       onclick="javascript:doInspectorSearchTaskSearch()">Search</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <iais:pagination param="supTaskSearchParam" result="supTaskSearchResult"/>

            <div class="col-xs-12" style="margin-top: 2.5em;">
                <div class="components">
                    <h3>
                        <span>Search Results</span>
                    </h3>


                                <div class="table-gp">
                                    <table class="table" style="border-collapse:collapse;">
                                        <thead>
                                        <tr>
                                            <th>S/N</th>
                                            <th>Application No.</th>
                                            <th>Application Type</th>
                                            <th>Submission Type</th>
                                            <th>Application Date</th>
                                            <th>Payment Status</th>
                                        </tr>
                                        </thead>
                                        <c:choose>
                                            <c:when test="${empty supTaskSearchResult.rows}">
                                                <tr>
                                                    <td colspan="6">
                                                        <iais:message key="ACK018" escape="true"></iais:message>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="pool" items="${supTaskSearchResult.rows}"
                                                           varStatus="status">
                                                    <tr style="display: table-row;"
                                                        id="advfilter${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}">
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                            <p><c:out
                                                                    value="${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Application
                                                                No</p>
                                                            <p>
                                                                    <c:out value="${pool.applicationGroupNo}"/>
                                                                <a class="accordion-toggle  collapsed"
                                                                   data-toggle="collapse" aria-expanded="false"
                                                                   data-target="#advfilter${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"
                                                                   onclick="getAppByGroupId('${pool.applicationGroupNo}','${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}')">
                                                                </a>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Application
                                                                Type</p>
                                                            <p><c:out value="${pool.applicationType}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Submission
                                                                Type</p>
                                                            <c:if test="${pool.count > 1}">
                                                                <p><c:out value="Multiple"/></p>
                                                            </c:if>
                                                            <c:if test="${pool.count == 1}">
                                                                <p><c:out value="Single"/></p>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Application
                                                                Date</p>
                                                            <p><c:out value="${pool.submitDate}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Payment
                                                                Status</p>
                                                            <p><c:out value="${pool.paymentstatus}"/></p>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </table>
                                </div>
                </div>
            </div>
            <c:choose>
                <c:when test="${\"AO1\".equals(curRole)}">
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-11 col-md-11">
                                <div class="text-right">
                                    <a class="btn btn-primary"
                                       onclick="javascript:approve()">Support</a>
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
                                    <a class="btn btn-primary"
                                       onclick="javascript:approve()">Support</a>
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
                                    <a class="btn btn-primary"
                                       onclick="javascript:approve()">Approve</a>
                                    <a class="btn btn-primary"
                                       onclick="javascript:trigger()">Trigger to DMS</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:when>
            </c:choose>
        </div>
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

    function doInspectorSearchTaskAssign(taskId) {
        $("#taskId").val(taskId);
        showWaiting();
        submit('assign');
    }

    function doInspectorSearchTaskClear() {
        $('input[name="application_no"]').val("");
        $('input[name="hci_code"]').val("");
        $('input[name="hci_address"]').val("");
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#inspector_name option:first").prop("selected", 'selected');
        $("#searchCondition .current").text("Please Select");
    }

    function submit(action) {
        showWaiting();
        $("[name='SearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainSupForm');
        mainPoolForm.submit();
    }

    function doInspectorSearchTaskSearch() {
        showWaiting();
        submit('search');
    }

    function getAppByGroupId(applicationGroupNo, divid) {
        if (!isInArray(dividajaxlist,divid)) {
            dividajaxlist.push(divid);
            $.post(
                '/main-web/backend/appGroup.do',
                {groupno: applicationGroupNo},
                function (data, status) {
                    var serviceName = data.serviceName;
                    var res = data.ajaxResult;
                    var url = data.appNoUrl;
                    var taskList = data.taskList;
                    var hastaskList = data.hastaskList;
                    var html = '';
                    html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                        '<td colspan="6" style="padding: 0px 8px !important;">' +
                        '<div class="accordian-body p-3 collapse in" id="row1" aria-expanded="true" style="">' +
                        '<table class="table" style="background-color: #F3F3F3;margin-bottom:0px;" >' +
                        '<thead>' +
                        '<tr>';
                    if (hastaskList == "true") {
                        html += '<th><input type="checkbox" id="checkbox' + divid + '" onclick="chooseAllcheckBox(' + divid + ')" </th>';
                    }

                    html += '<th>Application No</th>' +
                        '<th>Service</th>' +
                        '<th>Licence Expiry Date</th>' +
                        '<th>Application Status</th>' +
                        '<th>HCI Code</th>' +
                        '<th>HCI Address</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody>';
                    for (var i = 0; i < res.rowCount; i++) {
                        var color = "black";
                        if (res.rows[i].timeLimitWarning == "black") {
                            color = "black";
                        } else if (res.rows[i].timeLimitWarning == "red") {
                            color = "red";
                        } else if (res.rows[i].timeLimitWarning == "amber") {
                            color = "#DD9C00";
                        }
                        var address = res.rows[i].address;
                        // if(res.rows[i].blkNo != null && res.rows[i].blkNo != ""){
                        //     address = res.rows[i].blkNo;
                        // }
                        // if(res.rows[i].streetName != null && res.rows[i].streetName != ""){
                        //     address +=" " + res.rows[i].streetName;
                        // }
                        // if(res.rows[i].buildingName != null && res.rows[i].buildingName != ""){
                        //     address +=" " + res.rows[i].buildingName;
                        // }
                        // if(res.rows[i].floorNo != null && res.rows[i].floorNo != ""){
                        //     address +=" # " + res.rows[i].floorNo;
                        // }
                        // if(res.rows[i].unitNo != null && res.rows[i].unitNo != ""){
                        //     address += "-" + res.rows[i].unitNo;
                        // }
                        // if(res.rows[i].postalCode != null && res.rows[i].postalCode != ""){
                        //     address += "," + res.rows[i].postalCode;
                        // }
                        html += '<tr style = "color : ' + color + ';">';
                        if (hastaskList == "true") {
                            html += '<td><input type="checkbox" name="taskcheckbox" value="' + taskList[res.rows[i].refNo] + '" onclick="chooseFirstcheckBox(' + divid + ')"></td>'
                        }
                        html += '<td><p class="visible-xs visible-sm table-row-title">Application No</p><p><a href=' + url[res.rows[i].refNo] + '>' + res.rows[i].applicationNo + '</a></p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Service</p><p>' + serviceName[res.rows[i].serviceId] + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">License Expiry Date</p><p>N/A</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Application Status</p><p>' + res.rows[i].status + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">HCi Code</p><p>' + res.rows[i].hciCode + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">HCi Address</p><p>' + address + '</p></td>' +
                            '</tr>';
                    }
                    html += '</tbody></table></div></td></tr>';
                    $('#advfilter' + divid).after(html);
                }
            )

        } else {
            var display = $('#advfilterson' + divid).css('display');
            console.log(display)
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

    function approve() {
        if ($("input:checkbox:checked").length > 0) {
            $('#action').val('approve');
            showWaiting();
            submit('approve');
        } else {
            alert('Please select at least one application');
        }

    }

    function trigger() {
        if ($("input:checkbox:checked").length > 0) {
            $('#action').val('trigger');
            showWaiting();
            submit('approve');
        } else {
            alert('No checked');
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
        submit('page');
    }

    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
    }

</script>
