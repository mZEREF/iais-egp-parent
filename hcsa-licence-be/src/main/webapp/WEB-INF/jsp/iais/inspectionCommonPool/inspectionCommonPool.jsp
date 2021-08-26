<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">


  <form method="post" id="mainPoolForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="inspectionPoolType" value="">
    <input type="hidden" id="appCorrelationId" name="appCorrelationId" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Common Pool</span>
                </h2>
              </div>
              <iais:body>
                <iais:section title="" id = "demoList">
                  <div class="form-horizontal">
                    <div class="form-group">
                      <label class="col-xs-12 col-md-4 control-label">Role</label>
                      <div class="col-xs-8 col-sm-6 col-md-6">
                        <iais:select name="commonRoleId" cssClass="roleIds" options="commonPoolRoleIds" id="commonPoolRole" value="${poolRoleCheckDto.checkCurRole}"></iais:select>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-xs-10 col-md-12">
                      <div class="components">
                        <a name="filterBtn" class="btn btn-secondary" data-toggle="collapse"
                           data-target="#commonPool">Filter</a>
                      </div>
                    </div>
                  </div>
                  <p></p>
                  <div id="commonPool" class="collapse">
                    <iais:row>
                      <iais:field value="Application No."/>
                      <iais:value width="18">
                        <input type="text" name="application_no" value="${cPoolSearchParam.filters['application_no']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Application Type"/>
                      <iais:value width="18">
                        <div id = "appComPoolSelect">
                          <iais:select cssClass="application_type" needSort="true" name="application_type" options="appTypeOption" firstOption="Please Select" value="${cPoolSearchParam.filters['application_type']}" ></iais:select>
                        </div>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="HCI Code"/>
                      <iais:value width="18">
                        <input type="text" name="hci_code" value="${cPoolSearchParam.filters['hci_code']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="HCI Name"/>
                      <iais:value width="18">
                        <input type="text" name="hci_name" value="${cPoolSearchParam.filters['hci_name']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="HCI Address"/>
                      <iais:value width="18">
                        <input type="text" name="hci_address" value="${cPoolSearchParam.filters['hci_address']}" />
                      </iais:value>
                    </iais:row>
                    <iais:action style="text-align:right;">
                      <button name="clearBtn" class="btn btn-secondary" type="button" onclick="javascript:doInspectionCommonPoolClear()">Clear</button>
                      <button name="searchBtn" class="btn btn-primary" type="button" onclick="javascript:doInspectionCommonPoolSearch()">Search</button>
                    </iais:action>
                  </div>
                </iais:section>
                <h3>
                  <span>Search Results</span>
                </h3>
                <iais:pagination  param="cPoolSearchParam" result="cPoolSearchResult"/>
                <div class="table-gp">
                  <table aria-describedby="" class="table application-group">
                    <thead>
                    <tr>
                      <th scope="col" style="display: none"></th>
                      <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="GROUP_NO" value="Application No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="APP_TYPE" value="Application Type"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="COU" value="Submission Type"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="SUBMIT_DT" value="Application Date"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="PMT_STATUS" value="Payment Status"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty cPoolSearchResult.rows}">
                        <tr>
                          <td colspan="7">
                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="pool" items="${cPoolSearchResult.rows}" varStatus="status">
                          <tr style = "display: table-row;" id = "advfilter${(status.index + 1) + (cPoolSearchParam.pageNo - 1) * cPoolSearchParam.pageSize}">
                            <td class="row_no"><c:out value="${(status.index + 1) + (cPoolSearchParam.pageNo - 1) * cPoolSearchParam.pageSize}"/></td>
                            <td>
                              <p>
                                <c:out value="${pool.appGroupNo}"/>
                                <a href="javascript:void(0);" class="accordion-toggle  collapsed"
                                   data-toggle="collapse" aria-expanded="false"
                                   data-target="#dropdown${(status.index + 1) + (cPoolSearchParam.pageNo - 1) * cPoolSearchParam.pageSize}"
                                   onclick="javascript:commonPoolByGroupId('<iais:mask name="appGroupNo" value="${pool.appGroupNo}"/>','${(status.index + 1) + (cPoolSearchParam.pageNo - 1) * cPoolSearchParam.pageSize}')">
                                </a>
                              </p>
                            </td>
                            <td><iais:code code="${pool.applicationType}"/></td>
                            <td><c:out value="${pool.submissionType}"/></td>
                            <td><fmt:formatDate value='${pool.submitDt}' pattern='dd/MM/yyyy' /></td>
                            <td><iais:code code="${pool.paymentStatus}"/></td>
                          </tr>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>
                    </tbody>
                  </table>
                  <iais:action style="text-align:right;">
                    <button name="assignComBtn" class="btn btn-primary" type="button" onclick="javascript:comPoolBatchAssignment()">Assign</button>
                  </iais:action>
                </div>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
    <iais:confirm msg="GENERAL_ERR0023"  needCancel="false" callBack="comPoolCancel()" popupOrder="comPoolAssign" ></iais:confirm>
  </form>
</div>
<script type="text/javascript">

    function doInspectionCommonPoolAssign(appCorrelationId) {
        showWaiting();
        $("#appCorrelationId").val(appCorrelationId);
        inspectionCommonPoolSubmit('assign');
    }

    function doInspectionCommonPoolClear() {
        $('input[name="application_no"]').val("");
        $('#application_type option:first').prop('selected', 'selected');
        $("#appComPoolSelect .current").text("Please Select");
        $('input[name="hci_code"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="hci_address"]').val("");
        $('input[name="service_name"]').val("");
        $('input[name="sub_date"]').val("");
    }

    function comPoolBatchAssignment() {
        if ($("input:checkbox:checked").length > 0) {
            showWaiting();
            inspectionCommonPoolSubmit('mulassign');
        } else {
            $('#comPoolAssign').modal('show');
        }
    }

    function comPoolCancel() {
        $('#comPoolAssign').modal('hide');
    }

    function tagConfirmCallbackcomPoolAssign(){
        $('#comPoolAssign').modal('hide');
    }

    function inspectionCommonPoolSubmit(action){
        $("[name='inspectionPoolType']").val(action);
        var mainPoolForm = document.getElementById('mainPoolForm');
        mainPoolForm.submit();
    }

    $("#commonPoolRole").change(function() {
        doInspectionCommonPoolSearch();
    });

    function doInspectionCommonPoolSearch() {
        showWaiting();
        inspectionCommonPoolSubmit('search');
    }

    function jumpToPagechangePage(){
        showWaiting();
        inspectionCommonPoolSubmit('page');
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        inspectionCommonPoolSubmit('sort');
    }

    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
    }

    var dividajaxlist = [];
    function commonPoolByGroupId(applicationGroupNo, divid) {
        if (!isInArray(dividajaxlist,divid)) {
            dividajaxlist.push(divid);
            $.post(
                '/hcsa-licence-web/common-pool/common.do',
                {groupNo: applicationGroupNo},
                function (data) {
                    var result = data.result;
                    if('Success' == result){
                        var res = data.ajaxResult;
                        var html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                            '<td colspan="6" style="padding: 0px 8px !important;">' +
                            '<div class="accordian-body p-3 collapse in" id="dropdown' + divid + '" aria-expanded="true" style="">' +
                            '<table aria-describedby="" class="table application-item" style="background-color: #F3F3F3;margin-bottom:0px;" >' +
                            '<thead>' +
                            '<tr>';
                        html += '<th scope="col" ><input type="checkbox" id="checkbox' + divid + '" onclick="commonPoolAllCheckBox(' + divid + ')" </th>';
                        html += '<th width="15%">Application No.</th>' +
                            '<th width="15%">Service</th>' +
                            '<th width="15%">Licence Expiry Date</th>' +
                            '<th width="15%">Application Status</th>' +
                            '<th width="15%">HCI Code</th>' +
                            '<th width="25%">HCI Name / Address</th>' +
                            '</tr>' +
                            '</thead>' +
                            '<tbody>';
                        for (var i = 0; i < res.rowCount; i++) {
                            html += '<tr>';
                            html += '<td><input type="checkbox" name="comPoolMulCheck" data-appNo="'+ res.rows[i].applicationNo+'" data-taskstatus = "' + res.rows[i].appStatus + '" value="' + res.rows[i].applicationNo + '" onclick="commonPoolCheckBox(' + divid + ')"></td>'
                            html += '<td><p class="visible-xs visible-sm table-row-title">Application No.</p><p><a href="#" onclick="javascript:doInspectionCommonPoolAssign(' + "'" + res.rows[i].maskId + "'" + ');">' + res.rows[i].applicationNo + '</a></p></td>' +
                                '<td><p class="visible-xs visible-sm table-row-title">Service</p><p>' + res.rows[i].serviceName + '<p></td>' +
                                '<td><p class="visible-xs visible-sm table-row-title">Licence Expiry Date</p><p>' + res.rows[i].licenceExpiryDateStr + '<p></td>' +
                                '<td><p class="visible-xs visible-sm table-row-title">Application Status</p><p>Pending Task Assignment</p></td>' +
                                '<td><p class="visible-xs visible-sm table-row-title">HCI Code</p><p>' + res.rows[i].hciCode + '</p></td>' +
                                '<td><p class="visible-xs visible-sm table-row-title">HCI Name / Address</p><p>' + res.rows[i].hciAddress + '</p></td>' +
                                '</tr>';
                        }
                        html += '</tbody></table></div></td></tr>';
                        $("#advfilter" + divid).after(html);
                    }
                }
            )
        } else {
            var display = $('#advfilterson' + divid).css('display');
            if (display == 'none') {
                $('#advfilterson' + divid).show();
            } else {
                $('#advfilterson' + divid).hide();
            }
        }
    }

    function commonPoolAllCheckBox(id) {
        if ($('#checkbox' + id).prop('checked')) {
            $('#advfilterson' + id + ' input[type="checkbox"]').prop("checked", true)
        } else {
            $('#advfilterson' + id + ' input[type="checkbox"]').prop("checked", false)
        }
    }

    function commonPoolCheckBox(id) {
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
</script>

