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

    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span>Search Criteria</span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <iais:section title="" id = "supPoolList">
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
                        <iais:field value="Application No."/>
                        <iais:value width="18">
                          <input type="text" name="application_no" value="${supTaskSearchParam.filters['application_no']}" />
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Application Type"/>
                        <iais:value width="18">
                          <iais:select name="application_type" options="appTypeOption" firstOption="Please select" value="${supTaskSearchParam.filters['application_type']}" ></iais:select>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Application Status"/>
                        <iais:value width="18">
                          <iais:select name="application_status" options="appStatusOption" firstOption="Please select" value="${supTaskSearchParam.filters['application_status']}" ></iais:select>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="${groupRoleFieldDto.groupMemBerName} Name"/>
                        <iais:value width="18">
                          <iais:select name="memberName" options="${groupRoleFieldDto.memberOption}" firstOption="Please select" value="${groupRoleFieldDto.checkUser}" ></iais:select>
                        </iais:value>
                      </iais:row>
                      <iais:action style="text-align:center;">
                        <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspectorSearchTaskSearch()">Search</button>
                        <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspectorSearchTaskClear()">Clear</button>
                      </iais:action>
                    </iais:section>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <iais:pagination  param="supTaskSearchParam" result="supTaskSearchResult"/>
      <div class="container">
        <div class="col-xs-12">
          <div class="components">
            <h3>
              <span>Search Result</span>
            </h3>
            <div class="table-gp">
              <table class="table">
                <thead>
                <tr align="center">
                  <iais:sortableHeader needSort="false" field = "" value="S/N"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field = "GROUP_NO" value="Application No."></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field = "APP_TYPE" value="Application Type"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field = "Status" value="Application Status"></iais:sortableHeader>
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
                      <tr>
                        <td class="row_no"><c:out value="${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"/></td>
                        <td><c:out value="${superPool.appGroupNo}"/></td>
                        <td><iais:code code="${superPool.applicationType}"/></td>
                        <td><iais:code code="${superPool.applicationStatus}"/></td>
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
          </div>
        </div>
      </div>
    </iais:body>
  </form>
</div>
<script type="text/javascript">
    function inspectorSearchTask_optionNameAuto(value){
      if(value != null && value != null){
          $("#memberName").val(value);
      }
        doInspectorSearchTaskSelect();
    }

    function doInspectorSearchTaskSelect(){
        var options=$("#inspectorSearchTask_inspectorName option:selected");
        $("#inspector_name").val(options);
    }

    function doInspectorSearchTaskAssign(taskId) {
        $("#taskId").val(taskId);
        inspectorSearchTaskSubmit('assign');
    }

    function doInspectorSearchTaskClear() {
        $('input[name="application_no"]').val("");
        $("#application_type option[text = 'Please select']").attr("selected", "selected");
        $("#application_status option:first").prop("selected", 'selected');
        $("#inspector_name option:first").prop("selected", 'selected');
        $(".current").text("Please select");
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
</script>
