<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot= IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainSupForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="InspectionSupSearchSwitchType" value="">
    <input type="hidden" id="taskId" name="taskId" value="">
    <input type="hidden" id="inspector_name" name="inspector_name" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Supervisor Assignment Pool</span>
                </h2>
              </div>
              <iais:body >
                <iais:section title="" id = "supPoolList">
                  <div id = "superPoolSearch">
                    <iais:row>
                      <iais:field value="Application No."/>
                      <iais:value width="18">
                        <input type="text" name="application_no" value="${supTaskSearchParam.filters['application_no']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Application Type"/>
                      <iais:value width="18">
                        <iais:select name="application_type" options="appTypeOption" firstOption="Please Select" value="${supTaskSearchParam.filters['application_type']}" ></iais:select>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Application Status"/>
                      <iais:value width="18">
                        <iais:select name="application_status" options="appStatusOption" firstOption="Please Select" value="${supTaskSearchParam.filters['application_status']}" ></iais:select>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="HCI Code"/>
                      <iais:value width="18">
                        <input type="text" name="hci_code" value="${supTaskSearchParam.filters['hci_code']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="HCI Name"/>
                      <iais:value width="18">
                        <input type="text" name="hci_name" value="${supTaskSearchParam.filters['hci_name']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="HCI Address"/>
                      <iais:value width="18">
                        <input type="text" name="hci_address" value="${supTaskSearchParam.filters['hci_address']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="${groupRoleFieldDto.groupMemBerName} Name"/>
                      <iais:value width="18">
                        <iais:select name="memberName" options="memberOption" firstOption="Please Select" value="${groupRoleFieldDto.checkUser}" ></iais:select>
                      </iais:value>
                    </iais:row>
                  </div>
                  <iais:action style="text-align:right;">
                    <button class="btn btn-secondary" type="button" onclick="javascript:doInspectorSearchTaskClear()">Clear</button>
                    <button class="btn btn-primary" type="button" onclick="javascript:doInspectorSearchTaskSearch()">Search</button>
                  </iais:action>
                </iais:section>
                <iais:pagination  param="supTaskSearchParam" result="supTaskSearchResult"/>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr align="center">
                      <iais:sortableHeader needSort="false" field = "" value="S/N"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field = "GROUP_NO" value="Application No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field = "APP_TYPE" value="Application Type"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field = "" value="Inspection Lead"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty supTaskSearchResult.rows}">
                        <tr>
                          <td colspan="12">
                            <iais:message key="ACK018" escape="true"></iais:message>
                            <!--No Record!!-->
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="superPool" items="${supTaskSearchResult.rows}" varStatus="status">
                          <tr style = "display: table-row;" id = "advfilter${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}">
                            <td class="row_no"><c:out value="${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"/></td>
                            <td>
                              <p>
                                <c:out value="${superPool.appGroupNo}"/>
                                <a class="accordion-toggle  collapsed"
                                   data-toggle="collapse" aria-expanded="false"
                                   data-target="#advfilter${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"
                                   onclick="javascript:supervisorByGroupId('<iais:mask name="appGroupId" value="${superPool.id}"/>','${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}')">
                                </a>
                              </p>
                            </td>
                            <td><iais:code code="${superPool.applicationType}"/></td>
                            <td>
                              <c:forEach var="lead" items="${superPool.groupLead}">
                                <c:out value="${lead}"/><br>
                              </c:forEach>
                            </td>
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

  function doInspectorSearchTaskAssign(taskId) {
    $("#taskId").val(taskId);
    inspectorSearchTaskSubmit('assign');
  }

  function doInspectorSearchTaskClear() {
    $('input[name="application_no"]').val("");
    $('#application_type option:first').prop('selected', 'selected');
    $('#application_status option:first').prop('selected', 'selected');
    $('#memberName option:first').prop('selected', 'selected');
    $("#superPoolSearch .current").text("Please Select");
    $('input[name="hci_code"]').val("");
    $('input[name="hci_name"]').val("");
    $('input[name="hci_address"]').val("");
  }

  function inspectorSearchTaskSubmit(action){
    $("[name='InspectionSupSearchSwitchType']").val(action);
    var mainPoolForm = document.getElementById('mainSupForm');
    mainPoolForm.submit();
  }

  function doInspectorSearchTaskSearch() {
    inspectorSearchTaskSubmit('search');
  }

  function jumpToPagechangePage(){
    inspectorSearchTaskSubmit('page');
  }

  function sortRecords(sortFieldName,sortType){
    $("[name='crud_action_value']").val(sortFieldName);
    $("[name='crud_action_additional']").val(sortType);
    inspectorSearchTaskSubmit('sort');
  }

  var superPool = new Array();
  function supervisorByGroupId(appGroupId, divid) {
    var superFlag = true;
    for(var i = 0; i < superPool.length; i++){
      if(superPool[i] == divid){
        superFlag = false;
      }
    }
    if (superFlag) {
      superPool.push(divid);
      $.post(
              '/hcsa-licence-web/common-pool/supervisor.do',
              {groupId: appGroupId},
              function (data) {
                var result = data.result;
                var memberName = data.memberName;
                if('Success' == result){
                  var res = data.ajaxResult;
                  var html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                          '<td colspan="6" style="padding: 0px 8px !important;">' +
                          '<div class="accordian-body p-3 collapse in" id="row1" aria-expanded="true" style="">' +
                          '<table class="table" style="background-color: #F3F3F3;margin-bottom:0px;" >' +
                          '<thead>' +
                          '<tr>';

                  html += '<th>Application No</th>' +
                          '<th>Application Status</th>' +
                          '<th>HCI Code</th>' +
                          '<th>HCI Name / Address</th>' +
                          '<th>Licence Expiry Date</th>' +
                          '<th>Inspection Date</th>' +
                          '<th>' + memberName + '</th>' +
                          '</tr>' +
                          '</thead>' +
                          '<tbody>';
                  for (var i = 0; i < res.rowCount; i++) {
                    html += '<tr>';
                    html += '<td><p class="visible-xs visible-sm table-row-title">Application No</p><p><a onclick="javascript:doInspectorSearchTaskAssign(' + "'" + res.rows[i].maskId + "'" + ');">' + res.rows[i].appNo + '</a></p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Application Status</p><p>' + res.rows[i].appStatus + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">HCI Code</p><p>' + res.rows[i].hciCode + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">HCI Name / Address</p><p>' + res.rows[i].hciAddress + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Licence Expiry Date</p><p>' + res.rows[i].licenceExpiryDateStr + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Inspection Date</p><p>' + res.rows[i].inspectionDateStr + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">' + memberName + '</p><p>' + res.rows[i].memberName + '</p></td>' +
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
