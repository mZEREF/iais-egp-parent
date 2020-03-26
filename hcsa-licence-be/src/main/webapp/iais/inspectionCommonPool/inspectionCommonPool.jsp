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
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">


<form method="post" id="mainPoolForm" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
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
            <iais:body >
              <iais:section title="" id = "demoList">
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
                        <iais:select name="application_type" options="appTypeOption" firstOption="Please Select" value="${cPoolSearchParam.filters['application_type']}" ></iais:select>
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
                  <iais:row>
                    <iais:field value="Submission Date"/>
                    <iais:value width="18">
                      <iais:datePicker id = "sub_date" name = "sub_date" value="${sub_date2}"></iais:datePicker>
                    </iais:value>
                  </iais:row>
                  <iais:action style="text-align:right;">
                    <button class="btn btn-secondary" type="button" onclick="javascript:doInspectionCommonPoolClear()">Clear</button>
                    <button class="btn btn-primary" type="button" onclick="javascript:doInspectionCommonPoolSearch()">Search</button>
                  </iais:action>
                </iais:section>
              <iais:pagination  param="cPoolSearchParam" result="cPoolSearchResult"/>
              <div class="table-gp">
                <table class="table">
                  <thead>
                    <tr align="center">
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
                            <iais:message key="ACK018" escape="true"></iais:message>
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
                                <a class="accordion-toggle  collapsed"
                                   data-toggle="collapse" aria-expanded="false"
                                   data-target="#advfilter${(status.index + 1) + (cPoolSearchParam.pageNo - 1) * cPoolSearchParam.pageSize}"
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

    function doInspectionCommonPoolAssign(appCorrelationId) {
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
    function inspectionCommonPoolSubmit(action){
        $("[name='inspectionPoolType']").val(action);
        var mainPoolForm = document.getElementById('mainPoolForm');
        mainPoolForm.submit();
    }
    function doInspectionCommonPoolSearch() {
        inspectionCommonPoolSubmit('search');
    }
    function jumpToPagechangePage(){
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
                  '<div class="accordian-body p-3 collapse in" id="row1" aria-expanded="true" style="">' +
                  '<table class="table" style="background-color: #F3F3F3;margin-bottom:0px;" >' +
                  '<thead>' +
                  '<tr>';

              html += '<th>Application No</th>' +
                  '<th>Service</th>' +
                  '<th>Application Status</th>' +
                  '<th>HCI Code</th>' +
                  '<th>HCI Address</th>' +
                  '</tr>' +
                  '</thead>' +
                  '<tbody>';
              for (var i = 0; i < res.rowCount; i++) {
                html += '<tr>';
                  html += '<td><p class="visible-xs visible-sm table-row-title">Application No</p><p><a onclick="javascript:doInspectionCommonPoolAssign(' + "'" + res.rows[i].maskId + "'" + ');">' + res.rows[i].applicationNo + '</a></p></td>' +
                      '<td><p class="visible-xs visible-sm table-row-title">Service</p><p>' + res.rows[i].serviceName + '<p></td>' +
                      '<td><p class="visible-xs visible-sm table-row-title">Application Status</p><p>' + res.rows[i].appStatus + '</p></td>' +
                      '<td><p class="visible-xs visible-sm table-row-title">HCI Code</p><p>' + res.rows[i].hciCode + '</p></td>' +
                      '<td><p class="visible-xs visible-sm table-row-title">HCI Name / Address</p><p>' + res.rows[i].hciAddress + '</p></td>' +
                      '</tr>';
              }
              html += '</tbody></table></div></td></tr>';
              $("#advfilter" + divid).after(html);
              dividlist.push(divid);
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
</script>

